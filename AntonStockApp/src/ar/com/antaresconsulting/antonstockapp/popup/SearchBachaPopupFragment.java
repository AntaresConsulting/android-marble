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
import ar.com.antaresconsulting.antonstockapp.util.AntonConstants;

public class SearchBachaPopupFragment extends DialogFragment implements BachasDAO.BachasCallbacks{

	private Spinner lTipoMaterial;
	private Spinner lMarca;
	private Spinner lTipo;
	private Spinner lAcero;
	private Spinner lColocacion;	
	private AutoCompleteTextView lNombre;
	private BachasDAO pDao;
	private int productoSelecPos;
	private ProgressBar lNombreProg;



	private View view;
	
	public interface SearchProductListener{
	     void searchProductsBacha();
	}
	 
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		// Get the layout inflater
		LayoutInflater inflater = getActivity().getLayoutInflater();
		view = inflater.inflate(R.layout.popup_search_bacha, null);


		SharedPreferences sharedPref = getActivity().getSharedPreferences(getString(R.string.saved_bacha_search),Context.MODE_PRIVATE);

		String tipoMaterial = sharedPref.getString(getString(R.string.search_bacha_material),"");
		String marca = sharedPref.getString(getString(R.string.search_bacha_marca),"");
		String nombreProd = sharedPref.getString(getString(R.string.search_nombre),"");
		String tbacha = sharedPref.getString(getString(R.string.search_bacha_tipo),"");
		String acero = sharedPref.getString(getString(R.string.search_bacha_acero),"");
		String colocacion = sharedPref.getString(getString(R.string.search_bacha_colocacion),"");

		
		lTipoMaterial = (Spinner) view.findViewById(R.id.tipoMaterial);
		lMarca = (Spinner) view.findViewById(R.id.marca);
		lNombreProg = (ProgressBar) view.findViewById(R.id.loading_spinner);
		lAcero = (Spinner) view.findViewById(R.id.aceroBacha);
		lColocacion = (Spinner) view.findViewById(R.id.colocacionBacha);
		lTipo = (Spinner) view.findViewById(R.id.tipoBacha);
		
		lAcero.setAdapter(new ArrayAdapter<SelectionObject>(this.getActivity(),android.R.layout.simple_list_item_1,SelectionObject.getAceroBachaData()));
		lColocacion.setAdapter(new ArrayAdapter<SelectionObject>(this.getActivity(),android.R.layout.simple_list_item_1,SelectionObject.getColocacionBachaData()));
		lTipo.setAdapter(new ArrayAdapter<SelectionObject>(this.getActivity(),android.R.layout.simple_list_item_1,SelectionObject.getTipoBachaData()));
		lTipoMaterial.setAdapter(new ArrayAdapter<SelectionObject>(this.getActivity(),android.R.layout.simple_list_item_1,SelectionObject.getTipoBachaData()));
		
		if(tipoMaterial.equalsIgnoreCase(AntonConstants.ACERO)){
			lMarca.setAdapter(new ArrayAdapter<SelectionObject>(this.getActivity(),android.R.layout.simple_list_item_1,SelectionObject.getMarcaAceroBachaData()));					
			lMarca.setSelection(SelectionObject.getIndexInList(SelectionObject.getMarcaAceroBachaData(),marca));
		}else{
			lMarca.setAdapter(new ArrayAdapter<SelectionObject>(this.getActivity(),android.R.layout.simple_list_item_1,SelectionObject.getMarcaLosaBachaData()));
			lMarca.setSelection(SelectionObject.getIndexInList(SelectionObject.getMarcaLosaBachaData(),marca));
		}
		
		lTipoMaterial.setSelection(((ArrayAdapter<SelectionObject>)lTipoMaterial.getAdapter()).getPosition(SelectionObject.getBachaMaterial(tipoMaterial)));
		lAcero.setSelection(SelectionObject.getIndexInList(SelectionObject.getAceroBachaData(),acero));
		lColocacion.setSelection(SelectionObject.getIndexInList(SelectionObject.getColocacionBachaData(),colocacion));
		lTipo.setSelection(SelectionObject.getIndexInList(SelectionObject.getTipoBachaData(),tbacha));

		lNombre = (AutoCompleteTextView) view.findViewById(R.id.nombreProd);

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
		pDao = new BachasDAO(this);
		pDao.getBachasNames();
		// Inflate and set the layout for the dialog
		// Pass null as the parent view because its going in the dialog layout
		builder.setView(view).setTitle(getString(R.string.search_bacha_title))
		// Add action buttons
		.setPositiveButton(R.string.search_btn, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int id) {
				SharedPreferences sharedPref = getActivity().getSharedPreferences(getString(R.string.saved_bacha_search),Context.MODE_PRIVATE);
				SharedPreferences.Editor editor = sharedPref.edit();
				editor.putString(getString(R.string.search_bacha_material), lTipoMaterial.getSelectedItem().toString() );
				editor.putString(getString(R.string.search_bacha_marca), lMarca.getSelectedItem().toString() );
				editor.putString(getString(R.string.search_bacha_nombre), lNombre.getText().toString());
				editor.putString(getString(R.string.search_bacha_acero), lAcero.getSelectedItem().toString());
				editor.putString(getString(R.string.search_bacha_tipo), lTipo.getSelectedItem().toString());
				editor.putString(getString(R.string.search_bacha_colocacion), lColocacion.getSelectedItem().toString());
				
				editor.commit();
				Fragment parentFragment = getTargetFragment();
				if(parentFragment == null)
					((SearchProductListener) getActivity()).searchProductsBacha();
				else
					((SearchProductListener) parentFragment).searchProductsBacha();
			}
		})
		.setNegativeButton(R.string.cancel_btn, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				SearchBachaPopupFragment.this.getDialog().cancel();
			}
		});      
		return builder.create();
	}

	@Override
	public void setBachas() {
		lNombreProg.setVisibility(View.GONE);
		lNombre.setVisibility(View.VISIBLE);		
		ArrayAdapter<String> adapterProd = new ArrayAdapter<String>(this.getActivity(),android.R.layout.simple_list_item_1,this.pDao.getBachasNamesList());
		lNombre.setAdapter(adapterProd);			
	}


}
