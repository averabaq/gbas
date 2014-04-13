/* 
 * $Id: StaticResources.java,v 1.0 2013-01-27 23:29:05 averab Exp $
 *
 * @copyright Universidad Carlos III de Madrid. proprietary/confidential. Use is subject to license terms.
 */
package es.uc3m.softlab.cbi4api.gbas.event.subscriber;

/**
 * In this class is defined all global and generic static 
 * resources of the project system module.
 * 
 * @author averab
 */
public class StaticResources {
    /** Global application resource bundle */
    public static final String APP_CONFIG_RESOURCE_BUNDLE="gbas";
	/** Module resource bundle */
	public static final String MODULE_CONFIG_RESOURCE_BUNDLE="event-subscriber";
	/** basu-event BPAF xml schema classpath location */
    public static final String BASU_EVENT_BPAF_XML_SCHEMA_CLASSPATH_FILE="/xsd/basu-event.xsd";
    /** Default application locale */
    public static final String DEFAULT_LOCALE="en_IE";

    /** Locale url bundle key */
    public static final String LOCALE_RESOURCE_BUNDLE_KEY="cbi4api.gbas.event.subscriber.application.locale";
    /** Broker url bundle key */
    public static final String BROKER_URL_RESOURCE_BUNDLE_KEY="cbi4api.gbas.event.subscriber.activemq.broker.url";
    /** GBAS global process definition bundle key */
    public static final String GBAS_GLOBAL_PROCESS_DEF_RESOURCE_BUNDLE_KEY="cbi4api.gbas.global.process.definition";

	/** Component name for the event subscriber configuration */
	public static final String COMPONENT_NAME_CONFIG = "eventSubscriberConfig";
	/** Component name for the event writer */
	public static final String COMPONENT_NAME_EVENT_WRITER = "/cbi4api-gbas/event-subscriber/component/EventWriter";
	/** Component name for the event reader service */
	public static final String COMPONENT_NAME_EVENT_READER = "/cbi4api-gbas/event-subscriber/component/EventReader";
	/** Component name for the event converter service */
	public static final String COMPONENT_NAME_EVENT_CONVERTER = "/cbi4api-gbas/event-subscriber/component/EventConverter";
	/** Component name for the event writer */
	public static final String COMPONENT_NAME_EVENT_CORRELATOR = "/cbi4api-gbas/event-subscriber/component/EventCorrelator";
	
	/** Service name for the event subscriber ETL (Extract, Transform & Load) module */
	public static final String SERVICE_NAME_ETL_PROCESSOR = "/cbi4api-gbas/event-subscriber/service/etl-event-processor";
	
	/** Facade warn login code for generic spring components exceptions */ 
	public static final int ERROR_GENERIC_SPRING_COMPONENTS = 1;
	/** Facade warn login code for generic event subscriber unexpected exceptions */ 
	public static final int ERROR_GENERIC_EVENT_SUBSCRIBER_UNEXPECTED_EXCEPTION = 10;
	
	/** Facade warn code for processing an incoming event which does not supply enough information to correlate the event */ 
	public static final int ERROR_INCOMING_EVENT_EMPTY_CORRELATION_DATA = 10001;	
	/** Facade warn code for processing an incoming event which has not state transition */ 
	public static final int WARN_EVENT_WITH_NO_STATE_TRANSITION = 10002;
}
