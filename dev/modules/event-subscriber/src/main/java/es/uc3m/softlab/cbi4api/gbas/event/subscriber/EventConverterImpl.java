/* 
 * $Id: EventConverterImpl.java,v 1.0 2013-01-27 23:29:05 averab Exp $
 *
 * @copyright Universidad Carlos III de Madrid. proprietary/confidential. Use is subject to license terms.
 */
package es.uc3m.softlab.cbi4api.gbas.event.subscriber;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import es.uc3m.softlab.cbi4api.gbas.event.store.domain.ActivityInstance;
import es.uc3m.softlab.cbi4api.gbas.event.store.domain.Event;
import es.uc3m.softlab.cbi4api.gbas.event.store.domain.EventCorrelation;
import es.uc3m.softlab.cbi4api.gbas.event.store.domain.EventData;
import es.uc3m.softlab.cbi4api.gbas.event.store.domain.EventPayload;
import es.uc3m.softlab.cbi4api.gbas.event.store.domain.ProcessInstance;
import es.uc3m.softlab.cbi4api.gbas.event.store.domain.Source;
import es.uc3m.softlab.cbi4api.gbas.event.store.domain.State;
import es.uc3m.softlab.cbi4api.gbas.event.store.facade.ActivityInstanceException;
import es.uc3m.softlab.cbi4api.gbas.event.store.facade.EventException;
import es.uc3m.softlab.cbi4api.gbas.event.store.facade.ModelException;
import es.uc3m.softlab.cbi4api.gbas.event.store.facade.ModelFacade;
import es.uc3m.softlab.cbi4api.gbas.event.store.facade.ProcessInstanceException;
import es.uc3m.softlab.cbi4api.gbas.event.store.facade.SourceException;
import es.uc3m.softlab.cbi4api.gbas.event.store.facade.SourceFacade;
import es.uc3m.softlab.cbi4api.gbas.event.subscriber.xsd.basu.event.CorrelationElement;
import es.uc3m.softlab.cbi4api.gbas.event.subscriber.xsd.basu.event.DataElement;
import es.uc3m.softlab.cbi4api.gbas.event.subscriber.xsd.basu.event.Payload;


import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Component implementation for converting the incoming events in BPAF-F4BPA extended format 
 * into an BPAF entity object model.
 * 
 * <p> This class follows the  
 * <a href="http://www.wfmc.org/business-process-analytics-format.html"> BPAF
 * (Business Process Analytics Format)</a> specification standard published by
 * the <a href="http://www.wfmc.org">WfMC (Workflow Management Coalition) by
 * extracting the event data from an extended BPAF-F4BPA format and transforming its content 
 * into an BPAF entity object model.</a>. 
 * 
 * @author averab
 * @version 1.0.0
 */
@Component(value=EventConverter.COMPONENT_NAME)
public class EventConverterImpl implements EventConverter {
    /** Logger for tracing */
    private transient Logger logger = Logger.getLogger(EventConverterImpl.class);
    /** Source session facade */
    @Autowired private SourceFacade sourceFacade;   
    /** Model session facade */
    @Autowired private ModelFacade modelFacade;
    /** Event correlator component */
    @Autowired private EventCorrelator eventCorrelator;   
    
    /**
     * Convert the input event contents in BPAF-F4BPA extension format into 
     * modeled <strong>BPAF</strong> entity beans.
     * 
     * @param event event in BPAF-F4BPA extension format.
     * @return event in BPAF entity object model.
     * @throws ModelException if any model exception occurred during processing.
     * @throws ProcessInstanceException if any process instance exception occurred during processing.
     * @throws ActivityInstanceException if any activity instance exception occurred during processing.
     * @throws EventException if any event exception occurred during processing.
     * @throws SourceException if the source it is not defined at the database.
     */
    public Event transform(es.uc3m.softlab.cbi4api.gbas.event.subscriber.xsd.basu.event.Event event) throws SourceException, ModelException, ProcessInstanceException, ActivityInstanceException, EventException {
		/* creates the new event */
    	Event evt = new Event();

    	/* checks the source definition */    	
		Source source = sourceFacade.getSource(event.getServerID());			
		if (source == null) {
			logger.error(String.format("Cannot get source " + event.getServerID() + ". Rejecting the event [%s]", event.getEventID()));
			throw new SourceException(es.uc3m.softlab.cbi4api.gbas.event.store.StaticResources.WARN_GET_SOURCE_NOT_EXIST,
                    String.format("Cannot get source " + event.getServerID() + ". Rejecting the event [%s]", event.getEventID()));
		}						
		/* correlates the event by obtaining an existing process instance or creating a new one if necessary */
		ProcessInstance processInstance = eventCorrelator.correlateProcess(event, source);		
		/* sets the process instance */
		evt.setProcessInstance(processInstance);

		/* correlates the event by obtaining an existing activity instance or creating a new one if necessary */
		ActivityInstance activityInstance = eventCorrelator.correlateActivity(event, source);		
		/* sets the activity instance */
		evt.setActivityInstance(activityInstance);
		
		/* sets the event time stamp */
		evt.setTimestamp(event.getTimestamp().getTime());
		
		/* sets the event states */
		State currentState = null;
		State previousState = null;	
		currentState = State.values()[event.getEventDetails().getCurrentState().ordinal()];
		if (event.getEventDetails().getPreviousState() != null)
			previousState = State.values()[event.getEventDetails().getPreviousState().ordinal()];
		evt.getEventDetails().setCurrentState(currentState);	
		evt.getEventDetails().setPreviousState(previousState);		
		
		if (currentState.equals(previousState)) 
			throw new EventException(StaticResources.WARN_EVENT_WITH_NO_STATE_TRANSITION, "The event " + evt + " does not present state transition. Thus it is not meaningful, and so it will be ignored.");		
				
		/* adds the event payload */
		evt.setPayload(transformPayload(evt, event.getPayload()));
		/* adds the additional data */
		evt.setDataElement(transformData(evt, event.getDataElement()));
		/* if the event correlation data is supplied */
		if (event.getCorrelation().isSetCorrelationData()) {
			/* adds the event correlation data */
			evt.setCorrelations(transformCorrelation(evt, event.getCorrelation().getCorrelationData().getCorrelationElement()));			
		}		
		return evt;		
    }  
	/**
	 * Transform a list of {@link es.uc3m.softlab.cbi4api.gbas.event.subscriber.xsd.basu.event.Payload}
	 * objects into a list of {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.EventPayload} objects.
	 * @param event {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.Event} to attach the payload.
	 * @param payloads list of {@link es.uc3m.softlab.cbi4api.gbas.event.subscriber.xsd.basu.event.Payload} objects.
	 * @return list of {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.EventPayload} objects.
	 */
	private Set<EventPayload> transformPayload(Event event, List<Payload> payloads) {
		if (payloads == null)
			return null;
		Set<EventPayload> _payloads = new HashSet<EventPayload>();
		for (Payload payload : payloads) {
			EventPayload _payload = new EventPayload();
			_payload.setKey(payload.getKey());
			_payload.setValue(payload.getValue());
			_payload.setEvent(event);			
			_payloads.add(_payload);
		}
		return _payloads;
	}    
	/**
	 * Transform a list of {@link es.uc3m.softlab.cbi4api.gbas.event.subscriber.xsd.basu.event.CorrelationElement}
	 * objects into a list of {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.EventCorrelation} objects.
	 * @param event {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.Event} to attach the correlation data.
	 * @param correlations list of {@link es.uc3m.softlab.cbi4api.gbas.event.subscriber.xsd.basu.event.CorrelationElement} objects.
	 * @return list of {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.EventCorrelation} objects.
	 */
	private Set<EventCorrelation> transformCorrelation(Event event, List<CorrelationElement> correlations) {
		if (correlations == null)
			return null;
		Set<EventCorrelation> eventCorrelations = new HashSet<EventCorrelation>();
		for (CorrelationElement correlation : correlations) {
			EventCorrelation eventCorrelation = new EventCorrelation();
			eventCorrelation.setKey(correlation.getKey());
			eventCorrelation.setValue(correlation.getValue());
			eventCorrelation.setEvent(event);			
			eventCorrelations.add(eventCorrelation);
		}
		return eventCorrelations;
	}
	/**
	 * Transform a list of {@link es.uc3m.softlab.cbi4api.gbas.event.subscriber.xsd.basu.event.Payload}
	 * objects into a list of {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.EventPayload} objects.
	 * @param event {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.Event} to attach the event data.
	 * @param dataElements list of {@link es.uc3m.softlab.cbi4api.gbas.event.subscriber.xsd.basu.event.Payload} objects.
	 * @return list of {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.EventPayload} objects.
	 */
	private Set<EventData> transformData(Event event, List<DataElement> dataElements) {
		if (dataElements == null)
			return null;
		Set<EventData> eventData = new HashSet<EventData>();
		for (DataElement dataElement : dataElements) {
			EventData data = new EventData();
			data.setKey(dataElement.getKey());
			data.setValue(dataElement.getValue());
			data.setEvent(event);
			eventData.add(data);
		}
		return eventData;
	}  	
}