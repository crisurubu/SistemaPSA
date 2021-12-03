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
import model.dao.VehicleDao;
import model.entities.Vehicle;
import model.entities.VehicleStatus;


public class VehicleDaoJDBC implements VehicleDao {

	private Connection conn;

	public VehicleDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	public Vehicle findById(Integer id) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			String sql = "SELECT vehicle.*, vehicleStatus.Status as Status "
					+ "FROM vehicle INNER JOIN vehicleStatus "
					+ "ON vehicle.Status_Id = vehicleStatus.Id "
					+ " WHERE vehicle.Id = ?";
			st = conn.prepareStatement(sql);
			st.setInt(1, id);
			rs = st.executeQuery();

			if (rs.next()) {

				VehicleStatus vst = instantiateVehicleStatus(rs);
				Vehicle obj = instantiateVehicle(rs, vst);				
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
	public void insert(Vehicle obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
					"INSERT INTO vehicle "
					+ "(Chassis, Model, DateEntry, ExitDate, Status_Id) "
					+ "VALUES "
					+ "(?, ?, ?, ?, ?)"
					, Statement.RETURN_GENERATED_KEYS);
						

			st.setString(1, obj.getChassis());
			st.setString(2, obj.getModel());
			st.setDate(3, new java.sql.Date(obj.getDateEntry().getTime()));
			st.setDate(4, new java.sql.Date(obj.getExitDate().getTime()));
			st.setInt(5, obj.getVehicleStatus().getId());


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
					+ "on a.Status_Id = b.Id");
							
			
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


	@Override
	public void update(Vehicle obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
				"UPDATE vehicle " 
			   +"SET Chassis = ?, Model = ?, DateEntry = ?, ExitDate = ?, Status_Id = ? "
			   +"WHERE Id = ?");

			st.setString(1, obj.getChassis());
			st.setString(2, obj.getModel());
			st.setDate(3, new java.sql.Date(obj.getDateEntry().getTime()));
			st.setDate(4, new java.sql.Date(obj.getExitDate().getTime()));
			st.setInt(5, obj.getVehicleStatus().getId());
			st.setInt(6, obj.getId());


			st.executeUpdate();
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		} 
		finally {
			DB.closeStatement(st);
		}

	}
	
	private VehicleStatus instantiateVehicleStatus(ResultSet rs) throws SQLException {
		VehicleStatus dep = new VehicleStatus();
		dep.setId(rs.getInt("Status_Id"));
		dep.setStatus(rs.getString("Status"));
		return dep;
	}


	@Override
	public void deleteByChassis(String chassis) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
				"DELETE FROM vehicle WHERE Chassis = ?");

			st.setString(1, chassis);

			st.executeUpdate();
		}
		catch (SQLException e) {
			throw new DbIntegrityException(e.getMessage());
		} 
		finally {
			DB.closeStatement(st);
		}
	}
	
	@Override
	public List<Vehicle> findByVehicleStatus(VehicleStatus vehicleStatus) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT vehicle.* ,vehicleStatus.Status as Status "
					+ "FROM vehicle INNER JOIN vehicleStatus "
					+ "ON vehicle.Status_Id = vehicleStatus.Id "
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

	
}


