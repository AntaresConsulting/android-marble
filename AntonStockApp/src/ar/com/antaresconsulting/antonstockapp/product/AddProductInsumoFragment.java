package ar.com.antaresconsulting.antonstockapp.product;

import java.util.HashMap;
import java.util.List;

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
import ar.com.antaresconsulting.antonstockapp.model.Insumo;
import ar.com.antaresconsulting.antonstockapp.model.SelectionObject;
import ar.com.antaresconsulting.antonstockapp.model.dao.ProductDAO;
import ar.com.antaresconsulting.antonstockapp.util.AntonConstants;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass. Use the
 * {@link AddProductInsumoFragment#newInstance} factory method to create an instance
 * of this fragment.
 * 
 */
public class AddProductInsumoFragment extends Fragment implements AddProductInterface,ProductDAO.UomsCallbacks{
	private ProductDAO prodDao;
	private Spinner uoms;
	private EditText desc;
	private static final String ARG_PARAM1 = "param1";
	private Insumo idProd;
	
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
		desc = (EditText) rootView.findViewById(R.id.insuDesc);
		prodDao = new ProductDAO(this);
		prodDao.getUOMS();
		idProd = (Insumo) getArguments().getSerializable(ARG_PARAM1);
		if(idProd != null){
			((AddProductsCallbacks)getActivity()).setProductDetail(idProd);			
			desc.setText(idProd.getDescription());
		}
		return rootView;
	}


	@Override
	public HashMap<String, Object> addProduct(HashMap<String, Object> params){
		BaseProduct idProd = (BaseProduct) getArguments().getSerializable(ARG_PARAM1);
		
		if(idProd == null){
			params.put("type", "consu");
			params.put("uom_id", ((SelectionObject)uoms.getSelectedItem()).getId());
			params.put("uom_po_id", ((SelectionObject)uoms.getSelectedItem()).getId());
			params.put("movile_categ_name", AntonConstants.CATEGORY_INSUMO);
		}
		params.put("description", this.desc.getText().toString());
		return params;
		
	}

	@Override
	public void setUoms() {
		ArrayAdapter<SelectionObject> dataUoms =  new ArrayAdapter<SelectionObject>(this.getActivity(),android.R.layout.simple_list_item_1,this.prodDao.getUomList());
		this.uoms.setAdapter(dataUoms);
		if(idProd != null){
			int maxUom = dataUoms.getCount();
			for (int j = 0; j < maxUom; j++) {
				SelectionObject objAux =  dataUoms.getItem(j);
				if(objAux.getId().equalsIgnoreCase(idProd.getUomSO().getId()))
					uoms.setSelection(j);
			}	
		}				
	}

}
