/* 
 * $Id: EventDetail.java,v 1.0 2013-01-27 23:29:05 averab Exp $
 *
 * @copyright Universidad Carlos III de Madrid. proprietary/confidential. Use is subject to license terms.
 */
package es.uc3m.softlab.cbi4api.gbas.event.store.domain;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

import es.uc3m.softlab.cbi4api.gbas.event.store.StaticResources;

/**
 * Event detail embedded into the
 * {@link es.uc3m.softlab.cbi4api.gbas.event.store.domain.Event} class.
 * 
 * <p>Its database fields are the following ones:
 * <ul>
 * <li><b>current_state</b>: Event current state.</li>
 * <li><b>previous_state</b>: Event previous state.</li>
 * </ul>
 * 
 * <p> This class is compliant with the 
 * <a href="http://www.wfmc.org/business-process-analytics-format.html"> BPAF
 * (Business Process Analytics Format)</a> specification standard published by
 * the <a href="http://www.wfmc.org">WfMC (Workflow Management Coalition)</a>.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;attribute name="CurrentState" use="required" type="{http://www.wfmc.org/2009/BPAF2.0}State" />
 *       &lt;attribute name="PreviousState" type="{http://www.wfmc.org/2009/BPAF2.0}State" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * @author averab
 * @version 1.0.0 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name=StaticResources.STRING_EMPTY)
public class EventDetail implements Serializable {
	/** Serial Version UID */
	private static final long serialVersionUID = 91504827318153973L;
	/** Event current state. */
	@XmlAttribute(name="CurrentState", required=true)
	private State currentState;
	/** Event previous state. */
	@XmlAttribute(name="PreviousState")
	private State previousState;

	/**
	 * Creates a new object with null property values. 	 
	 */
	public EventDetail() {		
	}
	/**
	 * Gets the {@link #currentState} property.
	 * @return the {@link #currentState} property.
	 */	
	public State getCurrentState() {
		return currentState;
	}
	/**
	 * Sets the {@link #currentState} property.
	 * @param currentState the {@link #currentState} property to set.
	 */
	public void setCurrentState(State currentState) {
		this.currentState = currentState;
	}
	/**
	 * Gets the {@link #previousState} property.
	 * @return the {@link #previousState} property.
	 */	
	public State getPreviousState() {
		return previousState;
	}
	/**
	 * Sets the {@link #previousState} property.
	 * @param previousState the {@link #previousState} property to set.
	 */
	public void setPreviousState(State previousState) {
		this.previousState = previousState;
	}
	/**
	 * Returns a string representation of the object.
	 * @return string representation of the object.
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("[");
		if (previousState != null) 
			builder.append(previousState + " ==> ");
		else
			builder.append(" >> ");
		builder.append(currentState);
		builder.append("]");
		return builder.toString();
	}
}
