package ar.com.antaresconsulting.antonstockapp.product;

import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import ar.com.antaresconsulting.antonstockapp.AntonConstants;
import ar.com.antaresconsulting.antonstockapp.R;
import ar.com.antaresconsulting.antonstockapp.R.id;
import ar.com.antaresconsulting.antonstockapp.R.layout;

public class ProductTypeActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_product_type);
		getFragmentManager().beginTransaction().add(R.id.mainConatiner, ProductTypeFragment.newInstance(true,true,true,true)).commit();			
		
	}

	public void setBachas(View view) {
		Intent outProds = new Intent(this, ProductListActivity.class);
		Bundle bund = new Bundle();
		bund.putInt(AntonConstants.TPROD, AntonConstants.BACHAS);
		outProds.putExtras(bund);
		startActivity(outProds);
	}
	public void setMP(View view) {
		Intent outProds = new Intent(this, ProductListActivity.class);
		Bundle bund = new Bundle();
		bund.putInt(AntonConstants.TPROD, AntonConstants.MATERIA_PRIMA);
		outProds.putExtras(bund);
		startActivity(outProds);
	}
	public void setInsumos(View view) {
		Intent outProds = new Intent(this, ProductListActivity.class);
		Bundle bund = new Bundle();
		bund.putInt(AntonConstants.TPROD, AntonConstants.INSUMOS);
		outProds.putExtras(bund);
		startActivity(outProds);
	}
	public void setServicios(View view) {
		Intent outProds = new Intent(this, ProductListActivity.class);
		Bundle bund = new Bundle();
		bund.putInt(AntonConstants.TPROD, AntonConstants.SERVICIOS);
		outProds.putExtras(bund);
		startActivity(outProds);
	}
	
	
}
