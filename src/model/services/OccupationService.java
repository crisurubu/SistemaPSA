package model.services;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.OccupationDao;
import model.entities.Occupation;

public class OccupationService {
	
	private OccupationDao dao = DaoFactory.createOccupationDao();

	
	public List<Occupation> findAll(){
		return dao.findAll();
		
	}
	
	public void saveOrUpdate(Occupation obj) {
		if(obj.getId() == null) {
			dao.insert(obj);
		}
		else {
			dao.update(obj);
		}

		
	}
	public void remove(Occupation obj) {
		dao.deleteById(obj.getId());
	}

	

}
