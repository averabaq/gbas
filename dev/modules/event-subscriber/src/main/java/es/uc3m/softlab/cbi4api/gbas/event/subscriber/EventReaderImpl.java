/* 
 * $Id: EventReaderImpl.java,v 1.0 2013-01-27 23:29:05 averab Exp $
 *
 * @copyright Universidad Carlos III de Madrid. proprietary/confidential. Use is subject to license terms.
 */
package es.uc3m.softlab.cbi4api.gbas.event.subscriber;

import es.uc3m.softlab.cbi4api.gbas.event.subscriber.xsd.basu.event.Event;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.apache.camel.Exchange;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.xml.sax.SAXException;

/**
 * Component implementation for retrieving the incoming events in GBAS-BPAF format
 * from the message queue. 
 * This interface defines all methods for accessing to the <strong>event</strong> 
 * entity data based upon the Spring framework.
 * 
 * @author averab
 * @version 1.0.0
 */
@Component(value=EventReader.COMPONENT_NAME)
public class EventReaderImpl implements EventReader {
	/** Logger for tracing */
	private Logger logger = Logger.getLogger(EventReaderImpl.class);
	
	/**
	 * Extract the events in f4bpa-bpaf extension format from messages retrieved from 
	 * the jms queue.
	 * 
	 * @param exchange payload exchange message containing events in gbas-bpaf format.
	 * @return gbas-bpaf event extracted from the message.
	 * @throws EventReaderException if any error occurred during event information retrieval.
	 */
	@Override
	public Event extractEvent(Exchange exchange) throws EventReaderException {		
		logger.debug("Extracing incoming event message with ID " + exchange.getIn().getMessageId());
		byte[] xml = (byte[])exchange.getIn().getBody();						
		logger.debug(new String(xml));
		
		Event event = null;
		ByteArrayInputStream bios = new ByteArrayInputStream(xml);		
		try {
			// create a JAXBContext capable of handling classes  
			JAXBContext jc = JAXBContext.newInstance("es.uc3m.softlab.cbi4api.gbas.event.subscriber.xsd.basu.event");
			// create an Unmarshaller 
			Unmarshaller u = jc.createUnmarshaller();        			
			// Performs an xml validation against the GBAS-BPAF schema
			SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
			Schema schema = factory.newSchema(getClass().getResource(StaticResources.BASU_EVENT_BPAF_XML_SCHEMA_CLASSPATH_FILE));
			u.setSchema(schema);			
			// unmarshal an instance document into a tree of Java content objects. 			
			event = (Event) u.unmarshal(bios);		
		} catch (JAXBException jaxb) {
			logger.error(jaxb.fillInStackTrace());
			throw new EventReaderException(jaxb);
		} catch(SAXException saxex) {
			logger.error(saxex.fillInStackTrace());
			throw new EventReaderException(saxex);
		} finally {
			try {
				bios.close();
			} catch (IOException ioex) {
				logger.error(ioex.fillInStackTrace());
			}
		}
		return event;
	}
}