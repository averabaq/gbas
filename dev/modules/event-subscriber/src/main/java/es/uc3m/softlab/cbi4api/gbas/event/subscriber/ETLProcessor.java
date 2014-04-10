/* 
 * $Id: ETLEvent.java,v 1.0 2013-01-27 23:29:05 averab Exp $
 *
 * @copyright Universidad Carlos III de Madrid. proprietary/confidential. Use is subject to license terms.
 */
package es.uc3m.softlab.cbi4api.gbas.event.subscriber;

import es.uc3m.softlab.cbi4api.gbas.event.store.facade.ActivityInstanceException;
import es.uc3m.softlab.cbi4api.gbas.event.store.facade.EventException;
import es.uc3m.softlab.cbi4api.gbas.event.store.facade.ModelException;
import es.uc3m.softlab.cbi4api.gbas.event.store.facade.ProcessInstanceException;
import es.uc3m.softlab.cbi4api.gbas.event.store.facade.SourceException;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Specific ETL (Extract, Transform & Load) service interface for GBAS-BPAF
 * events as an extension of <a href="http://www.wfmc.org/business-process-analytics-format.html">BPAF</a>.
 * 
 * <p> This class follows the  
 * <a href="http://www.wfmc.org/business-process-analytics-format.html"> BPAF
 * (Business Process Analytics Format)</a> specification standard published by
 * the <a href="http://www.wfmc.org">WfMC (Workflow Management Coalition) by
 * extracting the event data from heterogeneous systems, transforming its GBAS-BPAF xml content
 * into the BPAF entity object model, and loading the results into the database.</a>. 
 * 
 * @author averab
 * @version 1.0.0
 */
@Service(value=StaticResources.SERVICE_NAME_ETL_PROCESSOR)
public class ETLProcessor implements Processor {
    /** Log for tracing */
    private static final Logger logger = Logger.getLogger(ETLProcessor.class);  
    /** Event reader to extract the event data from the jms messages */
	@Autowired private EventReader extractor;
    /** Event converter to transform  into the database */
    @Autowired private EventConverter transformer; 
    /** Event writer to store the formatted event into the database */
    @Autowired private EventWriter loader; 

	/**
	 * Passes exchange data object to this processor.
	 * @param exchange data object to process.
	 */
    @Override
	public void process(Exchange exchange) throws Exception {	
		try {					
			es.uc3m.softlab.cbi4api.gbas.event.subscriber.xsd.gbas.event.Event gbasEvent;
			// extract gbas event
            gbasEvent = extractor.extractEvent(exchange);
			// this block has to be synchronized as the events must be processed
			// in strictly order as they arrive
			synchronized(this) {
				// transform event 
				es.uc3m.softlab.cbi4api.gbas.event.store.domain.Event bpafEvent = transformer.transform(gbasEvent);
				// load the event into the data source and forwards the gbas event to the output channel
				loader.loadEvent(exchange, bpafEvent, gbasEvent);
			}
		} catch(ProcessInstanceException piex) {			
			logger.error(piex.fillInStackTrace());			
		} catch(ActivityInstanceException aiex) {			
			logger.error(aiex.fillInStackTrace());			
		} catch(EventReaderException erex) {
			logger.error(erex.fillInStackTrace());		
		} catch(SourceException sex) {
			logger.error(sex.fillInStackTrace());		
		} catch(ModelException mex) {
			logger.error(mex.fillInStackTrace());		
		} catch(EventException evex) {
			if (evex.getCode() == StaticResources.WARN_EVENT_WITH_NO_STATE_TRANSITION) 
				logger.warn(evex);
			else
				logger.error(evex.fillInStackTrace());		
		} 
	}
}