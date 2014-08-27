package ar.com.antaresconsulting.antonstockapp.model;

public class MateriaPrima extends BaseProduct  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2033980945838135860L;

	private SelectionObject material;
	private SelectionObject color;
	private SelectionObject finished;
	
	public MateriaPrima() {}
	
	public MateriaPrima(BaseProduct prod) {
		this.setNombre(prod.getNombre());
		this.setCantidadReal(prod.getCantidadReal());
		this.setCodigo(prod.getCodigo());
		this.setEan13(this.getEan13());
		this.setId(prod.getId());
		this.setProductImg(prod.getProductImg());
		this.setPrice(prod.getPrice());
	}
		


	public SelectionObject getMaterial() {
		return material;
	}

	public void setMaterial(SelectionObject material) {
		this.material = material;
	}

	public SelectionObject getColor() {
		return color;
	}

	public void setColor(SelectionObject color) {
		this.color = color;
	}

	public SelectionObject getFinished() {
		return finished;
	}

	public void setFinished(SelectionObject finished) {
		this.finished = finished;
	}

	
	
}
