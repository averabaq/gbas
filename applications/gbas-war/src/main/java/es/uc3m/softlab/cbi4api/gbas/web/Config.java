/* 
 * $Id: Config.java,v 1.4 2013-01-26 21:47:15 averab Exp $
 *
 * @copyright Universidad Carlos III de Madrid. proprietary/confidential. Use is subject to license terms.
 */
package es.uc3m.softlab.cbi4api.gbas.web;

import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

import org.apache.log4j.Logger;

/**
 * Class that configures the global application. It takes
 * the configuration parameters from the pair (key, value) 
 * specified at the <i>messgae_[locale].properties</i> file. 
 * <br>
 * The locales supported are the following ones:
 * <ul>
 * 		<li>es (Spanish)</li>
 * 		<li>en (English)</li>
 * 		<li>de (German)</li>
 * 		<li>fr (French)</li>
 * 		<li>it (Italian)</li>
 * </ul>
 *
 * @author averab
 */
public class Config {
    /** Instance of this singleton class */
    private static Config instance = null;
    /** Log for tracing */
    private static final Logger logger = Logger.getLogger(Config.class);  
    
    /**
     * Constructor of this singleton class.     
     */
    private Config() {    
    }
    /**
     * Initializes this singleton class and creates a new instance
     * if it's already not.
     * 
     * @return instance of this singleton class.
     */
    public static Config init() {
       	if (instance == null)
    		instance = new Config();
    	return instance;    	
    }
    /**
     * Gets the instance of this singleton class.
     * 
     * @return instance of this singleton class.
     */
    public static Config getInstance() {
       	if (instance == null)
    		instance = init();
    	return instance;
    }
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
     * Gets the current faces locale.     
     * @return the current faces locale. 
     *
    public Locale getLocale() {
    	FacesContext facesContext = FacesContext.getCurrentInstance();
    	if (facesContext.getViewRoot() != null)
    		return facesContext.getViewRoot().getLocale(); 
    	String locales[] = StaticResources.DEFAULT_LOCALE.split("_");		
		return new Locale(locales[0], locales[1]);
    } */
    /**
     * Gets the bundle package version defined at the manifest file.     
     * @return the bundle package version defined at the manifest file.   
     *
    public String getBundleVersion() {
    	FacesContext facesContext = FacesContext.getCurrentInstance();
    	ExternalContext externalContext = facesContext.getExternalContext();
    	InputStream inputStream = externalContext.getResourceAsStream("/META-INF/MANIFEST.MF");    	
		try {
			Manifest manifest = new Manifest(inputStream);
	    	Attributes version = manifest.getMainAttributes();
	    	if (version == null)
	    		return StaticResources.MANIFEST_UNDEFINED_ATTRIBUTE;
	    	else
	    		return version.getValue(StaticResources.MANIFEST_IMPLEMENTATION_VERSION);
		} catch (IOException ioex) {
			logger.error(ioex.fillInStackTrace());			
		} catch (IllegalArgumentException iaex) {
			logger.warn("The entry '" + StaticResources.MANIFEST_IMPLEMENTATION_VERSION + "' could not be found in the MANIFEST file. Is it defined?");
		}
		return StaticResources.MANIFEST_UNDEFINED_ATTRIBUTE;
    }  */
}