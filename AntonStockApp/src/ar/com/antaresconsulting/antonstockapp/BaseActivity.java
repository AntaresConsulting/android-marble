package ar.com.antaresconsulting.antonstockapp;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class BaseActivity extends Activity {

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.common, menu);
		return true;
	}

	public void userInfo(MenuItem v) {
		Toast.makeText(getApplicationContext(), "this is my Toast message!!! =)",
				   Toast.LENGTH_LONG).show();
	}

	public void exitApp(MenuItem v) {
		SharedPreferences sharedPref = this.getSharedPreferences(getString(R.string.preference_setting), Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPref.edit();
		editor.remove(getString(R.string.credential_user)).commit();
		editor.remove(getString(R.string.credential_pass)).commit();
		finish();	
	}
}
