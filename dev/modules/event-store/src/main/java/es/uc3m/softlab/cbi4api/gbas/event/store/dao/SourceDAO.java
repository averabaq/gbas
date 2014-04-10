/* 
 * $Id: SourceDAO.java,v 1.0 2013-01-27 23:29:05 averab Exp $
 *
 * @copyright Universidad Carlos III de Madrid. proprietary/confidential. Use is subject to license terms.
 */
package es.uc3m.softlab.cbi4api.gbas.event.store.dao;

import es.uc3m.softlab.cbi4api.gbas.event.store.StaticResources;
import es.uc3m.softlab.cbi4api.gbas.event.store.domain.Source;

import java.util.List;

import javax.persistence.TransactionRequiredException;

/**
 * Data access object interface for the {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.Source}
 * entity object. This interface defines all methods for accessing to the 
 * <strong>Source</strong> entity data through the JPA persistence layer.
 * 
 * @author averab
 * @version 1.0.0
 */
public interface SourceDAO {
 	/** Local jndi name */
    public static final String COMPONENT_NAME = StaticResources.COMPONENT_NAME_SOURCE_DAO;        

	/**
	 * Find all {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.Source} entity
	 * objects.
	 * 
	 * @return all {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.Source} entity
	 * objects.
	 * @throws IllegalArgumentException if an illegal argument error occurred.
	 */    
    public List<Source> findAll() throws IllegalArgumentException;
	/**
	 * Find the {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.Source} entity
	 * object associated to the  
	 * {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.Source#getId()} as primary key.
	 * 
	 * @param id source's identifier
	 * @return {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.Source} entity
	 * object associated.
	 * @throws IllegalArgumentException if an illegal argument error occurred.
	 */
	public Source findById(String id) throws IllegalArgumentException;
	/**
	 * Merges and synchronizes the {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.Source}
	 * entity object state with the data base.
	 * 
	 * @param source {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.Source}
	 * entity object to merge.
	 * @throws IllegalArgumentException if an illegal argument error occurred.
	 * @throws TransactionRequiredException if a transaction error occurred.
	 */
	public void merge(Source source) throws IllegalArgumentException, TransactionRequiredException;
	/**
	 * Deletes the {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.Source}
	 * entity object associated from the data base.
	 * 
	 * @param source {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.Source}
	 * entity object to delete.
	 * @throws IllegalArgumentException if an illegal argument error occurred.
	 * @throws TransactionRequiredException if a transaction error occurred.
	 */	
	public void delete(Source source) throws IllegalArgumentException, TransactionRequiredException;
	/**
	 * Saves the {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.Source}
	 * entity object associated to the data base.
	 * 
	 * @param source {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.Source}
	 * entity object to save.
	 * @throws IllegalArgumentException if an illegal argument error occurred.
	 * @throws TransactionRequiredException if a transaction error occurred.
	 */	
	public void save(Source source) throws IllegalArgumentException, TransactionRequiredException;
}
