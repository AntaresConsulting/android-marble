package com.openerp;

import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import ar.com.antaresconsulting.antonstockapp.LoginActivityInterface;
import ar.com.antaresconsulting.antonstockapp.R;
import ar.com.antaresconsulting.antonstockapp.util.AntonConstants;

public class ConnectAsyncTask extends
		AsyncTask<String, Integer, OpenErpConnect> {
	public ProgressDialog dialog;
	private Activity activity;
	private OpenErpConnect oc;

	public ConnectAsyncTask(Activity act) {
		this.activity = act;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		dialog = new ProgressDialog(activity);
		dialog.setMessage(activity.getString(R.string.sConnecting) + "...");
		dialog.setIndeterminate(false);
		dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		dialog.setCancelable(true);
		dialog.setCanceledOnTouchOutside(false);		
		dialog.show();
	}

	private void loadConnection() {
		this.oc = OpenErpHolder.getInstance().getmOConn();
	}

	@Override
	protected OpenErpConnect doInBackground(String... params) {
		loadConnection();
		oc = OpenErpConnect.connect(params[0], Integer.parseInt(params[1]), params[2],	params[3], params[4]);
		if(oc == null)
			return null;
		List<HashMap<String, Object>> resp = oc.read(AntonConstants.USERS_MODEL,new Long[]{oc.getmUserId().longValue()},new String[]{"groups_id"} );
		Object[] grpIds = (Object[]) resp.get(0).get("groups_id");
		Long[] grpIdsn = new Long[grpIds.length];
		int i = 0 ;
		for (Object object : grpIds) {
			grpIdsn[i++] = new Long(((Integer)object).longValue());
		}
		List<HashMap<String, Object>> respg = oc.read(AntonConstants.GROUPS_MODEL,grpIdsn,new String[]{"name"} );
		for (HashMap<String, Object> hashMap : respg) {
			String grpName = (String) hashMap.get("name");
			if(grpName.equalsIgnoreCase(AntonConstants.MARBLE_MANAGER_GROUP))
				oc.setManager(true);
			if(grpName.equalsIgnoreCase(AntonConstants.MARBLE_CUTTER_GROUP))
				oc.setCutter(true);
			if(grpName.equalsIgnoreCase(AntonConstants.MARBLE_RESP_GROUP))
				oc.setResponsable(true);			
		}
		return oc;
	}

	// This is called each time you call publishProgress()
	 @Override
	protected void onProgressUpdate(Integer... progress) {
         
     }
	 

	// This is called when doInBackground() is finished
	@Override
	protected void onPostExecute(OpenErpConnect result) {

		super.onPostExecute(null);
		if (result != null) {
			Log.d(this.getClass().getName(), "Connected");
			OpenErpHolder.getInstance().setmOConn(this.oc);
            ((LoginActivityInterface) activity).connectionResolved(true);
		} else {
			Log.d(this.getClass().getName(), "Failed connection");
            ((LoginActivityInterface) activity).connectionResolved(false);

		}
		if (this.dialog.isShowing()) {
			this.dialog.dismiss();
		}
	}
}