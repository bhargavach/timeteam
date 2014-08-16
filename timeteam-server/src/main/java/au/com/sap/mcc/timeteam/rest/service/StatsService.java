package au.com.sap.mcc.timeteam.rest.service;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

@Consumes("application/json")
@Produces("application/json")
public interface StatsService {

	@GET
	@Path("/timeAllocationByProject")
	public void timeAllocationByProject();
	
	@GET
	@Path("/timeAllocationByTask/{projectId}/")
	public void timeAllocationByTask(String projectId);
	
	@GET
	@Path("/monthlyTimeTotal/{month}/")
	public void monthlyTimeTotal(int month);
	
	@GET
	@Path("/timeAllocationByDay/{projectId}/")
	public void timeAllocationByDay(String projectId);
	
}
