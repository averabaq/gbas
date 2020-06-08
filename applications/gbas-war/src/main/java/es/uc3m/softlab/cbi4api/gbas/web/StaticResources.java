/* 
 * $Id: StaticResources.java,v 1.4 2013-01-26 21:47:15 averab Exp $
 *
 * @copyright Universidad Carlos III de Madrid. proprietary/confidential. Use is subject to license terms.
 */
package es.uc3m.softlab.cbi4api.gbas.web;

/**
 * In this class is defined all global and generic static 
 * resources of the application.
 * 
 * @author averab
 */
public class StaticResources {
	/** Internationalization resource bundle */
	public static final String CONFIG_RESOURCE_BUNDLE="gbas";
    /** Default application locale */
    public static final String DEFAULT_LOCALE="en_IE";
    /** Root path for defining process model to load into the system unit */
    public static final String ROOT_MODEL_DEFINITION_PATH = "models";
    /** Service name for resource loader config */
    public static final String SERVICE_NAME_RESOURCE_LOADER_CONFIG = "/cbi4api-gbas/config/service/resource-loader-config";

	/** Root page reference */
	public static final String ROOT_PAGE="/index.jsp";
	/** Desktop page reference */
	public static final String VIEW_ID_DESKTOP="/ui/desktop.xhtml";

	/** String html blank space representation */
	public static final String HTML_BLANK_SPACE = "&nbsp;";	
	
	/** Output files encoding */
	public static final String OUTPUT_ENCODING = "utf-8";
	/** Session map user attribute */
	public static final String SESSION_MAP_USER="userSession";
	
	/** Manifest implementation version attribute */
	public static final String MANIFEST_IMPLEMENTATION_VERSION = "Implementation-Version";
	/** Manifest entry value for undefined attributes */
	public static final String MANIFEST_UNDEFINED_ATTRIBUTE = "unknown";
	
	/** User session controller managed bean name */
	public static final String MANAGED_BEAN_USER_SESSION_CONTROLLER = "userSessionController";
	/** Process mapping controller managed bean name */
	public static final String MANAGED_BEAN_PROCESS_MAPPING_CONTROLLER = "processMappingController";
	/** Process model controller managed bean name */
	public static final String MANAGED_BEAN_PROCESS_MODEL_CONTROLLER = "processModelController";
	/** Process instance controller managed bean name */
	public static final String MANAGED_BEAN_PROCESS_INSTANCE_CONTROLLER = "processInstanceController";
	/** Event controller managed bean name */
	public static final String MANAGED_BEAN_EVENT_CONTROLLER = "eventController";
	/** OLAP controller managed bean name */
	public static final String MANAGED_BEAN_OLAP_CONTROLLER = "olapController";
	/** BPEQL controller managed bean name */
	public static final String MANAGED_BEAN_BPEQL_CONTROLLER = "bpeqlController";
	
	/** Navigation handling login outcome success */
	public static final String NAVIGATION_LOGIN_OUTCOME_SUCCESS="success";
	/** Navigation handling login outcome fail */
	public static final String NAVIGATION_LOGIN_OUTCOME_FAIL="failure";
	/** Navigation handling logout outcome success */
	public static final String NAVIGATION_LOGOUT_OUTCOME_SUCCESS="success";
}