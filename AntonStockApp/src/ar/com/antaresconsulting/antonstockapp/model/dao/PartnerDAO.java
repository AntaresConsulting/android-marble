package ar.com.antaresconsulting.antonstockapp.model.dao;

import java.util.HashMap;

import android.app.Activity;
import android.app.Fragment;
import ar.com.antaresconsulting.antonstockapp.model.Partner;

import com.openerp.ReadAsyncTask;

public class PartnerDAO extends ReadAsyncTask{
	private static final int PARTNER_CLIENTS = 1;
	private static final int PARTNER_SUPPLIERS = 2;
	private static final int PARTNER_CLIENT_DETAIL = 3;
	private int dataToSet;
	private Fragment activityPart;
	
	public interface ClientsCallbacks{
		void setClients();
		void setClientDetail();
	}
	public interface SuppliersCallbacks{
		void setSuppliers();
	}	
		
	public PartnerDAO(Fragment frag){
		super(frag);
		this.activityPart = frag;
		this.setmModel("res.partner");
	}
	
	public PartnerDAO(Activity act){
		super(act);
		this.setmModel("res.partner");
	}
	
	public Partner[] getPartnersArray(){
		
		Partner[] datosProds = new Partner[this.mData.size()];
		int i = 0 ;
		for (HashMap<String, Object> obj : this.mData) {
			String company =  obj.get("parent_id") instanceof Boolean  ?"":(String)(((Object[])obj.get("parent_id"))[1]);
			String street = obj.get("street") instanceof Boolean?"":(String)obj.get("street");
			String imageResp = obj.get("image_medium") instanceof Boolean?"":(String)obj.get("image_medium");
			String web = obj.get("website") instanceof Boolean?"":(String)obj.get("website");
			String phone = obj.get("phone") instanceof Boolean?"":(String)obj.get("phone");
			String email = obj.get("email") instanceof Boolean?"":(String)obj.get("email");
			String cuit = obj.get("vat") instanceof Boolean?"":(String)obj.get("vat");
			// For each field
			datosProds[i++] = new Partner((Integer)obj.get("id"),(String)obj.get("name"),street,web,email,phone,imageResp,cuit);
		}	
		return datosProds;
	}
	
	public void getAllCompanies(){
		this.setmFilters(new Object[] {new Object[] {"customer", "=", true}});
		this.execute(new String[]{"id","name","image_medium","street","parent_id","phone","website","email","vat"});
		this.dataToSet=PartnerDAO.PARTNER_CLIENTS;
		
	}
	public void getAllCompanies(String filter){
		this.setmFilters(new Object[] {new Object[] {"customer", "=", true},new Object[] {"name", "ilike", filter+"%"}});
		this.execute(new String[]{"id","name","image_medium","street","parent_id","phone","website","email","vat"});
		this.dataToSet=PartnerDAO.PARTNER_CLIENTS;
		
	}	
	public void getAllSuppliers(){
		this.setmFilters(new Object[] {new Object[] {"supplier", "=", true}});
		this.execute(new String[]{"id","name","image_medium","street","parent_id","phone","website","email","vat"});
		this.dataToSet=PartnerDAO.PARTNER_SUPPLIERS;
		
	}
	public void getClientDetail(String id){
		this.setmFilters(new Object[] {new Object[] {"id", "=", id}});
		this.execute(new String[]{"id","name","image_medium","street","parent_id","phone","website","email","vat"});
		this.dataToSet=PartnerDAO.PARTNER_CLIENT_DETAIL;		
	}	
	
	@Override
	public void dataFetched() {
		switch (this.dataToSet) {
		case PartnerDAO.PARTNER_CLIENTS:
			if(this.activityPart != null)
				((ClientsCallbacks)this.activityPart).setClients();
			else
				((ClientsCallbacks)this.activity).setClients();
			break;
		case PartnerDAO.PARTNER_SUPPLIERS:
			((SuppliersCallbacks)this.activityPart).setSuppliers();
			break;
		case PartnerDAO.PARTNER_CLIENT_DETAIL:
			((ClientsCallbacks)this.activityPart).setClientDetail();
			break;			
		default:
			break;
		}
	}

	public String[] getSuppliersArray() {
		String[] suppliers = new String[this.mData.size()];
		int i = 0 ;
		for (HashMap<String, Object> obj : this.mData) {
			suppliers[i++] = (String)obj.get("name");
		}	
		return suppliers;
	}
	
}
