package ar.com.antaresconsulting.antonstockapp.popup;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import ar.com.antaresconsulting.antonstockapp.R;
import ar.com.antaresconsulting.antonstockapp.R.id;
import ar.com.antaresconsulting.antonstockapp.R.layout;
import ar.com.antaresconsulting.antonstockapp.R.string;
import ar.com.antaresconsulting.antonstockapp.model.Bacha;
import ar.com.antaresconsulting.antonstockapp.model.SelectionObject;
import ar.com.antaresconsulting.antonstockapp.model.dao.BachasDAO;
import ar.com.antaresconsulting.antonstockapp.model.dao.InsumosDAO;
import ar.com.antaresconsulting.antonstockapp.model.dao.InsumosDAO.InsumosCallbacks;

public class SearchInsumoPopupFragment extends DialogFragment implements InsumosDAO.InsumosCallbacks{


	private AutoCompleteTextView lNombre;
	private InsumosDAO insuDao;
	private int productoSelecPos;
	private ProgressBar lNombreProg;



	private View view;
	
	public interface SearchProductListener{
	     void searchProductsInsumos();
	}
	 
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		// Get the layout inflater
		LayoutInflater inflater = getActivity().getLayoutInflater();
		view = inflater.inflate(R.layout.fragment_search_insumo, null);


		SharedPreferences sharedPref = getActivity().getSharedPreferences(getString(R.string.saved_insumo_search),Context.MODE_PRIVATE);


		String nombreProd = sharedPref.getString(getString(R.string.search_nombre),"");

		lNombre = (AutoCompleteTextView) view.findViewById(R.id.nombreProd);
		lNombreProg = (ProgressBar) view.findViewById(R.id.loading_spinner);
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
				productoSelecPos= pos;
			}
		});	
		insuDao = new InsumosDAO(this);
		insuDao.getInsumosNames();
		// Inflate and set the layout for the dialog
		// Pass null as the parent view because its going in the dialog layout
		builder.setView(view).setTitle(getString(R.string.search_insumo_title))
		// Add action buttons
		.setPositiveButton(R.string.search_btn, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int id) {
				SharedPreferences sharedPref = getActivity().getSharedPreferences(getString(R.string.saved_insumo_search),Context.MODE_PRIVATE);
				SharedPreferences.Editor editor = sharedPref.edit();
				editor.putString(getString(R.string.search_bacha_nombre), lNombre.getText().toString());
				editor.commit();
				Fragment parentFragment = getTargetFragment();
				if(parentFragment == null)
					((SearchProductListener) getActivity()).searchProductsInsumos();
				else
					((SearchProductListener) parentFragment).searchProductsInsumos();


			}
		})
		.setNegativeButton(R.string.cancel_btn, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				SearchInsumoPopupFragment.this.getDialog().cancel();
			}
		});      
		return builder.create();
	}

	@Override
	public void setInsumos() {
		lNombreProg.setVisibility(View.GONE);
		lNombre.setVisibility(View.VISIBLE);		
		ArrayAdapter<String> adapterProd = new ArrayAdapter<String>(this.getActivity(),android.R.layout.simple_list_item_1,this.insuDao.getInsumosNamesList());
		lNombre.setAdapter(adapterProd);			
	}


}
