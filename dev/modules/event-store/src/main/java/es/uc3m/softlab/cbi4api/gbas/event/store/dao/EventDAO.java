/* 
 * $Id: EventDAO.java,v 1.0 2013-01-27 23:29:05 averab Exp $
 *
 * @copyright Universidad Carlos III de Madrid. proprietary/confidential. Use is subject to license terms.
 */
package es.uc3m.softlab.cbi4api.gbas.event.store.dao;

import java.util.List;

import es.uc3m.softlab.cbi4api.gbas.event.store.StaticResources;
import es.uc3m.softlab.cbi4api.gbas.event.store.domain.Event;

import javax.persistence.TransactionRequiredException;

/**
 * Data access object interface for the {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.Event}
 * entity object. This interface defines all methods for accessing to the 
 * <strong>Event</strong> entity data through the JPA persistence layer.
 * 
 * @author averab
 * @version 1.0.0
 */
public interface EventDAO {
 	/** Local jndi name */
    public static final String COMPONENT_NAME = StaticResources.COMPONENT_NAME_EVENT_DAO;        

	/**
	 * Find all {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.Event} entity
	 * objects.
	 * @return all {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.Event} entity
	 * objects.
	 * @throws IllegalArgumentException if an illegal argument error occurred.
	 */
    public List<Event> findAll() throws IllegalArgumentException;
	/**
	 * Find all {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.Event} entity
	 * objects of a determined process instance 
	 * ({@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.Event#getProcessInstanceID()}).
	 * 
	 * @param processInstId process instance identifier. 
	 * ({@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.Event#getProcessInstanceID()}) associated.
	 * @return all {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.Event} entity
	 * objects of a determined process instance identifier.
	 * @throws IllegalArgumentException if an illegal argument error occurred.
	 */
    public List<Event> findAllByProcessInstId(long processInstId) throws IllegalArgumentException;
	/**
	 * Find the {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.Event} entity
	 * object associated to the  
	 * {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.Event#getEventID()} as primary key.
	 * 
	 * @param eventId event's identifier
	 * @return {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.Event} entity
	 * object associated.
	 * @throws IllegalArgumentException if an illegal argument error occurred.
	 */
	public Event findById(long eventId) throws IllegalArgumentException;
	/**
	 * Merges and synchronizes the {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.Event}
	 * entity object state with the data base.
	 * 
	 * @param event {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.Event}
	 * entity object to merge.
	 * @throws IllegalArgumentException if an illegal argument error occurred.
	 * @throws TransactionRequiredException if a transaction error occurred.
	 */
	public void merge(Event event) throws IllegalArgumentException, TransactionRequiredException;
	/**
	 * Deletes the {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.Event}
	 * entity object associated from the data base.
	 * 
	 * @param event {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.Event}
	 * entity object to delete.
	 * @throws IllegalArgumentException if an illegal argument error occurred.
	 * @throws TransactionRequiredException if a transaction error occurred.
	 */	
	public void delete(Event event) throws IllegalArgumentException, TransactionRequiredException;
	/**
	 * Saves the {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.Event}
	 * entity object associated to the data base.
	 * 
	 * @param event {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.Event}
	 * entity object to save.
	 * @throws IllegalArgumentException if an illegal argument error occurred.
	 * @throws TransactionRequiredException if a transaction error occurred.
	 */	
	public void save(Event event) throws IllegalArgumentException, TransactionRequiredException;
	/**
	 * Load the {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.Event#getPayload()} associated
	 * from the data base. This is needed due to the fetch property is set to lazy, and the 
	 * object is loaded out of synchronism when the {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.Event}
	 * is loaded out of the same transaction.
	 * 
	 * @param event {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.Event} to load the event payload on.
	 * @throws IllegalArgumentException if an illegal argument error occurred.
	 */	
	public void loadPayload(Event event) throws IllegalArgumentException;
	/**
	 * Load the {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.Event#getCorrelations()} associated
	 * from the data base. This is needed due to the fetch property is set to lazy, and the 
	 * object is loaded out of synchronism when the {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.Event}
	 * is loaded out of the same transaction.
	 * 
	 * @param event {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.Event} to load the event correlation data on.
	 * @throws IllegalArgumentException if an illegal argument error occurred.
	 */	
	public void loadCorrelation(Event event) throws IllegalArgumentException;
	/**
	 * Load the {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.Event#getData()} associated
	 * from the data base. This is needed due to the fetch property is set to lazy, and the 
	 * object is loaded out of synchronism when the {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.Event}
	 * is loaded out of the same transaction.
	 * 
	 * @param event {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.Event} to load the event data elements on.
	 * @throws IllegalArgumentException if an illegal argument error occurred.
	 */	
	public void loadData(Event event) throws IllegalArgumentException;
}
