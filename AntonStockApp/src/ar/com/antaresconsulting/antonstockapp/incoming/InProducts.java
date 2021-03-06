package ar.com.antaresconsulting.antonstockapp.incoming;

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
import ar.com.antaresconsulting.antonstockapp.AntonLauncherActivity;
import ar.com.antaresconsulting.antonstockapp.R;
import ar.com.antaresconsulting.antonstockapp.R.id;
import ar.com.antaresconsulting.antonstockapp.R.layout;
import ar.com.antaresconsulting.antonstockapp.R.menu;
import ar.com.antaresconsulting.antonstockapp.partner.PartnerTypeFragment;
import ar.com.antaresconsulting.antonstockapp.product.ProductTypeFragment;
import ar.com.antaresconsulting.antonstockapp.util.AntonConstants;

public class InProducts extends Activity {

	protected boolean isScanSearch = false;
	private Menu myMenu;

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
		this.myMenu = menu;
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

	public void searchPM(MenuItem menu) {
		((InBachaProductCliFragment) getFragmentManager().findFragmentById(	R.id.container)).searchProduct();		
	}

	public void addProduct(View view) {
		((InBachaProductCliFragment) getFragmentManager().findFragmentById(	R.id.container)).addProduct(view);		
	}
	
	public void setBachas(View view) {
		getFragmentManager().beginTransaction().replace(R.id.container, new PartnerTypeFragment()).commit();
	}
	public void setBachasCli(View view) {
		this.myMenu.getItem(0).setVisible(true);
		getFragmentManager().beginTransaction().replace(R.id.container, InBachaProductCliFragment.newInstance(AntonConstants.BACHAS_CLI)).commit();
	}
	public void setBachasProv(View view) {
		getFragmentManager().beginTransaction().replace(R.id.container, InBachaProductProvFragment.newInstance(AntonConstants.BACHAS_PROV)).commit();
	}
	public void setMP(View view) {
		getFragmentManager().beginTransaction().replace(R.id.container, InMPProductFragment.newInstance(AntonConstants.MATERIA_PRIMA)).commit();
	}
	public void setInsumos(View view) {
		getFragmentManager().beginTransaction().replace(R.id.container, InInsumoProductFragment.newInstance(AntonConstants.INSUMOS)).commit();
	}
	
}
