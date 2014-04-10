/* 
 * $Id: HEventPayloadIdKey.java,v 1.0 2013-01-27 23:29:05 averab Exp $
 *
 * @copyright Universidad Carlos III de Madrid. proprietary/confidential. Use is subject to license terms.
 */
package es.uc3m.softlab.cbi4api.gbas.event.store.entity;

import java.io.Serializable;

/**
 * <p>Event payload primary key class. This class is bound to the <strong>event_correlation</strong> table at the data base 
 * through the JPA persistence layer. 
 * 
 * <p>Its database fields are the following ones:
 * <ul>
 * <li><b>event</b>: Event identifier which owns this correlation data.</li>
 * <li><b>key</b>: Event correlation key.</li>
 * </ul>
 * 
 * @author averab
 * @version 1.0.0 
 */
public class HEventPayloadIdKey implements Serializable {
	/** Serial Version UID */
	private static final long serialVersionUID = 8347419638164912L;
	/** Correlation event ID. */
	private long eventID;
	/** Correlation key */
	private String key;

	/**
	 * Creates a new object with null property values. 	 
	 */
	public HEventPayloadIdKey() {		
	}
	/**
	 * Gets the {@link #key} property.
	 * @return the key property.
	 */
	public String getKey() {
		return key;
	}
	/**
	 * Sets the {@link #key} property.
	 * @param key the {@link #key} to set
	 */
	public void setKey(String key) {
		this.key = key;
	}
	/**
	 * Gets the {@link #eventID} property.
	 * @return the eventID property.
	 */
	public long getEventID() {
		return eventID;
	}
	/**
	 * Sets the {@link #eventID} property.
	 * @param eventID the {@link #eventID} to set
	 */
	public void setEventID(long eventID) {
		this.eventID = eventID;
	}			
	/**
	 * Returns a string representation of the object.
	 * @return string representation of the object.
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Correlation Key [");
		builder.append(eventID);
		builder.append(", ");
		builder.append(key);
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
		HEventPayloadIdKey other = (HEventPayloadIdKey) obj;
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