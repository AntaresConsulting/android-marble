package ar.com.antaresconsulting.antonstockapp;

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
import ar.com.antaresconsulting.antonstockapp.adapters.PedidoLineaAdapter;
import ar.com.antaresconsulting.antonstockapp.listener.SwipeDismissListViewTouchListener;
import ar.com.antaresconsulting.antonstockapp.model.Dimension;
import ar.com.antaresconsulting.antonstockapp.model.Insumo;
import ar.com.antaresconsulting.antonstockapp.model.MateriaPrima;
import ar.com.antaresconsulting.antonstockapp.model.Partner;
import ar.com.antaresconsulting.antonstockapp.model.PedidoLinea;
import ar.com.antaresconsulting.antonstockapp.model.SelectionObject;
import ar.com.antaresconsulting.antonstockapp.model.StockMove;
import ar.com.antaresconsulting.antonstockapp.model.StockPicking;
import ar.com.antaresconsulting.antonstockapp.model.dao.MateriaPrimaDAO;
import ar.com.antaresconsulting.antonstockapp.model.dao.PartnerDAO;
import ar.com.antaresconsulting.antonstockapp.popup.SearchMPPopupFragment;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass. Use the
 * {@link AddPMMPFragment#newInstance} factory method to create an instance of
 * this fragment.
 * 
 */
public class AddPMMPFragment extends Fragment implements PartnerDAO.SuppliersCallbacks, AddPMActions,SearchMPPopupFragment.SearchProductListener,MateriaPrimaDAO.MateriaPrimaCallbacks{
	private MateriaPrimaDAO mpDao;
	private PartnerDAO partDao;

	private EditText pl;
	
	private EditText cantPlacas;
	private EditText dimH;
	private EditText dimW;
	private Spinner dimT;
	private Spinner dimTipo;

	private Spinner proveedor;
	private ListView prodsPedido;
	private ListView prodsDispo;

	public static AddPMMPFragment newInstance() {
		AddPMMPFragment fragment = new AddPMMPFragment();
		return fragment;
	}


	public AddPMMPFragment() {
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
		View root = inflater.inflate(R.layout.fragment_add_pmm, container, false);
		pl = (EditText) root.findViewById(R.id.pickingList);
		proveedor = (Spinner) root.findViewById(R.id.proveedorList);
		prodsPedido = (ListView) root.findViewById(R.id.productosPedido);
		prodsPedido.setAdapter(new PedidoLineaAdapter(getActivity()));
		prodsDispo = (ListView) root.findViewById(R.id.productosDispo);
		prodsDispo.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		prodsDispo.setAdapter(new ArrayAdapter<MateriaPrima>(this.getActivity(),android.R.layout.simple_list_item_single_choice));
		cantPlacas = (EditText) root.findViewById(R.id.cantidadPlacas);
		dimH = (EditText) root.findViewById(R.id.altoPlaca);
		dimW = (EditText) root.findViewById(R.id.anchoPlaca);
		dimT = (Spinner) root.findViewById(R.id.espesorPlaca);
		dimT.setSelection(AntonConstants.DEFAULT_ESPESORES);
		dimTipo = (Spinner) root.findViewById(R.id.tipoDim);
		dimTipo.setAdapter(new ArrayAdapter<SelectionObject>(this.getActivity(),android.R.layout.simple_list_item_1,SelectionObject.getDimTipoData()));
	
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
		SearchMPPopupFragment popconf = new SearchMPPopupFragment();
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
		
		StockPicking picking = new StockPicking(origin,AntonConstants.PICKING_TYPE_ID_IN,proveedor.getId().toString(),loc_source,loc_destination,AntonConstants.RAW_PICKING);

		for (int i = 0; i < maxProds; i++) {
			PedidoLinea prod = (PedidoLinea) this.prodsPedido.getAdapter().getItem(i);
			StockMove move = new StockMove(prod.getNombre(),((MateriaPrima)prod.getProduct()[0]).getId(), (Integer)prod.getUom()[0], loc_source, loc_destination, origin, prod.getCant().toString());				
			picking.addMove(move);
		}
		saveData.execute(picking);
	}

	@Override
	public void searchProductsMP() {
		SharedPreferences sharedPref = getActivity().getSharedPreferences(getString(R.string.saved_mp_search),Context.MODE_PRIVATE);
		String tm = sharedPref.getString(getString(R.string.search_tipo),"");
		String color = sharedPref.getString(getString(R.string.search_color),"");
		String acabado = sharedPref.getString(getString(R.string.search_terminado),"");
		String nombreProd = sharedPref.getString(getString(R.string.search_nombre),"");

		this.mpDao = new MateriaPrimaDAO(this);
		this.mpDao.getMarmolesProducts(tm,color,acabado,nombreProd);		
	}


	@Override
	public void setMateriaPrima() {
		ArrayAdapter<MateriaPrima> adapterAux = (ArrayAdapter<MateriaPrima>) this.prodsDispo.getAdapter();
		adapterAux.clear();
		adapterAux.addAll(this.mpDao.getMateriaPrimasList());
		adapterAux.notifyDataSetChanged();			
	}

	@Override
	public void setSuppliers() {
		proveedor.setAdapter(new ArrayAdapter<Partner>(this.getActivity(),android.R.layout.simple_list_item_1,this.partDao.getPartnersArray()));
	}


	@Override
	public void setLineaPedido() {
		boolean cancel = false;
		View focus = null; 
		PedidoLineaAdapter adapt =  (PedidoLineaAdapter) prodsPedido.getAdapter();
		int position =  prodsDispo.getCheckedItemPosition();
		if(position == AdapterView.INVALID_POSITION){
			Toast tt = Toast.makeText(this.getActivity(), "Primero debe seleccionar un producto", Toast.LENGTH_SHORT);
			tt.show();
			return;
		}

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
		MateriaPrima prod = (MateriaPrima) this.prodsDispo.getAdapter().getItem(this.prodsDispo.getCheckedItemPosition());
		
		Dimension dim = new Dimension();
		dim.setDimH(this.dimH.getText().toString());
		dim.setDimT((String)this.dimT.getSelectedItem());
		dim.setDimW(this.dimW.getText().toString());
		dim.setDimTipo((SelectionObject) this.dimTipo.getSelectedItem());
		
		PedidoLinea linea = new PedidoLinea();
		linea.setNombre(prod.getNombre());
		linea.setUom(prod.getUom());
		linea.setDimension(dim);
		linea.setCant(Double.parseDouble(this.dimH.getText().toString())*Double.parseDouble(this.dimW.getText().toString())*Double.parseDouble(this.cantPlacas.getText().toString()));
		linea.setCantDim(Integer.parseInt(this.cantPlacas.getText().toString()));

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

}
