package au.com.sap.mcc.timeteam.dao;

import org.springframework.data.repository.CrudRepository;

import au.com.sap.mcc.timeteam.model.Activity;

public interface ActivityRepository extends CrudRepository<Activity, String> {

}
