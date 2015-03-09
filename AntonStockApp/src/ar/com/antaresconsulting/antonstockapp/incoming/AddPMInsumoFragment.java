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
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import ar.com.antaresconsulting.antonstockapp.R;
import ar.com.antaresconsulting.antonstockapp.R.id;
import ar.com.antaresconsulting.antonstockapp.R.layout;
import ar.com.antaresconsulting.antonstockapp.R.string;
import ar.com.antaresconsulting.antonstockapp.adapters.PedidoLineaAdapter;
import ar.com.antaresconsulting.antonstockapp.listener.SwipeDismissListViewTouchListener;
import ar.com.antaresconsulting.antonstockapp.model.Insumo;
import ar.com.antaresconsulting.antonstockapp.model.Partner;
import ar.com.antaresconsulting.antonstockapp.model.PedidoLinea;
import ar.com.antaresconsulting.antonstockapp.model.StockMove;
import ar.com.antaresconsulting.antonstockapp.model.StockPicking;
import ar.com.antaresconsulting.antonstockapp.model.dao.InsumosDAO;
import ar.com.antaresconsulting.antonstockapp.model.dao.PartnerDAO;
import ar.com.antaresconsulting.antonstockapp.popup.SearchInsumoPopupFragment;
import ar.com.antaresconsulting.antonstockapp.util.AntonConstants;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass. Use the
 * {@link AddPMInsumoFragment#newInstance} factory method to create an instance
 * of this fragment.
 * 
 */
public class AddPMInsumoFragment extends Fragment implements AddPMActions,InsumosDAO.InsumosCallbacks,PartnerDAO.SuppliersCallbacks,SearchInsumoPopupFragment.SearchProductListener {

	private InsumosDAO insuDao;
	private PartnerDAO partDao;

	private EditText cant;
	private EditText pl;
	private Spinner proveedor;
	private ListView prodsPedido;
	private ListView prodsDispo;
	private TextView unidades;
	private Date arrivalDate;
	
	public static AddPMInsumoFragment newInstance() {
		AddPMInsumoFragment fragment = new AddPMInsumoFragment();

		return fragment;
	}

	public AddPMInsumoFragment() {
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
		View root = inflater.inflate(R.layout.fragment_add_pminsumo, container, false);
		cant = (EditText) root.findViewById(R.id.cantidadProducto);
		pl = (EditText) root.findViewById(R.id.pickingList);
		unidades = (TextView) root.findViewById(R.id.unidadesProd);
		proveedor = (Spinner) root.findViewById(R.id.proveedorList);
		prodsPedido = (ListView) root.findViewById(R.id.productosPedido);
		prodsPedido.setAdapter(new PedidoLineaAdapter(getActivity()));
		prodsDispo = (ListView) root.findViewById(R.id.productosDispo);
		prodsDispo.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		prodsDispo.setAdapter(new ArrayAdapter<Insumo>(this.getActivity(),android.R.layout.simple_list_item_single_choice));
		prodsDispo.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Insumo insu =  (Insumo) prodsDispo.getAdapter().getItem(arg2);
				unidades.setText((String)insu.getUom()[1]);
								
			}			
		});
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
		SearchInsumoPopupFragment popconf = new SearchInsumoPopupFragment();
		popconf.setTargetFragment(this, 1234);
		popconf.show(getFragmentManager(),"Server_Search");		
	}

	@Override
	public void addPM() {
		CreatePickingAsyncTask saveData = new CreatePickingAsyncTask(this.getActivity());
		int maxProds = this.prodsPedido.getAdapter().getCount();		

		Partner proveedor = (Partner) this.proveedor.getSelectedItem();
		String origin = pl.getText().toString();

		String loc_source = AntonConstants.PRODUCT_LOCATION_SUPPLIER;
		String loc_destination = AntonConstants.PRODUCT_LOCATION_STOCK;			
		
		StockPicking picking = new StockPicking(origin,AntonConstants.PICKING_TYPE_ID_IN,proveedor.getId(),loc_source,loc_destination,AntonConstants.INSU_PICKING);

		for (int i = 0; i < maxProds; i++) {
			PedidoLinea prod = (PedidoLinea) this.prodsPedido.getAdapter().getItem(i);
			StockMove move = new StockMove(prod.getNombre(),((Insumo)prod.getProduct()[0]).getId(), (Integer)prod.getUom()[0], loc_source, loc_destination, origin, prod.getCant(),this.arrivalDate);				
			picking.addMove(move);
		}
		saveData.execute(picking);				
	}

	@Override
	public void setInsumos() {
		ArrayAdapter<Insumo> adapterAux = (ArrayAdapter<Insumo>) this.prodsDispo.getAdapter();
		adapterAux.clear();
		adapterAux.addAll(this.insuDao.getInsumosList());
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
		Insumo prod = (Insumo) this.prodsDispo.getAdapter().getItem(this.prodsDispo.getCheckedItemPosition());
		PedidoLinea linea = new PedidoLinea();
		linea.setCant(Double.valueOf(cant.getText().toString()));
		linea.setNombre(prod.toString());
		linea.setUom(prod.getUom());
		Object[] prodData=new Object[1];
		prodData[0] = prod;
		linea.setProduct(prodData);
		adapt.addLinea(linea);
		adapt.notifyDataSetChanged();
	}

	@Override
	public void searchProductsInsumos() {
		SharedPreferences sharedPref = getActivity().getSharedPreferences(getString(R.string.saved_insumo_search),Context.MODE_PRIVATE);
		String nombreProd = sharedPref.getString(getString(R.string.search_nombre),"");

		this.insuDao = new InsumosDAO(this);
		this.insuDao.getInsumos(nombreProd);			
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
