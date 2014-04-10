/* 
 * $Id: Config.java,v 1.0 2013-01-23 22:29:27 averab Exp $
 *
 * @copyright Universidad Carlos III de Madrid. proprietary/confidential. Use is subject to license terms.
 */
package es.uc3m.softlab.cbi4api.gbas.event.subscriber;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Class that configures the global application. It takes
 * the configuration parameters from the pair (key, value) 
 * specified at the <i>event-subscriber.properties</i> file. 
 *
 * @author averab
 */
@Scope(value=BeanDefinition.SCOPE_SINGLETON)
@Component(value=StaticResources.COMPONENT_NAME_CONFIG)
public class Config {
    /** Log for tracing */
    private static final Logger logger = Logger.getLogger(Config.class);  

    /**
     * Gets the string value of the key property given.
     * @param key key property.
     * @return value of the key property.
     */
    public String getString(String key) {
    	String value = null;
    	try {
    		ResourceBundle propFile = ResourceBundle.getBundle(StaticResources.MODULE_CONFIG_RESOURCE_BUNDLE);
    		value = propFile.getString(key);    		
    	}
    	catch (MissingResourceException mre) {
    		logger.warn("Not found " + key + " property. Is it defined?");
    		logger.error(mre.getMessage());         
    	}    	
    	return value;    	
    }  
    /**
     * Gets the broker url endpoint where to receive event data from. 
     * 
     * @return broker url.
     */
    public String getBrokerUrl() {		
		String url = getString(StaticResources.BROKER_URL_RESOURCE_BUNDLE_KEY);
		if (url == null) 
			logger.error(String.format("The property '%s' containing the identification of the broker url could not be found. Please, provide this information.", StaticResources.BROKER_URL_RESOURCE_BUNDLE_KEY));
		return url;
    }
    /**
     * Gets the current GBAS node identifier.
     * @return GBAS node identifier.
     */
    public String getNodeId() {
        String value = null;
        try {
            ResourceBundle propFile = ResourceBundle.getBundle(StaticResources.APP_CONFIG_RESOURCE_BUNDLE);
            value = propFile.getString(StaticResources.GBAS_NODE_ID_RESOURCE_BUNDLE_KEY);
        }
        catch (MissingResourceException mre) {
            logger.warn(String.format("Not found '%s' property. Is it defined?", StaticResources.GBAS_NODE_ID_RESOURCE_BUNDLE_KEY));
            logger.error(mre.getMessage());
        }
        return value;
    }
    /**
     * Gets the local application configuration from business logic layer. It may differ from
     * the presentation layer. 
     * 
     * @return locale locale application configuration.
     */
    public Locale getLocale() {
		Locale locale;
		String strLocale = getString(StaticResources.LOCALE_RESOURCE_BUNDLE_KEY);
		if (strLocale == null) 
			strLocale = StaticResources.DEFAULT_LOCALE;				
		String locales[] = strLocale.split("_");
		try {
			locale = new Locale(locales[0], locales[1]);
		} catch(ArrayIndexOutOfBoundsException auobex) {
			logger.warn("Locale from properties file bad formed. Setting default locale...");
			locales = StaticResources.DEFAULT_LOCALE.split("_");		
			locale = new Locale(locales[0], locales[1]);
		}
    	return locale;    	
    }          
}