package ar.com.antaresconsulting.antonstockapp;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

public class AntonStockApp extends Application{

    private static Context context;

    public void onCreate(){
        super.onCreate();
        AntonStockApp.context = getApplicationContext();
    }

    public static Context getAppContext() {
        return AntonStockApp.context;
    }
    
	public static Integer getExternalId(String value){
		SharedPreferences prefs = getAppContext().getSharedPreferences(getAppContext().getString(R.string.saved_external_ids),Context.MODE_PRIVATE);
		return new Integer(prefs.getInt(value, 0));
	}
}