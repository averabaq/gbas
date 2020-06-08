/* 
 * $Id: EventSubscriberTest.java,v 1.0 2013-01-27 23:29:05 averab Exp $
 *
 * @copyright Universidad Carlos III de Madrid. proprietary/confidential. Use is subject to license terms.
 */
package es.uc3m.softlab.cbi4api.gbas.event.subscriber.test;

import es.uc3m.softlab.cbi4api.gbas.event.store.facade.EventException;
import es.uc3m.softlab.cbi4api.gbas.event.subscriber.ETLProcessor;
import es.uc3m.softlab.cbi4api.gbas.event.subscriber.EventConverter;
import es.uc3m.softlab.cbi4api.gbas.event.subscriber.EventReader;
import es.uc3m.softlab.cbi4api.gbas.event.subscriber.EventWriter;
import es.uc3m.softlab.cbi4api.gbas.event.subscriber.StaticResources;
import es.uc3m.softlab.cbi4api.gbas.event.subscriber.xsd.basu.event.Correlation;
import es.uc3m.softlab.cbi4api.gbas.event.subscriber.xsd.basu.event.CorrelationData;
import es.uc3m.softlab.cbi4api.gbas.event.subscriber.xsd.basu.event.CorrelationElement;
import es.uc3m.softlab.cbi4api.gbas.event.subscriber.xsd.basu.event.Event;
import es.uc3m.softlab.cbi4api.gbas.event.subscriber.xsd.basu.event.Event.EventDetails;
import es.uc3m.softlab.cbi4api.gbas.event.subscriber.xsd.basu.event.State;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import org.apache.camel.CamelContext;
import org.apache.camel.Endpoint;
import org.apache.camel.EndpointInject;
import org.apache.camel.Exchange;
import org.apache.camel.PollingConsumer;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.impl.DefaultExchange;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext-test.xml"})
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
@Transactional(propagation=Propagation.REQUIRES_NEW)
public class EventSubscriberTest extends AbstractShowcaseTest  {
	/** Configuration object */
	@Autowired private CamelContext context;
	/** ETL (Extract, Transform & Load) processor */
	@Autowired private ETLProcessor etl;
	/** ETL XES Extractor */
	@Autowired private EventReader extractor;
	/** ETL XES Loader */
	@Autowired private EventWriter loader;
	/** ETL XES Transformer */
	@Autowired private EventConverter transformer;	
	/** Mock source BPAF etl endpoint */
    @EndpointInject(uri = "mock:bpafSource")
    protected MockEndpoint sourceEndpoint;
    /** Mock target XES etl endpoint */
    @EndpointInject(uri = "mock:bpafTarget")
    protected MockEndpoint targetEndpoint;	
    @EndpointInject(ref = "gbas.bpaf.queue.endpoint")
    ProducerTemplate gbasProducer;
    @EndpointInject(ref = "gbas.bpaf.queue.endpoint")
    protected Endpoint gbasEndpoint;	
    
	@Before
	public void setUp() {
	}		
	
	@Test
	@DirtiesContext 
	@Rollback(true)
    public void testETLProcess() throws Throwable { 		
		Assert.assertNotNull(etl);
		for (int i = 0; i < 25; i++) {
			Event event = generateMockEvent();
			
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(baos));  
    		JAXBContext context = JAXBContext.newInstance(Event.class);
    		Marshaller marshaller = context.createMarshaller();
    		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
    		//logger.debug("Marshalling event " + event + "...");
    		marshaller.marshal(event, writer);
    		final byte[] xml = baos.toByteArray();    			
    		DefaultExchange exchange = new DefaultExchange(sourceEndpoint);
    		exchange.getIn().setBody(xml, byte[].class);  	
    		sourceEndpoint.assertIsSatisfied();
    		//logger.debug("Performing ETL process...");	
    		etl.process(exchange);
    		//logger.debug("ETL process performed.");		    		
		}		
    }
	
	@Test
	@DirtiesContext 
	@Rollback(true)
    public void testETLExtractor() throws Throwable { 		
		Assert.assertNotNull(extractor);
		for (int i = 0; i < 50; i++) {
			Event event = generateMockEvent();
			
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(baos));  
    		JAXBContext context = JAXBContext.newInstance(Event.class);
    		Marshaller marshaller = context.createMarshaller();
    		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
    		//logger.debug("Marshalling event " + event + "...");
    		marshaller.marshal(event, writer);
    		final byte[] xml = baos.toByteArray();
    		DefaultExchange exchange = new DefaultExchange(sourceEndpoint);
    		exchange.getIn().setBody(xml, byte[].class);  
    		sourceEndpoint.assertIsSatisfied();
    		Event _event = extractor.extractEvent(exchange);
    		assert (_event != null && _event.getEventID() != event.getEventID());    		
		}		
    }

	@Test
	@DirtiesContext 
	@Rollback(true)
    public void testETLTransformer() throws Throwable { 		
		Assert.assertNotNull(transformer);		
		for (int i = 0; i < 50; i++) {
			es.uc3m.softlab.cbi4api.gbas.event.store.domain.Event _event = null;
			Event event = generateMockEvent();		
			try {
				_event = transformer.transform(event);
				assert(_event != null && !String.valueOf(_event.getEventID()).equals(event.getEventID()));   
			} catch(EventException evex) {
				// a non-state-transition must be considered as an acceptable scenario
				if (evex.getCode() != StaticResources.WARN_EVENT_WITH_NO_STATE_TRANSITION) 
					assert(false);	
			}    		 		    		
		}		
    }
	
	@Test
	@DirtiesContext 
	@Rollback(true)
    public void testETLLoader() throws Throwable { 		
		Assert.assertNotNull(loader);		
		for (int i = 0; i < 50; i++) {		
			es.uc3m.softlab.cbi4api.gbas.event.store.domain.Event _event = null;
			Event event = generateMockEvent();
			try {
				_event = transformer.transform(event);
				assert(_event != null && !String.valueOf(_event.getEventID()).equals(event.getEventID()));   
	    		DefaultExchange exchange = new DefaultExchange(targetEndpoint);	
				loader.loadEvent(exchange, _event, event);
	    		targetEndpoint.assertIsSatisfied();  
			} catch(EventException evex) {
				// a non-state-transition must be considered as an acceptable scenario
				if (evex.getCode() != StaticResources.WARN_EVENT_WITH_NO_STATE_TRANSITION) 
					assert(false);	
			}  
		}		
    }
	
	@Test
	@Rollback(true)
	@DirtiesContext 
	public void testCorrelation() throws Throwable {	
		// TODO
	}
	
	@Test
	@DirtiesContext 
	@Rollback(true)
	public void testRouting() throws Throwable {		
		Event event = generateMockEvent();

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(baos));  
		JAXBContext context = JAXBContext.newInstance(Event.class);
		Marshaller marshaller = context.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		//logger.debug("Marshalling event " + event + "...");
		marshaller.marshal(event, writer);
		final byte[] xml = baos.toByteArray();
		gbasProducer.sendBody(xml);

		PollingConsumer consumer = gbasEndpoint.createPollingConsumer();
		Exchange exchange = consumer.receive(5000);		
		assert (exchange != null && exchange.getIn() != null);
		byte[] _xml = (byte[])exchange.getIn().getBody();
		assert (_xml != null);
		//logger.debug(new String(_xml));
	}	
		
	private Event generateMockEvent() {
		Event event = new Event();
		event.setActivityDefinitionID("SRC_ACT_BP_MODEL_ID");
		event.setActivityInstanceID(UUID.randomUUID().toString());
		event.setActivityName(UUID.randomUUID().toString());
		event.setEventID(String.valueOf((long)(Math.random() * 10000)));
		event.setProcessDefinitionID("SRC_PRC_BP_MODEL_ID");		
		event.setProcessName(UUID.randomUUID().toString());
		event.setServerID("GBAS-SOURCE");
		event.setEventDetails(new EventDetails());
		State currentState = State.values()[(int)(Math.random() * State.values().length)];
		State previousState = State.values()[(int)(Math.random() * State.values().length)];
		event.getEventDetails().setCurrentState(currentState);
		event.getEventDetails().setPreviousState(previousState); 
		Calendar tstamp = Calendar.getInstance();
		tstamp.setTime(new Date());
		event.setTimestamp(tstamp);
		event.setCorrelation(new Correlation());
		event.getCorrelation().setProcessInstanceID(UUID.randomUUID().toString());
		event.getCorrelation().setCorrelationData(new CorrelationData());
		for (int i=0; i < (1 + ((int)Math.random() * 10)); i++) {
			CorrelationElement elem = new CorrelationElement();
			elem.setKey(UUID.randomUUID().toString());
			elem.setValue(UUID.randomUUID().toString());
			event.getCorrelation().getCorrelationData().getCorrelationElement().add(elem);
		}
		return event;
	}
	
	@After
	public void tearDown() {
		
	}
}