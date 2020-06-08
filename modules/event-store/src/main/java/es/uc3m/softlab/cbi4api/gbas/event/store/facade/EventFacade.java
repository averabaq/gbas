/* 
 * $Id: EventFacade.java,v 1.0 2011-10-28 12:29:27 averab Exp $
 *
 * @copyright Universidad Carlos III de Madrid. proprietary/confidential. Use is subject to license terms.
 */
package es.uc3m.softlab.cbi4api.gbas.event.store.facade;

import es.uc3m.softlab.cbi4api.gbas.event.store.StaticResources;
import es.uc3m.softlab.cbi4api.gbas.event.store.domain.Event;

import java.util.List;

/**
 * Facade design pattern interface for the  {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.Event}  entity
 * object. This interface defines all methods for accessing to the <strong>event</strong> entity data 
 * throughout the Spring framework.
 * 
 * @author averab
 * @version 1.0.0
 */
public interface EventFacade {
 	/** Spring component name */
    public static final String COMPONENT_NAME = StaticResources.COMPONENT_NAME_EVENT_FACADE;        

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
    public Event getEvent(long id) throws EventException;
	/**
	 * Gets all {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.Event} entity
	 * objects.
	 * 
	 * @return all {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.Event} entity
	 * objects.
	 */
    public List<Event> getAll();    
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
    public List<Event> getAllFromProcessInstId(long processInstId);
	/**
	 * Saves and synchronizes the {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.Event}
	 * entity object state with the data base.
	 * 
	 * @param event {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.Event}
	 * entity object to save.
	 * @throws EventException if any illegal data access or inconsistent event data error occurred.
	 */
	public void storeEvent(Event event) throws EventException;
	/**
	 * Updates and synchronizes the {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.Event}
	 * entity object state with the data base.
	 * 
	 * @param event {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.Event}
	 * entity object to update.
	 * @throws EventException if any illegal data access or inconsistent event data error occurred.
	 */
	public void updateEvent(Event event) throws EventException;
	/**
	 * Removes and synchronizes the {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.Event}
	 * entity object state with the data base.
	 * 
	 * @param event {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.Event}
	 * entity object to delete.
	 * @throws EventException if any illegal data access or inconsistent event data error occurred.
	 */
	public void deleteEvent(Event event) throws EventException;
	/**
	 * Loads the {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.Event#data} associated
	 * from the data base. This is needed due to the fetch property is set to lazy, and their associated 
	 * objects are loaded out of synchronism when the {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.Event}
	 * is initially loaded within a JTA transaction.
	 * 
	 * @param event {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.Event} to load the event data on.
	 * @throws EventException if any illegal data access or inconsistent event data error occurred.
	 */	
    public void loadEventData(Event event) throws EventException;
	/**
	 * Loads the {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.Event#payload} associated
	 * from the data base. This is needed due to the fetch property is set to lazy, and their associated 
	 * objects are loaded out of synchronism when the {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.Event}
	 * is initially loaded within a JTA transaction.
	 * 
	 * @param event {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.Event} to load the event payload on.
	 * @throws EventException if any illegal data access or inconsistent event data error occurred.
	 */	
    public void loadEventPayload(Event event) throws EventException;
	/**
	 * Loads the {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.Event#correlations} associated
	 * from the data base. This is needed due to the fetch property is set to lazy, and their associated 
	 * objects are loaded out of synchronism when the {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.Event}
	 * is initially loaded within a JTA transaction.
	 * 
	 * @param event {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.Event} to load the event correlation on.
	 * @throws EventException if any illegal data access or inconsistent event data error occurred.
	 */	
    public void loadEventCorrelation(Event event) throws EventException;
}