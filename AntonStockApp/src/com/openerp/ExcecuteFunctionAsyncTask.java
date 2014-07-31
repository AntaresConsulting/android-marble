package com.openerp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;

import java.util.HashMap;

import ar.com.antaresconsulting.antonstockapp.R;

public class ExcecuteFunctionAsyncTask extends AsyncTask<HashMap<String, Object>, String, Object> {
	private Activity activity;
	private String methodCall;
	public ProgressDialog dialog;
	private OpenErpConnect oc;
	HashMap<String, String> context;
	
	
	public ExcecuteFunctionAsyncTask(Activity act,String method) {
		this.activity = act;
		this.context = new HashMap<String, String>();
		this.methodCall = method;
		
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
	protected Object doInBackground(HashMap<String, Object>... values) {
		loadConnection();	
		if(values[0].get("parameters")!=null){
			Object[] params = (Object[]) values[0].get("parameters");
			return oc.call(OpenErpHolder.getInstance().getmModelName(), this.methodCall, params);
		}else{
			return oc.call(OpenErpHolder.getInstance().getmModelName(), this.methodCall, values);	
		}
		
		
	}

	@Override
	protected void onPostExecute(Object result) {
		if(result != null){
            activity.setResult(Activity.RESULT_OK);
            //activity.finish();
		}
		else{
			
		}
		if (dialog.isShowing()) {
			dialog.dismiss();
		}
	}



}
