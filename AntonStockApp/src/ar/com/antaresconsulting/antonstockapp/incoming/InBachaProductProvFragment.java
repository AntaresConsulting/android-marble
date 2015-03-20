package ar.com.antaresconsulting.antonstockapp.incoming;

import java.util.ArrayList;
import java.util.List;

import com.openerp.ConfirmMovesAsyncTask;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import ar.com.antaresconsulting.antonstockapp.R;
import ar.com.antaresconsulting.antonstockapp.adapters.PedidoLineaAdapter;
import ar.com.antaresconsulting.antonstockapp.model.StockMove;
import ar.com.antaresconsulting.antonstockapp.model.StockPicking;
import ar.com.antaresconsulting.antonstockapp.model.dao.BachasDAO;
import ar.com.antaresconsulting.antonstockapp.model.dao.PartnerDAO;
import ar.com.antaresconsulting.antonstockapp.model.dao.PedidoDAO;
import ar.com.antaresconsulting.antonstockapp.util.AntonConstants;

/**
 * A placeholder fragment containing a simple view.
 */
public class InBachaProductProvFragment extends Fragment implements   PartnerDAO.ClientsCallbacks,OnItemSelectedListener, InProductActions,BachasDAO.BachasCallbacks, PedidoDAO.PedidosCallbacks{

	private BachasDAO baDao;
	private PedidoDAO pedDao;
	private PartnerDAO partDao;

	private Spinner pedidos;

	private ListView productos;

	private boolean isExcecute=false;
	private TextView provee;
	private Fragment thisFrag;

	private static final String ARG_PARAM1 = "param1";

	// TODO: Rename and change types of parameters
	private int mParam1;
	public InBachaProductProvFragment() {

	}

	public static InBachaProductProvFragment newInstance(int param1) {
		InBachaProductProvFragment fragment = new InBachaProductProvFragment();
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

		rootView = inflater.inflate(R.layout.fragment_in_products,	container, false);			
		this.provee = (TextView) rootView.findViewById(R.id.proveedorNombre);
		this.pedidos = (Spinner) rootView.findViewById(R.id.pedidosCombo);
		this.pedidos.setOnItemSelectedListener(this);
		this.productos = (ListView) rootView.findViewById(R.id.productosDispo);

		this.pedDao = new PedidoDAO(this);
		this.pedDao.getPedidosPend(AntonConstants.BACHA_PICKING);			



		return rootView;
	}


	public void confirmStock() {
		if(this.pedidos.getSelectedItem() == null){
			Toast tt = Toast.makeText(this.getActivity().getApplicationContext(), "Debe haber almenos un pedido.", Toast.LENGTH_SHORT);
			tt.show();		
			return;
		}	
		List<StockMove> pls = new ArrayList<StockMove>();
		for (int i = 0; i < this.productos.getAdapter().getCount(); i++) {
			StockMove pl = (StockMove) this.productos.getAdapter().getItem(i);
			if(pl.isVerificado()){
				pls.add(pl);
			}

		}
		if(pls.size() <= 0){
			Toast tt = Toast.makeText(this.getActivity().getApplicationContext(), "Debe confirmar almenos un producto.", Toast.LENGTH_SHORT);
			tt.show();		
			return;			
		}			
		StockPicking ped = (StockPicking) this.pedidos.getSelectedItem();
		ped.setMoves(pls);
		ConfirmMovesAsyncTask confAction = new ConfirmMovesAsyncTask(getActivity());
		confAction.setMoveType("in");
		confAction.setModelStockPicking(AntonConstants.PICKING_MODEL);
		confAction.execute(ped);				


	}

	@Override
	public void setPedidos() {
		this.pedidos.setAdapter(new ArrayAdapter<StockPicking>(this.getActivity(),android.R.layout.simple_list_item_1,this.pedDao.getPedidoList()));
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int pos,long arg3) {
		StockPicking ped = (StockPicking) arg0.getItemAtPosition(pos);
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
		PedidoLineaAdapter pla =  new PedidoLineaAdapter(this,this.pedDao.getPedidoLineaList());
		pla.setForCheck(true);		
		this.productos.setAdapter(pla);		
	}


	@Override
	public void setClientDetail() {
		// TODO Auto-generated method stub

	}

	@Override
	public void setBachas() {
		// TODO Auto-generated method stub

	}

	@Override
	public void searchByEAN(String scanContent) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setClients() {
		// TODO Auto-generated method stub

	}

}
