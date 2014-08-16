package au.com.sap.mcc.timeteam.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import au.com.sap.mcc.timeteam.model.AppUser;

@Service
public class AppUserDaoImpl extends AbstractBaseDao<AppUser> implements AppUserDao {

	@Autowired
	AppUserRepository repository;
	
	@SuppressWarnings("rawtypes")
	@Override
	public CrudRepository getRepository() {
		return repository;
	}


}
