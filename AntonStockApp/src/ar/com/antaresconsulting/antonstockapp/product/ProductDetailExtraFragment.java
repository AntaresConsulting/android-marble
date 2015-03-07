package ar.com.antaresconsulting.antonstockapp.product;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import ar.com.antaresconsulting.antonstockapp.AntonConstants;
import ar.com.antaresconsulting.antonstockapp.R;
import ar.com.antaresconsulting.antonstockapp.R.id;
import ar.com.antaresconsulting.antonstockapp.R.layout;
import ar.com.antaresconsulting.antonstockapp.model.Bacha;
import ar.com.antaresconsulting.antonstockapp.model.BaseProduct;
import ar.com.antaresconsulting.antonstockapp.model.Insumo;
import ar.com.antaresconsulting.antonstockapp.model.MateriaPrima;

/**
 * A fragment representing a single Product detail screen. This fragment is
 * either contained in a {@link ProductListActivity} in two-pane mode (on
 * tablets) or a {@link ProductDetailActivity} on handsets.
 */
public class ProductDetailExtraFragment extends Fragment  {
	/**
	 * The fragment argument representing the item ID that this fragment
	 * represents.
	 */
	public static final String ARG_TPROD_ID = "tprod_id";
	private int tProd;
	
	private BaseProduct pData;
	

	public static ProductDetailExtraFragment newInstance(BaseProduct mp,int tprod) {
		ProductDetailExtraFragment fragment = new ProductDetailExtraFragment();
		Bundle args = new Bundle();
		args.putSerializable(AntonConstants.PRODUCT_SELECTED, mp);
		args.putInt(AntonConstants.TPROD, tprod);
		fragment.setArguments(args);
		return fragment;
	}

	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public ProductDetailExtraFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);	
		if (getArguments() != null) {
			pData = (BaseProduct) getArguments().getSerializable(AntonConstants.PRODUCT_SELECTED);
			tProd = getArguments().getInt(AntonConstants.TPROD);
		}			
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
		// Show the dummy content as text in a TextView.
		if (this.pData != null) {

			switch (this.tProd) {
			case AntonConstants.MATERIA_PRIMA:
				MateriaPrima prod = (MateriaPrima) this.pData;
				((TextView) view.findViewById(R.id.material)).setText(prod.getMaterial().getName());
				((TextView) view.findViewById(R.id.color)).setText(prod.getColor().getName());
				((TextView) view.findViewById(R.id.acabado)).setText(prod.getFinished().getName());				
				break;
			case AntonConstants.BACHAS:
				Bacha bacha = (Bacha) this.pData;
				((TextView) view.findViewById(R.id.material)).setText(bacha.getTipoMaterial().getName());
				if(bacha.getMarca() != null)
					((TextView) view.findViewById(R.id.marca)).setText(bacha.getMarca().getName());
				if(bacha.getTipo() != null)
					((TextView) view.findViewById(R.id.tipoBacha)).setText(bacha.getTipo().getName());
				if(bacha.getTipo() != null)				
					((TextView) view.findViewById(R.id.acero)).setText(bacha.getAcero().getName());
				((TextView) view.findViewById(R.id.colocacion)).setText(bacha.getColocacion().getName());
				((TextView) view.findViewById(R.id.bachaProf)).setText(bacha.getProfundidad().toString());
				((TextView) view.findViewById(R.id.bachaLargo)).setText(bacha.getLargo().toString());
				((TextView) view.findViewById(R.id.bachaAncho)).setText(bacha.getAncho().toString());

				break;
			case AntonConstants.INSUMOS:
				Insumo insu = (Insumo) this.pData;
				((TextView) view.findViewById(R.id.insuDesc)).setText(insu.getDescription());
				break;	
			case AntonConstants.SERVICIOS:
//				BaseProduct serv = (BaseProduct) this.pData;
//				View.inflate(getActivity(), R.layout.fragment_detail_insumo, (ViewGroup) getActivity().findViewById(R.id.productDetail));
//				((TextView) view.findViewById(R.id.insuDesc)).setText(serv.getDescription());
				break;					
			default:
				break;
			}
		}		
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = null;
			switch (this.tProd) {
			case AntonConstants.MATERIA_PRIMA:
				rootView = inflater.inflate(R.layout.fragment_detail_mp,	container, false);
				break;
			case AntonConstants.BACHAS:
				rootView = inflater.inflate(R.layout.fragment_detail_bacha,	container, false);
				break;
			case AntonConstants.INSUMOS:
				rootView = inflater.inflate(R.layout.fragment_detail_insumo,	container, false);
				break;	
			case AntonConstants.SERVICIOS:
				break;					
			default:
				break;
			}		
		return rootView;
	}

	
	public BaseProduct getProductSelected(){
		return this.pData;
	}


    @Override
    public void onStart()
    {
        super.onStart();
    }

    @Override
    public void onResume()
    {
        super.onResume();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
    }
}
