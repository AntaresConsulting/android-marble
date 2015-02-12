package ar.com.antaresconsulting.antonstockapp.popup;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import ar.com.antaresconsulting.antonstockapp.R;
import ar.com.antaresconsulting.antonstockapp.model.Partner;
import ar.com.antaresconsulting.antonstockapp.model.dao.PartnerDAO;

public class AssignSuppPopupFragment extends DialogFragment implements PartnerDAO.SuppliersCallbacks  {

	private View view;
	private AutoCompleteTextView lNombre;
	private PartnerDAO pDao;
	private Partner selectSupp;


	public interface AssignSuppListener{
		void assignSupplierAction(Integer i);
	}

	public static AssignSuppPopupFragment newInstance(int num) {
		AssignSuppPopupFragment f = new AssignSuppPopupFragment();
	    Bundle args = new Bundle();
	    args.putInt("tprod", num);
	    f.setArguments(args);
	    return f;
	}
	
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		// Get the layout inflater
		LayoutInflater inflater = getActivity().getLayoutInflater();
		view = inflater.inflate(R.layout.fragment_assign_supp, null);
		lNombre = (AutoCompleteTextView) view.findViewById(R.id.nombreSupp);
		lNombre.setOnFocusChangeListener( new View.OnFocusChangeListener() {
			public void onFocusChange( View v, boolean hasFocus ) {
				if( hasFocus ) {
					lNombre.setText( "", TextView.BufferType.EDITABLE );
				}
			}

		} );

		lNombre.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View arg1, int pos,
					long id) {
					selectSupp = (Partner) lNombre.getAdapter().getItem(pos);
			}
		});	
		pDao = new PartnerDAO(this);
		pDao.getAllSuppliers();
		// Inflate and set the layout for the dialog
		// Pass null as the parent view because its going in the dialog layout
		builder.setView(view).setTitle(getString(R.string.update_stock_title))
		// Add action buttons
		.setPositiveButton(R.string.update_btn, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int id) {
					Fragment parentFragment = getTargetFragment();
					if(parentFragment == null)
						((AssignSuppListener) getActivity()).assignSupplierAction(selectSupp.getId());
					else
						((AssignSuppListener) parentFragment).assignSupplierAction(selectSupp.getId());
				
			}
		})
		.setNegativeButton(R.string.cancel_btn, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
					AssignSuppPopupFragment.this.getDialog().cancel();
			}
		});      
		return builder.create();
	}

	@Override
	public void setSuppliers() {
		ArrayAdapter<Partner> adapterProd = new ArrayAdapter<Partner>(this.getActivity(),android.R.layout.simple_list_item_1,this.pDao.getPartnersArray());
		lNombre.setAdapter(adapterProd);		
	}

	@Override
	public void setSuppliersProd() {
		// TODO Auto-generated method stub
		
	}

}
