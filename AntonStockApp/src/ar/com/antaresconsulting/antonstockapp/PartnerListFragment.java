package ar.com.antaresconsulting.antonstockapp;

import android.app.Activity;
import android.app.ListFragment;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import ar.com.antaresconsulting.antonstockapp.adapters.PartnerAdapter;
import ar.com.antaresconsulting.antonstockapp.model.BaseProduct;
import ar.com.antaresconsulting.antonstockapp.model.Partner;
import ar.com.antaresconsulting.antonstockapp.model.dao.PartnerDAO;

/**
 * A list fragment representing a list of Partners. This fragment also supports
 * tablet devices by allowing list items to be given an 'activated' state upon
 * selection. This helps indicate which item is currently being viewed in a
 * {@link PartnerDetailFragment}.
 * <p>
 * Activities containing this fragment MUST implement the {@link Callbacks}
 * interface.
 */
public class PartnerListFragment extends ListFragment  implements PartnerDAO.SuppliersCallbacks,PartnerDAO.ClientsCallbacks{
	private PartnerAdapter mAdapter;
	private PartnerDAO pDao;
	private Partner[] customers;
	private Partner[] suppliers;

	/**
	 * The serialization (saved instance state) Bundle key representing the
	 * activated item position. Only used on tablets.
	 */
	private static final String STATE_ACTIVATED_POSITION = "activated_position";

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
		public void onItemSelected(String id);
	}

	/**
	 * A dummy implementation of the {@link Callbacks} interface that does
	 * nothing. Used only when this fragment is not attached to an activity.
	 */
	private static Callbacks sDummyCallbacks = new Callbacks() {
		@Override
		public void onItemSelected(String id) {
		}
	};

	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public PartnerListFragment() {	
		this.pDao =  new PartnerDAO(this);
	}
	
    public static PartnerListFragment newInstance(int index) {
    	PartnerListFragment f = new PartnerListFragment();
        Bundle args = new Bundle();
        args.putInt("index", index);
        f.setArguments(args);
        return f;
    }
    
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setRetainInstance(true);
		Boolean listsup = null;
		Bundle args = getArguments();
		BaseProduct prod = null;
		if (args != null) {
			listsup = args.getBoolean(AntonConstants.SET_SUPPLIER);
			prod = (BaseProduct) args.getSerializable(AntonConstants.PRODUCT_SELECTED);

		}        
		if(listsup != null && listsup.booleanValue()){
			this.pDao.getSuppliersByProd(prod.getTemplateId());
		}else{
			this.pDao.getAllCompanies();	
		}        
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
		//mCallbacks.onItemSelected(DummyContent.ITEMS.get(position).id);
		mCallbacks.onItemSelected(this.mAdapter.getItem(position).getId().toString());
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		
		if (mActivatedPosition != ListView.INVALID_POSITION) {
			// Serialize and persist the activated item position.
			outState.putInt(STATE_ACTIVATED_POSITION, mActivatedPosition);
		}
	}

	/**
	 * Turns on activate-on-click mode. When this mode is on, list items will be
	 * given the 'activated' state when touched.
	 */
	public void setActivateOnItemClick(boolean activateOnItemClick) {
		// When setting CHOICE_MODE_SINGLE, ListView will automatically
		// give items the 'activated' state when touched.
		getListView().setChoiceMode(
				activateOnItemClick ? ListView.CHOICE_MODE_SINGLE
						: ListView.CHOICE_MODE_NONE);
	}

	private void setActivatedPosition(int position) {
		if (position == ListView.INVALID_POSITION) {
			getListView().setItemChecked(mActivatedPosition, false);
		} else {
			getListView().setItemChecked(position, true);
		}

		mActivatedPosition = position;
	}

	@Override
	public void setClients() {
		if(this.customers == null)
			this.customers = this.pDao.getPartnersArray();
		this.mAdapter = new PartnerAdapter(this,this.customers);
		setListAdapter(this.mAdapter);
	}

	@Override
	public void setSuppliers() {
		if(this.suppliers == null)
			this.suppliers = this.pDao.getPartnersArray();
		this.mAdapter = new PartnerAdapter(this,this.suppliers);
		setListAdapter(this.mAdapter);
	}

	@Override
	public void setClientDetail() {
		// TODO Auto-generated method stub
		
	}

	public void refreshPartners(String partnerType) {
		
		if(partnerType.equalsIgnoreCase(PartnerListActivity.CUSTOMERS)){
			if(this.customers != null){
				this.setClients();
			}else{
				this.pDao = new PartnerDAO(this);
				this.pDao.getAllCompanies();
			}
			
		}else if(partnerType.equalsIgnoreCase(PartnerListActivity.SUPPLIERS)){
			if(this.suppliers != null){
				this.setSuppliers();
			}else{
				this.pDao = new PartnerDAO(this);
				this.pDao.getAllSuppliers();
			}
		}
		
	}

	@Override
	public void setSuppliersProd() {
		if(this.suppliers == null)
			this.suppliers = this.pDao.getProdSupplier();
		this.mAdapter = new PartnerAdapter(this,this.suppliers);
		setListAdapter(this.mAdapter);
		
	}		
}
