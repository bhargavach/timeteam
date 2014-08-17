package au.com.sap.mcc.timeteam.rest.service;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import au.com.sap.mcc.timeteam.rest.model.ProjectRequest;
import au.com.sap.mcc.timeteam.rest.model.ProjectResponse;

@Consumes("application/json")
@Produces("application/json")
public interface ProjectService {

	@GET
	@Path("/get/{projectId}/")
	public ProjectResponse get(@PathParam(value = "projectId") String id);
	
	@GET
	@Path("/list")
	public ProjectResponse list();
	
	@POST
	@Path("/create")
	public ProjectResponse create(ProjectRequest request);
	
	@PUT
	@Path("/update")
	public ProjectResponse update(ProjectRequest request);
	
	@DELETE
	@Path("/delete")
	public ProjectResponse delete(ProjectRequest request);
}
