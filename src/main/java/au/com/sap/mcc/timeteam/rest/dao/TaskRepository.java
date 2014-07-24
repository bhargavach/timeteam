package au.com.sap.mcc.timeteam.rest.dao;

import org.springframework.data.repository.CrudRepository;

import au.com.sap.mcc.timeteam.model.Task;

public interface TaskRepository extends CrudRepository<Task, String>{

}
