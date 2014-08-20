package au.com.sap.mcc.timeteam.rest.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Task {

	protected String id;
	protected int number;
	protected Long version = 0l;
	protected String name;
	protected String shortname;
	protected String projectId;
	protected int totalduration;
	protected List<Activity> activities = new ArrayList<Activity>();

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}

	public String getShortname() {
		return shortname;
	}

	public void setShortname(String shortname) {
		this.shortname = shortname;
	}

	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String project_id) {
		this.projectId = project_id;
	}

	public List<Activity> getActivities() {
		return activities;
	}

	public void setActivities(List<Activity> activities) {
		this.activities = activities;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public int getTotalduration() {
		return totalduration;
	}

	public void setTotalduration(int totalduration) {
		this.totalduration = totalduration;
	}

	@JsonIgnore
	public static final Task convert(au.com.sap.mcc.timeteam.model.Task jpaTask) {
		Task task = new Task();

		task.setId(jpaTask.getId());
		task.setNumber(jpaTask.getNumber());
		task.setVersion(jpaTask.getVersion());
		task.setName(jpaTask.getName());
		task.setTotalduration(jpaTask.totalDuration());

		if (jpaTask.getProject() != null) {
			task.setProjectId(jpaTask.getProject().getId());
			task.setShortname(jpaTask.getShortName());
		}

		List<Activity> activities = new ArrayList<Activity>();

		if (activities != null) {
			for (au.com.sap.mcc.timeteam.model.Activity jpaActivity : jpaTask.getActivities()) {
				Activity activity = Activity.convert(jpaActivity);
				activities.add(activity);
			}
		}

		task.setActivities(activities);

		return task;
	}

}
