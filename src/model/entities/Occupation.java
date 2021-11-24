package model.entities;

import java.io.Serializable;
import java.util.Objects;

public class Occupation implements Serializable {
	
	
	private static final long serialVersionUID = 1L;
	private Integer id;
	private String name;
	private Double baseSalary;
	
	public Occupation() {}

	public Occupation(Integer id, String name, Double baseSalary) {
		super();
		this.id = id;
		this.name = name;
		this.baseSalary = baseSalary;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Double getBaseSalary() {
		return baseSalary;
	}

	public void setBaseSalary(Double baseSalary) {
		this.baseSalary = baseSalary;
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
		Occupation other = (Occupation) obj;
		return Objects.equals(id, other.id);
	}

	@Override
	public String toString() {
		return "Occupation [id=" + id + ", name=" + name + ", baseSalary=" + baseSalary + "]";
	}

	
	
	

}
