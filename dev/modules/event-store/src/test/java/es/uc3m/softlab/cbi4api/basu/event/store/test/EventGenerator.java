/* 
 * $Id: EventGenerator.java,v 1.0 2013-03-24 12:29:27 averab Exp $
 *
 * @copyright Universidad Carlos III de Madrid. proprietary/confidential. Use is subject to license terms.
 */
package es.uc3m.softlab.cbi4api.gbas.event.store.test;

import java.util.Date;
import java.util.UUID;


import org.apache.log4j.Logger;

import es.uc3m.softlab.cbi4api.gbas.event.store.domain.ActivityInstance;
import es.uc3m.softlab.cbi4api.gbas.event.store.domain.Event;
import es.uc3m.softlab.cbi4api.gbas.event.store.domain.EventCorrelation;
import es.uc3m.softlab.cbi4api.gbas.event.store.domain.EventData;
import es.uc3m.softlab.cbi4api.gbas.event.store.domain.EventPayload;
import es.uc3m.softlab.cbi4api.gbas.event.store.domain.ProcessInstance;
import es.uc3m.softlab.cbi4api.gbas.event.store.facade.EventException;
import es.uc3m.softlab.cbi4api.gbas.event.store.facade.EventFacade;

/**
 * This is the main class to launch the event generator service used for
 * triggering events from a pre-configured BPEL engine (Apache ODE 1.3.5).
 * 
 * @author averab
 * @version 1.0.0
 */
public class EventGenerator extends Thread {
    /** Logger for tracing */
    private static Logger logger = Logger.getLogger(EventGenerator.class);
	/** Event session facade */
	private EventFacade eventFacade;
	/** */
	private ProcessInstance processInstance;
	/** */
	private ActivityInstance activityInstance;
    
    /**
     * 
     * @param eventFacade
     * @param processInstance
     * @param activityInstance
     */
    public EventGenerator(EventFacade eventFacade, ProcessInstance processInstance, ActivityInstance activityInstance) {
    	this.eventFacade = eventFacade;
    	this.processInstance = processInstance;
    	this.activityInstance = activityInstance;
    }
	/**
	 * 
	 */
	public void run() {	
		Event event = new Event();			
		event.setProcessInstance(processInstance);

		event.setActivityInstance(activityInstance);

		event.setTimestamp(new Date());	

		es.uc3m.softlab.cbi4api.gbas.event.store.domain.State currentState;
		es.uc3m.softlab.cbi4api.gbas.event.store.domain.State previousState;
		currentState = es.uc3m.softlab.cbi4api.gbas.event.store.domain.State.values()[(int)(Math.random() * es.uc3m.softlab.cbi4api.gbas.event.store.domain.State.values().length)];
		previousState = es.uc3m.softlab.cbi4api.gbas.event.store.domain.State.values()[(int)(Math.random() * es.uc3m.softlab.cbi4api.gbas.event.store.domain.State.values().length)];
		event.getEventDetails().setCurrentState(currentState);
		event.getEventDetails().setPreviousState(previousState); 

		for (int j = 0; j < (1+((int)(Math.random() * 10))); j++) {				
			EventPayload payload = new EventPayload();
			payload.setKey(UUID.randomUUID().toString());
			payload.setValue(UUID.randomUUID().toString());
			payload.setEvent(event);
			event.getPayload().add(payload);
		}

		String[] corKey = new String[] {"orderNo", "CustomerNo", "ItemNo", "transCode", "opCode", "invoiceNo", "supplierId", "deptNo"};
		
		/*
		EventCorrelation correlation = new EventCorrelation();
		correlation.setKey(corKey[0]);
		correlation.setValue("0401");
		correlation.setEvent(event);
		event.getCorrelations().add(correlation);
		
		correlation = new EventCorrelation();
		correlation.setKey(corKey[1]);
		correlation.setValue("0748");
		correlation.setEvent(event);
		event.getCorrelations().add(correlation);
		
		correlation = new EventCorrelation();
		correlation.setKey(corKey[2]);
		correlation.setValue("0125");
		correlation.setEvent(event);
		event.getCorrelations().add(correlation);
		
		correlation = new EventCorrelation();
		correlation.setKey(corKey[4]);
		correlation.setValue("0286");
		correlation.setEvent(event);
		event.getCorrelations().add(correlation);
		*/
		
		for (int j = 0; j < (1+((int)(Math.random() * 8))); j++) {				
			EventCorrelation correlation = new EventCorrelation();
			correlation.setKey(corKey[j]);
			correlation.setValue(String.format("%04d", (j+((int)(Math.random() * 1000)))));
			correlation.setEvent(event);
			event.getCorrelations().add(correlation);
		}

		for (int j = 0; j < (1+((int)(Math.random() * 10))); j++) {				
			EventData element = new EventData();
			element.setKey(UUID.randomUUID().toString());
			element.setValue(UUID.randomUUID().toString());
			element.setEvent(event);
			event.getDataElement().add(element);
		}
		try {				
			eventFacade.storeEvent(event);	
		} catch (EventException ex) {
			logger.error(ex.fillInStackTrace());
			ex.printStackTrace();
		} 
	}
}
