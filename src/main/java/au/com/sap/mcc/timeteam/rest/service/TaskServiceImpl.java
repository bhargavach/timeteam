package au.com.sap.mcc.timeteam.rest.service;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;

import au.com.sap.mcc.timeteam.dao.TaskDao;
import au.com.sap.mcc.timeteam.rest.model.TaskRequest;
import au.com.sap.mcc.timeteam.rest.model.TaskResponse;

public class TaskServiceImpl implements TaskService {
	
	@Autowired
	protected TaskDao taskDao;

	public TaskResponse fetchTaskById(TaskRequest request) {
		TaskResponse response = new TaskResponse();
		try {
			response.setTasks(Arrays.asList(taskDao.fetchTaskById(request.getTask().getId())));
		} catch(Exception e) {
			response.setSuccess(false);
			response.setErrorMessage(e.getClass() + ":" + e.getMessage());
		}
		return response;
	}

	public TaskResponse fetchAllTasks(TaskRequest request) {
		TaskResponse response = new TaskResponse();
		try {
			response.setTasks(taskDao.fetchAllTasks());
		} catch(Exception e) {
			response.setSuccess(false);
			response.setErrorMessage(e.getClass() + ":" + e.getMessage());
		}
		return response;
	}

	public TaskResponse saveTask(TaskRequest request) {
		TaskResponse response = new TaskResponse();
		try {
			response.setTasks(Arrays.asList(taskDao.saveTask(request.getTask())));
		} catch(Exception e) {
			response.setSuccess(false);
			response.setErrorMessage(e.getClass() + ":" + e.getMessage());
		}
		return response;
	}

	public TaskResponse deleteTask(TaskRequest request) {
		TaskResponse response = new TaskResponse();
		try {
			taskDao.deleteTask(request.getTask());
		} catch(Exception e) {
			response.setSuccess(false);
			response.setErrorMessage(e.getClass() + ":" + e.getMessage());
		}
		return response;
	}

}
