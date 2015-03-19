package ar.com.antaresconsulting.antonstockapp.model.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.app.Fragment;
import ar.com.antaresconsulting.antonstockapp.model.Bacha;
import ar.com.antaresconsulting.antonstockapp.model.BaseProduct;
import ar.com.antaresconsulting.antonstockapp.model.Insumo;
import ar.com.antaresconsulting.antonstockapp.model.MateriaPrima;
import ar.com.antaresconsulting.antonstockapp.model.MateriaPrimaOut;
import ar.com.antaresconsulting.antonstockapp.model.SelectionObject;
import ar.com.antaresconsulting.antonstockapp.model.dao.InsumosDAO.InsumosCallbacks;
import ar.com.antaresconsulting.antonstockapp.util.AntonConstants;

import com.openerp.ReadAsyncTask;

public class MateriaPrimaDAO extends ProductDAO  {

	private String[] mpExtFields = new String[] { "id", "name", "image_medium", "image", "code", "list_price", "qty_available","virtual_available","seller_qty", "ean13", "uom_id", "attrs_material","raw_color","raw_finished","raw_material","product_tmpl_id" ,"stock_location_id"};
	private String[] mpFields = new String[] { "id", "name", "code", "list_price", "qty_available","virtual_available","seller_qty", "ean13", "uom_id", "attrs_material","raw_color","raw_finished","raw_material","product_tmpl_id" ,"stock_location_id"};
	
	private Fragment activityPart;

	public interface MateriaPrimaCallbacks {
		void setMateriaPrima();
	}
	
	public MateriaPrimaDAO(Activity frag) {
		super(frag);
		this.mModel = "product.product";
	}
	
	public MateriaPrimaDAO(Fragment frag) {
		super(frag);
		this.activityPart = frag;
		this.mModel = "product.product";
	}

	public void getMateriaPrima() {
		this.setmFilters(new Object[] { new Object[] { AntonConstants.PRODUCT_TYPE, "ilike","raw" } });
		this.execute(this.mpExtFields);

	}
	public void getMateriaPrimaNames() {
		this.setmFilters(new Object[] { new Object[] { AntonConstants.PRODUCT_TYPE, "ilike","raw" } });
		this.execute(new String[] { "id", "name" });

	}	

	public void getProductDetail(String id) {
		this.setmFilters(new Object[] { new Object[] { "id", "=", id } });
		this.execute(this.mpFields);	
	}

	public void getProductDetail(Long[] id) {
		this.ids = id;
		this.execute(this.mpFields);	
	}

	public void getMarmolesProducts(String tm, String color, String acabado, String nombreProd) {
		ArrayList<Object> filtros = new ArrayList<Object>();
		filtros.add(new Object[] { AntonConstants.PRODUCT_TYPE, "ilike", "raw" });
		if (!tm.equalsIgnoreCase("")) {
			filtros.add(new Object[] { "raw_material", "ilike",
					tm.toLowerCase().substring(0, 3) });
		}
		if (!color.equalsIgnoreCase("")) {
			filtros.add(new Object[] { "raw_color", "ilike",
					color.toLowerCase().substring(0, 3) });
		}
		if (!acabado.equalsIgnoreCase("")) {
			filtros.add(new Object[] { "raw_finished", "ilike",
					acabado.toLowerCase().substring(0, 3) });
		}
		if (!nombreProd.equalsIgnoreCase("")) {
			filtros.add(new Object[] { "name", "ilike",
					nombreProd.toLowerCase() });
		}		
		
		this.setmFilters(filtros.toArray());
		this.execute(this.mpFields);
	}


	public List<MateriaPrima> getMateriaPrimasList() {
		List<MateriaPrima> datosProds = new ArrayList<MateriaPrima>();
		for (HashMap<String, Object> obj : this.mData) {
			MateriaPrima resp = new MateriaPrima();
			this.getProductsArray(obj, resp);
			String material = obj.get("raw_material") instanceof Boolean ? "": (String) obj.get("raw_material");
			String color = obj.get("raw_color") instanceof Boolean ? "": (String) obj.get("raw_color");
			String finished = obj.get("raw_finished") instanceof Boolean ? "": (String) obj.get("raw_finished");
			resp.setColor(SelectionObject.getColorMPById(color));
			resp.setMaterial(SelectionObject.getMaterialMPById(material));
			resp.setFinished(SelectionObject.getFinishedMPById(finished));			
			datosProds.add(resp);
		}
		return datosProds;

	}
	
	public List<MateriaPrimaOut> getMateriaPrimasOutList() {
		List<MateriaPrimaOut> datosProds = new ArrayList<MateriaPrimaOut>();
		for (HashMap<String, Object> obj : this.mData) {
			MateriaPrimaOut resp = new MateriaPrimaOut();
			this.getProductsArray(obj, resp);
			String material = obj.get("raw_material") instanceof Boolean ? "": (String) obj.get("raw_material");
			String color = obj.get("raw_color") instanceof Boolean ? "": (String) obj.get("raw_color");
			String finished = obj.get("raw_finished") instanceof Boolean ? "": (String) obj.get("raw_finished");
			resp.setColor(SelectionObject.getColorMPById(color));
			resp.setMaterial(SelectionObject.getMaterialMPById(material));
			resp.setFinished(SelectionObject.getFinishedMPById(finished));
			datosProds.add(resp);
		}
		return datosProds;

	}

	@Override
	public void dataFetched() {
		if(this.activityPart == null)
			((MateriaPrimaCallbacks) getActivity()).setMateriaPrima();
		else				
			((MateriaPrimaCallbacks) this.activityPart).setMateriaPrima();
	}

	public void getMateriaPrimaInProduction(String loc_id) {
		this.customSearchMethod = "get_prod_by_location";
		this.isCustomSearch = true;
		this.setmFilters(new Object[] { new Object[] {Integer.parseInt(loc_id) } });
		this.execute(new String[] { "id", "name", "code", "list_price", "qty_available","virtual_available","seller_qty", "ean13", "uom_id","product_tmpl_id" });
	}

	public List<String> getMateriaPrimasNamesList() {
		List<String> datosProds = new ArrayList<String>();
		for (HashMap<String, Object> obj : this.mData) {
			String nombre = obj.get("name") instanceof Boolean ? "": (String) obj.get("name");
			datosProds.add(nombre);
		}
		return datosProds;
	}
	
}
