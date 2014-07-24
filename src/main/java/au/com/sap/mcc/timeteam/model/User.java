package au.com.sap.mcc.timeteam.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "USER")
public class User extends BaseEntity implements Serializable{

	private static final long serialVersionUID = 1L;

}
