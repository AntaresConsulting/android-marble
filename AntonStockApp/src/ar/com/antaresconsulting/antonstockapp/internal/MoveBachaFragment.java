package ar.com.antaresconsulting.antonstockapp.internal;

import com.openerp.CreatePickingAsyncTask;

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
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;
import ar.com.antaresconsulting.antonstockapp.AntonStockApp;
import ar.com.antaresconsulting.antonstockapp.R;
import ar.com.antaresconsulting.antonstockapp.adapters.PedidoLineaAdapter;
import ar.com.antaresconsulting.antonstockapp.listener.SwipeDismissListViewTouchListener;
import ar.com.antaresconsulting.antonstockapp.model.Bacha;
import ar.com.antaresconsulting.antonstockapp.model.StockMove;
import ar.com.antaresconsulting.antonstockapp.model.StockPicking;
import ar.com.antaresconsulting.antonstockapp.model.dao.BachasDAO;
import ar.com.antaresconsulting.antonstockapp.model.dao.PedidoDAO;
import ar.com.antaresconsulting.antonstockapp.popup.SearchBachaPopupFragment;
import ar.com.antaresconsulting.antonstockapp.util.AntonConstants;

/**
 * A placeholder fragment containing a simple view.
 */
public class MoveBachaFragment extends Fragment implements OnItemSelectedListener,
BachasDAO.BachasCallbacks, MoveProductActions,PedidoDAO.OrdenesDeEntregaCallbacks,PedidoDAO.PedidosCallbacks {
	private BachasDAO baDao;
	private Spinner pedidos;
	private PedidoDAO pedDao;

	private TextView cliente;
	private StockPicking selectPed;

	private Spinner placasList;
	private ListView productos;
	private ListView productosDispo;
	private static final String ARG_PARAM1 = "param1";
	private int mParam1;
	private boolean isRaw = false;

	public MoveBachaFragment() {

	}

	public static MoveBachaFragment newInstance(int param1) {
		MoveBachaFragment fragment = new MoveBachaFragment();
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
	
			rootView = inflater.inflate(R.layout.fragment_out_products_bacha,	container, false);
			this.productos = (ListView) rootView.findViewById(R.id.productosEgreso);
			this.productos.setAdapter(new PedidoLineaAdapter(this.getActivity()));
			this.productosDispo = (ListView) rootView.findViewById(R.id.productosDispo);
			this.productosDispo.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);			
			
			this.cliente = (TextView) rootView.findViewById(R.id.clienteNombre);

			this.pedidos = (Spinner) rootView.findViewById(R.id.pedidosCombo);
			this.pedidos.setOnItemSelectedListener(this);			
			this.pedDao = new PedidoDAO(this);
			this.pedDao.getOrdenesDeEntregaPendBachas();

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
		CreatePickingAsyncTask saveData = new CreatePickingAsyncTask(this.getActivity());
		int maxProds = this.productos.getAdapter().getCount();
		if(maxProds <= 0){
			Toast tt = Toast.makeText(this.getActivity().getApplicationContext(), "Debe haber seleccionado almenos un producto!", Toast.LENGTH_SHORT);
			tt.show();		
			return;
		}
		
		Integer clienteId = (Integer) this.selectPed.getPartner()[0];
		String origin = "";
		Object[] cliente = new Object[1];
		cliente[0] = clienteId;
		
		Integer loc_destination = AntonStockApp.getExternalId(AntonConstants.PRODUCT_LOCATION_OUTPUT);			
		
		StockPicking picking = new StockPicking(origin,AntonConstants.PICKING_TYPE_ID_INTERNAL,cliente,AntonConstants.BACHA_PICKING);

		for (int i = 0; i < maxProds; i++) {
			StockMove pl = (StockMove) this.productos.getAdapter().getItem(i);
			Integer loc_source;
			if(pl.isUseClientLocation())
				loc_source = this.selectPed.getCustomerLocation();
			else
				loc_source = (Integer) pl.getLocationProduct();
			StockMove move = new StockMove(pl.getName(), pl.getProduct(), pl.getUom(), loc_source, loc_destination, origin, pl.getQty(),null);				
			picking.addMove(move);
		}
		picking.setActionDone(true);
		saveData.execute(picking);
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
	public void setBachas() {
		this.productos.setAdapter(new ArrayAdapter<Bacha>(this.getActivity(),android.R.layout.simple_list_item_1,this.baDao.getBachasList()));	
	}

	public void searchByEAN(String scanContent) {
		this.baDao = new BachasDAO(this);
		this.baDao.getProductByEAN(scanContent);

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

