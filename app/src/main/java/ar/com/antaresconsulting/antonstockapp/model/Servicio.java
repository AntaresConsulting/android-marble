package ar.com.antaresconsulting.antonstockapp.model;

public class Servicio extends BaseProduct {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7884778785076871238L;

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return this.getNombre()+" unidad "+this.getUom()[1];
	}

	
}
