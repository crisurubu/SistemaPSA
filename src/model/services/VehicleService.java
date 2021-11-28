package model.services;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.VehicleDao;
import model.entities.Vehicle;

public class VehicleService {
	
	private VehicleDao dao = DaoFactory.createVehicleDao();
	
	
	public List<Vehicle> findAll(){
		return dao.findAll();
	}
	
	public void saveOrUpdate(Vehicle obj) {
		if(obj.getChassis() == null)
		{
			dao.insert(obj);
		}
		else
		{
			dao.update(obj);
		}
		
	}
	public void remove(Vehicle obj) {
		dao.deleteByChassis(obj.getChassis());
	}
}
