/* 
 * $Id: EventWriterImpl.java,v 1.0 2013-01-27 23:29:05 averab Exp $
 *
 * @copyright Universidad Carlos III de Madrid. proprietary/confidential. Use is subject to license terms.
 */
package es.uc3m.softlab.cbi4api.gbas.event.subscriber;

import es.uc3m.softlab.cbi4api.gbas.event.store.domain.ActivityInstance;
import es.uc3m.softlab.cbi4api.gbas.event.store.domain.Event;
import es.uc3m.softlab.cbi4api.gbas.event.store.domain.Model;
import es.uc3m.softlab.cbi4api.gbas.event.store.domain.Source;
import es.uc3m.softlab.cbi4api.gbas.event.store.facade.ActivityInstanceException;
import es.uc3m.softlab.cbi4api.gbas.event.store.facade.ActivityInstanceFacade;
import es.uc3m.softlab.cbi4api.gbas.event.store.facade.EventException;
import es.uc3m.softlab.cbi4api.gbas.event.store.facade.EventFacade;

import org.apache.camel.Exchange;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Component implementation for writing events to the global data store. 
 * This interface defines all methods for storing <strong>event</strong> 
 * entity data throughout a BPAF model extension and based upon the Spring framework.
 * 
 * @author averab
 * @version 1.0.0
 */
@Component(value=EventWriter.COMPONENT_NAME)
public class EventWriterImpl implements EventWriter {
	/** Logger for tracing */
	private Logger logger = Logger.getLogger(EventWriterImpl.class);
    /** Configuration object */
    @Autowired private Config config;
	/** Event session facade */
    @Autowired private EventFacade eventFacade;
    /** Event queue for handling events in quarantine state */
    @Autowired private EventQueuer eventQueuer;
	
    /**
     * Stores the event in the data source and sets the processed event back to be forwarded to the
     * output the channel in xml format.  
     * @param exchange exchange data object to route. 
     * @param bpafEvent BPAF event {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.Event} to store
     * @param gbasEvent GBAS event {@link es.uc3m.softlab.cbi4api.gbas.event.subscriber.xsd.basu.event.Event}
	 *  object to forward in xml format to the GBAS output channel.
     * @throws EventException if any illegal data access or inconsistent event data error occurred.
     */
    @Override
    public void loadEvent(Exchange exchange, Event bpafEvent, es.uc3m.softlab.cbi4api.gbas.event.subscriber.xsd.basu.event.Event gbasEvent) throws EventException {
    	// store the BPAF event in the data source
    	storeEvent(bpafEvent);
    }
	/**
	 * Saves and synchronizes the {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.Event}
	 * entity object state with the data source.
	 * 
	 * @param event {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.Event}
	 * entity object to save.
	 * @throws EventException if any illegal data access or inconsistent event data error occurred.
	 */
	private void storeEvent(Event event) throws EventException {
        // process the event if, and only if the event is not in quarantine
        if (!eventQueuer.isInQuarantine(event.getActivityInstance())) {
            logger.debug("Saving the event " + event + "...");
            eventFacade.storeEvent(event);
            logger.info("Event " + event + " stored successfully.");
            eventQueuer.processEventsInQuarantine(event);
        }
	}
}