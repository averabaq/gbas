/* 
 * $Id: EventStoreTest.java,v 1.0 2013-01-27 23:29:05 averab Exp $
 *
 * @copyright Universidad Carlos III de Madrid. proprietary/confidential. Use is subject to license terms.
 */
package es.uc3m.softlab.cbi4api.gbas.event.store.test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import es.uc3m.softlab.cbi4api.gbas.event.store.domain.ActivityInstance;
import es.uc3m.softlab.cbi4api.gbas.event.store.domain.Event;
import es.uc3m.softlab.cbi4api.gbas.event.store.domain.EventCorrelation;
import es.uc3m.softlab.cbi4api.gbas.event.store.domain.Model;
import es.uc3m.softlab.cbi4api.gbas.event.store.domain.ModelType;
import es.uc3m.softlab.cbi4api.gbas.event.store.domain.ProcessInstance;
import es.uc3m.softlab.cbi4api.gbas.event.store.domain.Source;
import es.uc3m.softlab.cbi4api.gbas.event.store.facade.ActivityInstanceFacade;
import es.uc3m.softlab.cbi4api.gbas.event.store.facade.EventException;
import es.uc3m.softlab.cbi4api.gbas.event.store.facade.EventFacade;
import es.uc3m.softlab.cbi4api.gbas.event.store.facade.ModelException;
import es.uc3m.softlab.cbi4api.gbas.event.store.facade.ModelFacade;
import es.uc3m.softlab.cbi4api.gbas.event.store.facade.ProcessInstanceException;
import es.uc3m.softlab.cbi4api.gbas.event.store.facade.ProcessInstanceFacade;
import es.uc3m.softlab.cbi4api.gbas.event.store.facade.SourceException;
import es.uc3m.softlab.cbi4api.gbas.event.store.facade.SourceFacade;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext-test.xml"})
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = false)
@Transactional(propagation=Propagation.REQUIRES_NEW)
public class EventStorePerformanceTest extends AbstractShowcaseTest {
    /** concurrency degree */
	private static final int CONCURRENCY_DEGREE = 4;
    /** subprocess span level */
	private static final int SUBPROCESS_SPAN_LEVEL = 7;	
	/** number of events per instance */
	private static final int EVENTS_PER_INSTANCE = 10;
	/** Number of event records to store in this test */
	private static final int TEST_CASE_EVENT_NUM = 100000;
	/** Event session facade */
	@Autowired private EventFacade eventFacade;
	/** Source session facade */
	@Autowired private SourceFacade sourceFacade;
	/** Model session facade */
	@Autowired private ModelFacade modelFacade;
	/** Process instance session facade */
	@Autowired private ProcessInstanceFacade processInstanceFacade;
	/** Activity instance session facade */
	@Autowired private ActivityInstanceFacade activityInstanceFacade;
	
	/**
	 * Tests the creation and storing of events.
	 * @throws SourceException if any source access error occurred.
	 * @throws ModelException if any model access error occurred.
	 * @throws ProcessInstanceException if any process instance access error occurred.
	 * @throws EventException if any event access error occurred.
	 */
	//@Test
	@Rollback(false)
	public void testPerformance() throws SourceException, ModelException, ProcessInstanceException, EventException {
		ArrayList<Source> sources = new ArrayList<Source>();
		for (int i = 0; i < CONCURRENCY_DEGREE; i++) {
			Source source = new Source();
			source.setId("ApacheODE(" + i + ")-192.168.1.1" + i);
			source.setName("ApacheODE[" + i + "]");
			source.setDescription("BPEL engine");
			source.setInetAddress("192.168.1.1" + i);
			source.setPort(8080);
			sources.add(source);
			sourceFacade.saveSource(source);			
		}
		long modelId = (long)(Math.random() * 1000);
		
		// Simulation of one global process per source 
		ArrayList<Model> processModels = new ArrayList<Model>();
		for (int i = 0; i < CONCURRENCY_DEGREE; i++) {
			Model processModel = new Model();	
			processModel.setModelSrcId(String.valueOf(++modelId));
			processModel.setName("Process model [" + String.format("%03d", Long.valueOf(processModel.getModelSrcId())) + "]");		
			processModel.setType(ModelType.PROCESS);
			processModel.setSource(sources.get(i));
			processModels.add(processModel);
			//modelFacade.saveModel(processModel);
		}
		// Simulation of one global process per source 
		ArrayList<Model> activityModels = new ArrayList<Model>();
		for (int i = 0; i < CONCURRENCY_DEGREE; i++) {
			for (int j = 0; j < SUBPROCESS_SPAN_LEVEL; j++) {
				Model activityModel = new Model();
				activityModel.setModelSrcId(String.valueOf(++modelId));
				activityModel.setName("Activity model [" + String.format("%03d", Long.valueOf(activityModel.getModelSrcId())) + "]");				
				activityModel.setType(ModelType.ACTIVITY);
				activityModel.setSource(sources.get(i));
				activityModels.add(activityModel);
				//modelFacade.saveModel(activityModel);
			}
		}

		/* Used for testing map-reduce jobs */
		HashSet<EventCorrelation> dummyList = new HashSet<EventCorrelation>(); 
		EventCorrelation cor = new EventCorrelation();
		cor.setEvent(null);
		cor.setKey("orderNo");
		cor.setValue("0201");	
		dummyList.add(cor);		
		cor = new EventCorrelation();
		cor.setEvent(null);
		cor.setKey("CustomerNo");
		cor.setValue("0394");				
		dummyList.add(cor);	
		cor = new EventCorrelation();
		cor.setEvent(null);
		cor.setKey("ItemNo");
		cor.setValue("0195");				
		dummyList.add(cor);	
		
		processModels.get(0).setId(5L);
		processInstanceFacade.getProcessInstance(processModels.get(0), dummyList);

		System.exit(0);
		
		long pInstanceId = (long)(Math.random() * 10000);
		long aInstanceId = (long)(Math.random() * 10000);
		for (int i = 0; i < TEST_CASE_EVENT_NUM; i++) {				
			for (int j = 0; j < CONCURRENCY_DEGREE; j++) {
				ProcessInstance processInstance = new ProcessInstance();
				processInstance.setInstanceSrcId(String.valueOf(pInstanceId++));
				processInstance.setModel(processModels.get(j));
				processInstance.setName("Process instance [" + String.format("%05d", Long.valueOf(processInstance.getInstanceSrcId())) + "]");
				for (int k = 0; k < SUBPROCESS_SPAN_LEVEL; k++) {
					ActivityInstance activityInstance = new ActivityInstance();
					activityInstance.setInstanceSrcId(String.valueOf(aInstanceId++));
					activityInstance.setModel(activityModels.get((j * SUBPROCESS_SPAN_LEVEL) + k));	
					activityInstance.setName("Activity instance [" + String.format("%05d", Long.valueOf(activityInstance.getInstanceSrcId())) + "]");
					//(new EventGenerator(eventFacade, processInstance, null)).run();
					
					for (int l = 0; l < EVENTS_PER_INSTANCE; l++) {		
						if (((long)(Math.random() * 10)) == 1) {
							(new EventGenerator(eventFacade, processInstance, activityInstance)).run();
						} else {
							(new EventGenerator(eventFacade, processInstance, null)).run();
						}
					}
				}
			}
		}
	}
	/**
	 * Test events retrieval methods.
	 * @throws EventException if any event access error occurred.
	 */
	//@Test
	public void testFindEvents() throws EventException {	
		List<Source> sources = sourceFacade.getAll();
		Assert.notEmpty(sources);
		List<ProcessInstance> processInstances = processInstanceFacade.getAll();
		Assert.notEmpty(processInstances);
		List<ActivityInstance> activityInstances = activityInstanceFacade.getAll();
		Assert.notEmpty(activityInstances);
		List<Model> models = modelFacade.getAll();
		Assert.notEmpty(models);
		List<Event> events = eventFacade.getAllFromProcessInstId(processInstances.get(processInstances.size()-1).getId());
		Assert.notEmpty(events);
		for (Event event : events) {
			Assert.notEmpty(event.getCorrelations()); 
			Assert.notEmpty(event.getPayload()); 
			Assert.notEmpty(event.getDataElement()); 				
		}		
	}
}
