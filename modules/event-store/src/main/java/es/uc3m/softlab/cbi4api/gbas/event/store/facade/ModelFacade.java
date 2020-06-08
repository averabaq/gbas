/* 
 * $Id: ModelFacade.java,v 1.0 2011-10-28 12:29:27 averab Exp $
 *
 * @copyright Universidad Carlos III de Madrid. proprietary/confidential. Use is subject to license terms.
 */
package es.uc3m.softlab.cbi4api.gbas.event.store.facade;

import es.uc3m.softlab.cbi4api.gbas.event.store.StaticResources;
import es.uc3m.softlab.cbi4api.gbas.event.store.domain.Model;
import es.uc3m.softlab.cbi4api.gbas.event.store.domain.ModelType;
import es.uc3m.softlab.cbi4api.gbas.event.store.domain.Source;

import java.util.List;

/**
 * Facade design pattern interface for the  {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.Model}  entity
 * object. This interface defines all methods for accessing to the <strong>model</strong> entity data 
 * throughout the Spring framework.
 * 
 * @author averab
 * @version 1.0.0
 */
public interface ModelFacade {
 	/** Spring component name */
    public static final String COMPONENT_NAME = StaticResources.COMPONENT_NAME_MODEL_FACADE;        

	/**
	 * Gets the {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.Model} entity
	 * object associated to the  
	 * {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.Model#getId()} as primary key.
	 * 
	 * @param id model's identifier
	 * @return {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.Model} entity
	 * object associated.
	 */
    public Model getModel(long id);
	/**
	 * Gets the {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.Model} entity
	 * object associated to the 
	 * ({@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.Model#getModelSrcId()},
	 *  {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.Model#getSource()} and
	 *  {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.Model#getType()})
	 * as unique keys.
	 * 
	 * @param modelSrcId model source identifier given at the original source.
	 * @param source model's source.
	 * @return {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.Model} entity object associated.
	 * @throws ModelException if any model error occurred.
	 */
    public Model getModel(String modelSrcId, Source source, ModelType type) throws ModelException;
	/**
	 * Gets all {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.Model} entity
	 * objects defined at the data base.
	 * 
	 * @return all {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.Model} entity
	 * objects defined at the data base.
	 */
	public List<Model> getAll();
	/**
	 * Saves and synchronizes the {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.Model}
	 * entity object state with the data base.
	 * 
	 * @param model {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.Model}
	 * entity object to save.
	 * @throws ModelException if any model error occurred.
	 */
	public void saveModel(Model model) throws ModelException;
	/**
	 * Updates and synchronizes the {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.Model}
	 * entity object state with the data base.
	 * 
	 * @param model {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.Model}
	 * entity object to update.
	 * @throws ModelException if any illegal data access or inconsistent model data error occurred.
	 */
	public void updateModel(Model model) throws ModelException;
	/**
	 * Removes and synchronizes the {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.Model}
	 * entity object state with the data base.
	 * 
	 * @param model {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.Model}
	 * entity object to delete.
	 * @throws ModelException if any illegal data access or inconsistent model data error occurred.
	 */
	public void deleteModel(Model model) throws ModelException;
}