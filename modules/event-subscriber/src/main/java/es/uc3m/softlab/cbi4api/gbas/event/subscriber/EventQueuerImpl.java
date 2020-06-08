/* 
 * $Id: EventQueuerImpl.java,v 1.0 2013-01-27 23:29:05 averab Exp $
 *
 * @copyright Universidad Carlos III de Madrid. proprietary/confidential. Use is subject to license terms.
 */
package es.uc3m.softlab.cbi4api.gbas.event.subscriber;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import es.uc3m.softlab.cbi4api.gbas.event.store.domain.ActivityInstance;
import es.uc3m.softlab.cbi4api.gbas.event.store.domain.Event;
import es.uc3m.softlab.cbi4api.gbas.event.store.domain.Model;
import es.uc3m.softlab.cbi4api.gbas.event.store.domain.ModelType;
import es.uc3m.softlab.cbi4api.gbas.event.store.domain.Source;
import es.uc3m.softlab.cbi4api.gbas.event.store.facade.ActivityInstanceException;
import es.uc3m.softlab.cbi4api.gbas.event.store.facade.ActivityInstanceFacade;
import es.uc3m.softlab.cbi4api.gbas.event.store.facade.EventException;
import es.uc3m.softlab.cbi4api.gbas.event.store.facade.EventFacade;
import es.uc3m.softlab.cbi4api.gbas.event.store.facade.ModelException;
import es.uc3m.softlab.cbi4api.gbas.event.store.facade.ModelFacade;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;

/**
 * Component implementation for queuing those events whose parents have not arrived yet.
 * This interface defines all methods for queuing <strong>event</strong>
 * entity data throughout a BPAF model extension and based upon the Spring framework.
 * 
 * @author averab
 */
@Component(value=EventQueuer.COMPONENT_NAME)
@Scope(value= BeanDefinition.SCOPE_SINGLETON)
public class EventQueuerImpl implements EventQueuer {
	/** Logger for tracing */
	private Logger logger = Logger.getLogger(EventQueuerImpl.class);
    /** Event service facade */
    @Autowired private EventFacade eventFacade;
    /** Model facade */
    @Autowired private ModelFacade modelFacade;
    /** Activity instance facade */
    @Autowired private ActivityInstanceFacade activityInstanceFacade;
    /** Activity instances cache */
    private Map<ActivityInstanceUK, ActivityInstance> activityCache;
    /** Activity instances cache */
    private Map<ActivityInstanceUK, List<Event>> quarantine;

    @PostConstruct
    public void setup() {
        activityCache = Maps.newHashMap();
        quarantine = Maps.newHashMap();
    }
    /**
     * Add the activity instance to quarantine state
     * @param instance activity instance to set in quarantine state
     */
    @Override
    public void addQuarantine(ActivityInstance instance) {
        ActivityInstanceUK uk = getUK(instance.getInstanceSrcId(), instance.getModel().getModelSrcId());
        if (!activityCache.containsKey(uk)) {
            activityCache.put(uk, instance);
        }
    }
    /**
     * Removes the activity instance from the quarantine state.
     * @param instance activity instance for being removed from quarantine state.
     */
    @Override
    public void removeQuarantine(ActivityInstance instance) {
        ActivityInstanceUK uk = getUK(instance.getInstanceSrcId(), instance.getModel().getModelSrcId());
        if (activityCache.containsKey(uk)) {
            activityCache.remove(uk);
        }
    }
    /**
     * Returns whether the activity instance is in quarantine state or not.
     * @param instance activity instance to get the quarantine state from.
     * @return whether the activity instance is in quarantine state or not.
     */
    @Override
    public boolean isInQuarantine(ActivityInstance instance) {
        ActivityInstanceUK uk = getUK(instance.getInstanceSrcId(), instance.getModel().getModelSrcId());
        return activityCache.containsKey(uk);
    }
    /**
     * Add the event to quarantine state
     * @param event event to set in quarantine state
     */
    @Override
    public void addQuarantine(Event event) {
        String instanceId = event.getActivityInstance().getInstanceSrcId();
        String modelId = event.getActivityInstance().getModel().getModelSrcId();
        ActivityInstanceUK uk = getUK(instanceId, modelId);
        ActivityInstance instance = activityCache.get(uk);
        uk = getUK(instance.getParent().getInstanceSrcId(), instance.getParent().getModel().getModelSrcId());
        if (!quarantine.containsKey(uk)) {
            List<Event> events = Lists.newLinkedList();
            quarantine.put(uk, events);
        }
        quarantine.get(uk).add(event);
    }
    /**
     * Removes a complete activity instance from the quarantine state.
     * @param instance activity instance for being removed from quarantine state.
     */
    private void removeQuarantineInFull(ActivityInstance instance) {
        String instanceId = instance.getInstanceSrcId();
        String modelId = instance.getModel().getModelSrcId();
        ActivityInstanceUK uk = getUK(instanceId, modelId);
        if (quarantine.containsKey(uk)) {
            List<Event> events = quarantine.get(uk);
            for (Event evt : events)
                removeQuarantine(evt.getActivityInstance());
            quarantine.remove(uk);
        }
    }
    /**
     * Gets the event that are in quarantine for the given activity
     * @param instance activity instance to get the quarantine events from
     * @return event that are in quarantine
     */
    private List<Event> getEventsInQuarantine(ActivityInstance instance) {
        ActivityInstanceUK uk = getUK(instance.getInstanceSrcId(), instance.getModel().getModelSrcId());
        return quarantine.get(uk);
    }
    /**
     * Process the events in quarantine for the associated event.
     * @param event event to get the subsequent events in quarantine.
     */
    @Override
    public void processEventsInQuarantine(Event event) {
        List<Event> events = getEventsInQuarantine(event.getActivityInstance());
        if (events != null) {
            logger.info(String.format("Processing events in quarantine for activity [%s]", event.getActivityInstance()));
            for (Event evt : events) {
                try {
                    String parentInstanceId = event.getActivityInstance().getInstanceSrcId();
                    Source source = event.getActivityInstance().getModel().getSource();
                    Model activityModel = modelFacade.getModel(evt.getActivityInstance().getModel().getModelSrcId(), source, ModelType.ACTIVITY);
                    ActivityInstance parent = activityInstanceFacade.getActivityInstance(parentInstanceId, activityModel);
                    evt.getActivityInstance().setParent(parent);
                    logger.debug("Saving the event " + event + " in quarantine...");
                    eventFacade.storeEvent(event);
                    logger.info("Event " + event + " in quarantine stored successfully.");
                } catch (ModelException mex) {
                    logger.error(mex.fillInStackTrace());
                } catch (ActivityInstanceException aiex) {
                    logger.error(aiex.fillInStackTrace());
                } catch (EventException evtex) {
                    logger.error(evtex.fillInStackTrace());
                }
            }
            // remove from quarantine after being processed
            removeQuarantineInFull(event.getActivityInstance());
        }
    }
    /**
     * Gets the unique key from the activity instance.
     * @param instanceId activity instance identifier.
     * @param modelId activity model id to get the unique key from.
     * @return activity instance unique key.
     */
    private ActivityInstanceUK getUK(String instanceId, String modelId) {
        ActivityInstanceUK uk = new ActivityInstanceUK();
        uk.setInstanceSrcId(instanceId);
        uk.setModelSrcId(modelId);
        return uk;
    }

    protected class ActivityInstanceUK {
        private String instanceSrcId;
        private String modelSrcId;
        /**
         * Gets the {@link #instanceSrcId} property.
         * @return the {@link #instanceSrcId} property.
         */
        protected String getInstanceSrcId() {
            return instanceSrcId;
        }
        /**
         * Sets the {@link #instanceSrcId} property.
         * @param instanceSrcId the {@link #instanceSrcId} property to set.
         */
        protected void setInstanceSrcId(String instanceSrcId) {
            this.instanceSrcId = instanceSrcId;
        }
        /**
         * Gets the {@link #modelSrcId} property.
         * @return the {@link #modelSrcId} property.
         */
        protected String getModelSrcId() {
            return modelSrcId;
        }
        /**
         * Sets the {@link #modelSrcId} property.
         * @param modelSrcId the {@link #modelSrcId} property to set.
         */
        protected void setModelSrcId(String modelSrcId) {
            this.modelSrcId = modelSrcId;
        }
        /**
         * Indicates whether some other object is "equal to" this one.
         * @param obj the reference object with which to compare.
         * @return true if this object is the same as the obj argument; false otherwise.
         * @see java.lang.Object#equals(java.lang.Object)
         */
        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;

            ActivityInstanceUK that = (ActivityInstanceUK) obj;

            if (!instanceSrcId.equals(that.instanceSrcId)) return false;
            if (!modelSrcId.equals(that.modelSrcId)) return false;

            return true;
        }
        /**
         * Returns a hash code value for the object.
         * @return a hash code value for this object.
         * @see java.lang.Object#hashCode()
         */
        @Override
        public int hashCode() {
            int result = instanceSrcId.hashCode();
            result = 31 * result + modelSrcId.hashCode();
            return result;
        }
    }
}