package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import db.DB;
import db.DbException;
import db.DbIntegrityException;
import model.dao.ProductionDao;
//import model.entities.Appointments;
import model.entities.Production;
import model.entities.Vehicle;
import model.entities.VehicleStatus;


public class ProductionDaoJDBC implements ProductionDao {

	private Connection conn;

	public ProductionDaoJDBC(Connection conn) {
		this.conn = conn;
	}
	@Override
	public Production findByIdProduction(Integer id) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			String sql = "SELECT production.*, vehicle.Id "
					+ "FROM production INNER JOIN vehicle "
					+ "ON production.Vehicle_Id = vehicle.Id  "
					+ "WHERE vehicle.Id = ?";
			st = conn.prepareStatement(sql);
			st.setInt(1, id);
			rs = st.executeQuery();

			while (rs.next()) {
				Production production = new Production();
				production.setId(rs.getInt("Id"));

							
				return production;

			}

		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
		
		return null;
		
	}
	
	
	@Override
	public Production findByProduction(Integer id) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			String sql = "SELECT * FROM production WHERE Id = ? ";
					
					
			st = conn.prepareStatement(sql);
			st.setInt(1, id);
			rs = st.executeQuery();

			while (rs.next()) {
				Production production = new Production();
				production.setId(rs.getInt("Id"));
				production.setVehicle_Id(rs.getInt("Vehicle_Id"));

							
				return production;

			}

		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
		
		return null;
		
	}


	@Override
	public Vehicle findById(Integer id) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			String sql = "SELECT vehicle.*, vehicleStatus.Status as Status "
					+ "FROM vehicle INNER JOIN vehicleStatus "
					+ "ON vehicle.Status_Id = vehicleStatus.Id  "
					+ "WHERE vehicle.Id = ?";
			st = conn.prepareStatement(sql);
			st.setInt(1, id);
			rs = st.executeQuery();

			if (rs.next()) {

				VehicleStatus vst = instantiateVehicleStatus(rs);
				Vehicle obj       = instantiateVehicle(rs, vst);				
				return obj;

			}

		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
		return null;

	}
	
	@Override
	public void insert(Production obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
					"INSERT INTO production "
					+ "(Vehicle_Id) "
					+ "VALUES "
					+ "(?)"
					, Statement.RETURN_GENERATED_KEYS);
						

			st.setInt(1, obj.getVehicle_Id());
			


			int rowsAffected = st.executeUpdate();
			
			if (rowsAffected > 0) {
				ResultSet rs = st.getGeneratedKeys();
				if (rs.next()) {
					int id = rs.getInt(1);
					obj.setId(id);
				}
				DB.closeResultSet(rs);
			}
			else {
				throw new DbException("Unexpected error! No rows affected!");
			}
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
		}
	}	
	@Override
	public List<Vehicle> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement("SELECT a.*, b.Status "
					+ "FROM vehicle as A "
					+ "INNER JOIN vehicleStatus as B "
					+ "on a.Status_Id = b.Id and a.Status_Id = 1");
							
			
			rs = st.executeQuery();
			
			List<Vehicle> list = new ArrayList<>();
			Map<Integer, VehicleStatus> map = new HashMap<>();
			
			while (rs.next()) {
				
				VehicleStatus vst = map.get(rs.getInt("Status_Id"));
				
				if (vst == null) {
					vst = instantiateVehicleStatus(rs);
					map.put(rs.getInt("Status_Id"), vst);
				}
				
				Vehicle obj = instantiateVehicle(rs, vst);
				list.add(obj);
			}
			return list;
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}


	
	
	private VehicleStatus instantiateVehicleStatus(ResultSet rs) throws SQLException {
		VehicleStatus dep = new VehicleStatus();
		dep.setId(rs.getInt("Status_Id"));
		dep.setStatus(rs.getString("Status"));
		return dep;
	}


	
	
	@Override
	public List<Vehicle> findByVehicleStatus(VehicleStatus vehicleStatus) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT vehicle.* ,vehicleStatus.Status as Status "
					+ "FROM vehicle INNER JOIN vehicleStatus "
					+ "ON vehicle.Status_Id = vehicleStatus.Id and vehicle.Status_Id = 1 "
					+ "WHERE Status_Id = ? "
					+ "ORDER BY Chassis");
			
			st.setInt(1, vehicleStatus.getId());
			
			rs = st.executeQuery();
			
			List<Vehicle> list = new ArrayList<>();
			Map<Integer, VehicleStatus> map = new HashMap<>();
			
			while (rs.next()) {
				
				VehicleStatus vst = map.get(rs.getInt("Status_Id"));
				
				if (vst == null) {
					vst = instantiateVehicleStatus(rs);
					map.put(rs.getInt("Status_Id"), vst);
				}				
				Vehicle obj = instantiateVehicle(rs, vst);
				list.add(obj);
			}
			return list;
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}

	private Vehicle instantiateVehicle(ResultSet rs, VehicleStatus vst) throws SQLException {
		Vehicle obj = new Vehicle();
		obj.setId(rs.getInt("Id"));
		obj.setChassis(rs.getString("Chassis"));
		obj.setModel(rs.getString("Model"));
		obj.setDateEntry(new java.util.Date(rs.getTimestamp("DateEntry").getTime()));
		obj.setExitDate(new java.util.Date(rs.getTimestamp("ExitDate").getTime()));
		obj.setVehicleStatus(vst);
		return obj;
	}

	@Override
	public void update(Production obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
				"UPDATE production " 
			   +"SET vehicle_Id = ? "
			   +"WHERE Id = ?");

			st.setInt(1, obj.getId());
			st.setInt(2, obj.getVehicle_Id());
			


			st.executeUpdate();
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		} 
		finally {
			DB.closeStatement(st);
		}
		
	}

	@Override
	public void deleteById(Integer id) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
				"DELETE FROM production WHERE Id = ?");

			st.setInt(1, id);

			st.executeUpdate();
		}
		catch (SQLException e) {
			throw new DbIntegrityException(e.getMessage());
		} 
		finally {
			DB.closeStatement(st);
		}
		
	}
	
		
	
}


