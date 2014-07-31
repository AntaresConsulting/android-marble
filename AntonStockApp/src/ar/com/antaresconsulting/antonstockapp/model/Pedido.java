package ar.com.antaresconsulting.antonstockapp.model;

import java.io.Serializable;


public class Pedido implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8332442030304208826L;
	private Integer id;
	private Object[] partner;
	private String nota;
	private String tipo;
	private String origen;
	private String nombre;
	private Object[] lineas;
	
	
	public Object[] getLineas() {
		return lineas;
	}
	public void setLineas(Object[] lineas) {
		this.lineas = lineas;
	}
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
	public Object[] getPartner() {
		return partner;
	}
	public void setPartner(Object[] partner) {
		this.partner = partner;
	}
	public String getNota() {
		return nota;
	}
	public void setNota(String nota) {
		this.nota = nota;
	}
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	public String getOrigen() {
		return origen;
	}
	public void setOrigen(String origen) {
		this.origen = origen;
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return this.getNombre();
	}
	
	

}
