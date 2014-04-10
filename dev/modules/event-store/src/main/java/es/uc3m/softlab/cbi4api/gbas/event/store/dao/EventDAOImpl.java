/* 
 * $Id: EventDAOImpl.java,v 1.0 2013-01-27 23:29:05 averab Exp $
 *
 * @copyright Universidad Carlos III de Madrid. proprietary/confidential. Use is subject to license terms.
 */
package es.uc3m.softlab.cbi4api.gbas.event.store.dao;

import es.uc3m.softlab.cbi4api.gbas.event.store.StaticResources;
import es.uc3m.softlab.cbi4api.gbas.event.store.Stats;
import es.uc3m.softlab.cbi4api.gbas.event.store.domain.ActivityInstance;
import es.uc3m.softlab.cbi4api.gbas.event.store.domain.Event;
import es.uc3m.softlab.cbi4api.gbas.event.store.domain.EventCorrelation;
import es.uc3m.softlab.cbi4api.gbas.event.store.domain.EventData;
import es.uc3m.softlab.cbi4api.gbas.event.store.domain.EventPayload;
import es.uc3m.softlab.cbi4api.gbas.event.store.domain.ProcessInstance;
import es.uc3m.softlab.cbi4api.gbas.event.store.entity.HEvent;
import es.uc3m.softlab.cbi4api.gbas.event.store.entity.HEventCorrelation;
import es.uc3m.softlab.cbi4api.gbas.event.store.entity.HEventData;
import es.uc3m.softlab.cbi4api.gbas.event.store.entity.HEventPayload;
import es.uc3m.softlab.cbi4api.gbas.event.store.entity.HEventCorrelationIndex;
import es.uc3m.softlab.cbi4api.gbas.event.store.entity.HIndexBuilder;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnit;
import javax.persistence.Query;
import javax.persistence.TransactionRequiredException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Data access object class implementation for the <strong>event</strong> 
 * entity object. This class manages all data access throughout the JPA 
 * persistence layer.
 * 
 * @author averab  
 * @version 1.0.0
 */
@PersistenceUnit(name=StaticResources.PERSISTENCE_NAME_EVENT_STORE, 
		         unitName=StaticResources.PERSISTENCE_UNIT_NAME_EVENT_STORE)
@Transactional(propagation=Propagation.MANDATORY)
@Repository(value=EventDAO.COMPONENT_NAME)
public class EventDAOImpl implements EventDAO {
    /** Logger for tracing */
    private transient Logger logger = Logger.getLogger(getClass());
    /** Entity manager bound to this data access object within the persistence context */
    @PersistenceContext(unitName=StaticResources.PERSISTENCE_UNIT_NAME_EVENT_STORE)
    protected EntityManager entityManager;
    /** Process instance data access object */
    @Autowired private ProcessInstanceDAO processInstanceDAO;
    /** Activity instance data access object */
    @Autowired private ActivityInstanceDAO activityInstanceDAO;    
    /** Statistical performance object */
    @Autowired private Stats stats; 

	/**
	 * Find all {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.Event} entity
	 * objects.
	 * @return all {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.Event} entity
	 * objects.
	 * @throws IllegalArgumentException if an illegal argument error occurred.
	 */
    @Override
    @SuppressWarnings("unchecked")
    public List<Event> findAll() throws IllegalArgumentException {
		logger.debug("Finding all events...");
		Query query = entityManager.createQuery("select e from " + HEvent.class.getName() + " e order by e.eventID asc");
		List<HEvent> list = query.getResultList();
		logger.debug("All events retrieved successfully.");
		/* convert to business object */
		List<Event> events = new ArrayList<Event>();
		for (HEvent hevent : list) {
			Event event = BusinessObjectAssembler.getInstance().toBusinessObject(hevent);
			// load the process instance
			ProcessInstance processInstance = processInstanceDAO.findById(hevent.getProcessInstance());
			event.setProcessInstance(processInstance);
			// load the activity instance if present 	
			if (hevent.getActivityInstance() != null) {
				ActivityInstance activityInstance = activityInstanceDAO.findById(hevent.getActivityInstance());
				event.setActivityInstance(activityInstance);
			}
			// load remaining sets of data
			loadPayload(event); 
			loadCorrelation(event); 
			loadData(event); 
			// add the event to the list
			events.add(event);
		}
		return events;
	}    
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
    @Override
    @SuppressWarnings("unchecked")
    public List<Event> findAllByProcessInstId(long processInstId) throws IllegalArgumentException {
		logger.debug("Finding all events of process instance with id " + processInstId + "...");
    	//
    	// JPQL parameters must be explicitly set as string values due to a bug  
    	// found on DataNucleus implementation (3.2.0-release) for HBase when 
    	// caching previous query parameters. 
    	//
    	StringBuffer sql = new StringBuffer("select e from [ENTITY] e ");
    	sql.append("where e.processInstance = :processInstId ");
    	sql.append("order by eventID asc ");
    	// set parameters
    	String _sql = sql.toString();
    	_sql = _sql.replace("[ENTITY]", HEvent.class.getName());
    	_sql = _sql.replace(":processInstId", "" + processInstId + "");
    	// creates query without setting parameters
    	Query query = entityManager.createQuery(_sql);
    	
    	//
    	// the snippet code below does not work as the DataNucleus implementation (3.2.0-release) 
    	// for HBase has a bug on cached parameters.
    	//
		// Query query = entityManager.createQuery("select e from " + HEvent.class.getName() + " e where e.processInstance = :processInstId order by eventID asc");
		// query.setParameter("processInstId", processInstId); 
    	
		List<HEvent> list = query.getResultList();
		logger.debug("All events of process instance with id " + processInstId + " found successfully.");
		// convert to business object 
		List<Event> events = new ArrayList<Event>();
		for (HEvent hevent : list) {
			Event event = BusinessObjectAssembler.getInstance().toBusinessObject(hevent);
			// load the process instance
			ProcessInstance processInstance = processInstanceDAO.findById(hevent.getProcessInstance());
			event.setProcessInstance(processInstance);
			// load the activity instance if present 	
			if (hevent.getActivityInstance() != null) {
				ActivityInstance activityInstance = activityInstanceDAO.findById(hevent.getActivityInstance());
				event.setActivityInstance(activityInstance);
			}
			// load remaining sets of data
			loadPayload(event); 
			loadCorrelation(event); 
			loadData(event); 
			// add the event to the list
			events.add(event);
		}
		return events;
	}     
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
    @Override
	public Event findById(long eventId) throws IllegalArgumentException {
		logger.debug("Retrieving event with identifier " + eventId + "...");
		long ini = System.nanoTime();
		HEvent hevent = entityManager.find(HEvent.class, eventId);	
		long end = System.nanoTime();	
		stats.writeStat(Stats.Operation.READ_BY_ID, HEvent.class, hevent, ini, end);
		if (hevent != null) {					
			Event event = BusinessObjectAssembler.getInstance().toBusinessObject(hevent);
			// load the process instance
			ProcessInstance processInstance = processInstanceDAO.findById(hevent.getProcessInstance());
			event.setProcessInstance(processInstance);
			// load the activity instance if present 	
			if (hevent.getActivityInstance() != null) {
				ActivityInstance activityInstance = activityInstanceDAO.findById(hevent.getActivityInstance());
				event.setActivityInstance(activityInstance);
			}
			// load remaining sets of data
			loadPayload(event); 
			loadCorrelation(event); 
			loadData(event); 
			logger.debug("Event " + event + " retrieved successfully.");
			return event;
		} else {
			logger.debug("Event with identifier " + eventId + " not found.");
			return null;
		}				
	}	
	/**
	 * Merges and synchronizes the {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.Event}
	 * entity object state with the data base.
	 * 
	 * @param event {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.Event}
	 * entity object to merge.
	 * @throws IllegalArgumentException if an illegal argument error occurred.
	 * @throws TransactionRequiredException if a transaction error occurred.
	 */
    @Override
	public void merge(Event event) throws IllegalArgumentException, TransactionRequiredException {
		logger.debug("Merging event " + event + "...");	
		HEvent hevent = BusinessObjectAssembler.getInstance().toEntity(event);
		long ini = System.nanoTime();
		entityManager.merge(hevent);
		long end = System.nanoTime();
		stats.writeStat(Stats.Operation.UPDATE, HEvent.class, hevent, ini, end);	
		logger.debug("Event " + event + " merged successfully.");
	}
	/**
	 * Deletes the {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.Event}
	 * entity object associated from the data base.
	 * 
	 * @param event {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.Event}
	 * entity object to delete.
	 * @throws IllegalArgumentException if an illegal argument error occurred.
	 * @throws TransactionRequiredException if a transaction error occurred.
	 */
    @Override
	public void delete(Event event) throws IllegalArgumentException, TransactionRequiredException {
		logger.debug("Deleting event " + event + "...");
		HEvent hevent = entityManager.find(HEvent.class, event.getEventID());
		if (hevent == null) {
			logger.warn("Event [" + event.getEventID() + "] could not be deleted. It does not exists.");
			return;
		}	
		// remove associated data
		deleteEventCorrelators(event);
		deleteEventPayload(event);
		deleteEventData(event);		
		long ini = System.nanoTime();
		entityManager.remove(hevent);
		long end = System.nanoTime();
		stats.writeStat(Stats.Operation.DELETE, HEvent.class, hevent, ini, end);
		logger.debug("Event " + event + " removed successfully.");
	}	
	/**
	 * Deletes the {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.EventCorrelation}
	 * entity objects associated to a particular the 
	 * {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.Event}.
	 * 
	 * @param event {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.Event}
	 * entity object whose {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.EventCorrelation}
	 * objects are to be deleted.
	 * @throws IllegalArgumentException if an illegal argument error occurred.
	 * @throws TransactionRequiredException if a transaction error occurred.
	 */
	@SuppressWarnings("unchecked")
	private void deleteEventCorrelators(Event event) throws IllegalArgumentException, TransactionRequiredException {
		logger.debug("Deleting event correlators of event " + event + "...");
		HEvent hevent = entityManager.find(HEvent.class, event.getEventID());
		if (hevent == null) {
			logger.warn("Event correlators of event [" + event.getEventID() + "] could not be deleted. The event does not exists.");
			return;
		}	
    	//
    	// JPQL parameters must be explicitly set as string values due to a bug  
    	// found on DataNucleus implementation (3.2.0-release) for HBase when 
    	// caching previous query parameters. 
    	//
    	StringBuffer sql = new StringBuffer("select c from [ENTITY] c ");
    	sql.append("where c.eventID = :eventID ");
    	// set parameters
    	String _sql = sql.toString();
    	_sql = _sql.replace("[ENTITY]", HEventCorrelation.class.getName());
    	_sql = _sql.replace(":eventID", "" + event.getEventID() + "");
    	// creates query without setting parameters
    	Query query = entityManager.createQuery(_sql);
    	
    	//
    	// the snippet code below does not work as the DataNucleus implementation (3.2.0-release) 
    	// for HBase has a bug on cached parameters.
    	//
		// Query query = entityManager.createQuery("select c from " + HEventCorrelation.class.getName() + " as c where c.eventID = :eventID");
		// query.setParameter("eventID", event.getEventID());
		
		// remove event associated data
		List<HEventCorrelation> hcorrelations = new ArrayList<HEventCorrelation>(query.getResultList());
		for (HEventCorrelation hcorrelation : hcorrelations) {
			// remove event	correlation	
			long ini = System.nanoTime();
			entityManager.remove(hcorrelation);
			long end = System.nanoTime();
			stats.writeStat(Stats.Operation.DELETE, HEventCorrelation.class, hcorrelation, ini, end);
			logger.debug("Event correlation " + hcorrelation + " removed successfully.");			
		}
		logger.debug("Event correlations of event " + event + " removed successfully.");
	}	
	/**
	 * Deletes the {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.EventPayload}
	 * entity objects associated to a particular the 
	 * {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.Event}.
	 * 
	 * @param event {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.Event}
	 * entity object whose {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.EventPayload}
	 * objects are to be deleted.
	 * @throws IllegalArgumentException if an illegal argument error occurred.
	 * @throws TransactionRequiredException if a transaction error occurred.
	 */
	@SuppressWarnings("unchecked")
	private void deleteEventPayload(Event event) throws IllegalArgumentException, TransactionRequiredException {
		logger.debug("Deleting event payload of event " + event + "...");
		HEvent hevent = entityManager.find(HEvent.class, event.getEventID());
		if (hevent == null) {
			logger.warn("Event payload of event [" + event.getEventID() + "] could not be deleted. The event does not exists.");
			return;
		}	
    	//
    	// JPQL parameters must be explicitly set as string values due to a bug  
    	// found on DataNucleus implementation (3.2.0-release) for HBase when 
    	// caching previous query parameters. 
    	//
    	StringBuffer sql = new StringBuffer("select p from [ENTITY] p ");
    	sql.append("where p.eventID = :eventID ");
    	// set parameters
    	String _sql = sql.toString();
    	_sql = _sql.replace("[ENTITY]", HEventPayload.class.getName());
    	_sql = _sql.replace(":eventID", "" + event.getEventID() + "");
    	// creates query without setting parameters
    	Query query = entityManager.createQuery(_sql);
    	
    	//
    	// the snippet code below does not work as the DataNucleus implementation (3.2.0-release) 
    	// for HBase has a bug on cached parameters.
    	//
		// Query query = entityManager.createQuery("select p from " + HEventPayload.class.getName() + " as p where p.eventID = :eventID");
		// query.setParameter("eventID", event.getEventID());
    	
		// remove event associated data
		List<HEventPayload> hpayload = new ArrayList<HEventPayload>(query.getResultList());
		for (HEventPayload _hpayload : hpayload) {
			// remove event	payload	
			long ini = System.nanoTime();
			entityManager.remove(_hpayload);
			long end = System.nanoTime();
			stats.writeStat(Stats.Operation.DELETE, HEventPayload.class, _hpayload, ini, end);
			logger.debug("Event payload " + _hpayload + " removed successfully.");			
		}
		logger.debug("Event payload of event " + event + " removed successfully.");
	}	
	/**
	 * Deletes the {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.EventData}
	 * entity objects associated to a particular the 
	 * {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.Event}.
	 * 
	 * @param event {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.Event}
	 * entity object whose {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.EventData}
	 * objects are to be deleted.
	 * @throws IllegalArgumentException if an illegal argument error occurred.
	 * @throws TransactionRequiredException if a transaction error occurred.
	 */
	@SuppressWarnings("unchecked")
	private void deleteEventData(Event event) throws IllegalArgumentException, TransactionRequiredException {
		logger.debug("Deleting event data elements of event " + event + "...");
		HEvent hevent = entityManager.find(HEvent.class, event.getEventID());
		if (hevent == null) {
			logger.warn("Event data elements of event [" + event.getEventID() + "] could not be deleted. The event does not exists.");
			return;
		}	
    	//
    	// JPQL parameters must be explicitly set as string values due to a bug  
    	// found on DataNucleus implementation (3.2.0-release) for HBase when 
    	// caching previous query parameters. 
    	//
    	StringBuffer sql = new StringBuffer("select d from [ENTITY] d ");
    	sql.append("where d.eventID = :eventID ");
    	// set parameters
    	String _sql = sql.toString();
    	_sql = _sql.replace("[ENTITY]", HEventData.class.getName());
    	_sql = _sql.replace(":eventID", "" + event.getEventID() + "");
    	// creates query without setting parameters
    	Query query = entityManager.createQuery(_sql);
    	
    	//
    	// the snippet code below does not work as the DataNucleus implementation (3.2.0-release) 
    	// for HBase has a bug on cached parameters.
    	//
		// Query query = entityManager.createQuery("select d from " + HEventData.class.getName() + " as d where d.eventID = :eventID");
		// query.setParameter("eventID", event.getEventID());
    	
		// remove event associated data
		List<HEventData> hdata = new ArrayList<HEventData>(query.getResultList());
		for (HEventData _hdata : hdata) {
			// remove event	data elements	
			long ini = System.nanoTime();
			entityManager.remove(_hdata);
			long end = System.nanoTime();
			stats.writeStat(Stats.Operation.DELETE, HEventData.class, _hdata, ini, end);
			logger.debug("Event data elements " + _hdata + " removed successfully.");			
		}
		logger.debug("Event data elements of event " + event + " removed successfully.");
	}	
	/**
	 * Saves the {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.Event}
	 * entity object associated to the data base.
	 * 
	 * @param event {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.Event}
	 * entity object to save.
	 * @throws IllegalArgumentException if an illegal argument error occurred.
	 * @throws TransactionRequiredException if a transaction error occurred.
	 */
    @Override
	public void save(Event event) throws IllegalArgumentException, TransactionRequiredException {
		logger.debug("Saving event " + event + "...");	
		/* save process instance */
		processInstanceDAO.save(event.getProcessInstance());
		/* save activity instance */
		if (event.getActivityInstance() != null)
			activityInstanceDAO.save(event.getActivityInstance());	
		HEvent hevent = BusinessObjectAssembler.getInstance().toEntity(event);
		// insert event		
		long ini = System.nanoTime();
		entityManager.persist(hevent);
		long end = System.nanoTime();
		stats.writeStat(Stats.Operation.WRITE, HEvent.class, hevent, ini, end);
		// updates the current event back with the new assigned identifier
		entityManager.refresh(hevent);
		event.setEventID(hevent.getEventID());
		// save correlations
		saveCorrelations(event);
		// save payload
		savePayload(event);
		// save data
		saveData(event);		
		logger.debug("Event " + event + " saved successfully.");
	}	
	/**
	 * Load the {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.Event#getPayload()} associated
	 * from the data base. This is needed due to the fetch property is set to lazy, and the 
	 * object is loaded out of synchronism when the {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.Event}
	 * is loaded out of the same transaction.
	 * 
	 * @param event {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.Event} to load the event payload on.
	 * @throws IllegalArgumentException if an illegal argument error occurred.
	 */
    @Override
	@SuppressWarnings("unchecked")
	public void loadPayload(Event event) throws IllegalArgumentException {
		if (event == null) {
			logger.warn("Cannot load event payload from a non existing (null) event.");
			return;
		}
		logger.debug("Loading event data elements from event " + event + " ...");				
    	//
    	// JPQL parameters must be explicitly set as string values due to a bug  
    	// found on DataNucleus implementation (3.2.0-release) for HBase when 
    	// caching previous query parameters. 
    	//
    	StringBuffer sql = new StringBuffer("select p from [ENTITY] p ");
    	sql.append("where p.eventID = :eventID ");
    	// set parameters
    	String _sql = sql.toString();
    	_sql = _sql.replace("[ENTITY]", HEventPayload.class.getName());
    	_sql = _sql.replace(":eventID", "" + event.getEventID() + "");
    	// creates query without setting parameters
    	Query query = entityManager.createQuery(_sql);
    	
    	//
    	// the snippet code below does not work as the DataNucleus implementation (3.2.0-release) 
    	// for HBase has a bug on cached parameters.
    	//
		// Query query = entityManager.createQuery("select p from " + HEventPayload.class.getName() + " as p where p.eventID = :eventID");
		// query.setParameter("eventID", event.getEventID());
		Set<HEventPayload> hpayload = new HashSet<HEventPayload>(query.getResultList());
		for (HEventPayload _hpayload : hpayload) {
			EventPayload payload = BusinessObjectAssembler.getInstance().toBusinessObject(_hpayload, event);
			event.getPayload().add(payload);
		}		
		logger.debug("Payload from event " + event + " loaded successfully.");				
	}	
	/**
	 * Load the {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.Event#getCorrelations()} associated
	 * from the data base. This is needed due to the fetch property is set to lazy, and the 
	 * object is loaded out of synchronism when the {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.Event}
	 * is loaded out of the same transaction.
	 * 
	 * @param event {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.Event} to load the event correlation data on.
	 * @throws IllegalArgumentException if an illegal argument error occurred.
	 */
    @Override
	@SuppressWarnings("unchecked")
	public void loadCorrelation(Event event) throws IllegalArgumentException {
		if (event == null) {
			logger.warn("Cannot load event correlation data from a non existing (null) event.");
			return;
		}
		logger.debug("Loading event correlation data from event " + event + " ...");				
    	//
    	// JPQL parameters must be explicitly set as string values due to a bug  
    	// found on DataNucleus implementation (3.2.0-release) for HBase when 
    	// caching previous query parameters. 
    	//
    	StringBuffer sql = new StringBuffer("select c from [ENTITY] c ");
    	sql.append("where c.eventID = :eventID ");
    	// set parameters
    	String _sql = sql.toString();
    	_sql = _sql.replace("[ENTITY]", HEventCorrelation.class.getName());
    	_sql = _sql.replace(":eventID", "" + event.getEventID() + "");
    	// creates query without setting parameters
    	Query query = entityManager.createQuery(_sql);
    	
    	//
    	// the snippet code below does not work as the DataNucleus implementation (3.2.0-release) 
    	// for HBase has a bug on cached parameters.
    	//
		// Query query = entityManager.createQuery("select c from " + HEventCorrelation.class.getName() + " as c where c.eventID = :eventID");
		// query.setParameter("eventID", event.getEventID());
		Set<HEventCorrelation> hcorrelations = new HashSet<HEventCorrelation>(query.getResultList());
		for (HEventCorrelation _hcorrelation : hcorrelations) {
			EventCorrelation correlation = BusinessObjectAssembler.getInstance().toBusinessObject(_hcorrelation, event);
			event.getCorrelations().add(correlation);
		}		
		logger.debug("Correlation data from event " + event + " loaded successfully.");				
	}	
	/**
	 * Load the {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.Event#getCorrelations()} associated
	 * from the data base. This is needed due to the fetch property is set to lazy, and the 
	 * object is loaded out of synchronism when the {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.Event}
	 * is loaded out of the same transaction.
	 * 
	 * @param event {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.Event} to load the event data elements on.
	 * @throws IllegalArgumentException if an illegal argument error occurred.
	 */
    @Override
	@SuppressWarnings("unchecked")
	public void loadData(Event event) throws IllegalArgumentException {
		if (event == null) {
			logger.warn("Cannot load event data elements from a non existing (null) event.");
			return;
		}
		logger.debug("Loading event data elements from event " + event + " ...");				
    	//
    	// JPQL parameters must be explicitly set as string values due to a bug  
    	// found on DataNucleus implementation (3.2.0-release) for HBase when 
    	// caching previous query parameters. 
    	//
    	StringBuffer sql = new StringBuffer("select d from [ENTITY] d ");
    	sql.append("where d.eventID = :eventID ");
    	// set parameters
    	String _sql = sql.toString();
    	_sql = _sql.replace("[ENTITY]", HEventData.class.getName());
    	_sql = _sql.replace(":eventID", "" + event.getEventID() + "");
    	// creates query without setting parameters
    	Query query = entityManager.createQuery(_sql);
    	
    	//
    	// the snippet code below does not work as the DataNucleus implementation (3.2.0-release) 
    	// for HBase has a bug on cached parameters.
    	//
		// Query query = entityManager.createQuery("select d from " + HEventData.class.getName() + " as d where d.eventID = :eventID");
		// query.setParameter("eventID", event.getEventID());
		Set<HEventData> hdata = new HashSet<HEventData>(query.getResultList());
		for (HEventData _hdata : hdata) {
			EventData dataElement = BusinessObjectAssembler.getInstance().toBusinessObject(_hdata, event);
			event.getDataElement().add(dataElement);
		}		
		logger.debug("Data elements from event " + event + " loaded successfully.");				
	}		
	/**
	 * Saves the {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.EventCorrelation} objects
	 * associated to {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.Event#getCorrelations()}
	 * @param event event whose correlation data is to be persisted
	 * @throws IllegalArgumentException if an illegal argument error occurred.
	 */
	private void saveCorrelations(Event event) throws IllegalArgumentException {
		logger.debug("Saving event correlations of event " + event + "...");	
		for (EventCorrelation correlation : event.getCorrelations()) {
			HEventCorrelation hcorrelation = BusinessObjectAssembler.getInstance().toEntity(correlation);
			// insert event	correlation	
			long ini = System.nanoTime();
			entityManager.persist(hcorrelation);
			long end = System.nanoTime();
			stats.writeStat(Stats.Operation.WRITE, HEventCorrelation.class, hcorrelation, ini, end);
			logger.debug("Event correlations " + correlation + " saved successfully.");
		}
        if (!event.getCorrelations().isEmpty()) {
            // insert event	correlation index
            HEventCorrelationIndex index = HIndexBuilder.getInstance().buildCorrelationIndex(event);
             // save the index if it doesn't exist
            if (entityManager.find(HEventCorrelationIndex.class, index.getCorrelationSet()) == null) {
                long ini = System.nanoTime();
                entityManager.persist(index);
                long end = System.nanoTime();
                stats.writeStat(Stats.Operation.WRITE, HEventCorrelationIndex.class, index, ini, end);
                logger.debug("Event correlations of event " + event + " indexed successfully.");
            } else {
                // TODO: add eventID to index
                long ini = System.nanoTime();
                entityManager.merge(index);
                long end = System.nanoTime();
                stats.writeStat(Stats.Operation.UPDATE, HEventCorrelationIndex.class, index, ini, end);
                logger.debug("Event correlations of event " + event + " indexed (updated) successfully.");
            }
        }
	}
	/**
	 * Saves the {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.EventCorrelation} objects
	 * associated to {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.Event#getCorrelations()}
	 * @param event event whose payload data is to be persisted
	 * @throws IllegalArgumentException if an illegal argument error occurred.
	 */
	private void savePayload(Event event) throws IllegalArgumentException {
		logger.debug("Saving event payload data of event " + event + "...");	
		for (EventPayload payload : event.getPayload()) {
			HEventPayload hpayload = BusinessObjectAssembler.getInstance().toEntity(payload);
			// insert event	payload	
			long ini = System.nanoTime();
			entityManager.persist(hpayload);
			long end = System.nanoTime();
			stats.writeStat(Stats.Operation.WRITE, HEventPayload.class, hpayload, ini, end);
			logger.debug("Event correlation " + payload + " saved successfully.");			
		}
		logger.debug("Event correlations of event " + event + " saved successfully.");		
	}	
	/**
	 * Saves the {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.EventData} objects
	 * associated to {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.Event#getDataElement()}
	 * @param event event whose data elements are to be persisted
	 * @throws IllegalArgumentException if an illegal argument error occurred.
	 */
	private void saveData(Event event) throws IllegalArgumentException {
		logger.debug("Saving event data elements of event " + event + "...");	
		for (EventData dataElement : event.getDataElement()) {
			HEventData hdata = BusinessObjectAssembler.getInstance().toEntity(dataElement);
			// insert event	data	
			long ini = System.nanoTime();
			entityManager.persist(hdata);
			long end = System.nanoTime();
			stats.writeStat(Stats.Operation.WRITE, HEventData.class, hdata, ini, end);
			logger.debug("Event data element " + dataElement + " saved successfully.");			
		}
		logger.debug("Event data elements of event " + event + " saved successfully.");		
	}	
}
