package au.com.sap.mcc.timeteam.rest.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import au.com.sap.mcc.timeteam.dao.ActivityDao;
import au.com.sap.mcc.timeteam.dao.TaskDao;
import au.com.sap.mcc.timeteam.model.Task;
import au.com.sap.mcc.timeteam.rest.model.Activity;
import au.com.sap.mcc.timeteam.rest.model.ActivityRequest;
import au.com.sap.mcc.timeteam.rest.model.ActivityResponse;

public class ActivityServiceImpl implements ActivityService {
	
	private Logger log = LoggerFactory.getLogger(ActivityServiceImpl.class);
	
	@Autowired
	protected ActivityDao activityDao;
	
	@Autowired
	protected TaskDao taskDao;
	
	public ActivityResponse get(String id) {
		ActivityResponse response = new ActivityResponse();
		try {
			au.com.sap.mcc.timeteam.model.Activity jpaActivity = activityDao.fetchById(id);
			Activity activity = Activity.convert(jpaActivity);
			response.setActivities(Arrays.asList(activity));
		} catch(Exception e) {
			log.error("getActivity", e);
			response.setSuccess(false);
			response.setErrorMessage(e.getClass() + ":" + e.getMessage());
		}
		return response;
	}

	public ActivityResponse list() {
		ActivityResponse response = new ActivityResponse();
		try {
			List<au.com.sap.mcc.timeteam.model.Activity> jpaActivities =  activityDao.findAll();
			List<Activity> activityList = new ArrayList<Activity>();
			for(au.com.sap.mcc.timeteam.model.Activity jpaActivity : jpaActivities) {
				activityList.add(Activity.convert(jpaActivity));
			}
			response.setActivities(activityList);
		} catch(Exception e) {
			log.error("listAll", e);
			response.setSuccess(false);
			response.setErrorMessage(e.getClass() + ":" + e.getMessage());
		}
		return response;
	}

	public ActivityResponse create(ActivityRequest request) {
		ActivityResponse response = new ActivityResponse();
		try {
			au.com.sap.mcc.timeteam.model.Activity jpaActivity = new au.com.sap.mcc.timeteam.model.Activity();
			Task jpaTask = taskDao.fetchById(request.getActivity().getTaskId());
			
			jpaActivity.setCapturedate(request.getActivity().getCapturedate());
			jpaActivity.setDuration(request.getActivity().getDuration());
			jpaActivity.setTask(jpaTask);
			
			jpaActivity = activityDao.save(jpaActivity);
			jpaTask.getActivities().add(jpaActivity);
			
			Activity activity = Activity.convert(jpaActivity);
			
			response.setActivities(Arrays.asList(activity));
		} catch(Exception e) {
			log.error("saveActivity", e);
			response.setSuccess(false);
			response.setErrorMessage(e.getClass() + ":" + e.getMessage());
		}
		return response;
	}
	
	@Override
	public ActivityResponse update(ActivityRequest request) {
		ActivityResponse response = new ActivityResponse();
		try {
			au.com.sap.mcc.timeteam.model.Activity jpaActivity = activityDao.fetchById(request.getActivity().getId());
			
			jpaActivity.setCapturedate(request.getActivity().getCapturedate());
			jpaActivity.setDuration(request.getActivity().getDuration());
			jpaActivity.setTask(taskDao.fetchById(request.getActivity().getTaskId()));
			
			Activity activity = Activity.convert(activityDao.save(jpaActivity));
			
			response.setActivities(Arrays.asList(activity));
		} catch(Exception e) {
			log.error("updateActivity", e);
			response.setSuccess(false);
			response.setErrorMessage(e.getClass() + ":" + e.getMessage());
		}
		return response;
	}

	public ActivityResponse delete(ActivityRequest request) {
		ActivityResponse response = new ActivityResponse();
		try {
			au.com.sap.mcc.timeteam.model.Activity jpaActivity = activityDao.fetchById(request.getActivity().getId());
			activityDao.delete(jpaActivity);
		} catch(Exception e) {
			log.error("deleteActivity", e);
			response.setSuccess(false);
			response.setErrorMessage(e.getClass() + ":" + e.getMessage());
		}
		return response;
	}
}
