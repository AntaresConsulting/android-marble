package ar.com.antaresconsulting.antonstockapp.outgoing;

import java.util.Calendar;
import java.util.Date;

import com.openerp.CreatePickingAsyncTask;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.app.Activity;
import android.app.DialogFragment;
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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;
import ar.com.antaresconsulting.antonstockapp.AntonLauncherActivity;
import ar.com.antaresconsulting.antonstockapp.AntonStockApp;
import ar.com.antaresconsulting.antonstockapp.R;
import ar.com.antaresconsulting.antonstockapp.adapters.PedidoLineaAdapter;
import ar.com.antaresconsulting.antonstockapp.listener.SwipeDismissListViewTouchListener;
import ar.com.antaresconsulting.antonstockapp.model.Bacha;
import ar.com.antaresconsulting.antonstockapp.model.BaseProduct;
import ar.com.antaresconsulting.antonstockapp.model.Dimension;
import ar.com.antaresconsulting.antonstockapp.model.Insumo;
import ar.com.antaresconsulting.antonstockapp.model.MateriaPrima;
import ar.com.antaresconsulting.antonstockapp.model.Partner;
import ar.com.antaresconsulting.antonstockapp.model.SelectionObject;
import ar.com.antaresconsulting.antonstockapp.model.StockMove;
import ar.com.antaresconsulting.antonstockapp.model.StockPicking;
import ar.com.antaresconsulting.antonstockapp.model.dao.BachasDAO;
import ar.com.antaresconsulting.antonstockapp.model.dao.InsumosDAO;
import ar.com.antaresconsulting.antonstockapp.model.dao.MateriaPrimaDAO;
import ar.com.antaresconsulting.antonstockapp.model.dao.PartnerDAO;
import ar.com.antaresconsulting.antonstockapp.model.dao.ProductDAO;
import ar.com.antaresconsulting.antonstockapp.popup.DatePickerPopupFragment;
import ar.com.antaresconsulting.antonstockapp.popup.ProductTypePopupFragment;
import ar.com.antaresconsulting.antonstockapp.popup.SearchBachaPopupFragment;
import ar.com.antaresconsulting.antonstockapp.popup.SearchInsumoPopupFragment;
import ar.com.antaresconsulting.antonstockapp.popup.SearchMPPopupFragment;
import ar.com.antaresconsulting.antonstockapp.popup.SearchServiciosPopupFragment;
import ar.com.antaresconsulting.antonstockapp.util.AntonConstants;

public class AddOEActivity extends ActionBarActivity implements ProductDAO.ServiciosCallbacks, PartnerDAO.ClientsCallbacks, MateriaPrimaDAO.MateriaPrimaCallbacks, InsumosDAO.InsumosCallbacks, BachasDAO.BachasCallbacks,SearchMPPopupFragment.SearchProductListener,SearchInsumoPopupFragment.SearchProductListener,SearchBachaPopupFragment.SearchProductListener,SearchServiciosPopupFragment.SearchProductListener, DatePickerPopupFragment.DatePickerListener{

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
	private boolean isExcecute=false;
	private Activity thisAct;
	private Date expectedDate;
	private Spinner esp;
	private CheckBox bachasCli;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.thisAct= this; 
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
			        	partDao = new PartnerDAO(thisAct);
			        	partDao.getAllCompanies(text);
			        }
		        }
		    }
		});		
		clientes.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				selectCliente = (Partner) arg0.getItemAtPosition(arg2);
				if((selectCliente != null) && selectCliente.getHasLoc() && (bachasCli != null) )
					bachasCli.setVisibility(View.VISIBLE);
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
							((PedidoLineaAdapter) prodsPedido.getAdapter()).delProduct((StockMove) prodsPedido.getAdapter().getItem(position));
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
		CreatePickingAsyncTask saveData = new CreatePickingAsyncTask(this);
		int maxProds = this.prodsPedido.getAdapter().getCount();		
		if(maxProds <= 0){
			Toast tt = Toast.makeText(this.getApplicationContext(), "Debe haber seleccionado almenos un producto!", Toast.LENGTH_SHORT);
			tt.show();		
			return;
		}
		
		Object[] cliente = new Object[1];
		cliente[0] = this.selectCliente;
		String origin = "";
		if(cliente == null){
			Toast tt = Toast.makeText(this.getApplicationContext(), "Debe definir un cliente!", Toast.LENGTH_SHORT);
			tt.show();		
			return;		
		}

		Integer loc_source = AntonStockApp.getExternalId(AntonConstants.PRODUCT_LOCATION_OUTPUT);
		Integer loc_destination = AntonStockApp.getExternalId(AntonConstants.PRODUCT_LOCATION_CUSTOMER);			
		
		StockPicking picking = new StockPicking(origin,AntonConstants.PICKING_TYPE_ID_OUT,cliente);

		for (int i = 0; i < maxProds; i++) {
			StockMove prod = (StockMove) this.prodsPedido.getAdapter().getItem(i);
			StockMove move = new StockMove(prod.getName(),prod.getProduct(), prod.getUom(), loc_source, loc_destination, origin, prod.getQty(),prod.getDimension(),prod.getQtytDim(),this.expectedDate);
			move.setUseClientLocation(prod.isUseClientLocation());
			picking.addMove(move);
		}
		saveData.execute(picking);	
		
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
		
		this.bachasCli =  (CheckBox) findViewById(R.id.bachasCli);
		if((this.selectCliente != null) && this.selectCliente.getHasLoc())
			this.bachasCli.setVisibility(View.VISIBLE);
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
		esp = (Spinner) findViewById(R.id.espesorPlaca);
		esp.setSelection(AntonConstants.DEFAULT_ESPESORES);			
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
		StockMove linea = new StockMove();
		EditText cant = (EditText) findViewById(R.id.cantProds);
		linea.setQty(Double.valueOf(cant.getText().toString()));
		linea.setName(prod.getNombre());
		linea.setUom(prod.getUom());
		if((this.bachasCli != null)&&(this.bachasCli.isChecked())){
			linea.setUseClientLocation(true);
		}
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
		StockMove linea = new StockMove();


		Spinner tipo = (Spinner) findViewById(R.id.tipoDim);
		linea.setQty(Double.valueOf(cant.getText().toString()));
		linea.setName(prod.getNombre());
		linea.setUom(prod.getUom());
		linea.setQtytDim(Integer.parseInt(cant.getText().toString()));
		Object[] prodData=new Object[1];
		prodData[0] = prod;
		linea.setProduct(prodData);
		Dimension dim = new Dimension();
		dim.setDimH(new Double(alto.getText().toString()));
		dim.setDimT(new Double((String)esp.getSelectedItem()));
		dim.setDimTipo(SelectionObject.getDimTipo((String)tipo.getSelectedItem()));
		dim.setDimW(new Double(ancho.getText().toString()));
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
	
	public void setFecha(View v) {
	    DialogFragment newFragment = new DatePickerPopupFragment();
	    newFragment.show(getFragmentManager(), "datePicker");
	}

	@Override
	public void setSelectedDate(int year, int month, int day) {
		Calendar c = Calendar.getInstance();
		c.set(year,month, day,0,0);
		this.expectedDate = c.getTime();		
	}
	
}

