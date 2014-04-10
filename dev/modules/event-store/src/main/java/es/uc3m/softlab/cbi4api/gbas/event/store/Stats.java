/* 
 * $Id: Stats.java,v 1.0 2013-03-30 22:29:05 averab Exp $
 *
 * @copyright Universidad Carlos III de Madrid. proprietary/confidential. Use is subject to license terms.
 */
package es.uc3m.softlab.cbi4api.gbas.event.store;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import javax.annotation.PostConstruct;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import es.uc3m.softlab.cbi4api.gbas.event.store.StaticResources;

/**
 * Class that configures the global application. It takes
 * the configuration parameters from the pair (key, value) 
 * specified at the <i>event-store.properties</i> file. 
 *
 * @author averab
 */
@Scope(value=BeanDefinition.SCOPE_SINGLETON)
@Component(value="stats")
public class Stats {
    /** Log for tracing */
    private static final Logger logger = Logger.getLogger(Stats.class);  
    /** Operation stats */
    public enum Operation {
    	READ_MAX_ID,
    	READ_BY_ID,
    	READ_BY_SOURCE_DATA,
    	READ,
    	WRITE,
    	DELETE,
    	UPDATE,
    	LOAD,
    	MAP_REDUCE_INNER_PROCESS_CORRELATION,
    	MAP_REDUCE_INTER_PROCESS_CORRELATION
    }
    /** Statistic file */
    private PrintWriter writer;
    /** Configuration object */
    @Autowired private Config config;	

    /**
     * Post-constructor method to initialize the stats process.
     */
    @PostConstruct
    public void init() {
		try {
			if (config.isStatsActive()) {				
				String statsPropFile = config.getString(StaticResources.BUNDLE_CONFIG_STATS_FILE_KEY);
				String statsFile = FilenameUtils.normalize(statsPropFile);
				File file = new File(statsFile);
				if (!file.exists()) {
					FileUtils.forceMkdir(file.getParentFile());
				}
				this.writer = new PrintWriter(new BufferedWriter(new FileWriter(statsFile, true)));
			} 
		} catch (IOException ex) {			
			logger.fatal(ex.fillInStackTrace());
		}
    }
    /**
     * Write statistical performance entry in the stats result file.
     * @param op performance statistical operation.
     * @param clazz performance class that the operation has been performed on.
     * @param instance instance operation.
     * @param start start time.
     * @param end end time.
     */
    synchronized public void writeStat(Operation op, Class<?> clazz, Object instance, long start, long end) {
    	if (config.isStatsActive()) {
    		writer.printf("%s,%s,%s,%s,%s,%s\n", op, clazz.getName(), String.valueOf(instance), start, end, (end - start));
    		writer.flush();
    	}
    }    
}