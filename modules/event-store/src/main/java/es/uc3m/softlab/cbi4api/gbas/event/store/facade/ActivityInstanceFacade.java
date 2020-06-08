/* 
 * $Id: ActivityInstanceFacade.java,v 1.0 2011-10-28 12:29:27 averab Exp $
 *
 * @copyright Universidad Carlos III de Madrid. proprietary/confidential. Use is subject to license terms.
 */
package es.uc3m.softlab.cbi4api.gbas.event.store.facade;

import es.uc3m.softlab.cbi4api.gbas.event.store.StaticResources;
import es.uc3m.softlab.cbi4api.gbas.event.store.domain.ActivityInstance;
import es.uc3m.softlab.cbi4api.gbas.event.store.domain.Model;
import es.uc3m.softlab.cbi4api.gbas.event.store.domain.EventCorrelation;
import es.uc3m.softlab.cbi4api.gbas.event.store.domain.Model;

import java.util.List;
import java.util.Set;

/**
 * Facade design pattern interface for the  {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.ActivityInstance}  entity
 * object. This interface defines all methods for accessing to the <strong>ActivityInstance</strong> entity data 
 * throughout the Spring framework.
 * 
 * @author averab
 * @version 1.0.0
 */
public interface ActivityInstanceFacade {
 	/** Spring component name */
    public static final String COMPONENT_NAME = StaticResources.COMPONENT_NAME_ACTIVITY_INSTANCE_FACADE;        

	/**
	 * Gets the {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.ActivityInstance} entity
	 * object associated to the  
	 * {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.ActivityInstance#getId()} as primary key.
	 * 
	 * @param id activity instance's identifier
	 * @return {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.ActivityInstance} entity
	 * object associated.
	 */
    public ActivityInstance getActivityInstance(long id);
	/**
	 * Gets the {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.ActivityInstance} entity
	 * object associated to the  
	 * ({@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.ActivityInstance#getInstanceSrcId()} and
	 *  {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.Model#getSource()} retrieve from
	 *  {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.ActivityInstance#getModel()})
	 * as unique keys.
	 * 
	 * @param activityId activity instance identifier given at the original source.
	 * @param source activity instance model.
	 * @return {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.ActivityInstance} entity object associated.
	 * @throws ActivityInstanceException if any activity instance error occurred.
	 */
    public ActivityInstance getActivityInstance(String activityId, Model model) throws ActivityInstanceException;   
	/**
	 * Gets the {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.ActivityInstance} entity
	 * object associated to the correlation information provided by a determined list of
	 * ({@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.EventCorrelation} objects,
	 * a determined {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.Model}
	 * and a determined ({@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.Source} given by
	 * the {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.Model#getSource()}) property.
	 * 
	 * @param correlation list of event correlation objects associated to the activity instance that is trying to be found.
	 * @param model activity model associated to the activity instance that is trying to be found.
	 * @return {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.ActivityInstance} entity object associated.
	 * @throws ActivityInstanceException if any activity instance error occurred.
	 */
    public ActivityInstance getActivityInstance(Set<EventCorrelation> correlation, Model model) throws ActivityInstanceException;
	/**
	 * Loads the {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.ActivityInstance#events} associated
	 * from the data base. This is needed due to the fetch property is set to lazy, and their associated 
	 * objects are loaded out of synchronism when the {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.ActivityInstance}
	 * is initially loaded within a JTA transaction.
	 * 
	 * @param instance {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.ActivityInstance} to load the events on.
	 * @throws ActivityInstanceException if any illegal data access or inconsistent activity instance data error occurred.
	 */	
    public void loadEvents(ActivityInstance instance) throws ActivityInstanceException;    
	/**
	 * Gets all {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.ActivityInstance} entity
	 * objects defined at the data base.
	 * 
	 * @return all {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.ActivityInstance} entity
	 * objects defined at the data base.
	 */
	public List<ActivityInstance> getAll();
	/**
	 * Saves and synchronizes the {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.ActivityInstance}
	 * entity object state with the data base.
	 * 
	 * @param instance {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.ActivityInstance}
	 * entity object to save.
	 * @throws ActivityInstanceException if any activity instance error occurred.
	 */
	public void saveActivityInstance(ActivityInstance instance) throws ActivityInstanceException;
	/**
	 * Updates and synchronizes the {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.ActivityInstance}
	 * entity object state with the data base.
	 * 
	 * @param instance {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.ActivityInstance}
	 * entity object to update.
	 * @throws ActivityInstanceException if any illegal data access or inconsistent activity instance data error occurred.
	 */
	public void updateActivityInstance(ActivityInstance instance) throws ActivityInstanceException;
	/**
	 * Removes and synchronizes the {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.ActivityInstance}
	 * entity object state with the data base.
	 * 
	 * @param instance {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.ActivityInstance}
	 * entity object to delete.
	 * @throws ActivityInstanceException if any illegal data access or inconsistent activity instance data error occurred.
	 */
	public void deleteActivityInstance(ActivityInstance instance) throws ActivityInstanceException;
}