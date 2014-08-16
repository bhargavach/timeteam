package au.com.sap.mcc.timeteam.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import org.apache.commons.lang3.builder.ToStringBuilder;

@Entity
@Table(name = "APPUSER")
public class AppUser extends BaseEntity implements Serializable{

	private static final long serialVersionUID = 1L;

	@Column(name="USERNAME", length = 30, nullable=true)
	@Size(max = 30, message = "{model.user.name.size.error}")
	protected String username = null;
	
	/*@ManyToMany
    @JoinTable(
            name = "user_project",
            joinColumns = { @JoinColumn(name = "user_id") },
            inverseJoinColumns = @JoinColumn(name = "projectId")
    )
	protected List<Project> projects;
*/
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	/*public List<Project> getProjects() {
		return projects;
	}

	public void setProjects(List<Project> projects) {
		this.projects = projects;
	}
*/
	public String toString()
	{
	    return new ToStringBuilder(this).appendSuper(super.toString()).append("username", this.username).toString();
	}
}
