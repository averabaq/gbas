/* 
 * $Id: FacadeException.java,v 1.0 2011-11-28 12:29:27 averab Exp $
 *
 * @copyright Universidad Carlos III de Madrid. proprietary/confidential. Use is subject to license terms.
 */
package es.uc3m.softlab.cbi4api.gbas.event.store.facade;

import java.io.Serializable;

/**
 * Exception to handle service facade localization errors.
 * 
 * @author averab
 * @version 1.0.0
 */
abstract public class FacadeException extends Exception implements Serializable {
	/** Serial Version UID */
	private static final long serialVersionUID = 26047156963247763L;
	/** Exception code */
	private int code;

	/**
	 * Constructs a new exception with the specified detail message. The 
	 * cause is not initialized, and may subsequently be initialized by a 
	 * call to Throwable.initCause(java.lang.Throwable).
	 * 
	 * @param code exception facade code.
	 * @param message the detail message. The detail message is saved for later 
	 * retrieval by the Throwable.getMessage() method.
	 */
	public FacadeException(int code, String message) {
		super(message);
		this.code = code;
	}
	/**
	 * Constructs a new exception with the specified cause and a detail 
	 * message of (cause==null ? null : cause.toString()) (which typically 
	 * contains the class and detail message of cause). This constructor is 
	 * useful for exceptions that are little more than wrappers for other 
	 * throwables.
	 * 
	 * @param cause the cause (which is saved for later retrieval by the 
	 * Throwable.getCause() method). (A null value is permitted, and indicates 
	 * that the cause is nonexistent or unknown.)
	 */
	public FacadeException(Throwable cause) {
		super(cause);
	}
	/**
	 * Constructs a new exception with the specified detail message and cause.
	 * <br>
	 * Note that the detail message associated with cause is not automatically 
	 * incorporated in this exception's detail message. 
	 * 
	 * @param code exception facade code.
	 * @param message the detail message (which is saved for later retrieval by 
	 * the Throwable.getMessage() method).
	 * @param cause the cause (which is saved for later retrieval by the 
	 * Throwable.getCause() method). (A null value is permitted, and indicates 
	 * that the cause is nonexistent or unknown.)
	 */
	public FacadeException(int code, String message, Throwable cause) {
		super(message, cause);
		this.code = code;
	}
	/**
	 * Gets the {@link #code} property.
	 * @return the {@link #code} property.
	 */
	public int getCode() {
		return code;
	}
	/**
	 * Sets the {@link #code} property.
	 * @param code the {@link #code} property to set.
	 */
	public void setCode(int code) {
		this.code = code;
	}
}