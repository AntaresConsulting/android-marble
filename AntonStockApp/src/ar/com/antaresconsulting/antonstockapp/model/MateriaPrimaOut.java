package ar.com.antaresconsulting.antonstockapp.model;

import java.util.List;

public class MateriaPrimaOut extends MateriaPrima  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2033980945838135860L;
	private List<PedidoLinea> pl;
	private DimensionBalance dim;
	private Integer cant;
	public List<PedidoLinea> getPl() {
		return pl;
	}
	public void setPl(List<PedidoLinea> pl) {
		this.pl = pl;
	}
	public DimensionBalance getDim() {
		return dim;
	}
	public void setDim(DimensionBalance dim) {
		this.dim = dim;
	}
	public Integer getCant() {
		return cant;
	}
	public void setCant(Integer cant) {
		this.cant = cant;
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return this.getDim().toString();
	}
	
	
	
}
