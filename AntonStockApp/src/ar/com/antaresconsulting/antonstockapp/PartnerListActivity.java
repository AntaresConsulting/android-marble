package ar.com.antaresconsulting.antonstockapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;

/**
 * An activity representing a list of Items. This activity has different
 * presentations for handset and tablet-size devices. On handsets, the activity
 * presents a list of items, which when touched, lead to a
 * {@link ItemDetailActivity} representing item details. On tablets, the
 * activity presents the list of items and item details side-by-side using two
 * vertical panes.
 * <p>
 * The activity makes heavy use of fragments. The list of items is a
 * {@link ItemListFragment} and the item details (if present) is a
 * {@link ItemDetailFragment}.
 * <p>
 * This activity also implements the required {@link ItemListFragment.Callbacks}
 * interface to listen for item selections.
 */
public class PartnerListActivity extends ActionBarActivity implements
NavigationDrawerFragment.NavigationDrawerCallbacks,PartnerListFragment.Callbacks {

	/**
	 * Whether or not the activity is in two-pane mode, i.e. running on a tablet
	 * device.
	 */
	
	public static final String CUSTOMERS = "customers";
	public static final String SUPPLIERS = "suppliers";
	
	private boolean mTwoPane;
	private String partnerType;
	private CharSequence mTitle;
	private PartnerListFragment listFragment;
	private NavigationDrawerFragment mNavigationDrawerFragment;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_partner_list);	
		mNavigationDrawerFragment = (NavigationDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
		// Set up the drawer.
		mNavigationDrawerFragment.setUp(R.id.navigation_drawer,	(DrawerLayout) findViewById(R.id.drawer_layout));

		listFragment = (PartnerListFragment) getFragmentManager().findFragmentById(R.id.partner_list);

		getActionBar().setDisplayHomeAsUpEnabled(true);
		mTitle = getTitle();

		if (findViewById(R.id.partner_detail_container) != null) {
			mTwoPane = true;
			((PartnerListFragment) getFragmentManager().findFragmentById(R.id.partner_list)).setActivateOnItemClick(true);
		}

		// TODO: If exposing deep links into your app, handle intents here.
	}

	public void restoreActionBar() {
		ActionBar actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setTitle(mTitle);
	}
	/**
	 * Callback method from {@link ItemListFragment.Callbacks} indicating that
	 * the item with the given ID was selected.
	 */
	
	@Override
	public void onNavigationDrawerItemSelected(int position) {

		switch (position) {
		case 0:
			mTitle = getString(R.string.title_clientes);
			this.listFragment.refreshPartners(CUSTOMERS);
			break;	
		case 1:
			mTitle = getString(R.string.title_suppliers);
			this.listFragment.refreshPartners(SUPPLIERS);
			break;				
		default:
			finish(); 
			break;			
		}

		restoreActionBar();
	}
	
	@Override
	public void onItemSelected(String id) {
		if (mTwoPane) {
			// In two-pane mode, show the detail view in this activity by
			// adding or replacing the detail fragment using a
			// fragment transaction.
			Bundle arguments = new Bundle();
			arguments.putString(PartnerDetailFragment.ARG_ITEM_ID, id);
			PartnerDetailFragment fragment = new PartnerDetailFragment();
			fragment.setArguments(arguments);
			getFragmentManager().beginTransaction()
					.replace(R.id.partner_detail_container, fragment).commit();

		} else {
			// In single-pane mode, simply start the detail activity
			// for the selected item ID.
			Intent detailIntent = new Intent(this, PartnerDetailActivity.class);
			detailIntent.putExtra(PartnerDetailFragment.ARG_ITEM_ID, id);
			startActivity(detailIntent);
		}
	}
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == android.R.id.home) {
			NavUtils.navigateUpTo(this, new Intent(this,AntonLauncherActivity.class));
			return true;
		}		
		return super.onOptionsItemSelected(item);
	}	
}
