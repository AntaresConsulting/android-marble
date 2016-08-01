package ar.com.antaresconsulting.antonstockapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class BaseActivity extends AppCompatActivity {

	public void exitApp(MenuItem v) {
		SharedPreferences sharedPref = this.getSharedPreferences(getString(R.string.preference_setting), Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPref.edit();
		editor.remove(getString(R.string.credential_user)).commit();
		editor.remove(getString(R.string.credential_pass)).commit();
		finish();	
	}
	public void returnHome(MenuItem v) {
		Intent intent = new Intent(this, AntonLauncherActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);  //It is use to finish current activity
		startActivity(intent);
		this.finish();
	}
}
