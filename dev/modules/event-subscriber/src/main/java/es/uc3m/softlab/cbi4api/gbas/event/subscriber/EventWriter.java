/* 
 * $Id: EventWriter.java,v 1.0 2011-10-28 12:29:27 averab Exp $
 *
 * @copyright Universidad Carlos III de Madrid. proprietary/confidential. Use is subject to license terms.
 */
package es.uc3m.softlab.cbi4api.gbas.event.subscriber;

import org.apache.camel.Exchange;

import es.uc3m.softlab.cbi4api.gbas.event.store.domain.Event;
import es.uc3m.softlab.cbi4api.gbas.event.store.facade.EventException;

/**
 * Component interface for writing events to the global data store. 
 * This interface defines all methods for storing <strong>event</strong> 
 * entity data throughout a BPAF model extension and based upon the Spring framework.
 * 
 * @author averab
 * @version 1.0.0
 */
public interface EventWriter {
 	/** Spring component name */
    public static final String COMPONENT_NAME = StaticResources.COMPONENT_NAME_EVENT_WRITER;

    /**
     * Stores the event in the data source and sets the processed event back to be forwarded to the
     * output the channel in xml format.  
     * @param exchange exchange data object to route. 
     * @param bpafEvent BPAF event {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.Event} to store
     * @param gbasEvent GBAS event {@link es.uc3m.softlab.cbi4api.gbas.event.subscriber.xsd.basu.event.Event}
     *  object to forward in xml format to the GBAS output channel.
     * @throws EventException if any illegal data access or inconsistent event data error occurred.
     */
    public void loadEvent(Exchange exchange, Event bpafEvent, es.uc3m.softlab.cbi4api.gbas.event.subscriber.xsd.basu.event.Event gbasEvent) throws EventException;
}