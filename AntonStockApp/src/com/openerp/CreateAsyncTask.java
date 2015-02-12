package com.openerp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;
import ar.com.antaresconsulting.antonstockapp.R;
import ar.com.antaresconsulting.antonstockapp.model.dao.ProductDAO.ProductCallbacks;

import java.util.HashMap;

import com.xmlrpc.XMLRPCException;


public class CreateAsyncTask extends AsyncTask<HashMap<String, Object>, String, Long> {
	private Activity activity;
	public ProgressDialog dialog;
	private OpenErpConnect oc;
	HashMap<String, String> context;
	private String  strError;

	
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
		try {
			HashMap<String, Object> defaultRes;
			if(values.length > 1){
				String[] defaults = (String[]) values[1].get("defaults");
				defaultRes =  (HashMap<String, Object>) oc.call(OpenErpHolder.getInstance().getmModelName(), "default_get",new Object[]{ defaults});
				for (int i = 0; i < defaults.length; i++) {
					values[0].put(defaults[i], defaultRes.get(defaults[i]));
				}
			}
			return oc.create(OpenErpHolder.getInstance().getmModelName(), values[0], this.context);
		} catch (XMLRPCException e) {
			strError = e.getMessage();
			return null;
		}

	}

	@Override
	protected void onPostExecute(Long result) {
		if(result != null){
            activity.setResult(Activity.RESULT_OK);
			((CreateAsyncTaskCallbacks) this.activity).setResultCreate(result);
		}
		else{
            Toast tt = Toast.makeText(this.activity.getApplicationContext(), "Ha ocurrido un error vuelva a intentarlo!"+strError, Toast.LENGTH_LONG);
            tt.show();
            activity.finish();				
		}
		if (dialog.isShowing()) {
			dialog.dismiss();
		}
	}



}
