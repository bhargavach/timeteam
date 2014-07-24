package au.com.sap.mcc.timeteam.rest.service;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import au.com.sap.mcc.timeteam.rest.model.TaskRequest;
import au.com.sap.mcc.timeteam.rest.model.TaskResponse;


@Consumes("application/json")
@Produces("application/json")
public interface TaskManager {

	@POST
	@Path("/fetchTaskById/")
	public TaskResponse fetchTaskById(TaskRequest request);
	
	@POST
	@Path("/fatchAllTasks")
	public TaskResponse fetchAllTasks(TaskRequest request);
	
	@POST
	@Path("/saveTask")
	public TaskResponse saveTask(TaskRequest request);
	
	@POST
	@Path("/deleteTask")
	public TaskResponse deleteTask(TaskRequest request);
}
