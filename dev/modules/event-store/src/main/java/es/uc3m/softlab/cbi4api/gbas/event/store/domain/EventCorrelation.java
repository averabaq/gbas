/* 
 * $Id: EventCorrelation.java,v 1.0 2013-01-27 23:29:05 averab Exp $
 *
 * @copyright Universidad Carlos III de Madrid. proprietary/confidential. Use is subject to license terms.
 */
package es.uc3m.softlab.cbi4api.gbas.event.store.domain;

import java.io.Serializable;

/**
 * <p>Event correlation entity class. This class is bound to the <strong>event_correlation</strong> table at the data base 
 * through the JPA persistence layer. 
 * 
 * <p>Its database fields are the following ones:
 * <ul>
 * <li><b>key</b>: Event correlation key.</li>
 * <li><b>value</b>: Event correlation value.</li>
 * <li><b>event</b>: Event identifier which owns this correlation data.</li>
 * </ul>
 * 
 * @author averab
 * @version 1.0.0 
 */
public class EventCorrelation implements Comparable<EventCorrelation>, Serializable {
	/** Serial Version UID */
	private static final long serialVersionUID = 1847387461208318702L;
    /** Event correlation key */
    protected String key;
    /** Event correlation value */
    protected String value;
    /** Event associated of this correlation data */
    protected Event event;

	/**
	 * Creates a new object with null property values. 	 
	 */
	public EventCorrelation() {		
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
	 * @param correlation the object to be compared.
	 * @return a negative integer, zero, or a positive integer as this object is less than, equal to, or greater 
	 * than the specified object. 
	 */
	@Override
	public int compareTo(EventCorrelation correlation) {
		if (correlation == null)
			return -1;	
		if (key == null)
			return 1;
		return key.compareTo(correlation.getKey()) ;
	}	
	/**
	 * Returns a string representation of the object.
	 * @return string representation of the object.
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Correlation [event=");
		builder.append(event);
		builder.append(", key=");
		builder.append(key);
		builder.append(", value=");
		builder.append(value);
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
		EventCorrelation other = (EventCorrelation) obj;
		if (key == null) {
			if (other.key != null)
				return false;
		} else if (!key.equals(other.key))
			return false;
		return true;
	}
}