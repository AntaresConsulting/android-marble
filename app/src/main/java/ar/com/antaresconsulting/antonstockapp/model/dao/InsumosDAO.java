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
import ar.com.antaresconsulting.antonstockapp.model.dao.BachasDAO.BachasCallbacks;

import com.openerp.ReadAsyncTask;

public class InsumosDAO extends ProductDAO {

	private String[] insuFilds = new String[] { "id", "name", "image_medium", "image", "code", "list_price", "qty_available","virtual_available","seller_qty", "ean13", "uom_id", "description" };
	
	private Fragment activityPart;
	
	public interface InsumosCallbacks {
		void setInsumos();
	}
	
	public InsumosDAO(Activity frag) {
		super(frag);
		this.mModel = "product.product";
	}
	public InsumosDAO(Fragment frag) {
		super(frag);
		this.activityPart = frag;
		this.mModel = "product.product";
	}

	public void getProductDetail(String id) {
		this.setmFilters(new Object[] { new Object[] { "id", "=", id } });
		this.execute(this.insuFilds);	
	}

	public void getInsumos() {
		this.setmFilters(new Object[] { new Object[] { "categ_name", "ilike",
				"input" } });
		this.execute(this.insuFilds);
	}
	
	public void getInsumosNames() {
		this.setmFilters(new Object[] { new Object[] { "categ_name", "ilike",
				"input" } });
		this.execute(new String[] { "id", "name"});
	}

	public List<Insumo> getInsumosList() {
		List<Insumo> datosProds = new ArrayList<Insumo>();
		for (HashMap<String, Object> obj : this.mData) {
			Insumo resp = new Insumo();
			this.getProductsArray(obj, resp);
			String description = obj.get("description") instanceof Boolean ? "": (String) obj.get("description");
			resp.setDescription(description);
			datosProds.add(resp);
		}
		return datosProds;

	}

	public List<String> getInsumosNamesList() {
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
			((InsumosCallbacks) getActivity()).setInsumos();
		else		
			((InsumosCallbacks) this.activityPart).setInsumos();
	}
	
	public void getInsumos(String nombreProd) {
		this.setmFilters(new Object[] { new Object[] { "categ_name", "ilike",
		"input" },new Object[] { "name", "ilike",
		nombreProd } });
		this.execute(this.insuFilds);		
	}
}
