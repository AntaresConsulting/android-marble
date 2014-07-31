package com.openerp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import ar.com.antaresconsulting.antonstockapp.R;

import java.util.HashMap;
import java.util.List;

/**
 * Gets extra data needed for the FormView: many2_ lists, binary data and selection fields
 * <p/>
 * Populates binary fields
 */
public class ReadExtraAsyncTask extends AsyncTask<String, String, OpenErpConnect> {
    private final Fragment activity;
    private final HashMap<String, Object> mRecord;
    public ProgressDialog dialog;
    private final OpenErpConnect oc;
    private HashMap<String, Object> fieldAttrs;
    private HashMap<String, List<HashMap<String, Object>>> mMany2DataLists;
    private List<HashMap<String, Object>> mListBinary;
    private List<HashMap<String, Object>> mListBinaryNames;


    public ReadExtraAsyncTask(Fragment act, HashMap<String, Object> record) {
        mRecord = record;
        activity = act;
        fieldAttrs = OpenErpHolder.getInstance().getmFieldsDescrip();
        mMany2DataLists = null;
        mListBinary = null;
        oc = OpenErpHolder.getInstance().getmOConn();
        this.mMany2DataLists = new HashMap<String, List<HashMap<String, Object>>>();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        dialog = new ProgressDialog(activity.getActivity());
        dialog.setMessage(activity.getString(R.string.sConnecting) + "...");
        dialog.setIndeterminate(false);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setCancelable(false);
        dialog.show();
    }

    @Override
    protected OpenErpConnect doInBackground(String... params) {

        Long[] ids = new Long[1];
        for (String fieldname : OpenErpHolder.getInstance().getmFieldNames()) {
            String type = getFieldType(fieldname);
            if (type.equals("many2one") || type.equals("many2many")) {
                this.populateMany2OneDataLists(fieldname);
            }
            if (this.mRecord != null){
                ids[0] = Long.valueOf((Integer) mRecord.get("id"));
                if (type.equals("binary")) {
                    this.populateBinaryFields(ids, fieldname);
                    this.populateBinaryFieldNames(ids, fieldname);
                }
            }
        }

        return this.oc;
    }


    @Override
    protected void onPostExecute(OpenErpConnect result) {
       // ((ReadActivityInterface) activity).dataFetched();
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    /**
     * Populates many2one and many2many mMany2DataLists
     * String key is the field name
     * value is a list of records
     */
    private void populateMany2OneDataLists(String fieldname) {

        String[] retFields = {"id", "name"};
        String rel = getFieldRelation(fieldname);
        Long[] ids = this.oc.search(rel, new Object[0]); //Get all names from other model.
        if (ids != null) {
            List<HashMap<String, Object>> listData = oc.read(rel,
                    ids, retFields);
            mMany2DataLists.put(fieldname, listData);
        }
    }

    private void populateBinaryFields(Long[] ids, String fieldname) {
        mListBinary = this.oc.read(OpenErpHolder.getInstance().getmModelName(), ids, new String[]{fieldname});
    }

    //Receives binary field name: must search for filename attribute in field description.
    //Expects that field name to be: "binaryfieldname_name"
    private void populateBinaryFieldNames(Long[] ids, String fieldname) {
        mListBinaryNames = this.oc.read(OpenErpHolder.getInstance().getmModelName(), ids, new String[]{fieldname + "_name"});
    }


    private String getFieldRelation(String fieldname) {
        String relation = null;
        if (this.fieldAttrs != null) {
            HashMap<String, Object> fieldAttr = (HashMap<String, Object>) fieldAttrs.get(fieldname);
            relation = (String) fieldAttr.get("relation");
        }
        return relation;
    }


    private String getFieldType(String fieldname) {
        String type = null;
        if (this.fieldAttrs != null) {
            HashMap<String, Object> fieldAttr = (HashMap<String, Object>) fieldAttrs.get(fieldname);
            type = (String) fieldAttr.get("type");
        }
        return type;
    }



    public List<HashMap<String, Object>> getListBinaryNames() {
        return mListBinaryNames;
    }

    public HashMap<String, List<HashMap<String, Object>>> getMany2DataLists() {
        return mMany2DataLists;
    }

    public List<HashMap<String, Object>> getListBinary() {
        return mListBinary;
    }


}
