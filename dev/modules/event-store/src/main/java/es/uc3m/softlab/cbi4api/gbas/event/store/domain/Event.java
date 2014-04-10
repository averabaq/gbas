/* 
 * $Id: Event.java,v 1.0 2013-01-27 23:29:05 averab Exp $
 *
 * @copyright Universidad Carlos III de Madrid. proprietary/confidential. Use is subject to license terms.
 */
package es.uc3m.softlab.cbi4api.gbas.event.store.domain;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

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
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="EventDetails">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;attribute name="CurrentState" use="required" type="{http://www.wfmc.org/2009/BPAF2.0}State" />
 *                 &lt;attribute name="PreviousState" type="{http://www.wfmc.org/2009/BPAF2.0}State" />
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="DataElement" type="{http://www.w3.org/2001/XMLSchema}anyType" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="EventID" use="required" type="{http://www.wfmc.org/2009/BPAF2.0}ID" />
 *       &lt;attribute name="ServerID" type="{http://www.w3.org/2001/XMLSchema}NMTOKEN" />
 *       &lt;attribute name="ProcessDefinitionID" use="required" type="{http://www.w3.org/2001/XMLSchema}NMTOKEN" />
 *       &lt;attribute name="ProcessInstanceID" use="required" type="{http://www.w3.org/2001/XMLSchema}NMTOKEN" />
 *       &lt;attribute name="ProcessName" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="ActivityDefinitionID" type="{http://www.w3.org/2001/XMLSchema}NMTOKEN" />
 *       &lt;attribute name="ActivityInstanceID" type="{http://www.w3.org/2001/XMLSchema}NMTOKEN" />
 *       &lt;attribute name="ActivityName" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="Timestamp" use="required" type="{http://www.w3.org/2001/XMLSchema}dateTime" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * @author averab
 * @version 1.0.0 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "eventDetails",
    "dataElement"
})
@XmlRootElement(name="Event", namespace="http://www.wfmc.org/2009/BPAF2.0")
public class Event implements Comparable<Event>, Serializable {
	/** Serial Version UID */
	private static final long serialVersionUID = 431274381493517L;
    /** Event identifier */
	@XmlAttribute(name = "EventID", required = true)
	private long eventID;
    /** Event process instance */
	@XmlTransient
	private ProcessInstance processInstance;
    /** Event activity instance */
    @XmlTransient
	private ActivityInstance activityInstance;
    /** Event timestamp */
    @XmlAttribute(name = "Timestamp", required = true)
    @XmlSchemaType(name = "dateTime")
	private Date timestamp;
	/** Event details */
    @XmlElement(name = "EventDetails", required = true)
	private EventDetail eventDetails;
    /** Event payload */    
    @XmlTransient
	private Set<EventPayload> payload;
    /** Event correlation data */ 
    @XmlTransient
	private Set<EventCorrelation> correlations;
    /** Event data elements */ 
    @XmlElement(name = "DataElement")
	private Set<EventData> dataElement;

	/**
	 * Creates a new object with null property values. 	 
	 */
	public Event() {		
	}
    /**
     * Gets the value of the eventID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     */
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
     * Gets the value of the serverID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     */
    @XmlAttribute(name = "ServerID")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NMTOKEN")
    public String getServerID() {
    	if (processInstance == null) 
    		return null;
    	if (processInstance.getModel() == null) 
   			return null;
    	if (processInstance.getModel().getSource() == null)
    		return null;
		return processInstance.getModel().getSource().getId();  
    }
	/**
	 * Gets the {@link #processInstance} property.
	 * @return the {@link #processInstance} property.
	 */
	@XmlTransient
	public ProcessInstance getProcessInstance() {
		return processInstance;
	}
	/**
	 * Sets the {@link #processInstance} property.
	 * @param processInstance the {@link #processInstance} property to set.
	 */
	public void setProcessInstance(ProcessInstance processInstance) {
		this.processInstance = processInstance;
	}	
	/**
     * Gets the value of the processDefinitionID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     */
    @XmlAttribute(name = "ProcessDefinitionID", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NMTOKEN")
    public String getProcessDefinitionID() {
    	if (processInstance == null) 
    		return null;    	
    	if (processInstance.getModel() == null) 
   			return null;
		return String.valueOf(processInstance.getModel().getId());
    }
    /**
     * Gets the value of the processInstanceID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     */
    @XmlAttribute(name = "ProcessInstanceID", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NMTOKEN")
    public String getProcessInstanceID() {
    	if (processInstance == null) 
    		return null;
   		return String.valueOf(processInstance.getId());
    }
    /**
     * Gets the value of the processName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     */
    @XmlAttribute(name = "ProcessName")
    public String getProcessName() {
    	if (processInstance == null)
    		return null;
        return processInstance.getName();
    }
	/**
	 * Gets the {@link #activityInstance} property.
	 * @return the {@link #activityInstance} property.
	 */
	@XmlTransient
	public ActivityInstance getActivityInstance() {
		return activityInstance;
	}
	/**
	 * Sets the {@link #activityInstance} property.
	 * @param activityInstance the {@link #activityInstance} property to set.
	 */
	public void setActivityInstance(ActivityInstance activityInstance) {
		this.activityInstance = activityInstance;
	}
	/**
     * Gets the value of the activityDefinitionID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     */
    @XmlAttribute(name = "ActivityDefinitionID")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NMTOKEN")
    public String getActivityDefinitionID() {
    	if (activityInstance == null) 
    		return null;
    	if (activityInstance.getModel() == null) 
   			return null;
		return String.valueOf(activityInstance.getModel().getId());    		
    }
    /**
     * Gets the value of the activityInstanceID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     */
    @XmlAttribute(name = "ActivityInstanceID")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NMTOKEN")
    public String getActivityInstanceID() {
    	if (activityInstance == null)
    		return null;
        return String.valueOf(activityInstance.getId());
    }
    /**
     * Gets the value of the activityName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     */
    @XmlAttribute(name = "ActivityName")
    public String getActivityName() {
    	if (activityInstance == null)
    		return null;
        return activityInstance.getName();
    }
    /**
     * Gets the value of the timestamp property.
     * 
     * @return
     *     possible object is
     *     {@link java.util.Date}
     *     
     */
    public Date getTimestamp() {
        return timestamp;
    }
    /**
     * Sets the value of the timestamp property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.util.Date}
     *     
     */
    public void setTimestamp(Date value) {
        this.timestamp = value;
    }
    /**
     * Gets the value of the eventDetails property.
     * 
     * @return
     *     possible object is
     *     {@link EventDetail}
     *     
     */
    public EventDetail getEventDetails() {
    	if (eventDetails == null) {
    		eventDetails = new EventDetail();
    	}    		
        return eventDetails;
    }
    /**
     * Sets the value of the eventDetails property.
     * 
     * @param value
     *     allowed object is
     *     {@link EventDetail}
     *     
     */
    public void setEventDetails(EventDetail value) {
        this.eventDetails = value;
    }
    /**
	 * Gets the {@link #payload} property.
	 * @return the {@link #payload} property.
	 */
	public Set<EventPayload> getPayload() {
    	if (this.payload == null) {
    		this.payload = new HashSet<EventPayload>();
    	}    
		return this.payload;
	}
	/**
	 * Sets the {@link #payload} property.
	 * @param payload the {@link #payload} property to set.
	 */
	public void setPayload(Set<EventPayload> payload) {
		this.payload = payload;
	}
	/**
	 * Gets the {@link #correlations} property.
	 * @return the {@link #correlations} property.
	 */
	public Set<EventCorrelation> getCorrelations() {
		if (this.correlations == null) {
    		this.correlations = new HashSet<EventCorrelation>();
    	}  
		return this.correlations;
	}
	/**
	 * Sets the {@link #correlations} property.
	 * @param correlations the {@link #correlations} property to set.
	 */
	public void setCorrelations(Set<EventCorrelation> correlations) {
		this.correlations = correlations;
	}
	/**
     * Gets the value of the data property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there should be not a <CODE>set</CODE> method for the dataElement property.
     * However there is because such method is required by the JPA and persistence layer.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getData().add(newItem);
     * </pre>
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list {@link Object }
     * 
     */    
    public Set<EventData> getDataElement() {
        if (dataElement == null) {
        	dataElement = new HashSet<EventData>();
        }
        return this.dataElement;
    }
	/**
	 * Sets the {@link #dataElement} property.
	 * @param dataElement the {@link #dataElement} property to set.
	 */
	public void setDataElement(Set<EventData> dataElement) {
		this.dataElement = dataElement;
	}
	/**
	 * Gets the {@link #dataElement} property as an ordered list object.
	 * @return the {@link #dataElement} property as an ordered list object.
	 */
	public List<EventData> getEventDataList() {
		if (this.dataElement == null)
			return null;
		EventData _data[] = new EventData[this.dataElement.size()];
		this.dataElement.toArray(_data);
		Arrays.sort(_data);		
		return Arrays.asList(_data);
	}	
	/**
	 * Gets the {@link #payload} property as an ordered list object.
	 * @return the {@link #payload} property as an ordered list object.
	 */
	public List<EventPayload> getEventPayloadList() {
		if (this.payload == null)
			return null;
		EventPayload _payload[] = new EventPayload[this.payload.size()];
		this.payload.toArray(_payload);
		Arrays.sort(_payload);		
		return Arrays.asList(_payload);
	}
	/**
	 * Gets the {@link #correlations} property as an ordered list object.
	 * @return the {@link #correlations} property as an ordered list object.
	 */
	public List<EventCorrelation> getEventCorrelationList() {
		if (this.correlations == null)
			return null;
		EventCorrelation _correlations[] = new EventCorrelation[this.correlations.size()];
		this.correlations.toArray(_correlations);
		Arrays.sort(_correlations);		
		return Arrays.asList(_correlations);
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
	public int compareTo(Event event) {
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
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
		StringBuilder builder = new StringBuilder();
		builder.append("Event [eventID=");
		builder.append(eventID);
		builder.append(", source=");
		builder.append(getServerID());
		builder.append(", processInstance=");
		builder.append(processInstance);
		builder.append(", activityInstance=");
		builder.append(activityInstance);
		builder.append(", timestamp=");
		builder.append(formatter.format(timestamp));
		builder.append(", eventDetails=");
		builder.append(eventDetails);
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
		Event other = (Event) obj;
		if (eventID != other.eventID)
			return false;
		return true;
	}
}