package ar.com.antaresconsulting.antonstockapp;

import java.util.Iterator;
import java.util.List;

import com.openerp.DeleteAsyncTask;
import com.openerp.WriteAsyncTask;
import com.openerp.OpenErpHolder;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import ar.com.antaresconsulting.antonstockapp.adapters.BaseProductAdapter;
import ar.com.antaresconsulting.antonstockapp.model.BaseProduct;

/**
 * An activity representing a list of Items. This activity has different
 * presentations for handset and tablet-size devices. On handsets, the activity
 * presents a list of items, which when touched, lead to a
 * {@link ItemDetailActivity} representing item details. On tablets, the
 * activity presents the list of items and item details side-by-side using two
 * vertical panes.
 * <p>
 * The activity makes heavy use of fragments. The list of items is a
 * {@link ItemListFragment} and the item details (if present) is a
 * {@link ItemDetailFragment}.
 * <p>
 * This activity also implements the required {@link ItemListFragment.Callbacks}
 * interface to listen for item selections.
 */
public class ProductListActivity extends ActionBarActivity implements WriteAsyncTask.WriteAsyncTaskCallbacks,
NavigationDrawerFragment.NavigationDrawerCallbacks,ProductListFragment.Callbacks {

	/**
	 * Whether or not the activity is in two-pane mode, i.e. running on a tablet
	 * device.
	 */
	private boolean mTwoPane;
	private CharSequence mTitle;
	private NavigationDrawerFragment mNavigationDrawerFragment;
	private ProductListFragment listFragment;
	private ProductDetailFragment fragment;
	private int tProd;
	private Menu myMenu;

	@Override
	public void onSaveInstanceState(Bundle savedInstanceState){
		super.onSaveInstanceState(savedInstanceState);		
		savedInstanceState.putInt(AntonConstants.TPROD, this.tProd);
	} 

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_product_list);		
		getActionBar().setDisplayHomeAsUpEnabled(true);
		if(savedInstanceState != null){
			this.tProd = savedInstanceState.getInt(AntonConstants.TPROD);
		}else{
			this.tProd = AntonConstants.MATERIA_PRIMA;
		}

		mNavigationDrawerFragment = (NavigationDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
		listFragment = (ProductListFragment) getFragmentManager().findFragmentById(R.id.product_list);
		mTitle = getString(R.string.title_forstock);
		// Set up the drawer.
		mNavigationDrawerFragment.setUp(R.id.navigation_drawer,	(DrawerLayout) findViewById(R.id.drawer_layout));

		if (findViewById(R.id.product_detail_container) != null) {
			mTwoPane = true;
			((ProductListFragment) getFragmentManager().findFragmentById(R.id.product_list)).setActivateOnItemClick(true);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		if(OpenErpHolder.getInstance().getmOConn().isManager()){
			getMenuInflater().inflate(R.menu.list_products, menu);
			this.myMenu = menu;
		}
		return true;
	}


	/**
	 * Callback method from {@link ItemListFragment.Callbacks} indicating that
	 * the item with the given ID was selected.
	 */
	@Override
	public void onItemSelected(String id) {
		if (mTwoPane) {
			Bundle arguments = new Bundle();
			arguments.putString(PartnerDetailFragment.ARG_ITEM_ID, id);
			arguments.putInt(AntonConstants.TPROD, this.tProd);
			fragment = new ProductDetailFragment();
			fragment.setArguments(arguments);
			getFragmentManager().beginTransaction().replace(R.id.product_detail_container, fragment).commit();

		} else {
			// In single-pane mode, simply start the detail activity
			// for the selected item ID.
			Intent detailIntent = new Intent(this, ProductDetailActivity.class);
			detailIntent.putExtra(ProductDetailFragment.ARG_ITEM_ID, id);
			startActivity(detailIntent);
		}
	}

	public void restoreActionBar() {
		ActionBar actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setTitle(mTitle);
	}
	@Override
	public void onNavigationDrawerItemSelected(int position) {
		switch (position) {
		case 0:
			mTitle = getString(R.string.title_forstock);
			this.tProd = AntonConstants.MATERIA_PRIMA;
			if(this.myMenu != null)
				this.myMenu.getItem(0).setTitle(getString(R.string.title_action_add_mp));
			this.listFragment.refreshProducts(AntonConstants.MATERIA_PRIMA);
			break;	
		case 1:
			mTitle = getString(R.string.title_expenses);
			this.tProd = AntonConstants.INSUMOS;
			if(this.myMenu != null)
				this.myMenu.getItem(0).setTitle(getString(R.string.title_action_add_insumo));
			this.listFragment.refreshProducts(AntonConstants.INSUMOS);
			break;	
		case 2:
			mTitle = getString(R.string.title_bachas);
			this.tProd = AntonConstants.BACHAS;
			if(this.myMenu != null)
				this.myMenu.getItem(0).setTitle(getString(R.string.title_action_add_bacha));
			this.listFragment.refreshProducts(AntonConstants.BACHAS);
			break;	
		case 3:
			mTitle = getString(R.string.title_services);
			this.tProd = AntonConstants.SERVICIOS;
			this.listFragment.refreshProducts(AntonConstants.SERVICIOS);
			break;				
		default:
			finish();
			break;			
		}

		restoreActionBar();
	}
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == android.R.id.home) {
			NavUtils.navigateUpTo(this, new Intent(this,AntonLauncherActivity.class));
			return true;
		}		
		return super.onOptionsItemSelected(item);
	}	
	public void editProduct(MenuItem view){
		Intent addProds = new Intent(this, AddProductActivity.class);
		Bundle b = new Bundle();
		b.putInt(AntonConstants.TPROD, this.tProd); //Your id
		ProductDetailFragment prodDet = (ProductDetailFragment) getFragmentManager().findFragmentById(R.id.product_detail_container);
		if(prodDet != null){
			b.putSerializable(AntonConstants.ARG_ITEM_ID, prodDet.getProductSelected()); //Your id
			addProds.putExtras(b); 		
			startActivity(addProds);			
		}
	}
	
	public void delProduct(MenuItem view){
		List<BaseProduct> checked = ((BaseProductAdapter)this.listFragment.getListView().getAdapter()).getCheckedItems();
		Long[] toDelete = new Long[checked.size()];
		int i = 0;
		for (Iterator<BaseProduct> iterator = checked.iterator(); iterator.hasNext();) {
			BaseProduct baseProduct = (BaseProduct) iterator.next();
			toDelete[i++] = new Long(baseProduct.getId());
		}
		if(toDelete.length > 0){
			DeleteAsyncTask delProd = new DeleteAsyncTask(this);
			OpenErpHolder.getInstance().setmModelName(AntonConstants.PRODUCT_MODEL);
			delProd.execute(toDelete);			
		}
	}
	public void takePhoto(View view){
		this.fragment.takePhoto();
	}
	public void addProduct(MenuItem view){
		Intent addProds = new Intent(this, AddProductActivity.class);
		Bundle b = new Bundle();
		b.putInt(AntonConstants.TPROD, this.tProd); //Your id
		addProds.putExtras(b); 		
		startActivity(addProds);		
	}

	@Override
	public void setResultCreate(Boolean res) {
		Toast tt =Toast.makeText(getApplicationContext(), "Se ha guardado la foto en forma correcta!.", Toast.LENGTH_SHORT);
		tt.show();
		this.onNavigationDrawerItemSelected(this.tProd);
	}	
}
