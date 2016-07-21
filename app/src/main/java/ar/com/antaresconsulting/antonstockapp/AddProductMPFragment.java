package ar.com.antaresconsulting.antonstockapp;

import java.util.HashMap;
import com.openerp.WriteAsyncTask;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import ar.com.antaresconsulting.antonstockapp.model.BaseProduct;
import ar.com.antaresconsulting.antonstockapp.model.MateriaPrima;
import ar.com.antaresconsulting.antonstockapp.model.SelectionObject;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass. Use the
 * {@link AddProductMPFragment#newInstance} factory method to create an instance
 * of this fragment.
 * 
 */
public class AddProductMPFragment extends Fragment implements AddProductInterface {

	private WriteAsyncTask saveData;
	private Spinner color;
	private Spinner material;
	private Spinner finished;
	private static final String ARG_PARAM1 = "param1";

	
	public static AddProductMPFragment newInstance(BaseProduct param1) {
		AddProductMPFragment fragment = new AddProductMPFragment();
		Bundle args = new Bundle();
		args.putSerializable(ARG_PARAM1, param1);
		fragment.setArguments(args);
		return fragment;
	}

	public AddProductMPFragment() {
		// Required empty public constructor
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);	
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = null;
		rootView =inflater.inflate(R.layout.fragment_add_product_mp, container,	false);
		material = (Spinner) rootView.findViewById(R.id.tmaterial);
		color = (Spinner) rootView.findViewById(R.id.color);
		finished = (Spinner) rootView.findViewById(R.id.terminacion);
		BaseProduct idProd = (BaseProduct) getArguments().getSerializable(ARG_PARAM1);
		if(idProd != null){
			MateriaPrima mp = (MateriaPrima) idProd;
			((AddProductsCallbacks)getActivity()).setProductDetail(idProd);
			color.setSelection(SelectionObject.getIndexInArray(getResources().getStringArray(R.array.color),mp.getColor().getName()));
			material.setSelection(SelectionObject.getIndexInArray(getResources().getStringArray(R.array.tipoMaterial),mp.getMaterial().getName()));
			finished.setSelection(SelectionObject.getIndexInArray(getResources().getStringArray(R.array.terminaciones),mp.getFinished().getName()));
		}		
		return rootView;
	}

	@Override
	public HashMap<String, Object> addProduct(HashMap<String, Object> params){
		params.put("material", ((String)material.getSelectedItem()).toLowerCase().substring(0, 3));
		params.put("color", ((String)color.getSelectedItem()).toLowerCase().substring(0, 3));
		params.put("finished",((String)finished.getSelectedItem()).toLowerCase().substring(0, 3));
		BaseProduct idProd = (BaseProduct) getArguments().getSerializable(ARG_PARAM1);		
		if(idProd == null){
			params.put("type", "product");			
			params.put("uom_id", AntonConstants.UOM_MP);
			params.put("uom_po_id", AntonConstants.UOM_MP);		
			params.put("categ_id", AntonConstants.CATEGORY_MP);			
		}
		return params;

	}


}
