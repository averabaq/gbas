/* 
 * $Id: EventStoreTest.java,v 1.0 2013-01-27 23:29:05 averab Exp $
 *
 * @copyright Universidad Carlos III de Madrid. proprietary/confidential. Use is subject to license terms.
 */
package es.uc3m.softlab.cbi4api.gbas.event.store.test;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import es.uc3m.softlab.cbi4api.gbas.event.store.domain.ActivityInstance;
import es.uc3m.softlab.cbi4api.gbas.event.store.domain.Model;
import es.uc3m.softlab.cbi4api.gbas.event.store.domain.Event;
import es.uc3m.softlab.cbi4api.gbas.event.store.domain.EventCorrelation;
import es.uc3m.softlab.cbi4api.gbas.event.store.domain.EventData;
import es.uc3m.softlab.cbi4api.gbas.event.store.domain.EventPayload;
import es.uc3m.softlab.cbi4api.gbas.event.store.domain.ModelType;
import es.uc3m.softlab.cbi4api.gbas.event.store.domain.ProcessInstance;
import es.uc3m.softlab.cbi4api.gbas.event.store.domain.Source;
import es.uc3m.softlab.cbi4api.gbas.event.store.domain.State;
import es.uc3m.softlab.cbi4api.gbas.event.store.facade.ActivityInstanceException;
import es.uc3m.softlab.cbi4api.gbas.event.store.facade.ActivityInstanceFacade;
import es.uc3m.softlab.cbi4api.gbas.event.store.facade.EventException;
import es.uc3m.softlab.cbi4api.gbas.event.store.facade.EventFacade;
import es.uc3m.softlab.cbi4api.gbas.event.store.facade.ModelException;
import es.uc3m.softlab.cbi4api.gbas.event.store.facade.ModelFacade;
import es.uc3m.softlab.cbi4api.gbas.event.store.facade.ProcessInstanceException;
import es.uc3m.softlab.cbi4api.gbas.event.store.facade.ProcessInstanceFacade;
import es.uc3m.softlab.cbi4api.gbas.event.store.facade.SourceException;
import es.uc3m.softlab.cbi4api.gbas.event.store.facade.SourceFacade;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

@RunWith(SpringJUnit4ClassRunner.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@ContextConfiguration(locations = {"classpath:applicationContext-test.xml"})
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = false)
@Transactional(propagation=Propagation.REQUIRES_NEW)
public class EventStoreTest extends AbstractShowcaseTest {
	/** Number of event records to store in this test */
	private static final int TEST_CASE_EVENT_NUM = 10;
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
	@Test
	@Rollback(false)
	public void aTestSaveEvents() throws SourceException, ModelException, ProcessInstanceException, EventException {
		/*
		Source source = new Source();
		source.setId("ApacheODE(1)-192.168.1.10");
		source.setName("ApacheODE-10");
		source.setDescription("BPEL engine");
		source.setInetAddress("192.168.1.10");
		source.setPort(8080);
		sourceFacade.saveSource(source);
		
		source = new Source();
		source.setId("ApacheODE(2)-192.168.1.20");
		source.setName("ApacheODE-20");
		source.setDescription("BPEL engine");
		source.setInetAddress("192.168.1.20");
		source.setPort(8080);
		sourceFacade.saveSource(source);
		
		source = new Source();
		source.setId("ApacheODE(3)-192.168.1.30");
		source.setName("ApacheODE-30");
		source.setDescription("BPEL engine");
		source.setInetAddress("192.168.1.30");
		source.setPort(8080);
		sourceFacade.saveSource(source);
		
		Model processModel = new Model();	
		processModel.setId((long)(Math.random() * 10000000));
		processModel.setName("Model1");
		processModel.setModelSrcId("src-Model1");
		processModel.setType(ModelType.PROCESS);
		processModel.setSource(source);
		modelFacade.saveModel(processModel);
		
		Model activityModel = new Model();
		activityModel.setId((long)(Math.random() * 10000000));
		activityModel.setName("Model1");
		activityModel.setModelSrcId("src-Model1");
		activityModel.setType(ModelType.ACTIVITY);
		activityModel.setSource(source);
		modelFacade.saveModel(activityModel);
						
		for (int i = 0; i < TEST_CASE_EVENT_NUM; i++) {			
			Event event = new Event();	
			event.setEventID((long)(Math.random() * 10000000));
			event.setProcessInstance(new ProcessInstance());
			event.getProcessInstance().setId(3L);
			event.getProcessInstance().setInstanceSrcId(String.valueOf(Math.random() * 100000));
			event.getProcessInstance().setModel(processModel);
			event.getProcessInstance().setCorrelatorId((long)(Math.random() * 10000000));
			event.getProcessInstance().setName(UUID.randomUUID().toString());
			
			event.setActivityInstance(new ActivityInstance());
			event.getActivityInstance().setId((long)(Math.random() * 10000000));
			event.getActivityInstance().setInstanceSrcId(String.valueOf(Math.random() * 100000));
			event.getActivityInstance().setModel(activityModel);
			event.getActivityInstance().setName(UUID.randomUUID().toString());
			
			event.setTimestamp(new Date());	
			
			State currentState = State.values()[(int)(Math.random() * State.values().length)];
			State previousState = State.values()[(int)(Math.random() * State.values().length)];
			event.getEventDetails().setCurrentState(currentState);
			event.getEventDetails().setPreviousState(previousState); 

			for (int j = 0; j < ((int)(Math.random() * 10)); j++) {				
				EventPayload payload = new EventPayload();
				payload.setKey(UUID.randomUUID().toString());
				payload.setValue(UUID.randomUUID().toString());
				payload.setEvent(event);
				event.getPayload().add(payload);
			}
			
			for (int j = 0; j < ((int)(Math.random() * 10)); j++) {				
				EventCorrelation correlation = new EventCorrelation();
				correlation.setKey(UUID.randomUUID().toString());
				correlation.setValue(UUID.randomUUID().toString());
				correlation.setEvent(event);
				event.getCorrelations().add(correlation);
			}
			
			for (int j = 0; j < ((int)(Math.random() * 10)); j++) {				
				EventData element = new EventData();
				element.setKey(UUID.randomUUID().toString());
				element.setValue(UUID.randomUUID().toString());
				element.setEvent(event);
				event.getDataElement().add(element);
			}
			eventFacade.storeEvent(event);
		}
		List<Event> events = eventFacade.getAll();
		Assert.notEmpty(events);
		*/
	}
	/**
	 * Test events retrieval methods.
	 * @throws EventException if any event access error occurred.
	 */
	@Test
	public void bTestFindEvents() throws EventException {	
		/*
		List<Event> events = eventFacade.getAll();
		Assert.notEmpty(events);
		List<Source> sources = sourceFacade.getAll();
		Assert.notEmpty(sources);
		List<ProcessInstance> processInstances = processInstanceFacade.getAll();
		Assert.notEmpty(processInstances);
		List<ActivityInstance> activityInstances = activityInstanceFacade.getAll();
		Assert.notEmpty(activityInstances);
		List<Model> models = modelFacade.getAll();
		Assert.notEmpty(models);
		*/
	}
	/**
	 * Test deletion of events.
	 * 
	 * @throws EventException if any event error occurred.
	 * @throws ModelException if any model error occurred.
	 * @throws ProcessInstanceException if any process instance error occurred. 
	 * @throws ActivityInstanceException if any activity instance error occurred. 
	 * @throws SourceException if any source error occurred. 
	 *
	@Test
	@Rollback(false)
	public void cTestDeleteEvents() throws EventException, ModelException, ProcessInstanceException, ActivityInstanceException, SourceException {
		List<Event> events = eventFacade.getAll();
		for (Event event : events) {
			eventFacade.deleteEvent(event);
		}
		events = eventFacade.getAll();
		Assert.state(events.isEmpty());
		
		List<ProcessInstance> processInstances = processInstanceFacade.getAll();
		for (ProcessInstance instance : processInstances) {
			processInstanceFacade.deleteProcessInstance(instance);
		}		
		processInstances = processInstanceFacade.getAll();
		Assert.state(processInstances.isEmpty());
		
		List<ActivityInstance> activityInstances = activityInstanceFacade.getAll();
		for (ActivityInstance instance : activityInstances) {
			activityInstanceFacade.deleteActivityInstance(instance);
		}		
		activityInstances = activityInstanceFacade.getAll();
		Assert.state(activityInstances.isEmpty());
		
		List<Model> models = modelFacade.getAll();
		for (Model model : models) {
			modelFacade.deleteModel(model);
		}		
		models = modelFacade.getAll();
		Assert.state(models.isEmpty());
		
		List<Source> sources = sourceFacade.getAll();
		for (Source source : sources) {
			sourceFacade.deleteSource(source);
		}		
		sources = sourceFacade.getAll();
		Assert.state(sources.isEmpty());
	}*/
}
