/* 
 * $Id: EventReader.java,v 1.0 2011-10-28 12:29:27 averab Exp $
 *
 * @copyright Universidad Carlos III de Madrid. proprietary/confidential. Use is subject to license terms.
 */
package es.uc3m.softlab.cbi4api.gbas.event.subscriber;

import es.uc3m.softlab.cbi4api.gbas.event.subscriber.xsd.basu.event.Event;

import org.apache.camel.Exchange;

/**
 * Component interface for retrieving the incoming events in GBAS-BPAF format
 * from the message queue. 
 * This interface defines all methods for accessing to the <strong>event</strong> 
 * entity data based upon the Spring framework.
 * 
 * @author averab
 * @version 1.0.0
 */
public interface EventReader {
 	/** Spring component name */
    public static final String COMPONENT_NAME = StaticResources.COMPONENT_NAME_EVENT_READER;        

	/**
	 * Extract the events in f4bpa-bpaf extension format from messages retrieved from 
	 * the jms queue.
	 * 
	 * @param exchange payload exchange message containing events in gbas-bpaf format.
	 * @return gbas-bpaf event extracted from the message.
	 * @throws EventReaderException if any error occurred during event information retrieval.
	 */
	public Event extractEvent(Exchange exchange) throws EventReaderException;
}