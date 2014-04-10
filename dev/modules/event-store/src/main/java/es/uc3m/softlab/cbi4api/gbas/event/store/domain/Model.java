/* 
 * $Id: Model.java,v 1.0 2013-01-27 23:29:05 averab Exp $
 *
 * @copyright Universidad Carlos III de Madrid. proprietary/confidential. Use is subject to license terms.
 */
package es.uc3m.softlab.cbi4api.gbas.event.store.domain;

import java.io.Serializable;

/**
 * Model entity class. This class is bound to the
 * <strong>model</strong> table at the data base through the EJB3
 * persistence layer. <br/>
 * Its fields are the following:
 * <ul>
 * <li><b>id</b>: Model's identifier.</li>
 * <li><b>type</b>: Model's type (or level) related to process/activity.</li>
 * <li><b>name</b>: Model's name.</li>
 * <li><b>description</b>: Model's description.</li>
 * </ul>
 * 
 * @author averab
 */
public class Model implements Serializable {
	/** Serial Version UID */
	private static final long serialVersionUID = 19293806384692541L;
	/** Model identifier. */
	private Long id;
	/** Model type. */
	private ModelType type;
	/** Model source definition identifier. */
	private String modelSrcId;	
	/** Model name. */
	private String name;
	/** Model source. */
	private Source source;	
	/** Model description. */
	private String description;

	/**
	 * Creates a new object with null property values. 	 
	 */
	public Model() {		
	}
	/**
	 * Gets the {@link #id} property.
	 * @return the {@link #id} property.
	 */
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
	 * Gets the {@link #type} property.
	 * @return the {@link #type} property.
	 */
	public ModelType getType() {
		return type;
	}
	/**
	 * Sets the {@link #type} property.
	 * @param type the {@link #type} property to set.
	 */
	public void setType(ModelType type) {
		this.type = type;
	}
	/**
	 * Gets the {@link #name} property.
	 * @return the {@link #name} property.
	 */
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
	public Source getSource() {
		return source;
	}
	/**
	 * Sets the {@link #source} property.
	 * @param source the {@link #source} property to set.
	 */
	public void setSource(Source source) {
		this.source = source;
	}
	/**
	 * Gets the {@link #description} property.
	 * @return the {@link #description} property.
	 */
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
		builder.append("Model [");
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
        result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		Model other = (Model) obj;
		if (id != other.id)
			return false;
		return true;
	}
}