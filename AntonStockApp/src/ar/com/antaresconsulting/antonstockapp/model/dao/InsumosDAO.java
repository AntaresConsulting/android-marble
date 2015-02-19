package ar.com.antaresconsulting.antonstockapp.model.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
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

	private String[] insuFilds = new String[] { "id", "name", "image_medium", "image", "code", "list_price", "qty_available","virtual_available","seller_qty", "ean13", "uom_id", "description","attribute_value_ids","product_tmpl_id" };
	
	private Fragment activityPart;
	
	public interface InsumosCallbacks {
		void setInsumos();
	}
	
	public InsumosDAO(Activity frag) {
		super(frag);
//		this.extraData =  true;
		this.tProd = "insumos";
		
		this.mModel = "product.product";
	}
	public InsumosDAO(Fragment frag) {
		super(frag);
		this.tProd = "insumos";
		
//		this.extraData =  true;

		this.activityPart = frag;
		this.mModel = "product.product";
	}

	public void getProductDetail(String id) {
		this.setmFilters(new Object[] { new Object[] { "id", "=", id } });
		this.execute(this.insuFilds);	
	}

	public void getInsumos() {
		this.setmFilters(new Object[] { new Object[] { AntonConstants.PRODUCT_TYPE, "ilike",
				AntonConstants.CATEGORY_INSUMO } });
		this.execute(this.insuFilds);
	}
	
	public void getInsumosNames() {
		this.setmFilters(new Object[] { new Object[] { AntonConstants.PRODUCT_TYPE, "ilike",
				AntonConstants.CATEGORY_INSUMO} });
		this.execute(new String[] { "id", "name", "attribute_value_ids"});
	}

	public List<Insumo> getInsumosList() {
		List<Insumo> datosProds = new ArrayList<Insumo>();
		int i = 0;
		for (HashMap<String, Object> obj : this.mData) {
			//HashMap<String, Object> extraData = this.mExtraData.get(i++);
			Insumo resp = new Insumo();
			this.getProductsArray(obj, resp);
			String description = obj.get("description") instanceof Boolean ? "": (String) obj.get("description");
			resp.setDescription(description);
			List<HashMap<String, Object>>  atribs = (List<HashMap<String, Object>>)obj.get("attribute_value_ids");
			Iterator it = atribs.iterator();
			String atributosInsu = "";
			for (Iterator iterator = atribs.iterator(); iterator.hasNext();) {
				HashMap<String, Object> atribsVals = (HashMap<String, Object>) iterator.next();
				atributosInsu += (String) atribsVals.get("display_name")+" ";
			}
			resp.setAtributos(atributosInsu);
			datosProds.add(resp);
		}
		return datosProds;

	}

	public List<String> getInsumosNamesList() {
		List<String> datosProds = new ArrayList<String>();
		int i= 0 ;
		for (HashMap<String, Object> obj : this.mData) {
			String nombre = obj.get("name") instanceof Boolean ? "": (String) obj.get("name");
			List<HashMap<String, Object>>  atribs = (List<HashMap<String, Object>>)obj.get("attribute_value_ids");
			Iterator it = atribs.iterator();
			String atributosInsu = "";
			for (Iterator iterator = atribs.iterator(); iterator.hasNext();) {
				HashMap<String, Object> atribsVals = (HashMap<String, Object>) iterator.next();
				atributosInsu += (String) atribsVals.get("display_name")+" ";
			}			
			datosProds.add(nombre+" "+atributosInsu);
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
		this.setmFilters(new Object[] { new Object[] { AntonConstants.PRODUCT_TYPE, "ilike",
				AntonConstants.CATEGORY_INSUMO },new Object[] { "name", "ilike",
		nombreProd } });
		this.execute(this.insuFilds);		
	}
}
