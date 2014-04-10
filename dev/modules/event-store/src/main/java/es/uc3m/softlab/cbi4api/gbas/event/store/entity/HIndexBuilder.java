/* 
 * $Id: HCorrelationIndexBuilder.java,v 1.0 2013-12-16 16:29:05 averab Exp $
 *
 * @copyright Universidad Carlos III de Madrid. proprietary/confidential. Use is subject to license terms.
 */
package es.uc3m.softlab.cbi4api.gbas.event.store.entity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import es.uc3m.softlab.cbi4api.gbas.event.store.domain.Model;
import org.apache.log4j.Logger;

import es.uc3m.softlab.cbi4api.gbas.event.store.domain.Event;
import es.uc3m.softlab.cbi4api.gbas.event.store.domain.EventCorrelation;

/**
 * Index entity object builder for using as secondary indexes in HBase.
 * 
 * @author averab
 * @version 1.0.0  
 */
public class HIndexBuilder {
    /** Logger for tracing */
    private transient Logger logger = Logger.getLogger(getClass());
    /** Instance of this singleton class */
    private static HIndexBuilder instance = null;
    
    /**
     * Constructor of this singleton class.     
     */
    private HIndexBuilder() {    
    }
    /**
     * Gets the instance of this singleton class.
     * 
     * @return instance of this singleton class.
     */
    public static HIndexBuilder getInstance() {
       	if (instance == null)
    		instance = new HIndexBuilder();
    	return instance;
    }
    /**
     * 
     * @param eventCorrelations
     * @return
     */
    public byte[] getIndexKey(Set<EventCorrelation> eventCorrelations, Model model) {
    	List<EventCorrelation> correlationSet = new LinkedList<EventCorrelation>(eventCorrelations);
    	Collections.sort(correlationSet);
    	ByteArrayOutputStream correlationIds = new ByteArrayOutputStream();
    	try {
    		for (EventCorrelation correlation : correlationSet) {
    			correlationIds.write(correlation.getKey().getBytes());
    			correlationIds.write(correlation.getValue().getBytes());
    		}
            // attach source byte stream
            correlationIds.write(model.getSource().getId().getBytes());
            // attach model byte stream
            correlationIds.write(model.getModelSrcId().getBytes());
    	} catch (IOException ioex) {
    		logger.error(String.format("Can't create event correlation key index"));
    	} finally {
    		try {
    			correlationIds.close();
    		} catch (IOException ioex) {
    			logger.warn(ioex.fillInStackTrace());
    		}
    	}
    	return correlationIds.toByteArray(); 
    }
    /**
     * Builds the correlation index entity object as secondary index 
     * @param event event object with the correlation set to index.
     * @return event correlation index object.
     */
    public HEventCorrelationIndex buildCorrelationIndex(Event event) {
    	HEventCorrelationIndex index = new HEventCorrelationIndex();
    	index.setEventID(event.getEventID());
        if (event.getActivityInstance() != null)
   		    index.setCorrelationSet(getIndexKey(event.getCorrelations(), event.getActivityInstance().getModel()));
        else
            index.setCorrelationSet(getIndexKey(event.getCorrelations(), event.getProcessInstance().getModel()));
		return index;
    }    
}
