package ar.com.antaresconsulting.antonstockapp.model;


public class Insumo extends BaseProduct{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8692454968701174067L;
	private SelectionObject marca;
	private String description;
	private Empleado entregado;
	
	public Insumo() {}
	
	
	public Insumo(BaseProduct prod) {
		this.setNombre(prod.getNombre());
		this.setCantidadReal(prod.getCantidadReal());
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

	

	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}


	public Empleado getEntregado() {
		return entregado;
	}


	public void setEntregado(Empleado entregado) {
		this.entregado = entregado;
	}

	@Override
	public String toString() {
		return this.getNombre()+" - "+this.getAtributos();
	}




	
	

}
