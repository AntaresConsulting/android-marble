package com.openerp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import com.xmlrpc.XMLRPCException;

import ar.com.antaresconsulting.antonstockapp.R;
import ar.com.antaresconsulting.antonstockapp.model.BaseProduct;
import ar.com.antaresconsulting.antonstockapp.model.Dimension;
import ar.com.antaresconsulting.antonstockapp.model.MateriaPrima;
import ar.com.antaresconsulting.antonstockapp.util.AntonConstants;

public class UpdateStockAsyncTask extends AsyncTask<HashMap<String, Object>, String, Long> {
	private Activity activity;
	private String methodCall;
	public ProgressDialog dialog;
	private OpenErpConnect oc;
	HashMap<String, Object> context;
	private String  strError;

	
	
	public UpdateStockAsyncTask(Activity act) {
		this.activity = act;
		this.context = new HashMap<String, Object>();		
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
		Long retVal = null;
		Dimension dim = (Dimension) values[0].get("dimension_id");
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
				try {
					idDim = oc.create("product.marble.dimension", dimVals, this.context);
				} catch (XMLRPCException e) {
					strError = e.getMessage();
					return null;
				}
				ArrayList<Long> vals2 = new ArrayList<Long>();
				vals2.add(idDim);
				oc.call("product.marble.dimension", "action_confirm", vals2);
				cantM2 = dim.getM2()*cant;
			}else{
				idDim = res[0];
			}	
			 values[0].put("dimension_id",idDim);
		}
		try {
			Integer[] aa = new Integer[]{(Integer)values[0].get("product_id")};
			this.context.put("active_id", aa[0]);
			this.context.put("active_ids", aa );
			retVal = oc.create(AntonConstants.PRODUCT_CHANGE_MODEL, values[0], this.context);			
		} catch (XMLRPCException e) {
			strError = e.getMessage();
			return null;
		}			
		oc.call(AntonConstants.PRODUCT_CHANGE_MODEL, AntonConstants.PRODUCT_CHANGE_ACTION, retVal);					
		return retVal;
	}

	@Override
	protected void onPostExecute(Long result) {
		if(result != null){
            activity.setResult(Activity.RESULT_OK);
            //activity.finish();
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
