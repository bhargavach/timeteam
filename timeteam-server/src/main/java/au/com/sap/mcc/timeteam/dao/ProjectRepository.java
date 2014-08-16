package au.com.sap.mcc.timeteam.dao;

import org.springframework.data.repository.CrudRepository;

import au.com.sap.mcc.timeteam.model.Project;

public interface ProjectRepository extends CrudRepository<Project, String> {

}
