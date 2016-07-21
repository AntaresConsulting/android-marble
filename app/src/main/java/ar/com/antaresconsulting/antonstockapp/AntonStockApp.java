package ar.com.antaresconsulting.antonstockapp;

import android.app.Application;
import android.content.Context;

public class AntonStockApp extends Application{

    private static Context context;

    public void onCreate(){
        super.onCreate();
        AntonStockApp.context = getApplicationContext();
    }

    public static Context getAppContext() {
        return AntonStockApp.context;
    }
}