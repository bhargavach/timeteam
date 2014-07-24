package au.com.sap.mcc.timeteam.dao;

import java.util.List;

import au.com.sap.mcc.timeteam.model.Task;

public interface TaskDao {

	public Task fetchTaskById(Integer id);
	public List<Task> fetchAllTasks();
	public Task saveTask(Task task);
	public void deleteTask(Task task);
	
}
