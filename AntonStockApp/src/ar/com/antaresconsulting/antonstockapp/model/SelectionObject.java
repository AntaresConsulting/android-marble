package ar.com.antaresconsulting.antonstockapp.model;

import java.io.Serializable;

import ar.com.antaresconsulting.antonstockapp.AntonStockApp;
import ar.com.antaresconsulting.antonstockapp.R;
import ar.com.antaresconsulting.antonstockapp.util.AntonConstants;

public class SelectionObject  implements Serializable  {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4349295860326589358L;
	private String id;
	private String name;
	private static SelectionObject[] marcaBachaAceroData = {new SelectionObject("",""),new SelectionObject("joh","Johnson Acero"),new SelectionObject("mip","Mi Pileta"),new SelectionObject("fra","Franke"),new SelectionObject("ari","Ariel Del Plata")};
	private static SelectionObject[] marcaBachaLosaData = {new SelectionObject("",""),new SelectionObject("fer","Ferrum"),new SelectionObject("roc","Roca"),new SelectionObject("cer","Cerart"),new SelectionObject("dec","Deca Piazza")};	
	private static SelectionObject[] tipoMaterialBachaData = {new SelectionObject("",""),new SelectionObject("los","Losa"),new SelectionObject("ace","Acero")};
	private static SelectionObject[] dimTipoData = {new SelectionObject("pla","Placa"),new SelectionObject("lef","Recorte"),new SelectionObject("mar","Marmeta")};
	private static SelectionObject[] tipoBachaData = {new SelectionObject("",""),new SelectionObject("sim","Simple"),new SelectionObject("dob","Doble"),new SelectionObject("red","Redonda")};
	private static SelectionObject[] colocacionBachaData = {new SelectionObject("",""),new SelectionObject("peg","Pegado de Abajo"),new SelectionObject("enc","Encastrado")};
	private static SelectionObject[] aceroBachaData = {new SelectionObject("",""),new SelectionObject("304","304"),new SelectionObject("403","403")};	
	private static SelectionObject[] tipoPedido = {new SelectionObject("",""),new SelectionObject(AntonConstants.PICKING_TYPE_ID_IN,"Pedido"),new SelectionObject(AntonConstants.PICKING_TYPE_ID_OUT,"Remito")};	
	private static SelectionObject[] estadoPedido = {new SelectionObject("",""),new SelectionObject("draft","Creado"),new SelectionObject("assigned","Pendiente"),new SelectionObject("partially_available","Parcialmente Completo"),new SelectionObject("done","Completo")};	
	private static SelectionObject[] tipoAntonPedido = {new SelectionObject(AntonConstants.RAW_PICKING,"Materia Prima"),new SelectionObject(AntonConstants.BACHA_PICKING,"Bacha"),new SelectionObject(AntonConstants.INSU_PICKING,"Insumo")};	
	
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

	

	public static SelectionObject[] getTipoPedido() {
		return tipoPedido;
	}
	public static void setTipoPedido(SelectionObject[] tipoPedido) {
		SelectionObject.tipoPedido = tipoPedido;
	}
	public static SelectionObject[] getEstadoPedido() {
		return estadoPedido;
	}
	public static void setEstadoPedido(SelectionObject[] estadoPedido) {
		SelectionObject.estadoPedido = estadoPedido;
	}
	public static SelectionObject[] getTipoBachaData() {
		return tipoBachaData;
	}
	public static void setTipoBachaData(SelectionObject[] tipoBachaData) {
		SelectionObject.tipoBachaData = tipoBachaData;
	}
	public static SelectionObject[] getColocacionBachaData() {
		return colocacionBachaData;
	}
	public static void setColocacionBachaData(SelectionObject[] colocacionBachaData) {
		SelectionObject.colocacionBachaData = colocacionBachaData;
	}
	public static SelectionObject[] getAceroBachaData() {
		return aceroBachaData;
	}
	public static void setAceroBachaData(SelectionObject[] aceroBachaData) {
		SelectionObject.aceroBachaData = aceroBachaData;
	}
	public static SelectionObject[] getDimTipoData() {
		return dimTipoData;
	}
	public static void setDimTipoData(SelectionObject[] dimTipoData) {
		SelectionObject.dimTipoData = dimTipoData;
	}
	public static SelectionObject[] getMarcaAceroBachaData() {
		return marcaBachaAceroData;
	}
	
	public static void setMarcaAceroBachaData(SelectionObject[] marcaData) {
		marcaBachaAceroData = marcaData;
	}

	public static SelectionObject[] getMarcaLosaBachaData() {
		return marcaBachaLosaData;
	}
	
	public static void setMarcaLosaBachaData(SelectionObject[] marcaData) {
		marcaBachaLosaData = marcaData;
	}
	public static SelectionObject[] getTipoMaterialBachaData() {
		return tipoMaterialBachaData;
	}
	
	public static void setTipoMaterialBachaData(SelectionObject[] tipoBachaData) {
		tipoMaterialBachaData = tipoBachaData;
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
	public static SelectionObject getBachaMarcaAcero(String marca) {
		// TODO Auto-generated method stub
		return getObject(marca,marcaBachaAceroData);
	}
	public static SelectionObject getBachaMarcaLosa(String marca) {
		// TODO Auto-generated method stub
		return getObject(marca,marcaBachaLosaData);
	}	
	public static SelectionObject getBachaMaterial(String tipoMaterialVal) {
		// TODO Auto-generated method stub
		return getObject(tipoMaterialVal,tipoMaterialBachaData);
	}
	public static SelectionObject getBachaMarcaAceroById(String marca) {
		// TODO Auto-generated method stub
		return getObjectById(marca,marcaBachaAceroData);
	}
	public static SelectionObject getBachaMarcaLosaById(String marca) {
		// TODO Auto-generated method stub
		return getObjectById(marca,marcaBachaLosaData);
	}	
	public static SelectionObject getBachaAceroById(String acero) {
		// TODO Auto-generated method stub
		return getObjectById(acero,aceroBachaData);
	}	
	public static SelectionObject getBachaColocaiconById(String colocacion) {
		// TODO Auto-generated method stub
		return getObjectById(colocacion,colocacionBachaData);
	}
	public static SelectionObject getAntonTipoById(String tipo) {
		// TODO Auto-generated method stub
		return getObjectById(tipo,tipoAntonPedido);
	}
	public static SelectionObject getBachaTipoById(String tipo) {
		// TODO Auto-generated method stub
		return getObjectById(tipo,tipoBachaData);
	}	
	public static SelectionObject getBachaMaterialById(String tipoMaterialVal) {
		// TODO Auto-generated method stub
		return getObjectById(tipoMaterialVal,tipoMaterialBachaData);
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
