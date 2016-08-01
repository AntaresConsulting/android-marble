package ar.com.antaresconsulting.antonstockapp.model.dao;

import android.app.Activity;
import android.app.Fragment;

import com.openerp.ReadAsyncTask;

import java.util.HashMap;

import ar.com.antaresconsulting.antonstockapp.model.Partner;
import ar.com.antaresconsulting.antonstockapp.model.User;

public class UserDAO extends ReadAsyncTask{
	private static final int PARTNER_CLIENTS = 1;
	private static final int PARTNER_SUPPLIERS = 2;
	private static final int PARTNER_CLIENT_DETAIL = 3;
	private int dataToSet;
	private Fragment activityPart;

	public interface UserCallbacks{
		void setUserDetail();
	}

	public UserDAO(Fragment frag){
		super(frag);
		this.activityPart = frag;
		this.setmModel("res.users");
	}

	public UserDAO(Activity act){
		super(act);
		this.setmModel("res.users");
	}
	
	public User getUserData(){
		
		User[] datosProds = new User[this.mData.size()];
		int i = 0 ;
		for (HashMap<String, Object> obj : this.mData) {
			String company =  obj.get("parent_id") instanceof Boolean  ?"":(String)(((Object[])obj.get("parent_id"))[1]);
			String imageResp = obj.get("image") instanceof Boolean?"":(String)obj.get("image");
			String birthdate = obj.get("birthdate") instanceof Boolean?"":(String)obj.get("birthdate");
			String phone = obj.get("phone") instanceof Boolean?"":(String)obj.get("phone");
			String email = obj.get("email") instanceof Boolean?"":(String)obj.get("email");
			String cel = obj.get("mobile") instanceof Boolean?"":(String)obj.get("mobile");
			// For each field
			datosProds[i++] = new User((Integer)obj.get("id"),(String)obj.get("name"),email,phone,imageResp,cel,birthdate);
		}	
		return datosProds[0];
	}

	public void getUserDetail(Integer id){
		this.setmFilters(new Object[] {new Object[] {"id", "=", id}});
		this.execute(new String[]{"id","name","image","birthdate","parent_id","phone","mobile","email"});
		this.dataToSet= UserDAO.PARTNER_CLIENT_DETAIL;
	}	
	
	@Override
	public void dataFetched() {
		((UserCallbacks) getActivity()).setUserDetail();
	}

	
}
