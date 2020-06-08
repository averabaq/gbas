/* 
 * $Id: ModelFacadeImpl.java,v 1.0 2013-01-27 23:29:05 averab Exp $
 *
 * @copyright Universidad Carlos III de Madrid. proprietary/confidential. Use is subject to license terms.
 */
package es.uc3m.softlab.cbi4api.gbas.event.store.facade;

import es.uc3m.softlab.cbi4api.gbas.event.store.StaticResources;
import es.uc3m.softlab.cbi4api.gbas.event.store.dao.ModelDAO;
import es.uc3m.softlab.cbi4api.gbas.event.store.domain.Model;
import es.uc3m.softlab.cbi4api.gbas.event.store.domain.ModelType;
import es.uc3m.softlab.cbi4api.gbas.event.store.domain.Source;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Facade design pattern class implementation for the {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.Model}
 * entity object. This class manages all data access throughout the Spring framework.
 * 
 * @author averab 
 * @version 1.0.0 
 */
@Transactional
@Service(value=ModelFacade.COMPONENT_NAME)
public class ModelFacadeImpl implements ModelFacade {
    /** Logger for tracing */
    private transient Logger logger = Logger.getLogger(ModelFacadeImpl.class);
    /** Entity manager bound to this session bean throughout the JTA */
    @PersistenceContext(unitName=StaticResources.PERSISTENCE_UNIT_NAME_EVENT_STORE)
    protected EntityManager entityManager;
    /** Model data access object */
    @Autowired private ModelDAO modelDAO;
    
	/**
	 * Gets the {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.Model} entity
	 * object associated to the  
	 * {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.Model#getId()} as primary key.
	 * 
	 * @param id model's identifier
	 * @return {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.Model} entity
	 * object associated.
	 */
    public Model getModel(long id) {
		logger.debug("Retrieving model with identifier " + id + "...");
		Model model = modelDAO.findById(id);
		if (model == null) {
			logger.debug("Cannot get model. Model with identifier: " + id + " does not exist.");
		}
		logger.debug("Model " + model + " retrieved successfully.");
		return model;
	}
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
    public Model getModel(String modelSrcId, Source source, ModelType type) throws ModelException {
		logger.debug("Retrieving model with source data as set of (" + modelSrcId + ", " + source + ", " + type + ")...");
		if (modelSrcId == null) 
			throw new ModelException(StaticResources.WARN_GET_MODEL_WITHOUT_MODEL_SRC_ID,"Cannot retrieve model if the source model id is not properly provided.");
		if (source == null) 
			throw new ModelException(StaticResources.WARN_GET_MODEL_WITHOUT_SOURCE,"Cannot retrieve model if the source is not properly provided.");
		if (type == null) 
			throw new ModelException(StaticResources.WARN_GET_MODEL_UNKNOWN_TYPE,"Cannot retrieve model if the model type is not properly provided.");

		Model model = modelDAO.findBySourceData(modelSrcId, source, type);
		if (model == null) {
			logger.debug("Cannot get model. Model with source data as set of (" + modelSrcId + ", " + source + ", " + type + ") does not exist.");
		}
		logger.debug("Model " + model + " retrieved successfully.");
		return model;
	}    
	/**
	 * Gets all {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.Model} entity
	 * objects defined at the data base.
	 * 
	 * @return all {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.Model} entity
	 * objects defined at the data base.
	 */
	public List<Model> getAll() {
		logger.debug("Retrieving all models...");
		List<Model> models = modelDAO.findAll();
		logger.debug("Models retrieved successfully.");
		return models;
	}      
	/**
	 * Saves and synchronizes the {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.Model}
	 * entity object state with the data base.
	 * 
	 * @param model {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.Model}
	 * entity object to save.
	 * @throws ModelException if any model error occurred.
	 */
	public void saveModel(Model model) throws ModelException {
		logger.debug("Saving the model " + model + "...");		
		if (model == null) {
			logger.warn("Cannot save model. Trying to save a null model.");
			throw new ModelException(StaticResources.WARN_SAVE_NULL_MODEL, "Cannot save model. Trying to save a null model.");
		}
		if (model.getName() == null) {
			logger.warn("Cannot save model. Trying to save a model without a name.");
			throw new ModelException(StaticResources.WARN_SAVE_MODEL_WITHOUT_NAME, "Cannot save model. Trying to save a model without a name.");
		}
		if (model.getModelSrcId() == null) {
			logger.warn("Cannot save model. Trying to save a model without a source model identifier.");
			throw new ModelException(StaticResources.WARN_SAVE_MODEL_WITHOUT_MODEL_SRC_ID, "Cannot save model. Trying to save a model without a source model identifier.");
		}
		if (model.getSource() == null) {
			logger.warn("Cannot save model. Trying to save a model without a source.");
			throw new ModelException(StaticResources.WARN_SAVE_MODEL_WITHOUT_SOURCE, "Cannot save model. Trying to save a model without a source.");
		}
		Model _model = modelDAO.findBySourceData(model.getModelSrcId(), model.getSource(), model.getType());
		if (_model != null) {
			logger.warn("Cannot save model. Model with source data as pairs of (" + model.getModelSrcId() + ", " + model.getSource() + ") already exists.");
			throw new ModelException(StaticResources.WARN_SAVE_MODEL_WITH_NAME_ALREADY_EXISTS, "Cannot save model. Model with source data as pairs of (" + model.getModelSrcId() + ", " + model.getSource() + ") already exists.");
		}
		modelDAO.save(model);		
		logger.debug("Model " + model + " saved successfully.");
	}

	/**
	 * Updates and synchronizes the {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.Model}
	 * entity object state with the data base.
	 * 
	 * @param model {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.Model}
	 * entity object to update.
	 * @throws ModelException if any illegal data access or inconsistent model data error occurred.
	 */
	public void updateModel(Model model) throws ModelException {
		logger.debug("Updating model " + model + "...");	
		Model _model = modelDAO.findById(model.getId());
		if (_model == null) {
			logger.warn("Cannot update model. Model with identifier: " + model.getId() + " does not exist.");
			throw new ModelException(StaticResources.WARN_UPDATE_MODEL_NOT_EXIST, "Cannot update model. Model with identifier: " + model.getId() + " does not exist.");
		}		
		modelDAO.merge(model);		
		logger.info("Model " + model + " updated successfully.");
	}     
	/**
	 * Removes and synchronizes the {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.Model}
	 * entity object state with the data base.
	 * 
	 * @param model {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.Model}
	 * entity object to delete.
	 * @throws ModelException if any illegal data access or inconsistent model data error occurred.
	 */
	public void deleteModel(Model model) throws ModelException {
		logger.debug("Removing model " + model + "...");	
		Model _model = modelDAO.findById(model.getId());
		if (_model == null) {
			logger.warn("Cannot remove model. Model with identifier: " + model.getId() + " does not exist.");
			throw new ModelException(StaticResources.WARN_DELETE_MODEL_NOT_EXIST, "Cannot update model. Model with identifier: " + model.getId() + " does not exist.");
		}
		modelDAO.delete(_model);		
		logger.info("Model " + _model + " removed successfully.");
	}	   	
}