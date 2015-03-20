package ar.com.antaresconsulting.antonstockapp.model;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;

import ar.com.antaresconsulting.antonstockapp.util.AntonConstants;
import ar.com.antaresconsulting.antonstockapp.util.DateUtil;

public class StockMove implements Serializable, Cloneable {

	private static final long serialVersionUID = -8332442030304208826L;

	private Integer id;
	private Object[] product;
	private Integer qtytDim;
	private Double qty;
	private String name;
	private String estado;
	private String origin;
	private Object[] uom;
	private Object[] dimension;
	private boolean verificado;
	private Integer pickingId;
	private Integer pickingTypeId;
	private Integer locationSrc;
	private Integer locationDest;
	private String employee;
	private Date dateExpected;
	private Integer locationProduct;
	private boolean useClientLocation;



	
	
	public StockMove(String nameStr,Object[] product, Object[] uom, Integer locationSrc,
			Integer locationDest, String origin, Double qty,Date date) {
		super();
		this.name = nameStr;
		this.product = product;
		this.uom = uom;
		this.locationSrc = locationSrc;
		this.locationDest = locationDest;
		this.origin = origin;
		this.qty = qty;
		this.dateExpected = date;

	}
	public StockMove(String nameStr,Object[] productId, Object[] prodUOM, Integer locationSrc,
			Integer locationDest, String origin, Double qty, Object[] dimId, Integer dimQty,Date date) {
		super();
		this.name = nameStr;
		this.product = productId;
		this.uom = prodUOM;
		this.locationSrc = locationSrc;
		this.locationDest = locationDest;
		this.origin = origin;
		this.qty = qty;
		this.dimension = dimId;
		this.qtytDim = dimQty;
		this.dateExpected = date;		
	}
	
	
	
	public StockMove() {
		// TODO Auto-generated constructor stub
	}
	public StockMove(Integer moveId) {
		this.id = moveId;
	}
	
	public boolean isUseClientLocation() {
		return useClientLocation;
	}
	public void setUseClientLocation(boolean useClientLocation) {
		this.useClientLocation = useClientLocation;
	}
	public Date getDateExpected() {
		return dateExpected;
	}
	public void setDateExpected(Date dateExpected) {
		this.dateExpected = dateExpected;
	}
	public String getOrigin() {
		return origin;
	}
	public void setOrigin(String origin) {
		this.origin = origin;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Object[] getProduct() {
		return product;
	}
	public void setProduct(Object[] product) {
		this.product = product;
	}

	public Integer getLocationProduct() {
		return locationProduct;
	}
	public void setLocationProduct(Integer locationProduct) {
		this.locationProduct = locationProduct;
	}
	public Integer getQtytDim() {
		return qtytDim;
	}
	public void setQtytDim(Integer qtytDim) {
		this.qtytDim = qtytDim;
	}
	public Double getQty() {
		return qty;
	}
	public void setQty(Double qty) {
		this.qty = qty;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	public Object[] getUom() {
		return uom;
	}
	public void setUom(Object[] uom) {
		this.uom = uom;
	}
	public Object[] getDimension() {
		return dimension;
	}
	public void setDimension(Object[] dimension) {
		this.dimension = dimension;
	}
	public boolean isVerificado() {
		return verificado;
	}
	public void setVerificado(boolean verificado) {
		this.verificado = verificado;
	}
	public Integer getPickingTypeId() {
		return pickingTypeId;
	}
	public void setPickingTypeId(Integer pickingTypeId) {
		this.pickingTypeId = pickingTypeId;
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
	public Integer getLocationSrc() {
		return locationSrc;
	}
	public void setLocationSrc(Integer locationSrc) {
		this.locationSrc = locationSrc;
	}
	public Integer getLocationDest() {
		return locationDest;
	}
	public void setLocationDest(Integer locationDest) {
		this.locationDest = locationDest;
	}

	public Integer getPickingId() {
		return pickingId;
	}
	public void setPickingId(Integer pickingId) {
		this.pickingId = pickingId;
	}
	public HashMap<String, Object> getMap(){
		HashMap<String, Object> res = new HashMap<String, Object>();
		res.put("picking_id",this.getPickingId());
		res.put("picking_type_id",this.getPickingTypeId());

		res.put("name",this.getName());

		res.put("product_uos_qty",this.getQty());
		
		res.put("product_uos_qty",this.getQty());
		res.put("product_uom",this.getUom()[0]);
		if(this.getProduct()[0] instanceof Integer)
			res.put("product_id",this.getProduct()[0]);
		else
			res.put("product_id",((BaseProduct)this.getProduct()[0]).getId());
		res.put("product_uom_qty",this.getQty());
		res.put("product_uos",this.getUom()[0]);
		if(this.getDateExpected() != null)
			res.put("date_expected",DateUtil.formatDateToStr(this.getDateExpected())+" 03:10:00");

		if(this.getDimension()!=null){
			res.put("dimension_id",(Integer)this.getDimension()[0]);
			res.put("dimension_unit",this.getQtytDim());
		}

		res.put("location_id",this.getLocationSrc());
		res.put("location_dest_id",this.getLocationDest());				
		res.put("company_id",AntonConstants.ANTON_COMPANY_ID);
		//res.put("prodlot_id",false);
		//res.put("tracking_id",false);		
		res.put("origin",this.getOrigin());
		res.put("state","draft");
		res.put("use_client_location",new Boolean(this.useClientLocation));

		if(this.getEmployee() != null)
			res.put("employee_id",this.getEmployee());
		return res;
	}
	public boolean hasDimension() {
		return (this.dimension != null);
	}
	public void setDimension(Dimension dim) {
		Object[] obj = new Object[1];
		obj[0] = dim;
		this.dimension = obj;
	}
	
	@Override
	public String toString() {
		if((this.getDimension() != null)&&(this.getDimension().length > 0)){
			String dimName;
			if(this.getDimension()[0] instanceof Integer)
				dimName = (String) this.getDimension()[1]; 
			else
				dimName = ((Dimension) this.getDimension()[0]).getDisplayName();
			return ((String)this.getProduct()[1])+" "+this.getQty()+" "+(String)this.getUom()[1]+" --> "+this.getQtytDim()+" x "+dimName;
		}
		return ((String)this.getProduct()[1])+" cantidad "+this.getQty()+" "+(String)this.getUom()[1];
	}
}
