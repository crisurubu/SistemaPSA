package model.dao;

import java.util.List;

import model.entities.Appointments;
import model.entities.Vehicle;
import model.entities.VehicleStatus;

public interface AppointmentsDao {
	
	void insert(Appointments obj);
	void update(Appointments obj);
	void deleteById(Integer id);
	Vehicle findById(Integer id);
	Appointments findByIdAppointments(Integer id);
	List<Vehicle> findAll();
	List<Vehicle> findByVehicleStatus(VehicleStatus vehicleStatus);
	List<Appointments> findAllAppointments();
	
	

}
