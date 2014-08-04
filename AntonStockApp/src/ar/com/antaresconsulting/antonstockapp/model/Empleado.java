package ar.com.antaresconsulting.antonstockapp.model;

import java.io.Serializable;


public class Empleado implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1784651458465945902L;
	private String nombre;
	private Integer id;
	private Object[] cargo;
	private Object[] departamento;
	private String imagen;
	
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Object[] getCargo() {
		return cargo;
	}
	public void setCargo(Object[] cargo) {
		this.cargo = cargo;
	}
	public Object[] getDepartamento() {
		return departamento;
	}
	public void setDepartamento(Object[] departamento) {
		this.departamento = departamento;
	}
	public String getImagen() {
		return imagen;
	}
	public void setImagen(String imagen) {
		this.imagen = imagen;
	}
	
	
	
}
