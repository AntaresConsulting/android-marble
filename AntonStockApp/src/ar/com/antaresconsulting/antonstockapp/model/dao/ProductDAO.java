package ar.com.antaresconsulting.antonstockapp.model.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.app.Fragment;
import ar.com.antaresconsulting.antonstockapp.model.BaseProduct;
import ar.com.antaresconsulting.antonstockapp.model.SelectionObject;
import ar.com.antaresconsulting.antonstockapp.model.Servicio;
import ar.com.antaresconsulting.antonstockapp.util.AntonConstants;

import com.openerp.ReadAsyncTask;

public class ProductDAO extends ReadAsyncTask {

	private static final int PRODUCT_DETAIL = 5;
	private static final int PRODUCT_SELECTION = 6;
	private static final int UOMS = 7;
	private static final int LOCATION = 8;

	private String[] baseFields = new String[] { "id", "name", "image_medium", "image", "code", "list_price", "qty_available", "ean13", "uom_id" ,"attrs_material","virtual_available","seller_qty","product_tmpl_id" };

	private int dataToSet;
	private Fragment activityPart;

	public interface ProductsLocCallbacks {
		void setProducts();
	}	

	public interface UomsCallbacks {
		void setUoms();

	}
	public interface ServiciosCallbacks {
		void setServicios();
	}
	
	public interface ProductCallbacks {

		void setProductOthers();

		void setProductsSelection();

		void setProductDetail();
	}
	public ProductDAO(Activity frag) {
		super(frag);
		this.mModel = "product.product";
	}
	public ProductDAO(Fragment frag) {
		super(frag);
		this.activityPart = frag;
		this.mModel = "product.product";
	}

	public void getProductDetail(String id) {
		this.setmFilters(new Object[] { new Object[] { "id", "=", id } });
		this.execute(this.baseFields);	
		this.dataToSet = ProductDAO.PRODUCT_DETAIL;
	}

	public void getProductByEAN(String ean13) {
		this.setmFilters(new Object[] { new Object[] { "ean13", "=", ean13 } });
		this.execute(this.baseFields);
		this.dataToSet = ProductDAO.PRODUCT_SELECTION;
	}

	public void getServicios() {
		this.setmFilters(new Object[] { new Object[] { AntonConstants.PRODUCT_TYPE, "ilike",AntonConstants.CATEGORY_SERVICIO } });
		this.execute(this.baseFields);
		this.dataToSet = AntonConstants.SERVICIOS;
	}

	public void getServicios(String nomServ) {
		this.setmFilters(new Object[] { new Object[] { AntonConstants.PRODUCT_TYPE, "ilike",AntonConstants.CATEGORY_SERVICIO },new Object[] { "name", "ilike",nomServ } });
		this.execute(this.baseFields);
		this.dataToSet = AntonConstants.SERVICIOS;
	}
	
	protected void getProductsArray(HashMap<String, Object> registro,
			BaseProduct prod) {

		String imageResp = registro.get("image_medium") instanceof Boolean ? "": (String) registro.get("image_medium");
		String imageBig = registro.get("image") instanceof Boolean ? "": (String) registro.get("image");

		Double price = ((registro.get("list_price") instanceof Boolean) ? 0: (Double) registro.get("list_price"));
		String code = registro.get("code") instanceof Boolean ? "": (String) registro.get("code");
		String ean13 = registro.get("ean13") instanceof Boolean ? "": (String) registro.get("ean13");
		Object[] uom = registro.get("uom_id") instanceof Boolean ? new Object[0]: (Object[]) registro.get("uom_id");
		Double cantReal = registro.get("qty_available") instanceof Boolean ? 0: (Double) registro.get("qty_available");
		Object[] templId = registro.get("product_tmpl_id") instanceof Boolean ? new Object[0]: (Object[]) registro.get("product_tmpl_id");
		Double cantidadForecast = registro.get("virtual_available") instanceof Boolean ? 0: (Double) registro.get("virtual_available");
		Double cantIncome = registro.get("seller_qty") instanceof Boolean ? 0: (Double) registro.get("seller_qty");
		String nombre = registro.get("name") instanceof Boolean ? "": (String) registro.get("name");
		String descrAtribs = registro.get("attrs_material") instanceof Boolean ? "": (String) registro.get("attrs_material");

		prod.setAtributos(descrAtribs);
		prod.setId((Integer) registro.get("id"));
		prod.setCantidadReal(cantReal);
		prod.setCantidadForecast(cantidadForecast);
		prod.setCantidadIncome(cantIncome);
		prod.setCodigo(code);
		prod.setEan13(ean13);
		prod.setNombre(nombre);
		prod.setPrice(price);
		prod.setProductImg(imageResp);
		prod.setProductBig(imageBig);
		prod.setUom(uom);
		prod.setTemplateId((Integer) templId[0]);
	}

	public List<BaseProduct> getBaseProductsList() {
		List<BaseProduct> datosProds = new ArrayList<BaseProduct>();
		for (HashMap<String, Object> obj : this.mData) {
			BaseProduct resp = new BaseProduct();
			this.getProductsArray(obj, resp);

			datosProds.add(resp);
		}
		return datosProds;
	}
	public List<Servicio> getServiciosList() {
		List<Servicio> datosProds = new ArrayList<Servicio>();
		for (HashMap<String, Object> obj : this.mData) {
			Servicio resp = new Servicio();
			this.getProductsArray(obj, resp);

			datosProds.add(resp);
		}
		return datosProds;
	}

	@Override
	public void dataFetched() {
		switch (this.dataToSet) {
		case AntonConstants.SERVICIOS:
			if(this.activityPart == null)
				((ServiciosCallbacks) this.activity).setServicios();
			else
				((ServiciosCallbacks) this.activityPart).setServicios();
			break;
		case ProductDAO.PRODUCT_DETAIL:
			((ProductCallbacks) this.activityPart).setProductDetail();
			break;
		case ProductDAO.PRODUCT_SELECTION:
			((ProductCallbacks) this.activityPart).setProductsSelection();
			break;
		case ProductDAO.UOMS:
			((UomsCallbacks) this.activityPart).setUoms();
			break;	
		case ProductDAO.LOCATION:
			((ProductsLocCallbacks) this.activity).setProducts();
			break;				
		default:
			break;
		}
	}

	public void getUOMS() {
		this.mModel = AntonConstants.UOM_MODEL;
		this.setmFilters(new Object[0]);
		this.execute(new String[] { "id", "name"});
		this.dataToSet = ProductDAO.UOMS;
	}

	public SelectionObject[] getUomList() {
		SelectionObject[] datosProds = new SelectionObject[this.mData.size()];
		int i = 0;
		for (HashMap<String, Object> obj : this.mData) {

			Integer iduom = obj.get("id") instanceof Boolean ? new Integer(0): (Integer) obj.get("id");
			String nameuom = obj.get("name") instanceof Boolean ? "": (String) obj.get("name");

			datosProds[i++] = new SelectionObject(iduom.toString(),nameuom);
		}
		return datosProds;
	}

	public void getProductsToSubmit() {
		this.customSearchMethod = "get_prod_by_location";
		this.isCustomSearch = true;
		this.setmFilters(new Object[] { new Object[] {Integer.parseInt(AntonConstants.PRODUCT_LOCATION_OUTPUT) } });
		this.execute(this.baseFields);
		this.dataToSet = ProductDAO.LOCATION;			
	}
	
	public void getServiciosNames() {
		this.setmFilters(new Object[] { new Object[] { AntonConstants.PRODUCT_TYPE, "ilike",AntonConstants.CATEGORY_SERVICIO } });
		this.execute(new String[] { "id", "name"});
		this.dataToSet = AntonConstants.SERVICIOS;
		
	}	
	public List<String> getBaseProductsNameList() {
		List<String> datosProds = new ArrayList<String>();
		for (HashMap<String, Object> obj : this.mData) {
			String nombre = obj.get("name") instanceof Boolean ? "": (String) obj.get("name");

			datosProds.add(nombre);
		}
		return datosProds;

	}	
}
