package ar.com.antaresconsulting.antonstockapp;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class MoveProducts extends ActionBarActivity {

	protected boolean isScanSearch = false;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_out_products);
		getActionBar().setDisplayHomeAsUpEnabled(true);

		
		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction().add(R.id.container, ProductTypeFragment.newInstance(true,true,false,true)).commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.move_products, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == android.R.id.home) {
			NavUtils.navigateUpTo(this, new Intent(this,AntonLauncherActivity.class));
			return true;
		}		
		return super.onOptionsItemSelected(item);
	}
	
	public void closeIncome(MenuItem view) {
		((MoveProductActions) getFragmentManager().findFragmentById(R.id.container)).closeMoves();	
	}
	public void scanProduct(View view) {
		IntentIntegrator scanIntegrator = new IntentIntegrator(this);
		scanIntegrator.initiateScan();
		isScanSearch=true;

	}
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
		if (scanningResult != null) {
			String scanContent = scanningResult.getContents();
			String scanFormat = scanningResult.getFormatName();
			((MoveProductActions) getFragmentManager().findFragmentById(R.id.container)).searchByEAN(scanContent);
		}else{
			Toast toast = Toast.makeText(getApplicationContext(), 
					"No scan data received!", Toast.LENGTH_SHORT);
			toast.show();
		}
	}	
	
	
	public void popSearchMP(View view) {
		((MoveMPFragment) getFragmentManager().findFragmentById(	R.id.container)).popSearchMP(view);
	}
	public void popSearchBacha(View view) {
		((MoveBachaFragment) getFragmentManager().findFragmentById(	R.id.container)).popSearchBacha(view);
	}

	public void addProductMP(View view) {
		((MoveMPFragment) getFragmentManager().findFragmentById(	R.id.container)).addProductMP(view);
	}
	public void addProductBacha(View view) {
		((MoveBachaFragment) getFragmentManager().findFragmentById(	R.id.container)).addProductBacha(view);
	}

	public void checkDispoPlacas(View view) {
		((MoveMPFragment) getFragmentManager().findFragmentById(	R.id.container)).checkDispoPlacas(view);
	}
	
	public void setBachas(View view) {
		getFragmentManager().beginTransaction().replace(R.id.container, MoveBachaFragment.newInstance(AntonConstants.BACHAS)).commit();
	}
	public void setMP(View view) {
		getFragmentManager().beginTransaction().replace(R.id.container, MoveMPFragment.newInstance(AntonConstants.MATERIA_PRIMA)).commit();
	}
	public void setServicios(View view) {
		getFragmentManager().beginTransaction().replace(R.id.container, MoveServicioFragment.newInstance(AntonConstants.SERVICIOS)).commit();
	}	
}
