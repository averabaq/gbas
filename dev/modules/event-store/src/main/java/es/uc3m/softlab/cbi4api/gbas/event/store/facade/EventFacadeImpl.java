/* 
 * $Id: EventFacadeImpl.java,v 1.0 2013-01-27 23:29:05 averab Exp $
 *
 * @copyright Universidad Carlos III de Madrid. proprietary/confidential. Use is subject to license terms.
 */
package es.uc3m.softlab.cbi4api.gbas.event.store.facade;

import es.uc3m.softlab.cbi4api.gbas.event.store.StaticResources;
import es.uc3m.softlab.cbi4api.gbas.event.store.dao.EventDAO;
import es.uc3m.softlab.cbi4api.gbas.event.store.domain.Event;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Facade design pattern class implementation for the {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.Event}
 * entity object. This class manages all data access throughout the Spring framework.
 * 
 * @author averab  
 * @version 1.0.0
 */
@Transactional
@Service(value=EventFacade.COMPONENT_NAME)
public class EventFacadeImpl implements EventFacade {
    /** Logger for tracing */
    private transient Logger logger = Logger.getLogger(EventFacadeImpl.class);
    /** Entity manager bound to this session bean throughout the JTA */
    @PersistenceContext(unitName=StaticResources.PERSISTENCE_UNIT_NAME_EVENT_STORE)
    protected EntityManager entityManager;
    /** Event data access object */
    @Autowired private EventDAO eventDAO;
    
	/**
	 * Gets the {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.Event} entity
	 * object associated to the  
	 * {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.Event#getEventID()} as primary key.
	 * 
	 * @param id event's identifier
	 * @return {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.Event} entity
	 * object associated.
	 * @throws EventException if any event error occurred.
	 */
    public Event getEvent(long id) throws EventException {
		logger.debug("Retrieving event with identifier " + id + "...");
		Event event = eventDAO.findById(id);
		if (event == null) {
			logger.warn("Cannot get event. Event with identifier: " + id + " does not exist.");
			throw new EventException(StaticResources.WARN_GET_EVENT_NOT_EXIST, "Event with id: " + id + " does not exist.");
		}
		logger.debug("Event " + event + " retrieved successfully.");
		return event;
	}
	/**
	 * Gets all {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.Event} entity
	 * objects.
	 * 
	 * @return all {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.Event} entity
	 * objects.
	 */
    public List<Event> getAll() {
		logger.debug("Getting all events...");
		List<Event> list = eventDAO.findAll();
		/*
		for (Event event : list) {
			eventDAO.loadPayload(event);
			eventDAO.loadCorrelation(event);
			eventDAO.loadData(event);
		}		
		*/
		logger.debug("All events retrieved successfully.");
		return list;
	}       
	/**
	 * Gets all {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.Event} entity
	 * objects of a determined process instance 
	 * ({@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.Event#getProcessInstanceID()}).
	 * 
	 * @param processInstId process instance identifier. 
	 * ({@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.Event#getProcessInstanceID()}) associated.
	 * @return all {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.Event} entity
	 * objects of a determined process instance identifier.
	 */
    public List<Event> getAllFromProcessInstId(long processInstId) {
		logger.debug("Getting all events from process instance with identifier " + processInstId + "...");
		List<Event> list = eventDAO.findAllByProcessInstId(processInstId);
		/*
		for (Event event : list) {
			eventDAO.loadPayload(event);
			eventDAO.loadCorrelation(event);
			eventDAO.loadData(event);
		}		
		*/
		logger.debug("All events from process instance with identifier " + processInstId + " found successfully.");
		return list;
	}    
	/**
	 * Saves and synchronizes the {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.Event}
	 * entity object state with the data base.
	 * 
	 * @param event {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.Event}
	 * entity object to save.
	 * @throws EventException if any illegal data access or inconsistent event data error occurred.
	 */
    public void storeEvent(Event event) throws EventException {
		logger.debug("Saving the event " + event + "...");
		if (event == null) {
			logger.warn("Cannot save event. Event is null.");
			throw new EventException(StaticResources.WARN_SAVE_EVENT_NULL, "Cannot save event. Event is null.");
		}
		if (event.getProcessInstance() == null) {
			logger.warn("Cannot save event. Event has not the process instance information associated. Process instance is required.");
			throw new EventException(StaticResources.WARN_SAVE_EVENT_NULL_PROCESS_INSTANCE_ID, "Cannot save event. Event has not the process instance information associated. Process instance is required.");
		}
		Event _event = eventDAO.findById(event.getEventID());
		if (_event != null) {
			logger.warn("Cannot save event. Event already exists.");
			throw new EventException(StaticResources.WARN_SAVE_EVENT_ALREADY_EXISTS, "Cannot save event. Event already exists.");			
		}
		if (event.getTimestamp() == null)
			event.setTimestamp(new Date());
		eventDAO.save(event);
		logger.debug("Event " + event + " saved successfully.");
	}    
	/**
	 * Updates and synchronizes the {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.Event}
	 * entity object state with the data base.
	 * 
	 * @param event {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.Event}
	 * entity object to update.
	 * @throws EventException if any illegal data access or inconsistent event data error occurred.
	 */
	public void updateEvent(Event event) throws EventException {
		logger.debug("Updating event " + event + "...");	
		Event _event = eventDAO.findById(event.getEventID());
		if (_event == null) {
			logger.warn("Cannot update event. Event with identifier: " + event.getEventID() + " does not exist.");
			throw new EventException(StaticResources.WARN_UPDATE_EVENT_NOT_EXIST, "Cannot update event. Event with identifier: " + event.getEventID() + " does not exist.");
		}		
		/* 
		 * The data elements, correlation and payload information is not managed by this 
		 * method because it is supposed not to be updatable.
		 */
		eventDAO.merge(event);		
		logger.info("Event " + event + " updated successfully.");
	}     
	/**
	 * Removes and synchronizes the {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.Event}
	 * entity object state with the data base.
	 * 
	 * @param event {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.Event}
	 * entity object to delete.
	 * @throws EventException if any illegal data access or inconsistent event data error occurred.
	 */
	public void deleteEvent(Event event) throws EventException {
		logger.debug("Removing event " + event + "...");	
		Event _event = eventDAO.findById(event.getEventID());
		if (_event == null) {
			logger.warn("Cannot remove event. Event with identifier: " + event.getEventID() + " does not exist.");
			throw new EventException(StaticResources.WARN_DELETE_EVENT_NOT_EXIST, "Cannot remove event. Event with identifier: " + event.getEventID() + " does not exist.");
		}
		eventDAO.delete(_event);		
		logger.info("Event " + _event + " removed successfully.");
	}	
	/**
	 * Loads the {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.Event#dataElement} associated
	 * from the data base. This is needed due to the fetch property is set to lazy, and their associated 
	 * objects are loaded out of synchronism when the {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.Event}
	 * is initially loaded within a JTA transaction.
	 * 
	 * @param event {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.Event} to load the event data on.
	 * @throws EventException if any illegal data access or inconsistent event data error occurred.
	 */	
    public void loadEventData(Event event) throws EventException {
		logger.debug("Loading event data on event '" + event + "'...");
		Event _event = eventDAO.findById(event.getEventID());
		if (_event == null) {
			logger.debug("Cannot load event data. Event with identifier: '" + event + "' does not exist.");
			throw new EventException(StaticResources.WARN_LOAD_EVENT_DATA_EVENT_NOT_EXIST, "Cannot load event data. Event '" + event + "' does not exist.");
		}
		eventDAO.loadData(event);
		logger.debug("Event data from event " + event + " loaded successfully.");
	}    	
	/**
	 * Loads the {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.Event#payload} associated
	 * from the data base. This is needed due to the fetch property is set to lazy, and their associated 
	 * objects are loaded out of synchronism when the {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.Event}
	 * is initially loaded within a JTA transaction.
	 * 
	 * @param event {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.Event} to load the event payload on.
	 * @throws EventException if any illegal data access or inconsistent event data error occurred.
	 */	
    public void loadEventPayload(Event event) throws EventException {
		logger.debug("Loading event payload on event '" + event + "'...");
		Event _event = eventDAO.findById(event.getEventID());
		if (_event == null) {
			logger.debug("Cannot load event payload. Event with identifier: '" + event + "' does not exist.");
			throw new EventException(StaticResources.WARN_LOAD_EVENT_PAYLOAD_EVENT_NOT_EXIST, "Cannot load event payload. Event '" + event + "' does not exist.");
		}
		eventDAO.loadPayload(event);
		logger.debug("Event payload from event " + event + " loaded successfully.");
	}    
	/**
	 * Loads the {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.Event#correlations} associated
	 * from the data base. This is needed due to the fetch property is set to lazy, and their associated 
	 * objects are loaded out of synchronism when the {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.Event}
	 * is initially loaded within a JTA transaction.
	 * 
	 * @param event {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.Event} to load the event correlation on.
	 * @throws EventException if any illegal data access or inconsistent event data error occurred.
	 */	
    public void loadEventCorrelation(Event event) throws EventException {
		logger.debug("Loading event correlation on event '" + event + "'...");
		Event _event = eventDAO.findById(event.getEventID());
		if (_event == null) {
			logger.debug("Cannot load event correlation. Event with identifier: '" + event + "' does not exist.");
			throw new EventException(StaticResources.WARN_LOAD_EVENT_CORRELATION_EVENT_NOT_EXIST, "Cannot load event correlation. Event '" + event + "' does not exist.");
		}
		eventDAO.loadCorrelation(event);
		logger.debug("Event correlation from event " + event + " loaded successfully.");
	}     
}