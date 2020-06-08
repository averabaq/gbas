/* 
 * $Id: ProcessInstanceDAOImpl.java,v 1.0 2013-01-27 23:29:05 averab Exp $
 *
 * @copyright Universidad Carlos III de Madrid. proprietary/confidential. Use is subject to license terms.
 */
package es.uc3m.softlab.cbi4api.gbas.event.store.dao;

import es.uc3m.softlab.cbi4api.gbas.event.store.StaticResources;
import es.uc3m.softlab.cbi4api.gbas.event.store.Stats;
import es.uc3m.softlab.cbi4api.gbas.event.store.domain.Event;
import es.uc3m.softlab.cbi4api.gbas.event.store.domain.EventCorrelation;
import es.uc3m.softlab.cbi4api.gbas.event.store.domain.Model;
import es.uc3m.softlab.cbi4api.gbas.event.store.domain.ModelType;
import es.uc3m.softlab.cbi4api.gbas.event.store.domain.ProcessInstance;
import es.uc3m.softlab.cbi4api.gbas.event.store.entity.HEvent;
import es.uc3m.softlab.cbi4api.gbas.event.store.entity.HEventCorrelation;
import es.uc3m.softlab.cbi4api.gbas.event.store.entity.HModel;
import es.uc3m.softlab.cbi4api.gbas.event.store.entity.HProcessInstance;
import es.uc3m.softlab.cbi4api.gbas.event.store.entity.HEventCorrelationIndex;
import es.uc3m.softlab.cbi4api.gbas.event.store.entity.HIndexBuilder;
import es.uc3m.softlab.cbi4api.gbas.event.store.mapred.MapReduceInnerCorrelation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
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
 * Data access object class implementation for the <strong>ProcessInstance</strong> 
 * entity object. This class manages all data access throughout the JPA 
 * persistence layer.
 * 
 * @author averab
 * @version 1.0.0  
 */
@PersistenceUnit(name=StaticResources.PERSISTENCE_NAME_EVENT_STORE, 
		         unitName=StaticResources.PERSISTENCE_UNIT_NAME_EVENT_STORE)
@Transactional(propagation=Propagation.MANDATORY)
@Repository(value=ProcessInstanceDAO.COMPONENT_NAME)
public class ProcessInstanceDAOImpl implements ProcessInstanceDAO {
    /** Logger for tracing */
    private transient Logger logger = Logger.getLogger(getClass());
    /** Entity manager bound to this data access object within the persistence context */
    @PersistenceContext(unitName=StaticResources.PERSISTENCE_UNIT_NAME_EVENT_STORE)
    protected EntityManager entityManager;
    /** Event data access object */
    @Autowired private EventDAO eventDAO;    
    /** Model data access object */
    @Autowired private ModelDAO modelDAO;
    /** Statistical performance object */
    @Autowired private Stats stats; 

	/**
	 * Find all {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.ProcessInstance} entity
	 * objects.
	 * 
	 * @return all {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.ProcessInstance} entity
	 * objects.
	 * @throws IllegalArgumentException if an illegal argument error occurred.
	 */
    @Override
    @SuppressWarnings("unchecked")
    public List<ProcessInstance> findAll() throws IllegalArgumentException {
		logger.debug("Finding all process instances...");
		List<HProcessInstance> list = entityManager.createQuery("select i from " + HProcessInstance.class.getName() + " i order by id asc").getResultList();
		logger.debug("All process instances found successfully.");
		/* convert to business object */
		List<ProcessInstance> instances = new ArrayList<ProcessInstance>();
		for (HProcessInstance hinstance : list) {
			ProcessInstance instance = BusinessObjectAssembler.getInstance().toBusinessObject(hinstance);
			HModel hprocessModel = entityManager.find(HModel.class, hinstance.getModel());
			Model model = BusinessObjectAssembler.getInstance().toBusinessObject(hprocessModel);
			instance.setModel((Model)model); 
			instances.add(instance);
		}
		return instances;
	}
	/**
	 * Find the {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.ProcessInstance} entity
	 * object associated to the  
	 * {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.ProcessInstance#getId()} as primary key.
	 * 
	 * @param id instance's identifier
	 * @return {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.ProcessInstance} entity
	 * object associated.
	 * @throws IllegalArgumentException if an illegal argument error occurred.
	 */
    @Override
	public ProcessInstance findById(long id) throws IllegalArgumentException {
		logger.debug("Retrieving process instance with identifier " + id + "...");
		HProcessInstance hinstance = entityManager.find(HProcessInstance.class, id);	
		if (hinstance != null) {			
			ProcessInstance instance = BusinessObjectAssembler.getInstance().toBusinessObject(hinstance);
			Model model = modelDAO.findById(hinstance.getModel());
			instance.setModel((Model)model);	
			logger.debug("Process instance " + instance + " retrieved successfully.");
			return instance;
		} else {	
			logger.debug("Process instance with identifier " + id + " not found.");
		}				
		return null;
	}	    
	/**
	 * Find the {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.ProcessInstance} entity
	 * object associated to the 
	 * ({@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.ProcessInstance#getInstanceSrcId()} and
	 *  {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.Model#getSource()} retrieve from
	 *  {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.ProcessInstance#getModel()})
	 * as unique keys.
	 * 
	 * @param processId process instance identifier given at the original source.
	 * @param model process instance model.
	 * @return {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.ProcessInstance} entity object associated.
	 * @throws IllegalArgumentException if an illegal argument error occurred.
	 */
    @Override
    public ProcessInstance findBySourceData(String processId, Model model) throws IllegalArgumentException {
		logger.debug("Retrieving process instance with source data as pairs of (" + processId + ", " + model.getId() + ")...");
		Model _model = modelDAO.findBySourceData(model.getModelSrcId(), model.getSource(), ModelType.PROCESS);
		if (_model == null)
			return null;
    	//
    	// JPQL parameters must be explicitly set as string values due to a bug  
    	// found on DataNucleus implementation (3.2.0-release) for HBase when 
    	// caching previous query parameters. 
    	//
    	StringBuffer sql = new StringBuffer("select p from [ENTITY] p ");
    	sql.append("where p.instanceSrcId = :sourceId ");
    	sql.append("and p.model = :modelId ");
    	// set parameters
    	String _sql = sql.toString();
    	_sql = _sql.replace("[ENTITY]", HProcessInstance.class.getName());
    	_sql = _sql.replace(":sourceId", "\"" + processId + "\"");
    	_sql = _sql.replace(":modelId", "" + _model.getId() + "");
    	// creates query without setting parameters
    	Query query = entityManager.createQuery(_sql);
    	
    	//
    	// the snippet code below does not work as the DataNucleus implementation (3.2.0-release) 
    	// for HBase has a bug on cached parameters.
    	//
		// Query query = entityManager.createQuery("select p from " + HProcessInstance.class.getName() + " p where p.instanceSrcId = :sourceId and p.model = :modelId");
		// query.setParameter("sourceId", processId);
		// query.setParameter("modelId", _model.getId());			
		
    	HProcessInstance hinstance = null;
		try {
			long ini = System.nanoTime();
			hinstance = (HProcessInstance)query.getSingleResult();
			long end = System.nanoTime();
			stats.writeStat(Stats.Operation.READ_BY_SOURCE_DATA, HProcessInstance.class, hinstance, ini, end);		
			logger.debug("Process instance " + hinstance + " retrieved successfully.");
		} catch(NoResultException nrex) {
			logger.debug("Process instance with source data as pairs of (" + processId + ", " + model.getSource() + ") does not exist.");
			return null;
		} catch(NonUniqueResultException nurex) {
			logger.error(nurex.fillInStackTrace());
			logger.fatal("This message should never appear. Inconsistence in the database has been found. There exists two or more different local process instances for a unique pair of source and source process instances.");			
			throw new IllegalArgumentException("Inconsistence in the database has been found. There exists two or more different local process instances for a unique pair of source and source process instances.");
		}
        // gets the process instance to undertake the event correlation
        ProcessInstance instance = findById(hinstance.getId());
        logger.debug("Process instance " + instance + " retrieved successfully.");
		return instance;
	}   
	/**
	 * Find the {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.ProcessInstance} entity
	 * object associated to the correlation information provided by a determined list of
	 * ({@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.EventCorrelation} objects,
	 * a determined {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.Model}
	 * and a determined ({@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.Source} given by
	 * the {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.Model#getSource()}) property.
	 * 
	 * @param correlation list of event correlation objects associated to the process instance that is trying to be found.
	 * @param model process model associated to the process instance that is trying to be found.
	 * @return {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.ProcessInstance} entity object associated.
	 * @throws IllegalArgumentException if an illegal argument error occurred.
	 */
    public ProcessInstance findByCorrelationData(Model model, Set<EventCorrelation> correlation) throws IllegalArgumentException {
    	logger.debug("Retrieving process instance as pairs of model (" + model + ") and some correlation data...");
		if (model == null) 
			throw new IllegalArgumentException("Cannot find the correlated instance. Model cannot be null.");
		// checks the model existence
		Model _model = modelDAO.findById(model.getId());
		if (_model == null) 
			throw new IllegalArgumentException("The model with id " + model.getId() + " does not exist in the repository.");
		// performs a map-reduce job to get the process instance associated with this model and correlation data
		Long eventId = null;
		try {
			long ini = System.nanoTime();
			eventId = MapReduceInnerCorrelation.perform(_model.getId(), correlation);
			long end = System.nanoTime();
			stats.writeStat(Stats.Operation.MAP_REDUCE_INNER_PROCESS_CORRELATION, MapReduceInnerCorrelation.class, _model, ini, end);	
		} catch (Exception ex) {
			logger.error(ex.fillInStackTrace());
			throw new IllegalArgumentException(ex);
		}
		// if any process instance was found
		ProcessInstance instance = null;
		if (eventId != null) {
			Event event = eventDAO.findById(eventId);
			instance = event.getProcessInstance();			
		}
		return instance;
	}   
	/**
	 * Find the {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.ProcessInstance} entity
	 * object associated to the correlation information provided by a determined list of
	 * ({@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.EventCorrelation} objects,
	 * a determined {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.Model}
	 * and a determined ({@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.Source} given by
	 * the {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.Model#getSource()}) property.
	 * 
	 * @param correlation list of event correlation objects associated to the process instance that is trying to be found.
	 * @param model process model associated to the process instance that is trying to be found.
	 * @return {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.ProcessInstance} entity object associated.
	 * @throws IllegalArgumentException if an illegal argument error occurred.
	 */
    @SuppressWarnings("unchecked")
    public ProcessInstance findByCorrelationDataMP(Model model, Set<EventCorrelation> correlation) throws IllegalArgumentException {
    	logger.debug("Retrieving process instance as pairs of model (" + model + ") and some correlation data...");
		if (model == null) 
			throw new IllegalArgumentException("Cannot find the correlated instance. Model cannot be null.");
		// checks the model existence
		Model _model = modelDAO.findById(model.getId());
		if (_model == null) 
			throw new IllegalArgumentException("The model with id " + model.getId() + " does not exist in the repository.");		
		
		HashMap<String, Set<Long>> eventKeyMap = new HashMap<String, Set<Long>>();
		for (EventCorrelation eventCorrelation : correlation) {
			//
			// JPQL parameters must be explicitly set as string values due to a bug  
			// found on DataNucleus implementation (3.2.0-release) for HBase when 
			// caching previous query parameters. 
			//
			StringBuffer sql = new StringBuffer("select c.id from [ENTITY] c ");
			sql.append("where c.key = :key ");
			sql.append("and c.value = :value ");
			sql.append("and c.processDefinitionId = :modelId ");
			sql.append("and c.activityInstance is null ");
			// set parameters
			String _sql = sql.toString();
			_sql = _sql.replace("[ENTITY]", HEventCorrelation.class.getName());
			_sql = _sql.replace(":key", "'" + eventCorrelation.getKey() + "'");
			_sql = _sql.replace(":value", "'" + eventCorrelation.getValue() + "'");
			_sql = _sql.replace(":modelId", "" + _model.getId() + "");

			// creates query without setting parameters
			Query query = entityManager.createQuery(_sql);
			List<Long> events = (List<Long>)query.getResultList();
			
			//
			// the snippet code below does not work as the DataNucleus implementation (3.2.0-release) 
			// for HBase has a bug on cached parameters.
			//
			// Query query = entityManager.createQuery("select p from " + HProcessInstance.class.getName() + " p where p.instanceSrcId = :sourceId and p.model = :modelId");
			// query.setParameter("sourceId", processId);
			// query.setParameter("modelId", _model.getId());			
		}
		
		// nested-join algorithm implementation	
		Query query = entityManager.createQuery("select c from " + HEventCorrelation.class.getName() + " c where c.key = :key and c.value = :value and c.processDefinitionId = :modelId and c.activityInstance is null");
		
		query.setParameter("modelId", _model.getId());		
		HProcessInstance hinstance = null;
		List<Long> instances = query.getResultList();
		for (Long instance : instances) {
			query = entityManager.createQuery("select e.id from " + HEvent.class.getName() + " e where e.processInstance = :processInstanceId and e.activeInstance is null");
			query.setParameter("processInstanceId", instance);	
			
		}

		//ProcessInstance instance = BusinessObjectAssembler.getInstance().toBusinessObject(hinstance);
		return null;
	}      
	/**
	 * Find the {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.ProcessInstance} entity
	 * object associated to the correlation information provided by a determined list of
	 * ({@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.EventCorrelation} objects,
	 * a determined {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.Model}
	 * and a determined ({@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.Source} given by
	 * the {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.Model#getSource()}) property.
	 * 
	 * @param correlation list of event correlation objects associated to the process instance that is trying to be found.
	 * @param model process model associated to the process instance that is trying to be found.
	 * @return {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.ProcessInstance} entity object associated.
	 * @throws IllegalArgumentException if an illegal argument error occurred.
	 */
    @Override
    public ProcessInstance findByCorrelationData(Set<EventCorrelation> correlation, Model model) throws IllegalArgumentException {
    	logger.debug("Retrieving process instance associated to a determined correlation data from the model " + model + "...");
		if (correlation == null || correlation.isEmpty()) {
			throw new IllegalArgumentException("Cannot retrieve process instance because no correlation data has been provided.");
		}
		byte[] key = HIndexBuilder.getInstance().getIndexKey(correlation, model);
		// filter by correlation set
		HEventCorrelationIndex index = entityManager.find(HEventCorrelationIndex.class, key);
		// if no index was found it means a new process instance
		if (index == null)
			return null;
		// gets the event
		HEvent event = entityManager.find(HEvent.class, index.getEventID());		
		if (model.getId() != event.getProcessDefinitionID())
			return null;
		// gets the process instance from the most recent event to undertake the event correlation 
		ProcessInstance instance = findById(event.getProcessInstance());
		logger.debug("Process instance " + instance + " retrieved successfully.");
		return instance;
	}       
	/**
	 * Merges and synchronizes the {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.ProcessInstance}
	 * entity object state with the data base.
	 * 
	 * @param instance {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.ProcessInstance}
	 * entity object to merge.
	 * @throws IllegalArgumentException if an illegal argument error occurred.
	 * @throws TransactionRequiredException if a transaction error occurred.
	 */
    @Override
	public void merge(ProcessInstance instance) throws IllegalArgumentException, TransactionRequiredException {
		logger.debug("Merging process instance " + instance + "...");
		HProcessInstance hinstance = BusinessObjectAssembler.getInstance().toEntity(instance);
		entityManager.merge(hinstance);		
		logger.debug("Process instance " + instance + " merged successfully.");
	}
	/**
	 * Deletes the {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.ProcessInstance}
	 * entity object associated from the data base.
	 * 
	 * @param instance {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.ProcessInstance}
	 * entity object to delete.
	 * @throws IllegalArgumentException if an illegal argument error occurred.
	 * @throws TransactionRequiredException if a transaction error occurred.
	 */
    @Override
	public void delete(ProcessInstance instance) throws IllegalArgumentException, TransactionRequiredException {
		logger.debug("Deleting process instance " + instance + "...");
		HProcessInstance hinstance = entityManager.find(HProcessInstance.class, instance.getId());
		if (hinstance == null) {
			logger.warn("Process instance [" + instance.getId() + "] could not be deleted. It does not exists.");
			return;
		}
		/* TODO: remove events */
		entityManager.remove(hinstance);	
		logger.debug("Process instance " + instance + " removed successfully.");
	}	
	/**
	 * Saves the {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.ProcessInstance}
	 * entity object associated to the data base.
	 * 
	 * @param instance {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.ProcessInstance}
	 * entity object to save.
	 * @throws IllegalArgumentException if an illegal argument error occurred.
	 * @throws TransactionRequiredException if a transaction error occurred.
	 */
    @Override
	public void save(ProcessInstance instance) throws IllegalArgumentException, TransactionRequiredException {
		logger.debug("Saving process instance " + instance + "...");			
		
		if (instance == null) {
			logger.warn("Cannot save process instance. Process instance is null.");
			throw new IllegalArgumentException("Cannot save process instance. Process instance is null.");
		}
		if (instance.getId() != null) {
			long ini = System.nanoTime();
			// checks for the process instance existence 
			HProcessInstance _hprocessInstance = entityManager.find(HProcessInstance.class, instance.getId());		
			long end = System.nanoTime();
			stats.writeStat(Stats.Operation.READ_BY_ID, HProcessInstance.class, _hprocessInstance, ini, end);
			if (_hprocessInstance != null) {
				logger.debug("Process instance " + instance.getId() + " cannot be persisted because it already exists.");
				return;
			}
		}
		if (instance.getInstanceSrcId() != null) {
			ProcessInstance _instance = findBySourceData(instance.getInstanceSrcId(), instance.getModel());
			if (_instance != null) {
				logger.debug("Process Instance " + _instance.getId() + " cannot be persisted because it already exists.");
				return;
			}
		}
		logger.debug("Process instance " + instance.getId() + " does not exists. Saving process instance...");
		// saves the model 	
		modelDAO.save(instance.getModel());		
		// saves the instance 
		HProcessInstance hprocessInstance = BusinessObjectAssembler.getInstance().toEntity(instance);
		long ini = System.nanoTime();
		entityManager.persist(hprocessInstance);
		long end = System.nanoTime();
		stats.writeStat(Stats.Operation.WRITE, HProcessInstance.class, hprocessInstance, ini, end);
		// updates the current instance back with the new assigned identifier
		entityManager.refresh(hprocessInstance);
		instance.setId(hprocessInstance.getId());
		logger.debug("Process instance " + instance + " saved successfully.");
	}	
	/**
	 * Loads the {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.ProcessInstance#events} associated
	 * from the data base. This is needed due to the fetch property is set to lazy, and their associated 
	 * objects are loaded out of synchronism when the {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.ProcessInstance}
	 * is initially loaded within a JTA transaction.
	 * 
	 * @param instance {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.ProcessInstance} to load the events on.
	 * @throws IllegalArgumentException if an illegal argument error occurred.
	 */
    @Override
	@SuppressWarnings("unchecked")
	public void loadEvents(ProcessInstance instance) throws IllegalArgumentException {
		if (instance == null) {
			logger.warn("Cannot load events from a non existing (null) process instance.");
			return;
		}
		if (instance.getId() == null) {
			logger.warn("Process instance does not have any identifier. Cannot load events from a non existing (null) process instance.");
			return;
		}
		logger.debug("Loading events from the process instance " + instance + " ...");				
    	//
		// TODO: modify JPQL statement to set explicit parameters
    	// the snippet code below does not work as the DataNucleus implementation (3.2.0-release) 
    	// for HBase has a bug on cached parameters.
    	//
		Query query = entityManager.createQuery("select elements(i.events) from event-store.ProcessInstance as i where i.id = :id");
		query.setParameter("id", instance.getId());
		instance.setEvents(query.getResultList());		
		logger.debug("Events from process instance '" + instance + "' loaded successfully.");				
	}		
}
