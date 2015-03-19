package ar.com.antaresconsulting.antonstockapp.incoming;

import java.util.Calendar;
import java.util.Date;

import com.openerp.CreatePickingAsyncTask;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;
import ar.com.antaresconsulting.antonstockapp.AntonStockApp;
import ar.com.antaresconsulting.antonstockapp.R;
import ar.com.antaresconsulting.antonstockapp.R.id;
import ar.com.antaresconsulting.antonstockapp.R.layout;
import ar.com.antaresconsulting.antonstockapp.R.string;
import ar.com.antaresconsulting.antonstockapp.adapters.PedidoLineaAdapter;
import ar.com.antaresconsulting.antonstockapp.listener.SwipeDismissListViewTouchListener;
import ar.com.antaresconsulting.antonstockapp.model.Bacha;
import ar.com.antaresconsulting.antonstockapp.model.Insumo;
import ar.com.antaresconsulting.antonstockapp.model.Partner;
import ar.com.antaresconsulting.antonstockapp.model.PedidoLinea;
import ar.com.antaresconsulting.antonstockapp.model.StockMove;
import ar.com.antaresconsulting.antonstockapp.model.StockPicking;
import ar.com.antaresconsulting.antonstockapp.model.dao.BachasDAO;
import ar.com.antaresconsulting.antonstockapp.model.dao.PartnerDAO;
import ar.com.antaresconsulting.antonstockapp.popup.SearchBachaPopupFragment;
import ar.com.antaresconsulting.antonstockapp.util.AntonConstants;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass. Use the
 * {@link AddPMBachaFragment#newInstance} factory method to create an instance
 * of this fragment.
 * 
 */
public class AddPMBachaFragment extends Fragment implements AddPMActions,BachasDAO.BachasCallbacks,PartnerDAO.SuppliersCallbacks,SearchBachaPopupFragment.SearchProductListener {

	private BachasDAO baDao;
	private PartnerDAO partDao;
	private Date arrivalDate;

	private EditText cant;
	private EditText pl;
	private Spinner proveedor;
	private ListView prodsPedido;
	private ListView prodsDispo;

	public static AddPMBachaFragment newInstance() {
		AddPMBachaFragment fragment = new AddPMBachaFragment();

		return fragment;
	}

	public AddPMBachaFragment() {
		// Required empty public constructor
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {

		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View root = inflater.inflate(R.layout.fragment_add_pmbacha, container, false);
		cant = (EditText) root.findViewById(R.id.cantidadProducto);
		pl = (EditText) root.findViewById(R.id.pickingList);
		proveedor = (Spinner) root.findViewById(R.id.proveedorList);
		prodsPedido = (ListView) root.findViewById(R.id.productosPedido);
		prodsPedido.setAdapter(new PedidoLineaAdapter(getActivity()));
		prodsDispo = (ListView) root.findViewById(R.id.productosDispo);
		prodsDispo.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		prodsDispo.setAdapter(new ArrayAdapter<Bacha>(this.getActivity(),android.R.layout.simple_list_item_single_choice));
		partDao = new PartnerDAO(this);
		partDao.getAllSuppliers();
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
		return root;
	}

	@Override
	public void searchProduct() {
		SearchBachaPopupFragment popconf = new SearchBachaPopupFragment();
		popconf.setTargetFragment(this, 1234);
		popconf.show(getFragmentManager(),"Server_Search");		
	}

	@Override
	public void addPM() {
		CreatePickingAsyncTask saveData = new CreatePickingAsyncTask(this.getActivity());
		int maxProds = this.prodsPedido.getAdapter().getCount();		
		if(maxProds <= 0){
			Toast tt = Toast.makeText(this.getActivity().getApplicationContext(), "Debe haber seleccionado almenos un producto!", Toast.LENGTH_SHORT);
			tt.show();		
			return;
		}
		
		Partner proveedor = (Partner) this.proveedor.getSelectedItem();
		String origin = pl.getText().toString();

		Integer loc_source = AntonStockApp.getExternalId(AntonConstants.PRODUCT_LOCATION_SUPPLIER);
		
		StockPicking picking = new StockPicking(origin,AntonConstants.PICKING_TYPE_ID_IN,proveedor.getId(),AntonConstants.BACHA_PICKING);

		for (int i = 0; i < maxProds; i++) {			
			PedidoLinea prod = (PedidoLinea) this.prodsPedido.getAdapter().getItem(i);
			Bacha prodObj = (Bacha)prod.getProduct()[0];
			Integer loc_destination = (Integer) prodObj.getLocId()[0];				
			StockMove move = new StockMove(prod.getNombre(),((Bacha)prod.getProduct()[0]).getId(), (Integer)prod.getUom()[0], loc_source, loc_destination, origin, prod.getCant(),this.arrivalDate);				
			picking.addMove(move);
		}
		saveData.execute(picking);	
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


	@Override
	public void setBachas() {
		ArrayAdapter<Bacha> adapterAux = (ArrayAdapter<Bacha>) this.prodsDispo.getAdapter();
		adapterAux.clear();
		adapterAux.addAll(this.baDao.getBachasList());
		adapterAux.notifyDataSetChanged();			
	}

	@Override
	public void setSuppliers() {
		proveedor.setAdapter(new ArrayAdapter<Partner>(this.getActivity(),android.R.layout.simple_list_item_1,this.partDao.getPartnersArray()));
	}


	@Override
	public void setLineaPedido() {
		PedidoLineaAdapter adapt =  (PedidoLineaAdapter) prodsPedido.getAdapter();
		int position =  prodsDispo.getCheckedItemPosition();		
		if(position == AdapterView.INVALID_POSITION){
			Toast tt = Toast.makeText(this.getActivity(), "Primero debe seleccionar un producto", Toast.LENGTH_SHORT);
			tt.show();
			return;
		}		
		boolean cancel = false;
		View focus = null; 
		if(this.cant.getText().toString().trim().equalsIgnoreCase("")){
			this.cant.setError(getString(R.string.error_field_required));
			cancel = true;
			focus = this.cant;
		}
		if(cancel){
			focus.requestFocus();
			return;
		}		
		Bacha prod = (Bacha) this.prodsDispo.getAdapter().getItem(this.prodsDispo.getCheckedItemPosition());
		PedidoLinea linea = new PedidoLinea();
		linea.setCant(Double.valueOf(cant.getText().toString()));
		linea.setNombre(prod.getNombre());
		linea.setUom(prod.getUom());
		Object[] prodData=new Object[1];
		prodData[0] = prod;
		linea.setProduct(prodData);
		adapt.addLinea(linea);
		adapt.notifyDataSetChanged();
	}

	@Override
	public void setSuppliersProd() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setDate(int y, int m, int d) {
		Calendar c = Calendar.getInstance();
		c.set(y, m, d,0,0);
		this.arrivalDate = c.getTime();
	}


}
