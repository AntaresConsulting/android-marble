package ar.com.antaresconsulting.antonstockapp;

import java.util.HashMap;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import ar.com.antaresconsulting.antonstockapp.model.Bacha;
import ar.com.antaresconsulting.antonstockapp.model.BaseProduct;
import ar.com.antaresconsulting.antonstockapp.model.MateriaPrima;
import ar.com.antaresconsulting.antonstockapp.model.Pedido;
import ar.com.antaresconsulting.antonstockapp.model.SelectionObject;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass. Use the
 * {@link AddProductBachaFragment#newInstance} factory method to create an instance
 * of this fragment.
 * 
 */
public class AddProductBachaFragment extends Fragment implements OnItemSelectedListener, AddProductInterface {

	private Spinner lTipoMaterial;
	private Spinner lMarca;
	private Spinner lTipo;
	private Spinner lAcero;
	private Spinner lColocacion;
	private EditText ancho;
	private EditText prof;
	private EditText largo;
	private EditText diam;
	
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
			lAcero = (Spinner) rootView.findViewById(R.id.acero);
			lColocacion = (Spinner) rootView.findViewById(R.id.colocacion);
			lTipo = (Spinner) rootView.findViewById(R.id.tipo);
			ancho = (EditText) rootView.findViewById(R.id.ancho);
			prof = (EditText) rootView.findViewById(R.id.prof);
			largo = (EditText) rootView.findViewById(R.id.largo);
			diam = (EditText) rootView.findViewById(R.id.diametro);
			
			lMarca.setAdapter(new ArrayAdapter<SelectionObject>(this.getActivity(),android.R.layout.simple_list_item_1));
			lAcero.setAdapter(new ArrayAdapter<SelectionObject>(this.getActivity(),android.R.layout.simple_list_item_1,SelectionObject.getAceroBachaData()));
			lColocacion.setAdapter(new ArrayAdapter<SelectionObject>(this.getActivity(),android.R.layout.simple_list_item_1,SelectionObject.getColocacionBachaData()));
			lTipo.setAdapter(new ArrayAdapter<SelectionObject>(this.getActivity(),android.R.layout.simple_list_item_1,SelectionObject.getTipoBachaData()));
			lTipoMaterial.setAdapter(new ArrayAdapter<SelectionObject>(this.getActivity(),android.R.layout.simple_list_item_1,SelectionObject.getTipoMaterialBachaData()));
			lMarca.setAdapter(new ArrayAdapter<SelectionObject>(this.getActivity(),android.R.layout.simple_list_item_1));
			
			BaseProduct idProd = (BaseProduct) getArguments().getSerializable(ARG_PARAM1);
			if(idProd != null){
				Bacha mp = (Bacha) idProd;
				((AddProductsCallbacks)getActivity()).setProductDetail(idProd);
				lTipoMaterial.setSelection(SelectionObject.getIndexInList(SelectionObject.getTipoMaterialBachaData(),mp.getTipoMaterial().getId()));
				if(mp.getTipoMaterial().getId().equalsIgnoreCase(AntonConstants.ACERO)){
					((ArrayAdapter<SelectionObject>)lMarca.getAdapter()).addAll(SelectionObject.getMarcaAceroBachaData()); 					
					lMarca.setSelection(SelectionObject.getIndexInList(SelectionObject.getMarcaAceroBachaData(),mp.getMarca().getId()));
				}else{
					((ArrayAdapter<SelectionObject>)lMarca.getAdapter()).addAll(SelectionObject.getMarcaLosaBachaData());					
					lMarca.setSelection(SelectionObject.getIndexInList(SelectionObject.getMarcaLosaBachaData(),mp.getMarca().getId()));
				}
				lAcero.setSelection(SelectionObject.getIndexInList(SelectionObject.getAceroBachaData(),mp.getAcero().getId()));
				lColocacion.setSelection(SelectionObject.getIndexInList(SelectionObject.getColocacionBachaData(),mp.getColocacion().getId()));
				lTipo.setSelection(SelectionObject.getIndexInList(SelectionObject.getTipoBachaData(),mp.getTipo().getId()));
				ancho.setText(mp.getAncho().toString());
				prof.setText(mp.getProfundidad().toString());
				largo.setText(mp.getLargo().toString());
			}	
			lTipoMaterial.setOnItemSelectedListener(this);
			lTipo.setOnItemSelectedListener(this);
			return rootView;
	}


	@Override
	public HashMap<String, Object> addProduct(HashMap<String, Object> params){
		params.put("type", "product");
		params.put("bacha_material", ((SelectionObject)lTipoMaterial.getSelectedItem()).getId());
		params.put("bacha_marca", ((SelectionObject)lMarca.getSelectedItem()).getId());
		params.put("bacha_acero", ((SelectionObject)lAcero.getSelectedItem()).getId());
		params.put("bacha_colocacion", ((SelectionObject)lColocacion.getSelectedItem()).getId());
		params.put("bacha_tipo", ((SelectionObject)lTipo.getSelectedItem()).getId());
		params.put("bacha_largo", largo.getText().toString());
		params.put("bacha_ancho", ancho.getText().toString());
		params.put("bacha_prof", prof.getText().toString());
		params.put("bacha_diam", diam.getText().toString());
		BaseProduct idProd = (BaseProduct) getArguments().getSerializable(ARG_PARAM1);
		
		if(idProd == null){		
			params.put("uom_id", AntonConstants.UOM_BACHA);
			params.put("uom_po_id", AntonConstants.UOM_BACHA);		
			params.put("movile_categ_name", AntonConstants.CATEGORY_BACHA);
		}
		return params;
		
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int pos,
			long arg3) {
		SelectionObject tm = (SelectionObject) arg0.getItemAtPosition(pos);
		if(tm.getId().equalsIgnoreCase(AntonConstants.ACERO)){
			this.getActivity().findViewById(R.id.rowAcero).setVisibility(View.VISIBLE);
			((ArrayAdapter<SelectionObject>)lMarca.getAdapter()).clear();
			((ArrayAdapter<SelectionObject>)lMarca.getAdapter()).addAll(SelectionObject.getMarcaAceroBachaData());
			((ArrayAdapter<SelectionObject>)lMarca.getAdapter()).notifyDataSetChanged();			
		}else if(tm.getId().equalsIgnoreCase(AntonConstants.LOSA)){
			this.getActivity().findViewById(R.id.rowAcero).setVisibility(View.GONE);
			((ArrayAdapter<SelectionObject>)lMarca.getAdapter()).clear();
			((ArrayAdapter<SelectionObject>)lMarca.getAdapter()).addAll(SelectionObject.getMarcaLosaBachaData());
			((ArrayAdapter<SelectionObject>)lMarca.getAdapter()).notifyDataSetChanged();
		}
		if(tm.getId().equalsIgnoreCase(AntonConstants.REDONDA)){
			this.getActivity().findViewById(R.id.dimRedonda).setVisibility(View.VISIBLE);
			this.getActivity().findViewById(R.id.dimSimple).setVisibility(View.GONE);
		}else if(tm.getId().equalsIgnoreCase(AntonConstants.SIMPLE) || tm.getId().equalsIgnoreCase(AntonConstants.DOBLE)){
			this.getActivity().findViewById(R.id.dimSimple).setVisibility(View.VISIBLE);
			this.getActivity().findViewById(R.id.dimRedonda).setVisibility(View.GONE);
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}

}
