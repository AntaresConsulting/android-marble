package ar.com.antaresconsulting.antonstockapp.model.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.app.Fragment;
import ar.com.antaresconsulting.antonstockapp.AntonConstants;
import ar.com.antaresconsulting.antonstockapp.model.Bacha;
import ar.com.antaresconsulting.antonstockapp.model.BaseProduct;
import ar.com.antaresconsulting.antonstockapp.model.Insumo;
import ar.com.antaresconsulting.antonstockapp.model.MateriaPrima;
import ar.com.antaresconsulting.antonstockapp.model.MateriaPrimaOut;
import ar.com.antaresconsulting.antonstockapp.model.SelectionObject;

import com.openerp.ReadAsyncTask;

public class BachasDAO extends ProductDAO {

	private String[] bachFields = new String[] { "id", "name", "image_medium","image",  "code", "list_price", "qty_available", "virtual_available","seller_qty","ean13", "uom_id","attrs_material" ,"bacha_marca", "bacha_material", "bacha_tipo", "bacha_acero", "bacha_colocacion", "bacha_ancho", "bacha_largo", "bacha_prof","product_tmpl_id"};
	
	private Fragment activityPart;
	
	public interface BachasCallbacks {
		void setBachas();
	}
	
	public BachasDAO(Activity frag) {
		super(frag);
		this.mModel = "product.product";
	}
	
	public BachasDAO(Fragment frag) {
		super(frag);
		this.activityPart = frag;
		this.mModel = "product.product";
	}


	public void getProductDetail(String id) {
		this.setmFilters(new Object[] { new Object[] { "id", "=", id } });
		this.execute(this.bachFields);	
	}

	public void getBachas() {
		this.setmFilters(new Object[] { new Object[] { AntonConstants.PRODUCT_TYPE, "ilike",AntonConstants.CATEGORY_BACHA } });
		this.execute(this.bachFields);
	}
	
	public void getBachasNames() {
		this.setmFilters(new Object[] { new Object[] { AntonConstants.PRODUCT_TYPE, "ilike",AntonConstants.CATEGORY_BACHA  } });
		this.execute( new String[] { "id", "name"});
	}

	
	public List<Bacha> getBachasList() {
		List<Bacha> datosProds = new ArrayList<Bacha>();
		for (HashMap<String, Object> obj : this.mData) {
			Bacha resp = new Bacha();
			this.getProductsArray(obj, resp);
			String marca = obj.get("bacha_marca") instanceof Boolean ? "": (String) obj.get("bacha_marca");
			String material = obj.get("bacha_material") instanceof Boolean ? "": (String) obj.get("bacha_material");
			String bacha_acero = obj.get("bacha_acero") instanceof Boolean ? "": (String) obj.get("bacha_acero");
			String bacha_tipo = obj.get("bacha_tipo") instanceof Boolean ? "": (String) obj.get("bacha_tipo");
			String bacha_colocacion = obj.get("bacha_colocacion") instanceof Boolean ? "": (String) obj.get("bacha_colocacion");
			Double bacha_prof = obj.get("bacha_prof") instanceof Boolean ? new Double(0): (Double) obj.get("bacha_prof");
			Double bacha_ancho = obj.get("bacha_ancho") instanceof Boolean ? new Double(0): (Double) obj.get("bacha_ancho");
			Double bacha_largo = obj.get("bacha_largo") instanceof Boolean ? new Double(0): (Double) obj.get("bacha_largo");
			resp.setTipoMaterial(SelectionObject.getBachaMaterialById(material));
			if(material.equalsIgnoreCase(AntonConstants.ACERO))
				resp.setMarca(SelectionObject.getBachaMarcaAceroById(marca));
			else
				resp.setMarca(SelectionObject.getBachaMarcaLosaById(marca));
	
			resp.setAcero(SelectionObject.getBachaAceroById(bacha_acero));
			resp.setColocacion(SelectionObject.getBachaColocaiconById(bacha_colocacion));
			resp.setTipo(SelectionObject.getBachaTipoById(bacha_tipo));
			resp.setAncho(bacha_ancho);
			resp.setLargo(bacha_largo);
			resp.setProfundidad(bacha_prof);
			datosProds.add(resp);
		}
		return datosProds;

	}
	
	public List<String> getBachasNamesList() {
		List<String> datosProds = new ArrayList<String>();
		for (HashMap<String, Object> obj : this.mData) {
			String nombre = obj.get("name") instanceof Boolean ? "": (String) obj.get("name");
			datosProds.add(nombre);
		}
		return datosProds;
	}
	
	@Override
	public void dataFetched() {
		if(this.activityPart == null)
			((BachasCallbacks) getActivity()).setBachas();
		else
			((BachasCallbacks) this.activityPart).setBachas();
	}
	
	public void getBachasProducts(String tipo, String marca, String nombreProd,String tipoBacha,String acero, String colocacion) {
		ArrayList<Object> filtros = new ArrayList<Object>();
		filtros.add(new Object[] { AntonConstants.PRODUCT_TYPE, "ilike", AntonConstants.CATEGORY_BACHA });
		if (!tipo.equalsIgnoreCase("")) {
			filtros.add(new Object[] { "bacha_material", "ilike",
					tipo.toLowerCase().substring(0, 3) });
		}
		if (!marca.equalsIgnoreCase("")) {
			filtros.add(new Object[] { "bacha_marca", "ilike",
					marca.toLowerCase().substring(0, 3) });
		}
		if (!tipoBacha.equalsIgnoreCase("")) {
			filtros.add(new Object[] { "bacha_tipo", "ilike",
					tipoBacha.toLowerCase().substring(0, 3) });
		}
		if (!colocacion.equalsIgnoreCase("")) {
			filtros.add(new Object[] { "bacha_colocacion", "ilike",
					colocacion.toLowerCase().substring(0, 3) });
		}
		if (!acero.equalsIgnoreCase("")) {
			filtros.add(new Object[] { "bacha_acero", "ilike",
					acero.toLowerCase().substring(0, 3) });
		}		
		if (!nombreProd.equalsIgnoreCase("")) {
			filtros.add(new Object[] { "name", "ilike",
					nombreProd.toLowerCase() });
		}	
		this.setmFilters(filtros.toArray());
		this.execute(this.bachFields);
	}

}
