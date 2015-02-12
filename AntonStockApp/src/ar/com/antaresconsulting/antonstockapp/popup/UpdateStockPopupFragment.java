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
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import ar.com.antaresconsulting.antonstockapp.AntonConstants;
import ar.com.antaresconsulting.antonstockapp.R;
import ar.com.antaresconsulting.antonstockapp.R.id;
import ar.com.antaresconsulting.antonstockapp.R.layout;
import ar.com.antaresconsulting.antonstockapp.R.string;
import ar.com.antaresconsulting.antonstockapp.model.Dimension;
import ar.com.antaresconsulting.antonstockapp.model.SelectionObject;
import ar.com.antaresconsulting.antonstockapp.model.dao.MateriaPrimaDAO;

public class UpdateStockPopupFragment extends DialogFragment  {

	private View view;
	private EditText cant;
	private EditText alto;
	private EditText ancho;
	private Spinner espesor;
	private Spinner tipo;
	private Dimension dim;
	private int tprod;

	public interface UpdateStockListener{
		void updateStockAction(int i, Dimension dim);
	}

	public static UpdateStockPopupFragment newInstance(int num) {
		UpdateStockPopupFragment f = new UpdateStockPopupFragment();
	    Bundle args = new Bundle();
	    args.putInt("tprod", num);
	    f.setArguments(args);
	    return f;
	}
	
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		// Get the layout inflater
		LayoutInflater inflater = getActivity().getLayoutInflater();
	    tprod = getArguments().getInt("tprod");
	    switch (tprod) {
		case AntonConstants.MATERIA_PRIMA:
			view = inflater.inflate(R.layout.fragment_ustock_mp, null);
			espesor = (Spinner) view.findViewById(R.id.espesorDim);
			espesor.setSelection(AntonConstants.DEFAULT_ESPESORES);
			alto = (EditText) view.findViewById(R.id.largoDim);
			ancho = (EditText) view.findViewById(R.id.anchoDim);
			tipo = (Spinner) view.findViewById(R.id.tiposDim);
			tipo.setAdapter(new ArrayAdapter<SelectionObject>(this.getActivity(),android.R.layout.simple_list_item_1,SelectionObject.getDimTipoData()));			
			break;

		default:
			view = inflater.inflate(R.layout.fragment_ustock, null);
			break;
		}
	    
	    cant = (EditText) view.findViewById(R.id.cantUStock);
		// Inflate and set the layout for the dialog
		// Pass null as the parent view because its going in the dialog layout
		builder.setView(view).setTitle(getString(R.string.update_stock_title))
		// Add action buttons
		.setPositiveButton(R.string.update_btn, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int id) {
					Fragment parentFragment = getTargetFragment();
				    switch (tprod) {
						case AntonConstants.MATERIA_PRIMA:
						dim = new Dimension(alto.getText().toString(),ancho.getText().toString(),espesor.getSelectedItem().toString(),(SelectionObject) tipo.getSelectedItem());
						break;
						default:
						break;
					}					
					if(parentFragment == null)
						((UpdateStockListener) getActivity()).updateStockAction(Integer.parseInt(cant.getText().toString()),dim);
					else
						((UpdateStockListener) parentFragment).updateStockAction(Integer.parseInt(cant.getText().toString()),dim);
				
			}
		})
		.setNegativeButton(R.string.cancel_btn, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
					UpdateStockPopupFragment.this.getDialog().cancel();
			}
		});      
		return builder.create();
	}

}
