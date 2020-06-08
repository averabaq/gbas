/* 
 * $Id: Event.java,v 1.0 2013-01-27 23:29:05 averab Exp $
 *
 * @copyright Universidad Carlos III de Madrid. proprietary/confidential. Use is subject to license terms.
 */
package es.uc3m.softlab.cbi4api.gbas.event.store.entity;

import es.uc3m.softlab.cbi4api.gbas.event.store.StaticResources;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PersistenceUnit;
import javax.persistence.Table;

/**
 * <p>Event entity class. This class is bound to the <strong>event</strong> table at the data base 
 * through the JPA persistence layer. 
 * 
 * <p>Its database fields are the following ones:
 * <ul>
 * <li><b>event_id</b>: Event identifier.</li>
 * <li><b>server_id</b>: Event server identifier.</li>
 * <li><b>process_def_id</b>: Event process definition identifier.</li>
 * <li><b>process_inst_id</b>: Event process instance identifier.</li>
 * <li><b>process_name</b>: Event process name.</li>
 * <li><b>activity_def_id</b>: Event activity definition identifier.</li>
 * <li><b>activity_inst_id</b>: Event activity instance identifier.</li>
 * <li><b>activity_name</b>: Event activity name.</li>
 * <li><b>timestamp</b>: Event timestamp.</li>
 * <li><b>current_state</b>: Event current state.</li>
 * <li><b>previous_state</b>: Event previous state.</li>
 * </ul>
 * 
 * <p> This class is compliant with the 
 * <a href="http://www.wfmc.org/business-process-analytics-format.html"> BPAF
 * (Business Process Analytics Format)</a> specification standard published by
 * the <a href="http://www.wfmc.org">WfMC (Workflow Management Coalition)</a>.
 *  
 * @author averab
 * @version 1.0.0 
 */
@Entity(name="event-store.Event")
@Table(name="event", schema="event_store")
@PersistenceUnit(name=StaticResources.PERSISTENCE_NAME_EVENT_STORE, unitName=StaticResources.PERSISTENCE_UNIT_NAME_EVENT_STORE)
/*@NamedQueries({
	@NamedQuery(name="eventsFromProcessInstance", 
		        query="select e from event-store.HEvent e where e.processInstance = :process order by timestamp desc"),
  	@NamedQuery(name="activityEventsFromProcessInstance", 
		        query="select e from event-store.HEvent e where e.processInstance = :process and e.activityInstance = :activity order by timestamp desc"),
   	@NamedQuery(name="processEventsFromProcessInstanceAtOrigin", 
		        query="select e from event-store.HEvent e where e.processInstance.instanceSrcId = :sourceId and e.processInstance.model.source = :source and e.activityInstance is null order by timestamp desc"),		        
   	@NamedQuery(name="processEventOfActivityInstance", 
		        query="select distinct e.processInstance from event-store.HEvent e where e.activityInstance = :activityInstance"),
   	@NamedQuery(name="instancesOfModelOrderedByExecutionTimeline", 
		        query="select e.processInstance.id from event-store.HEvent e where e.processInstance.model = :model group by e.processInstance.id order by min(e.timestamp), max(e.timestamp) asc")		        
})*/
public class HEvent implements Comparable<HEvent>, Serializable {
	/** Serial Version UID */
	private static final long serialVersionUID = 8504720225072841L;
    /** HEvent identifier */
	private long eventID;
    /** HEvent process instance */
    private long processInstance;
	/** HEvent process instance definition identifier */
	private long processDefinitionID;
    /** HEvent activity instance */
    private Long activityInstance;
    /** HEvent activity definition identifier */
    private Long activityDefinitionID;    
    /** HEvent timestamp */
    private Long timestamp;
	/** HEvent current state. */
	private String currentState;
	/** HEvent previous state. */
	private String previousState;

	/**
	 * Creates a new object with null property values. 	 
	 */
	public HEvent() {		
	}
    /**
     * Gets the value of the eventID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     */
	@Id
	@Column(name="event_id")
	@GeneratedValue(strategy=GenerationType.AUTO)
    public long getEventID() {
        return eventID;
    }
    /**
     * Sets the value of the eventID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     */
    public void setEventID(long value) {
        this.eventID = value;
    }
	/**
	 * Gets the {@link #processInstance} property.
	 * @return the processInstance property.
	 */
    @Column(name="process_instance", nullable=false, updatable=false)
	public long getProcessInstance() {
		return processInstance;
	}
	/**
	 * Sets the {@link #processInstance} property.
	 * @param processInstance the {@link #processInstance} to set
	 */
	public void setProcessInstance(long processInstance) {
		this.processInstance = processInstance;
	}
	/**
	 * Gets the {@link #activityInstance} property.
	 * @return the activityInstance property.
	 */
	@Column(name="activity_instance", nullable=true, updatable=false)
	public Long getActivityInstance() {
		return activityInstance;
	}
	/**
	 * Sets the {@link #activityInstance} property.
	 * @param activityInstance the {@link #activityInstance} to set
	 */
	public void setActivityInstance(Long activityInstance) {
		this.activityInstance = activityInstance;
	}
	/**
	 * Gets the {@link #processDefinitionID} property.
	 * @return the processDefinitionID property.
	 */
	@Column(name="process_model", nullable=true, updatable=false)
	public long getProcessDefinitionID() {
		return processDefinitionID;
	}
	/**
	 * Sets the {@link #processDefinitionID} property.
	 * @param processDefinitionID the {@link #processDefinitionID} to set
	 */
	public void setProcessDefinitionID(long processDefinitionID) {
		this.processDefinitionID = processDefinitionID;
	}
	/**
	 * Gets the {@link #activityDefinitionID} property.
	 * @return the activityDefinitionID property.
	 */
	@Column(name="activity_model", nullable=true, updatable=false)
	public Long getActivityDefinitionID() {
		return activityDefinitionID;
	}
	/**
	 * Sets the {@link #activityDefinitionID} property.
	 * @param activityDefinitionID the {@link #activityDefinitionID} to set
	 */
	public void setActivityDefinitionID(Long activityDefinitionID) {
		this.activityDefinitionID = activityDefinitionID;
	}
	/**
	 * Gets the {@link #timestamp} property.
	 * @return the timestamp property.
	 */
	//@Temporal(TemporalType.TIMESTAMP)
	@Column(name="tstamp", nullable=false, updatable=false)
	public Long getTimestamp() {
		return timestamp;
	}
	/**
	 * Sets the {@link #timestamp} property.
	 * @param timestamp the {@link #timestamp} to set
	 */
	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}
	/**
	 * Gets the {@link #currentState} property.
	 * @return the currentState property.
	 */
	@Column(name="current_state", length=100, nullable=false, updatable=false)
	public String getCurrentState() {
		return currentState;
	}
	/**
	 * Sets the {@link #currentState} property.
	 * @param currentState the {@link #currentState} to set
	 */
	public void setCurrentState(String currentState) {
		this.currentState = currentState;
	}
	/**
	 * Gets the {@link #previousState} property.
	 * @return the previousState property.
	 */
	@Column(name="previous_state", length=100, nullable=true, updatable=false)
	public String getPreviousState() {
		return previousState;
	}
	/**
	 * Sets the {@link #previousState} property.
	 * @param previousState the {@link #previousState} to set
	 */
	public void setPreviousState(String previousState) {
		this.previousState = previousState;
	}
	/**
	 * Compares this object with the specified object for order. Returns a negative integer, zero, or a positive 
	 * integer as this object is less than, equal to, or greater than the specified object. 
	 * The implementor must ensure sgn(x.compareTo(y)) == -sgn(y.compareTo(x)) for all x and y. (This implies that 
	 * x.compareTo(y) must throw an exception iff y.compareTo(x) throws an exception.)
	 * The implementor must also ensure that the relation is transitive: (x.compareTo(y)>0 && y.compareTo(z)>0) 
	 * implies x.compareTo(z)>0. Finally, the implementor must ensure that x.compareTo(y)==0 implies that 
	 * sgn(x.compareTo(z)) == sgn(y.compareTo(z)), for all z. 
	 * 
	 * @param event the object to be compared. 
	 * @return a negative integer, zero, or a positive integer as this object is less than, equal to, or greater 
	 * than the specified object. 
	 */
	@Override
	public int compareTo(HEvent event) {
		if (event == null)
			return -1;	
		if (timestamp == null)
			return 1;
		if (timestamp.equals(event.getTimestamp()))
			return Long.valueOf(eventID).compareTo(event.getEventID());
		return timestamp.compareTo(event.getTimestamp());
	}		
	/**
	 * Returns a string representation of the object.
	 * @return string representation of the object.
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("HEvent [eventID=");
		builder.append(eventID);
		builder.append(", processInstance=");
		builder.append(processInstance);
		builder.append(", activityInstance=");
		builder.append(activityInstance);
		builder.append(", timestamp=");
		builder.append(timestamp);
		builder.append(", currentState=");
		builder.append(currentState);
		builder.append(", previousState=");
		builder.append(previousState);
		builder.append("]");
		return builder.toString();
	}
	/**
	 * Returns a hash code value for the object.
	 * @return a hash code value for this object.
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (eventID ^ (eventID >>> 32));
		return result;
	}
	/**
	 * Indicates whether some other object is "equal to" this one.
	 * @param obj the reference object with which to compare.
	 * @return true if this object is the same as the obj argument; false otherwise.
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		HEvent other = (HEvent) obj;
		if (eventID != other.eventID)
			return false;
		return true;
	}
}