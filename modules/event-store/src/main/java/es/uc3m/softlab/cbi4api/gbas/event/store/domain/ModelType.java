/* 
 * $Id: ModelType.java,v 1.0 2011-12-19 12:29:27 averab Exp $
 *
 * @copyright Universidad Carlos III de Madrid. proprietary/confidential. Use is subject to license terms.
 */
package es.uc3m.softlab.cbi4api.gbas.event.store.domain;

/**
 * Enumerator class for discriminating between process and activity models.
 * 
 * @author averab
 * @version 1.0.0 
 */
public enum ModelType {	
	/**
	 * Process model type.
	 */
	PROCESS("process"),
    /**
     * Activity model type.
     */
    ACTIVITY("activity");
	/** enum constant value */
	private final String value;
	
	/**
	 * Constructor for this enumerator class.
	 * @param value
	 */
    private ModelType(String value) {
    	this.value=value;
    }
    /**
     * Gets the enum current constant value.
     * @return enum current constant value.
     */
    public String value() {
    	return this.value;
    }
}