package model.dao.impl;

import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import db.DB;
import db.DbException;
import db.DbIntegrityException;
import model.dao.VehicleDao;
import model.entities.Vehicle;
import model.entities.enums.VehicleStatus;

public class VehicleDaoJDBC implements VehicleDao {

	private Connection conn;

	public VehicleDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	public Vehicle findByChassis(String chassis) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			String sql = "SELECT * FROM vehicle WHERE Chassis = ?";
			st = conn.prepareStatement(sql);
			rs = st.executeQuery();

			if (rs.next()) {

				Vehicle obj = new Vehicle();
				obj.setChassis(rs.getString("Chassis"));
				obj.setModel(rs.getString("Model"));
				obj.setDateEntry(rs.getDate("DateEntry"));
				obj.setExitDate(rs.getDate("ExitDate"));
				obj.setVehicleStatus(VehicleStatus.valueOf(rs.getString("Status")));
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
	public List<Vehicle> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			String sql = "SELECT * FROM Vehicle ORDER BY Chassis";
			st = conn.prepareStatement(sql);
			rs = st.executeQuery();

			List<Vehicle> list = new ArrayList<>();

			while (rs.next()) {
				Vehicle obj = new Vehicle();
				obj.setChassis(rs.getString("Chassis"));
				obj.setModel(rs.getString("Model"));
				obj.setDateEntry(new java.sql.Date(rs.getDate("DateEntry").getTime()));
				obj.setExitDate(new java.sql.Date(rs.getDate("ExitDate").getTime()));
				obj.setVehicleStatus(VehicleStatus.valueOf(rs.getString("Status")));
				list.add(obj);
			}
			return list;
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}

	}

	@Override
	public void insert(Vehicle obj) {
		PreparedStatement st = null;
		try {
			String sql = "INSERT INTO vehicle " + " (Chassis, Model, DateEntry, ExitDate, Status) " + " VALUES "
					+ " (?, ?, ?, ?, ?)";
			st = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

			st.setString(1, obj.getChassis());
			st.setString(2, obj.getModel());
			st.setDate(3, new java.sql.Date(obj.getDateEntry().getTime()));
			st.setDate(4, new java.sql.Date(obj.getExitDate().getTime()));
			st.setString(5, obj.getStatus());

			int rowsAffected = st.executeUpdate();

			if (rowsAffected > 0) {
				JOptionPane.showMessageDialog(null, "Success full savad Vehicle");
			} else {
				throw new DbException("Unexpected error! No rows affected!");
			}

		} 
		catch (SQLException e)
		{
			throw new DbException(e.getMessage());
		} 
		finally {
			DB.closeStatement(st);
		}

	}

	@Override
	public void update(Vehicle obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
				"UPDATE vehicle " +
				"SET Model = ?, DateEntry = ?, ExitDate = ?, Status = ?," +
				"WHERE Chassis = ?");

			st.setString(5, obj.getChassis());
			st.setString(1, obj.getModel());
			st.setDate(2, new java.sql.Date(obj.getDateEntry().getTime()));
			st.setDate(3, new java.sql.Date(obj.getExitDate().getTime()));
			st.setString(4, obj.getStatus());


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
}


