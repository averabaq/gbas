/* 
 * $Id: EventData.java,v 1.0 2013-01-27 23:29:05 averab Exp $
 *
 * @copyright Universidad Carlos III de Madrid. proprietary/confidential. Use is subject to license terms.
 */
package es.uc3m.softlab.cbi4api.gbas.event.store.entity;

import es.uc3m.softlab.cbi4api.gbas.event.store.StaticResources;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.PersistenceUnit;
import javax.persistence.Table;

/**
 * <p>Event data entity class. This class is bound to the <strong>event_data</strong> table at the data base 
 * through the JPA persistence layer. 
 * 
 * <p>Its database fields are the following ones:
 * <ul>
 * <li><b>key</b>: Event data key.</li>
 * <li><b>value</b>: Event data value.</li>
 * <li><b>event</b>: Event identifier which owns this current data.</li>
 * </ul>
 * 
 * @author averab
 * @version 1.0.0 
 */
@Entity(name="event-store.EventData")
@Table(name="event_data", schema="event_store")
@IdClass(HEventDataIdKey.class)
@PersistenceUnit(name=StaticResources.PERSISTENCE_NAME_EVENT_STORE, unitName=StaticResources.PERSISTENCE_UNIT_NAME_EVENT_STORE)
public class HEventData implements Comparable<HEventData>, Serializable {
	/** Serial Version UID */
	private static final long serialVersionUID = 592740117290173801L;
	/** Data event ID. */
	private long eventID;
	/** Data key */
	private String key;
	/** Data value. */
	private String value;

	/**
	 * Creates a new object with null property values. 	 
	 */
	public HEventData() {		
	}
	/**
	 * Gets the {@link #eventID} property.
	 * @return the {@link #eventID} property.
	 */
	@Id
	@Column(name="event_id", nullable=false)
	public long getEventID() {
		return eventID;
	}
	/**
	 * Sets the {@link #eventID} property.
	 * @param eventID the {@link #eventID} property to set.
	 */
	public void setEventID(long eventID) {
		this.eventID = eventID;
	}	
	/**
	 * Gets the {@link #key} property.
	 * @return the {@link #key} property.
	 */
	@Id
	@Column(name="key", length=250, nullable=false, updatable=false)
	public String getKey() {
		return key;
	}
	/**
	 * Sets the {@link #key} property.
	 * @param key the {@link #key} property to set.
	 */
	public void setKey(String key) {
		this.key = key;
	}
	/**
	 * Gets the {@link #value} property.
	 * @return the {@link #value} property.
	 */
	@Column(name="value", nullable=false, updatable=false)
	public String getValue() {
		return value;
	}
	/**
	 * Sets the {@link #value} property.
	 * @param value the {@link #value} property to set.
	 */
	public void setValue(String value) {
		this.value = value;
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
	 * @param data the object to be compared.
	 * @return a negative integer, zero, or a positive integer as this object is less than, equal to, or greater 
	 * than the specified object. 
	 */
	@Override
	public int compareTo(HEventData data) {
		if (data == null)
			return -1;	
		if (eventID != data.getEventID())
			return Long.valueOf(eventID).compareTo(data.getEventID());
		if (key == null)
			return 1;
		return key.compareTo(data.getKey());
	}			
	/**
	 * Returns a string representation of the object.
	 * @return string representation of the object.
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Data [");
		builder.append(eventID);
		builder.append("]: (");
		builder.append(key);
		builder.append(",");
		builder.append(value);
		builder.append(").");		
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
		result = prime * result + ((key == null) ? 0 : key.hashCode());
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
		HEventData other = (HEventData) obj;
		if (eventID != other.eventID)
			return false;
		if (key == null) {
			if (other.key != null)
				return false;
		} else if (!key.equals(other.key))
			return false;
		return true;
	}
}