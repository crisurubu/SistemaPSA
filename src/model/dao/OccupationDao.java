package model.dao;

import java.util.List;

import model.entities.Occupation;

public interface OccupationDao {
	
	void insert(Occupation obj);
	void update(Occupation obj);
	void deleteById(Integer id);
    Occupation findById(Integer id);
    List<Occupation> findAll();

}
