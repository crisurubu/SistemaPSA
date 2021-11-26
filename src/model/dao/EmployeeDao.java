package model.dao;

import java.util.List;

import model.entities.Occupation;
import model.entities.Employee;

public interface EmployeeDao {

	void insert(Employee obj);
	void update(Employee obj);
	void deleteById(Integer id);
	Employee findById(Integer id);
	List<Employee> findAll();
	
	List<Employee> findByOccupation(Occupation occupation);
}
