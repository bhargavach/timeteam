package au.com.sap.mcc.timeteam.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.apache.commons.lang3.builder.ToStringBuilder;

@Entity
@Table(name = "PROJECT")
public class Project extends BaseEntity {

	@Column(name="NAME", length = 30, nullable=false)
	@NotNull(message = "{model.project.name.null.error}")
	@Size(max = 30, message = "{model.project.name.size.error}")
	protected String name;
	
	@Column(name="SHORTNAME", length = 3, nullable=true)
	@Size(max = 3, message = "{model.project.shortname.size.error}")
	protected String shortname;
	
	@OneToMany(mappedBy = "project", fetch = FetchType.EAGER)
	protected List<Task> tasks = new ArrayList<Task>();
	
	/*@ManyToMany(mappedBy = "projects")
	protected List<AppUser> users;*/

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

	public List<Task> getTasks() {
		return tasks;
	}

	public void setTasks(List<Task> tasks) {
		this.tasks = tasks;
	}

/*	public List<AppUser> getUsers() {
		return users;
	}

	public void setUsers(List<AppUser> users) {
		this.users = users;
	}*/	
	
	public int totalDuration() {
		int total = 0;
		
		if(this.tasks != null) {
			for(Task task : tasks) {
				total += task.totalDuration();
			}
		}
		
		return total;
	}
	
	@Override
	public String toString() {
		return new ToStringBuilder(this).appendSuper(super.toString()).append("name", this.name).append("shortname", this.shortname).toString();
	}
}
