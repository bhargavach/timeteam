package au.com.sap.mcc.timeteam.dao;

import java.util.List;

import au.com.sap.mcc.timeteam.model.BaseEntity;

public interface BaseDao<K extends BaseEntity> {

	public K fetchById(String id);
	public List<K> findAll();
	public K save(K k);
	public void delete(K k);
	
}
