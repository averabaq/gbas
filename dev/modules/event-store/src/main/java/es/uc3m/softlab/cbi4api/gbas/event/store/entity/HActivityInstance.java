/* 
 * $Id: HActivityInstance.java,v 1.0 2013-01-27 23:29:05 averab Exp $
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
 * Activity instance entity class. This class is bound to the
 * <strong>activity_instance</strong> table at the data base through the EJB3
 * persistence layer. <br/>
 * Its fields are the following:
 * <ul>
 * <li><b>id</b>: Activity instance's identifier.</li>
 * <li><b>name</b>: Activity instance's name.</li>
 * <li><b>description</b>: Activity instance's description.</li>
 * <li><b>model</b>: Activity instance's model.</li>
 * <li><b>activity_src_id</b>: Activity instance identifier at source.</li>
 * <li><b>source</b>: Activity instance's source.</li> 
 * </ul>
 * 
 * @author averab
 */
@Entity(name="event-store.ActivityInstance")
@Table(name="activity_instance", schema="event_store")
@PersistenceUnit(name=StaticResources.PERSISTENCE_NAME_EVENT_STORE, unitName=StaticResources.PERSISTENCE_UNIT_NAME_EVENT_STORE)
/*@NamedQueries({
	@NamedQuery(name="activityFromProcessInstanceAndActivityName", 
		        query="select e.activityInstance from event-store.HEvent e where e.processInstance = :process and e.activityInstance.name = :activityName")
})*/
public class HActivityInstance implements Serializable {
	/** Serial Version UID */
	private static final long serialVersionUID = 54947203468236181L;
	/** Activity id */
	private long id;
	/** Activity name. */
	private String name;
	/** Activity description. */
	private String description;
	/** Activity model */
	private long model;
	/** Activity instance identifier at source */
	private String instanceSrcId;
	/** Activity instance's parent */
	private Long parent;

	/**
	 * Creates a new object with null property values. 	 
	 */
	public HActivityInstance() {		
	}
	/**
	 * Gets the {@link #id} property.
	 * @return the {@link #id} property.
	 */
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Long getId() {
		return id;
	}
	/**
	 * Sets the {@link #id} property.
	 * @param id the {@link #id} property to set
	 */
	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * Gets the {@link #name} property.
	 * @return the {@link #name} property.
	 */
	@Column(name="name", length=200, nullable=false, updatable=false)
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
	 * Gets the {@link #description} property.
	 * @return the {@link #description} property.
	 */
	@Column(name="description", updatable=false)
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
	 * Gets the {@link #model} property.
	 * @return the {@link #model} property.
	 */
	@Column(name="model", nullable=false, updatable=false)
	public long getModel() {
		return model;
	}
	/**
	 * Sets the {@link #model} property.
	 * @param model the {@link #model} property to set.
	 */
	public void setModel(long model) {
		this.model = model;
	}
	/**
	 * Gets the {@link #instanceSrcId} property.
	 * @return the {@link #instanceSrcId} property.
	 */
	@Column(name="instance_src_id", length=250, nullable=true, updatable=false)
	public String getInstanceSrcId() {
		return instanceSrcId;
	}
	/**
	 * Sets the {@link #instanceSrcId} property.
	 * @param instanceSrcId the {@link #instanceSrcId} property to set.
	 */
	public void setInstanceSrcId(String instanceSrcId) {
		this.instanceSrcId = instanceSrcId;
	}
	/**
	 * Gets the {@link #parent} property.
	 * @return the {@link #parent} property.
	 */
	@Column(name="parent", nullable=true, updatable=false)
	public Long getParent() {
		return parent;
	}
	/**
	 * Sets the {@link #parent} property.
	 * @param parent the {@link #parent} property to set.
	 */
	public void setParent(Long parent) {
		this.parent = parent;
	}			
	/**
	 * Returns a string representation of the object.
	 * @return string representation of the object.
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("HActivityInstance (");
		builder.append(id);
		builder.append("): ");
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
		HActivityInstance other = (HActivityInstance) obj;
		if (id != other.id)
			return false;
		return true;
	}
}
