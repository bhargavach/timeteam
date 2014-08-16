package au.com.sap.mcc.timeteam.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.apache.commons.lang3.builder.ToStringBuilder;

@Entity
@Table(name = "TASK")
public class Task extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name = "NAME", length = 30, nullable = false)
	@NotNull(message = "{model.task.name.null.error}")
	@Size(max = 30, message = "{model.task.name.size.error}")
	protected String name = null;
	
	@Column(name = "NUMBER")
	@GeneratedValue(strategy = GenerationType.AUTO)
	protected int number;

	@ManyToOne
	@JoinColumn(name = "PROJECT_ID", nullable = false, updatable = false)
	protected Project project;

	@OneToMany(mappedBy = "task", fetch = FetchType.EAGER)
	protected List<Activity> activities;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public List<Activity> getActivities() {
		return activities;
	}

	public void setActivities(List<Activity> activities) {
		this.activities = activities;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}
	
	public String getShortName() {
		if(this.project == null) {
			return this.name.concat("-" + String.valueOf(this.number));
		}
		return this.project.getShortname().concat("-" + String.valueOf(this.number));
	}

	public String toString() {
		return new ToStringBuilder(this).appendSuper(super.toString()).append("name", this.name).append("shortname", this.getShortName()).toString();
	}
	
	
}
