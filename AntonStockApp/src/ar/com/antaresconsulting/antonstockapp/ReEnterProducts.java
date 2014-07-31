package ar.com.antaresconsulting.antonstockapp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.openerp.CreateMovesAsyncTask;
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
import ar.com.antaresconsulting.antonstockapp.adapters.BaseProductAdapter;
import ar.com.antaresconsulting.antonstockapp.adapters.PedidoLineaMPAdapter;
import ar.com.antaresconsulting.antonstockapp.listener.SwipeDismissListViewTouchListener;
import ar.com.antaresconsulting.antonstockapp.model.BaseProduct;
import ar.com.antaresconsulting.antonstockapp.model.Dimension;
import ar.com.antaresconsulting.antonstockapp.model.MateriaPrima;
import ar.com.antaresconsulting.antonstockapp.model.PickingMove;
import ar.com.antaresconsulting.antonstockapp.model.SelectionObject;
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
	private CreateMovesAsyncTask saveData;

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
		this.prodDao.getMateriaPrimaInProduction(AntonConstants.OUT_PORDUCT_LOCATION_PROD);

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
		this.saveData = new CreateMovesAsyncTask(this);
		OpenErpHolder.getInstance().setmModelName("stock.move");
		int maxProds = this.productos.getAdapter().getCount();
		PickingMove[] values = new PickingMove[1];
		values[0] = new PickingMove();

		HashMap<String, Object> headerPicking = new HashMap<String, Object>();
		String loc_source;
		String loc_destination;

		headerPicking.put("partner_id", AntonConstants.ANTON_COMPANY_ID);
		this.saveData.setModelStockPicking("stock.picking");
		loc_source = AntonConstants.OUT_PORDUCT_LOCATION_PROD;
		loc_destination = AntonConstants.OUT_PORDUCT_LOCATION_STOCK;			


		headerPicking.put("type", AntonConstants.INTERNAL_PORDUCT_TYPE);
		headerPicking.put("auto_picking",true);
		headerPicking.put("company_id",AntonConstants.ANTON_COMPANY_ID);
		headerPicking.put("move_type",AntonConstants.DELIVERY_METHOD);
		headerPicking.put("state","draft");
		headerPicking.put("location_id",loc_source);
		headerPicking.put("location_dest_id",loc_destination);			

		values[0].setHeaderPicking(headerPicking);

		//HashMap<String, Object>[] moves = new HashMap<String, Object>[3];
		List<HashMap<String,Object>> moves = new ArrayList<HashMap<String, Object>>();

		for (int i = 0; i < maxProds; i++) {
			BaseProduct prod = (BaseProduct) this.productos.getAdapter().getItem(i);
			MateriaPrima mp =  (MateriaPrima) prod;

			HashMap<String,Object> move = new HashMap<String,Object>();
//			if(mp.getDimension()!= null){
//				move.put("dimension", (Dimension) prod.getDimension()[0]);
//			}				
			move.put("dimension_qty",prod.getCantidad());
			move.put("product_uos_qty",prod.getCantidad());
			move.put("product_id",prod.getId());
			move.put("product_uom",prod.getUom()[0]);
			move.put("location_id",loc_source);
			move.put("location_dest_id",loc_destination);				
			move.put("company_id",AntonConstants.ANTON_COMPANY_ID);
			move.put("prodlot_id",false);
			move.put("tracking_id",false);
			move.put("product_qty",prod.getCantidad());
			move.put("product_uos",false);
			move.put("type",AntonConstants.INTERNAL_PORDUCT_TYPE);
			move.put("origin",false);
			move.put("state","draft");
			move.put("name",prod.getNombre());

			moves.add(move);
		}
		values[0].setMoves(moves);
		values[0].setMoveType(AntonConstants.INTERNAL_PORDUCT_TYPE);
		this.saveData.execute(values);
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
		if(((hight * width)* cantP) > prod.getCantidad().doubleValue()){
			Toast tt = Toast.makeText(this, "La superficie a reingresar debe ser menor a la superficie disponible", Toast.LENGTH_SHORT);
			tt.show();
			return;			
		}

		//		MateriaPrimaAdapter adapter = (MateriaPrimaAdapter) this.productos.getAdapter();
		//
		//		prod.setCantidad(cantP);
		//		prod.setDimH(String.valueOf(hight));
		//		prod.setDimW(String.valueOf(width));
		//		prod.setDimT(this.dimT.getSelectedItem().toString());
		//		prod.setDimTipo((SelectionObject) this.dimTipo.getSelectedItem());
		//		adapter.addProduct(prod);
		//		adapter.notifyDataSetChanged();		
	}

	@Override
	public void setMateriaPrima() {
		prodBuscaAdapter.addAll(this.prodDao.getMateriaPrimasList());			
	}



}
