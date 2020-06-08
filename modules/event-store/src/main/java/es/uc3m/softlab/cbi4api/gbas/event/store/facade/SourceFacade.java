/* 
 * $Id: SourceFacade.java,v 1.0 2011-10-28 12:29:27 averab Exp $
 *
 * @copyright Universidad Carlos III de Madrid. proprietary/confidential. Use is subject to license terms.
 */
package es.uc3m.softlab.cbi4api.gbas.event.store.facade;

import es.uc3m.softlab.cbi4api.gbas.event.store.StaticResources;
import es.uc3m.softlab.cbi4api.gbas.event.store.domain.Source;

import java.util.List;

/**
 * Facade design pattern interface for the  {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.Source}  entity
 * object. This interface defines all methods for accessing to the <strong>source</strong> entity data 
 * throughout the Spring framework.
 * 
 * @author averab
 * @version 1.0.0
 */
public interface SourceFacade {
 	/** Spring component name */
    public static final String COMPONENT_NAME = StaticResources.COMPONENT_NAME_SOURCE_FACADE;        

	/**
	 * Gets the {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.Source} entity
	 * object associated to the  
	 * {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.Source#getId()} as primary key.
	 * 
	 * @param id source's identifier
	 * @return {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.Source} entity
	 * object associated.
	 */
    public Source getSource(String id);
	/**
	 * Gets all {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.Source} entity
	 * objects defined at the data base.
	 * 
	 * @return all {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.Source} entity
	 * objects defined at the data base.
	 */
	public List<Source> getAll();
	/**
	 * Saves and synchronizes the {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.Source}
	 * entity object state with the data base.
	 * 
	 * @param source {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.Source}
	 * entity object to save.
	 * @throws SourceException if any source error occurred.
	 */
	public void saveSource(Source source) throws SourceException;
	/**
	 * Updates and synchronizes the {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.Source}
	 * entity object state with the data base.
	 * 
	 * @param source {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.Source}
	 * entity object to update.
	 * @throws SourceException if any illegal data access or inconsistent source data error occurred.
	 */
	public void updateSource(Source source) throws SourceException;
	/**
	 * Removes and synchronizes the {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.Source}
	 * entity object state with the data base.
	 * 
	 * @param source {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.Source}
	 * entity object to delete.
	 * @throws SourceException if any illegal data access or inconsistent source data error occurred.
	 */
	public void deleteSource(Source source) throws SourceException;
}