package au.com.sap.mcc.timeteam.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import au.com.sap.mcc.timeteam.model.Activity;

@Service
public class ActivityDaoImpl extends AbstractBaseDao<Activity> implements ActivityDao {

	@Autowired
	ActivityRepository repository;

	@SuppressWarnings("rawtypes")
	@Override
	public CrudRepository getRepository() {
		return repository;
	}

}
