/* 
 * $Id: EventDataPK.java,v 1.0 2013-01-27 23:29:05 averab Exp $
 *
 * @copyright Universidad Carlos III de Madrid. proprietary/confidential. Use is subject to license terms.
 */
package es.uc3m.softlab.cbi4api.gbas.event.store.domain;

import java.io.Serializable;

/**
 * EventDataPK (<i>Event data primary key</i>) composite primary 
 * key. This class is the primary key of 
 * {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.EventData}
 * entity class. 
 * 
 * @author averab
 * @version 1.0.0
 */
public class EventDataPK implements Serializable {
	/** Serial Version UID */
	private static final long serialVersionUID = 37821648796273L;
    /** Event data element key */
    private String key;
    /** Event owner of this data element */
    private Event event;
    
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
	 * Returns a string representation of the object.
	 * @return string representation of the object.
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("EventDataPK [key=");
		builder.append(key);
		builder.append(", event=");
		builder.append(event);
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
		result = prime * result + ((event == null) ? 0 : event.hashCode());
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
		EventDataPK other = (EventDataPK) obj;
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
		return true;
	}        
}