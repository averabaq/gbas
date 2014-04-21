/* 
 * $Id: EventQueuer.java,v 1.0 2014-10-28 12:29:27 averab Exp $
 *
 * @copyright Universidad Carlos III de Madrid. proprietary/confidential. Use is subject to license terms.
 */
package es.uc3m.softlab.cbi4api.gbas.event.subscriber;

import es.uc3m.softlab.cbi4api.gbas.event.store.domain.ActivityInstance;
import es.uc3m.softlab.cbi4api.gbas.event.store.domain.Event;
import es.uc3m.softlab.cbi4api.gbas.event.store.facade.EventException;

/**
 * Component interface for queuing those events whose parents have not arrived yet.
 * This interface defines all methods for queuing <strong>event</strong>
 * entity data throughout a BPAF model extension and based upon the Spring framework.
 * 
 * @author averab
 */
public interface EventQueuer {
 	/** Spring component name */
    public static final String COMPONENT_NAME = StaticResources.COMPONENT_NAME_EVENT_QUEUER;

    /**
     * Add the activity instance to quarantine state
     * @param instance activity instance to set in quarantine state
     */
    public void addQuarantine(ActivityInstance instance);
    /**
     * Removes the activity instance from the quarantine state.
     * @param instance activity instance for being removed from quarantine state.
     */
    public void removeQuarantine(ActivityInstance instance);
    /**
     * Returns whether the activity instance is in quarantine state or not.
     * @param instance activity instance to get the quarantine state from.
     * @return whether the activity instance is in quarantine state or not.
     */
    public boolean isInQuarantine(ActivityInstance instance);
    /**
     * Add the event to quarantine state
     * @param event event to set in quarantine state
     */
    public void addQuarantine(Event event);
    /**
     * Process the events in quarantine for the associated event.
     * @param event event to get the subsequent events in quarantine.
     * @throws es.uc3m.softlab.cbi4api.gbas.event.store.facade.EventException if any illegal data access or inconsistent event data error occurred.
     */
    public void processEventsInQuarantine(Event event) throws EventException;
}