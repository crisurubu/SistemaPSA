package model.dao;

import java.util.List;

import model.entities.Vehicle;

public interface VehicleDao {
	
	void insert(Vehicle obj);
	void update(Vehicle obj);
	void deleteByChassis(String chassis);
	Vehicle findByChassis(String chassis);
	List<Vehicle> findAll();

}
