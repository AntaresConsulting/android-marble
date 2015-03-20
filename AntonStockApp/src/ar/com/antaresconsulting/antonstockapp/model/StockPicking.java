package ar.com.antaresconsulting.antonstockapp.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.R.integer;
import ar.com.antaresconsulting.antonstockapp.util.AntonConstants;
import ar.com.antaresconsulting.antonstockapp.util.DateUtil;

public class StockPicking implements Serializable{
	
	private static final long serialVersionUID = -8332442030304208826L;
	private Integer id;
	private String origin;
	private String type;
	private Object[] partner;	
	private String prodType;
	private boolean actionDone = false;
	private String arrivalDate;	
	private String name;
	private String note;	
	private List<StockMove> moves;
	private Integer customerLocation;


	
	public StockPicking(String origin, String type, Object[] partner) {
		super();
		this.origin = origin;
		this.type = type;
		this.partner = partner;
	}


	public StockPicking(String origin, String type, Object[] partner,
			String prodType) {
		super();
		this.origin = origin;
		this.type = type;
		this.partner = partner;
		this.prodType = prodType;
	}

	
	public StockPicking() {
		// TODO Auto-generated constructor stub
	}


	public String getProdType() {
		return prodType;
	}


	public void setProdType(String prodType) {
		this.prodType = prodType;
	}




	public Integer getCustomerLocation() {
		return customerLocation;
	}


	public void setCustomerLocation(Integer customerLocation) {
		this.customerLocation = customerLocation;
	}


	public Object[] getPartner() {
		return partner;
	}


	public void setPartner(Object[] partner) {
		this.partner = partner;
	}


	public String getArrivalDate() {
		return arrivalDate;
	}


	public void setArrivalDate(String arrivalDate) {
		this.arrivalDate = arrivalDate;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
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
	public void setMoves(Object[] moves) {
		List<StockMove> moresLst = new ArrayList<StockMove>();
		for (Object moveId : moves) {
			moresLst.add(new StockMove((Integer)moveId));
		}
		this.moves = moresLst;
	}

	public String getNote() {
		return note;
	}


	public void setNote(String note) {
		this.note = note;
	}


	public Integer getId() {
		return id;
	}


	public void setId(Integer id) {
		this.id = id;
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
		if(this.getPartner()!= null){
			if(this.getPartner()[0] instanceof Integer)
				res.put("partner_id",this.getPartner()[0]);
			else
				res.put("partner_id",((Partner)this.getPartner()[0]).getId());
		}
		return res;
	}

	public void setPickingId(Long header_picking_id) {
		for (StockMove move : this.moves) {
			move.setPickingId(new Integer(header_picking_id.intValue()));
			move.setPickingTypeId(new Integer(this.getType()));
		}
		
	}
	
	@Override
	public String toString() {
		String empresa = "";
		String movType = "";
		
		// TODO Auto-generated method stub
		if((getProdType() != null) && !getProdType().equalsIgnoreCase(""))
			movType = " - Tipo: "+SelectionObject.getAntonTipoById(getProdType());
		
		if(getPartner() != null && getPartner().length >1 )
			empresa = " - Empresa: "+getPartner()[1];
			
		return this.getName()+empresa+movType+" - Fecha: "+DateUtil.formatDateToStr(this.arrivalDate);
			
	}

}
