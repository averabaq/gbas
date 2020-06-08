/* 
 * $Id: EventCorrelation.java,v 1.0 2013-01-27 23:29:05 averab Exp $
 *
 * @copyright Universidad Carlos III de Madrid. proprietary/confidential. Use is subject to license terms.
 */
package es.uc3m.softlab.cbi4api.gbas.event.store.entity;

import es.uc3m.softlab.cbi4api.gbas.event.store.StaticResources;

import java.io.Serializable;
import java.util.Arrays;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.PersistenceUnit;
import javax.persistence.Table;

/**
 * <p>Event correlation index entity class. This class is bound to the <strong>idx_event_correlation</strong> table at the data base 
 * through the JPA persistence layer. 
 * 
 * <p>Its database fields are the following ones:
 * <ul>
 * <li><b>correlationSet</b>: Event correlation data set as index key.</li>
 * <li><b>event_id</b>: Event identifier which owns this correlation data.</li>
 * </ul>
 * 
 * @author averab
 * @version 1.0.0 
 */
@Entity(name="event-store.EventCorrelationIndex")
@Table(name="idx_event_correlation", schema="event_store")
@PersistenceUnit(name=StaticResources.PERSISTENCE_NAME_EVENT_STORE, unitName=StaticResources.PERSISTENCE_UNIT_NAME_EVENT_STORE)
public class HEventCorrelationIndex implements Serializable {
	/** Serial Version UID */
	private static final long serialVersionUID = 15938046238082042L;
	/** Raw correlation set */
	private byte[] correlationSet;
	/** Correlation event ID. */
	private long eventID;

	/**
	 * Creates a new object with null property values. 	 
	 */
	public HEventCorrelationIndex() {		
	}
	/**
	 * Gets the {@link #correlationSet} property.
	 * @return the correlationSet property.
	 */
	@Id
	@Column(name="correlation_set", nullable=false)
	public byte[] getCorrelationSet() {
		return correlationSet;
	}
	/**
	 * Sets the {@link #correlationSet} property.
	 * @param correlationSet the {@link #correlationSet} to set
	 */
	public void setCorrelationSet(byte[] correlationSet) {
		this.correlationSet = correlationSet;
	}
	/**
	 * Gets the {@link #eventID} property.
	 * @return the {@link #eventID} property.
	 */
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
	 * Returns a string representation of the object.
	 * @return string representation of the object.
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Correlation Set [");
		builder.append(correlationSet);
		builder.append("]: (");
		builder.append(eventID);
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
		result = prime * result + Arrays.hashCode(correlationSet);
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
		HEventCorrelationIndex other = (HEventCorrelationIndex) obj;
		if (!Arrays.equals(correlationSet, other.correlationSet))
			return false;
		return true;
	}
}