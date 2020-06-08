/* 
 * $Id: State.java,v 1.0 2011-10-26 17:32:14 averab Exp $
 *
 * @copyright Universidad Carlos III de Madrid. proprietary/confidential. Use is subject to license terms.
 */
package es.uc3m.softlab.cbi4api.gbas.event.store.domain;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * <p>
 * This class is compliant with the 
 * <a href="http://www.wfmc.org/business-process-analytics-format.html"> BPAF
 * (Business Process Analytics Format)</a> specification standard published by
 * the <a href="http://www.wfmc.org">WfMC (Workflow Management Coalition)</a>.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;xs:simpleType name="State"> 
 *    &lt;xs:annotation> 
 *       &lt;xs:documentation>Enumeration of Possible Process and Activity States&lt;/xs:documentation> 
 *    &lt;/xs:annotation> 
 *    &lt;xs:list> 
 *       &lt;xs:simpleType> 
 *          &lt;xs:restriction base="xs:string"> 
 *             &lt;xs:enumeration value="Open"/> 
 *             &lt;xs:enumeration value="Open.NotRunning"/> 
 *             &lt;xs:enumeration value="Open.NotRunning.Ready"/> 
 *             &lt;xs:enumeration value="Open.NotRunning.Assigned"/> 
 *             &lt;xs:enumeration value="Open.NotRunning.Reserved"/> 
 *             &lt;xs:enumeration value="Open.NotRunning.Suspended"/> 
 *             &lt;xs:enumeration value="Open.NotRunning.Suspended.Assigned"/> 
 *             &lt;xs:enumeration value="Open.NotRunning.Suspended.Reserved"/> 
 *             &lt;xs:enumeration value="Open.Running"/> 
 *             &lt;xs:enumeration value="Open.Running.InProgress"/> 
 *             &lt;xs:enumeration value="Open.Running.Suspended"/> 
 *             &lt;xs:enumeration value="Closed"/> 
 *             &lt;xs:enumeration value="Closed.Completed"/> 
 *             &lt;xs:enumeration value="Closed.Completed.Success"/> 
 *             &lt;xs:enumeration value="Closed.Completed.Failed"/> 
 *             &lt;xs:enumeration value="Closed.Cancelled"/> 
 *             &lt;xs:enumeration value="Closed.Cancelled.Exited"/> 
 *             &lt;xs:enumeration value="Closed.Cancelled.Error"/> 
 *             &lt;xs:enumeration value="Closed.Cancelled.Obsolete"/> 
 *             &lt;xs:enumeration value="Closed.Cancelled.Aborted"/> 
 *             &lt;xs:enumeration value="Closed.Cancelled.Terminated"/> 
 *          &lt;/xs:restriction>    
 *       &lt;/xs:simpleType>  
 *    &lt;/xs:list>  
 * &lt;/xs:simpleType>
 * </pre>
 * 
 * @author averab
 * @version 1.0.0 
 */
@XmlRootElement(name="State", namespace="http://www.wfmc.org/2009/BPAF2.0")
public enum State {
	/** State Open. */
	OPEN("Open"),
	/** State Open not running. */
	OPEN_NOT_RUNNING("Open.NotRunning"),
	/** State Open not running ready. */
	OPEN_NOT_RUNNING_READY("Open.NotRunning.Ready"),
	/** State Open not running assigned. */
	OPEN_NOT_RUNNING_ASSIGNED("Open.NotRunning.Assigned"),
	/** State Open not running reserved. */
	OPEN_NOT_RUNNING_RESERVED("Open.NotRunning.Reserved"),
	/** State Open not running suspended. */
	OPEN_NOT_RUNNING_SUSPENDED("Open.NotRunning.Suspended"),
	/** State Open not running suspended assigned. */
	OPEN_NOT_RUNNING_SUSPENDED_ASSIGNED("Open.NotRunning.Suspended.Assigned"),
	/** State Open not running suspended reserved. */
	OPEN_NOT_RUNNING_SUSPENDED_RESERVED("Open.NotRunning.Suspended.Reserved"),
	/** State Open running. */
	OPEN_RUNNING("Open.Running"),
	/** State Open running in progress. */
	OPEN_RUNNING_IN_PROGRESS("Open.Running.InProgress"),
	/** State Open running suspended. */
	OPEN_RUNNING_SUSPENDED("Open.Running.Suspended"),
	/** State Closed. */
	CLOSED("Closed"),
	/** State Closed completed. */
	CLOSED_COMPLETED("Closed.Completed"),
	/** State Closed completed success. */
	CLOSED_COMPLETED_SUCCESS("Closed.Completed.Success"),
	/** State Closed completed failed. */
	CLOSED_COMPLETED_FAILED("Closed.Completed.Failed"),
	/** State Closed cancelled. */
	CLOSED_CANCELLED("Closed.Cancelled"),
	/** State Closed cancelled exited. */
	CLOSED_CANCELLED_EXITED("Closed.Cancelled.Exited"),
	/** State Closed cancelled error. */
	CLOSED_CANCELLED_ERROR("Closed.Cancelled.Error"),	
	/** State Closed cancelled obsolete. */
	CLOSED_CANCELLED_OBSOLETE("Closed.Cancelled.Obsolete"),
	/** State Closed cancelled aborted. */
	CLOSED_CANCELLED_ABORTED("Closed.Cancelled.Aborted"),
	/** State Closed cancelled terminated. */
	CLOSED_CANCELLED_TERMINATED("Closed.Cancelled.Terminated");

	private final String value;

	/**
	 * Constructor for this enumerator class.
	 * 
	 * @param value
	 */
	private State(String value) {
		this.value = value;
	}

	/**
	 * Gets the enum current constant value.
	 * 
	 * @return enum current constant value.
	 */
	public String value() {
		return this.value;
	}
}