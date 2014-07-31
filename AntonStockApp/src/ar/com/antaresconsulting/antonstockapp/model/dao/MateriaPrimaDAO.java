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
import ar.com.antaresconsulting.antonstockapp.model.dao.InsumosDAO.InsumosCallbacks;

import com.openerp.ReadAsyncTask;

public class MateriaPrimaDAO extends ProductDAO  {

	private String[] mpFields = new String[] { "id", "name", "image_medium", "code", "list_price", "qty_available", "ean13", "uom_id", "attrs_material","color","finished","material" };
	
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
		this.setmFilters(new Object[] { new Object[] { "categ_name", "ilike","raw" } });
		this.execute(this.mpFields);

	}
	public void getMateriaPrimaNames() {
		this.setmFilters(new Object[] { new Object[] { "categ_name", "ilike","raw" } });
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
		filtros.add(new Object[] { "categ_name", "ilike", "raw" });
		if (!tm.equalsIgnoreCase("")) {
			filtros.add(new Object[] { "material", "ilike",
					tm.toLowerCase().substring(0, 3) });
		}
		if (!color.equalsIgnoreCase("")) {
			filtros.add(new Object[] { "color", "ilike",
					color.toLowerCase().substring(0, 3) });
		}
		if (!acabado.equalsIgnoreCase("")) {
			filtros.add(new Object[] { "finished", "ilike",
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
			String material = obj.get("material") instanceof Boolean ? "": (String) obj.get("material");
			String color = obj.get("color") instanceof Boolean ? "": (String) obj.get("color");
			String finished = obj.get("finished") instanceof Boolean ? "": (String) obj.get("finished");
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
			String material = obj.get("material") instanceof Boolean ? "": (String) obj.get("material");
			String color = obj.get("color") instanceof Boolean ? "": (String) obj.get("color");
			String finished = obj.get("finished") instanceof Boolean ? "": (String) obj.get("finished");
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
		this.execute(this.mpFields);
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
