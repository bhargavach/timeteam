package au.com.sap.mcc.timeteam.rest.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Activity {

	private String id;
	private String taskId;
	protected Date capturedate = null;
	protected int duration = 0;
	protected Long version = 0l;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public Date getCapturedate() {
		return capturedate;
	}

	public void setCapturedate(Date capturedate) {
		this.capturedate = capturedate;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}

	@JsonIgnore
	public static final Activity convert(au.com.sap.mcc.timeteam.model.Activity jpaActivity) {
		Activity activity = new Activity();

		activity.setCapturedate(jpaActivity.getCapturedate());
		activity.setDuration(jpaActivity.getDuration());
		activity.setId(jpaActivity.getId());

		if (jpaActivity.getTask() != null) {
			activity.setTaskId(jpaActivity.getTask().getId());
		}
		return activity;
	}

}
