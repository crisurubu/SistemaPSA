package model.services;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.VehicleStatusDao;
import model.entities.Vehicle;
import model.entities.VehicleStatus;

public class VehicleStatusService {
	
private VehicleStatusDao dao =  DaoFactory.createVehicleStatusDao();
	
	
	public List<VehicleStatus> findAll(){
		return dao.findAll();
	}

	public void saveOrUpdate(VehicleStatus obj) {
		if(obj.getId() == null) {
			dao.insert(obj);
		}
		else {
			dao.update(obj);
		}

		
	}
	public void remove(VehicleStatus obj) {
		dao.deleteById(obj.getId());
	}
	
	public VehicleStatus findById(Integer id) {
		return dao.findById(id);
	}
	public void upDateStatus(Vehicle vehicle) {
		 dao.updateStatus(vehicle);
	}
	

}
