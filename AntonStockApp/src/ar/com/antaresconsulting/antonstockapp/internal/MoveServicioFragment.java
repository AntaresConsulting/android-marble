package ar.com.antaresconsulting.antonstockapp.internal;

import android.app.Fragment;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;
import ar.com.antaresconsulting.antonstockapp.R;
import ar.com.antaresconsulting.antonstockapp.adapters.PedidoLineaAdapter;
import ar.com.antaresconsulting.antonstockapp.listener.SwipeDismissListViewTouchListener;
import ar.com.antaresconsulting.antonstockapp.model.Servicio;
import ar.com.antaresconsulting.antonstockapp.model.StockMove;
import ar.com.antaresconsulting.antonstockapp.model.StockPicking;
import ar.com.antaresconsulting.antonstockapp.model.dao.PedidoDAO;
import ar.com.antaresconsulting.antonstockapp.model.dao.ProductDAO;
import ar.com.antaresconsulting.antonstockapp.popup.SearchBachaPopupFragment;
import ar.com.antaresconsulting.antonstockapp.util.AntonConstants;

/**
 * A placeholder fragment containing a simple view.
 */
public class MoveServicioFragment extends Fragment implements OnItemSelectedListener,
ProductDAO.ServiciosCallbacks, MoveProductActions,PedidoDAO.OrdenesDeEntregaCallbacks,PedidoDAO.PedidosCallbacks {
	private ProductDAO prodDao;
	private Spinner pedidos;
	private PedidoDAO pedDao;

	private TextView cliente;
	private StockPicking selectPed;

	private ListView productos;
	private ListView productosDispo;
	private static final String ARG_PARAM1 = "param1";
	private int mParam1;
	public MoveServicioFragment() {

	}

	public static MoveServicioFragment newInstance(int param1) {
		MoveServicioFragment fragment = new MoveServicioFragment();
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
	
			rootView = inflater.inflate(R.layout.fragment_out_products_servicio,	container, false);
			this.productos = (ListView) rootView.findViewById(R.id.productosEgreso);
			this.productos.setAdapter(new PedidoLineaAdapter(this.getActivity()));
			this.productosDispo = (ListView) rootView.findViewById(R.id.productosDispo);
			this.productosDispo.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);			
			
			this.cliente = (TextView) rootView.findViewById(R.id.clienteNombre);

			this.pedidos = (Spinner) rootView.findViewById(R.id.pedidosCombo);
			this.pedidos.setOnItemSelectedListener(this);			
			this.pedDao = new PedidoDAO(this);
			this.pedDao.getOrdenesDeEntregaPend();

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
							((PedidoLineaAdapter) productos.getAdapter()).delProduct((StockMove) productos.getAdapter().getItem(position));
						}
						((BaseAdapter) productos.getAdapter()).notifyDataSetChanged();
					}
				});
		this.productos.setOnTouchListener(touchListener);
		this.productos.setOnScrollListener(touchListener.makeScrollListener());
		return rootView;

	}

	public void popSearchBacha(View view) {
		SearchBachaPopupFragment popconf = new SearchBachaPopupFragment();
		popconf.setTargetFragment(this, 1234);
		popconf.show(getFragmentManager(),"Server_Search");
	}
	
	public void closeMoves() {
//		this.saveData = new CreateMovesAsyncTask(this.getActivity());
//		OpenErpHolder.getInstance().setmModelName("stock.move");
//		int maxProds = this.productos.getAdapter().getCount();
//		PickingMove[] values = new PickingMove[1];
//		values[0] = new PickingMove();
//
//		HashMap<String, Object> headerPicking = new HashMap<String, Object>();
//		String loc_source;
//		String loc_destination;
//
//		this.saveData.setModelStockPicking("stock.picking");
//		loc_source = AntonConstants.PRODUCT_LOCATION_STOCK;
//		loc_destination = AntonConstants.PRODUCT_LOCATION_OUTPUT;			
//		String origin = selectPed.getNombre();
//		values[0].setPedidoOut(selectPed);
//		headerPicking.put("type", AntonConstants.INTERNAL_PORDUCT_TYPE);
//		headerPicking.put("auto_picking",false);
//		headerPicking.put("company_id",AntonConstants.ANTON_COMPANY_ID);
//		headerPicking.put("partner_id",AntonConstants.ANTON_COMPANY_ID);
//		headerPicking.put("move_type",AntonConstants.DIRECT_METHOD);
//		headerPicking.put("origin",origin);		
//		headerPicking.put("location_id",loc_source);
//		headerPicking.put("location_dest_id",loc_destination);			
//
//		values[0].setHeaderPicking(headerPicking);
//
//		List<HashMap<String,Object>> moves = new ArrayList<HashMap<String, Object>>();
//		
//		for (int i = 0; i < maxProds; i++) {
//			PedidoLinea prod = (PedidoLinea) this.productos.getAdapter().getItem(i);
//			HashMap<String,Object> move = new HashMap<String,Object>();
//			move.put("product_uos_qty",prod.getCant());
//			move.put("product_id",(Integer)prod.getProduct()[0]);
//			move.put("product_uom",prod.getUom()[0]);
//			move.put("location_id",loc_source);
//			move.put("location_dest_id",loc_destination);				
//			move.put("company_id",AntonConstants.ANTON_COMPANY_ID);
//			move.put("prodlot_id",false);
//			move.put("tracking_id",false);
//			move.put("product_qty",prod.getCant());
//			move.put("product_uos",prod.getUom()[0]);
//			move.put("state","draft");
//			move.put("name",prod.getNombre());
//			moves.add(move);
//		}
//		values[0].setMoves(moves);
//		values[0].setMoveType(AntonConstants.INTERNAL_PORDUCT_TYPE);
//		this.saveData.execute(values);	
	}
	
	public void addProductBacha(View view) {

		SparseBooleanArray checked = this.productosDispo.getCheckedItemPositions();

		PedidoLineaAdapter adapter = (PedidoLineaAdapter) this.productos.getAdapter();
		for (int i = 0; i < checked.size(); i++) {
			int position = checked.keyAt(i);
			if (checked.valueAt(i)){
				StockMove pl = (StockMove) this.productosDispo.getAdapter().getItem(position);
				adapter.addLinea(pl);
			}

		}
		adapter.notifyDataSetChanged();	
	
	}		
		

	
	@Override
	public void setServicios() {
		this.productos.setAdapter(new ArrayAdapter<Servicio>(this.getActivity(),android.R.layout.simple_list_item_1,this.prodDao.getServiciosList()));	
	}

	public void searchByEAN(String scanContent) {
		this.prodDao = new ProductDAO(this);
		this.prodDao.getProductByEAN(scanContent);

	}

	@Override
	public void setPedidosLineas() {
		this.productosDispo.setAdapter(new ArrayAdapter<StockMove>(this.getActivity(),android.R.layout.simple_list_item_multiple_choice,this.pedDao.getPedidoLineaList()));		
	}


	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int pos,long arg3) {
		selectPed = (StockPicking) arg0.getItemAtPosition(pos);
		this.cliente.setText((String) selectPed.getPartner()[1]);
		this.pedDao = new PedidoDAO(this);
		this.pedDao.getMoveByPed(selectPed.getId(),AntonConstants.BACHA_FILTER);
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setOrdenes() {
		this.pedidos.setAdapter(new ArrayAdapter<StockPicking>(this.getActivity(),android.R.layout.simple_list_item_1,this.pedDao.getPedidoList()));		
	}

	@Override
	public void setPedidos() {
		// TODO Auto-generated method stub
		
	}
	
}

