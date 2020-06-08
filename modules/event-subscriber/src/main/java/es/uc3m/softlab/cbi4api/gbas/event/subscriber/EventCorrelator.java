/* 
 * $Id: EventCorrelator.java,v 1.0 2011-10-28 12:29:27 averab Exp $
 *
 * @copyright Universidad Carlos III de Madrid. proprietary/confidential. Use is subject to license terms.
 */
package es.uc3m.softlab.cbi4api.gbas.event.subscriber;

import es.uc3m.softlab.cbi4api.gbas.event.store.domain.ActivityInstance;
import es.uc3m.softlab.cbi4api.gbas.event.store.domain.ProcessInstance;
import es.uc3m.softlab.cbi4api.gbas.event.store.domain.Source;
import es.uc3m.softlab.cbi4api.gbas.event.store.facade.ActivityInstanceException;
import es.uc3m.softlab.cbi4api.gbas.event.store.facade.EventException;
import es.uc3m.softlab.cbi4api.gbas.event.store.facade.ModelException;
import es.uc3m.softlab.cbi4api.gbas.event.store.facade.ProcessInstanceException;
import es.uc3m.softlab.cbi4api.gbas.event.subscriber.xsd.basu.event.Event;

/**
 * Component interface for correlating events before being stored into 
 * the global data store. 
 * This interface defines all methods for correlating <strong>event</strong> 
 * entity data throughout a BPAF model extension and based upon the Spring framework.
 * 
 * @author averab
 * @version 1.0.0
 */
public interface EventCorrelator {
 	/** Spring component name */
    public static final String COMPONENT_NAME = StaticResources.COMPONENT_NAME_EVENT_CORRELATOR;

    /**
     * Correlates the incoming {@link es.uc3m.softlab.cbi4api.gbas.event.subscriber.xsd.basu.event.Event}
     * by obtaining an existing process instance or creating a new one if necessary.
     * @param event {@link es.uc3m.softlab.cbi4api.gbas.event.subscriber.xsd.basu.event.Event} to get the associated
     * process instance if it exists, otherwise it creates a new one.
     * @param source {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.Source} associated to the incoming
     * {@link es.uc3m.softlab.cbi4api.gbas.event.subscriber.xsd.basu.event.Event}.
     * @return exact {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.ProcessInstance} associated to properly
     * correlated the incoming {@link es.uc3m.softlab.cbi4api.gbas.event.subscriber.xsd.basu.event.Event}.
     * @throws ProcessInstanceException if any process instance exception occurred during processing.
     * @throws ModelException if any model exception occurred during processing.
     * @throws EventException if any event exception occurred during processing.
     */
    public ProcessInstance correlateProcess(Event event, Source source) throws ModelException, ProcessInstanceException, EventException;
    /**
     * Correlates the incoming {@link es.uc3m.softlab.cbi4api.gbas.event.subscriber.xsd.basu.event.Event}
     * by obtaining an existing process instance or creating a new one if necessary.
     * @param event {@link es.uc3m.softlab.cbi4api.gbas.event.subscriber.xsd.basu.event.Event} to get the associated
     * process instance if it exists, otherwise it creates a new one.
     * @param source {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.Source} associated to the
     * {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.Model} of the incoming
     * {@link es.uc3m.softlab.cbi4api.gbas.event.subscriber.xsd.basu.event.Event}.
     * @return right {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.ActivityInstance} associated to properly
     * correlated the incoming {@link es.uc3m.softlab.cbi4api.gbas.event.subscriber.xsd.basu.event.Event}.
     * @throws ActivityInstanceException if any activity instance exception occurred during processing.
     * @throws ModelException if any model exception occurred during processing.
     * @throws EventException if any event exception occurred during processing.
     */
    public ActivityInstance correlateActivity(Event event, Source source) throws ModelException, ActivityInstanceException, EventException;
}