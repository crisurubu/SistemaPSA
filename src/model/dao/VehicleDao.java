package model.dao;

import java.util.List;

import model.entities.Vehicle;
import model.entities.VehicleStatus;

public interface VehicleDao {
	
	void insert(Vehicle obj);
	void update(Vehicle obj);
	void deleteByChassis(String chassis);
	Vehicle findById(Integer id);
	List<Vehicle> findAll();
	List<Vehicle> findByVehicleStatus(VehicleStatus vehicleStatus);
	
	

}
