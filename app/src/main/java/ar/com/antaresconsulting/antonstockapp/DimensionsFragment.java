package ar.com.antaresconsulting.antonstockapp;

import java.util.List;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import ar.com.antaresconsulting.antonstockapp.model.BaseProduct;
import ar.com.antaresconsulting.antonstockapp.model.DimensionBalance;
import ar.com.antaresconsulting.antonstockapp.model.dao.DimensionDAO;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass. Activities that
 * contain this fragment must implement the
 * {@link DimensionsFragment.OnFragmentInteractionListener} interface to handle
 * interaction events. Use the {@link DimensionsFragment#newInstance} factory
 * method to create an instance of this fragment.
 * 
 */
public class DimensionsFragment extends Fragment implements DimensionDAO.DimsCallbacks  {
	// TODO: Rename parameter arguments, choose names that match
	// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
	public static final String ARG_ITEM_ID = "item_id";
	public static final String ARG_TPROD_ID = "tprod_id";
	private TableLayout dimsTable;

	private DimensionDAO dimDao;
	// TODO: Rename and change types of parameters
	private int tProd;
	private BaseProduct idProd;


	/**
	 * Use this factory method to create a new instance of this fragment using
	 * the provided parameters.
	 * 
	 * @param param1
	 *            Parameter 1.
	 * @param param2
	 *            Parameter 2.
	 * @return A new instance of fragment DimensionsFragment.
	 */
	// TODO: Rename and change types and number of parameters
	public static DimensionsFragment newInstance(String param1, int param2) {
		DimensionsFragment fragment = new DimensionsFragment();
		Bundle args = new Bundle();
		args.putString(ARG_ITEM_ID, param1);
		args.putInt(ARG_TPROD_ID, param2);
		fragment.setArguments(args);
		return fragment;
	}

	public DimensionsFragment() {
		// Required empty public constructor
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			tProd = getArguments().getInt(ARG_TPROD_ID);
			idProd = (BaseProduct) getArguments().getSerializable(ARG_ITEM_ID);
		}
		dimDao = new DimensionDAO(this);
		dimDao.getAllDims(idProd.getId(),false);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View root = inflater.inflate(R.layout.fragment_dimensions, container, false);
		dimsTable = (TableLayout) root.findViewById(R.id.dims_table);
		return root;
	}



	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}

	@Override
	public void onDetach() {
		super.onDetach();
	}

	/**
	 * This interface must be implemented by activities that contain this
	 * fragment to allow an interaction in this fragment to be communicated to
	 * the activity and potentially other fragments contained in that activity.
	 * <p>
	 * See the Android Training lesson <a href=
	 * "http://developer.android.com/training/basics/fragments/communicating.html"
	 * >Communicating with Other Fragments</a> for more information.
	 */
	public interface OnFragmentInteractionListener {
		// TODO: Update argument type and name
		public void onFragmentInteraction(Uri uri);
	}

	@Override
	public void setDimensions() {
		List<DimensionBalance> dimensions = this.dimDao.getDimensionList();
		
	    TableRow row;
        TextView t1, t2, t3;

        for (int current = 0; current < dimensions.size(); current++) {
            row = new TableRow(this.getActivity());
 
            t1 = new TextView(this.getActivity());
            t2 = new TextView(this.getActivity());
            t3 = new TextView(this.getActivity());
            
            DimensionBalance dimb = dimensions.get(current);
            t1.setText((String) dimb.getDimId()[1]);
            t2.setText(dimb.getQtyM2().toString());
            t3.setText(dimb.getQtyUnits().toString());
 

            t1.setTextSize(15);
            t2.setTextSize(15);
            t3.setTextSize(15);
            LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            params.topMargin = 10;
            params.bottomMargin = 10;
            t1.setLayoutParams(params);
            t2.setLayoutParams(params);
            t3.setLayoutParams(params);            
            t1.setGravity(Gravity.CENTER_HORIZONTAL);
            t2.setGravity(Gravity.CENTER_HORIZONTAL);
            t3.setGravity(Gravity.CENTER_HORIZONTAL);

            row.addView(t1);
            row.addView(t2);
            row.addView(t3);
 
            dimsTable.addView(row, new TableLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
 
        }
		
	}

}
