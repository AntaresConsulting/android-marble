package ar.com.antaresconsulting.antonstockapp.model;

import java.io.Serializable;
import java.util.HashMap;


public class DimensionBalance implements Serializable  {

	private Object[] prodId;
	private Object[] dimId;
	private Double qtyM2;
	private Double qtyUnits;
	private Dimension dim;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1784651458465945902L;

	
	public DimensionBalance() {}



	
	public Dimension getDim() {
		return dim;
	}




	public void setDim(Dimension dim) {
		this.dim = dim;
	}




	public Object[] getProdId() {
		return prodId;
	}


	public void setProdId(Object[] prodId) {
		this.prodId = prodId;
	}


	public Object[] getDimId() {
		return dimId;
	}


	public void setDimId(Object[] dimId) {
		this.dimId = dimId;
	}


	public Double getQtyM2() {
		return qtyM2;
	}


	public void setQtyM2(Double qtyM2) {
		this.qtyM2 = qtyM2;
	}


	public Double getQtyUnits() {
		return qtyUnits;
	}


	public void setQtyUnits(Double qtyUnits) {
		this.qtyUnits = qtyUnits;
	}


	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return this.dimId[1]+" - Cant: "+this.qtyUnits+" - Total: "+this.qtyM2+" m2";
	}
	

	
}
