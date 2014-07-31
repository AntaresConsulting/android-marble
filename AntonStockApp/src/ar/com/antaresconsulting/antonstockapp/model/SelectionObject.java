package ar.com.antaresconsulting.antonstockapp.model;

import java.io.Serializable;

import ar.com.antaresconsulting.antonstockapp.AntonStockApp;
import ar.com.antaresconsulting.antonstockapp.R;

public class SelectionObject  implements Serializable  {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4349295860326589358L;
	private String id;
	private String name;
	private static SelectionObject[] marcaData = {new SelectionObject("",""),new SelectionObject("mip","Mi Pileta"),new SelectionObject("joh","Johnson Acero")};
	private static SelectionObject[] tipoBachaData = {new SelectionObject("",""),new SelectionObject("los","Losa"),new SelectionObject("ace","Acero")};
	private static SelectionObject[] dimTipoData = {new SelectionObject("pla","Placa"),new SelectionObject("lef","Recorte"),new SelectionObject("mar","Marmeta")};

	
	
	public SelectionObject(String id, String name) {
		this.id = id;
		this.name = name;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return this.name;
	}

	

	public static SelectionObject[] getDimTipoData() {
		return dimTipoData;
	}
	public static void setDimTipoData(SelectionObject[] dimTipoData) {
		SelectionObject.dimTipoData = dimTipoData;
	}
	public static SelectionObject[] getMarcaData() {
		return marcaData;
	}
	
	public static void setMarcaData(SelectionObject[] marcaData) {
		marcaData = marcaData;
	}
	
	public static SelectionObject[] getTipoBachaData() {
		return tipoBachaData;
	}
	
	public static void setTipoBachaData(SelectionObject[] tipoBachaData) {
		tipoBachaData = tipoBachaData;
	}
	
	public static SelectionObject getObject(String valor, SelectionObject[] datos) {
		for (int i = 0; i < datos.length; i++) {
			if(datos[i].getName().equalsIgnoreCase(valor))
				return datos[i];
		}
		return null;
	}
	public static SelectionObject getObjectById(String valor, SelectionObject[] datos) {
		for (int i = 0; i < datos.length; i++) {
			if(datos[i].getId().equalsIgnoreCase(valor))
				return datos[i];
		}
		return null;
	}	
	public static SelectionObject getBachaMarca(String marca) {
		// TODO Auto-generated method stub
		return getObject(marca,marcaData);
	}
	public static SelectionObject getBachaMaterial(String tipoMaterialVal) {
		// TODO Auto-generated method stub
		return getObject(tipoMaterialVal,tipoBachaData);
	}
	public static SelectionObject getBachaMarcaById(String marca) {
		// TODO Auto-generated method stub
		return getObjectById(marca,marcaData);
	}
	public static SelectionObject getBachaMaterialById(String tipoMaterialVal) {
		// TODO Auto-generated method stub
		return getObjectById(tipoMaterialVal,tipoBachaData);
	}	
	public static SelectionObject getDimTipo(String tipoDim) {
		// TODO Auto-generated method stub
		return getObject(tipoDim,dimTipoData);
	}
	public static SelectionObject getDimTipoById(String tipoDim) {
		// TODO Auto-generated method stub
		return getObjectById(tipoDim,dimTipoData);
	}
	public static SelectionObject getColorMPById(String color) {
		String[] colors = AntonStockApp.getAppContext().getResources().getStringArray(R.array.color); 	
		for (String materialVar : colors) {
			if((materialVar.length() > 2) && materialVar.substring(0, 3).equalsIgnoreCase(color)){
				return new SelectionObject(color, materialVar);
			}
		}
		return null;
	}
	public static SelectionObject getMaterialMPById(String material) {
		String[] tmats = AntonStockApp.getAppContext().getResources().getStringArray(R.array.tipoMaterial);
		for (String materialVar : tmats) {
			if((materialVar.length() > 2) && materialVar.substring(0, 3).equalsIgnoreCase(material)){
				return new SelectionObject(material, materialVar);
			}
		}
		return null;
	}
	public static SelectionObject getFinishedMPById(String finished) {
		String[] terms = AntonStockApp.getAppContext().getResources().getStringArray(R.array.terminaciones);
		for (String materialVar : terms) {
			if((materialVar.length() > 2) && materialVar.substring(0, 3).equalsIgnoreCase(finished)){
				return new SelectionObject(finished, materialVar);
			}
		}
		return null;
	}	
	public static int getIndexInList(SelectionObject[] vals,String id) {
		for (int i = 0; i < vals.length; i++) {
			if(vals[i].getId().equalsIgnoreCase(id))
				return i;
		}
		return -1;
	}
	public static int getIndexInArray(String[] vals,String value) {
		for (int i = 0; i < vals.length; i++) {
			if(vals[i].equalsIgnoreCase(value))
				return i;
		}
		return -1;
	}
}
