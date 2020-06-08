/* 
 * $Id: EventReaderException.java,v 1.0 2013-01-27 23:29:05 averab Exp $
 *
 * @copyright Universidad Carlos III de Madrid. proprietary/confidential. Use is subject to license terms.
 */
package es.uc3m.softlab.cbi4api.gbas.event.subscriber;

/**
 * Exception to identify errors produced at the event reader module.
 * 
 * @author averab
 * @version 1.0.0
 */
public class EventReaderException extends Exception {
	/** Serial Version UID */
	private static final long serialVersionUID = 4728391340273179L;
	
	/**
	 * Constructs a new exception with the specified detail message. The 
	 * cause is not initialized, and may subsequently be initialized by a 
	 * call to Throwable.initCause(java.lang.Throwable).
	 * 
	 * @param message the detail message. The detail message is saved for later 
	 * retrieval by the Throwable.getMessage() method.
	 */
	public EventReaderException(String message) {
		super(message);
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
	public EventReaderException(Throwable cause) {
		super(cause);
	}
	/**
	 * Constructs a new exception with the specified detail message and cause.
	 * <br>
	 * Note that the detail message associated with cause is not automatically 
	 * incorporated in this exception's detail message. 
	 * 
	 * @param message the detail message (which is saved for later retrieval by 
	 * the Throwable.getMessage() method).
	 * @param cause the cause (which is saved for later retrieval by the 
	 * Throwable.getCause() method). (A null value is permitted, and indicates 
	 * that the cause is nonexistent or unknown.)
	 */
	public EventReaderException(String message, Throwable cause) {
		super(message, cause);
	}
}