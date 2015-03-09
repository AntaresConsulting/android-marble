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
	HashMap<String, String> context;
	private String  strError;

	
	
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
		Long retVal = null;
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
			
//			HashMap<String, Object> vals3 = new HashMap<String, Object>();
//			vals3.put("prod_id", values[0].get("product_id"));
//			vals3.put("dim_id",idDim);
//			vals3.put("dimension_qty",values[0].get("new_quantity"));
//			vals3.put("dimension_m2",cantM2);										
//			vals3.put("typeMove","in");			
//			oc.call("product.marble.dimension.balance", "register_balance", vals3);		
//			values[0].put("new_quantity", cantM2);
			BaseProduct prod  =  (BaseProduct) values[0].get("product");
			HashMap<String,Object> move = new HashMap<String,Object>();
			move.put(AntonConstants.STOCK_MOVE_DIM_ID,idDim);
			move.put(AntonConstants.STOCK_MOVE_DIM_QTY,values[0].get("new_quantity"));
			move.put("product_uos_qty",values[0].get("new_quantity"));
			move.put("product_id",values[0].get("product_id"));
			move.put("product_uom",prod.getUom()[0]);
			move.put("location_id",AntonConstants.PRODUCT_LOCATION_INIT);
			move.put("location_dest_id",AntonConstants.PRODUCT_LOCATION_STOCK);				
			move.put("company_id",AntonConstants.ANTON_COMPANY_ID);
			move.put("prodlot_id",false);
			move.put("tracking_id",false);
//			move.put("product_qty",prod.getCant());
			move.put("product_uos",prod.getUom()[0]);
//			move.put("type",AntonConstants.IN_PORDUCT_TYPE);
			move.put("origin","inicializacion");
			move.put("state","draft");
			move.put("name",prod.getNombre());	
			
			Long idM;
			try {
				idM = oc.create("stock.move",move, this.context);
			} catch (XMLRPCException e) {
				strError = e.getMessage();
				return null;
			}
			
			ArrayList<Long> vals2 = new ArrayList<Long>();
			vals2.add(idM);
			oc.call("stock.move", "action_done", vals2);

		}else{
			values[0].remove("dimension");
			values[0].remove("product");
			try {
				retVal = oc.create(AntonConstants.PRODUCT_CHANGE_MODEL, values[0], this.context);
			} catch (XMLRPCException e) {
				strError = e.getMessage();
				return null;
			}	
			oc.call(AntonConstants.PRODUCT_CHANGE_MODEL, "change_product_qty", retVal);			
		}
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
