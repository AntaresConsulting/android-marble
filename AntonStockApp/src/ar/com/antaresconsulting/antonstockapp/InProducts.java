package ar.com.antaresconsulting.antonstockapp;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import android.support.v4.app.NavUtils;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class InProducts extends Activity {

	protected boolean isScanSearch = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_in_products);
		getActionBar().setDisplayHomeAsUpEnabled(true);

		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction().add(R.id.container, ProductTypeFragment.newInstance(true,true,true,false)).commit();
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.in_products, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == android.R.id.home) {
			NavUtils.navigateUpTo(this, new Intent(this,
					AntonLauncherActivity.class));
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public void closeIncome(MenuItem view) {
		((InProductActions) getFragmentManager().findFragmentById(	R.id.container)).confirmStock();
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
			((InProductActions) getFragmentManager().findFragmentById(	R.id.container)).searchByEAN(scanContent);
		}else{
			Toast toast = Toast.makeText(getApplicationContext(), 
					"No scan data received!", Toast.LENGTH_SHORT);
			toast.show();
		}
	}	

	public void addProduct(View view) {
		((InProductActions) getFragmentManager().findFragmentById(	R.id.container)).addProduct(view);
	}
	public void addProductBacha(View view) {
		((InBachaProductFragment) getFragmentManager().findFragmentById(	R.id.container)).addProduct(view);
	}
	public void addProductInsumo(View view) {
		((InInsumoProductFragment) getFragmentManager().findFragmentById(	R.id.container)).addProduct(view);
	}
	
	public void setBachas(View view) {
		getFragmentManager().beginTransaction().replace(R.id.container, InBachaProductFragment.newInstance(AntonConstants.BACHAS)).commit();
	}
	public void setMP(View view) {
		getFragmentManager().beginTransaction().replace(R.id.container, InMPProductFragment.newInstance(AntonConstants.MATERIA_PRIMA)).commit();
	}
	public void setInsumos(View view) {
		getFragmentManager().beginTransaction().replace(R.id.container, InInsumoProductFragment.newInstance(AntonConstants.INSUMOS)).commit();
	}
	
}
