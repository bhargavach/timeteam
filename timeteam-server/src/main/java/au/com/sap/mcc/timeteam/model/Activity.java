package au.com.sap.mcc.timeteam.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.commons.lang3.builder.ToStringBuilder;

@Entity
@Table(name = "ACTIVITY")
public class Activity extends BaseEntity {

	@ManyToOne
	@JoinColumn(name = "TASK_ID", nullable = false, updatable = false)
	protected Task task;
	
	@Temporal(TemporalType.TIMESTAMP)
    @Column(name="CAPTUREDATE", updatable = false)
    protected Date capturedate = null;
	
	@Column(name = "DURATION")
	protected int duration = 0;

	public Task getTask() {
		return task;
	}

	public void setTask(Task task) {
		this.task = task;
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
	
	public String toString() {
		return new ToStringBuilder(this).appendSuper(super.toString()).append("task", task.getShortName()).append("capturedate", this.capturedate).append("duration", this.duration).toString();
	}
}
