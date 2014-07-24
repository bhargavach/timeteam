package au.com.sap.mcc.timeteam.rest.model;

import java.util.List;

import au.com.sap.mcc.timeteam.model.Task;


public class TaskResponse {

	private List<Task> tasks;
	private String errorMessage;
	private Boolean success = true;
	
	public List<Task> getTasks() {
		return tasks;
	}
	public void setTasks(List<Task> tasks) {
		this.tasks = tasks;
	}
	public String getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	public Boolean getSuccess() {
		return success;
	}
	public void setSuccess(Boolean success) {
		this.success = success;
	}	
}
