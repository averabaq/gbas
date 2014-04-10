/* 
 * $Id: LoaderConfig.java,v 1.0 2014-01-28 12:29:27 averab Exp $
 *
 * @copyright Universidad Carlos III de Madrid. proprietary/confidential. Use is subject to license terms.
 */
package es.uc3m.softlab.cbi4api.gbas.web.config;

/**
 * Component interface for loading basic configs on GBAS node.
 * 
 * @author averab
 */
public interface LoaderConfig {
    /** Configuration label for sources */
    public static final String SOURCES = "sources.source";
    /** Configuration label for source id */
    public static final String SOURCE_ID = "[@id]";
    /** Configuration label for source name */
    public static final String SOURCE_NAME = "[@name]";
    /** Configuration label for source description */
    public static final String SOURCE_DESCRIPTION = "description";
    /** Configuration label for source inet address */
    public static final String SOURCE_INET_ADDRESS = "inetAddress";
    /** Configuration label for source port */
    public static final String SOURCE_PORT = "port";
    /** Configuration label for process models */
    public static final String PROCESS_MODELS = "processModels.processModel";
    /** Configuration label for process model id */
    public static final String PROCESS_MODEL_ID = "[@id]";
    /** Configuration label for process model name */
    public static final String PROCESS_MODEL_NAME = "[@name]";
    /** Configuration label for process model description */
    public static final String PROCESS_MODEL_DESCRIPTION = "description";
    /** Configuration label for process models */
    public static final String PROCESS_MODEL_SOURCE = "source";

    /**
     * Setup the GBAS node on a given model defined in path given at arguments.
     * @param model model name to to configure from external file.
     * @throws LoaderConfigurationException if any error occurred during the loader configuration process.
     */
    public void setup(String model) throws LoaderConfigurationException;
}