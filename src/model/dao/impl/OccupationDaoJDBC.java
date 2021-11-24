package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import db.DB;
import db.DbException;
import db.DbIntegrityException;
import model.dao.OccupationDao;
import model.entities.Occupation;

public class OccupationDaoJDBC implements OccupationDao{

	
	private Connection conn;
	
	public OccupationDaoJDBC(Connection conn) {
		this.conn = conn;
	}
	
	
	@Override
	public Occupation findById(Integer id) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT * FROM occupation WHERE Id = ?");
			st.setInt(1, id);
			rs = st.executeQuery();
			
			if(rs.next()) {
				Occupation obj = new Occupation();
				obj.setId(rs.getInt("Id"));
				obj.setName(rs.getString("Name"));
				obj.setBaseSalary(rs.getDouble("BaseSalary"));
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
	public List<Occupation> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			st = conn.prepareStatement(
					"SELECT * FROM occupation ORDER BY Name");
			rs = st.executeQuery();
			
			List<Occupation> list = new ArrayList<>();
			
			while(rs.next()) {
				Occupation obj = new Occupation();
				obj.setId(rs.getInt("Id"));
				obj.setName(rs.getString("Name"));
				obj.setBaseSalary(rs.getDouble("BaseSalary"));
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
	public void insert(Occupation obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
					"INSERT INTO occupation " +
			        "(Name , BaseSalary)" +
					"VALUES " +
			        "(? , ?)", Statement.RETURN_GENERATED_KEYS);
			st.setString(1, obj.getName());
			st.setDouble(2, obj.getBaseSalary());
			
			int rowsAffected = st.executeUpdate();
			
			if (rowsAffected > 0 ) {
				ResultSet rs = st.getGeneratedKeys();
				if(rs.next()) {
					int id = rs.getInt(1);
					obj.setId(id);
				}
			}
			else {
				throw new DbException("Unexpected error! No rows affected!");
			}
			
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
		}
		
	}

	@Override
	public void update(Occupation obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
				"UPDATE occupation " +
				"SET Name = ? , BaseSalary = ? " +
				"WHERE Id = ?");

			st.setString(1, obj.getName());
			st.setDouble(2, obj.getBaseSalary());
			st.setInt(3, obj.getId());

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
				"DELETE FROM occupation WHERE Id = ?");

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

	

	
	
	


