package com.openerp;

import java.util.HashMap;
import java.util.List;

/**
 * Singleton Class to manage connections and data
 */
public class OpenErpHolder {
    private static final OpenErpHolder instance = new OpenErpHolder();

	private OpenErpConnect mOConn;
	private String mModelName;
    private String[] mFieldNames;
    private List<HashMap<String,Object>> mData;
    private HashMap<String, Object> mFieldsDescrip;
    private Class mainClass;


    protected OpenErpHolder(){
    }

    public static synchronized OpenErpHolder getInstance() {
        return instance;
    }
    public Class getMainClass() {
        return mainClass;
    }

    public void setMainClass(Class lmainClass) {
        this.mainClass = lmainClass;
    }

    public enum OoType {
        BOOLEAN, INTEGER, FLOAT, CHAR, TEXT, DATE, DATETIME, BINARY, SELECTION, ONE2ONE, MANY2ONE, ONE2MANY, MANY2MANY, RELATED,
    }

    public HashMap<String, Object> getmFieldsDescrip() {
        return mFieldsDescrip;
    }

    public String[] getmFieldNames() {
        return mFieldNames;
    }

    public void setmFieldNames(String[] fieldNames) {
        mFieldNames = fieldNames;
    }
    public void setmFieldsDescrip(HashMap<String, Object> mFieldsAttributes) {
        this.mFieldsDescrip = mFieldsAttributes;
    }

    public List<HashMap<String, Object>> getmData() {
        return mData;
    }

    public void setmData(List<HashMap<String, Object>> mData) {
        this.mData = mData;
    }

    public String getmModelName() {
        return mModelName;
    }

    public void setmModelName(String mModelName) {
        this.mModelName = mModelName;
    }

    public OpenErpConnect getmOConn() {
        return mOConn;
    }

    public void setmOConn(OpenErpConnect mOConn) {
        this.mOConn = mOConn;
    }
}


