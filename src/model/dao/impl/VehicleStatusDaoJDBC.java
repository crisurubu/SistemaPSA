package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import db.DB;
import db.DbException;
import model.dao.VehicleStatusDao;
import model.entities.Vehicle;
import model.entities.VehicleStatus;

public class VehicleStatusDaoJDBC implements VehicleStatusDao{

	
	private Connection conn;
	
	public VehicleStatusDaoJDBC(Connection conn) {
		this.conn = conn;
	}
	
		
	
	@Override
	public List<VehicleStatus> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			st = conn.prepareStatement(
					"SELECT * FROM vehicleStatus ORDER BY Status");
			rs = st.executeQuery();
			
			List<VehicleStatus> list = new ArrayList<>();
			
			while(rs.next()) {
				VehicleStatus obj = new VehicleStatus();
				obj.setId(rs.getInt("Id"));
				obj.setStatus(rs.getString("Status"));
				
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
	public VehicleStatus findById(Integer id) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT * FROM vehicleStatus WHERE Id = ?");
			st.setInt(1, id);
			rs = st.executeQuery();
			
			if(rs.next()) {
				VehicleStatus obj = new VehicleStatus();
				obj.setId(rs.getInt("Id"));
				obj.setStatus(rs.getString("Status"));
				return  obj;
			}
			return null;
			
			
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
	public void insert(VehicleStatus obj) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void updateStatus(Vehicle obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
				"UPDATE vehicle.Status " 
			   +"SET  Status_Id =  3"
			   +"WHERE Id = ?");

			
			st.setInt(1, obj.getId());


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
		// TODO Auto-generated method stub
		
	}



	@Override
	public void update(VehicleStatus obj) {
		// TODO Auto-generated method stub
		
	}


	
}

	

	
	
	


