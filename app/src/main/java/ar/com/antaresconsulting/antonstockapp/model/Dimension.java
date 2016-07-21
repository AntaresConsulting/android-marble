package ar.com.antaresconsulting.antonstockapp.model;

import java.io.Serializable;


public class Dimension implements Serializable  {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2335380404419758743L;
	private String dimH;
	private String dimW;
	private String dimT;	
	private SelectionObject dimTipo;

	public SelectionObject getDimTipo() {
		return dimTipo;
	}
	public void setDimTipo(SelectionObject dimTipo) {
		this.dimTipo = dimTipo;
	}
	public String getDimH() {
		return dimH;
	}
	public void setDimH(String dimH) {
		this.dimH = dimH;
	}
	public String getDimW() {
		return dimW;
	}
	public void setDimW(String dimW) {
		this.dimW = dimW;
	}
	public String getDimT() {
		return dimT;
	}
	public void setDimT(String dimT) {
		this.dimT = dimT;
	}	
}
