package ar.com.antaresconsulting.antonstockapp.internal;

import com.openerp.CreatePickingAsyncTask;
import android.support.v4.app.NavUtils;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;
import ar.com.antaresconsulting.antonstockapp.AntonLauncherActivity;
import ar.com.antaresconsulting.antonstockapp.AntonStockApp;
import ar.com.antaresconsulting.antonstockapp.R;
import ar.com.antaresconsulting.antonstockapp.adapters.PedidoLineaMPAdapter;
import ar.com.antaresconsulting.antonstockapp.listener.SwipeDismissListViewTouchListener;
import ar.com.antaresconsulting.antonstockapp.model.BaseProduct;
import ar.com.antaresconsulting.antonstockapp.model.Dimension;
import ar.com.antaresconsulting.antonstockapp.model.MateriaPrima;
import ar.com.antaresconsulting.antonstockapp.model.SelectionObject;
import ar.com.antaresconsulting.antonstockapp.model.StockMove;
import ar.com.antaresconsulting.antonstockapp.model.StockPicking;
import ar.com.antaresconsulting.antonstockapp.model.dao.MateriaPrimaDAO;
import ar.com.antaresconsulting.antonstockapp.util.AntonConstants;

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
		this.cantPlacas = (EditText) findViewById(R.id.canDim);
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
							((PedidoLineaMPAdapter) productos.getAdapter()).delLinea((StockMove) productos.getAdapter().getItem(position));
						}
						((PedidoLineaMPAdapter) productos.getAdapter()).notifyDataSetChanged();
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
		getMenuInflater().inflate(R.menu.reenter_products, menu);
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
		if(maxProds <= 0){
			Toast tt = Toast.makeText(this.getApplicationContext(), "Debe haber seleccionado almenos un producto!", Toast.LENGTH_SHORT);
			tt.show();		
			return;
		}

		String origin = "";
		Object[] cliente = new Object[1];
		cliente[0] = new Integer(0);
		
		Integer loc_source = AntonStockApp.getExternalId(AntonConstants.PRODUCT_LOCATION_PRODUCTION);
		
		StockPicking picking = new StockPicking(origin,AntonConstants.PICKING_TYPE_ID_INTERNAL,cliente,AntonConstants.RAW_PICKING);

		for (int i = 0; i < maxProds; i++) {
			StockMove prod = (StockMove) this.productos.getAdapter().getItem(i);
			MateriaPrima prodObj = (MateriaPrima)prod.getProduct()[0];
			Integer loc_destination = (Integer) prodObj.getLocId()[0];
			StockMove move = new StockMove(prod.getName(),prod.getProduct(), prod.getUom(), loc_source, loc_destination, origin, prod.getQty(),prod.getDimension(),prod.getQtytDim(),null);				
			picking.addMove(move);
		}
		picking.setActionDone(true);
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
		StockMove pl = new StockMove();		
		pl.setName(prod.getNombre());
		pl.setUom(prod.getUom());		
		pl.setDimension(new Dimension(this.dimH.getText().toString(),this.dimW.getText().toString(),(String)this.dimT.getSelectedItem(),(SelectionObject) this.dimTipo.getSelectedItem()));
		pl.setQtytDim(new Integer(this.cantPlacas.getText().toString()));
		pl.setQty(new Double((hight * width)* cantP));
		Object[] prodData=new Object[1];
		prodData[0] = prod;
		pl.setProduct(prodData);		
		((PedidoLineaMPAdapter)this.productos.getAdapter()).addLinea(pl);
		((PedidoLineaMPAdapter)this.productos.getAdapter()).notifyDataSetChanged();
	}

	@Override
	public void setMateriaPrima() {
		prodBuscaAdapter.addAll(this.prodDao.getMateriaPrimasList());			
	}



}
