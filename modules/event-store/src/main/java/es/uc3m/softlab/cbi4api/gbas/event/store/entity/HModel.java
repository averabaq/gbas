/* 
 * $Id: HModel.java,v 1.0 2013-01-27 23:29:05 averab Exp $
 *
 * @copyright Universidad Carlos III de Madrid. proprietary/confidential. Use is subject to license terms.
 */
package es.uc3m.softlab.cbi4api.gbas.event.store.entity;

import es.uc3m.softlab.cbi4api.gbas.event.store.StaticResources;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PersistenceUnit;
import javax.persistence.Table;
import javax.persistence.Column;

/**
 * HModel entity class. This class is bound to the
 * <strong>model</strong> table at the data base through the EJB3
 * persistence layer. <br/>
 * Its fields are the following:
 * <ul>
 * <li><b>id</b>: HModel's identifier.</li>
 * <li><b>type</b>: HModel's type (or level) related to process/activity.</li>
 * <li><b>name</b>: HModel's name.</li>
 * <li><b>description</b>: HModel's description.</li>
 * </ul>
 * 
 * @author averab
 */
@Entity(name="event-store.HModel")
@Table(name="model", schema="event_store") 
@PersistenceUnit(name=StaticResources.PERSISTENCE_NAME_EVENT_STORE, unitName=StaticResources.PERSISTENCE_UNIT_NAME_EVENT_STORE)
public class HModel implements Serializable {
	/** Serial Version UID */
	private static final long serialVersionUID = 19293806384692541L;
	/** HModel identifier. */
	private long id;
	/** HModel type. */
	private String type;
	/** HModel source definition identifier. */
	private String modelSrcId;	
	/** HModel name. */
	private String name;
	/** HModel source. */
	private String source;	
	/** HModel description. */
	private String description;

	/**
	 * Creates a new object with null property values. 	 
	 */
	public HModel() {		
	}
	/**
	 * Gets the {@link #id} property.
	 * @return the {@link #id} property.
	 */
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public long getId() {
		return id;
	}
	/**
	 * Sets the {@link #id} property.
	 * @param id the {@link #id} property to set
	 */
	public void setId(long id) {
		this.id = id;
	}
	/**
	 * Gets the {@link #type} property.
	 * @return the {@link #type} property.
	 */
	@Column(name="type", length=50, nullable=false, updatable=false)
	public String getType() {
		return type;
	}
	/**
	 * Sets the {@link #type} property.
	 * @param type the {@link #type} property to set.
	 */
	public void setType(String type) {
		this.type = type;
	}
	/**
	 * Gets the {@link #name} property.
	 * @return the {@link #name} property.
	 */
	@Column(name="name", length=250, nullable=false)
	public String getName() {
		return name;
	}
	/**
	 * Sets the {@link #name} property.
	 * @param name the {@link #name} property to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * Gets the {@link #modelSrcId} property.
	 * @return the {@link #modelSrcId} property.
	 */
	@Column(name="model_src_id", length=250, nullable=false, updatable=false)
	public String getModelSrcId() {
		return modelSrcId;
	}
	/**
	 * Sets the {@link #modelSrcId} property.
	 * @param modelSrcId the {@link #modelSrcId} property to set.
	 */
	public void setModelSrcId(String modelSrcId) {
		this.modelSrcId = modelSrcId;
	}
	/**
	 * Gets the {@link #source} property.
	 * @return the {@link #source} property.
	 */
	@Column(name="source", length=150, nullable=false)
	public String getSource() {
		return source;
	}
	/**
	 * Sets the {@link #source} property.
	 * @param source the {@link #source} property to set.
	 */
	public void setSource(String source) {
		this.source = source;
	}
	/**
	 * Gets the {@link #description} property.
	 * @return the {@link #description} property.
	 */
	@Column(name="description")
	public String getDescription() {
		return description;
	}
	/**
	 * Sets the {@link #description} property.
	 * @param description the {@link #description} property to set.
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * Returns a string representation of the object.
	 * @return string representation of the object.
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("HModel [");
		builder.append(type);
		builder.append("](");
		builder.append(id);
		builder.append("){");
		builder.append(source);
		builder.append("}:");		
		builder.append(name);
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
		result = prime * result + (int) (id ^ (id >>> 32));
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
		HModel other = (HModel) obj;
		if (id != other.id)
			return false;
		return true;
	}
}
