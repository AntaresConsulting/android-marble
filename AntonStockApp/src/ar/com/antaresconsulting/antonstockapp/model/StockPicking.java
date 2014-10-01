package ar.com.antaresconsulting.antonstockapp.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ar.com.antaresconsulting.antonstockapp.AntonConstants;

public class StockPicking {
	
	private String origin;
	private String type;
	private String partnerId;
	private String locationSrc;
	private String locationDest;
	private String prodType;
	private List<StockMove> moves;



	
	public StockPicking(String origin, String type, String partnerId,
			String locationSrc, String locationDest,String prodType) {
		super();
		this.origin = origin;
		this.type = type;
		this.partnerId = partnerId;
		this.locationSrc = locationSrc;
		this.locationDest = locationDest;
		this.prodType = prodType;
	}

	
	public String getProdType() {
		return prodType;
	}


	public void setProdType(String prodType) {
		this.prodType = prodType;
	}


	public String getPartnerId() {
		return partnerId;
	}

	public void setPartnerId(String partnerId) {
		this.partnerId = partnerId;
	}

	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
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

	public List<StockMove> getMoves() {
		return moves;
	}

	public void setMoves(List<StockMove> moves) {
		this.moves = moves;
	}

	public void addMove(StockMove move){
		if(this.moves == null)
			this.moves = new ArrayList<StockMove>();
		this.moves.add(move);
	}
	
	public HashMap<String, Object> getMap(){
		HashMap<String, Object> res = new HashMap<String, Object>();
		res.put("type", AntonConstants.IN_PORDUCT_TYPE);
		res.put("auto_picking",false);
		res.put("company_id",AntonConstants.ANTON_COMPANY_ID);
		res.put("move_type",AntonConstants.DELIVERY_METHOD);
		res.put("state","draft");
		res.put("origin",origin);
		res.put("move_prod_type",this.getProdType());
		res.put("location_id",this.getLocationSrc());
		res.put("location_dest_id",this.getLocationDest());	
		res.put("partner_ir",this.getPartnerId());
		return res;
	}

	public void setPickingId(Long header_picking_id) {
		for (StockMove move : this.moves) {
			move.setPickingId(header_picking_id.toString());
		}
		
	}

}
