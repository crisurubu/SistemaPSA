package model.dao;

import java.util.List;

import model.entities.Production;
import model.entities.Vehicle;
import model.entities.VehicleStatus;

public interface ProductionDao {
	
	void insert(Production obj);
	void update(Production obj);
	void deleteById(Integer id);
	Vehicle findById(Integer id);
	List<Vehicle> findAll();
	List<Vehicle> findByVehicleStatus(VehicleStatus vehicleStatus);
	
	

}
