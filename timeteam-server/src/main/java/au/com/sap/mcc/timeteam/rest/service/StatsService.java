package au.com.sap.mcc.timeteam.rest.service;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import au.com.sap.mcc.timeteam.rest.model.StatsResponse;

@Consumes("application/json")
@Produces("application/json")
public interface StatsService {

	@GET
	@Path("/timeAllocationByProject")
	public StatsResponse timeAllocationByProject();
	
	@GET
	@Path("/timeAllocationByTask/{projectId}/")
	public StatsResponse timeAllocationByTask(@PathParam(value = "projectid") String id);
	
	@GET
	@Path("/monthlyTimeTotal/{month}/")
	public void monthlyTimeTotal(int month);
	
	@GET
	@Path("/timeAllocationByDay/{projectId}/")
	public void timeAllocationByDay(String projectId);
	
}
