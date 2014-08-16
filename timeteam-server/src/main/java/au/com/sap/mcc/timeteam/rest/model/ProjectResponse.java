package au.com.sap.mcc.timeteam.rest.model;

import java.util.ArrayList;
import java.util.List;

public class ProjectResponse extends RestResponse {
	
	protected List<Project> projects = new ArrayList<Project>();

	public List<Project> getProjects() {
		return projects;
	}

	public void setProjects(List<Project> projects) {
		this.projects = projects;
	}
	
	

}
