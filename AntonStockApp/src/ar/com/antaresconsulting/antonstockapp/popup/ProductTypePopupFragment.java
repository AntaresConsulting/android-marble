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

public class ProductTypePopupFragment extends DialogFragment {

	private View view;

	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		// Get the layout inflater
		LayoutInflater inflater = getActivity().getLayoutInflater();
		view = inflater.inflate(R.layout.fragment_product_type, null);
		view.findViewById(R.id.mpButton).setVisibility(View.VISIBLE);
		view.findViewById(R.id.insumosButton).setVisibility(View.GONE);
		view.findViewById(R.id.bachasButton).setVisibility(View.VISIBLE);
		view.findViewById(R.id.serviciosButton).setVisibility(View.VISIBLE);
		
		builder.setTitle(getString(R.string.search_product_type));
		builder.setView(view);
		return builder.create();
	}

}
