package com.openerp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.Toast;

import java.util.HashMap;
import java.util.List;

import com.xmlrpc.XMLRPCException;

import ar.com.antaresconsulting.antonstockapp.AntonConstants;
import ar.com.antaresconsulting.antonstockapp.R;
import ar.com.antaresconsulting.antonstockapp.model.StockMove;
import ar.com.antaresconsulting.antonstockapp.model.StockPicking;

public class CreatePickingAsyncTask extends AsyncTask<StockPicking, String, Long> {
	private Activity activity;
	public ProgressDialog dialog;
	private OpenErpConnect oc;
	private String  strError;

	private Long  outPicking;

	HashMap<String, String> context;
	
	
	
	public Long getOutPicking() {
		return outPicking;
	}

	public void setOutPicking(Long outPicking) {
		this.outPicking = outPicking;
	}

	public CreatePickingAsyncTask(Activity act) {
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
	protected Long doInBackground(StockPicking... values) {
		loadConnection();	
		for (int i = 0; i < values.length; i++) {		
			Long header_picking_id = null;
			try {
				header_picking_id = oc.create(AntonConstants.PICKING_MODEL, values[i].getMap(), this.context);
			} catch (XMLRPCException e) {
				strError = e.getMessage();
				return null;
			}
			values[i].setPickingId(header_picking_id);
			List<StockMove> moves = values[i].getMoves();
			for (StockMove stockMove : moves) {
				if(stockMove.hasDimension()){
					Object[] conditions = new Object[] {new Object[] {AntonConstants.DIMENSION_WIDTH, "=",stockMove.getDim().getDimW()},new Object[] {AntonConstants.DIMENSION_HIGHT, "=", stockMove.getDim().getDimH()},new Object[] {AntonConstants.DIMENSION_THICKNESS, "=", stockMove.getDim().getDimT()}};			
					Long[] res = oc.search(AntonConstants.MARBLE_DIM_MODEL,conditions);
					Long idDim = null;
					if((res == null) || (res.length == 0)){						
						try {
							idDim = oc.create(AntonConstants.MARBLE_DIM_MODEL, stockMove.getDim().getMap(), this.context);
						} catch (XMLRPCException e) {
							strError = e.getMessage();
							return null;
						}
						oc.call(AntonConstants.MARBLE_DIM_MODEL, "action_confirm", idDim);
					}else{
						idDim =  res[0];
					}
					stockMove.getDim().setDimId(new Integer(idDim.toString()));
				}
				try {
					oc.create(AntonConstants.MOVE_MODEL, stockMove.getMap(), this.context);
				} catch (XMLRPCException e) {
					strError = e.getMessage();
					return null;
				}
			}
			if(values[i].isActionDone())
				oc.call(AntonConstants.PICKING_MODEL, "action_done", header_picking_id);
			else
				oc.call(AntonConstants.PICKING_MODEL, "action_confirm", header_picking_id);
			
		}
        if(this.getOutPicking()!= null)
        	oc.call(AntonConstants.PICKING_MODEL, "action_assign", this.getOutPicking());
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
