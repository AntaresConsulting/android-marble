package com.openerp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import ar.com.antaresconsulting.antonstockapp.FieldsGetActivityInterface;
import ar.com.antaresconsulting.antonstockapp.R;

import java.util.HashMap;

public class FieldsGetAsyncTask extends AsyncTask<String, String, OpenErpConnect> {
	public ProgressDialog dialog;
	protected Activity activity;
	protected OpenErpConnect oc;
	HashMap<String, Object> data;
	protected String[] fields;

	public FieldsGetAsyncTask(Activity act, String[] fields2) {
		this.activity = act;
		this.fields = fields2;
		this.data = null;
	}


	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		dialog = new ProgressDialog(activity);
		dialog.setMessage(activity.getString(R.string.sConnecting) + "...");
		dialog.setIndeterminate(false);
		dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		dialog.setCancelable(false);
		dialog.show();
	}

	private void loadConnection() {
		this.oc = OpenErpHolder.getInstance().getmOConn();
	}

	/*
	 * Reads fields attributes (names, type...)
	 */
	@Override
	protected OpenErpConnect doInBackground(String... params) {
		loadConnection();
		this.data = oc.fieldsGet(OpenErpHolder.getInstance().getmModelName());
		return oc;
	}

	@Override
	protected void onPostExecute(OpenErpConnect result) {
		// TODO Handle Read errors
		if (this.data != null) {
			((FieldsGetActivityInterface) activity).fieldsFetched(data);
		}
		if (dialog.isShowing()) {
			dialog.dismiss();
		}
	}

}
