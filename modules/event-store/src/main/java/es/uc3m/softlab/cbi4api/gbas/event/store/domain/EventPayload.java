/* 
 * $Id: EventPayload.java,v 1.0 2013-01-27 23:29:05 averab Exp $
 *
 * @copyright Universidad Carlos III de Madrid. proprietary/confidential. Use is subject to license terms.
 */
package es.uc3m.softlab.cbi4api.gbas.event.store.domain;

import java.io.Serializable;

/**
 * Event payload entity class. This class is bound to the
 * <strong>event_payload</strong> table at the data base through the EJB3
 * persistence layer. <br/>
 * Its fields are the following:
 * <ul>
 * <li><b>key</b>: Event payload key.</li>
 * <li><b>value</b>: Event payload value.</li>
 * <li><b>event</b>: Event associated.</li>
 * </ul>
 * 
 * @author averab
 */
public class EventPayload implements Comparable<EventPayload>, Serializable {
	/** Serial Version UID */
	private static final long serialVersionUID = 205884628381791L;
	/** Payload key */
	private String key;
	/** Payload value. */
	private String value;
	/** Event owner of this payload. */
	private Event event;

	/**
	 * Creates a new object with null property values. 	 
	 */
	public EventPayload() {		
	}
	/**
	 * Gets the {@link #key} property.
	 * @return the {@link #key} property.
	 */
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
	 * Gets the {@link #event} property.
	 * @return the {@link #event} property.
	 */
	public Event getEvent() {
		return event;
	}
	/**
	 * Sets the {@link #event} property.
	 * @param event the {@link #event} property to set.
	 */
	public void setEvent(Event event) {
		this.event = event;
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
	 * @param payload the object to be compared.
	 * @return a negative integer, zero, or a positive integer as this object is less than, equal to, or greater 
	 * than the specified object. 
	 */
	@Override
	public int compareTo(EventPayload payload) {
		if (payload == null)
			return -1;	
		if (key == null)
			return 1;
		return key.compareTo(payload.getKey()) ;
	}			
	/**
	 * Returns a string representation of the object.
	 * @return string representation of the object.
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Payload [");
		builder.append(event);
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
		result = prime * result + ((event == null) ? 0 : event.hashCode());
		result = prime * result + ((key == null) ? 0 : key.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
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
		EventPayload other = (EventPayload) obj;
		if (event == null) {
			if (other.event != null)
				return false;
		} else if (!event.equals(other.event))
			return false;
		if (key == null) {
			if (other.key != null)
				return false;
		} else if (!key.equals(other.key))
			return false;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}
}