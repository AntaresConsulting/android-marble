package com.openerp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.xmlrpc.XMLRPCException;

import ar.com.antaresconsulting.antonstockapp.R;
import ar.com.antaresconsulting.antonstockapp.model.Dimension;
import ar.com.antaresconsulting.antonstockapp.model.StockMove;
import ar.com.antaresconsulting.antonstockapp.model.StockPicking;
import ar.com.antaresconsulting.antonstockapp.util.AntonConstants;

public class ConfirmMovesAsyncTask extends AsyncTask<StockPicking, String, Long> {
	private Activity activity;
	public ProgressDialog dialog;
	private OpenErpConnect oc;
	private String modelStockPicking;
	private String moveType;
	HashMap<String, Object> context;
	private String  strError;


	public String getModelStockPicking() {
		return modelStockPicking;
	}

	public void setModelStockPicking(String modelStockPicking) {
		this.modelStockPicking = modelStockPicking;
	}

	
	
	
	public String getMoveType() {
		return moveType;
	}

	public void setMoveType(String moveType) {
		this.moveType = moveType;
	}

	public ConfirmMovesAsyncTask(Activity act) {
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
	protected Long doInBackground(StockPicking... values) {
		loadConnection();	
		Integer pickingId = values[0].getId();
		Integer resId = (Integer) oc.call(this.modelStockPicking, "do_enter_transfer_details_marble", new Object[]{new Long[]{new Long(pickingId)}});
		
		Object[] conditions = new Object[] {new Object[] {"transfer_id", "=", resId}};
		Long[] transfers = oc.search("stock.transfer_details_items",conditions);
		List<StockMove> pls = values[0].getMoves();
		for (int i = 0; i < transfers.length; i++) {
			HashMap<String, Object> newVals = new HashMap<String, Object>();
			HashMap<String, Object> qty = new HashMap<String, Object>();
			Integer uid = new Integer(1);
			Double qtyProd = new Double(pls.get(i).getQty());
			
			newVals.put("item_ids", new Object[]{uid,transfers[i],qty.put("quantity", qtyProd)});
			oc.write("stock.transfer_details", new Long[]{new Long(values[0].getId())}, newVals, this.context);
				
		}
		
		Object resp = oc.call("stock.transfer_details", "do_detailed_transfer", new Object[]{new Long[]{new Long(resId)}});
		
		return new Long(1);
	}

	@Override
	protected void onPostExecute(Long result) {
		if(result != null){
            activity.setResult(Activity.RESULT_OK);
            Toast tt = Toast.makeText(this.activity.getApplicationContext(), "Se han registrado los movimientos en forma exitosa!", Toast.LENGTH_SHORT);
            tt.show();
            activity.finish();
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
