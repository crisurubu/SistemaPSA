package model.dao;

import java.util.List;

import model.entities.VehicleStatus;

public interface VehicleStatusDao {
	
	void insert(VehicleStatus obj);
	void update(VehicleStatus obj);
	void deleteById(Integer id);
	VehicleStatus findById(Integer id);
    List<VehicleStatus> findAll();

}
