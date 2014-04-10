/* 
 * $Id: ActivityInstanceDAO.java,v 1.0 2013-01-27 23:29:05 averab Exp $
 *
 * @copyright Universidad Carlos III de Madrid. proprietary/confidential. Use is subject to license terms.
 */
package es.uc3m.softlab.cbi4api.gbas.event.store.dao;

import es.uc3m.softlab.cbi4api.gbas.event.store.StaticResources;
import es.uc3m.softlab.cbi4api.gbas.event.store.domain.ActivityInstance;
import es.uc3m.softlab.cbi4api.gbas.event.store.domain.Model;
import es.uc3m.softlab.cbi4api.gbas.event.store.domain.EventCorrelation;
import es.uc3m.softlab.cbi4api.gbas.event.store.domain.Model;

import java.util.List;
import java.util.Set;

import javax.persistence.TransactionRequiredException;

/**
 * Data access object interface for the {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.ActivityInstance}
 * entity object. This interface defines all methods for accessing to the 
 * <strong>ActivityInstance</strong> entity data through the JPA persistence layer.
 * 
 * @author averab
 * @version 1.0.0
 */
public interface ActivityInstanceDAO {
 	/** Local jndi name */
    public static final String COMPONENT_NAME = StaticResources.COMPONENT_NAME_ACTIVITY_INSTANCE_DAO;        

	/**
	 * Find all {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.ActivityInstance} entity
	 * objects.
	 * 
	 * @return all {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.ActivityInstance} entity
	 * objects.
	 * @throws IllegalArgumentException if an illegal argument error occurred.
	 */    
    public List<ActivityInstance> findAll() throws IllegalArgumentException;
	/**
	 * Find the {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.ActivityInstance} entity
	 * object associated to the  
	 * {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.ActivityInstance#getId()} as primary key.
	 * 
	 * @param id instance's identifier
	 * @return {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.ActivityInstance} entity
	 * object associated.
	 * @throws IllegalArgumentException if an illegal argument error occurred.
	 */
	public ActivityInstance findById(long id) throws IllegalArgumentException;
	/**
	 * Find the {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.ActivityInstance} entity
	 * object associated to the 
	 * ({@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.ActivityInstance#getInstanceSrcId()} and
	 *  {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.Model#getSource()} retrieve from
	 *  {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.ActivityInstance#getModel()})
	 * as unique keys.
	 * 
	 * @param activityId activity instance identifier given at the original source.
	 * @param model activity instance model.
	 * @return {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.ActivityInstance} entity object associated.
	 * @throws IllegalArgumentException if an illegal argument error occurred.
	 */
    public ActivityInstance findBySourceData(String activityId, Model model) throws IllegalArgumentException;	
	/**
	 * Find the {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.ActivityInstance} entity
	 * object associated to the correlation information provided by a determined list of
	 * ({@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.EventCorrelation} objects,
	 * a determined {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.Model}
	 * and a determined ({@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.Source} given by
	 * the {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.Model#getSource()}) property.
	 * 
	 * @param correlation list of event correlation objects associated to the activity instance that is trying to be found.
	 * @param model activity model associated to the activity instance that is trying to be found.
	 * @return {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.ActivityInstance} entity object associated.
	 * @throws IllegalArgumentException if an illegal argument error occurred.
	 */
    public ActivityInstance findBySourceData(Set<EventCorrelation> correlation, Model model) throws IllegalArgumentException;    
	/**
	 * Merges and synchronizes the {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.ActivityInstance}
	 * entity object state with the data base.
	 * 
	 * @param instance {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.ActivityInstance}
	 * entity object to merge.
	 * @throws IllegalArgumentException if an illegal argument error occurred.
	 * @throws TransactionRequiredException if a transaction error occurred.
	 */
	public void merge(ActivityInstance instance) throws IllegalArgumentException, TransactionRequiredException;
	/**
	 * Deletes the {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.ActivityInstance}
	 * entity object associated from the data base.
	 * 
	 * @param instance {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.ActivityInstance}
	 * entity object to delete.
	 * @throws IllegalArgumentException if an illegal argument error occurred.
	 * @throws TransactionRequiredException if a transaction error occurred.
	 */	
	public void delete(ActivityInstance instance) throws IllegalArgumentException, TransactionRequiredException;
	/**
	 * Saves the {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.ActivityInstance}
	 * entity object associated to the data base.
	 * 
	 * @param instance {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.ActivityInstance}
	 * entity object to save.
	 * @throws IllegalArgumentException if an illegal argument error occurred.
	 * @throws TransactionRequiredException if a transaction error occurred.
	 */	
	public void save(ActivityInstance instance) throws IllegalArgumentException, TransactionRequiredException;
	/**
	 * Loads the {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.ActivityInstance#events} associated
	 * from the data base. This is needed due to the fetch property is set to lazy, and their associated 
	 * objects are loaded out of synchronism when the {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.ActivityInstance}
	 * is initially loaded within a JTA transaction.
	 * 
	 * @param instance {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.ActivityInstance} to load the events on.
	 * @throws IllegalArgumentException if an illegal argument error occurred.
	 */	
	public void loadEvents(ActivityInstance instance) throws IllegalArgumentException;	
}