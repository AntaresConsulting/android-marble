package ar.com.antaresconsulting.antonstockapp;

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
			PedidoLinea[] pls = new PedidoLinea[this.productos.getAdapter().getCount()];
			int j = 0;
			for (int i = 0; i < this.productos.getAdapter().getCount(); i++) {
				PedidoLinea pl = (PedidoLinea) this.productos.getAdapter().getItem(i);
				if(pl.isVerificado()){
					pls[j++] =  pl;
				}
				
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
	public void addProduct(View view) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setClients() {
		// TODO Auto-generated method stub
		
	}

}
