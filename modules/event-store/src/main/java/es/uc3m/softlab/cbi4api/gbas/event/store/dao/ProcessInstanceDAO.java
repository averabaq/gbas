/* 
 * $Id: ProcessInstanceDAO.java,v 1.0 2013-01-27 23:29:05 averab Exp $
 *
 * @copyright Universidad Carlos III de Madrid. proprietary/confidential. Use is subject to license terms.
 */
package es.uc3m.softlab.cbi4api.gbas.event.store.dao;

import es.uc3m.softlab.cbi4api.gbas.event.store.StaticResources;
import es.uc3m.softlab.cbi4api.gbas.event.store.domain.EventCorrelation;
import es.uc3m.softlab.cbi4api.gbas.event.store.domain.Model;
import es.uc3m.softlab.cbi4api.gbas.event.store.domain.ProcessInstance;

import java.util.List;
import java.util.Set;

import javax.persistence.TransactionRequiredException;

/**
 * Data access object interface for the {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.ProcessInstance}
 * entity object. This interface defines all methods for accessing to the 
 * <strong>ProcessInstance</strong> entity data through the JPA persistence layer.
 * 
 * @author averab
 * @version 1.0.0
 */
public interface ProcessInstanceDAO {
 	/** Local jndi name */
    public static final String COMPONENT_NAME = StaticResources.COMPONENT_NAME_PROCESS_INSTANCE_DAO;        

	/**
	 * Find all {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.ProcessInstance} entity
	 * objects.
	 * 
	 * @return all {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.ProcessInstance} entity
	 * objects.
	 * @throws IllegalArgumentException if an illegal argument error occurred.
	 */    
    public List<ProcessInstance> findAll() throws IllegalArgumentException;
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
	public ProcessInstance findById(long id) throws IllegalArgumentException;
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
    public ProcessInstance findBySourceData(String processId, Model model) throws IllegalArgumentException;
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
    public ProcessInstance findByCorrelationData(Set<EventCorrelation> correlation, Model model) throws IllegalArgumentException;
    /**
	 * Merges and synchronizes the {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.ProcessInstance}
	 * entity object state with the data base.
	 * 
	 * @param instance {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.ProcessInstance}
	 * entity object to merge.
	 * @throws IllegalArgumentException if an illegal argument error occurred.
	 * @throws TransactionRequiredException if a transaction error occurred.
	 */
	public void merge(ProcessInstance instance) throws IllegalArgumentException, TransactionRequiredException;
	/**
	 * Deletes the {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.ProcessInstance}
	 * entity object associated from the data base.
	 * 
	 * @param instance {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.ProcessInstance}
	 * entity object to delete.
	 * @throws IllegalArgumentException if an illegal argument error occurred.
	 * @throws TransactionRequiredException if a transaction error occurred.
	 */	
	public void delete(ProcessInstance instance) throws IllegalArgumentException, TransactionRequiredException;
	/**
	 * Saves the {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.ProcessInstance}
	 * entity object associated to the data base.
	 * 
	 * @param instance {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.ProcessInstance}
	 * entity object to save.
	 * @throws IllegalArgumentException if an illegal argument error occurred.
	 * @throws TransactionRequiredException if a transaction error occurred.
	 */	
	public void save(ProcessInstance instance) throws IllegalArgumentException, TransactionRequiredException;
	/**
	 * Loads the {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.ProcessInstance#events} associated
	 * from the data base. This is needed due to the fetch property is set to lazy, and their associated 
	 * objects are loaded out of synchronism when the {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.ProcessInstance}
	 * is initially loaded within a JTA transaction.
	 * 
	 * @param instance {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.ProcessInstance} to load the events on.
	 * @throws IllegalArgumentException if an illegal argument error occurred.
	 */	
	public void loadEvents(ProcessInstance instance) throws IllegalArgumentException;	
}