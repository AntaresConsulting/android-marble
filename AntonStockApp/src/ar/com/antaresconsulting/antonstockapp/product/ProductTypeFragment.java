package ar.com.antaresconsulting.antonstockapp.product;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import ar.com.antaresconsulting.antonstockapp.R;
import ar.com.antaresconsulting.antonstockapp.R.id;
import ar.com.antaresconsulting.antonstockapp.R.layout;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass. Use the
 * {@link ProductTypeFragment#newInstance} factory method to create an instance
 * of this fragment.
 * 
 */
public class ProductTypeFragment extends Fragment {
	// TODO: Rename parameter arguments, choose names that match
	// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
	private static final String ARG_PARAM1 = "param1";
	private static final String ARG_PARAM2 = "param2";
	private static final String ARG_PARAM3 = "param3";
	private static final String ARG_PARAM4 = "param4";

	// TODO: Rename and change types of parameters
	private boolean mp;
	private boolean bacha;
	private boolean insumos;
	private boolean servicios;

	/**
	 * Use this factory method to create a new instance of this fragment using
	 * the provided parameters.
	 * 
	 * @param param1
	 *            Parameter 1.
	 * @param param2
	 *            Parameter 2.
	 * @return A new instance of fragment ProductTypeFragment.
	 */
	// TODO: Rename and change types and number of parameters
	public static ProductTypeFragment newInstance(boolean mp,boolean bacha,boolean insu,boolean servicios) {
		ProductTypeFragment fragment = new ProductTypeFragment();
		Bundle args = new Bundle();
		args.putBoolean(ARG_PARAM1, mp);
		args.putBoolean(ARG_PARAM2, bacha);
		args.putBoolean(ARG_PARAM3, insu);
		args.putBoolean(ARG_PARAM4, servicios);
		fragment.setArguments(args);
		return fragment;
	}

	public ProductTypeFragment() {
		// Required empty public constructor
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			mp = getArguments().getBoolean(ARG_PARAM1);
			bacha = getArguments().getBoolean(ARG_PARAM2);
			insumos = getArguments().getBoolean(ARG_PARAM3);
			servicios = getArguments().getBoolean(ARG_PARAM4);
		}
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View mainView = inflater.inflate(R.layout.fragment_product_type, container,	false);
		
		if(!insumos)
			mainView.findViewById(R.id.insumosButton).setVisibility(View.GONE);
		
		if(!bacha)
			mainView.findViewById(R.id.bachasButton).setVisibility(View.GONE);
		
		if(!mp)
			mainView.findViewById(R.id.mpButton).setVisibility(View.GONE);
		
		if(!servicios)
			mainView.findViewById(R.id.serviciosButton).setVisibility(View.GONE);		
		return mainView;
	}
	
}
