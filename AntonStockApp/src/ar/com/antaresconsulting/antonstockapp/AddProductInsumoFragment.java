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
import ar.com.antaresconsulting.antonstockapp.model.SelectionObject;
import ar.com.antaresconsulting.antonstockapp.model.dao.ProductDAO;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass. Use the
 * {@link AddProductInsumoFragment#newInstance} factory method to create an instance
 * of this fragment.
 * 
 */
public class AddProductInsumoFragment extends Fragment implements AddProductInterface,ProductDAO.UomsCallbacks{
	private ProductDAO prodDao;
	private Spinner uoms;
	private static final String ARG_PARAM1 = "param1";

	
	public static AddProductInsumoFragment newInstance(BaseProduct param1) {
		AddProductInsumoFragment fragment = new AddProductInsumoFragment();
		Bundle args = new Bundle();
		args.putSerializable(ARG_PARAM1, param1);
		fragment.setArguments(args);
		return fragment;
	}

	public AddProductInsumoFragment() {
		// Required empty public constructor
	}

	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_add_product_insumo, container,	false);
		uoms = (Spinner) rootView.findViewById(R.id.uomVal);
		prodDao = new ProductDAO(this);
		prodDao.getUOMS();
		return rootView;
	}


	@Override
	public HashMap<String, Object> addProduct(HashMap<String, Object> params){
		BaseProduct idProd = (BaseProduct) getArguments().getSerializable(ARG_PARAM1);
		
		if(idProd == null){
			params.put("type", "consu");
			params.put("uom_id", ((SelectionObject)uoms.getSelectedItem()).getId());
			params.put("uom_po_id", ((SelectionObject)uoms.getSelectedItem()).getId());
			params.put("categ_id", AntonConstants.CATEGORY_INSUMO);
		}
		return params;
		
	}

	@Override
	public void setUoms() {
		this.uoms.setAdapter(new ArrayAdapter<SelectionObject>(this.getActivity(),android.R.layout.simple_list_item_1,this.prodDao.getUomList()));
	}

}
