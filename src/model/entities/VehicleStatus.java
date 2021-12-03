package model.entities;

import java.io.Serializable;
import java.util.Objects;

public class VehicleStatus implements Serializable{

	
	private static final long serialVersionUID = 1L;
	private Integer id;
	private String status;
	
	public VehicleStatus() {}

	public VehicleStatus(Integer id, String status) {
		super();
		this.id = id;
		this.status = status;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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
		VehicleStatus other = (VehicleStatus) obj;
		return Objects.equals(id, other.id);
	}

	@Override
	public String toString() {
		return "VehicleStatus [id=" + id + ", status=" + status + "]";
	}
	
	

}
