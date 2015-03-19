package ar.com.antaresconsulting.antonstockapp;

import java.util.HashMap;

import com.openerp.ReadAsyncTask;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import ar.com.antaresconsulting.antonstockapp.util.AntonConstants;

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
	
	public void dataSync(MenuItem v) {
		ReadAsyncTask read = new ReadAsyncTask(this) {
			
			@Override
			public void dataFetched() {
				SharedPreferences prefs = this.activity.getSharedPreferences(getString(R.string.saved_external_ids), MODE_PRIVATE);
				SharedPreferences.Editor editor = prefs.edit();
				for (HashMap<String, Object>  record : this.mData) {
					editor.putInt((String)record.get("complete_name"),((Integer)record.get("res_id")).intValue());	
				}
				editor.commit();

			}
		};
		read.setmModel(AntonConstants.IR_DATA_MODEL);
		read.setmFilters( new Object[] {new Object[] { "model", "=","stock.location"}});
		read.execute(new String[]{"res_id","complete_name"});
	}

	public void exitApp(MenuItem v) {
		SharedPreferences sharedPref = this.getSharedPreferences(getString(R.string.preference_setting), Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPref.edit();
		editor.remove(getString(R.string.credential_user)).commit();
		editor.remove(getString(R.string.credential_pass)).commit();
		finish();	
	}
}
