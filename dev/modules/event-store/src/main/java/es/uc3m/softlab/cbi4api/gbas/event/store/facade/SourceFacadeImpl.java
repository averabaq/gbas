/* 
 * $Id: SourceFacadeImpl.java,v 1.0 2013-01-27 23:29:05 averab Exp $
 *
 * @copyright Universidad Carlos III de Madrid. proprietary/confidential. Use is subject to license terms.
 */
package es.uc3m.softlab.cbi4api.gbas.event.store.facade;

import es.uc3m.softlab.cbi4api.gbas.event.store.StaticResources;
import es.uc3m.softlab.cbi4api.gbas.event.store.dao.SourceDAO;
import es.uc3m.softlab.cbi4api.gbas.event.store.domain.Source;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Facade design pattern class implementation for the {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.Source}
 * entity object. This class manages all data access throughout the Spring framework.
 * 
 * @author averab 
 * @version 1.0.0 
 */
@Transactional
@Service(value=SourceFacade.COMPONENT_NAME)
public class SourceFacadeImpl implements SourceFacade {
    /** Logger for tracing */
    private transient Logger logger = Logger.getLogger(SourceFacadeImpl.class);
    /** Entity manager bound to this session bean throughout the JTA */
    @PersistenceContext(unitName=StaticResources.PERSISTENCE_UNIT_NAME_EVENT_STORE)
    protected EntityManager entityManager;
    /** Source data access object */
    @Autowired private SourceDAO sourceDAO;
    
	/**
	 * Gets the {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.Source} entity
	 * object associated to the  
	 * {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.Source#getId()} as primary key.
	 * 
	 * @param id source's identifier
	 * @return {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.Source} entity
	 * object associated.
	 */
    public Source getSource(String id) {
		logger.debug("Retrieving source with identifier " + id + "...");
		Source source = sourceDAO.findById(id);
		if (source == null) {
			logger.warn("Cannot get source. Source with identifier: " + id + " does not exist.");
		} else {
            logger.debug("Source " + source + " retrieved successfully.");
        }
		return source;
	}
	/**
	 * Gets all {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.Source} entity
	 * objects defined at the data base.
	 * 
	 * @return all {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.Source} entity
	 * objects defined at the data base.
	 */
	public List<Source> getAll() {
		logger.debug("Retrieving all sources...");
		List<Source> sources = sourceDAO.findAll();
		logger.debug("Sources retrieved successfully.");
		return sources;
	}      
	/**
	 * Saves and synchronizes the {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.Source}
	 * entity object state with the data base.
	 * 
	 * @param source {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.Source}
	 * entity object to save.
	 * @throws SourceException if any source error occurred.
	 */
	public void saveSource(Source source) throws SourceException {
		logger.debug("Saving the source " + source + "...");		
		if (source == null) {
			logger.warn("Cannot save source. Trying to save a null source.");
			throw new SourceException(StaticResources.WARN_SAVE_NULL_SOURCE, "Cannot save source. Trying to save a null source.");
		}
		if (source.getId() == null) {
			logger.warn("Cannot save source. Trying to save a source without an identifier.");
			throw new SourceException(StaticResources.WARN_SAVE_SOURCE_WITHOUT_ID, "Cannot save source. Trying to save a source without an identifier.");
		}
		if (source.getName() == null) {
			logger.warn("Cannot save source. Trying to save a source without a name.");
			throw new SourceException(StaticResources.WARN_SAVE_SOURCE_WITHOUT_NAME, "Cannot save source. Trying to save a source without a name.");
		}
		Source _source = sourceDAO.findById(source.getId());
		if (_source != null) {
			logger.warn("Cannot save source. Source with id " + source.getId() + " already exists.");
			//throw new SourceException(StaticResources.WARN_SAVE_SOURCE_ALREADY_EXISTS, "Cannot save source. Source with id " + source.getId() + " already exists.");
			return;
		}
		sourceDAO.save(source);		
		logger.info("Source " + source + " saved successfully.");
	}

	/**
	 * Updates and synchronizes the {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.Source}
	 * entity object state with the data base.
	 * 
	 * @param source {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.Source}
	 * entity object to update.
	 * @throws SourceException if any illegal data access or inconsistent source data error occurred.
	 */
	public void updateSource(Source source) throws SourceException {
		logger.debug("Updating source " + source + "...");	
		Source _source = sourceDAO.findById(source.getId());
		if (_source == null) {
			logger.warn("Cannot update source. Source with identifier: " + source.getId() + " does not exist.");
			throw new SourceException(StaticResources.WARN_UPDATE_SOURCE_NOT_EXIST, "Cannot update source. Source with identifier: " + source.getId() + " does not exist.");
		}		
		sourceDAO.merge(source);		
		logger.info("Source " + source + " updated successfully.");
	}     
	/**
	 * Removes and synchronizes the {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.Source}
	 * entity object state with the data base.
	 * 
	 * @param source {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.Source}
	 * entity object to delete.
	 * @throws SourceException if any illegal data access or inconsistent source data error occurred.
	 */
	public void deleteSource(Source source) throws SourceException {
		logger.debug("Removing source " + source + "...");	
		Source _source = sourceDAO.findById(source.getId());
		if (_source == null) {
			logger.warn("Cannot remove source. Source with identifier: " + source.getId() + " does not exist.");
			throw new SourceException(StaticResources.WARN_DELETE_SOURCE_NOT_EXIST, "Cannot remove source. Source with identifier: " + source.getId() + " does not exist.");
		}
		sourceDAO.delete(_source);		
		logger.info("Source " + _source + " removed successfully.");
	}	   	
}