package au.com.sap.mcc.timeteam.rest.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import au.com.sap.mcc.timeteam.model.Task;

@Service
public class TaskManagerJPADao implements TaskManagerDao{
	
	@Autowired
	TaskRepository taskRepository = null;

	@Override
	public Task fetchTaskById(Integer id) {
		Task task = taskRepository.findOne(id.toString());
		
		if(task == null) {
			throw new RuntimeException("Task Not Found: " + id);
		}
		
		return task;
	}

	@Override
	public List<Task> fetchAllTasks() {
		return (List<Task>) taskRepository.findAll();
	}

	@Override
	public Task saveTask(Task task) {
		return taskRepository.save(task);
	}


	@Override
	public void deleteTask(Task task) {
		taskRepository.delete(task.getId());		
	}
}
