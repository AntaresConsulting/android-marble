package ar.com.antaresconsulting.antonstockapp.internal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.openerp.CreatePickingAsyncTask;
import com.openerp.OpenErpHolder;

import android.support.v4.app.NavUtils;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;
import ar.com.antaresconsulting.antonstockapp.AntonConstants;
import ar.com.antaresconsulting.antonstockapp.AntonLauncherActivity;
import ar.com.antaresconsulting.antonstockapp.R;
import ar.com.antaresconsulting.antonstockapp.R.id;
import ar.com.antaresconsulting.antonstockapp.R.layout;
import ar.com.antaresconsulting.antonstockapp.R.menu;
import ar.com.antaresconsulting.antonstockapp.R.string;
import ar.com.antaresconsulting.antonstockapp.adapters.BaseProductAdapter;
import ar.com.antaresconsulting.antonstockapp.adapters.PedidoLineaMPAdapter;
import ar.com.antaresconsulting.antonstockapp.listener.SwipeDismissListViewTouchListener;
import ar.com.antaresconsulting.antonstockapp.model.BaseProduct;
import ar.com.antaresconsulting.antonstockapp.model.Dimension;
import ar.com.antaresconsulting.antonstockapp.model.Insumo;
import ar.com.antaresconsulting.antonstockapp.model.MateriaPrima;
import ar.com.antaresconsulting.antonstockapp.model.Partner;
import ar.com.antaresconsulting.antonstockapp.model.PedidoLinea;
import ar.com.antaresconsulting.antonstockapp.model.SelectionObject;
import ar.com.antaresconsulting.antonstockapp.model.StockMove;
import ar.com.antaresconsulting.antonstockapp.model.StockPicking;
import ar.com.antaresconsulting.antonstockapp.model.dao.MateriaPrimaDAO;

public class ReEnterProducts extends Activity implements MateriaPrimaDAO.MateriaPrimaCallbacks{
	private MateriaPrimaDAO prodDao;
	private Spinner dimTipo;
	private ListView productos;
	private ListView productosDispo;
	private EditText cantPlacas;
	private EditText dimH;
	private EditText dimW;
	private Spinner dimT;
	ArrayAdapter<BaseProduct> prodBuscaAdapter;

	protected boolean isScanSearch = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_reenter_products);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		this.productosDispo = (ListView) findViewById(R.id.productosDispo);
		this.productosDispo.setChoiceMode(ListView.CHOICE_MODE_SINGLE);			
		this.prodBuscaAdapter = new ArrayAdapter<BaseProduct>(this,android.R.layout.simple_list_item_single_choice);
		this.productosDispo.setAdapter(this.prodBuscaAdapter);
		this.cantPlacas = (EditText) findViewById(R.id.clientesList);
		this.dimH = (EditText) findViewById(R.id.altoPlaca);
		this.dimW = (EditText) findViewById(R.id.anchoPlaca);
		this.dimT = (Spinner) findViewById(R.id.espesorPlaca);
		this.dimTipo = (Spinner) findViewById(R.id.tipoDim);
		this.dimTipo.setAdapter(new ArrayAdapter<SelectionObject>(this,android.R.layout.simple_list_item_1,SelectionObject.getDimTipoData()));
		this.productos = (ListView) findViewById(R.id.productosPedido);
		this.productos.setAdapter(new PedidoLineaMPAdapter(this));
		SwipeDismissListViewTouchListener touchListener = new SwipeDismissListViewTouchListener(
				this.productos,
				new SwipeDismissListViewTouchListener.DismissCallbacks() {
					@Override
					public boolean canDismiss(int position) {
						return true;
					}

					@Override
					public void onDismiss(ListView listView,
							int[] reverseSortedPositions) {
						for (int position : reverseSortedPositions) {
							((BaseProductAdapter) productos.getAdapter()).delProduct((BaseProduct) productos.getAdapter().getItem(position));
						}
						((BaseAdapter) productos.getAdapter()).notifyDataSetChanged();
					}
				});
		this.productos.setOnTouchListener(touchListener);
		this.productos.setOnScrollListener(touchListener.makeScrollListener());
		this.prodDao = new MateriaPrimaDAO(this);
		this.prodDao.getMateriaPrimaInProduction(AntonConstants.PRODUCT_LOCATION_PRODUCTION);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
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
		CreatePickingAsyncTask saveData = new CreatePickingAsyncTask(this);
		int maxProds = this.productos.getAdapter().getCount();

		Partner proveedor = null;
		String origin = "";

		String loc_source = AntonConstants.PRODUCT_LOCATION_PRODUCTION;
		String loc_destination = AntonConstants.PRODUCT_LOCATION_STOCK;			
		
		StockPicking picking = new StockPicking(origin,AntonConstants.PICKING_TYPE_ID_INTERNAL,proveedor.getId(),loc_source,loc_destination,AntonConstants.RAW_PICKING);

		for (int i = 0; i < maxProds; i++) {
			MateriaPrima prod = (MateriaPrima) this.productos.getAdapter().getItem(i);
			StockMove move = new StockMove(prod.getNombre(), prod.getId(), (Integer)prod.getUom()[0], loc_source, loc_destination, origin, prod.getCantidadReal());				
			picking.addMove(move);
		}
		saveData.execute(picking);
	}



	public void addProduct(View view) {
		boolean cancel = false;
		View focus = null; 
		if(this.cantPlacas.getText().toString().trim().equalsIgnoreCase("")){
			this.cantPlacas.setError(getString(R.string.error_field_required));
			cancel = true;
			focus = this.cantPlacas;
		}

		if(this.dimW.getText().toString().trim().equalsIgnoreCase("")){
			this.dimW.setError(getString(R.string.error_field_required));
			cancel = true;
			focus = this.dimW;
		}
		if(this.dimH.getText().toString().trim().equalsIgnoreCase("")){
			this.dimH.setError(getString(R.string.error_field_required));
			cancel = true;
			focus = this.dimH;
		}

		if(cancel){
			focus.requestFocus();
			return;
		}

		int position =  productosDispo.getCheckedItemPosition();
		if(position == AdapterView.INVALID_POSITION){
			Toast tt = Toast.makeText(this, "Primero debe seleccionar un producto", Toast.LENGTH_SHORT);
			tt.show();
			return;
		}
		MateriaPrima prod = (MateriaPrima) this.productosDispo.getAdapter().getItem(position);
		double cantP = Double.parseDouble(this.cantPlacas.getText().toString());
		double hight = Double.parseDouble(this.dimH.getText().toString());
		double width = Double.parseDouble(this.dimW.getText().toString());
		if(((hight * width)* cantP) > prod.getCantidadReal().doubleValue()){
			Toast tt = Toast.makeText(this, "La superficie a reingresar debe ser menor a la superficie disponible", Toast.LENGTH_SHORT);
			tt.show();
			return;			
		}
	
	}

	@Override
	public void setMateriaPrima() {
		prodBuscaAdapter.addAll(this.prodDao.getMateriaPrimasList());			
	}



}
