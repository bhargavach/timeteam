package au.com.sap.mcc.timeteam.dao;

import org.springframework.data.repository.CrudRepository;

import au.com.sap.mcc.timeteam.model.AppUser;

public interface AppUserRepository extends CrudRepository<AppUser, String> {

}
