package ar.com.antaresconsulting.antonstockapp.model;


public class Bacha extends BaseProduct {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1784651458465945902L;
	private SelectionObject marca;
	private SelectionObject tipoMaterial;
	private SelectionObject acero;
	private SelectionObject colocacion;
	private SelectionObject tipo;
	private Double largo;
	private Double ancho;
	private Double profundidad;
	
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

	public SelectionObject getAcero() {
		return acero;
	}

	public void setAcero(SelectionObject acero) {
		this.acero = acero;
	}

	public SelectionObject getColocacion() {
		return colocacion;
	}

	public void setColocacion(SelectionObject colocacion) {
		this.colocacion = colocacion;
	}

	public SelectionObject getTipo() {
		return tipo;
	}

	public void setTipo(SelectionObject tipo) {
		this.tipo = tipo;
	}

	public Double getLargo() {
		return largo;
	}

	public void setLargo(Double largo) {
		this.largo = largo;
	}

	public Double getAncho() {
		return ancho;
	}

	public void setAncho(Double ancho) {
		this.ancho = ancho;
	}

	public Double getProfundidad() {
		return profundidad;
	}

	public void setProfundidad(Double profundidad) {
		this.profundidad = profundidad;
	}

	
}
