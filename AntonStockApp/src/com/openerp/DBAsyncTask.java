package com.openerp;

import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import ar.com.antaresconsulting.antonstockapp.AntonConstants;
import ar.com.antaresconsulting.antonstockapp.LoginActivityInterface;
import ar.com.antaresconsulting.antonstockapp.R;

public class DBAsyncTask extends
		AsyncTask<String, Integer, Object[]> {
	public ProgressDialog dialog;
	private Activity activity;
	private OpenErpConnect oc;

	public DBAsyncTask(Activity act) {
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
	protected Object[] doInBackground(String... params) {
		loadConnection();
		Object[] res = OpenErpConnect.list_db(params[0], Integer.parseInt(params[1]));
		return res;
	}

	// This is called each time you call publishProgress()
	 @Override
	protected void onProgressUpdate(Integer... progress) {
         
     }
	 

	// This is called when doInBackground() is finished
	@Override
	protected void onPostExecute(Object[] result) {

		super.onPostExecute(null);
		if (result != null) {
			Log.d(this.getClass().getName(), "Connected");
			OpenErpHolder.getInstance().setmOConn(this.oc);
            ((LoginActivityInterface) activity).dbList(result);
		} else {
			Log.d(this.getClass().getName(), "Failed connection");
		}
		if (this.dialog.isShowing()) {
			this.dialog.dismiss();
		}
	}
}