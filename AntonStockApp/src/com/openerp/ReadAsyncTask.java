package com.openerp;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import ar.com.antaresconsulting.antonstockapp.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public abstract  class  ReadAsyncTask extends AsyncTask<String, String, OpenErpConnect> {
	public ProgressDialog dialog;
	protected Activity activity;
	protected Fragment frag;

	private OpenErpConnect oc;
	protected Long[] ids;

	protected boolean extraData = false;
	protected String tProd = "";
	protected List<HashMap<String, Object>> mData;
	protected List<HashMap<String, Object>>  mExtraData;
	protected String[] mFields;
	protected Object[] mFilters;
	protected String 	 mModel;
	protected HashMap<String, Object> paramsCustom;
	protected boolean 	 isCustomSearch = false;
	protected String 	 customSearchMethod;


	public ReadAsyncTask(Fragment act) {
		this.frag = act;
		this.ids = null;
		this.mData = null;
		this.mExtraData = new ArrayList<HashMap<String,Object>>();
	}
	public ReadAsyncTask(Activity act) {
		this.activity = act;
		this.ids = null;
		this.mData = null;
		this.mExtraData = new ArrayList<HashMap<String,Object>>();
	}


	public Activity getActivity() {
		return activity;
	}



	public void setActivity(Activity activity) {
		this.activity = activity;
	}



	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		if(activity == null)
			activity = this.frag.getActivity();
		dialog = new ProgressDialog(activity);
		dialog.setMessage(activity.getString(R.string.sConnecting) + "...");
		dialog.setIndeterminate(false);
		dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		dialog.setCancelable(false);
		dialog.setCanceledOnTouchOutside(false);
		dialog.show();
	}

	/*
	 * Reads field values from model params -> mFields
	 */
	@Override
	protected OpenErpConnect doInBackground(String... params) {
		HashMap<String, Object> tempRegs = new HashMap<String, Object>();
		HashMap<String, Object> fieldsData;
		HashMap<String, Object> fieldsData2;
		this.oc = OpenErpHolder.getInstance().getmOConn();
		this.mFields = new String[params.length];
		System.arraycopy(params, 0, this.mFields, 0, params.length);

		HashMap<String, Object> respCall = null;
		if(this.ids == null){
			if(this.isCustomSearch)
				respCall = (HashMap<String, Object>) oc.call(this.mModel, this.customSearchMethod, this.mFilters);
			else
				this.ids = oc.search(this.mModel, this.mFilters);
			int k = 0;
			if(respCall != null){
				this.ids = new Long[respCall.keySet().size()];
				for (String keyVal : respCall.keySet()) {
					if(((Double)respCall.get(keyVal)).doubleValue() > 0 ){
						this.ids[k++]=new Long(keyVal);
					}
				}
			}
		}
		if (this.ids != null) {
			this.mData = oc.read(this.mModel, this.ids, this.mFields);
			if(respCall != null){
				for (HashMap<String, Object> prod : this.mData) {
					prod.put("qty_available", respCall.get(((Integer)prod.get("id")).toString()));
				}
			}
			for (HashMap<String, Object> prod : this.mData) {
				if(this.tProd.equalsIgnoreCase("insumos")){				
					Object[] ids =(Object[])prod.get("attribute_value_ids");
					Long[] idsN = new Long[ids.length];					
					for (int j = 0; j < ids.length; j++) {
						idsN[j] = new Long(((Integer) ids[j]).longValue());
					}		
					prod.put("attribute_value_ids", oc.read("product.attribute.value",idsN,new String[]{"id","display_name"}));
				}
			}			


			if(this.extraData){
				Iterator it = this.mData.iterator();
				while (it.hasNext()) {
					HashMap<String, Object> dataVal = (HashMap<String, Object>) it.next();
					if(!tempRegs.containsKey(this.mModel)){
						fieldsData= oc.fieldsGet(this.mModel);
						tempRegs.put(this.mModel, fieldsData);
					}else{
						fieldsData = (HashMap<String, Object>) tempRegs.get(this.mModel);
					}
					HashMap<String, Object> valsExtra = new HashMap<String, Object>();
					for (int i = 0; i < this.mFields.length; i++) {
						String fieldName = this.mFields[i];
						if(fieldName.contains("_id")|| fieldName.contains("_ids")){
							if(!(dataVal.get(fieldName) instanceof Boolean)){
								Object[] vals = (Object[]) dataVal.get(fieldName);
								Long [] idsN = new Long[vals.length];
								HashMap<String, Object> descField = (HashMap<String, Object>) fieldsData.get(fieldName);
								if(fieldName.contains("_ids")){
									for (int j = 0; j < vals.length; j++) {
										idsN[j] = new Long(((Integer) vals[j]).longValue());
									}									
								}else{
									idsN = new Long[1];
									idsN[0] = new Long(((Integer) vals[0]).longValue());
								}

								if(!tempRegs.containsKey((String) descField.get("relation"))){
									fieldsData2= oc.fieldsGet((String) descField.get("relation"));
									tempRegs.put(this.mModel, fieldsData);
								}else{
									fieldsData2 = (HashMap<String, Object>) tempRegs.get((String) descField.get("relation"));
								}						
								String[] newFields = new String[fieldsData2.keySet().size()];
								fieldsData2.keySet().toArray(newFields);
								List<HashMap<String, Object>> resTemp = oc.read((String) descField.get("relation"),idsN,newFields);
								valsExtra.put(fieldName, resTemp);
							}
						}
					}
					this.mExtraData.add(valsExtra);

				}
			}
		}
		return oc;
	}

	public static <T> T[] concat(T[] first, T[] second) {
		  T[] result = Arrays.copyOf(first, first.length + second.length);
		  System.arraycopy(second, 0, result, first.length, second.length);
		  return result;
	}
	
	@Override
	protected void onPostExecute(OpenErpConnect result) {
		// TODO Handle Read errors
		if (this.mData != null) {
			this.dataFetched();
		}
		if (dialog.isShowing()) {
			dialog.dismiss();
		}
	}

	public List<HashMap<String, Object>> getData() {
		return mData;
	}


	public String[] getmFields() {
		return mFields;
	}

	public void setmFields(String[] mFields) {
		mFields = mFields ;
	}

	public Object[] getmFilters() {
		return mFilters;
	}



	public void setmFilters(Object[] mFilters) {
		this.mFilters = mFilters;
	}



	public String getmModel() {
		return mModel;
	}



	public void setmModel(String mModel) {
		this.mModel = mModel;
	}

	public abstract void dataFetched();

}
