/* 
 * $Id: Config.java,v 1.0 2013-01-27 23:29:05 averab Exp $
 *
 * @copyright Universidad Carlos III de Madrid. proprietary/confidential. Use is subject to license terms.
 */
package es.uc3m.softlab.cbi4api.gbas.event.store;

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
 * specified at the <i>event-store.properties</i> file. 
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
    		ResourceBundle propFile = ResourceBundle.getBundle(StaticResources.CONFIG_RESOURCE_BUNDLE);     
    		value = propFile.getString(key);    		
    	}
    	catch (MissingResourceException mre) {
    		logger.warn("Not found " + key + " property. Is it defined?");
    		logger.error(mre.getMessage());         
    	}    	
    	return value;     	
    }   
	/**
     * Gets the string value of the key property given.
     * @param key key property.
     * @return value of the key property.
     */
    public boolean isStatsActive() {
    	boolean active = false;
    	try {
    		ResourceBundle propFile = ResourceBundle.getBundle(StaticResources.CONFIG_RESOURCE_BUNDLE);     
    		String value = propFile.getString(StaticResources.BUNDLE_CONFIG_STATS_ACTIVE_KEY);
    		active = Boolean.valueOf(value);
    	}
    	catch (MissingResourceException mre) {
    		logger.warn("Not found " + StaticResources.BUNDLE_CONFIG_STATS_ACTIVE_KEY + " property. Is it defined?");
    		logger.error(mre.getMessage());  
    		return false;
    	}    	
    	return active;     	
    } 
    /**
     * Gets the local application configuration from business logic layer. It may differ from
     * the presentation layer. 
     * 
     * @return locale locale application configuration.
     */
    public Locale getLocale() {
		Locale locale;
		String strLocale = getString(StaticResources.BUNDLE_CONFIG_LOCALE_KEY);
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