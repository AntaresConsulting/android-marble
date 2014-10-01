package ar.com.antaresconsulting.antonstockapp.model;

import java.util.HashMap;

import ar.com.antaresconsulting.antonstockapp.AntonConstants;

public class StockMove {

	private String pickingId;
	private String productId;
	private String prodUOM;
	private String locationSrc;
	private String locationDest;
	private String origin;
	private String qty;
	private Dimension dim;
	
	
	
	public StockMove(String productId, String prodUOM, String locationSrc,
			String locationDest, String origin, String qty) {
		super();
		this.productId = productId;
		this.prodUOM = prodUOM;
		this.locationSrc = locationSrc;
		this.locationDest = locationDest;
		this.origin = origin;
		this.qty = qty;
	}
	public StockMove(String productId, String prodUOM, String locationSrc,
			String locationDest, String origin, String qty, Dimension dimId) {
		super();
		this.productId = productId;
		this.prodUOM = prodUOM;
		this.locationSrc = locationSrc;
		this.locationDest = locationDest;
		this.origin = origin;
		this.qty = qty;
		this.dim = dimId;
	}
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	public String getProdUOM() {
		return prodUOM;
	}
	public void setProdUOM(String prodUOM) {
		this.prodUOM = prodUOM;
	}
	public String getLocationSrc() {
		return locationSrc;
	}
	public void setLocationSrc(String locationSrc) {
		this.locationSrc = locationSrc;
	}
	public String getLocationDest() {
		return locationDest;
	}
	public void setLocationDest(String locationDest) {
		this.locationDest = locationDest;
	}
	public String getOrigin() {
		return origin;
	}
	public void setOrigin(String origin) {
		this.origin = origin;
	}
	public String getQty() {
		return qty;
	}
	public void setQty(String qty) {
		this.qty = qty;
	}
	public Dimension getDim() {
		return dim;
	}
	public void setDim(Dimension dimId) {
		this.dim = dimId;
	}
	
	public String getPickingId() {
		return pickingId;
	}
	public void setPickingId(String pickingId) {
		this.pickingId = pickingId;
	}
	public HashMap<String, Object> getMap(){
		HashMap<String, Object> res = new HashMap<String, Object>();
		res.put("picking_id",this.getPickingId());
		
		res.put("product_uos_qty",this.getQty());
		res.put("product_uom",this.getProdUOM());
		res.put("product_id",this.getProductId());
		res.put("product_qty",this.getQty());
		res.put("product_uos",this.getProdUOM());
		
		res.put("dimension_id",this.getDim().getDimId());

		res.put("location_id",this.getLocationSrc());
		res.put("location_dest_id",this.getLocationDest());				
		res.put("company_id",AntonConstants.ANTON_COMPANY_ID);
		res.put("prodlot_id",false);
		res.put("tracking_id",false);
		res.put("type",AntonConstants.IN_PORDUCT_TYPE);
		res.put("origin",this.getOrigin());
		res.put("state","draft");
		return res;
	}
	public boolean hasDimension() {
		return (this.dim != null);
	}
}
