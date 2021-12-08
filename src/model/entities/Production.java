package model.entities;

import java.io.Serializable;
import java.util.Objects;

public class Production implements Serializable {

	
	private static final long serialVersionUID = 1L;
	private Integer id;
	private Integer vehicle_Id;
	
	public Production() {}

	public Production(Integer id, Integer vehicle_Id) {
		super();
		this.id = id;
		this.vehicle_Id = vehicle_Id;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getVehicle_Id() {
		return vehicle_Id;
	}

	public void setVehicle_Id(Integer vehicle_Id) {
		this.vehicle_Id = vehicle_Id;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
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
		Production other = (Production) obj;
		return Objects.equals(id, other.id);
	}

	@Override
	public String toString() {
		return "Production [id=" + id + ", vehicle_Id=" + vehicle_Id + "]";
	}
	
	

}
