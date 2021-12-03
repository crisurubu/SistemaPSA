package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mysql.jdbc.Statement;

import db.DB;
import db.DbException;
import model.dao.EmployeeDao;
import model.entities.Employee;
import model.entities.Occupation;

public class EmployeeDaoJDBC implements EmployeeDao {

	private Connection conn;
	
	public EmployeeDaoJDBC(Connection conn) {
		this.conn = conn;
	}
	
	@Override
	public void insert(Employee obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
					"INSERT INTO employee "
					+ "(Name, Email, Celular, AdmissionDate, Department, Occupation_Id) "
					+ "VALUES "
					+ "(?, ?, ?, ?, ?, ?)",
					Statement.RETURN_GENERATED_KEYS);
			
			st.setString(1, obj.getName());
			st.setString(2, obj.getEmail());
			st.setString(3, obj.getCelular());
			st.setDate(4, new java.sql.Date(obj.getAdmissionDate().getTime()));
			st.setString(5, obj.getDepartment());
			st.setInt(6, obj.getOccupation().getId());
			
			
			
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
	public void update(Employee obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
					"UPDATE employee "
					+ "SET Name = ?, Email = ?, Celular = ?, AdmissionDate = ?, Department = ?, Occupation_Id = ? "
					+ "WHERE Id = ?");
			
			st.setString(1, obj.getName());
			st.setString(2, obj.getEmail());
			st.setString(3, obj.getCelular());
			st.setDate(4, new java.sql.Date(obj.getAdmissionDate().getTime()));
			st.setString(5, obj.getDepartment());
			st.setInt(6, obj.getOccupation().getId());
			st.setInt(7, obj.getId());
			
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
			st = conn.prepareStatement("DELETE FROM employee WHERE Id = ?");
			
			st.setInt(1, id);
			
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
	public Employee findById(Integer id) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT employee.*,occupation.Name as Name "
					+ "FROM employee INNER JOIN occupation "
					+ "ON employee.Occupation_Id = occupation.Id "
					+ "WHERE employee.Id = ?");
			
			st.setInt(1, id);
			rs = st.executeQuery();
			if (rs.next()) {
				Occupation dep = instantiateOccupation(rs);
				Employee obj = instantiateEmployee(rs, dep);
				return obj;
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

	private Employee instantiateEmployee(ResultSet rs, Occupation dep) throws SQLException {
		Employee obj = new Employee();
		obj.setId(rs.getInt("Id"));
		obj.setName(rs.getString("Name"));
		obj.setEmail(rs.getString("Email"));
		obj.setCelular(rs.getString("Celular"));
		obj.setAdmissionDate(new java.util.Date(rs.getTimestamp("AdmissionDate").getTime()));
		obj.setDepartment(rs.getString("Department"));
		obj.setOccupation(dep);
		return obj;
	}

	private Occupation instantiateOccupation(ResultSet rs) throws SQLException {
		Occupation dep = new Occupation();
		dep.setId(rs.getInt("Occupation_Id"));
		dep.setName(rs.getString("Name"));
		return dep;
	}

	@Override
	public List<Employee> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement("SELECT a.*, b.Name "
					+ "FROM employee as A "
					+ "INNER JOIN occupation as B "
					+ "on a.Occupation_Id = b.Id");
					/*"SELECT employee.*,occupation.Name "
					+ "FROM employee INNER JOIN occupation "
					+ "ON employee.Occupation_Id = occupation.Id "
					+ "ORDER BY Name");*/
			
			rs = st.executeQuery();
			
			List<Employee> list = new ArrayList<>();
			Map<Integer, Occupation> map = new HashMap<>();
			
			while (rs.next()) {
				
				Occupation dep = map.get(rs.getInt("Occupation_Id"));
				
				if (dep == null) {
					dep = instantiateOccupation(rs);
					map.put(rs.getInt("Occupation_Id"), dep);
				}
				
				Employee obj = instantiateEmployee(rs, dep);
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
	public List<Employee> findByOccupation(Occupation occupation) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT employee.*,occupation.Name as Name "
					+ "FROM employee INNER JOIN occupation "
					+ "ON employee.Occupation_Id = occupation.Id "
					+ "WHERE Occupation_Id = ? "
					+ "ORDER BY Name");
			
			st.setInt(1, occupation.getId());
			
			rs = st.executeQuery();
			
			List<Employee> list = new ArrayList<>();
			Map<Integer, Occupation> map = new HashMap<>();
			
			while (rs.next()) {
				
				Occupation dep = map.get(rs.getInt("Occupation_Id"));
				
				if (dep == null) {
					dep = instantiateOccupation(rs);
					map.put(rs.getInt("Occupation_Id"), dep);
				}
				
				Employee obj = instantiateEmployee(rs, dep);
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
}
