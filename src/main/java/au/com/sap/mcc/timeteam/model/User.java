package au.com.sap.mcc.timeteam.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import org.apache.commons.lang3.builder.ToStringBuilder;

@Entity
@Table(name = "USER")
public class User extends BaseEntity implements Serializable{

	private static final long serialVersionUID = 1L;

	@Column(name="NAME", length = 30, nullable=true)
	@Size(max = 30, message = "{model.user.name.size.error}")
	protected String name = null;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String toString()
	{
	    return new ToStringBuilder(this).appendSuper(super.toString()).append("name", this.name).toString();
	}
}
