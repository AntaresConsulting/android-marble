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
import ar.com.antaresconsulting.antonstockapp.model.Pedido;
import ar.com.antaresconsulting.antonstockapp.model.PickingMove;

public class CreateMovesAsyncTask extends AsyncTask<PickingMove, String, Long> {
	private Activity activity;
	public ProgressDialog dialog;
	private OpenErpConnect oc;
	private String modelStockPicking;
	
	public String getModelStockPicking() {
		return modelStockPicking;
	}

	public void setModelStockPicking(String modelStockPicking) {
		this.modelStockPicking = modelStockPicking;
	}

	HashMap<String, String> context;
	
	
	
	public CreateMovesAsyncTask(Activity act) {
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
	protected Long doInBackground(PickingMove... values) {
		loadConnection();	
		for (int i = 0; i < values.length; i++) {		
			Long header_picking_id = oc.create(this.modelStockPicking, values[i].getHeaderPicking(), this.context);
			List<HashMap<String, Object>> moves = values[i].getMoves();
			for (int j = 0; j < moves.size(); j++) {
				moves.get(j).put("picking_id", header_picking_id);	
				Dimension dim = (Dimension) moves.get(j).get("dimension");
				moves.get(j).remove("dimension");
				Object[] conditions = null;				
				if(dim != null){
						conditions = new Object[] {new Object[] {AntonConstants.DIMENSION_WIDTH, "=",dim.getDimW()},new Object[] {AntonConstants.DIMENSION_HIGHT, "=", dim.getDimH()},new Object[] {AntonConstants.DIMENSION_THICKNESS, "=", dim.getDimT()}};			
						Long[] res = oc.search("product.marble.dimension",conditions);
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
							moves.get(j).put("dimension_id", idDim);
						}else{
							moves.get(j).put("dimension_id", res[0]);
						}
//						if(values[i].isRegBalanace()){
//							HashMap<String, Object> vals3 = new HashMap<String, Object>();
//							vals3.put("prod_id", (Integer) moves.get(j).get("product_id"));
//							vals3.put("dim_id",(Long) moves.get(j).get("dimension_id"));
//							vals3.put("dimension_qty",(Integer) moves.get(j).get("dimension_qty"));
//							vals3.put("dimension_m2",(Double) moves.get(j).get("product_qty"));										
//							vals3.put("typeMove",moves.get(j).get("balance_type"));			
//							oc.call("product.marble.dimension.balance", "register_balance", vals3);		
//							moves.get(j).remove("balance_type");
//						}
				}
				Long idM = oc.create("stock.move", moves.get(j), this.context);
									
			}
			ArrayList<Long> vals = new ArrayList<Long>();
			vals.add(header_picking_id);
			oc.call(this.modelStockPicking, "draft_force_assign", vals);
			if(values[i].isConfirm()){
				ArrayList<Long> vals2 = new ArrayList<Long>();
				vals2.add(header_picking_id);
				oc.call(this.modelStockPicking, "action_move", vals);
				oc.call(this.modelStockPicking, "action_done", vals2);
			}			
			if(values[i].getMoveType().equalsIgnoreCase(AntonConstants.INTERNAL_PORDUCT_TYPE)){
				oc.call(this.modelStockPicking, "action_move", vals);
				oc.call(this.modelStockPicking, "action_done", vals);
				Pedido ped = values[0].getPedidoOut();
				if(ped != null){
//					HashMap<String, Object> vals2 = new HashMap<String, Object>();
//					vals2.put("state","assigned");				
//					oc.write("stock.picking.out", new Long[]{new Long(ped.getId().longValue())}, vals2, null);
					for (int j = 0; j < ped.getLineas().length; j++) {
						Integer idl = (Integer) ped.getLineas()[j];
						HashMap<String, Object> vals3 = new HashMap<String, Object>();
						ArrayList<Long> vals5 = new ArrayList<Long>();
						vals5.add(new Long(idl.longValue()));
//						vals3.put("state","assigned");
//						oc.write("stock.move", new Long[]{new Long(idl.longValue())}, vals3, null);
						oc.call("stock.move", "force_assign",vals5 );

						
					}
				}
				
			}
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
