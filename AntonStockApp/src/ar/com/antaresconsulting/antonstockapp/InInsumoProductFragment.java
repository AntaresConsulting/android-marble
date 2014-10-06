package ar.com.antaresconsulting.antonstockapp;

import com.openerp.ConfirmMovesAsyncTask;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import ar.com.antaresconsulting.antonstockapp.adapters.PedidoLineaAdapter;
import ar.com.antaresconsulting.antonstockapp.listener.SwipeDismissListViewTouchListener;
import ar.com.antaresconsulting.antonstockapp.model.Insumo;
import ar.com.antaresconsulting.antonstockapp.model.Pedido;
import ar.com.antaresconsulting.antonstockapp.model.PedidoLinea;
import ar.com.antaresconsulting.antonstockapp.model.dao.InsumosDAO;
import ar.com.antaresconsulting.antonstockapp.model.dao.PedidoDAO;

/**
 * A placeholder fragment containing a simple view.
 */
public class InInsumoProductFragment extends Fragment implements OnItemSelectedListener, InProductActions,InsumosDAO.InsumosCallbacks, PedidoDAO.PedidosCallbacks{
	private InsumosDAO insuDao;
	private PedidoDAO pedDao;

	private Spinner pedidos;

	private ListView productos;
	private ListView productosDispo;

	private EditText cantPlacas;
	private TextView provee;



	private static final String ARG_PARAM1 = "param1";

	// TODO: Rename and change types of parameters
	private int mParam1;
	public InInsumoProductFragment() {

	}

	public static InInsumoProductFragment newInstance(int param1) {
		InInsumoProductFragment fragment = new InInsumoProductFragment();
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
		this.insuDao = new InsumosDAO(this);

		rootView = inflater.inflate(R.layout.fragment_in_products_insumos,	container, false);

		this.productosDispo = (ListView) rootView.findViewById(R.id.productosDispo);
		this.productosDispo.setChoiceMode(ListView.CHOICE_MODE_SINGLE);			
		this.cantPlacas = (EditText) rootView.findViewById(R.id.clientesList);
		this.productos = (ListView) rootView.findViewById(R.id.productosPedido);
		this.productos.setAdapter(new PedidoLineaAdapter(this.getActivity()));
		this.provee = (TextView) rootView.findViewById(R.id.proveedorNombre);
		this.pedidos = (Spinner) rootView.findViewById(R.id.pedidosCombo);
		this.pedidos.setOnItemSelectedListener(this);

		this.pedDao = new PedidoDAO(this);
		this.pedDao.getPedidosPend(AntonConstants.INSU_PICKING);

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

		PedidoLinea prod = (PedidoLinea) this.productosDispo.getAdapter().getItem(this.productosDispo.getCheckedItemPosition());
		int position =  productosDispo.getCheckedItemPosition();
		if(position == AdapterView.INVALID_POSITION){
			Toast tt = Toast.makeText(this.getActivity(), "Primero debe seleccionar un producto", Toast.LENGTH_SHORT);
			tt.show();
			return;
		}
		
		PedidoLineaAdapter adapter = (PedidoLineaAdapter) this.productos.getAdapter();		
		prod.setCant(Double.parseDouble(this.cantPlacas.getText().toString()));
		
		adapter.addLinea(prod);
		adapter.notifyDataSetChanged();
	}

	@Override
	public void setInsumos() {
		this.productos.setAdapter(new ArrayAdapter<Insumo>(this.getActivity(),android.R.layout.simple_list_item_1,this.insuDao.getInsumosList()));	
	}


	public void confirmStock() {
		PedidoLinea[] pls = new PedidoLinea[this.productos.getAdapter().getCount()];
		for (int i = 0; i < this.productos.getAdapter().getCount(); i++) {
			pls[i] =  (PedidoLinea) this.productos.getAdapter().getItem(i);
		}
		ConfirmMovesAsyncTask confAction = new ConfirmMovesAsyncTask(getActivity());
		confAction.setMoveType("in");
		confAction.setModelStockPicking("stock.move");
		confAction.execute(pls);				

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

}
