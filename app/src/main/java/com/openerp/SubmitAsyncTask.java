package com.openerp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ar.com.antaresconsulting.antonstockapp.AntonConstants;
import ar.com.antaresconsulting.antonstockapp.R;
import ar.com.antaresconsulting.antonstockapp.model.Dimension;
import ar.com.antaresconsulting.antonstockapp.model.PedidoLinea;
import ar.com.antaresconsulting.antonstockapp.model.PickingMove;

public class SubmitAsyncTask extends AsyncTask<PedidoLinea, String, Long> {
	private Activity activity;
	public ProgressDialog dialog;
	private OpenErpConnect oc;
	private String modelStockPicking;
	private String moveType;
	HashMap<String, String> context;

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

	public SubmitAsyncTask(Activity act) {
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
	protected Long doInBackground(PedidoLinea... values) {
		loadConnection();	
		for (int i = 0; i < values.length; i++) {		
			PedidoLinea pl = values[i];
			ArrayList<Integer> vals = new ArrayList<Integer>();
			vals.add(pl.getId());
			//oc.call(this.modelStockPicking, "action_move", vals);
			oc.call(this.modelStockPicking, "action_done", vals);
		}
		
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
			
		}
		if (dialog.isShowing()) {
			dialog.dismiss();
		}
	}



}
