/* 
 * $Id: StaticResources.java,v 1.0 2013-01-27 23:29:05 averab Exp $
 *
 * @copyright Universidad Carlos III de Madrid. proprietary/confidential. Use is subject to license terms.
 */
package es.uc3m.softlab.cbi4api.gbas.event.store;

/**
 * In this class is defined all global and generic static 
 * resources of the application.
 * 
 * @author averab
 */
public class StaticResources {
	/** GBAS persistence name */
	public static final String PERSISTENCE_NAME_EVENT_STORE="cbi4api-gbas-event-store";
	/** GBAS persistence unit name */
	public static final String PERSISTENCE_UNIT_NAME_EVENT_STORE="cbi4api-gbas-event-store";
	/** Internationalization resource bundle */
	public static final String CONFIG_RESOURCE_BUNDLE="event-store";	
    /** Default application locale */
    public static final String DEFAULT_LOCALE="en_IE";
    /** Empty string representation */
    public static final String STRING_EMPTY="";

    /** Bundle key for current locale */
    public static final String BUNDLE_CONFIG_LOCALE_KEY="cbi4api.gbas.event.store.application.locale";
    /** Bundle key for stats file */
    public static final String BUNDLE_CONFIG_STATS_FILE_KEY="cbi4api.gbas.event.store.stats.file";
    /** Bundle key for stats active flag */
    public static final String BUNDLE_CONFIG_STATS_ACTIVE_KEY="cbi4api.gbas.event.store.stats.active";
    
	/** Component name for the event store configuration */
	public static final String COMPONENT_NAME_CONFIG = "eventStoreConfig";
	/** Component name for event data access object */
	public static final String COMPONENT_NAME_EVENT_DAO = "/cbi4api-gbas/event-store/dao/EventDAO";
	/** Component name for source data access object */
	public static final String COMPONENT_NAME_SOURCE_DAO = "/cbi4api-gbas/event-store/dao/SourceDAO";
	/** Component name for model data access object */
	public static final String COMPONENT_NAME_MODEL_DAO = "/cbi4api-gbas/event-store/dao/ModelDAO";
	/** Component name for process model data access object */
	public static final String COMPONENT_NAME_PROCESS_MODEL_DAO = "/cbi4api-gbas/event-store/dao/ModelDAO";
	/** Component name for activity model data access object */
	public static final String COMPONENT_NAME_ACTIVITY_MODEL_DAO = "/cbi4api-gbas/event-store/dao/ModelDAO";
	/** Component name for process instance data access object */
	public static final String COMPONENT_NAME_PROCESS_MAPPING_DAO = "/cbi4api-gbas/event-store/dao/ProcessMappingDAO";
	/** Component name for process instance data access object */
	public static final String COMPONENT_NAME_PROCESS_INSTANCE_DAO = "/cbi4api-gbas/event-store/dao/ProcessInstanceDAO";
	/** Component name for activity instance data access object */
	public static final String COMPONENT_NAME_ACTIVITY_INSTANCE_DAO = "/cbi4api-gbas/event-store/dao/ActivityInstanceDAO";
	
	/** Component name for event service facade */
	public static final String COMPONENT_NAME_EVENT_FACADE = "/cbi4api-gbas/event-store/facade/EventFacade";
	/** Component name for source service facade */
	public static final String COMPONENT_NAME_SOURCE_FACADE = "/cbi4api-gbas/event-store/facade/SourceFacade";
	/** Component name for model service facade */
	public static final String COMPONENT_NAME_MODEL_FACADE = "/cbi4api-gbas/event-store/facade/ModelFacade";
	/** Component name for process model service facade */
	public static final String COMPONENT_NAME_PROCESS_MODEL_FACADE = "/cbi4api-gbas/event-store/facade/ModelFacade";
	/** Component name for activity model service facade */
	public static final String COMPONENT_NAME_ACTIVITY_MODEL_FACADE = "/cbi4api-gbas/event-store/facade/ModelFacade";
	/** Component name for process instance service facade */
	public static final String COMPONENT_NAME_PROCESS_INSTANCE_FACADE = "/cbi4api-gbas/event-store/facade/ProcessInstanceFacade";
	/** Component name for activity instance service facade */
	public static final String COMPONENT_NAME_ACTIVITY_INSTANCE_FACADE = "/cbi4api-gbas/event-store/facade/ActivityInstanceFacade";
	/** Component name for process mapping service facade */
	public static final String COMPONENT_NAME_PROCESS_MAPPING_FACADE = "/cbi4api-gbas/event-store/facade/ProcessMappingFacade";
	
	/** Facade warn login code for generic spring components exceptions */ 
	public static final int ERROR_GENERIC_SPRING_COMPONENTS = 1;
	/** Facade warn login code for generic JPA unexpected exceptions */ 
	public static final int ERROR_GENERIC_JPA_UNEXPECTED_EXCEPTION = 10;
	
	/** Facade warn code for getting an event which does not exist */ 
	public static final int WARN_GET_EVENT_NOT_EXIST = 10001;	
	/** Facade warn code for saving an event which is a null object */ 
	public static final int WARN_SAVE_EVENT_NULL = 10002;
	/** Facade warn code for saving an event which has null process instance identifier */ 
	public static final int WARN_SAVE_EVENT_NULL_PROCESS_INSTANCE_ID = 10003;
	/** Facade warn code for saving an event which already exists at the database */ 
	public static final int WARN_SAVE_EVENT_ALREADY_EXISTS = 10004;
	/** Facade warn code for updating an event which does not exist */ 
	public static final int WARN_UPDATE_EVENT_NOT_EXIST = 10005;	
	/** Facade warn code for deleting an event which does not exist */
	public static final int WARN_DELETE_EVENT_NOT_EXIST = 10006;
	/** Facade warn code for loading event data on an event which does not exist */
	public static final int WARN_LOAD_EVENT_DATA_EVENT_NOT_EXIST = 10007;
	/** Facade warn code for loading event payload on an event which does not exist */
	public static final int WARN_LOAD_EVENT_PAYLOAD_EVENT_NOT_EXIST = 10008;
	/** Facade warn code for loading event correlation on an event which does not exist */
	public static final int WARN_LOAD_EVENT_CORRELATION_EVENT_NOT_EXIST = 10009;
	
	/** Facade warn code for getting a source which does not exist */ 
	public static final int WARN_GET_SOURCE_NOT_EXIST = 11001;	
	/** Facade warn code for saving a source which is a null object */ 
	public static final int WARN_SAVE_NULL_SOURCE = 11002;
	/** Facade warn code for saving a source with no identifier */ 
	public static final int WARN_SAVE_SOURCE_WITHOUT_ID = 11003;
	/** Facade warn code for saving a source with no information on the name field */ 
	public static final int WARN_SAVE_SOURCE_WITHOUT_NAME = 11004;
	/** Facade warn code for saving a source which already exists at the database */ 
	public static final int WARN_SAVE_SOURCE_ALREADY_EXISTS = 11005;
	/** Facade warn code for updating a source which does not exist */ 
	public static final int WARN_UPDATE_SOURCE_NOT_EXIST = 11006;
	/** Facade warn code for deleting a source which does not exist */
	public static final int WARN_DELETE_SOURCE_NOT_EXIST = 11007;

	/** Facade warn code for getting a model which does not exist */ 
	public static final int WARN_GET_MODEL_NOT_EXIST = 12001;	
	/** Facade warn code for getting a model with no information on the original source field */ 
	public static final int WARN_GET_MODEL_WITHOUT_SOURCE = 12002;
	/** Facade warn code for getting a model with no information on the model identifier of the original source */ 
	public static final int WARN_GET_MODEL_WITHOUT_MODEL_SRC_ID = 12003;	
	/** Facade warn code for getting a model with no information on the model type of the original source */ 
	public static final int WARN_GET_MODEL_UNKNOWN_TYPE = 12004;
	/** Facade warn code for saving a model which is a null object */ 
	public static final int WARN_SAVE_NULL_MODEL = 12005;
	/** Facade warn code for saving a model with no information on the name field */ 
	public static final int WARN_SAVE_MODEL_WITHOUT_NAME = 12006;
	/** Facade warn code for saving a model with no information on the model source identifier field */ 
	public static final int WARN_SAVE_MODEL_WITHOUT_MODEL_SRC_ID = 12007;
	/** Facade warn code for saving a model with no information on the source field */ 
	public static final int WARN_SAVE_MODEL_WITHOUT_SOURCE = 12008;
	/** Facade warn code for saving a model which already exists at the database with the given name */ 
	public static final int WARN_SAVE_MODEL_WITH_NAME_ALREADY_EXISTS = 12009;
	/** Facade warn code for updating a model which does not exist */ 
	public static final int WARN_UPDATE_MODEL_NOT_EXIST = 12010;
	/** Facade warn code for deleting a model which does not exist */
	public static final int WARN_DELETE_MODEL_NOT_EXIST = 12011;
	/** Facade warn code for loading instance on a model which does not exist */
	public static final int WARN_LOAD_PROCESS_INSTANCES_MODEL_NOT_EXIST=12012;
	
	/** Facade warn code for getting the previous process event from a null process instance */
	public static final int WARN_GET_PREVIOUS_PROCESS_EVENT_NULL_PROCESS = 13001;
	/** Facade warn code for getting the previous process event from a null process instance identifier from the original source */
	public static final int WARN_GET_PREVIOUS_PROCESS_EVENT_NULL_PROCESS_SRC_ID = 13002;
	/** Facade warn code for getting the previous process event from a null source */
	public static final int WARN_GET_PREVIOUS_PROCESS_EVENT_NULL_SOURCE = 13003;
	/** Facade warn code for getting the previous process event from a null model */
	public static final int WARN_GET_PREVIOUS_PROCESS_EVENT_NULL_MODEL = 13004;
	/** Facade warn code for getting the previous activity event from a null process instance */
	public static final int WARN_GET_PREVIOUS_ACTIVITY_EVENT_NULL_PROCESS = 13101;
	/** Facade warn code for getting the previous activity event from a null activity instance */
	public static final int WARN_GET_PREVIOUS_ACTIVITY_EVENT_NULL_ACTIVITY = 13102;	
	
	/** Facade warn code for getting a process instance which does not exist */ 
	public static final int WARN_GET_PROCESS_INSTANCE_NOT_EXIST = 14001;
	/** Facade warn code for getting a process instance with no information on the original source field */ 
	public static final int WARN_GET_PROCESS_INSTANCE_WITHOUT_SOURCE = 14002;
	/** Facade warn code for getting a process instance with no information on the process instance identifier of the original source */ 
	public static final int WARN_GET_PROCESS_INSTANCE_WITHOUT_INSTANCE_SRC_ID = 14003;
	/** Facade warn code for getting a process instance with no information on the correlation data */ 
	public static final int WARN_GET_PROCESS_INSTANCE_WITHOUT_CORRELATION_DATA = 14004;
	/** Facade warn code for getting a process instance with no information on the process model identifier */ 
	public static final int WARN_GET_PROCESS_INSTANCE_WITHOUT_PROCESS_MODEL_ID = 14005;	
	/** Facade warn code for saving a process instance which is a null object */ 
	public static final int WARN_SAVE_NULL_PROCESS_INSTANCE = 14006;
	/** Facade warn code for saving a process instance with no information on the process definition model field */ 
	public static final int WARN_SAVE_PROCESS_INSTANCE_WITHOUT_MODEL = 14007;
	/** Facade warn code for saving a process instance with no information on the process identifier of the original source */ 
	public static final int WARN_SAVE_PROCESS_INSTANCE_WITHOUT_PROCESS_SRC_ID = 14008;
	/** Facade warn code for saving a process instance with no information on the source field attached to the process model */ 
	public static final int WARN_SAVE_PROCESS_INSTANCE_WITHOUT_SOURCE = 14009;
	/** Facade warn code for saving a process instance which already exists at the database with the given source data information */ 
	public static final int WARN_SAVE_PROCESS_INSTANCE_WITH_SOURCE_DATA_ALREADY_EXISTS = 14010;
	/** Facade warn code for updating a process instance which does not exist */ 
	public static final int WARN_UPDATE_PROCESS_INSTANCE_NOT_EXIST = 14011;
	/** Facade warn code for deleting a process instance which does not exist */
	public static final int WARN_DELETE_PROCESS_INSTANCE_NOT_EXIST = 14012;
	/** Facade warn code for loading events on a process instance which does not exist */
	public static final int WARN_LOAD_EVENT_PROCESS_INSTANCE_NOT_EXIST = 14013;		
	
	/** Facade warn code for getting a activity instance which does not exist */ 
	public static final int WARN_GET_ACTIVITY_INSTANCE_NOT_EXIST = 15001;	
	/** Facade warn code for getting a activity instance from an unrelated process */ 
	public static final int WARN_GET_ACTIVITY_INSTANCE_NULL_PROCESS = 15002;	
	/** Facade warn code for getting a activity instance with no information on the original source field */ 
	public static final int WARN_GET_ACTIVITY_INSTANCE_WITHOUT_SOURCE = 15003;
	/** Facade warn code for getting a activity instance with no information on the activity instance identifier of the original source */ 
	public static final int WARN_GET_ACTIVITY_INSTANCE_WITHOUT_INSTANCE_SRC_ID = 15004;
	/** Facade warn code for getting a activity instance with no information on the correlation data */ 
	public static final int WARN_GET_ACTIVITY_INSTANCE_WITHOUT_CORRELATION_DATA = 15005;
	/** Facade warn code for getting a activity instance with no information on the activity model identifier */ 
	public static final int WARN_GET_ACTIVITY_INSTANCE_WITHOUT_PROCESS_MODEL_ID = 15006;	

	/** Facade warn code for saving a activity instance which is a null object */ 
	public static final int WARN_SAVE_NULL_ACTIVITY_INSTANCE = 15007;
	/** Facade warn code for updating a activity instance which does not exist */ 
	public static final int WARN_UPDATE_ACTIVITY_INSTANCE_NOT_EXIST = 15008;
	/** Facade warn code for deleting a activity instance which does not exist */
	public static final int WARN_DELETE_ACTIVITY_INSTANCE_NOT_EXIST = 15009;
	/** Facade warn code for loading events on an activity instance which does not exist */
	public static final int WARN_LOAD_EVENT_ACTIVITY_INSTANCE_NOT_EXIST = 15010;	
	
	/** Facade warn code for getting a process mapping which does not exist */ 
	public static final int WARN_GET_PROCESS_MAPPING_NOT_EXIST = 16001;	
	/** Facade warn code for saving a process mapping which is a null object */ 
	public static final int WARN_SAVE_NULL_PROCESS_MAPPING = 16002;
	/** Facade warn code for saving a process mapping with no information on the name field */ 
	public static final int WARN_SAVE_PROCESS_MAPPING_WITHOUT_NAME = 16003;
	/** Facade warn code for saving a process mapping with no information on the description field */ 
	public static final int WARN_SAVE_PROCESS_MAPPING_WITHOUT_DESCRIPTION = 16004;
	/** Facade warn code for updating a process mapping which does not exist */ 
	public static final int WARN_UPDATE_PROCESS_MAPPING_NOT_EXIST = 16005;
	/** Facade warn code for deleting a process mapping which does not exist */
	public static final int WARN_DELETE_PROCESS_MAPPING_NOT_EXIST = 16006;
	/** Facade warn code for loading model mappings on a process mapping which does not exist */
	public static final int WARN_LOAD_MODEL_MAPPINGS_PROCESS_MAPPING_NOT_EXIST = 16007;
	
}
