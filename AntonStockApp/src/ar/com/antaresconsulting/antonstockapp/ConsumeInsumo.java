package ar.com.antaresconsulting.antonstockapp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.openerp.CreateMovesAsyncTask;
import com.openerp.ExcecuteFunctionAsyncTask;
import com.openerp.OpenErpHolder;

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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import ar.com.antaresconsulting.antonstockapp.adapters.BaseProductAdapter;
import ar.com.antaresconsulting.antonstockapp.adapters.EmpleadosAdapter;
import ar.com.antaresconsulting.antonstockapp.adapters.InsumoAdapter;
import ar.com.antaresconsulting.antonstockapp.listener.SwipeDismissListViewTouchListener;
import ar.com.antaresconsulting.antonstockapp.model.BaseProduct;
import ar.com.antaresconsulting.antonstockapp.model.Dimension;
import ar.com.antaresconsulting.antonstockapp.model.Empleado;
import ar.com.antaresconsulting.antonstockapp.model.Insumo;
import ar.com.antaresconsulting.antonstockapp.model.PickingMove;
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
	private CreateMovesAsyncTask saveData;

	
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
		int maxProds = this.productos.getAdapter().getCount();
		if(maxProds > 0){
			this.saveData = new CreateMovesAsyncTask(this);
			OpenErpHolder.getInstance().setmModelName("stock.move");
			this.saveData.setModelStockPicking("stock.picking");		
			PickingMove[] values;
			values = new PickingMove[1];
			values[0] = regMove(AntonConstants.PORDUCT_LOCATION_STOCK,AntonConstants.PORDUCT_LOCATION_OUTPUT);
			this.saveData.execute(values);
		}		
	}
	
	private PickingMove regMove(String loc_source,String loc_des){
		PickingMove pm = new PickingMove();
		HashMap<String, Object> headerPicking = new HashMap<String, Object>(); 
		
		headerPicking.put("partner_id", AntonConstants.ANTON_COMPANY_ID);
		headerPicking.put("type", AntonConstants.INTERNAL_PORDUCT_TYPE);
		headerPicking.put("auto_picking",true);
		headerPicking.put("company_id",AntonConstants.ANTON_COMPANY_ID);
		headerPicking.put("move_type",AntonConstants.DELIVERY_METHOD);
		headerPicking.put("state","draft");
		headerPicking.put("location_id",loc_source);
		headerPicking.put("location_dest_id",loc_des);			
		pm.setHeaderPicking(headerPicking);
		
		List<HashMap<String,Object>> moves = new ArrayList<HashMap<String, Object>>();
		int maxProds = this.productos.getAdapter().getCount();

		for (int i = 1; i <= maxProds; i++) {
			Insumo prod = (Insumo) this.productos.getAdapter().getItem(i-1);
			
			HashMap<String,Object> move = new HashMap<String,Object>();

			move.put("dimension_qty",prod.getCantidadReal());
			move.put("product_uos_qty",prod.getCantidadReal());
			move.put("partner_id",AntonConstants.ANTON_COMPANY_ID);
			move.put("product_id",prod.getId());
			move.put("product_uom",prod.getUom()[0]);
			move.put("location_id",loc_source);
			move.put("location_dest_id",loc_des);
			move.put("company_id",AntonConstants.ANTON_COMPANY_ID);
			move.put("prodlot_id",false);
			move.put("tracking_id",false);
			move.put("product_qty",prod.getCantidadReal());
			move.put("product_uos",false);
			move.put("type", AntonConstants.INTERNAL_PORDUCT_TYPE);
			move.put("picking_id",false);
			move.put("origin",false);
			move.put("state","draft");
			move.put("employee",prod.getEntregado().getId());
			move.put("name",prod.getNombre());
			moves.add(move);
			
		}
		pm.setMoves(moves);
		pm.setMoveType(AntonConstants.INTERNAL_PORDUCT_TYPE);
		return pm;
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
