
package model.services;

import java.util.List;

import model.dao.AppointmentsDao;
import model.dao.DaoFactory;
import model.entities.Appointments;
import model.entities.Vehicle;

public class AppointmentsService {
	
	private AppointmentsDao dao = DaoFactory.createAppointmentsDao();
	
	
	public List<Vehicle> findAll(){
		return dao.findAll();
	}
	
	public List<Appointments> findAllAppointments(){
		return dao.findAllAppointments();
	}
	
	public void saveOrUpdate(Appointments obj) {
		if(obj.getId() == null)
		{
			dao.insert(obj);
		}
		else
		{
			dao.update(obj);
		}
		
	}
	public void remove(Appointments obj) {
		dao.deleteById(obj.getId());
	}
	
	public Vehicle findById(Integer id) {
		return dao.findById(id);
	}
	
	public Appointments findByIdAppointments(Integer id) {
		return dao.findByIdAppointments(id);
	}
	
	
}
