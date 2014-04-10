/* 
 * $Id: ProcessInstanceFacade.java,v 1.0 2011-10-28 12:29:27 averab Exp $
 *
 * @copyright Universidad Carlos III de Madrid. proprietary/confidential. Use is subject to license terms.
 */
package es.uc3m.softlab.cbi4api.gbas.event.store.facade;

import es.uc3m.softlab.cbi4api.gbas.event.store.StaticResources;
import es.uc3m.softlab.cbi4api.gbas.event.store.domain.EventCorrelation;
import es.uc3m.softlab.cbi4api.gbas.event.store.domain.Model;
import es.uc3m.softlab.cbi4api.gbas.event.store.domain.ProcessInstance;

import java.util.List;
import java.util.Set;

/**
 * Facade design pattern interface for the  {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.ProcessInstance}  entity
 * object. This interface defines all methods for accessing to the <strong>ProcessInstance</strong> entity data 
 * throughout the Spring framework.
 * 
 * @author averab
 * @version 1.0.0
 */
public interface ProcessInstanceFacade {
 	/** Spring component name */
    public static final String COMPONENT_NAME = StaticResources.COMPONENT_NAME_PROCESS_INSTANCE_FACADE;        

	/**
	 * Gets the {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.ProcessInstance} entity
	 * object associated to the  
	 * {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.ProcessInstance#getId()} as primary key.
	 * 
	 * @param id process instance's identifier
	 * @return {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.ProcessInstance} entity
	 * object associated.
	 */
    public ProcessInstance getProcessInstance(long id);
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
    public ProcessInstance getProcessInstance(String processId, Model model) throws ProcessInstanceException;
	/**
	 * Gets the {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.ProcessInstance} entity
	 * object associated to the correlation information provided by a determined list of
	 * ({@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.EventCorrelation} objects,
	 * a particular {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.Model}
	 * and a particular ({@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.Source} given by
	 * the {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.Model#getSource()}) property.
	 * 
	 * @param model process model associated to the process instance that is trying to be found.
	 * @param correlation list of event correlation objects associated to the process instance that is trying to be found.
	 * @return {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.ProcessInstance} entity object associated.
	 * @throws ProcessInstanceException if any process instance error occurred.
	 */
    public ProcessInstance getProcessInstance(Model model, Set<EventCorrelation> correlation) throws ProcessInstanceException;    
	/**
	 * Gets all {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.ProcessInstance} entity
	 * objects defined at the data base.
	 * 
	 * @return all {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.ProcessInstance} entity
	 * objects defined at the data base.
	 */
	public List<ProcessInstance> getAll();
	/**
	 * Saves and synchronizes the {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.ProcessInstance}
	 * entity object state with the data base.
	 * 
	 * @param instance {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.ProcessInstance}
	 * entity object to save.
	 * @throws ProcessInstanceException if any process instance error occurred.
	 */
	public void saveProcessInstance(ProcessInstance instance) throws ProcessInstanceException;
	/**
	 * Updates and synchronizes the {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.ProcessInstance}
	 * entity object state with the data base.
	 * 
	 * @param instance {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.ProcessInstance}
	 * entity object to update.
	 * @throws ProcessInstanceException if any illegal data access or inconsistent process instance data error occurred.
	 */
	public void updateProcessInstance(ProcessInstance instance) throws ProcessInstanceException;
	/**
	 * Removes and synchronizes the {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.ProcessInstance}
	 * entity object state with the data base.
	 * 
	 * @param instance {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.ProcessInstance}
	 * entity object to delete.
	 * @throws ProcessInstanceException if any illegal data access or inconsistent process instance data error occurred.
	 */
	public void deleteProcessInstance(ProcessInstance instance) throws ProcessInstanceException;
	/**
	 * Loads the {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.ProcessInstance#events} associated
	 * from the data base. This is needed due to the fetch property is set to lazy, and their associated 
	 * objects are loaded out of synchronism when the {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.ProcessInstance}
	 * is initially loaded within a JTA transaction.
	 * 
	 * @param instance {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.ProcessInstance} to load the events on.
	 * @throws ProcessInstanceException if any illegal data access or inconsistent process instance data error occurred.
	 */	
    public void loadEvents(ProcessInstance instance) throws ProcessInstanceException;
}