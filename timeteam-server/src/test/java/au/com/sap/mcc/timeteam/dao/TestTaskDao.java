package au.com.sap.mcc.timeteam.dao;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.junit.Assert.assertNotNull;

import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

import au.com.sap.mcc.timeteam.model.Activity;
import au.com.sap.mcc.timeteam.model.Task;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/datasource-context.xml" })
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class, TransactionalTestExecutionListener.class, DbUnitTestExecutionListener.class })
@DatabaseSetup(value={"classpath:/sample-data.xml"}, type = DatabaseOperation.CLEAN_INSERT)
@Profile("test")
public class TestTaskDao {

	@Autowired
	TaskDao taskDao;
	
	@Autowired
	ActivityDao activityDao;
	
	@Test
	public void testAddActivityToTask() throws Exception{
		Task task1 = taskDao.fetchById("-1");
		
		assertNotNull(task1);
		assertThat(task1.getId(), equalTo("-1"));
		
		Activity activity = new Activity();
		activity.setDuration(30);
		activity.setCapturedate(new Date(System.currentTimeMillis()));
		activity.setTask(task1);
		
		Activity persistentActivity = activityDao.save(activity);
		assertNotNull(persistentActivity);
		
		//Now fetch Task again, and check that the new Activity is present.
		Task task2 = taskDao.fetchById("-1");
		
		assertNotNull(task2);
		assertThat(task2.getId(), equalTo("-1"));
		assertThat(task2.getActivities(), hasItem(persistentActivity));
	}
}
