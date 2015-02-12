package ar.com.antaresconsulting.antonstockapp.popup;

import com.openerp.DBAsyncTask;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import ar.com.antaresconsulting.antonstockapp.AntonConstants;
import ar.com.antaresconsulting.antonstockapp.LoginActivityInterface;
import ar.com.antaresconsulting.antonstockapp.R;
import ar.com.antaresconsulting.antonstockapp.R.id;
import ar.com.antaresconsulting.antonstockapp.R.layout;
import ar.com.antaresconsulting.antonstockapp.R.string;

public class ServerPopupFragment extends DialogFragment implements LoginActivityInterface {
	
	private EditText mURL;
	private EditText mpuerto;
	private Spinner mdbname;
	
	private View view;
    public Dialog onCreateDialog(Bundle savedInstanceState) {
	    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	    // Get the layout inflater
	    LayoutInflater inflater = getActivity().getLayoutInflater();
	    view = inflater.inflate(R.layout.fragment_server_popup, null);
	    

 	    SharedPreferences sharedPref = getActivity().getSharedPreferences(getString(R.string.preference_setting),Context.MODE_PRIVATE);
 	    String urlSet = sharedPref.getString(getString(R.string.server_url),AntonConstants.DEFAULT_URL);
 	    String puertoSet = sharedPref.getString(getString(R.string.server_port),AntonConstants.DEFAULT_PORT);
 	    String dbSet = sharedPref.getString(getString(R.string.server_db),AntonConstants.DEFAULT_DB);

	    mURL = (EditText) view.findViewById(R.id.url);
	    mURL.setText(urlSet);
	    mpuerto = (EditText) view.findViewById(R.id.puertodb);
	    mpuerto.setText(puertoSet);
	    mdbname = (Spinner) view.findViewById(R.id.nombredb);

	    // Inflate and set the layout for the dialog
	    // Pass null as the parent view because its going in the dialog layout
	    builder.setView(view)
	    // Add action buttons
	           .setPositiveButton(R.string.save_btn, new DialogInterface.OnClickListener() {
	               @Override
	               public void onClick(DialogInterface dialog, int id) {
	            	   mURL = (EditText) view.findViewById(R.id.url);
	            	   mdbname = (Spinner) view.findViewById(R.id.nombredb);
	           	       mpuerto = (EditText) view.findViewById(R.id.puertodb);

	            	   SharedPreferences sharedPref = getActivity().getSharedPreferences(getString(R.string.preference_setting),Context.MODE_PRIVATE);
	            	   SharedPreferences.Editor editor = sharedPref.edit();
	            	   editor.putString(getString(R.string.server_url), mURL.getText().toString());
	            	   editor.putString(getString(R.string.server_port), mpuerto.getText().toString());
	            	   editor.putString(getString(R.string.server_db), mdbname.getSelectedItem().toString());
	            	   editor.commit();
	               }
	           })
	           .setNegativeButton(R.string.cancel_btn, new DialogInterface.OnClickListener() {
	               public void onClick(DialogInterface dialog, int id) {
	            	   ServerPopupFragment.this.getDialog().cancel();
	               }
	           });      
	    return builder.create();
    }
    
	public void searchDBs(View view) {
		DBAsyncTask dbproc = new DBAsyncTask(getActivity());
		String[] params = new String[]{mURL.getText().toString(),mpuerto.getText().toString()};
		dbproc.execute(params);
	}

	@Override
	public void connectionResolved(Boolean result) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dbList(Object[] dbs) {
	    ArrayAdapter<Object> dataAdapter = new ArrayAdapter<Object>(this.getActivity(), android.R.layout.simple_spinner_item,dbs);
	    mdbname.setAdapter(dataAdapter);		
	}

	@Override
	public void setActivities() {
		// TODO Auto-generated method stub
		
	}
}
