package ar.com.antaresconsulting.antonstockapp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.openerp.CreateMovesAsyncTask;
import com.openerp.OpenErpHolder;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import ar.com.antaresconsulting.antonstockapp.adapters.BaseProductAdapter;
import ar.com.antaresconsulting.antonstockapp.adapters.PedidoLineaAdapter;
import ar.com.antaresconsulting.antonstockapp.listener.SwipeDismissListViewTouchListener;
import ar.com.antaresconsulting.antonstockapp.model.BaseProduct;
import ar.com.antaresconsulting.antonstockapp.model.Dimension;
import ar.com.antaresconsulting.antonstockapp.model.MateriaPrima;
import ar.com.antaresconsulting.antonstockapp.model.Partner;
import ar.com.antaresconsulting.antonstockapp.model.PedidoLinea;
import ar.com.antaresconsulting.antonstockapp.model.PickingMove;
import ar.com.antaresconsulting.antonstockapp.model.SelectionObject;
import ar.com.antaresconsulting.antonstockapp.model.dao.MateriaPrimaDAO;
import ar.com.antaresconsulting.antonstockapp.model.dao.PartnerDAO;

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
	private CreateMovesAsyncTask saveData;

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
		this.saveData = new CreateMovesAsyncTask(this.getActivity());
		OpenErpHolder.getInstance().setmModelName("stock.move");
		int maxProds = this.prodsPedido.getAdapter().getCount();
		PickingMove[] values = new PickingMove[1];
		values[0] = new PickingMove();

		Partner proveedor = (Partner) this.proveedor.getSelectedItem();
		HashMap<String, Object> headerPicking = new HashMap<String, Object>();
		String loc_source;
		String loc_destination;

		headerPicking.put("partner_id", proveedor.getId());
		this.saveData.setModelStockPicking("stock.picking.in");
		loc_source = AntonConstants.PRODUCT_LOCATION_SUPPLIER;
		loc_destination = AntonConstants.PRODUCT_LOCATION_STOCK;			

		String origin = pl.getText().toString();

		headerPicking.put("type", AntonConstants.IN_PORDUCT_TYPE);
		headerPicking.put("auto_picking",false);
		headerPicking.put("company_id",AntonConstants.ANTON_COMPANY_ID);
		headerPicking.put("move_type",AntonConstants.DELIVERY_METHOD);
		headerPicking.put("state","draft");
		headerPicking.put("origin",origin);
		headerPicking.put("move_prod_type",AntonConstants.RAW_PICKING);
		headerPicking.put("location_id",loc_source);
		headerPicking.put("location_dest_id",loc_destination);			

		values[0].setHeaderPicking(headerPicking);

		List<HashMap<String,Object>> moves = new ArrayList<HashMap<String, Object>>();

		for (int i = 0; i < maxProds; i++) {
			PedidoLinea prod = (PedidoLinea) this.prodsPedido.getAdapter().getItem(i);
			HashMap<String,Object> move = new HashMap<String,Object>();
			move.put("dimension",prod.getDimension()[0]);
			move.put("dimension_qty",prod.getCantDim());
			move.put("product_uos_qty",prod.getCant());
			move.put("product_id",((MateriaPrima)prod.getProduct()[0]).getId());
			move.put("product_uom",prod.getUom()[0]);
			move.put("location_id",loc_source);
			move.put("location_dest_id",loc_destination);				
			move.put("company_id",AntonConstants.ANTON_COMPANY_ID);
			move.put("prodlot_id",false);
			move.put("tracking_id",false);
			move.put("product_qty",prod.getCant());
			move.put("product_uos",prod.getUom()[0]);
			move.put("type",AntonConstants.IN_PORDUCT_TYPE);
			move.put("origin",origin);
			move.put("state","draft");
			move.put("name",prod.getNombre());
			moves.add(move);
		}
		values[0].setMoves(moves);
		values[0].setMoveType(AntonConstants.IN_PORDUCT_TYPE);
		this.saveData.execute(values);				
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

}
