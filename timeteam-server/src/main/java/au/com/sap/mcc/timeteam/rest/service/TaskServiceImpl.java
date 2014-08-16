package au.com.sap.mcc.timeteam.rest.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import au.com.sap.mcc.timeteam.dao.ProjectDao;
import au.com.sap.mcc.timeteam.dao.TaskDao;
import au.com.sap.mcc.timeteam.model.Project;
import au.com.sap.mcc.timeteam.rest.model.Task;
import au.com.sap.mcc.timeteam.rest.model.TaskRequest;
import au.com.sap.mcc.timeteam.rest.model.TaskResponse;

public class TaskServiceImpl implements TaskService {
	
	private Logger log = LoggerFactory.getLogger(TaskServiceImpl.class);
	
	@Autowired
	protected TaskDao taskDao;
	
	@Autowired
	protected ProjectDao projectDao;
	
	public TaskResponse getTask(String id) {
		TaskResponse response = new TaskResponse();
		try {
			au.com.sap.mcc.timeteam.model.Task jpaTask = taskDao.fetchById(id);
			response.setTasks(Arrays.asList(Task.convert(jpaTask)));
		} catch(Exception e) {
			response.setSuccess(false);
			response.setErrorMessage(e.getClass() + ":" + e.getMessage());
		}
		return response;
	}

	public TaskResponse listAll() {
		TaskResponse response = new TaskResponse();
		try {
			List<Task> tasks = new ArrayList<Task>();
			List<au.com.sap.mcc.timeteam.model.Task> jpaTasks = taskDao.findAll();
			
			for(au.com.sap.mcc.timeteam.model.Task jpaTask : jpaTasks) {
				tasks.add(Task.convert(jpaTask));
			}
			response.setTasks(tasks);
		} catch(Exception e) {
			response.setSuccess(false);
			response.setErrorMessage(e.getClass() + ":" + e.getMessage());
		}
		return response;
	}

	public TaskResponse createTask(TaskRequest request) {
		TaskResponse response = new TaskResponse();
		try {
			Project jpaProject = projectDao.fetchById(request.getTask().getProjectId());
			au.com.sap.mcc.timeteam.model.Task jpaTask = new au.com.sap.mcc.timeteam.model.Task();
			
			jpaTask.setProject(jpaProject);
			jpaTask.setName(request.getTask().getName());
			
			jpaTask = taskDao.save(jpaTask);
			jpaProject.getTasks().add(jpaTask);
			
			Task task = Task.convert(jpaTask);
			response.setTasks(Arrays.asList(task));
		} catch(Exception e) {
			response.setSuccess(false);
			response.setErrorMessage(e.getClass() + ":" + e.getMessage());
		}
		return response;
	}
	
	@Override
	public TaskResponse updateTask(TaskRequest request) {
		TaskResponse response = new TaskResponse();
		try {
			au.com.sap.mcc.timeteam.model.Task jpaTask = taskDao.fetchById(request.getTask().getId());			
			jpaTask.setName(request.getTask().getName());
			Task task = Task.convert(taskDao.save(jpaTask));
			response.setTasks(Arrays.asList(task));
		} catch(Exception e) {
			response.setSuccess(false);
			response.setErrorMessage(e.getClass() + ":" + e.getMessage());
		}
		return response;
	}

	public TaskResponse deleteTask(TaskRequest request) {
		TaskResponse response = new TaskResponse();
		try {
			au.com.sap.mcc.timeteam.model.Task jpaTask = taskDao.fetchById(request.getTask().getId());
			taskDao.delete(jpaTask);
		} catch(Exception e) {
			response.setSuccess(false);
			response.setErrorMessage(e.getClass() + ":" + e.getMessage());
		}
		return response;
	}

}
