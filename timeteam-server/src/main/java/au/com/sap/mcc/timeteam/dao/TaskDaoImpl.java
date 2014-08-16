package au.com.sap.mcc.timeteam.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import au.com.sap.mcc.timeteam.model.Task;

@Service
public class TaskDaoImpl extends AbstractBaseDao<Task> implements TaskDao{
	
	@Autowired
	TaskRepository taskRepository = null;

	@SuppressWarnings("rawtypes")
	@Override
	public CrudRepository getRepository() {
		return taskRepository;
	}
	
}
