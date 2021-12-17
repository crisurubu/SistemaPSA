package model.entities;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class Appointments implements Serializable{

	
	private static final long serialVersionUID = 1L;
	private Integer id;
	private String appointments;
	private Integer production_Id;
	private String status;
	private List<Appointments> appointmentsList;
	
	public Appointments() {}

	public Appointments(Integer id, String appointments, Integer production_Id, String status) {
		super();
		this.id = id;
		this.appointments = appointments;
		this.production_Id = production_Id;
		this.status = status;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getAppointments() {
		return appointments;
	}

	public void setAppointments(String appointments) {
		this.appointments = appointments;
	}

	public Integer getProduction_Id() {
		return production_Id;
	}

	public void setProduction_Id(Integer production_Id) {
		this.production_Id = production_Id;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	public List<Appointments> getAppointmentsList() {
		return appointmentsList;
	}

	public void setAppointmentsList(List<Appointments> appointmentsList) {
		this.appointmentsList = appointmentsList;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Appointments other = (Appointments) obj;
		return Objects.equals(id, other.id);
	}

	@Override
	public String toString() {
		return "\nAppointments: "+appointments+ ", Production_Id: "+production_Id+ ", Status: "+status+" ";
	}
	
	

}
