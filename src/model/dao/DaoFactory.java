package model.dao;

import db.DB;
import model.dao.impl.AppointmentsDaoJDBC;
import model.dao.impl.EmployeeDaoJDBC;
import model.dao.impl.OccupationDaoJDBC;
import model.dao.impl.ProductionDaoJDBC;
import model.dao.impl.VehicleDaoJDBC;
import model.dao.impl.VehicleStatusDaoJDBC;

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
	
	
	public static VehicleStatusDao createVehicleStatusDao() {
		return new VehicleStatusDaoJDBC(DB.getConnection());
	}

	public static ProductionDao createProductionDao() {
		return new ProductionDaoJDBC(DB.getConnection());
		
	}

	public static AppointmentsDao createAppointmentsDao() {
		return new AppointmentsDaoJDBC(DB.getConnection());
	}

	
	
	
}
