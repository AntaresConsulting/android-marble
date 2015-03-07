package ar.com.antaresconsulting.antonstockapp.popup;

import java.math.BigDecimal;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import ar.com.antaresconsulting.antonstockapp.R;

public class OrderPointPopupFragment extends DialogFragment  {

	private View view;
	private EditText minqty;
	private EditText maxqty;	


	public interface  OrderPointListener{
		void setOrderRule(BigDecimal min,BigDecimal max);
	}

	public static OrderPointPopupFragment newInstance(int num) {
		OrderPointPopupFragment f = new OrderPointPopupFragment();
	    Bundle args = new Bundle();
	    args.putInt("tprod", num);
	    f.setArguments(args);
	    return f;
	}
	
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		// Get the layout inflater
		LayoutInflater inflater = getActivity().getLayoutInflater();
		view = inflater.inflate(R.layout.popup_orderpoint, null);
		minqty = (EditText) view.findViewById(R.id.minQty);
		maxqty = (EditText) view.findViewById(R.id.maxQty);

		// Inflate and set the layout for the dialog
		// Pass null as the parent view because its going in the dialog layout
		builder.setView(view).setTitle(getString(R.string.update_stock_title))
		// Add action buttons
		.setPositiveButton(R.string.update_btn, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int id) {
					Fragment parentFragment = getTargetFragment();
					if(parentFragment == null)
						((OrderPointListener) getActivity()).setOrderRule(new BigDecimal(minqty.getText().toString()),new BigDecimal(maxqty.getText().toString()));
					else
						((OrderPointListener) parentFragment).setOrderRule(new BigDecimal(minqty.getText().toString()),new BigDecimal(maxqty.getText().toString()));
				
			}
		})
		.setNegativeButton(R.string.cancel_btn, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
					OrderPointPopupFragment.this.getDialog().cancel();
			}
		});      
		return builder.create();
	}


}
