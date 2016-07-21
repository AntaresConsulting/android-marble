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
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import ar.com.antaresconsulting.antonstockapp.adapters.BaseProductAdapter;
import ar.com.antaresconsulting.antonstockapp.adapters.MateriaPrimaOutAdapter;
import ar.com.antaresconsulting.antonstockapp.adapters.PedidoLineaAdapter;
import ar.com.antaresconsulting.antonstockapp.listener.SwipeDismissListViewTouchListener;
import ar.com.antaresconsulting.antonstockapp.model.BaseProduct;
import ar.com.antaresconsulting.antonstockapp.model.Dimension;
import ar.com.antaresconsulting.antonstockapp.model.DimensionBalance;
import ar.com.antaresconsulting.antonstockapp.model.MateriaPrima;
import ar.com.antaresconsulting.antonstockapp.model.MateriaPrimaOut;
import ar.com.antaresconsulting.antonstockapp.model.Pedido;
import ar.com.antaresconsulting.antonstockapp.model.PedidoLinea;
import ar.com.antaresconsulting.antonstockapp.model.PickingMove;
import ar.com.antaresconsulting.antonstockapp.model.SelectionObject;
import ar.com.antaresconsulting.antonstockapp.model.dao.MateriaPrimaDAO;
import ar.com.antaresconsulting.antonstockapp.model.dao.DimensionDAO;
import ar.com.antaresconsulting.antonstockapp.model.dao.PedidoDAO;

/**
 * A placeholder fragment containing a simple view.
 */
public class MoveMPFragment extends Fragment implements OnItemSelectedListener,MoveProductActions, MateriaPrimaDAO.MateriaPrimaCallbacks,PedidoDAO.PedidosCallbacks ,PedidoDAO.OrdenesDeEntregaCallbacks, DimensionDAO.DimsCallbacks {
	private MateriaPrimaDAO mpDao;
	private DimensionDAO dimDao;

	private Spinner pedidos;
	private PedidoDAO pedDao;

	private TextView cliente;
	private Pedido selectPed;

	private Spinner placasList;
	private ExpandableListView productos;
	private ListView productosDispo;
	private EditText cantPlacasDispo;
	private List<PedidoLinea> pls;	
	private MateriaPrimaOutAdapter listAdapter;
	private static final String ARG_PARAM1 = "param1";
	private CreateMovesAsyncTask saveData;

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

		this.cantPlacasDispo = (EditText) rootView.findViewById(R.id.cantPlacasDispo);
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
		this.saveData = new CreateMovesAsyncTask(this.getActivity());
		OpenErpHolder.getInstance().setmModelName("stock.move");
		this.saveData.setModelStockPicking("stock.picking");		
		PickingMove[] values;
		values = new PickingMove[2];		
		values[0] = regMove1(AntonConstants.OUT_PORDUCT_LOCATION_STOCK,AntonConstants.OUT_PORDUCT_LOCATION_PROD);
		values[1] = regMove2(AntonConstants.OUT_PORDUCT_LOCATION_PROD,AntonConstants.OUT_PORDUCT_LOCATION_OUTPUT);
		this.saveData.execute(values);	
	}

	private PickingMove regMove1(String loc_source,String loc_des){
		PickingMove pm = new PickingMove();
		HashMap<String, Object> headerPicking = new HashMap<String, Object>(); 

		headerPicking.put("partner_id", AntonConstants.ANTON_COMPANY_ID);
		headerPicking.put("type", AntonConstants.INTERNAL_PORDUCT_TYPE);
		headerPicking.put("auto_picking",true);
		headerPicking.put("company_id",AntonConstants.ANTON_COMPANY_ID);
		headerPicking.put("move_type",AntonConstants.DELIVERY_METHOD);
		headerPicking.put("state","draft");
		headerPicking.put("location_id", loc_source);
		headerPicking.put("location_dest_id", loc_des);			
		pm.setHeaderPicking(headerPicking);

		List<HashMap<String,Object>> moves = new ArrayList<HashMap<String, Object>>();
		int cantDims = this.listAdapter.getGroupCount();
		for (int i = 0; i < cantDims; i++) {
			MateriaPrimaOut mpo = (MateriaPrimaOut) this.listAdapter.getGroup(i);
			HashMap<String,Object> move = new HashMap<String,Object>();
			DimensionBalance dimb = mpo.getDim();
			Dimension dim = new Dimension();
			dim.setDimH(((Double)dimb.getDimensionVals().get(AntonConstants.DIMENSION_HIGHT)).toString());
			dim.setDimW(((Double) dimb.getDimensionVals().get(AntonConstants.DIMENSION_WIDTH)).toString());
			dim.setDimT(((Double) dimb.getDimensionVals().get(AntonConstants.DIMENSION_THICKNESS)).toString());
			dim.setDimTipo(SelectionObject.getDimTipoById((String)dimb.getDimensionVals().get(AntonConstants.DIMENSION_TYPE)));

			double width = Double.valueOf(dim.getDimW());
			double height = Double.valueOf(dim.getDimH());
			int cant = mpo.getCant().intValue();
			double cantM2 = width*cant*height;
			move.put("dimension", dim);
			move.put("dimension_qty",mpo.getCant());
			move.put("balance_type","out");

			move.put("product_uos_qty",cantM2);
			move.put("product_qty",new Double(cantM2));

			move.put("partner_id",AntonConstants.ANTON_COMPANY_ID);
			move.put("product_id",mpo.getId());
			move.put("product_uom",mpo.getUom()[0]);
			move.put("location_id", loc_source);
			move.put("location_dest_id", loc_des);			
			move.put("company_id",AntonConstants.ANTON_COMPANY_ID);
//			move.put("prodlot_id",false);
//			move.put("tracking_id",false);
//			move.put("product_uos",false);
			move.put("type", AntonConstants.INTERNAL_PORDUCT_TYPE);
			move.put("picking_id",false);
			move.put("origin",false);
//			move.put("state","draft");
			move.put("name",mpo.getNombre());
			moves.add(move);

		}
		pm.setMoves(moves);
		pm.setPedidoOut(this.selectPed);
		pm.setMoveType(AntonConstants.INTERNAL_PORDUCT_TYPE);
		pm.setRegBalanace(true);
		return pm;
	}

	private PickingMove regMove2(String loc_source,String loc_des){
		PickingMove pm = new PickingMove();
		HashMap<String, Object> headerPicking = new HashMap<String, Object>(); 

		headerPicking.put("partner_id", AntonConstants.ANTON_COMPANY_ID);
		headerPicking.put("type", AntonConstants.INTERNAL_PORDUCT_TYPE);
		headerPicking.put("auto_picking",true);
		headerPicking.put("company_id",AntonConstants.ANTON_COMPANY_ID);
		headerPicking.put("move_type",AntonConstants.DELIVERY_METHOD);
		headerPicking.put("state","draft");
		headerPicking.put("location_id", loc_source);
		headerPicking.put("location_dest_id", loc_des);			
		pm.setHeaderPicking(headerPicking);

		List<HashMap<String,Object>> moves = new ArrayList<HashMap<String, Object>>();
		int cantDims = this.listAdapter.getGroupCount();
		for (int i = 0; i < cantDims; i++) {
			MateriaPrimaOut mpo = (MateriaPrimaOut) this.listAdapter.getGroup(i);
			List<PedidoLinea> pls = mpo.getPl();
			double totalPl = 0;
			for (PedidoLinea pedidoLinea : pls) {
				totalPl += pedidoLinea.getCant().doubleValue();
			}
			
			HashMap<String,Object> move = new HashMap<String,Object>();

			move.put("product_uos_qty",totalPl);
			move.put("product_qty",totalPl);

			move.put("partner_id",AntonConstants.ANTON_COMPANY_ID);
			move.put("product_id",mpo.getId());
			move.put("product_uom",mpo.getUom()[0]);
			move.put("location_id", loc_source);
			move.put("location_dest_id", loc_des);			
			move.put("company_id",AntonConstants.ANTON_COMPANY_ID);
//			move.put("prodlot_id",false);
//			move.put("tracking_id",false);
//			move.put("product_uos",false);
			move.put("type", AntonConstants.INTERNAL_PORDUCT_TYPE);
			move.put("picking_id",false);
			move.put("origin",false);
//			move.put("state","done");
			move.put("name",mpo.getNombre());
			moves.add(move);
		}
		pm.setMoves(moves);
		pm.setMoveType(AntonConstants.INTERNAL_PORDUCT_TYPE);
		pm.setRegBalanace(false);		
		return pm;
	}

	public void addProductMP(View view) {

		MateriaPrimaOutAdapter adapter = this.listAdapter;

		DimensionBalance dim = (DimensionBalance) this.placasList.getSelectedItem();
		MateriaPrimaOut prod = new MateriaPrimaOut();
		prod.setCant(new Integer(this.cantPlacasDispo.getText().toString()));
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
		pls = new ArrayList<PedidoLinea>();
		Integer prodId = null;
		for (int i = 0; i < checked.size(); i++) {
			int position = checked.keyAt(i);
			if (checked.valueAt(i)){
				PedidoLinea pl = (PedidoLinea) this.productosDispo.getAdapter().getItem(position);
				if(prodId != null){
					if(((Integer) pl.getProduct()[0]).intValue() != prodId.intValue()){
						Toast tt = Toast.makeText(getActivity(), "Las lineas seleccionadas debe corresponder al mismo tipo de producto", Toast.LENGTH_SHORT);
						tt.show();
						return;						
					}else{
						pls.add(pl);
					}
				}else{
					prodId = (Integer) pl.getProduct()[0];
					pls.add(pl);
				}				 
			}
		}
		dimDao = new DimensionDAO(this);
		dimDao.getAllDims(prodId);
	}

	@Override
	public void setMateriaPrima() {

	}

	@Override
	public void setOrdenes() {
		this.pedidos.setAdapter(new ArrayAdapter<Pedido>(this.getActivity(),android.R.layout.simple_list_item_1,this.pedDao.getPedidoList()));		

	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int pos,
			long arg3) {
		selectPed = (Pedido) arg0.getItemAtPosition(pos);
		this.cliente.setText((String) selectPed.getPartner()[1]);
		this.pedDao = new PedidoDAO(this);
		this.pedDao.getMoveByPed(selectPed.getId(),AntonConstants.MP_FILTER);		
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
		this.productosDispo.setAdapter(new ArrayAdapter<PedidoLinea>(this.getActivity(),android.R.layout.simple_list_item_multiple_choice,this.pedDao.getPedidoLineaList()));				
	}

}

