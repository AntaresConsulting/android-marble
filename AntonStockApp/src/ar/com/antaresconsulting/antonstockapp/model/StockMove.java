package ar.com.antaresconsulting.antonstockapp.model;

import java.util.HashMap;

import ar.com.antaresconsulting.antonstockapp.AntonConstants;

public class StockMove {

	private String name;
	private String pickingId;
	private Integer productId;
	private Integer prodUOM;
	private String locationSrc;
	private String locationDest;
	private String origin;
	private Double qty;
	private String employee;
	private Dimension dim;
	private Integer dimQty;
	
	
	
	public StockMove(String nameStr,Integer productId, Integer prodUOM, String locationSrc,
			String locationDest, String origin, Double qty) {
		super();
		this.name = nameStr;
		this.productId = productId;
		this.prodUOM = prodUOM;
		this.locationSrc = locationSrc;
		this.locationDest = locationDest;
		this.origin = origin;
		this.qty = qty;
	}
	public StockMove(String nameStr,Integer productId, Integer prodUOM, String locationSrc,
			String locationDest, String origin, Double qty, Dimension dimId, Integer dimQty) {
		super();
		this.name = nameStr;
		this.productId = productId;
		this.prodUOM = prodUOM;
		this.locationSrc = locationSrc;
		this.locationDest = locationDest;
		this.origin = origin;
		this.qty = qty;
		this.dim = dimId;
		this.dimQty = dimQty;
	}
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmployee() {
		return employee;
	}
	public void setEmployee(String employee) {
		this.employee = employee;
	}
	public Integer getProductId() {
		return productId;
	}
	public void setProductId(Integer productId) {
		this.productId = productId;
	}
	public Integer getProdUOM() {
		return prodUOM;
	}
	public void setProdUOM(Integer prodUOM) {
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
	public Double getQty() {
		return qty;
	}
	public void setQty(Double qty) {
		this.qty = qty;
	}
	public Dimension getDim() {
		return dim;
	}
	public void setDim(Dimension dimId) {
		this.dim = dimId;
	}
	
	public Integer getDimQty() {
		return dimQty;
	}
	public void setDimQty(Integer dimQty) {
		this.dimQty = dimQty;
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

		res.put("name",this.getName());

		res.put("product_uos_qty",this.getQty());
		
		res.put("product_uos_qty",this.getQty());
		res.put("product_uom",this.getProdUOM());
		res.put("product_id",this.getProductId());
		res.put("product_uom_qty",this.getQty());
		res.put("product_uos",this.getProdUOM());
		if(this.getDim()!=null){
			res.put("dimension_id",new Integer(this.getDim().getDimId()));
			res.put("dimension_unit",this.getDimQty());
		}

		res.put("location_id",this.getLocationSrc());
		res.put("location_dest_id",this.getLocationDest());				
		res.put("company_id",AntonConstants.ANTON_COMPANY_ID);
		res.put("prodlot_id",false);
		res.put("tracking_id",false);		
		res.put("origin",this.getOrigin());
		res.put("state","draft");
		if(this.getEmployee() != null)
			res.put("employee",this.getEmployee());
		return res;
	}
	public boolean hasDimension() {
		return (this.dim != null);
	}
}
