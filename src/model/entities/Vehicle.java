package model.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

import model.entities.enums.VehicleStatus;


public class Vehicle implements Serializable{

	
	private static final long serialVersionUID = 1L;
	private String chassis;
	private String model;
	private Date dateEntry;
	private Date exitDate;
	private VehicleStatus vehicleStatus;
	private String status;
	
	public Vehicle() {}

	public Vehicle(String chassis, String model, Date dateEntry, Date exitDate, VehicleStatus vehicleStatus) {
		super();
		this.chassis = chassis;
		this.model = model;
		this.dateEntry = dateEntry;
		this.exitDate = exitDate;
		this.vehicleStatus = vehicleStatus;
		
	}

	public String getChassis() {
		return chassis;
	}

	public void setChassis(String chassis) {
		this.chassis = chassis;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public Date getDateEntry() {
		return dateEntry;
	}

	public void setDateEntry(Date dateEntry) {
		this.dateEntry = dateEntry;
	}

	public Date getExitDate() {
		return exitDate;
	}

	public void setExitDate(Date exitDate) {
		this.exitDate = exitDate;
	}

	public VehicleStatus getVehicleStatus() {
		return vehicleStatus;
	}

	public void setVehicleStatus(VehicleStatus vehicleStatus) {
		this.vehicleStatus = vehicleStatus;
	}
	
	public String getStatus() {
		return status;
	}

	public String setStatus() {
		return status;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public int hashCode() {
		return Objects.hash(chassis);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Vehicle other = (Vehicle) obj;
		return Objects.equals(chassis, other.chassis);
	}

	@Override
	public String toString() {
		return "Vehicle [chassis=" + chassis + ", model=" + model + ", dateEntry=" + dateEntry + ", exitDate="
				+ exitDate + ", vehicleStatus=" + vehicleStatus + "]";
	}

	
	
	
	

}
