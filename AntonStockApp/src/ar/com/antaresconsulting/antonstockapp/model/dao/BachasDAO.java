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

	private String[] bachFields = new String[] { "id", "name", "image_medium", "code", "list_price", "qty_available", "ean13", "uom_id","attrs_material" ,"bacha_marca", "bacha_material"};
	
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
		this.setmFilters(new Object[] { new Object[] { "categ_name", "ilike","bacha" } });
		this.execute(this.bachFields);
	}
	
	public void getBachasNames() {
		this.setmFilters(new Object[] { new Object[] { "categ_name", "ilike","bacha" } });
		this.execute( new String[] { "id", "name"});
	}

	
	public List<Bacha> getBachasList() {
		List<Bacha> datosProds = new ArrayList<Bacha>();
		for (HashMap<String, Object> obj : this.mData) {
			Bacha resp = new Bacha();
			this.getProductsArray(obj, resp);
			String marca = obj.get("bacha_marca") instanceof Boolean ? "": (String) obj.get("bacha_marca");
			String material = obj.get("bacha_material") instanceof Boolean ? "": (String) obj.get("bacha_material");
			resp.setTipoMaterial(SelectionObject.getBachaMaterialById(material));
			resp.setMarca(SelectionObject.getBachaMarcaById(marca));
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
	
	public void getBachasProducts(String tipo, String marca, String nombreProd) {
		ArrayList<Object> filtros = new ArrayList<Object>();
		filtros.add(new Object[] { "categ_name", "ilike", "bacha" });
		if (!tipo.equalsIgnoreCase("")) {
			filtros.add(new Object[] { "bacha_material", "ilike",
					tipo.toLowerCase().substring(0, 3) });
		}
		if (!marca.equalsIgnoreCase("")) {
			filtros.add(new Object[] { "bacha_marca", "ilike",
					marca.toLowerCase().substring(0, 3) });
		}
		if (!nombreProd.equalsIgnoreCase("")) {
			filtros.add(new Object[] { "name", "ilike",
					nombreProd.toLowerCase() });
		}	
		this.setmFilters(filtros.toArray());
		this.execute(this.bachFields);
	}

}
