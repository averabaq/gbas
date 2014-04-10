/* 
 * $Id: EventConverter.java,v 1.0 2011-10-28 12:29:27 averab Exp $
 *
 * @copyright Universidad Carlos III de Madrid. proprietary/confidential. Use is subject to license terms.
 */
package es.uc3m.softlab.cbi4api.gbas.event.subscriber;

import es.uc3m.softlab.cbi4api.gbas.event.store.domain.Event;
import es.uc3m.softlab.cbi4api.gbas.event.store.facade.ActivityInstanceException;
import es.uc3m.softlab.cbi4api.gbas.event.store.facade.EventException;
import es.uc3m.softlab.cbi4api.gbas.event.store.facade.ModelException;
import es.uc3m.softlab.cbi4api.gbas.event.store.facade.ProcessInstanceException;
import es.uc3m.softlab.cbi4api.gbas.event.store.facade.SourceException;

/**
 * Component interface for converting the incoming events in BPAF-F4BPA extended format 
 * into an BPAF entity object model.
 * 
 * <p> This class follows the  
 * <a href="http://www.wfmc.org/business-process-analytics-format.html"> BPAF
 * (Business Process Analytics Format)</a> specification standard published by
 * the <a href="http://www.wfmc.org">WfMC (Workflow Management Coalition) by
 * extracting the event data from an extended f4bpa-bpaf format and transforming its content 
 * into an BPAF entity object model.</a>. 
 * 
 * @author averab
 * @version 1.0.0
 */
public interface EventConverter {
 	/** Spring component name */
    public static final String COMPONENT_NAME = StaticResources.COMPONENT_NAME_EVENT_CONVERTER;        

    /**
     * Convert the input event contents in BPAF-F4BPA extension format into 
     * modeled <strong>BPAF</strong> entity beans.
     * 
     * @param event event in BPAF-F4BPA extension format.
     * @return event in BPAF entity object model.
     * @throws ModelException if any model exception occurred during processing.
     * @throws ProcessInstanceException if any process instance exception occurred during processing.
     * @throws ActivityInstanceException if any activity instance exception occurred during processing.
     * @throws EventException if any event exception occurred during processing.
     * @throws SourceException if the source it is not defined at the database.
     */
    public Event transform(es.uc3m.softlab.cbi4api.gbas.event.subscriber.xsd.gbas.event.Event event) throws SourceException, ModelException, ProcessInstanceException, ActivityInstanceException, EventException;
}