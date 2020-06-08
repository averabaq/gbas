/* 
 * $Id: MapReduceCorrelation.java,v 1.0 2013-01-27 23:29:05 averab Exp $
 *
 * @copyright Universidad Carlos III de Madrid. proprietary/confidential. Use is subject to license terms.
 */
package es.uc3m.softlab.cbi4api.gbas.event.store.mapred;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.UUID;

import javax.persistence.Table;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.PathFilter;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.filter.CompareFilter.CompareOp;
import org.apache.hadoop.hbase.filter.FilterList;
import org.apache.hadoop.hbase.filter.SingleColumnValueFilter;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableMapReduceUtil;
import org.apache.hadoop.hbase.mapreduce.TableMapper;
import org.apache.hadoop.hbase.mapreduce.HFileOutputFormat;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.LongWritable ;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.KeyValueTextInputFormat;
import org.apache.hadoop.mapreduce.lib.jobcontrol.ControlledJob;
import org.apache.hadoop.mapreduce.lib.jobcontrol.JobControl;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.log4j.Logger;

import es.uc3m.softlab.cbi4api.gbas.event.store.domain.EventCorrelation;
import es.uc3m.softlab.cbi4api.gbas.event.store.entity.HEventCorrelation;

/**
 * Map-Reduce class that handles the logic for performing the inner-related process correlation.
 * 
 * @author averab
 * @version 1.0.0
 */
public class MapReduceInnerCorrelation {
    /** Logger for tracing */
    private static Logger logger = Logger.getLogger(MapReduceInnerCorrelation.class);
	/** Map reduce cache size */
	private static final int CACHING = 500;
	/** Path for this map reduce correlation task */
	private static final String MAP_REDUCE_CORRELATOR_PATH = "/opt/cbi4api/gbas/tmp/mapred/correlator";
	/** Path for this map reduce correlation task */
	private static final String MAP_REDUCE_COR_EVENT_CORRELATION_PATH = MAP_REDUCE_CORRELATOR_PATH + "/event-correlation";
	/** Map reduce task timeout */
	private static final int MAPRED_TIMEOUT = 1000;
	/** Number of reducer tasks */
	private static final int NUM_RED_TASKS = 10;
	/** Column family for event correlation  */
	private static final byte[] CF_EVENT_CORRELATION = "event_correlation".getBytes();	
	/** Column name attribute for the event identifier */
	private static final byte[] ATTR_EVENT_ID = "event_id".getBytes();
	/** Column name attribute for the activity model related to the event */
	private static final byte[] ATTR_PROCESS_MODEL = "process_model".getBytes();
	/** Column name attribute for the activity instance related to the event */
	private static final byte[] ATTR_ACTIVITY_INST = "activity_instance".getBytes();
	/** Column name attribute for the event correlation key */
	private static final byte[] ATTR_KEY = "key".getBytes();	
	/** Column name attribute for the event correlation value */
	private static final byte[] ATTR_VALUE = "value".getBytes();
			
	/**
	 * Maps input key/value pairs to a set of intermediate key/value pairs.
	 * @author averab
	 *
	 */
	public static class EventCorrelatorFilterMapper extends TableMapper<LongWritable , Text> {	
		/**
		 * 
		 */
		@Override
		public void map(ImmutableBytesWritable row, Result value, Context context) throws IOException, InterruptedException {
			BigInteger bevent = new BigInteger(value.getValue(CF_EVENT_CORRELATION, ATTR_EVENT_ID));
			String bkey = new String(value.getValue(CF_EVENT_CORRELATION, ATTR_KEY));
			LongWritable  _event = new LongWritable (bevent.longValue());
			Text _key = new Text(bkey);
			context.write(_event, _key);
		}
	}			
	/**
	 * 
	 * @author averab
	 *
	 */
	public static class EventCorrelatorMapper extends Mapper<Text, Text, LongWritable, LongWritable> {	
		private final static LongWritable one = new LongWritable (1L); 
		/**
		 * 
		 */
		@Override
		public void map(Text key, Text value, Context context) throws IOException, InterruptedException {			
			LongWritable _key = new LongWritable(Long.valueOf(key.toString()));
			context.write(_key, one);
			
		}
	}	
	/**
	 * 
	 * @author averab
	 *
	 */
	public static class EventCorrelatorCombiner extends Reducer<LongWritable, LongWritable, LongWritable, LongWritable> {
		/**
		 * 
		 */
		@Override
		public void reduce(LongWritable key, Iterable<LongWritable> values, Context context) throws IOException, InterruptedException { 
			long sum = 0;			
			for (LongWritable value : values) {
				sum += value.get();
			}
			context.write(key, new LongWritable(sum));
		}
	}		
	/**
	 * 
	 * @author averab
	 *
	 */
	public static class EventCorrelatorReducer extends Reducer<LongWritable, LongWritable, LongWritable, LongWritable> {
		/**
		 * 
		 */
		@Override
		public void reduce(LongWritable key, Iterable<LongWritable> values, Context context) throws IOException, InterruptedException { 
			Configuration conf = context.getConfiguration();
			int size = conf.getInt("correlationKeysSize", -1);
			
			long sum = 0;			
			for (LongWritable value : values) {
				sum += value.get();
			}
			// emits if and only if the eventId satisfies all correlation values
			if (sum == size)
				context.write(key, new LongWritable(sum));
		}
	}	
	/**
	 * Event combiner class that handles the process for mapping different events that satisfies
	 * the correlation criteria.
	 * @author averab
	 */
	public static class EventFinalMapper extends Mapper<Text, Text, LongWritable, Text> {	
		/**
		 * This method is called once for each key/value pair in the input split. It converts the event id read 
		 * from text input file into a long value.
		 * @param key event identifier.
		 * @param value value for the given key.
		 * @param context mapper context.
		 * @throws IOException if any input/output error occurred.
		 * @throws InterruptedException if any interruption error occurred.
		 */
		@Override
		public void map(Text key, Text value, Context context) throws IOException, InterruptedException {			
			LongWritable _key = new LongWritable(Long.valueOf(key.toString()));
			context.write(_key, value);			
		}
	}	
	/**
	 * Event combiner class that handles the process for combining the events that satisfies
	 * the correlation criteria.
	 * @author averab
	 */
	public static class EventFinalCombiner extends Reducer<LongWritable, Text, LongWritable, Text> {
		/**
		 * This method is called once for each key for combining the events per different key definition
		 * @param key event identifier.
		 * @param values list of values for the given key.
		 * @param context reducer context.
		 * @throws IOException if any input/output error occurred.
		 * @throws InterruptedException if any interruption error occurred.
		 */
		@Override
		public void reduce(LongWritable key, Iterable<Text> values, Context context) throws IOException, InterruptedException { 
			StringBuffer _buf = new StringBuffer();
			for (Text text : values) {
				_buf.append(text);
			}
			Text _value = new Text(_buf.toString());
			context.write(key, _value);
		}
	}	
	/**
	 * Event reducer class that handles the process for getting the events that satisfies
	 * the correlation criteria.
	 * @author averab
	 */
	public static class EventFinalReducer extends Reducer<LongWritable, Text, LongWritable, Text> {
		/**
		 * This method is called once for each key for getting number of matches between events
		 * for different key definitions. 
		 * @param key event identifier.
		 * @param values list of values for the given key.
		 * @param context reducer context.
		 * @throws IOException if any input/output error occurred.
		 * @throws InterruptedException if any interruption error occurred.
		 */
		@Override
		public void reduce(LongWritable key, Iterable<Text> values, Context context) throws IOException, InterruptedException { 
			StringBuffer _buf = new StringBuffer();
			Iterator<Text> iterator = values.iterator();
			while (iterator.hasNext()) {
				_buf.append(iterator.next());
				if (iterator.hasNext())
					_buf.append(":");			
			}	
			String[] tokens = _buf.toString().split(":");
			if (_buf.toString().contains(":")) {
				LongWritable _key = new LongWritable(Long.valueOf(tokens[0]));
				context.write(_key, new Text(String.valueOf(key.get())));
			}
		}
	}	
	/**
	 * Performs the map-reduce job for getting the specific event to correlate instances.
	 * @param modelId modelId model identifier to filter the events.
	 * @param correlations correlations correlation data.
	 * @throws IOException if any input/output error occurred.
	 * @throws ClassNotFoundException if the any class has been no found in the classpath at runtime.
	 * @throws InterruptedException if any interruption error occurred.
	 */
	public static Long perform(final long modelId, final Set<EventCorrelation> correlations) throws IOException, ClassNotFoundException, InterruptedException {
		// session identifier for the current map reduce process 
		String sessionId = UUID.randomUUID().toString();
		logger.debug("Session ID for map reduce job (correlation) [" + sessionId + "]");
		// synchronous job for filtering event_id-key
		List<ControlledJob> eventCorrelationFilterControlledJobList = new ArrayList<ControlledJob>();
		for (EventCorrelation correlation : correlations) {
			Job eventCorrelationFilterJob = eventCorrelatorFilterStage(modelId, correlation, sessionId);
			eventCorrelationFilterControlledJobList.add(new ControlledJob(eventCorrelationFilterJob, null));
			logger.debug("Map event correlation filter job (correlation) over the key " + correlation.getKey() + " and session [" + sessionId + "] completed.");
		}
		// synchronous job for reducing event_id that succeed the correlation set
		Job eventCorrelationReducerJob = eventCorrelationReducerStage(correlations, sessionId);		
		// controlled jobs definition
		ControlledJob eventCorrelationReducerControlledJob = new ControlledJob(eventCorrelationReducerJob, eventCorrelationFilterControlledJobList);
		List<ControlledJob> eventCorrelationControlledJobList = new ArrayList<ControlledJob>();
		eventCorrelationControlledJobList.add(eventCorrelationReducerControlledJob);
		
		// sets the job dependency control
		JobControl jobControl = new JobControl("corrleationJobGroup"); 		
		jobControl.addJobCollection(eventCorrelationFilterControlledJobList);
		jobControl.addJob(eventCorrelationReducerControlledJob);
		
		// starts map-reduce process
		Thread thread = new Thread(jobControl);
		thread.start();
		while (!jobControl.allFinished()); 
		logger.fatal("Map-Reduce job is finished.");
		// read the result back	
	    Long event = getResult(eventCorrelationReducerJob.getConfiguration(), new Path(MAP_REDUCE_COR_EVENT_CORRELATION_PATH + "/reduce/" + sessionId));
	    logger.debug("Map-Reduce correlation job of session [" + sessionId + "] found the event '" + event + "'.");
		logger.debug("Map-Reduce correlation job of session [" + sessionId + "] completed.");
		return event;
	}
	/**
	 * Gets the final reduce result from the HDFS file system
	 * @param configuration job configuration object
	 * @param path hdfs file path
	 * @return the reduced event with the highest identifier (the latest existent one)
	 * @throws IOException if any input/output error occurred.
	 */
	public static Long getResult(Configuration configuration, Path path) throws IOException {
		Long eventId = null;
		FileSystem fileSystem = null;
		BufferedReader bufferedReader = null;
		try {
			fileSystem = FileSystem.get(configuration);
			FileStatus[] fileStatus = fileSystem.listStatus(path, new PathFilter() {
				public boolean accept(Path _path) {
					return _path.toString().contains("part-");
				}
			});  		
			for (FileStatus status : fileStatus) {
				Path _path = status.getPath();
				bufferedReader = new BufferedReader(new InputStreamReader(fileSystem.open(_path)));		        
				String line = bufferedReader.readLine();
				while (line != null) {
					StringTokenizer tokenizer = new StringTokenizer(line);
					String key = tokenizer.nextToken();
					if (key != null) {
						eventId = Long.valueOf(key);
					}
					line = bufferedReader.readLine();					
				}
			}		    
		} finally {
			if (bufferedReader != null)
				bufferedReader.close();
			if (fileSystem != null)
				fileSystem.close();
		}
		return eventId;
	}
	/**
	 * Filter job for each event correlation data over a given process model.
	 * @param modelId model identifier to filter the events.
	 * @param correlation specific correlation pair (key,value) to filter.
	 * @param sessionId current session id.
	 * @return filter job for the given model and correlation data associated to the current session id.
	 * @throws IOException if any input/output error occurred.
	 * @throws ClassNotFoundException if the any class has been no found in the classpath at runtime.
	 * @throws InterruptedException if any interruption error occurred.
	 */
	private static Job eventCorrelatorFilterStage(final long modelId, final EventCorrelation correlation, final String sessionId) throws IOException, ClassNotFoundException, InterruptedException {
		Configuration config = HBaseConfiguration.create();
		
		Job job = new Job(config, "correlation-event-correlation-filter");
		// class that contains mapper and reducer
		job.setJarByClass(MapReduceInnerCorrelation.class);     

		Scan scan = new Scan();
		scan.setCaching(CACHING);      
		scan.setCacheBlocks(false);		
		
		// filters event-correlation on pairs (key, value)
		FilterList filterList = new FilterList();
		SingleColumnValueFilter keyFilter = new SingleColumnValueFilter(
			CF_EVENT_CORRELATION,
			ATTR_KEY,
			CompareOp.EQUAL,
			Bytes.toBytes(correlation.getKey()));
		SingleColumnValueFilter valueFilter = new SingleColumnValueFilter(
			CF_EVENT_CORRELATION,
			ATTR_VALUE,
			CompareOp.EQUAL,
			Bytes.toBytes(correlation.getValue()));
		// filters event-correlation by specific model only
		SingleColumnValueFilter modelFilter = new SingleColumnValueFilter(
			CF_EVENT_CORRELATION,
			ATTR_PROCESS_MODEL,
			CompareOp.EQUAL,
			Bytes.toBytes(modelId));
		// filters event-correlation by process instances only
		SingleColumnValueFilter processInstancefilter = new SingleColumnValueFilter(
			CF_EVENT_CORRELATION,
			ATTR_ACTIVITY_INST,
			CompareOp.EQUAL,
			Bytes.toBytes(Long.MIN_VALUE));
		processInstancefilter.setFilterIfMissing(false);		
		// filter by key pairs values	
		filterList.addFilter(keyFilter);
		filterList.addFilter(valueFilter);	
		// filter by secific process model 	
		filterList.addFilter(modelFilter);
		// filter by process instances only 	
		filterList.addFilter(processInstancefilter);
		scan.setFilter(filterList);		
        
		// gets the event table name
		if (!HEventCorrelation.class.isAnnotationPresent(Table.class))
			throw new RuntimeException("Cannot retrieved the process instance table name from annotated class. Table persistence annotation is not defined");
		Table table = HEventCorrelation.class.getAnnotation(Table.class);				
		
		TableMapReduceUtil.initTableMapperJob(
			table.name(),         
			scan,                 
			EventCorrelatorFilterMapper.class, 
			LongWritable.class,                
			Text.class,                         
			job);
		
		job.setNumReduceTasks(NUM_RED_TASKS);  
		HFileOutputFormat.setOutputPath(job, new Path(MAP_REDUCE_COR_EVENT_CORRELATION_PATH + "/" + sessionId + "/" + correlation.getKey()));  

		//job.submit();
		return job;
	}	
	/**
	 * Reducer job for the final event correlation stage.
	 * @param correlations correlation data.
	 * @param sessionId current session id.
	 * @return reducer job for the current session id.
	 * @throws IOException if any input/output error occurred.
	 * @throws ClassNotFoundException if the any class has been no found in the classpath at runtime.
	 * @throws InterruptedException if any interruption error occurred.
	 */
	private static Job eventCorrelationReducerStage(final Set<EventCorrelation> correlations, final String sessionId) throws IOException, ClassNotFoundException, InterruptedException {	
		Configuration conf = new Configuration();
		conf.setInt("mapreduce.task.timeout", MAPRED_TIMEOUT);
		conf.setInt("correlationKeysSize", correlations.size());

		Job job = new Job(conf, "correlation-event-correlation-reducer");
		job.setJarByClass(MapReduceInnerCorrelation.class); 
		job.setNumReduceTasks(NUM_RED_TASKS);
		
		job.setOutputKeyClass(LongWritable.class);
		job.setOutputValueClass(LongWritable.class);		
		
		job.setMapperClass(EventCorrelatorMapper.class);
		job.setCombinerClass(EventCorrelatorCombiner.class);
		job.setReducerClass(EventCorrelatorReducer.class);
		
		job.setInputFormatClass(KeyValueTextInputFormat.class);
		job.setOutputFormatClass(TextOutputFormat.class);

		for (EventCorrelation eventCorrelation : correlations) {
			FileInputFormat.addInputPath(job, new Path(MAP_REDUCE_COR_EVENT_CORRELATION_PATH + "/" + sessionId + "/" + eventCorrelation.getKey()));
		}
		FileOutputFormat.setOutputPath(job, new Path(MAP_REDUCE_COR_EVENT_CORRELATION_PATH + "/reduce/" + sessionId));

		//job.submit();
		return job;
	}
}