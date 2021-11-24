package model.dao;

import db.DB;
import model.dao.impl.OccupationDaoJDBC;

public class DaoFactory {
	
	public static OccupationDao createOccupationDao() {
		return new OccupationDaoJDBC(DB.getConnection());
	}

}
