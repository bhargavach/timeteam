package au.com.sap.mcc.timeteam.rest.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Project {
	
	protected String id;
	protected String name;
	protected String shortname;
	protected Long version = 0l;
	protected List<Task> tasks = new ArrayList<Task>();
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getShortname() {
		return shortname;
	}
	public void setShortname(String shortname) {
		this.shortname = shortname;
	}
	public Long getVersion() {
		return version;
	}
	public void setVersion(Long version) {
		this.version = version;
	}
	public List<Task> getTasks() {
		return tasks;
	}
	public void setTasks(List<Task> tasks) {
		this.tasks = tasks;
	}
	
	@JsonIgnore
	public static final Project convert(au.com.sap.mcc.timeteam.model.Project jpaProject) {
		Project project = new Project();
		project.setId(jpaProject.getId());
		project.setName(jpaProject.getName());
		project.setShortname(jpaProject.getShortname());
		project.setVersion(jpaProject.getVersion());
		List<Task> tasks = new ArrayList<Task>();
		
		for(au.com.sap.mcc.timeteam.model.Task jpaTask : jpaProject.getTasks()) {
			Task task = Task.convert(jpaTask);
			tasks.add(task);
		}
		
		project.setTasks(tasks);
		
		return project;
	}
}
