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

public class ConfirmMovesAsyncTask extends AsyncTask<PedidoLinea, String, Long> {
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

	public ConfirmMovesAsyncTask(Activity act) {
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
			if((pl.getDimension()!= null)&&(pl.getDimension().length > 0)){
				Dimension dim = (Dimension) pl.getDimension()[0];
				Object[] conditions = new Object[] {new Object[] {AntonConstants.DIMENSION_WIDTH, "=", dim.getDimW()},new Object[] {AntonConstants.DIMENSION_HIGHT, "=", dim.getDimH()},new Object[] {AntonConstants.DIMENSION_THICKNESS, "=", dim.getDimT()}};				
				Long[] res = oc.search("product.marble.dimension",conditions);
				Long dimId;
				if((res == null) || (res.length == 0)){				
					HashMap<String, Object> dimVals = new HashMap<String, Object>();
					dimVals.put(AntonConstants.DIMENSION_HIGHT, dim.getDimH());
					dimVals.put(AntonConstants.DIMENSION_WIDTH, dim.getDimW());
					dimVals.put(AntonConstants.DIMENSION_THICKNESS, dim.getDimT());
					dimVals.put(AntonConstants.DIMENSION_TYPE, dim.getDimTipo().getId());
					Long idDim = oc.create("product.marble.dimension", dimVals, this.context);
					ArrayList<Long> vals2 = new ArrayList<Long>();
					vals2.add(idDim);
					oc.call("product.marble.dimension", "action_confirm", vals2);
					dimId = idDim;
				}else{
					dimId = res[0];
				}				
				HashMap<String, Object> moveUp = new HashMap<String, Object>();
				moveUp.put("dimension_id", dimId);
				moveUp.put("dimension_qty", pl.getCantDim());				
				oc.write("stock.move", new Long[]{new Long(pl.getId().longValue())}, moveUp, null);
				
				HashMap<String, Object> vals3 = new HashMap<String, Object>();
				vals3.put("prod_id", pl.getProduct()[0]);
				vals3.put("dim_id",dimId);
				vals3.put("dimension_qty",pl.getCantDim());
				vals3.put("dimension_m2",pl.getCant());				
				vals3.put("typeMove",this.moveType);			
				oc.call("product.marble.dimension.balance", "register_balance", vals3);						
			}
			ArrayList<Integer> vals = new ArrayList<Integer>();
			vals.add(pl.getId());
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
