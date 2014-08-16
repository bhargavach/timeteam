package au.com.sap.mcc.timeteam.rest.model;

import java.util.ArrayList;
import java.util.List;

public class TaskResponse extends RestResponse{
	protected List<Task> tasks = new ArrayList<Task>();

	public List<Task> getTasks() {
		return tasks;
	}

	public void setTasks(List<Task> tasks) {
		this.tasks = tasks;
	}
	
	

}
