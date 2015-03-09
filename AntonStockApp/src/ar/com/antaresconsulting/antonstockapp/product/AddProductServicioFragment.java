package ar.com.antaresconsulting.antonstockapp.product;

import java.util.HashMap;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import ar.com.antaresconsulting.antonstockapp.R;
import ar.com.antaresconsulting.antonstockapp.R.id;
import ar.com.antaresconsulting.antonstockapp.R.layout;
import ar.com.antaresconsulting.antonstockapp.model.Bacha;
import ar.com.antaresconsulting.antonstockapp.model.BaseProduct;
import ar.com.antaresconsulting.antonstockapp.model.SelectionObject;
import ar.com.antaresconsulting.antonstockapp.model.Servicio;
import ar.com.antaresconsulting.antonstockapp.model.dao.ProductDAO;
import ar.com.antaresconsulting.antonstockapp.util.AntonConstants;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass. Use the
 * {@link AddProductServicioFragment#newInstance} factory method to create an instance
 * of this fragment.
 * 
 */
public class AddProductServicioFragment extends Fragment implements AddProductInterface,ProductDAO.UomsCallbacks{
	private ProductDAO prodDao;
	private Spinner uoms;
	private EditText desc;
	private static final String ARG_PARAM1 = "param1";

	
	public static AddProductServicioFragment newInstance(BaseProduct param1) {
		AddProductServicioFragment fragment = new AddProductServicioFragment();
		Bundle args = new Bundle();
		args.putSerializable(ARG_PARAM1, param1);
		fragment.setArguments(args);
		return fragment;
	}

	public AddProductServicioFragment() {
		// Required empty public constructor
	}

	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_add_product_servicio, container,	false);
		BaseProduct idProd = (BaseProduct) getArguments().getSerializable(ARG_PARAM1);
		if(idProd != null){
			Servicio mp = (Servicio) idProd;
			((AddProductsCallbacks)getActivity()).setProductDetail(idProd);
		}
		uoms = (Spinner) rootView.findViewById(R.id.uomVal);
		desc = (EditText) rootView.findViewById(R.id.insuDesc);
		
		prodDao = new ProductDAO(this);
		prodDao.getUOMS();
		return rootView;
	}


	@Override
	public HashMap<String, Object> addProduct(HashMap<String, Object> params){
		BaseProduct idProd = (BaseProduct) getArguments().getSerializable(ARG_PARAM1);
		
		if(idProd == null){
			params.put("type", "service");
			params.put("uom_id", ((SelectionObject)uoms.getSelectedItem()).getId());
			params.put("uom_po_id", ((SelectionObject)uoms.getSelectedItem()).getId());
			params.put("movile_categ_name", AntonConstants.CATEGORY_SERVICIO);
		}
		return params;
		
	}

	@Override
	public void setUoms() {
		this.uoms.setAdapter(new ArrayAdapter<SelectionObject>(this.getActivity(),android.R.layout.simple_list_item_1,this.prodDao.getUomList()));
	}

}
