package ar.com.antaresconsulting.antonstockapp;

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
import ar.com.antaresconsulting.antonstockapp.model.dao.MateriaPrimaDAO;

public class SearchMPPopupFragment extends DialogFragment implements MateriaPrimaDAO.MateriaPrimaCallbacks {

	private Spinner lTipoMaterial;
	private Spinner lColor;
	private Spinner lAcabado;
	private AutoCompleteTextView lNombre;
	private ProgressBar lNombreProg;
	private MateriaPrimaDAO pDao;
	private View view;

	public interface SearchProductListener{
		void searchProductsMP();
	}

	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		// Get the layout inflater
		LayoutInflater inflater = getActivity().getLayoutInflater();
		view = inflater.inflate(R.layout.fragment_search_mp, null);


		SharedPreferences sharedPref = getActivity().getSharedPreferences(getString(R.string.saved_mp_search),Context.MODE_PRIVATE);

		String tm = sharedPref.getString(getString(R.string.search_tipo),"");
		String color = sharedPref.getString(getString(R.string.search_color),"");
		String acabado = sharedPref.getString(getString(R.string.search_terminado),"");
		String nombreProd = sharedPref.getString(getString(R.string.search_nombre),"");

		lTipoMaterial = (Spinner) view.findViewById(R.id.tipoMaterial);
		lColor = (Spinner) view.findViewById(R.id.color);
		lAcabado = (Spinner) view.findViewById(R.id.acabado);
		lNombre = (AutoCompleteTextView) view.findViewById(R.id.nombreProd);
		lNombreProg = (ProgressBar) view.findViewById(R.id.loading_spinner);
		getResources().getStringArray(R.array.tipoMaterial);

		lTipoMaterial.setSelection(((ArrayAdapter<String>)lTipoMaterial.getAdapter()).getPosition(tm));
		lColor.setSelection(((ArrayAdapter<String>)lColor.getAdapter()).getPosition(color));
		lAcabado.setSelection(((ArrayAdapter<String>)lAcabado.getAdapter()).getPosition(acabado));
		lNombre.setText(nombreProd);


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
			}
		});	
		pDao =  new MateriaPrimaDAO(this);
		pDao.getMateriaPrimaNames();

		// Inflate and set the layout for the dialog
		// Pass null as the parent view because its going in the dialog layout
		builder.setView(view).setTitle(getString(R.string.search_mp_title))
		// Add action buttons
		.setPositiveButton(R.string.search_btn, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int id) {
					SharedPreferences sharedPref = getActivity().getSharedPreferences(getString(R.string.saved_mp_search),Context.MODE_PRIVATE);
					SharedPreferences.Editor editor = sharedPref.edit();
					editor.putString(getString(R.string.search_tipo), lTipoMaterial.getSelectedItem().toString() );
					editor.putString(getString(R.string.search_color), lColor.getSelectedItem().toString() );
					editor.putString(getString(R.string.search_terminado), lAcabado.getSelectedItem().toString() );
					editor.putString(getString(R.string.search_nombre), lNombre.getText().toString() );
					editor.commit();
					Fragment parentFragment = getTargetFragment();
					if(parentFragment == null)
						((SearchProductListener) getActivity()).searchProductsMP();
					else
						((SearchProductListener) parentFragment).searchProductsMP();
				
			}
		})
		.setNegativeButton(R.string.cancel_btn, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
					SearchMPPopupFragment.this.getDialog().cancel();
			}
		});      
		return builder.create();
	}

	@Override
	public void setMateriaPrima() {
		lNombreProg.setVisibility(View.GONE);
		lNombre.setVisibility(View.VISIBLE);
		ArrayAdapter<String> adapterProd = new ArrayAdapter<String>(this.getActivity(),android.R.layout.simple_list_item_1,this.pDao.getMateriaPrimasNamesList());
		lNombre.setAdapter(adapterProd);
	}

}
