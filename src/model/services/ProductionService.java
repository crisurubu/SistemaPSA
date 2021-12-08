
package model.services;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.ProductionDao;
import model.entities.Production;
import model.entities.Vehicle;

public class ProductionService {
	
	private ProductionDao dao = DaoFactory.createProductionDao();
	
	
	public List<Vehicle> findAll(){
		return dao.findAll();
	}
	
	public void saveOrUpdate(Production obj) {
		if(obj.getId() == null)
		{
			dao.insert(obj);
		}
		else
		{
			dao.update(obj);
		}
		
	}
	public void remove(Production obj) {
		dao.deleteById(obj.getId());
	}
	
	public Vehicle findById(Integer id) {
		return dao.findById(id);
	}
	
	
}
