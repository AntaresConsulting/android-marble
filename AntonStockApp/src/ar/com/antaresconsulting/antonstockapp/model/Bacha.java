package ar.com.antaresconsulting.antonstockapp.model;


public class Bacha extends BaseProduct {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1784651458465945902L;
	private SelectionObject marca;
	private SelectionObject tipoMaterial;
	
	public Bacha() {}
	
	public Bacha(BaseProduct prod) {
		this.setNombre(prod.getNombre());
		this.setCantidad(prod.getCantidad());
		this.setCodigo(prod.getCodigo());
		this.setEan13(this.getEan13());
		this.setId(prod.getId());
		this.setProductImg(prod.getProductImg());
		this.setPrice(prod.getPrice());
	}
	public SelectionObject getMarca() {
		return marca;
	}
	public void setMarca(SelectionObject marca) {
		this.marca = marca;
	}
	public SelectionObject getTipoMaterial() {
		return tipoMaterial;
	}
	public void setTipoMaterial(SelectionObject tipoMaterial) {
		this.tipoMaterial = tipoMaterial;
	}

	
}
