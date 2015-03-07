package ar.com.antaresconsulting.antonstockapp.model;

import java.io.Serializable;


public class PedidoLinea implements Serializable, Cloneable  {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8332442030304208826L;
	private Integer id;
	private Object[] product;
	private Integer cantDim;
	private Double cant;
	private String nombre;
	private String estado;
	private Object[] uom;
	private Object[] dimension;
	private boolean verificado;


	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	public Object[] getDimension() {
		return dimension;
	}
	public void setDimension(Object[] dimension) {
		this.dimension = dimension;
	}
	public void setDimension(Dimension dimension) {
		this.dimension = new Object[1];
		this.dimension[0] = dimension;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Object[] getProduct() {
		return product;
	}
	public void setProduct(Object[] product) {
		this.product = product;
	}
	public Integer getCantDim() {
		return cantDim;
	}
	public void setCantDim(Integer cantDim) {
		this.cantDim = cantDim;
	}
	public Double getCant() {
		return cant;
	}
	public void setCant(Double cant) {
		this.cant = cant;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public Object[] getUom() {
		return uom;
	}
	public void setUom(Object[] uom) {
		this.uom = uom;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}


	public boolean isVerificado() {
		return verificado;
	}

	public void setVerificado(boolean verificado) {
		this.verificado = verificado;
	}

	@Override
	public String toString() {
		if((this.getDimension() != null)&&(this.getDimension().length > 0)){
			String dimName;
			if(this.getDimension()[0] instanceof Integer)
				dimName = (String) this.getDimension()[1]; 
			else
				dimName = ((Dimension) this.getDimension()[0]).getDisplayName();
			return ((String)this.getProduct()[1])+" "+this.getCant()+" "+(String)this.getUom()[1]+" --> "+this.getCantDim()+" x "+dimName;
		}
		return ((String)this.getProduct()[1])+" cantidad "+this.getCant()+" "+(String)this.getUom()[1];
	}




}
