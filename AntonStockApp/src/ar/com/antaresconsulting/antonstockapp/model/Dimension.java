package ar.com.antaresconsulting.antonstockapp.model;

import java.io.Serializable;
import java.util.HashMap;

import ar.com.antaresconsulting.antonstockapp.AntonConstants;


public class Dimension implements Serializable  {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2335380404419758743L;
	private Integer dimId;
	private String displayName;

	private Double dimH;
	private Double dimW;
	private Double dimT;	
	private SelectionObject dimTipo;

	
	public Dimension() {
		super();
	}
	
	public Dimension(String dimH, String dimW, String dimT,
			SelectionObject dimTipo) {
		super();
		this.dimH = Double.parseDouble(dimH);
		this.dimW = Double.parseDouble(dimW);
		this.dimT = Double.parseDouble(dimT);
		this.dimTipo = dimTipo;
	}
	
	public Dimension(HashMap<String, Object> hashMap) {
		this.displayName = (String) hashMap.get("display_name");		
		this.dimH = (Double) hashMap.get("hight");
		this.dimW =  (Double) hashMap.get("width");
		this.dimT =  (Double) hashMap.get("thickness");
		this.dimTipo =  SelectionObject.getDimTipoById((String) hashMap.get("type"));
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public SelectionObject getDimTipo() {
		return dimTipo;
	}
	public void setDimTipo(SelectionObject dimTipo) {
		this.dimTipo = dimTipo;
	}
	public Double getDimH() {
		return dimH;
	}
	public void setDimH(Double dimH) {
		this.dimH = dimH;
	}
	public Double getDimW() {
		return dimW;
	}
	public void setDimW(Double dimW) {
		this.dimW = dimW;
	}
	public Double getDimT() {
		return dimT;
	}
	public void setDimT(Double dimT) {
		this.dimT = dimT;
	}	
	public double getM2(){
		return dimH.doubleValue() *dimW.doubleValue();
	}

	public Integer getDimId() {
		return dimId;
	}

	public void setDimId(Integer dimId) {
		this.dimId = dimId;
	}
	public HashMap<String, Object> getMap(){
		HashMap<String, Object> res = new HashMap<String, Object>();
		res.put(AntonConstants.DIMENSION_HIGHT, this.getDimH());
		res.put(AntonConstants.DIMENSION_WIDTH, this.getDimW());
		res.put(AntonConstants.DIMENSION_THICKNESS, this.getDimT());
		res.put(AntonConstants.DIMENSION_TYPE, this.getDimTipo().getId());
		return res;
	}	
}
