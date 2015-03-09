package ar.com.antaresconsulting.antonstockapp.model;

import java.io.Serializable;
import java.util.Date;

import ar.com.antaresconsulting.antonstockapp.util.DateUtil;


public class Pedido implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8332442030304208826L;
	private Integer id;
	private Object[] partner;
	private String nota;
	private String tipo;
	private String tipoMov;
	private String origen;
	private String nombre;
	private String arrivalDate;
	private Object[] lineas;
	
	
	
	public String getTipoMov() {
		return tipoMov;
	}
	public void setTipoMov(String tipoMov) {
		this.tipoMov = tipoMov;
	}
	public String getArrivalDate() {
		return arrivalDate;
	}
	public void setArrivalDate(String arrivalDate) {
		this.arrivalDate = arrivalDate;
	}
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
		String empresa = "";
		String movType = "";
		
		// TODO Auto-generated method stub
		if((getTipoMov() != null) && !getTipoMov().equalsIgnoreCase(""))
			movType = " - Tipo: "+SelectionObject.getAntonTipoById(getTipoMov());
		
		if(getPartner() != null && getPartner().length >1 )
			empresa = " - Empresa: "+getPartner()[1];
			
		return this.getNombre()+empresa+movType+" - Fecha: "+DateUtil.formatDateToStr(this.arrivalDate);
			
	}
	
	

}
