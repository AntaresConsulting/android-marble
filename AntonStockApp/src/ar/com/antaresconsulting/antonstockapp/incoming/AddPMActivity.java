package ar.com.antaresconsulting.antonstockapp.incoming;

import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import ar.com.antaresconsulting.antonstockapp.AntonLauncherActivity;
import ar.com.antaresconsulting.antonstockapp.R;
import ar.com.antaresconsulting.antonstockapp.R.id;
import ar.com.antaresconsulting.antonstockapp.R.layout;
import ar.com.antaresconsulting.antonstockapp.R.menu;
import ar.com.antaresconsulting.antonstockapp.popup.DatePickerPopupFragment;
import ar.com.antaresconsulting.antonstockapp.product.ProductTypeFragment;

public class AddPMActivity extends ActionBarActivity implements  DatePickerPopupFragment.DatePickerListener{



	private Menu mainMenu;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_in_products);	

		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction().add(R.id.container, ProductTypeFragment.newInstance(true,true,true,false)).commit();			
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.add_pm, menu);
		mainMenu = menu;
		return true;
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == android.R.id.home) {
			NavUtils.navigateUpTo(this, new Intent(this,AntonLauncherActivity.class));
			return true;
		}		
		return super.onOptionsItemSelected(item);
	}
	
	public void setFecha(View v) {
	    DialogFragment newFragment = new DatePickerPopupFragment();
	    newFragment.show(getFragmentManager(), "datePicker");
	}
	
	public void setBachas(View view) {
		enableMenuVis();
		getFragmentManager().beginTransaction().replace(R.id.container, AddPMBachaFragment.newInstance()).commit();
	}
	public void setMP(View view) {
		enableMenuVis();
		getFragmentManager().beginTransaction().replace(R.id.container, AddPMMPFragment.newInstance()).commit();
		
	}
	public void setInsumos(View view) {
		enableMenuVis();
		getFragmentManager().beginTransaction().replace(R.id.container, AddPMInsumoFragment.newInstance()).commit();
	}

	public void setLinea(View view) {
		((AddPMActions) getFragmentManager().findFragmentById(	R.id.container)).setLineaPedido();		
	}
	
	public void searchPM(MenuItem menu) {
		((AddPMActions) getFragmentManager().findFragmentById(	R.id.container)).searchProduct();		
	}
	
	public void scannPM(MenuItem menu) {
		
	}
	
	public void addPM(MenuItem menu) {
		((AddPMActions) getFragmentManager().findFragmentById(	R.id.container)).addPM();		
	}	
	
	private void enableMenuVis(){
		for (int i = 0; i < mainMenu.size(); i++) {
			mainMenu.getItem(i).setVisible(true);
		} 	
	}

	@Override
	public void setSelectedDate(int year, int month, int day) {
		((AddPMActions) getFragmentManager().findFragmentById(	R.id.container)).setDate(year, month, day);				
	}
	
}

