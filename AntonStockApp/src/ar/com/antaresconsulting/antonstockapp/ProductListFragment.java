package ar.com.antaresconsulting.antonstockapp;

import java.util.List;

import android.app.Activity;
import android.app.ListFragment;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import ar.com.antaresconsulting.antonstockapp.adapters.BaseProductAdapter;
import ar.com.antaresconsulting.antonstockapp.model.BaseProduct;
import ar.com.antaresconsulting.antonstockapp.model.dao.BachasDAO;
import ar.com.antaresconsulting.antonstockapp.model.dao.InsumosDAO;
import ar.com.antaresconsulting.antonstockapp.model.dao.MateriaPrimaDAO;
import ar.com.antaresconsulting.antonstockapp.model.dao.ProductDAO;

/**
 * A list fragment representing a list of Products. This fragment also supports
 * tablet devices by allowing list items to be given an 'activated' state upon
 * selection. This helps indicate which item is currently being viewed in a
 * {@link ProductDetailFragment}.
 * <p>
 * Activities containing this fragment MUST implement the {@link Callbacks}
 * interface.
 */
public class ProductListFragment extends ListFragment  implements ProductDAO.ServiciosCallbacks,  MateriaPrimaDAO.MateriaPrimaCallbacks,BachasDAO.BachasCallbacks,InsumosDAO.InsumosCallbacks{
	private BaseProductAdapter mAdapter;
	private MateriaPrimaDAO mpDao;
	private BachasDAO baDao;
	private InsumosDAO insuDao;
	private ProductDAO prodDao;
	
	private int tProd = -1;

	private List mp;
	private List bachas;
	private List insumos;
	private List servicios;
	/**
	 * The serialization (saved instance state) Bundle key representing the
	 * activated item position. Only used on tablets.
	 */
	private static final String STATE_ACTIVATED_POSITION = "activated_position";
	public static final String ARG_ITEM_ID = null;

	/**
	 * The fragment's current callback object, which is notified of list item
	 * clicks.
	 */
	private Callbacks mCallbacks = sDummyCallbacks;

	/**
	 * The current activated item position. Only used on tablets.
	 */
	private int mActivatedPosition = ListView.INVALID_POSITION;

	/**
	 * A callback interface that all activities containing this fragment must
	 * implement. This mechanism allows activities to be notified of item
	 * selections.
	 */
	public interface Callbacks {
		/**
		 * Callback for when an item has been selected.
		 */
		public void onItemSelected(BaseProduct prod);
	}

	/**
	 * A dummy implementation of the {@link Callbacks} interface that does
	 * nothing. Used only when this fragment is not attached to an activity.
	 */
	private static Callbacks sDummyCallbacks = new Callbacks() {
		@Override
		public void onItemSelected(BaseProduct id) {
		}
	};

	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public ProductListFragment() {
		this.mpDao =  new MateriaPrimaDAO(this);
		this.baDao =  new BachasDAO(this);
		this.insuDao =  new InsumosDAO(this);
	}

    public static ProductListFragment newInstance(int index) {
    	ProductListFragment f = new ProductListFragment();
        Bundle args = new Bundle();
        args.putInt("index", index);
        f.setArguments(args);
        return f;
    }
    
    
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if(savedInstanceState != null){
			this.tProd = savedInstanceState.getInt(AntonConstants.TPRODF);
		}
        setRetainInstance(true);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		// Restore the previously serialized activated item position.
		if (savedInstanceState != null
				&& savedInstanceState.containsKey(STATE_ACTIVATED_POSITION)) {
			setActivatedPosition(savedInstanceState
					.getInt(STATE_ACTIVATED_POSITION));
		}
		if(this.tProd != -1)
			refreshProducts(this.tProd);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		// Activities containing this fragment must implement its callbacks.
		if (!(activity instanceof Callbacks)) {
			throw new IllegalStateException(
					"Activity must implement fragment's callbacks.");
		}

		mCallbacks = (Callbacks) activity;

	}

	@Override
	public void onDetach() {
		super.onDetach();

		// Reset the active callbacks interface to the dummy implementation.
		mCallbacks = sDummyCallbacks;
	}

	@Override
	public void onListItemClick(ListView listView, View view, int position,
			long id) {
		super.onListItemClick(listView, view, position, id);

		// Notify the active callbacks interface (the activity, if the
		// fragment is attached to one) that an item has been selected.
		mCallbacks.onItemSelected(this.mAdapter.getItem(position));

	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		if (mActivatedPosition != ListView.INVALID_POSITION) {
			// Serialize and persist the activated item position.
			outState.putInt(STATE_ACTIVATED_POSITION, mActivatedPosition);
		}
		outState.putInt(AntonConstants.TPRODF, this.tProd);
	}

	
	/**
	 * Turns on activate-on-click mode. When this mode is on, list items will be
	 * given the 'activated' state when touched.
	 */
	public void setActivateOnItemClick(boolean activateOnItemClick) {
		// When setting CHOICE_MODE_SINGLE, ListView will automatically
		// give items the 'activated' state when touched.
		getListView().setChoiceMode(activateOnItemClick ? ListView.CHOICE_MODE_SINGLE: ListView.CHOICE_MODE_NONE);
		
	}

	private void setActivatedPosition(int position) {
		if (position == ListView.INVALID_POSITION) {
			getListView().setItemChecked(mActivatedPosition, false);
		} else {
			getListView().setItemChecked(position, true);
		}

		mActivatedPosition = position;
	}

	public void refreshProducts(int key) {
		this.tProd = key;
		switch (key) {
		case AntonConstants.MATERIA_PRIMA:
			if(this.mp != null){
				this.setMateriaPrima();
			}else{
				this.mpDao = new MateriaPrimaDAO(this);
				this.mpDao.getMateriaPrima();
			}			
			break;
		case AntonConstants.BACHAS:
			if(this.bachas != null){
				this.setBachas();
			}else{
				this.baDao = new BachasDAO(this);
				this.baDao.getBachas();
			}				
			break;
		case AntonConstants.INSUMOS:
			if(this.insumos != null){
				this.setInsumos();
			}else{
				this.insuDao = new InsumosDAO(this);
				this.insuDao.getInsumos();
			}				
			break;
		case AntonConstants.SERVICIOS:
			if(this.servicios != null){
				this.setServicios();
			}else{
				this.prodDao = new ProductDAO(this);
				this.prodDao.getServicios();
			}				
			break;
		default:
			break;
		}
		
	}
	
	@Override
	public void setMateriaPrima() {
		if(this.mp == null)
			this.mp = this.mpDao.getMateriaPrimasList();
		this.mAdapter = new BaseProductAdapter(this,this.mp);
		setListAdapter(this.mAdapter);		
	}
	
	@Override
	public void setBachas() {
		if(this.bachas == null)
			this.bachas = this.baDao.getBachasList();
		this.mAdapter = new BaseProductAdapter(this,this.bachas);
		setListAdapter(this.mAdapter);	
	}

	@Override
	public void setInsumos() {
		if(this.insumos == null)
			this.insumos = this.insuDao.getInsumosList();
		this.mAdapter = new BaseProductAdapter(this,this.insumos);
		setListAdapter(this.mAdapter);	
	}

	@Override
	public void setServicios() {
		if(this.servicios == null)
			this.servicios = this.prodDao.getServiciosList();
		this.mAdapter = new BaseProductAdapter(this,this.servicios);
		setListAdapter(this.mAdapter);			
	}

}
