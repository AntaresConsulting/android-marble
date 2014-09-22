package ar.com.antaresconsulting.antonstockapp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.openerp.CreateMovesAsyncTask;
import com.openerp.OpenErpHolder;

import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;
import ar.com.antaresconsulting.antonstockapp.adapters.PedidoLineaAdapter;
import ar.com.antaresconsulting.antonstockapp.listener.SwipeDismissListViewTouchListener;
import ar.com.antaresconsulting.antonstockapp.model.Bacha;
import ar.com.antaresconsulting.antonstockapp.model.BaseProduct;
import ar.com.antaresconsulting.antonstockapp.model.Dimension;
import ar.com.antaresconsulting.antonstockapp.model.Insumo;
import ar.com.antaresconsulting.antonstockapp.model.MateriaPrima;
import ar.com.antaresconsulting.antonstockapp.model.Partner;
import ar.com.antaresconsulting.antonstockapp.model.PedidoLinea;
import ar.com.antaresconsulting.antonstockapp.model.PickingMove;
import ar.com.antaresconsulting.antonstockapp.model.SelectionObject;
import ar.com.antaresconsulting.antonstockapp.model.dao.BachasDAO;
import ar.com.antaresconsulting.antonstockapp.model.dao.InsumosDAO;
import ar.com.antaresconsulting.antonstockapp.model.dao.MateriaPrimaDAO;
import ar.com.antaresconsulting.antonstockapp.model.dao.PartnerDAO;
import ar.com.antaresconsulting.antonstockapp.model.dao.ProductDAO;

public class AddOEActivity extends ActionBarActivity implements ProductDAO.ServiciosCallbacks, PartnerDAO.ClientsCallbacks, MateriaPrimaDAO.MateriaPrimaCallbacks, InsumosDAO.InsumosCallbacks, BachasDAO.BachasCallbacks,SearchMPPopupFragment.SearchProductListener,SearchInsumoPopupFragment.SearchProductListener,SearchBachaPopupFragment.SearchProductListener,SearchServiciosPopupFragment.SearchProductListener{

	private ProductTypePopupFragment popconf;
	private BachasDAO baDao;
	private ProductDAO prodDao;
	private InsumosDAO insuDao;
	private MateriaPrimaDAO mpDao;
	private ListView prodsDispo;
	private ListView prodsPedido;
	private PartnerDAO partDao;
	private Partner selectCliente;
	private AutoCompleteTextView clientes;
	private CreateMovesAsyncTask saveData;
	private boolean isExcecute=false;
	private Fragment thisFrag;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_oe);
		clientes = (AutoCompleteTextView) findViewById(R.id.clienteCombo);
		clientes.addTextChangedListener(new TextWatcher() {
		    @Override
		    public void afterTextChanged(Editable editable) {
		    }
		    @Override
		    public void beforeTextChanged(CharSequence charSequence, int arg1, int arg2, int arg3) {
		    }

		    @Override
		    public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
		        String text=charSequence.toString();
		        if(!clientes.isPopupShowing()){
			        if(isExcecute)
			        	return ;
			        if (text.length() > 3) {
			        	isExcecute = true;
			        	partDao = new PartnerDAO(thisFrag);
			        	partDao.getAllCompanies(text);
			        }
		        }
		    }
		});		
		clientes.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				selectCliente = (Partner) arg0.getItemAtPosition(arg2);
			}
		});		
		prodsDispo = (ListView) findViewById(R.id.productosDispo);
		prodsPedido = (ListView) findViewById(R.id.productosPedido);
		prodsPedido.setAdapter(new PedidoLineaAdapter(this));
		prodsDispo.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		partDao = new PartnerDAO(this);
		partDao.getAllCompanies();
		SwipeDismissListViewTouchListener touchListener = new SwipeDismissListViewTouchListener(
				this.prodsPedido,
				new SwipeDismissListViewTouchListener.DismissCallbacks() {
					@Override
					public boolean canDismiss(int position) {
						return true;
					}

					@Override
					public void onDismiss(ListView listView,
							int[] reverseSortedPositions) {
						for (int position : reverseSortedPositions) {
							((PedidoLineaAdapter) prodsPedido.getAdapter()).delProduct((PedidoLinea) prodsPedido.getAdapter().getItem(position));
						}
						((PedidoLineaAdapter) prodsPedido.getAdapter()).notifyDataSetChanged();
					}
				});
		this.prodsPedido.setOnTouchListener(touchListener);
		this.prodsPedido.setOnScrollListener(touchListener.makeScrollListener());				
		if (savedInstanceState == null) {
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.add_oe, menu);
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

	public void searchOE(MenuItem menu) {
		popconf = new ProductTypePopupFragment();
		popconf.show(getFragmentManager(),"Server_Search2");			
	}

	public void scannOE(MenuItem menu) {

	}

	public void addOE(MenuItem menu) {
		this.saveData = new CreateMovesAsyncTask(this);
		OpenErpHolder.getInstance().setmModelName("stock.move");
		int maxProds = this.prodsPedido.getAdapter().getCount();
		PickingMove[] values = new PickingMove[1];
		values[0] = new PickingMove();

		Partner cliente = this.selectCliente;
		HashMap<String, Object> headerPicking = new HashMap<String, Object>();
		String loc_source;
		String loc_destination;

		headerPicking.put("partner_id", cliente.getId());
		this.saveData.setModelStockPicking("stock.picking.out");
		loc_source = AntonConstants.PORDUCT_LOCATION_OUTPUT;
		loc_destination = AntonConstants.PORDUCT_LOCATION_CUSTOMER;			

		String origin = "";

		headerPicking.put("type", AntonConstants.OUT_PORDUCT_TYPE);
		headerPicking.put("auto_picking",false);
		headerPicking.put("company_id",AntonConstants.ANTON_COMPANY_ID);
		headerPicking.put("move_type",AntonConstants.DELIVERY_METHOD);
		headerPicking.put("state","draft");
		headerPicking.put("origin",origin);
		headerPicking.put("location_id",loc_source);
		headerPicking.put("location_dest_id",loc_destination);			

		values[0].setHeaderPicking(headerPicking);

		List<HashMap<String,Object>> moves = new ArrayList<HashMap<String, Object>>();
		for (int i = 0; i < maxProds; i++) {
			PedidoLinea prod = (PedidoLinea) this.prodsPedido.getAdapter().getItem(i);
			HashMap<String,Object> move = new HashMap<String,Object>();
			if(prod.getDimension()!= null){
				move.put("dimension", (Dimension) prod.getDimension()[0]);
			}				
			move.put("dimension_qty",new Integer(prod.getCant().intValue()));
			move.put("product_uos_qty",prod.getCant());
			move.put("product_id",((BaseProduct)prod.getProduct()[0]).getId());
			move.put("product_uom",prod.getUom()[0]);
			move.put("location_id",loc_source);
			move.put("location_dest_id",loc_destination);				
			move.put("company_id",AntonConstants.ANTON_COMPANY_ID);
			move.put("prodlot_id",false);
			move.put("tracking_id",false);
			move.put("product_qty",prod.getCant());
			move.put("product_uos",prod.getUom()[0]);
			move.put("type",AntonConstants.OUT_PORDUCT_TYPE);
			move.put("origin",origin);
			move.put("state","draft");
			move.put("name",prod.getNombre());
			moves.add(move);
		}
		values[0].setMoves(moves);
		values[0].setMoveType(AntonConstants.OUT_PORDUCT_TYPE);
		values[0].setRegBalanace(false);
		this.saveData.execute(values);		
	}	

	public void setMP(View view){
		popconf.dismiss();
		SearchMPPopupFragment popconf = new SearchMPPopupFragment();
		popconf.show(getFragmentManager(),"Server_Search");				
	}

	public void setInsumos(View view){
		popconf.dismiss();
		SearchInsumoPopupFragment popconf = new SearchInsumoPopupFragment();
		popconf.show(getFragmentManager(),"Server_Search");				
	}

	public void setBachas(View view){
		popconf.dismiss();
		SearchBachaPopupFragment popconf = new SearchBachaPopupFragment();
		popconf.show(getFragmentManager(),"Server_Search");		
	}

	public void setServicios(View view){
		popconf.dismiss();
		SearchServiciosPopupFragment popconf = new SearchServiciosPopupFragment();
		popconf.show(getFragmentManager(),"Server_Search");		
	}
	@Override
	public void searchProductsBacha() {
		SharedPreferences sharedPref = this.getSharedPreferences(getString(R.string.saved_bacha_search),Context.MODE_PRIVATE);
		String tipo = sharedPref.getString(getString(R.string.search_bacha_material),"");
		String marca = sharedPref.getString(getString(R.string.search_bacha_marca),"");
		String nombreProd = sharedPref.getString(getString(R.string.search_bacha_nombre),"");
		String tbacha = sharedPref.getString(getString(R.string.search_bacha_tipo),"");
		String acero = sharedPref.getString(getString(R.string.search_bacha_acero),"");
		String colocacion = sharedPref.getString(getString(R.string.search_bacha_colocacion),"");

		this.baDao = new BachasDAO(this);
		this.baDao.getBachasProducts(tipo, marca, nombreProd,tbacha,acero,colocacion);		
	}

	@Override
	public void searchProductsInsumos() {
		SharedPreferences sharedPref = this.getSharedPreferences(getString(R.string.saved_insumo_search),Context.MODE_PRIVATE);
		String nombreProd = sharedPref.getString(getString(R.string.search_nombre),"");

		this.insuDao = new InsumosDAO(this);
		this.insuDao.getInsumos(nombreProd);		
	}

	@Override
	public void searchProductsServicios() {
		SharedPreferences sharedPref = this.getSharedPreferences(getString(R.string.saved_servicios_search),Context.MODE_PRIVATE);
		String nombreProd = sharedPref.getString(getString(R.string.search_nombre),"");

		this.prodDao = new ProductDAO(this);
		this.prodDao.getServicios(nombreProd);		
	}
	
	@Override
	public void searchProductsMP() {
		SharedPreferences sharedPref = this.getSharedPreferences(getString(R.string.saved_mp_search),Context.MODE_PRIVATE);
		String tm = sharedPref.getString(getString(R.string.search_tipo),"");
		String color = sharedPref.getString(getString(R.string.search_color),"");
		String acabado = sharedPref.getString(getString(R.string.search_terminado),"");
		String nombreProd = sharedPref.getString(getString(R.string.search_nombre),"");

		this.mpDao = new MateriaPrimaDAO(this);
		this.mpDao.getMarmolesProducts(tm,color,acabado,nombreProd);			
	}

	@Override
	public void setBachas() {
		ArrayAdapter<Bacha> adapterAux = new ArrayAdapter<Bacha>(this,android.R.layout.simple_list_item_single_choice);
		prodsDispo.setAdapter(adapterAux);
		adapterAux.addAll(this.baDao.getBachasList());
		((FrameLayout) findViewById(R.id.addProductContainer)).removeAllViews();
		View.inflate(this, R.layout.fragment_add_product_action, (ViewGroup) findViewById(R.id.addProductContainer));		
	}

	@Override
	public void setInsumos() {
		ArrayAdapter<Insumo> adapterAux = new ArrayAdapter<Insumo>(this,android.R.layout.simple_list_item_single_choice);
		prodsDispo.setAdapter(adapterAux);
		adapterAux.addAll(this.insuDao.getInsumosList());
		((FrameLayout) findViewById(R.id.addProductContainer)).removeAllViews();
		View.inflate(this, R.layout.fragment_add_product_action, (ViewGroup) findViewById(R.id.addProductContainer));		
	}

	@Override
	public void setMateriaPrima() {
		ArrayAdapter<MateriaPrima> adapterAux = new ArrayAdapter<MateriaPrima>(this,android.R.layout.simple_list_item_single_choice);
		prodsDispo.setAdapter(adapterAux);
		adapterAux.addAll(this.mpDao.getMateriaPrimasList());
		((FrameLayout) findViewById(R.id.addProductContainer)).removeAllViews();		
		View.inflate(this, R.layout.fragment_add_mp_action, (ViewGroup) findViewById(R.id.addProductContainer));
	}

	@Override
	public void setClients() {
		ArrayAdapter<Partner> adapter = new ArrayAdapter<Partner>(this, android.R.layout.simple_list_item_1, this.partDao.getPartnersArray());
		this.clientes.setAdapter(adapter);
		adapter.notifyDataSetChanged();
		this.clientes.showDropDown();
		isExcecute= false;	}

	@Override
	public void setClientDetail() {
		// TODO Auto-generated method stub

	}	

	public void addProduct(View view) {
		PedidoLineaAdapter adapt =  (PedidoLineaAdapter) this.prodsPedido.getAdapter();
		int position =  this.prodsDispo.getCheckedItemPosition();
		if(position == AdapterView.INVALID_POSITION){
			Toast tt = Toast.makeText(this, "Primero debe seleccionar un producto", Toast.LENGTH_SHORT);
			tt.show();
			return;
		}		
		BaseProduct prod = (BaseProduct) this.prodsDispo.getAdapter().getItem(this.prodsDispo.getCheckedItemPosition());
		PedidoLinea linea = new PedidoLinea();
		EditText cant = (EditText) findViewById(R.id.cantProds);
		linea.setCant(Double.valueOf(cant.getText().toString()));
		linea.setNombre(prod.getNombre());
		linea.setUom(prod.getUom());
		Object[] prodData=new Object[1];
		prodData[0] = prod;
		linea.setProduct(prodData);
		adapt.addLinea(linea);
		adapt.notifyDataSetChanged();		
	}

	public void addProductMP(View view) {
		boolean cancel = false;
		EditText cant = (EditText) findViewById(R.id.cantProds);
		EditText ancho = (EditText) findViewById(R.id.anchoPlaca);
		EditText alto = (EditText) findViewById(R.id.altoPlaca);		
		View focus = null; 
		if(cant.getText().toString().trim().equalsIgnoreCase("")){
			cant.setError(getString(R.string.error_field_required));
			cancel = true;
			focus = cant;
		}

		if(ancho.getText().toString().trim().equalsIgnoreCase("")){
			ancho.setError(getString(R.string.error_field_required));
			cancel = true;
			focus = ancho;
		}
		if(alto.getText().toString().trim().equalsIgnoreCase("")){
			alto.setError(getString(R.string.error_field_required));
			cancel = true;
			focus = alto;
		}

		if(cancel){
			focus.requestFocus();
			return;
		}		
		PedidoLineaAdapter adapt =  (PedidoLineaAdapter) this.prodsPedido.getAdapter();
		int position =  prodsDispo.getCheckedItemPosition();
		if(position == AdapterView.INVALID_POSITION){
			Toast tt = Toast.makeText(this, "Primero debe seleccionar un producto", Toast.LENGTH_SHORT);
			tt.show();
			return;
		}
		
		MateriaPrima prod = (MateriaPrima) this.prodsDispo.getAdapter().getItem(this.prodsDispo.getCheckedItemPosition());
		PedidoLinea linea = new PedidoLinea();

		Spinner esp = (Spinner) findViewById(R.id.espesorPlaca);
		esp.setSelection(AntonConstants.DEFAULT_ESPESORES);	
		Spinner tipo = (Spinner) findViewById(R.id.tipoDim);
		linea.setCant(Double.valueOf(cant.getText().toString()));
		linea.setNombre(prod.getNombre());
		linea.setUom(prod.getUom());
		linea.setCantDim(Integer.parseInt(cant.getText().toString()));
		Object[] prodData=new Object[1];
		prodData[0] = prod;
		linea.setProduct(prodData);
		Dimension dim = new Dimension();
		dim.setDimH(alto.getText().toString());
		dim.setDimT((String)esp.getSelectedItem());
		dim.setDimTipo(SelectionObject.getDimTipo((String)tipo.getSelectedItem()));
		dim.setDimW(ancho.getText().toString());
		linea.setDimension(dim);
		adapt.addLinea(linea);
		adapt.notifyDataSetChanged();
	}

	@Override
	public void setServicios() {
		ArrayAdapter<BaseProduct> adapterAux = new ArrayAdapter<BaseProduct>(this,android.R.layout.simple_list_item_single_choice);
		prodsDispo.setAdapter(adapterAux);
		adapterAux.addAll(this.prodDao.getServiciosList());
		((FrameLayout) findViewById(R.id.addProductContainer)).removeAllViews();
		View.inflate(this, R.layout.fragment_add_product_action, (ViewGroup) findViewById(R.id.addProductContainer));			
	}

}

