package au.com.sap.mcc.timeteam.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import au.com.sap.mcc.timeteam.model.Task;

@Service
public class TaskDaoImpl implements TaskDao{
	
	@Autowired
	TaskRepository taskRepository = null;

	@Override
	public Task fetchTaskById(String id) {
		Task task = taskRepository.findOne(id);
		
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
