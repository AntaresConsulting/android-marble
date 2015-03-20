package ar.com.antaresconsulting.antonstockapp.internal;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.openerp.CreatePickingAsyncTask;

import android.app.Fragment;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import ar.com.antaresconsulting.antonstockapp.AntonStockApp;
import ar.com.antaresconsulting.antonstockapp.R;
import ar.com.antaresconsulting.antonstockapp.adapters.MateriaPrimaOutAdapter;
import ar.com.antaresconsulting.antonstockapp.listener.SwipeDismissListViewTouchListener;
import ar.com.antaresconsulting.antonstockapp.model.Dimension;
import ar.com.antaresconsulting.antonstockapp.model.DimensionBalance;
import ar.com.antaresconsulting.antonstockapp.model.MateriaPrimaOut;
import ar.com.antaresconsulting.antonstockapp.model.StockMove;
import ar.com.antaresconsulting.antonstockapp.model.StockPicking;
import ar.com.antaresconsulting.antonstockapp.model.dao.MateriaPrimaDAO;
import ar.com.antaresconsulting.antonstockapp.model.dao.DimensionDAO;
import ar.com.antaresconsulting.antonstockapp.model.dao.PedidoDAO;
import ar.com.antaresconsulting.antonstockapp.popup.SearchMPPopupFragment;
import ar.com.antaresconsulting.antonstockapp.util.AntonConstants;

/**
 * A placeholder fragment containing a simple view.
 */
public class MoveMPFragment extends Fragment implements OnItemSelectedListener,MoveProductActions, MateriaPrimaDAO.MateriaPrimaCallbacks,PedidoDAO.PedidosCallbacks ,PedidoDAO.OrdenesDeEntregaCallbacks, DimensionDAO.DimsCallbacks {
	private MateriaPrimaDAO mpDao;
	private DimensionDAO dimDao;

	private Spinner pedidos;
	private PedidoDAO pedDao;

	private TextView cliente;
	private StockPicking selectPed;

	private Spinner placasList;
	private ExpandableListView productos;
	private ListView productosDispo;
	private EditText cantPlacasS;
	private List<StockMove> pls;	
	private MateriaPrimaOutAdapter listAdapter;
	private static final String ARG_PARAM1 = "param1";

	public MoveMPFragment() {

	}

	public static MoveMPFragment newInstance(int param1) {
		MoveMPFragment fragment = new MoveMPFragment();
		Bundle args = new Bundle();
		args.putInt(ARG_PARAM1, param1);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			getArguments().getInt(ARG_PARAM1);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = null;

		rootView = inflater.inflate(R.layout.fragment_out_products_mp,	container, false);
		this.productosDispo = (ListView) rootView.findViewById(R.id.productosDispo);
		this.productosDispo.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

		this.cantPlacasS = (EditText) rootView.findViewById(R.id.cantPlacasSelect);
		this.placasList = (Spinner) rootView.findViewById(R.id.placasList);	
		this.productos = (ExpandableListView) rootView.findViewById(R.id.productosEgreso);
		this.listAdapter = new MateriaPrimaOutAdapter(this.getActivity());
		this.productos.setAdapter(this.listAdapter);
		this.dimDao = new DimensionDAO(this);
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
							listAdapter.delProduct((MateriaPrimaOut) listAdapter.getGroup(position));
						}
						((BaseAdapter) productos.getAdapter()).notifyDataSetChanged();
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

	public void closeMoves() {
		CreatePickingAsyncTask saveData = new CreatePickingAsyncTask(this.getActivity());
		int maxProds = this.productos.getAdapter().getCount();		
		if(maxProds <= 0){
			Toast tt = Toast.makeText(this.getActivity().getApplicationContext(), "Debe haber seleccionado almenos un producto!", Toast.LENGTH_SHORT);
			tt.show();		
			return;
		}
		
		String origin = selectPed.getOrigin();
		Integer clienteID = (Integer) selectPed.getPartner()[0];
		Object[] cliente = new Object[1];
		cliente[0] = clienteID;
		Integer loc_destinationPROD = AntonStockApp.getExternalId(AntonConstants.PRODUCT_LOCATION_PRODUCTION);			
		Integer loc_destinationOUT = AntonStockApp.getExternalId(AntonConstants.PRODUCT_LOCATION_OUTPUT);			

		saveData.setOutPicking(new Long(this.selectPed.getId().longValue()));
		
		StockPicking[] pickings = new StockPicking[2]; 
		
		pickings[0] = new StockPicking(origin,AntonConstants.PICKING_TYPE_ID_INTERNAL,cliente,AntonConstants.RAW_PICKING);
		pickings[1] = new StockPicking(origin,AntonConstants.PICKING_TYPE_ID_INTERNAL,cliente,AntonConstants.RAW_PICKING);
		for (int i = 0; i < maxProds; i++) {
			MateriaPrimaOut mpo = (MateriaPrimaOut) this.productos.getAdapter().getItem(i);
			StockMove prod = mpo.getPl().get(0);
			Dimension dim = mpo.getDim().getDim();
			Object[] dimension = new Object[1];
			dimension[0]= dim.getDimId();
			Integer loc_sourceSTK = (Integer) mpo.getLocId()[0];
			StockMove move = new StockMove(prod.getName(),prod.getProduct(), prod.getUom(), loc_sourceSTK, loc_destinationPROD, origin, prod.getQty(),dimension,mpo.getCant(),null);
			List pls = mpo.getPl();
			for (Iterator iterator = pls.iterator(); iterator.hasNext();) {
				StockMove plAux = (StockMove) iterator.next();		
				StockMove moveOut = new StockMove(prod.getName(),prod.getProduct(), prod.getUom(), loc_destinationPROD, loc_destinationOUT, origin, plAux.getQty(),plAux.getDimension(),plAux.getQtytDim(),null);
				pickings[1].addMove(moveOut);
			}
			pickings[0].addMove(move);
		}		
		pickings[0].setActionDone(true);
		pickings[1].setActionDone(true);

		saveData.execute(pickings);
	}

	public void addProductMP(View view) {

		MateriaPrimaOutAdapter adapter = this.listAdapter;

		DimensionBalance dim = (DimensionBalance) this.placasList.getSelectedItem();
		MateriaPrimaOut prod = new MateriaPrimaOut();
		prod.setCant(new Integer(this.cantPlacasS.getText().toString()));
		prod.setPl(this.pls);
		prod.setDim(dim);
		prod.setNombre((String) this.pls.get(0).getProduct()[1]);
		prod.setUom(this.pls.get(0).getUom());
		prod.setId((Integer) this.pls.get(0).getProduct()[0]);
		adapter.addProduct(prod);

		adapter.notifyDataSetChanged();		

	}




	public void searchByEAN(String scanContent) {
		this.mpDao = new MateriaPrimaDAO(this);
		this.mpDao.getProductByEAN(scanContent);

	}

	@Override
	public void setDimensions() {
		this.placasList.setAdapter(new ArrayAdapter<DimensionBalance>(this.getActivity(),android.R.layout.simple_list_item_1,this.dimDao.getDimensionList()));
	}

	public void checkDispoPlacas(View view) {
		SparseBooleanArray checked = this.productosDispo.getCheckedItemPositions();
		if(checked.size() <= 0){
			Toast tt = Toast.makeText(getActivity(), "Debe seleccionar almenos una linea", Toast.LENGTH_SHORT);
			tt.show();
			return;
		}		
		pls = new ArrayList<StockMove>();
		Integer prodId = null;
		Double tchkControl = new Double(0); 
		for (int i = 0; i < checked.size(); i++) {
			int position = checked.keyAt(i);
			if (checked.valueAt(i)){
				StockMove pl = (StockMove) this.productosDispo.getAdapter().getItem(position);
				if(prodId != null){					
					Dimension dim =  (Dimension) pl.getDimension()[0];
					Double thck = dim.getDimT();				
					Integer actProdId = (Integer) pl.getProduct()[0];
					if(actProdId.intValue() != prodId.intValue()){
						Toast tt = Toast.makeText(getActivity(), "Las lineas seleccionadas debe corresponder al mismo tipo de producto", Toast.LENGTH_SHORT);
						tt.show();
						return;						
					}if(!thck.equals(tchkControl)){
						Toast tt = Toast.makeText(getActivity(), "Las lineas seleccionadas debe tener el mismo espesor", Toast.LENGTH_SHORT);
						tt.show();
						return;						
					}else{
						pls.add(pl);
					}
				}else{
					Dimension dim =  (Dimension) pl.getDimension()[0];
					Double thck = dim.getDimT();
					if(tchkControl.doubleValue() == 0)
						tchkControl = thck;					
					prodId = (Integer) pl.getProduct()[0];
					pls.add(pl);
				}				 
			}
		}
		dimDao = new DimensionDAO(this);
		dimDao.getAllDims(prodId,tchkControl);
	}

	@Override
	public void setMateriaPrima() {

	}

	@Override
	public void setOrdenes() {
		this.pedidos.setAdapter(new ArrayAdapter<StockPicking>(this.getActivity(),android.R.layout.simple_list_item_1,this.pedDao.getPedidoList()));		

	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int pos,
			long arg3) {
		selectPed = (StockPicking) arg0.getItemAtPosition(pos);
		this.cliente.setText((String) selectPed.getPartner()[1]);
		this.pedDao = new PedidoDAO(this);
		this.pedDao.getMoveByPedMP(selectPed.getId(),AntonConstants.MP_FILTER);		
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setPedidos() {
		// TODO Auto-generated method stub

	}

	@Override
	public void setPedidosLineas() {
		this.productosDispo.setAdapter(new ArrayAdapter<StockMove>(this.getActivity(),android.R.layout.simple_list_item_multiple_choice,this.pedDao.getPedidoLineaList()));				
	}

}

