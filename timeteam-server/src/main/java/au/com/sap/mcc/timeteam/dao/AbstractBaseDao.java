package au.com.sap.mcc.timeteam.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import au.com.sap.mcc.timeteam.model.BaseEntity;

public abstract class AbstractBaseDao<K extends BaseEntity> implements BaseDao<K> {

	@SuppressWarnings("unchecked")
	public K fetchById(String id) {
		K entity = (K) getRepository().findOne(id);

		if (entity == null) {
			throw new RuntimeException("Entity Not Found: " + id);
		}

		return entity;
	}
	
	@SuppressWarnings("unchecked")
	public List<K> findAll(){
		return (List<K>) getRepository().findAll();
	}

	@SuppressWarnings("unchecked")
	public K save(K entity) {
		return (K) getRepository().save(entity);
	}

	@SuppressWarnings("unchecked")
	public void delete(K entity) {
		getRepository().delete(entity.getId());
	}

	@SuppressWarnings("rawtypes")
	public abstract CrudRepository getRepository();
}
