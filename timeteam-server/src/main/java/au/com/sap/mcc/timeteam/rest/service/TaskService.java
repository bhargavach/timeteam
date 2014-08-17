package au.com.sap.mcc.timeteam.rest.service;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import au.com.sap.mcc.timeteam.rest.model.TaskRequest;
import au.com.sap.mcc.timeteam.rest.model.TaskResponse;


@Consumes("application/json")
@Produces("application/json")
public interface TaskService {

	@GET
	@Path("/get/{taskId}/")
	public TaskResponse get(@PathParam(value = "taskId") String id);
	
	@GET
	@Path("/list")
	public TaskResponse list();
	
	@POST
	@Path("/create")
	public TaskResponse create(TaskRequest request);
	
	@PUT
	@Path("/update")
	public TaskResponse update(TaskRequest request);
	
	@DELETE
	@Path("/delete")
	public TaskResponse delete(TaskRequest request);
}
