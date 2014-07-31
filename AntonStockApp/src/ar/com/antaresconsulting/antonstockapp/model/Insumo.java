package ar.com.antaresconsulting.antonstockapp.model;


public class Insumo extends BaseProduct{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8692454968701174067L;
	private SelectionObject marca;
	
	public Insumo() {}
	
	
	public Insumo(BaseProduct prod) {
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


	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return this.getNombre();
	}
	
	

}
