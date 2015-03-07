package ar.com.antaresconsulting.antonstockapp.incoming;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.openerp.ConfirmMovesAsyncTask;
import com.openerp.CreatePickingAsyncTask;
import com.openerp.OpenErpHolder;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import ar.com.antaresconsulting.antonstockapp.AntonConstants;
import ar.com.antaresconsulting.antonstockapp.R;
import ar.com.antaresconsulting.antonstockapp.R.id;
import ar.com.antaresconsulting.antonstockapp.R.layout;
import ar.com.antaresconsulting.antonstockapp.R.string;
import ar.com.antaresconsulting.antonstockapp.adapters.PedidoLineaAdapter;
import ar.com.antaresconsulting.antonstockapp.listener.SwipeDismissListViewTouchListener;
import ar.com.antaresconsulting.antonstockapp.model.Bacha;
import ar.com.antaresconsulting.antonstockapp.model.Partner;
import ar.com.antaresconsulting.antonstockapp.model.Pedido;
import ar.com.antaresconsulting.antonstockapp.model.PedidoLinea;
import ar.com.antaresconsulting.antonstockapp.model.StockMove;
import ar.com.antaresconsulting.antonstockapp.model.StockPicking;
import ar.com.antaresconsulting.antonstockapp.model.dao.BachasDAO;
import ar.com.antaresconsulting.antonstockapp.model.dao.PartnerDAO;
import ar.com.antaresconsulting.antonstockapp.model.dao.PedidoDAO;
import ar.com.antaresconsulting.antonstockapp.popup.SearchBachaPopupFragment;
import ar.com.antaresconsulting.antonstockapp.popup.SearchMPPopupFragment;

/**
 * A placeholder fragment containing a simple view.
 */
public class InBachaProductCliFragment extends Fragment implements  SearchBachaPopupFragment.SearchProductListener, PartnerDAO.ClientsCallbacks,OnItemSelectedListener, InProductActions,BachasDAO.BachasCallbacks, PedidoDAO.PedidosCallbacks{

	private BachasDAO baDao;
	private PedidoDAO pedDao;
	private PartnerDAO partDao;

	private Spinner pedidos;

	private ListView productos;
	private ListView productosDispo;

	private EditText cantPlacas;
	private boolean isExcecute=false;
	private TextView provee;
	private AutoCompleteTextView cliente;
	private Fragment thisFrag;
	private Partner selectCliente;

	private static final String ARG_PARAM1 = "param1";

	// TODO: Rename and change types of parameters
	private int mParam1;
	public InBachaProductCliFragment() {

	}

	public static InBachaProductCliFragment newInstance(int param1) {
		InBachaProductCliFragment fragment = new InBachaProductCliFragment();
		Bundle args = new Bundle();
		args.putInt(ARG_PARAM1, param1);
		fragment.setArguments(args);
		return fragment;
	}


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			mParam1 = getArguments().getInt(ARG_PARAM1);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = null;
		this.baDao = new BachasDAO(this);
		rootView = inflater.inflate(R.layout.fragment_in_products_bachas_cli,	container, false);



		this.productosDispo = (ListView) rootView.findViewById(R.id.productosDispo);
		this.productosDispo.setChoiceMode(ListView.CHOICE_MODE_SINGLE);			
		this.cantPlacas = (EditText) rootView.findViewById(R.id.clientesList);
		this.productos = (ListView) rootView.findViewById(R.id.productosPedido);
		this.productos.setAdapter(new PedidoLineaAdapter(this.getActivity()));

		thisFrag = this;
		this.cliente = (AutoCompleteTextView) rootView.findViewById(R.id.clienteNombre);
		this.cliente.addTextChangedListener(new TextWatcher() {
			@Override
			public void afterTextChanged(Editable editable) {
			}
			@Override
			public void beforeTextChanged(CharSequence charSequence, int arg1, int arg2, int arg3) {
			}

			@Override
			public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
				String text=charSequence.toString();
				if(!cliente.isPopupShowing()){
					if(isExcecute)
						return ;
					if (text.length() > AntonConstants.MIN_CHAR_LENGTH) {
						isExcecute = true;
						partDao = new PartnerDAO(thisFrag);
						partDao.getAllCompanies(text);
					}
				}
			}
		});			
		this.cliente.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				selectCliente = (Partner) arg0.getItemAtPosition(arg2);
			}
		});			


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
							((PedidoLineaAdapter) productos.getAdapter()).delLinea((PedidoLinea) productos.getAdapter().getItem(position));
						}
						((PedidoLineaAdapter) productos.getAdapter()).notifyDataSetChanged();
					}
				});
		this.productos.setOnTouchListener(touchListener);
		this.productos.setOnScrollListener(touchListener.makeScrollListener());

		return rootView;
	}


	public void popSearchMP(View view) {
		SearchMPPopupFragment popconf = new SearchMPPopupFragment();
		popconf.setTargetFragment(this, 1234);
		popconf.show(getFragmentManager(),"Server_Search");
	}



	public void addProduct(View view) {
		boolean cancel = false;
		View focus = null; 
		if(this.cantPlacas.getText().toString().trim().equalsIgnoreCase("")){
			this.cantPlacas.setError(getString(R.string.error_field_required));
			cancel = true;
			focus = this.cantPlacas;
		}

		if(cancel){
			focus.requestFocus();
			return;
		}

		int position =  productosDispo.getCheckedItemPosition();
		if(position == AdapterView.INVALID_POSITION){
			Toast tt = Toast.makeText(this.getActivity(), "Primero debe seleccionar un producto", Toast.LENGTH_SHORT);
			tt.show();
			return;
		}
		Bacha prod = (Bacha) this.productosDispo.getAdapter().getItem(this.productosDispo.getCheckedItemPosition());
		PedidoLineaAdapter adapter = (PedidoLineaAdapter) this.productos.getAdapter();

		PedidoLinea linea = new PedidoLinea();
		linea.setCant(Double.valueOf(cantPlacas.getText().toString()));
		linea.setNombre(prod.getNombre());
		linea.setUom(prod.getUom());
		Object[] prodData=new Object[1];
		prodData[0] = prod;
		linea.setProduct(prodData);
		adapter.addLinea(linea);
		adapter.notifyDataSetChanged();			

	}

	@Override
	public void setBachas() {
		this.productosDispo.setAdapter(new ArrayAdapter<Bacha>(this.getActivity(),android.R.layout.simple_list_item_single_choice,this.baDao.getBachasList()));	
	}


	public void confirmStock() {

		CreatePickingAsyncTask saveData = new CreatePickingAsyncTask(this.getActivity());
		int maxProds = this.productos.getAdapter().getCount();

		Partner proveedor = this.selectCliente;
		String origin = "";

		String loc_source = AntonConstants.PRODUCT_LOCATION_SUPPLIER;
		String loc_destination = AntonConstants.PRODUCT_LOCATION_STOCK;			

		StockPicking picking = new StockPicking(origin,AntonConstants.PICKING_TYPE_ID_IN,proveedor.getId(),loc_source,loc_destination,AntonConstants.BACHA_PICKING);

		for (int i = 0; i < maxProds; i++) {
			PedidoLinea prod = (PedidoLinea) this.productos.getAdapter().getItem(i);
			StockMove move = new StockMove(prod.getNombre(),((Bacha)prod.getProduct()[0]).getId(), (Integer)prod.getUom()[0], loc_source, loc_destination, origin, prod.getCant());				
			picking.addMove(move);
		}
		saveData.execute(picking);	


	}

	@Override
	public void setPedidos() {
		this.pedidos.setAdapter(new ArrayAdapter<Pedido>(this.getActivity(),android.R.layout.simple_list_item_1,this.pedDao.getPedidoList()));
	}

	@Override
	public void searchByEAN(String scanContent) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int pos,long arg3) {
		Pedido ped = (Pedido) arg0.getItemAtPosition(pos);
		this.provee.setText((String) ped.getPartner()[1]);
		this.pedDao = new PedidoDAO(this);
		this.pedDao.getMoveByPed(ped.getId());
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setPedidosLineas() {
		this.productosDispo.setAdapter(new ArrayAdapter<PedidoLinea>(this.getActivity(),android.R.layout.simple_list_item_single_choice,this.pedDao.getPedidoLineaList()));		
	}

	@Override
	public void setClients() {
		ArrayAdapter<Partner> adapter = new ArrayAdapter<Partner>(this.getActivity(), android.R.layout.simple_list_item_1, this.partDao.getPartnersArray());
		this.cliente.setAdapter(adapter);
		adapter.notifyDataSetChanged();
		this.cliente.showDropDown();
		isExcecute= false;
	}

	@Override
	public void setClientDetail() {
		// TODO Auto-generated method stub

	}

	public void searchProduct() {
		SearchBachaPopupFragment popconf = new SearchBachaPopupFragment();
		popconf.setTargetFragment(this, 1234);
		popconf.show(getFragmentManager(),"Server_Search");		
	}

	@Override
	public void searchProductsBacha() {
		SharedPreferences sharedPref = getActivity().getSharedPreferences(getString(R.string.saved_bacha_search),Context.MODE_PRIVATE);
		String tipo = sharedPref.getString(getString(R.string.search_bacha_material),"");
		String marca = sharedPref.getString(getString(R.string.search_bacha_marca),"");
		String nombreProd = sharedPref.getString(getString(R.string.search_bacha_nombre),"");
		String tbacha = sharedPref.getString(getString(R.string.search_bacha_tipo),"");
		String acero = sharedPref.getString(getString(R.string.search_bacha_acero),"");
		String colocacion = sharedPref.getString(getString(R.string.search_bacha_colocacion),"");


		this.baDao = new BachasDAO(this);
		this.baDao.getBachasProducts(tipo, marca, nombreProd,tbacha,acero,colocacion);			
	}

}
