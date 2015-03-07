package ar.com.antaresconsulting.antonstockapp.partner;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import ar.com.antaresconsulting.antonstockapp.R;
import ar.com.antaresconsulting.antonstockapp.R.layout;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass. Use the
 * {@link PartnerTypeFragment#newInstance} factory method to create an instance
 * of this fragment.
 * 
 */
public class PartnerTypeFragment extends Fragment {


	public PartnerTypeFragment() {
		// Required empty public constructor
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View mainView = inflater.inflate(R.layout.fragment_partner_type, container,	false);
		
		return mainView;
	}
	
}
