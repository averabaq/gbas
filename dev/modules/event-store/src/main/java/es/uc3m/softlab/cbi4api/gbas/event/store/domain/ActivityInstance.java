/* 
 * $Id: ActivityInstance.java,v 1.0 2013-01-27 23:29:05 averab Exp $
 *
 * @copyright Universidad Carlos III de Madrid. proprietary/confidential. Use is subject to license terms.
 */
package es.uc3m.softlab.cbi4api.gbas.event.store.domain;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.io.Serializable;

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
public class ActivityInstance implements Serializable {
	/** Serial Version UID */
	private static final long serialVersionUID = 54947203468236181L;
	/** Activity id */
	private Long id;
	/** Activity name. */
	private String name;
	/** Activity description. */
	private String description;
	/** Activity model */
	private Model model;
	/** Activity instance identifier at source */
	private String instanceSrcId;
	/** Activity instance's parent */
	private ActivityInstance parent;
	/** Activity sub-activities instance's */
	private List<ActivityInstance> subactivities;
	/** Activity instance event */
	private List<Event> events;

	/**
	 * Creates a new object with null property values. 	 
	 */
	public ActivityInstance() {		
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
	 * Gets the {@link #model} property.
	 * @return the {@link #model} property.
	 */
	public Model getModel() {
		return model;
	}
	/**
	 * Sets the {@link #model} property.
	 * @param model the {@link #model} property to set.
	 */
	public void setModel(Model model) {
		this.model = model;
	}
	/**
	 * Gets the {@link #instanceSrcId} property.
	 * @return the {@link #instanceSrcId} property.
	 */
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
	public ActivityInstance getParent() {
		return parent;
	}
	/**
	 * Sets the {@link #parent} property.
	 * @param parent the {@link #parent} property to set.
	 */
	public void setParent(ActivityInstance parent) {
		this.parent = parent;
	}
	/**
	 * Gets the {@link #events} property.
	 * @return the {@link #events} property.
	 */
	public List<Event> getEvents() {
		return events;
	}
	/**
	 * Sets the {@link #events} property.
	 * @param events the {@link #events} property to set.
	 */
	public void setEvents(List<Event> events) {
		this.events = events;
	}
	/**
	 * Gets the {@link #events} property as an ordered list object.
	 * @return the {@link #events} property as an ordered list object.
	 */
	public List<Event> getEventList() {
		if (this.events == null)
			return null;
		Event _events[] = new Event[this.events.size()];
		this.events.toArray(_events);
		Arrays.sort(_events);		
		return Arrays.asList(_events);
	}		
	/**
	 * Gets the {@link #subactivities} property.
	 * @return the {@link #subactivities} property.
	 */
	public List<ActivityInstance> getSubactivities() {
		return subactivities;
	}
	/**
	 * Sets the {@link #subactivities} property.
	 * @param subactivities the {@link #subactivities} property to set.
	 */
	public void setSubactivities(List<ActivityInstance> subactivities) {
		this.subactivities = subactivities;
	}
	/**
	 * Gets the start time of the current activity instance.
	 * @return the start time of the current activity instance.
	 */
	public Date getStartTime() {
		/* if the events are not loaded, it cannot get the start time */
		if (events == null)
			return null;
		Event _events[] = new Event[events.size()];
		events.toArray(_events);
		Arrays.sort(_events);		
		setEvents(Arrays.asList(_events));
		return getEventList().get(0).getTimestamp();
	}	
	/**
	 * Gets the end time of the current activity instance.
	 * @return the end time of the current activity instance.
	 */
	public Date getEndTime() {
		/* if the events are not loaded, it cannot get the end time */
		if (events == null)
			return null;
		Event _events[] = new Event[events.size()];
		events.toArray(_events);
		Arrays.sort(_events);		
		setEvents(Arrays.asList(_events));
		return getEventList().get(getEventList().size() - 1).getTimestamp();
	}		
	/**
	 * Gets the time around metric of the current activity instance.
	 * @return the time around metric of the current activity instance.
	 */
	public Long getTurnAround() {
		/* if the events are not loaded, it cannot get the turn around time */
		if (events == null)
			return null;
		Event _events[] = new Event[events.size()];
		events.toArray(_events);
		Arrays.sort(_events);		
		setEvents(Arrays.asList(_events));
		long start = getEventList().get(0).getTimestamp().getTime();
		long end = getEventList().get(getEventList().size() - 1).getTimestamp().getTime();
		return end - start;
	}	
	/**
	 * Gets the waiting time metric of the current activity instance.
	 * @return the waiting time metric of the current activity instance.
	 */
	public Long getWaitingTime() {
		/* if the events are not loaded, it cannot get the waiting time */
		if (events == null)
			return null;		
		Event _events[] = new Event[events.size()];
		events.toArray(_events);
		Arrays.sort(_events);		
		setEvents(Arrays.asList(_events));
				
		Date start = null;
		Date end = null;
		Long watingTime = 0L;
		boolean assigned = false;
		for (Event event : getEventList()) {			
			if (!assigned && 
				event.getEventDetails().getCurrentState() == State.OPEN_NOT_RUNNING_ASSIGNED) {
				start = event.getTimestamp();
				assigned = true;
			}
			if (assigned && 
				event.getEventDetails().getCurrentState() == State.OPEN_NOT_RUNNING_RESERVED) {
				end = event.getTimestamp();
				assigned = false;
				watingTime += end.getTime() - start.getTime();
			}			
		}		
		return watingTime;
	}		
	/**
	 * Gets the change over time metric of the current activity instance.
	 * @return the change over time metric of the current activity instance.
	 */
	public Long getChangeOverTime() {
		/* if the events are not loaded, it cannot get the change over time */
		if (events == null)
			return null;		
		Event _events[] = new Event[events.size()];
		events.toArray(_events);
		Arrays.sort(_events);		
		setEvents(Arrays.asList(_events));
				
		Date start = null;
		Date end = null;
		Long notRunningTime = 0L;
		boolean notRunning = true;
		for (Event event : getEventList()) {			
			if (notRunning && 
				event.getEventDetails().getCurrentState() == State.OPEN_NOT_RUNNING ||
				event.getEventDetails().getCurrentState() == State.OPEN_NOT_RUNNING_READY ||
				event.getEventDetails().getCurrentState() == State.OPEN_NOT_RUNNING_ASSIGNED ||
				event.getEventDetails().getCurrentState() == State.OPEN_NOT_RUNNING_RESERVED ||
				event.getEventDetails().getCurrentState() == State.OPEN_NOT_RUNNING_SUSPENDED ||
				event.getEventDetails().getCurrentState() == State.OPEN_NOT_RUNNING_SUSPENDED_ASSIGNED ||
				event.getEventDetails().getCurrentState() == State.OPEN_NOT_RUNNING_SUSPENDED_RESERVED) {
				start = event.getTimestamp();
				notRunning = false;
			}
			if (!notRunning && 
				event.getEventDetails().getCurrentState() == State.OPEN_RUNNING ||
				event.getEventDetails().getCurrentState() == State.OPEN_RUNNING_IN_PROGRESS ||
				event.getEventDetails().getCurrentState() == State.OPEN_RUNNING_SUSPENDED) {
				end = event.getTimestamp();
				notRunning = true;
				notRunningTime += end.getTime() - start.getTime();
			}			
		}		
		return notRunningTime - getWaitingTime();
	}	
	/**
	 * Gets the change over time metric of the current activity instance.
	 * @return the change over time metric of the current activity instance.
	 */
	public Long getSuspendedTime() {
		/* if the events are not loaded, it cannot get the change over time */
		if (events == null)
			return null;		
		Event _events[] = new Event[events.size()];
		events.toArray(_events);
		Arrays.sort(_events);		
		setEvents(Arrays.asList(_events));
				
		Date start = null;
		Date end = null;
		Long suspendingTime = 0L;
		boolean running = true;
		for (Event event : getEventList()) {			
			if (running && 
				event.getEventDetails().getCurrentState() == State.OPEN_RUNNING ||
				event.getEventDetails().getCurrentState() == State.OPEN_RUNNING_IN_PROGRESS) {
				start = event.getTimestamp();
				running = false;
			}
			if (!running && 
				event.getEventDetails().getCurrentState() == State.OPEN_NOT_RUNNING ||
				event.getEventDetails().getCurrentState() == State.OPEN_NOT_RUNNING_READY ||
				event.getEventDetails().getCurrentState() == State.OPEN_NOT_RUNNING_ASSIGNED ||
				event.getEventDetails().getCurrentState() == State.OPEN_NOT_RUNNING_RESERVED ||
				event.getEventDetails().getCurrentState() == State.OPEN_NOT_RUNNING_SUSPENDED ||
				event.getEventDetails().getCurrentState() == State.OPEN_NOT_RUNNING_SUSPENDED_ASSIGNED ||
				event.getEventDetails().getCurrentState() == State.OPEN_NOT_RUNNING_SUSPENDED_RESERVED) {				
				running = true;				
			}	
			if (!running && 
				event.getEventDetails().getCurrentState() == State.OPEN_RUNNING_SUSPENDED) {
				end = event.getTimestamp();
				suspendingTime += end.getTime() - start.getTime();
				running = true;				
			}
		}		
		return suspendingTime;
	}		
	/**
	 * Gets the processing time metric of the current activity instance.
	 * @return the processing time metric of the current activity instance.
	 */
	public Long getProcessingTime() {
		/* if the events are not loaded, it cannot get the processing time */
		if (events == null)
			return null;		
		return getTurnAround() - getWaitingTime() - getChangeOverTime() - getSuspendedTime();
	}				
	/**
	 * Returns a string representation of the object.
	 * @return string representation of the object.
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ActivityInstance (");
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
		ActivityInstance other = (ActivityInstance) obj;
		if (id != other.id)
			return false;
		return true;
	}
}
