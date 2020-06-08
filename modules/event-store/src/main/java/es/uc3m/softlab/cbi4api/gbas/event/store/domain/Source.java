/* 
 * $Id: Source.java,v 1.0 2013-01-27 23:29:05 averab Exp $
 *
 * @copyright Universidad Carlos III de Madrid. proprietary/confidential. Use is subject to license terms.
 */
package es.uc3m.softlab.cbi4api.gbas.event.store.domain;

import java.io.Serializable;

/**
 * Source entity class. This class is bound to the
 * <strong>source</strong> table at the data base through the EJB3
 * persistence layer. <br/>
 * Its fields are the following:
 * <ul>
 * <li><b>id</b>: Event source identifier.</li>
 * <li><b>name</b>: Event source's name.</li>
 * <li><b>description</b>: Event source's description.</li>
 * <li><b>inet_address</b>: Event source's inet address.</li>
 * <li><b>port</b>: Event source's port.</li>
 * </ul>
 * 
 * @author averab
 */
public class Source implements Serializable {
	/** Serial Version UID */
	private static final long serialVersionUID = 7374182640173L;
	/** Event source identifier. */
	private String id;
	/** Event previous state. */
	private String name;
	/** Event previous state. */
	private String description;	
	/** Event source inet address. */
	private String inetAddress;
	/** Event source port. */
	private Integer port;

	/**
	 * Creates a new object with null property values. 	 
	 */
	public Source() {		
	}
	/**
	 * Gets the {@link #id} property.
	 * @return the {@link #id} property.
	 */
	public String getId() {
		return id;
	}
	/**
	 * Sets the {@link #id} property.
	 * @param id the {@link #id} property to set.
	 */
	public void setId(String id) {
		this.id = id;
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
	 * @param name the {@link #name} property to set.
	 */
	public void setName(String name) {
		this.name = name;
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
	 * Gets the {@link #inetAddress} property.
	 * @return the {@link #inetAddress} property.
	 */
	public String getInetAddress() {
		return inetAddress;
	}
	/**
	 * Sets the {@link #inetAddress} property.
	 * @param inetAddress the {@link #inetAddress} property to set.
	 */
	public void setInetAddress(String inetAddress) {
		this.inetAddress = inetAddress;
	}
	/**
	 * Gets the {@link #port} property.
	 * @return the {@link #port} property.
	 */
	public Integer getPort() {
		return port;
	}
	/**
	 * Sets the {@link #port} property.
	 * @param port the {@link #port} property to set.
	 */
	public void setPort(Integer port) {
		this.port = port;
	}
	/**
	 * Returns a string representation of the object.
	 * @return string representation of the object.
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Source (");
		builder.append(id);
		builder.append(")[");
		builder.append(name);
		builder.append("]: ");
		if (description != null)
			builder.append(description);
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
		Source other = (Source) obj;
		if ((!id.equals(other.id)))
			return false;
		return true;
	}
}
