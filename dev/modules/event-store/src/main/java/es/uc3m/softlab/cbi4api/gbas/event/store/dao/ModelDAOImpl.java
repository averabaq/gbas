/* 
 * $Id: ModelDAOImpl.java,v 1.0 2013-01-27 23:29:05 averab Exp $
 *
 * @copyright Universidad Carlos III de Madrid. proprietary/confidential. Use is subject to license terms.
 */
package es.uc3m.softlab.cbi4api.gbas.event.store.dao;

import es.uc3m.softlab.cbi4api.gbas.event.store.StaticResources;
import es.uc3m.softlab.cbi4api.gbas.event.store.Stats;
import es.uc3m.softlab.cbi4api.gbas.event.store.domain.Model;
import es.uc3m.softlab.cbi4api.gbas.event.store.domain.ModelType;
import es.uc3m.softlab.cbi4api.gbas.event.store.domain.Source;
import es.uc3m.softlab.cbi4api.gbas.event.store.entity.HModel;

import java.util.ArrayList;
import java.util.List;

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
 * Data access object class implementation for the <strong>model</strong> 
 * entity object. This class manages all data access throughout the JPA 
 * persistence layer.
 * 
 * @author averab
 * @version 1.0.0  
 */
@PersistenceUnit(name=StaticResources.PERSISTENCE_NAME_EVENT_STORE, 
		         unitName=StaticResources.PERSISTENCE_UNIT_NAME_EVENT_STORE)
@Transactional(propagation=Propagation.MANDATORY)
@Repository(value=ModelDAO.COMPONENT_NAME)
public class ModelDAOImpl implements ModelDAO {
    /** Logger for tracing */
    private transient Logger logger = Logger.getLogger(getClass());
    /** Entity manager bound to this data access object within the persistence context */
    @PersistenceContext(unitName=StaticResources.PERSISTENCE_UNIT_NAME_EVENT_STORE)
    protected EntityManager entityManager;
    /** Source data access object */
    @Autowired private SourceDAO sourceDAO;
    /** Statistical performance object */
    @Autowired private Stats stats; 
    
	/**
	 * Find all {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.Model} entity
	 * objects.
	 * 
	 * @return all {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.Model} entity
	 * objects.
	 * @throws IllegalArgumentException if an illegal argument error occurred.
	 */    
    @SuppressWarnings("unchecked")
    public List<Model> findAll() throws IllegalArgumentException {
		logger.debug("Finding all models...");
		List<HModel> list = entityManager.createQuery("select m from " + HModel.class.getName() + " m order by id asc").getResultList();
		logger.debug("All models found successfully.");
		/* convert to business object */
		List<Model> models = new ArrayList<Model>();
		for (HModel hmodel : list) {
			Model model = BusinessObjectAssembler.getInstance().toBusinessObject(hmodel);
			Source source = sourceDAO.findById(hmodel.getSource());
			model.setSource(source);	
			models.add(model);
		}
		return models;
	}
	/**
	 * Find the {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.Model} entity
	 * object associated to the  
	 * {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.Model#getId()} as primary key.
	 * 
	 * @param id model's identifier
	 * @return {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.Model} entity
	 * object associated.
	 * @throws IllegalArgumentException if an illegal argument error occurred.
	 */
	public Model findById(long id) throws IllegalArgumentException {
		logger.debug("Retrieving model with identifier " + id + "...");		
		HModel hmodel = entityManager.find(HModel.class, id);	
		if (hmodel != null) {			
			Model model = BusinessObjectAssembler.getInstance().toBusinessObject(hmodel);
			Source source = sourceDAO.findById(hmodel.getSource());
			model.setSource(source);	
			logger.debug("Model " + model + " retrieved successfully.");
			return model;
		} else {	
			logger.debug("Model with identifier " + id + " not found.");
		}				
		return null;
	}	    
	/**
	 * Find the {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.Model} entity
	 * object associated to the 
	 * ({@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.Model#getModelSrcId()} and
	 *  {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.Model#getSource()})
	 * as unique keys.
	 * 
	 * @param modelSrcId model source identifier given at the original source.
	 * @param source model's source.
	 * @param type model's type.
	 * @return {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.Model} entity object associated.
	 * @throws IllegalArgumentException if an illegal argument error occurred.
	 */
	public Model findBySourceData(String modelSrcId, Source source, ModelType type) throws IllegalArgumentException {
    	logger.debug("Retrieving model with source data as set of (" + modelSrcId + ", " + source + ", " + type + ")...");    	
    	//
    	// JPQL parameters must be explicitly set as string values due to a bug  
    	// found on DataNucleus implementation (3.2.0-release) for HBase when 
    	// caching previous query parameters. 
    	//
    	StringBuffer sql = new StringBuffer("select m from [ENTITY] m ");
    	sql.append("where m.modelSrcId = :modelSrcId ");
    	sql.append("and m.source = :sourceId ");
    	sql.append("and m.type = :type");
    	// set parameters
    	String _sql = sql.toString();
    	_sql = _sql.replace("[ENTITY]", HModel.class.getName());
    	_sql = _sql.replace(":modelSrcId", "'" + modelSrcId + "'");
    	_sql = _sql.replace(":sourceId", "'" + source.getId() + "'");
    	_sql = _sql.replace(":type", "'" + type.name() + "'");
    	// creates query without setting parameters
    	Query query = entityManager.createQuery(_sql);
    	
    	//
    	// the snippet code below does not work as the DataNucleus implementation (3.2.0-release) 
    	// for HBase has a bug on cached parameters.
    	//
    	// Query query = entityManager.createQuery("select m from " + HModel.class.getName() + " m where m.modelSrcId = :modelSrcId and m.source = :sourceId and m.type = :type");
		// query.setParameter("modelSrcId", modelSrcId);
		// query.setParameter("sourceId", source.getId());
		// query.setParameter("type", type.name());
		
		HModel hmodel = null;		
		try {			
			long ini = System.nanoTime();
			hmodel = (HModel)query.getSingleResult();
			long end = System.nanoTime();
			stats.writeStat(Stats.Operation.READ_BY_SOURCE_DATA, HModel.class, hmodel, ini, end);
			logger.debug("Model " + hmodel + " retrieved successfully.");
		} catch(NoResultException nrex) {
			logger.debug("Model with source data as set of (" + modelSrcId + ", " + source + ", " + type + ") does not exist.");
			return null;
		} catch(NonUniqueResultException nurex) {
			logger.debug(nurex.fillInStackTrace());
			throw nurex;
		}			
		Model model = BusinessObjectAssembler.getInstance().toBusinessObject(hmodel);		
		Source _source = sourceDAO.findById(hmodel.getSource());
		model.setSource(_source);	
		return model;
	}    
	/**
	 * Merges and synchronizes the {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.Model}
	 * entity object state with the data base.
	 * 
	 * @param model {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.Model}
	 * entity object to merge.
	 * @throws IllegalArgumentException if an illegal argument error occurred.
	 * @throws TransactionRequiredException if a transaction error occurred.
	 */
	public void merge(Model model) throws IllegalArgumentException, TransactionRequiredException {
		logger.debug("Merging model " + model + "...");	
		HModel hmodel = BusinessObjectAssembler.getInstance().toEntity(model);
		entityManager.merge(hmodel);			
		logger.debug("Model " + model + " merged successfully.");
	}
	/**
	 * Deletes the {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.Model}
	 * entity object associated from the data base.
	 * 
	 * @param model {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.Model}
	 * entity object to delete.
	 * @throws IllegalArgumentException if an illegal argument error occurred.
	 * @throws TransactionRequiredException if a transaction error occurred.
	 */	
	public void delete(Model model) throws IllegalArgumentException, TransactionRequiredException {
		logger.debug("Deleting model " + model + "...");		
		HModel _hmodel = entityManager.find(HModel.class, model.getId());
		if (_hmodel == null) {
			logger.warn("Model [" + model.getId() + "] could not be deleted. It does not exists.");
			return;
		}
		/* TODO: remove process instances */
		entityManager.remove(_hmodel);	
		logger.debug("Model " + model + " removed successfully.");
	}	
	/**
	 * Saves the {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.Model}
	 * entity object associated to the data base.
	 * 
	 * @param model {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.Model}
	 * entity object to save.
	 * @throws IllegalArgumentException if an illegal argument error occurred.
	 * @throws TransactionRequiredException if a transaction error occurred.
	 */	
	public void save(Model model) throws IllegalArgumentException, TransactionRequiredException {
		logger.debug("Saving model " + model + "...");			
		if (model == null) {
			logger.warn("Cannot save model. Model is null.");
			throw new IllegalArgumentException("Cannot save model. Model is null.");
		}
		if (model.getId() != null) {
			/* checks for the model existence */
			HModel _hmodel = entityManager.find(HModel.class, model.getId());		
			if (_hmodel != null) {
				logger.debug("Model " + model.getId() + " cannot be persisted because it already exists.");
				return;
			}
		}
		if (model.getModelSrcId() != null) {
			Model _model = findBySourceData(model.getModelSrcId(), model.getSource(), model.getType());
			if (_model != null) {
				// update the model object identifier
				model.setId(_model.getId());
				logger.debug("Model " + model.getModelSrcId() + " cannot be persisted because it already exists.");
				return;
			}
		}
		logger.debug("Model " + model.getId() + " does not exists. Saving model...");

		HModel hmodel = BusinessObjectAssembler.getInstance().toEntity(model);
		long ini = System.nanoTime();
		entityManager.persist(hmodel);	
		long end = System.nanoTime();
		stats.writeStat(Stats.Operation.WRITE, HModel.class, hmodel, ini, end);
		//updates the model back with the new identifier
		entityManager.refresh(hmodel);
		model.setId(hmodel.getId());
		entityManager.flush();
		logger.debug("Model " + model + " saved successfully.");
	}		
}
