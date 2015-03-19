package com.openerp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import com.xmlrpc.XMLRPCException;

import ar.com.antaresconsulting.antonstockapp.R;
import ar.com.antaresconsulting.antonstockapp.model.Dimension;
import ar.com.antaresconsulting.antonstockapp.model.Pedido;
import ar.com.antaresconsulting.antonstockapp.model.PedidoLinea;
import ar.com.antaresconsulting.antonstockapp.util.AntonConstants;

public class ConfirmMovesAsyncTask extends AsyncTask<Pedido, String, Long> {
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
	protected Long doInBackground(Pedido... values) {
		loadConnection();	
//		for (int i = 0; i < values.length; i++) {		
//			PedidoLinea pl = values[i];
//			if((pl.getDimension()!= null)&&(pl.getDimension().length > 0)){
//				Dimension dim = (Dimension) pl.getDimension()[0];
//				Object[] conditions = new Object[] {new Object[] {AntonConstants.DIMENSION_WIDTH, "=", dim.getDimW()},new Object[] {AntonConstants.DIMENSION_HIGHT, "=", dim.getDimH()},new Object[] {AntonConstants.DIMENSION_THICKNESS, "=", dim.getDimT()}};				
//				Long[] res = oc.search("product.marble.dimension",conditions);
//				Long dimId;
//				if((res == null) || (res.length == 0)){				
//					HashMap<String, Object> dimVals = new HashMap<String, Object>();
//					dimVals.put(AntonConstants.DIMENSION_HIGHT, dim.getDimH());
//					dimVals.put(AntonConstants.DIMENSION_WIDTH, dim.getDimW());
//					dimVals.put(AntonConstants.DIMENSION_THICKNESS, dim.getDimT());
//					dimVals.put(AntonConstants.DIMENSION_TYPE, dim.getDimTipo().getId());
//					Long idDim;
//					try {
//						idDim = oc.create("product.marble.dimension", dimVals, this.context);
//					} catch (XMLRPCException e) {
//						strError = e.getMessage();
//						return null;
//					}
//					ArrayList<Long> vals2 = new ArrayList<Long>();
//					vals2.add(idDim);
//					oc.call("product.marble.dimension", "action_confirm", vals2);
//					dimId = idDim;
//				}else{
//					dimId = res[0];
//				}				
//				HashMap<String, Object> moveUp = new HashMap<String, Object>();
//				moveUp.put(AntonConstants.STOCK_MOVE_DIM_ID, dimId);
//				moveUp.put(AntonConstants.STOCK_MOVE_DIM_QTY, pl.getCantDim());				
//				oc.write("stock.move", new Long[]{new Long(pl.getId().longValue())}, moveUp, null);
//				
//				HashMap<String, Object> vals3 = new HashMap<String, Object>();
//				vals3.put(AntonConstants.DIM_BALANCE_PROD_ID, pl.getProduct()[0]);
//				vals3.put(AntonConstants.DIM_BALANCE_DIM_ID,dimId);
//				vals3.put(AntonConstants.DIM_BALANCE_QTY_UNITS,pl.getCantDim());
//				vals3.put(AntonConstants.DIM_BALANCE_QTY_MT2,pl.getCant());				
//				vals3.put("typeMove",this.moveType);			
//				oc.call("product.marble.dimension.balance", "register_balance", vals3);						
//			}
//			ArrayList<Integer> vals = new ArrayList<Integer>();
//			vals.add(pl.getId());
//			oc.call(this.modelStockPicking, "action_done", vals);
//
//		}
		
		Integer pickingId = values[0].getId();
		Integer resId = (Integer) oc.call(this.modelStockPicking, "do_enter_transfer_details_marble", new Object[]{new Long[]{new Long(pickingId)}});
		
		Object[] conditions = new Object[] {new Object[] {"transfer_id", "=", resId}};
		Long[] transfers = oc.search("stock.transfer_details_items",conditions);
		PedidoLinea []pls = (PedidoLinea[]) values[0].getLineas();
		for (int i = 0; i < transfers.length; i++) {
			HashMap<String, Object> newVals = new HashMap<String, Object>();
			HashMap<String, Object> qty = new HashMap<String, Object>();
			Integer uid = new Integer(1);
			Double qtyProd = new Double(pls[i].getCant());
			
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
