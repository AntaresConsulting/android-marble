package ar.com.antaresconsulting.antonstockapp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.List;

import com.openerp.DeleteAsyncTask;
import com.openerp.WriteAsyncTask;
import com.openerp.OpenErpHolder;

import android.app.ActionBar.LayoutParams;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Base64;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TabHost;
import android.widget.Toast;
import ar.com.antaresconsulting.antonstockapp.adapters.BaseProductAdapter;
import ar.com.antaresconsulting.antonstockapp.adapters.TabsAdapter;
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
public class ProductListActivity extends ActionBarActivity implements
		WriteAsyncTask.WriteAsyncTaskCallbacks,
		NavigationDrawerFragment.NavigationDrawerCallbacks,
		ProductListFragment.Callbacks {

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
	private Menu myMenu = null;

	private TabHost mTabHost;
	private ViewPager mViewPager;
	private TabsAdapter mTabsAdapter;

	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
		super.onSaveInstanceState(savedInstanceState);
		savedInstanceState.putInt(AntonConstants.TPROD, this.tProd);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_product_list);
		Bundle params = getIntent().getExtras();
		if (params != null) {
			this.tProd = params.getInt(AntonConstants.TPROD);
		} else {
			if (savedInstanceState != null) {
				this.tProd = savedInstanceState.getInt(AntonConstants.TPROD);
			} else {
				this.tProd = AntonConstants.MATERIA_PRIMA;
			}
		}

		mTabHost = (TabHost) findViewById(android.R.id.tabhost);
		mTabHost.setup();

		mViewPager = (ViewPager) findViewById(R.id.pager);

		mNavigationDrawerFragment = (NavigationDrawerFragment) getSupportFragmentManager()
				.findFragmentById(R.id.navigation_drawer);
		listFragment = (ProductListFragment) getFragmentManager()
				.findFragmentById(R.id.product_list);
		mTitle = this.getTitle(this.tProd);
		// Set up the drawer.
		mNavigationDrawerFragment.setUp(R.id.navigation_drawer,
				(DrawerLayout) findViewById(R.id.drawer_layout));

		if (findViewById(R.id.product_detail_container) != null) {
			mTwoPane = true;
			((ProductListFragment) getFragmentManager().findFragmentById(
					R.id.product_list)).setActivateOnItemClick(true);
		}
		this.listFragment.refreshProducts(this.tProd);

	}

	private String getTitle(int valP) {
		String stringVal = "";
		switch (valP) {
		case AntonConstants.MATERIA_PRIMA:
			stringVal = getString(R.string.title_forstock);
			break;
		case AntonConstants.INSUMOS:
			stringVal = getString(R.string.title_expenses);
			break;
		case AntonConstants.SERVICIOS:
			stringVal = getString(R.string.title_services);
			break;
		case AntonConstants.BACHAS:
			stringVal = getString(R.string.title_bachas);
			break;

		default:
			break;
		}
		return stringVal;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	
			super.onCreateOptionsMenu(menu);
			getMenuInflater().inflate(R.menu.list_products, menu);
			this.myMenu = menu;			
		
//		if (mNavigationDrawerFragment.isDrawerOpen()) {
//			if (OpenErpHolder.getInstance().getmOConn().isManager()) {
//				getMenuInflater().inflate(R.menu.list_products, menu);
//			}
//			//return true;
//			this.myMenu = menu;
//		}
		
		//return super.onCreateOptionsMenu(menu);
//		restoreActionBar();
		return true;
	}

	/**
	 * Callback method from {@link ItemListFragment.Callbacks} indicating that
	 * the item with the given ID was selected.
	 */
	@Override
	public void onItemSelected(BaseProduct prod) {
		if (mTwoPane) {
			Bundle arguments = new Bundle();
			arguments.putSerializable(PartnerDetailFragment.ARG_ITEM_ID, prod);
			arguments.putInt(AntonConstants.TPROD, this.tProd);
			fragment = new ProductDetailFragment();
			fragment.setArguments(arguments);

			getFragmentManager().beginTransaction()
					.replace(R.id.product_detail_container, fragment).commit();

			mTabHost.clearAllTabs();

			mTabsAdapter = new TabsAdapter(this, mTabHost, mViewPager);
			mTabsAdapter.addTab(mTabHost.newTabSpec("detailExtra")
					.setIndicator("Atributos del Producto"),
					ProductDetailExtraFragment.class, arguments);
			mTabsAdapter.addTab(mTabHost.newTabSpec("proveedor").setIndicator("Prodveedores"),
			PartnerListFragment.class, arguments);
			if (tProd == AntonConstants.MATERIA_PRIMA)
				mTabsAdapter.addTab(mTabHost.newTabSpec("dimensiones")
						.setIndicator("Dimensiones"), DimensionsFragment.class,
						arguments);
			mTabsAdapter.notifyDataSetChanged();
		} else {
			// In single-pane mode, simply start the detail activity
			// for the selected item ID.
			Intent detailIntent = new Intent(this, ProductDetailActivity.class);
			detailIntent.putExtra(ProductDetailFragment.ARG_ITEM_ID, prod);
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
			if(this.myMenu != null)
			 this.myMenu.getItem(0).setTitle(getString(R.string.title_action_add_servicio));
			this.listFragment.refreshProducts(AntonConstants.SERVICIOS);
			break;
		default:
			finish();
			break;
		}

		restoreActionBar();
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		return super.onOptionsItemSelected(item);
	}

	public void editProduct(MenuItem view) {
		Intent addProds = new Intent(this, AddProductActivity.class);
		Bundle b = new Bundle();
		b.putInt(AntonConstants.TPROD, this.tProd); // Your id
		ProductDetailFragment prodDet = (ProductDetailFragment) getFragmentManager()
				.findFragmentById(R.id.product_detail_container);
		if (prodDet != null) {
			b.putSerializable(AntonConstants.ARG_ITEM_ID,
					prodDet.getProductSelected()); // Your id
			addProds.putExtras(b);
			startActivity(addProds);
		}
	}

	public void delProduct(MenuItem view) {
		List<BaseProduct> checked = ((BaseProductAdapter) this.listFragment
				.getListView().getAdapter()).getCheckedItems();
		Long[] toDelete = new Long[checked.size()];
		int i = 0;
		for (Iterator<BaseProduct> iterator = checked.iterator(); iterator
				.hasNext();) {
			BaseProduct baseProduct = (BaseProduct) iterator.next();
			toDelete[i++] = new Long(baseProduct.getId());
		}
		if (toDelete.length > 0) {
			// WriteAsyncTask delProd = new WriteAsyncTask(this);
			DeleteAsyncTask delProd = new DeleteAsyncTask(this);
			OpenErpHolder.getInstance().setmModelName(
					AntonConstants.PRODUCT_MODEL);
			delProd.execute(toDelete);
		}
	}

	public void takePhoto(View view) {
		this.fragment.takePhoto();
	}

	public void addProduct(MenuItem view) {
		Intent addProds = new Intent(this, AddProductActivity.class);
		Bundle b = new Bundle();
		b.putInt(AntonConstants.TPROD, this.tProd); // Your id
		addProds.putExtras(b);
		startActivity(addProds);
	}

	@Override
	public void setResultCreate(Boolean res) {
		Toast tt = Toast.makeText(getApplicationContext(),
				"Se ha guardado la foto en forma correcta!.",
				Toast.LENGTH_SHORT);
		tt.show();
		this.onNavigationDrawerItemSelected(this.tProd);
	}

	public void zoomPhoto(View view) {
		LayoutInflater layoutInflater = (LayoutInflater) getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);
		View popupView = layoutInflater.inflate(R.layout.solo_photo, null);
		ProductDetailFragment prodDet = (ProductDetailFragment) getFragmentManager().findFragmentById(R.id.product_detail_container);
		BaseProduct prod = prodDet.getProductSelected();
		
		byte[] decodedString = Base64.decode(prod.getProductBig().trim(), Base64.DEFAULT);
		Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0,decodedString.length);
	        
		((ImageView)popupView.findViewById(R.id.imageContainer)).setImageBitmap(decodedByte);
		
		final PopupWindow popupWindow = new PopupWindow(popupView,LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,true);
		popupWindow.setAnimationStyle(R.style.PopupWindowAnimation);
		popupWindow.setOutsideTouchable(true);
		popupWindow.setTouchable(true);
		popupWindow.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);
	}

}
