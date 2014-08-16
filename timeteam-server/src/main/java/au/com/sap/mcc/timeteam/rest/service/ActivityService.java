package au.com.sap.mcc.timeteam.rest.service;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import au.com.sap.mcc.timeteam.rest.model.ActivityRequest;
import au.com.sap.mcc.timeteam.rest.model.ActivityResponse;

@Consumes("application/json")
@Produces("application/json")
public interface ActivityService {

	@GET
	@Path("/get/{activityId}/")
	public ActivityResponse getActivity(@PathParam(value = "activityId") String id);
	
	@GET
	@Path("/list")
	public ActivityResponse listAll();
	
	@POST
	@Path("/create")
	public ActivityResponse createActivity(ActivityRequest request);
	
	@PUT
	@Path("/update")
	public ActivityResponse updateActivity(ActivityRequest request);
	
	@DELETE
	@Path("/delete")
	public ActivityResponse deleteActivity(ActivityRequest request);
}
