package ar.com.antaresconsulting.antonstockapp.product;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.openerp.CreateAsyncTask;
import com.openerp.DeleteAsyncTask;
import com.openerp.UpdateStockAsyncTask;
import com.openerp.WriteAsyncTask;
import com.openerp.OpenErpHolder;

import android.app.ActionBar.LayoutParams;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
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
import ar.com.antaresconsulting.antonstockapp.NavigationDrawerFragment;
import ar.com.antaresconsulting.antonstockapp.R;
import ar.com.antaresconsulting.antonstockapp.NavigationDrawerFragment.NavigationDrawerCallbacks;
import ar.com.antaresconsulting.antonstockapp.R.id;
import ar.com.antaresconsulting.antonstockapp.R.layout;
import ar.com.antaresconsulting.antonstockapp.R.menu;
import ar.com.antaresconsulting.antonstockapp.R.string;
import ar.com.antaresconsulting.antonstockapp.R.style;
import ar.com.antaresconsulting.antonstockapp.adapters.BaseProductAdapter;
import ar.com.antaresconsulting.antonstockapp.adapters.TabsAdapter;
import ar.com.antaresconsulting.antonstockapp.model.BaseProduct;
import ar.com.antaresconsulting.antonstockapp.model.Dimension;
import ar.com.antaresconsulting.antonstockapp.model.MateriaPrima;
import ar.com.antaresconsulting.antonstockapp.partner.PartnerListFragment;
import ar.com.antaresconsulting.antonstockapp.partner.PartnerListFragment.Callbacks;
import ar.com.antaresconsulting.antonstockapp.popup.AssignSuppPopupFragment;
import ar.com.antaresconsulting.antonstockapp.popup.OrderPointPopupFragment;
import ar.com.antaresconsulting.antonstockapp.popup.UpdateStockPopupFragment;
import ar.com.antaresconsulting.antonstockapp.util.AntonConstants;

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
		UpdateStockPopupFragment.UpdateStockListener,
		OrderPointPopupFragment.OrderPointListener,		
		AssignSuppPopupFragment.AssignSuppListener,	
		CreateAsyncTask.CreateAsyncTaskCallbacks,
		PartnerListFragment.Callbacks,
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
	private BaseProduct selected = null;

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
		switch (this.tProd) {
		case AntonConstants.MATERIA_PRIMA:
			 if(this.myMenu != null && this.myMenu.size() > 0 )
				 this.myMenu.getItem(0).setTitle(getString(R.string.title_action_add_mp));			
			break;
		case AntonConstants.SERVICIOS:
			 if(this.myMenu != null && this.myMenu.size() > 0 )
				 this.myMenu.getItem(0).setTitle(getString(R.string.title_action_add_servicio));			
			break;
		case AntonConstants.INSUMOS:
			 if(this.myMenu != null && this.myMenu.size() > 0 )
				 this.myMenu.getItem(0).setTitle(getString(R.string.title_action_add_insumo));						
			break;
		case AntonConstants.BACHAS:
			 if(this.myMenu != null && this.myMenu.size() > 0 )
				 this.myMenu.getItem(0).setTitle(getString(R.string.title_action_add_bacha));						
			break;			
		default:
			break;
		}
		restoreActionBar();
		return true;
	}

	/**
	 * Callback method from {@link ItemListFragment.Callbacks} indicating that
	 * the item with the given ID was selected.
	 */
	@Override
	public void onItemSelected(BaseProduct prod) {
		this.selected  = prod;
		if (mTwoPane) {
			Bundle arguments = new Bundle();
			arguments.putSerializable(AntonConstants.PRODUCT_SELECTED, prod);
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
			arguments.putBoolean(AntonConstants.SET_SUPPLIER, new Boolean(true));

			mTabsAdapter.addTab(mTabHost.newTabSpec("proveedor").setIndicator("Proveedores"),PartnerListFragment.class, arguments);
			if (tProd == AntonConstants.MATERIA_PRIMA)
				mTabsAdapter.addTab(mTabHost.newTabSpec("dimensiones")
						.setIndicator("Dimensiones"), DimensionsFragment.class,
						arguments);
			mTabsAdapter.notifyDataSetChanged();
		} else {
			// In single-pane mode, simply start the detail activity
			// for the selected item ID.
			Intent detailIntent = new Intent(this, ProductDetailActivity.class);
			detailIntent.putExtra(AntonConstants.PRODUCT_SELECTED, prod);
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
			this.listFragment.refreshProducts(AntonConstants.MATERIA_PRIMA);
			break;
		case 1:
			mTitle = getString(R.string.title_expenses);
			this.tProd = AntonConstants.INSUMOS;
			this.listFragment.refreshProducts(AntonConstants.INSUMOS);
			break;
		case 2:
			mTitle = getString(R.string.title_bachas);
			this.tProd = AntonConstants.BACHAS;
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
		Long[] prodsDelete = new Long[checked.size()];
		Long[] tplsDelete = new Long[checked.size()];
		
		int i = 0;
		for (Iterator<BaseProduct> iterator = checked.iterator(); iterator
				.hasNext();) {
			BaseProduct baseProduct = (BaseProduct) iterator.next();
			prodsDelete[i] = new Long(baseProduct.getId());
			tplsDelete[i++] = new Long(baseProduct.getTemplateId());			
		}
		if (prodsDelete.length > 0) {
	
			// WriteAsyncTask delProd = new WriteAsyncTask(this);
			DeleteAsyncTask delProd = new DeleteAsyncTask(this);
			DeleteAsyncTask delProd2 = new DeleteAsyncTask(this);
			
			OpenErpHolder.getInstance().setmModelName(AntonConstants.PRODUCT_MODEL);
			delProd.execute(prodsDelete);
			if(tProd != AntonConstants.INSUMOS){
				OpenErpHolder.getInstance().setmModelName(AntonConstants.PRODUCT_TPL_MODEL);
				delProd2.execute(tplsDelete);				
			}					
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

	@Override
	protected void onActivityResult (int requestCode, int resultCode, Intent data){
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK && data != null)
			tProd = data.getIntExtra("aaa",-1);
		
	}
	
	public void updateStock(View view) {
		UpdateStockPopupFragment popconf = UpdateStockPopupFragment.newInstance(this.tProd);
		popconf.show(getFragmentManager(),"Server_Search");		
	}
	
	public void asignSupp(View view) {
		AssignSuppPopupFragment popconf = AssignSuppPopupFragment.newInstance(this.tProd);
		popconf.show(getFragmentManager(),"Server_Search");		
	}
	
	public void stockRule(View view) {
		OrderPointPopupFragment popconf = OrderPointPopupFragment.newInstance(this.tProd);
		popconf.show(getFragmentManager(),"Server_Search");		
	}

	
	
	@Override
	public void updateStockAction(int cant, Dimension dim) {
		UpdateStockAsyncTask update = new UpdateStockAsyncTask(this);
		HashMap<String, Object> values = new HashMap<String, Object>();
		if(dim != null){
			values.put("dimension_id", dim);
			values.put("dimension_unit_new", cant);			
			values.put("is_raw", new Boolean(true));			
		}else{
			values.put("dimension_id", null);
			values.put("dimension_unit_new", 0);						
			values.put("is_raw", new Boolean(false));					
		}
		values.put("lot_id", false);
		values.put("new_quantity", cant);
		ProductDetailFragment prodDet = (ProductDetailFragment) getFragmentManager().findFragmentById(R.id.product_detail_container);
		BaseProduct prod = prodDet.getProductSelected();
		
		values.put("location_id", (Integer)prodDet.getProductSelected().getLocId()[0]);	
		values.put("product_id", prodDet.getProductSelected().getId());
		update.execute(values);
	}

	@Override
	public void onItemSelected(String id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void assignSupplierAction(Integer i) {
		WriteAsyncTask aa = new WriteAsyncTask(this);
        OpenErpHolder.getInstance().setmModelName("product.template");		
        HashMap[] values = new HashMap[2];
        values[0] = new HashMap<String, Object>();
        values[0].put("id",this.selected.getTemplateId());
        values[1] = new HashMap<String, Object>();
        HashMap<String, Object> newRecord= new HashMap<String, Object>();
        newRecord.put("name", new Integer(i));
        newRecord.put("sequence", new Integer(1));
        newRecord.put("product_code", new Boolean(false));
        newRecord.put("product_name", new Boolean(false));
        Integer[] tt = new Integer[0];
        newRecord.put("pricelist_ids",tt );
        newRecord.put("min_qty", new Integer(0));
        newRecord.put("delay", new Integer(1));
        newRecord.put("company_id", new Integer(1));
        values[1].put("seller_ids", new Object[]{new Object[]{0,new Boolean(false),newRecord}});
		aa.execute(values);
		
	}

	@Override
	public void setOrderRule(BigDecimal min, BigDecimal max) {
		CreateAsyncTask aa = new CreateAsyncTask(this);
        OpenErpHolder.getInstance().setmModelName("stock.warehouse.orderpoint");	
        HashMap[] values = new HashMap[2];        
        values[0] = new HashMap<String, Object>();
        values[0].put("active", new Boolean(true));
        values[0].put("product_id", this.selected.getId());
        values[0].put("product_max_qty", max.doubleValue());
        values[0].put("product_min_qty", min.doubleValue());

        values[1] = new HashMap<String, Object>();
        //values[1].put("defaults", new String[]{"product_max_qty","qty_multiple","name","company_id","location_id","qty_multiple","warehouse_id"});
        values[1].put("defaults", new String[]{"qty_multiple","name","company_id","location_id","qty_multiple","warehouse_id"});
		aa.execute(values);		
	}

	@Override
	public void setResultCreate(Long res) {
		// TODO Auto-generated method stub
		
	}
	
}
