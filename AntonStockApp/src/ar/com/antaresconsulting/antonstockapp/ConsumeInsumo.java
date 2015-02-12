package ar.com.antaresconsulting.antonstockapp;

import com.openerp.CreatePickingAsyncTask;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import ar.com.antaresconsulting.antonstockapp.adapters.BaseProductAdapter;
import ar.com.antaresconsulting.antonstockapp.adapters.EmpleadosAdapter;
import ar.com.antaresconsulting.antonstockapp.adapters.InsumoAdapter;
import ar.com.antaresconsulting.antonstockapp.listener.SwipeDismissListViewTouchListener;
import ar.com.antaresconsulting.antonstockapp.model.BaseProduct;
import ar.com.antaresconsulting.antonstockapp.model.Insumo;
import ar.com.antaresconsulting.antonstockapp.model.StockMove;
import ar.com.antaresconsulting.antonstockapp.model.StockPicking;
import ar.com.antaresconsulting.antonstockapp.model.dao.EmpleadoDAO;
import ar.com.antaresconsulting.antonstockapp.model.dao.InsumosDAO;

public class ConsumeInsumo extends ActionBarActivity implements EmpleadoDAO.EmpleCallbacks, InsumosDAO.InsumosCallbacks{

	private InsumosDAO insuDao;
	private EmpleadoDAO empleDao;
	private ListView productos;
	private AutoCompleteTextView prodBuscador;
	private ArrayAdapter<Insumo> prodBuscaAdapter;
	private EditText cantPlacasUtil;
	private ListView empleados;
	protected int productoSelecPos;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_consume_insumo);
		
		this.productos = (ListView) findViewById(R.id.productosEgreso);
		this.productos.setAdapter(new InsumoAdapter(this));
		this.prodBuscador = (AutoCompleteTextView) findViewById(R.id.productoNombreBuscar);
		this.prodBuscaAdapter = new ArrayAdapter<Insumo>(this,android.R.layout.simple_list_item_1);
		this.cantPlacasUtil = (EditText) findViewById(R.id.cantPlacaUtil);
		this.empleados = (ListView) findViewById(R.id.empleList);
		this.empleados.setAdapter(new EmpleadosAdapter(this));
		this.empleados.setChoiceMode(ListView.CHOICE_MODE_SINGLE);		
		this.empleDao = new EmpleadoDAO(this);
		this.empleDao.getAll();
		this.prodBuscador.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View arg1, int pos,
					long id) {
				productoSelecPos= pos;
			}
		});	
		this.prodBuscador.setOnFocusChangeListener( new View.OnFocusChangeListener() {
			 
		    public void onFocusChange( View v, boolean hasFocus ) {
		        if( hasFocus ) {
		        	prodBuscador.setText( "", TextView.BufferType.EDITABLE );
		        }
		    }
		 
		} );
		this.prodBuscador.setAdapter(this.prodBuscaAdapter);	
		this.insuDao = new InsumosDAO(this);
		this.insuDao.getInsumos();
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
		if (savedInstanceState == null) {

		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.consume_insumo, menu);
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

	public void scannProduct(MenuItem view) {

	}
	
	public void deliverInsumo(MenuItem view) {
		CreatePickingAsyncTask saveData = new CreatePickingAsyncTask(this);
		int maxProds = this.productos.getAdapter().getCount();

		String loc_source = AntonConstants.PRODUCT_LOCATION_STOCK;
		String loc_destination = AntonConstants.PRODUCT_LOCATION_INSUMOS;			
		
		StockPicking picking = new StockPicking("",AntonConstants.PICKING_TYPE_ID_INTERNAL,null,loc_source,loc_destination);
		for (int i = 0; i < maxProds; i++) {
			Insumo prod = (Insumo) this.productos.getAdapter().getItem(i);
			StockMove move = new StockMove(prod.getNombre(),prod.getId(), (Integer)prod.getUom()[0], loc_source, loc_destination, "", prod.getCantidadReal().toString());				
			move.setEmployee(prod.getEntregado().getId().toString());
			picking.addMove(move);
			picking.setActionDone(true);
		}
		saveData.execute(picking);		
	}
	
	@Override
	public void setInsumos() {
		this.prodBuscador.showDropDown();
		this.prodBuscaAdapter.addAll(this.insuDao.getInsumosList());
		this.prodBuscaAdapter.notifyDataSetChanged();				
	}	
	
	public void addProductInsumo(View view) {
		if(this.cantPlacasUtil.getText().toString().trim().equalsIgnoreCase("")){
			this.cantPlacasUtil.setError(getString(R.string.error_field_required));
			this.cantPlacasUtil.requestFocus();
			Toast tt = Toast.makeText(this.getApplicationContext(), "Primero debe definir la cantidad", Toast.LENGTH_SHORT);
			tt.show();
			return;
		}		
		InsumoAdapter adapter = (InsumoAdapter) this.productos.getAdapter();
		Insumo prod = (Insumo) this.prodBuscador.getAdapter().getItem(this.productoSelecPos);
		prod.setEntregado(((EmpleadosAdapter)this.empleados.getAdapter()).getSelectedEmple());
		prod.setCantidadReal(Double.parseDouble(this.cantPlacasUtil.getText().toString()));
		adapter.addProduct(prod);
		adapter.notifyDataSetChanged();
	}

	@Override
	public void setEmpleados() {
		((EmpleadosAdapter)this.empleados.getAdapter()).addAll(this.empleDao.getEmpleList());
	}	
}
