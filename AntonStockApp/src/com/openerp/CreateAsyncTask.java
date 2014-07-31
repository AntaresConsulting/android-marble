package com.openerp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import ar.com.antaresconsulting.antonstockapp.R;
import ar.com.antaresconsulting.antonstockapp.model.dao.ProductDAO.ProductCallbacks;

import java.util.HashMap;


public class CreateAsyncTask extends AsyncTask<HashMap<String, Object>, String, Long> {
	private Activity activity;
	public ProgressDialog dialog;
	private OpenErpConnect oc;
	HashMap<String, String> context;
	
	public interface CreateAsyncTaskCallbacks {
		void setResultCreate(Long res);

	}
	
	public CreateAsyncTask(Activity act) {
		this.activity = act;
		this.context = new HashMap<String, String>();
		
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		dialog = new ProgressDialog(activity);
		dialog.setMessage(activity.getString(R.string.sConnecting) + "...");
		dialog.setIndeterminate(false);
		dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		dialog.setCancelable(false);
		dialog.setCanceledOnTouchOutside(false);		
		dialog.show();
	}

	private void loadConnection() {
		this.oc = OpenErpHolder.getInstance().getmOConn();
	}
	

	/*
	 * Creates a record
	 */
	@Override
	protected Long doInBackground(HashMap<String, Object>... values) {
		loadConnection();	
		return oc.create(OpenErpHolder.getInstance().getmModelName(), values[0], this.context);

	}

	@Override
	protected void onPostExecute(Long result) {
		if(result != null){
            activity.setResult(Activity.RESULT_OK);
			((CreateAsyncTaskCallbacks) this.activity).setResultCreate(result);
		}
		else{
			
		}
		if (dialog.isShowing()) {
			dialog.dismiss();
		}
	}



}
