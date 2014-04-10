/* 
 * $Id: ProcessInstanceFacadeImpl.java,v 1.0 2013-01-27 23:29:05 averab Exp $
 *
 * @copyright Universidad Carlos III de Madrid. proprietary/confidential. Use is subject to license terms.
 */
package es.uc3m.softlab.cbi4api.gbas.event.store.facade;

import es.uc3m.softlab.cbi4api.gbas.event.store.StaticResources;
import es.uc3m.softlab.cbi4api.gbas.event.store.dao.ProcessInstanceDAO;
import es.uc3m.softlab.cbi4api.gbas.event.store.domain.EventCorrelation;
import es.uc3m.softlab.cbi4api.gbas.event.store.domain.Model;
import es.uc3m.softlab.cbi4api.gbas.event.store.domain.ProcessInstance;

import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Facade design pattern class implementation for the {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.ProcessInstance}
 * entity object. This class manages all data access throughout the Spring framework.
 * 
 * @author averab 
 * @version 1.0.0 
 */
@Transactional
@Service(value=ProcessInstanceFacade.COMPONENT_NAME)
public class ProcessInstanceFacadeImpl implements ProcessInstanceFacade {
    /** Logger for tracing */
    private transient Logger logger = Logger.getLogger(ProcessInstanceFacadeImpl.class);
    /** Entity manager bound to this session bean throughout the JTA */
    @PersistenceContext(unitName=StaticResources.PERSISTENCE_UNIT_NAME_EVENT_STORE)
    protected EntityManager entityManager;
    /** Process instance data access object */
    @Autowired private ProcessInstanceDAO processInstanceDAO;
    
	/**
	 * Gets the {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.ProcessInstance} entity
	 * object associated to the  
	 * {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.ProcessInstance#getId()} as primary key.
	 * 
	 * @param id process instance's identifier
	 * @return {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.ProcessInstance} entity
	 * object associated.
	 */
    public ProcessInstance getProcessInstance(long id) {
		logger.debug("Retrieving model with identifier " + id + "...");
		ProcessInstance instance = processInstanceDAO.findById(id);
		if (instance == null) {
			logger.warn("Cannot get model. Process instance with identifier: " + id + " does not exist.");
		}
		logger.debug("Process instance " + instance + " retrieved successfully.");
		return instance;
	}
	/**
	 * Gets the {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.ProcessInstance} entity
	 * object associated to the  
	 * ({@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.ProcessInstance#getInstanceSrcId()} and
	 *  {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.Model#getSource()} retrieve from
	 *  {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.ProcessInstance#getModel()})
	 * as unique keys.
	 * 
	 * @param processId process instance identifier given at the original source.
	 * @param model process instance model.
	 * @return {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.ProcessInstance} entity object associated.
	 * @throws ProcessInstanceException if any process instance error occurred.
	 */
    public ProcessInstance getProcessInstance(String processId, Model model) throws ProcessInstanceException {
		if (processId == null) 
			throw new ProcessInstanceException(StaticResources.WARN_GET_PROCESS_INSTANCE_WITHOUT_INSTANCE_SRC_ID,"Cannot retrieve process instance if the source process instance id is not properly provided.");
		if (model.getSource() == null) 
			throw new ProcessInstanceException(StaticResources.WARN_GET_PROCESS_INSTANCE_WITHOUT_SOURCE,"Cannot retrieve process instance if the source is not properly provided.");
		
		logger.debug("Retrieving process instance with source data as pairs of (" + processId + ", " + model.getSource() + ")...");
		/* retrieve the process instance associated with the source information provided */
		ProcessInstance instance = processInstanceDAO.findBySourceData(processId, model);
		if (instance == null) {
			logger.debug("Cannot get process instance. Process instance with source data as pairs of (" + processId + ", " + model.getSource() + ") does not exist.");
		}
		logger.debug("Process instance " + instance + " retrieved successfully.");
		return instance;
	}  
	/**
	 * Gets the {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.ProcessInstance} entity
	 * object associated to the correlation information provided by a determined list of
	 * ({@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.EventCorrelation} objects,
	 * a determined {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.Model}
	 * and a determined ({@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.Source} given by
	 * the {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.Model#getSource()}) property.
	 * 
	 * @param model process model associated to the process instance that is trying to be found.
	 * @param correlation list of event correlation objects associated to the process instance that is trying to be found.
	 * @return {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.ProcessInstance} entity object associated.
	 * @throws ProcessInstanceException if any process instance error occurred.
	 */
    public ProcessInstance getProcessInstance(Model model, Set<EventCorrelation> correlation) throws ProcessInstanceException {
		logger.debug("Retrieving process instance associted to a determined correlation data from the model " + model + " and source associated...");
		if (correlation == null || correlation.isEmpty()) 
			throw new ProcessInstanceException(StaticResources.WARN_GET_PROCESS_INSTANCE_WITHOUT_CORRELATION_DATA,"Cannot retrieve process instance if the correlation data is not properly provided.");		
		if (model == null) 
			throw new ProcessInstanceException(StaticResources.WARN_GET_PROCESS_INSTANCE_WITHOUT_PROCESS_MODEL_ID,"Cannot retrieve process instance if the process model is not properly provided.");
		if (model.getSource() == null) 
			throw new ProcessInstanceException(StaticResources.WARN_GET_PROCESS_INSTANCE_WITHOUT_SOURCE,"Cannot retrieve process instance if the source is not properly provided.");
		/* retrieve the process instance associated with the correlation information provided */
		ProcessInstance instance = processInstanceDAO.findByCorrelationData(correlation, model);
		if (instance == null) {
			logger.debug("Cannot get process instance. Process instance associted to a determined correlation data from the model " + model + " and associated source " + model.getSource() + " does not exist.");
		}
		logger.debug("Process instance " + instance + " retrieved successfully.");
		return instance;
	}     
	/**
	 * Loads the {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.ProcessInstance#events} associated
	 * from the data base. This is needed due to the fetch property is set to lazy, and their associated 
	 * objects are loaded out of synchronism when the {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.ProcessInstance}
	 * is initially loaded within a JTA transaction.
	 * 
	 * @param instance {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.ProcessInstance} to load the events on.
	 * @throws ProcessInstanceException if any illegal data access or inconsistent process instance data error occurred.
	 */	
    public void loadEvents(ProcessInstance instance) throws ProcessInstanceException {
		logger.debug("Loading events on process instance '" + instance + "'...");
		ProcessInstance _instance = processInstanceDAO.findById(instance.getId());
		if (_instance == null) {
			logger.debug("Cannot load events. Process instance with identifier: '" + instance + "' does not exist.");
			throw new ProcessInstanceException(StaticResources.WARN_LOAD_EVENT_PROCESS_INSTANCE_NOT_EXIST, "Cannot load events. Process instance '" + instance + "' does not exist.");
		}
		processInstanceDAO.loadEvents(instance);
		logger.debug("Events from process instance " + instance + " loaded successfully.");
	}        
	/**
	 * Gets all {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.ProcessInstance} entity
	 * objects defined at the data base.
	 * 
	 * @return all {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.ProcessInstance} entity
	 * objects defined at the data base.
	 */
	public List<ProcessInstance> getAll() {
		logger.debug("Retrieving all process instances...");
		List<ProcessInstance> instances = processInstanceDAO.findAll();
		logger.debug("Process instances retrieved successfully.");
		return instances;
	}      
	/**
	 * Saves and synchronizes the {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.ProcessInstance}
	 * entity object state with the data base.
	 * 
	 * @param instance {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.ProcessInstance}
	 * entity object to save.
	 * @throws ProcessInstanceException if any illegal data access or inconsistent process instance data error occurred.
	 */
	public void saveProcessInstance(ProcessInstance instance) throws ProcessInstanceException {
		logger.debug("Saving the process instance " + instance + "...");		
		if (instance == null) {
			logger.warn("Cannot save an empty process instance. Trying to save a null process instance.");
			throw new ProcessInstanceException(StaticResources.WARN_SAVE_NULL_PROCESS_INSTANCE, "Cannot save an empty process instance. Trying to save a null process instance.");
		}
		if (instance.getInstanceSrcId() == null) {
			logger.warn("Cannot save process instance. Trying to save a process instance without a original source process instance identifier.");
			throw new ProcessInstanceException(StaticResources.WARN_SAVE_PROCESS_INSTANCE_WITHOUT_PROCESS_SRC_ID, "Cannot save process instance. Trying to save a process instance without a original source process instance identifier.");
		}
		if (instance.getModel() == null) {
			logger.warn("Cannot save process instance. Trying to save a process instance without a process definition model.");
			throw new ProcessInstanceException(StaticResources.WARN_SAVE_PROCESS_INSTANCE_WITHOUT_MODEL, "Cannot save process instance. Trying to save a process instance without a process definition model.");
		}
		if (instance.getModel().getSource() == null) {
			logger.warn("Cannot save process instance. Trying to save a process instance without a original source identification.");
			throw new ProcessInstanceException(StaticResources.WARN_SAVE_PROCESS_INSTANCE_WITHOUT_SOURCE, "Cannot save process instance. Trying to save a process instance without a original source identification.");
		}
		ProcessInstance _instance = processInstanceDAO.findBySourceData(instance.getInstanceSrcId(), instance.getModel());
		if (_instance != null) {
			logger.warn("Cannot save process instance. Process instance with source data as pairs of (source instance id [" + instance.getInstanceSrcId() + "] and source of process definition model [" + instance.getModel().getSource() + "]) already exists.");
			throw new ProcessInstanceException(StaticResources.WARN_SAVE_PROCESS_INSTANCE_WITH_SOURCE_DATA_ALREADY_EXISTS, "Cannot save process instance. Process instance with source data as pairs of (source instance id [" + instance.getInstanceSrcId() + "] and source of process definition model [" + instance.getModel().getSource() + "]) already exists.");
		}
		processInstanceDAO.save(instance);		
		logger.info("Process instance " + instance + " saved successfully.");
	}
	/**
	 * Updates and synchronizes the {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.ProcessInstance}
	 * entity object state with the data base.
	 * 
	 * @param instance {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.ProcessInstance}
	 * entity object to update.
	 * @throws ProcessInstanceException if any illegal data access or inconsistent process instance data error occurred.
	 */
	public void updateProcessInstance(ProcessInstance instance) throws ProcessInstanceException {
		logger.debug("Updating process instance " + instance + "...");	
		ProcessInstance _instance = processInstanceDAO.findById(instance.getId());
		if (_instance == null) {
			logger.warn("Cannot update process instance. Process instance with identifier: " + instance.getId() + " does not exist.");
			throw new ProcessInstanceException(StaticResources.WARN_UPDATE_PROCESS_INSTANCE_NOT_EXIST, "Cannot update process instance. Process instance with identifier: " + instance.getId() + " does not exist.");
		}		
		processInstanceDAO.merge(instance);		
		logger.info("Process instance " + instance + " updated successfully.");
	}     
	/**
	 * Removes and synchronizes the {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.ProcessInstance}
	 * entity object state with the data base.
	 * 
	 * @param instance {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.ProcessInstance}
	 * entity object to delete.
	 * @throws ProcessInstanceException if any illegal data access or inconsistent process instance data error occurred.
	 */
	public void deleteProcessInstance(ProcessInstance instance) throws ProcessInstanceException {
		logger.debug("Removing process instance " + instance + "...");	
		ProcessInstance _instance = processInstanceDAO.findById(instance.getId());
		if (_instance == null) {
			logger.warn("Cannot remove process instance. Process instance with identifier: " + instance.getId() + " does not exist.");
			throw new ProcessInstanceException(StaticResources.WARN_DELETE_PROCESS_INSTANCE_NOT_EXIST, "Cannot remove process instance. Process instance with identifier: " + instance.getId() + " does not exist.");
		}
		processInstanceDAO.delete(_instance);		
		logger.info("Process instance " + _instance + " removed successfully.");
	}
}