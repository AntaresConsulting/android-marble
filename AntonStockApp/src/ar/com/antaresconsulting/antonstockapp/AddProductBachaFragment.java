package ar.com.antaresconsulting.antonstockapp;

import java.util.HashMap;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import ar.com.antaresconsulting.antonstockapp.model.Bacha;
import ar.com.antaresconsulting.antonstockapp.model.BaseProduct;
import ar.com.antaresconsulting.antonstockapp.model.MateriaPrima;
import ar.com.antaresconsulting.antonstockapp.model.SelectionObject;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass. Use the
 * {@link AddProductBachaFragment#newInstance} factory method to create an instance
 * of this fragment.
 * 
 */
public class AddProductBachaFragment extends Fragment implements AddProductInterface {

	private Spinner lTipoMaterial;
	private Spinner lMarca;
	private static final String ARG_PARAM1 = "param1";

	
	public static AddProductBachaFragment newInstance(BaseProduct param1) {
		AddProductBachaFragment fragment = new AddProductBachaFragment();
		Bundle args = new Bundle();
		args.putSerializable(ARG_PARAM1, param1);
		fragment.setArguments(args);
		return fragment;
	}

	public AddProductBachaFragment() {
		// Required empty public constructor
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_add_product_bacha, container,	false);
			
			lTipoMaterial = (Spinner) rootView.findViewById(R.id.tmaterial);
			lMarca = (Spinner) rootView.findViewById(R.id.marca);
			
			lMarca.setAdapter(new ArrayAdapter<SelectionObject>(this.getActivity(),android.R.layout.simple_list_item_1,SelectionObject.getMarcaData()));
			lTipoMaterial.setAdapter(new ArrayAdapter<SelectionObject>(this.getActivity(),android.R.layout.simple_list_item_1,SelectionObject.getTipoBachaData()));
			BaseProduct idProd = (BaseProduct) getArguments().getSerializable(ARG_PARAM1);
			if(idProd != null){
				Bacha mp = (Bacha) idProd;
				((AddProductsCallbacks)getActivity()).setProductDetail(idProd);
				lTipoMaterial.setSelection(SelectionObject.getIndexInList(SelectionObject.getTipoBachaData(),mp.getTipoMaterial().getId()));
				lMarca.setSelection(SelectionObject.getIndexInList(SelectionObject.getMarcaData(),mp.getMarca().getId()));
			}	
			
			return rootView;
	}


	@Override
	public HashMap<String, Object> addProduct(HashMap<String, Object> params){
		params.put("type", "product");
		params.put("bacha_material", ((SelectionObject)lTipoMaterial.getSelectedItem()).getId());
		params.put("bacha_marca", ((SelectionObject)lMarca.getSelectedItem()).getId());
		BaseProduct idProd = (BaseProduct) getArguments().getSerializable(ARG_PARAM1);
		
		if(idProd == null){		
			params.put("uom_id", AntonConstants.UOM_BACHA);
			params.put("uom_po_id", AntonConstants.UOM_BACHA);		
			params.put("categ_id", AntonConstants.CATEGORY_BACHA);
		}
		return params;
		
	}

}
