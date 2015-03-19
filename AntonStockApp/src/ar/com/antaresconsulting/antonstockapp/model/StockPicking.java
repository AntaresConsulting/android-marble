package ar.com.antaresconsulting.antonstockapp.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ar.com.antaresconsulting.antonstockapp.util.AntonConstants;

public class StockPicking {
	
	private String origin;
	private String type;
	private Integer partnerId;
	private String prodType;
	private boolean actionDone = false;
	private List<StockMove> moves;



	
	public StockPicking(String origin, String type, Integer partnerId) {
		super();
		this.origin = origin;
		this.type = type;
		this.partnerId = partnerId;
	}


	public StockPicking(String origin, String type, Integer partnerId,
			String prodType) {
		super();
		this.origin = origin;
		this.type = type;
		this.partnerId = partnerId;
		this.prodType = prodType;
	}

	
	public String getProdType() {
		return prodType;
	}


	public void setProdType(String prodType) {
		this.prodType = prodType;
	}


	public Integer getPartnerId() {
		return partnerId;
	}

	public void setPartnerId(Integer partnerId) {
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
	
	
	
	public boolean isActionDone() {
		return actionDone;
	}


	public void setActionDone(boolean actionDone) {
		this.actionDone = actionDone;
	}


	public HashMap<String, Object> getMap(){
		HashMap<String, Object> res = new HashMap<String, Object>();
		res.put("picking_type_id", Integer.parseInt(this.getType()));
		res.put("company_id",AntonConstants.ANTON_COMPANY_ID);
		res.put("move_type",AntonConstants.DIRECT_METHOD);
		res.put("state","draft");
		res.put("origin",origin);
		if(this.getProdType() != null)
			res.put("move_prod_type",this.getProdType());
		res.put("partner_id",this.getPartnerId());
		return res;
	}

	public void setPickingId(Long header_picking_id) {
		for (StockMove move : this.moves) {
			move.setPickingId(new Integer(header_picking_id.intValue()));
			move.setPickingTypeId(new Integer(this.getType()));
		}
		
	}

}
