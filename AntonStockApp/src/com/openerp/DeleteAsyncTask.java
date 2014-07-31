package com.openerp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import java.util.HashMap;

import ar.com.antaresconsulting.antonstockapp.R;

public class DeleteAsyncTask extends
		AsyncTask<Long, String, Boolean> {
	private Activity activity;
	public ProgressDialog dialog;
	private OpenErpConnect oc;
	HashMap<String, String> context;

	public DeleteAsyncTask(Activity act) {
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

	@Override
	protected Boolean doInBackground(Long... values) {
		Boolean writeOk = false;
		loadConnection();
		writeOk = oc.unlink(OpenErpHolder.getInstance().getmModelName(), values);
		return writeOk;

	}

	@Override
	protected void onPostExecute(Boolean result) {
		if (result) {
            activity.setResult(Activity.RESULT_OK);
            Toast tt = Toast.makeText(this.activity.getApplicationContext(), "Se ha borrado el producto en forma exitosa", Toast.LENGTH_SHORT);
            tt.show();
			Intent actReload = activity.getIntent();
			activity.finish();
			activity.startActivity(actReload);
		} else {
            Toast tt = Toast.makeText(this.activity.getApplicationContext(), "No se ha podido borrar alguno de los productos por tener registros relacionados.", Toast.LENGTH_SHORT);
            tt.show();
		}
		if (dialog.isShowing()) {
			dialog.dismiss();
		}
	}

}
