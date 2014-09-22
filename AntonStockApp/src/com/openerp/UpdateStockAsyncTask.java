package com.openerp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.HashMap;

import ar.com.antaresconsulting.antonstockapp.AntonConstants;
import ar.com.antaresconsulting.antonstockapp.R;
import ar.com.antaresconsulting.antonstockapp.model.Dimension;

public class UpdateStockAsyncTask extends AsyncTask<HashMap<String, Object>, String, Long> {
	private Activity activity;
	private String methodCall;
	public ProgressDialog dialog;
	private OpenErpConnect oc;
	HashMap<String, String> context;
	
	
	public UpdateStockAsyncTask(Activity act) {
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
		Dimension dim = (Dimension) values[0].get("dimension");
		if(dim != null){
			Object[] conditions = new Object[] {new Object[] {AntonConstants.DIMENSION_WIDTH, "=",dim.getDimW()},new Object[] {AntonConstants.DIMENSION_HIGHT, "=", dim.getDimH()},new Object[] {AntonConstants.DIMENSION_THICKNESS, "=", dim.getDimT()}};			
			Long[] res = oc.search("product.marble.dimension",conditions);
			Long idDim = null;
			Double cantM2 = null;
			int cant = (Integer) values[0].get("new_quantity");
			if((res == null) || (res.length == 0)){						
				HashMap<String, Object> dimVals = new HashMap<String, Object>();
				dimVals.put(AntonConstants.DIMENSION_HIGHT, dim.getDimH());
				dimVals.put(AntonConstants.DIMENSION_WIDTH, dim.getDimW());
				dimVals.put(AntonConstants.DIMENSION_THICKNESS, dim.getDimT());
				dimVals.put(AntonConstants.DIMENSION_TYPE, dim.getDimTipo().getId());
				idDim = oc.create("product.marble.dimension", dimVals, this.context);
				ArrayList<Long> vals2 = new ArrayList<Long>();
				vals2.add(idDim);
				oc.call("product.marble.dimension", "action_confirm", vals2);
				cantM2 = dim.getM2()*cant;
			}else{
				idDim = res[0];
			}	
			
			HashMap<String, Object> vals3 = new HashMap<String, Object>();
			vals3.put("prod_id", values[0].get("product_id"));
			vals3.put("dim_id",idDim);
			vals3.put("dimension_qty",values[0].get("new_quantity"));
			vals3.put("dimension_m2",cantM2);										
			vals3.put("typeMove","in");			
			oc.call("product.marble.dimension.balance", "register_balance", vals3);		
			values[0].put("new_quantity", cantM2);
		}
		values[0].remove("dimension");
		Long retVal = oc.create(AntonConstants.PRODUCT_CHANGE_MODEL, values[0], this.context);	
		oc.call(AntonConstants.PRODUCT_CHANGE_MODEL, "change_product_qty", retVal);	
		return retVal;
	}

	@Override
	protected void onPostExecute(Long result) {
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
