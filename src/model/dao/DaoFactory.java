package model.dao;

import db.DB;
import model.dao.impl.EmployeeDaoJDBC;
import model.dao.impl.OccupationDaoJDBC;
import model.dao.impl.VehicleDaoJDBC;

public class DaoFactory {
	
	public static OccupationDao createOccupationDao() {
		return new OccupationDaoJDBC(DB.getConnection());
	}

	public static EmployeeDao createEmployeeDao() {
		return new EmployeeDaoJDBC(DB.getConnection());
	}
	
	public static VehicleDao createVehicleDao() {
		return new VehicleDaoJDBC(DB.getConnection());
	}

}
