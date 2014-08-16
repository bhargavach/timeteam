package au.com.sap.mcc.timeteam.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import au.com.sap.mcc.timeteam.model.Project;

@Service
public class ProjectDaoImpl extends AbstractBaseDao<Project> implements ProjectDao {

	@Autowired
	ProjectRepository repository;
	
	@SuppressWarnings("rawtypes")
	@Override
	public CrudRepository getRepository() {
		return repository;
	}


}
