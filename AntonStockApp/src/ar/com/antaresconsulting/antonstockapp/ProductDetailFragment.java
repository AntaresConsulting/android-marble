package ar.com.antaresconsulting.antonstockapp;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.List;

import com.openerp.OpenErpHolder;
import com.openerp.WriteAsyncTask;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import ar.com.antaresconsulting.antonstockapp.model.Bacha;
import ar.com.antaresconsulting.antonstockapp.model.BaseProduct;
import ar.com.antaresconsulting.antonstockapp.model.Insumo;
import ar.com.antaresconsulting.antonstockapp.model.MateriaPrima;
import ar.com.antaresconsulting.antonstockapp.model.Servicio;
import ar.com.antaresconsulting.antonstockapp.model.dao.BachasDAO;
import ar.com.antaresconsulting.antonstockapp.model.dao.InsumosDAO;
import ar.com.antaresconsulting.antonstockapp.model.dao.MateriaPrimaDAO;
import ar.com.antaresconsulting.antonstockapp.model.dao.ProductDAO;

/**
 * A fragment representing a single Product detail screen. This fragment is
 * either contained in a {@link ProductListActivity} in two-pane mode (on
 * tablets) or a {@link ProductDetailActivity} on handsets.
 */
public class ProductDetailFragment extends Fragment   implements ProductDAO.ProductCallbacks, MateriaPrimaDAO.MateriaPrimaCallbacks,BachasDAO.BachasCallbacks,InsumosDAO.InsumosCallbacks{
	/**
	 * The fragment argument representing the item ID that this fragment
	 * represents.
	 */
	public static final String ARG_ITEM_ID = "item_id";
	public static final String ARG_PHOTO_SAVE = "photo_saved";
	private MateriaPrimaDAO mpDao;
	private BachasDAO baDao;
	private InsumosDAO insuDao;
	private ProductDAO prodDao;
	
	private BaseProduct pData;
	private ImageView photoContainer;
	private Bitmap savedThumb;
	private WriteAsyncTask saveData;
	private int tProd;


	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public ProductDetailFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);	

		if (getArguments().containsKey(ARG_ITEM_ID)) {
				if(!getArguments().containsKey("savedObject") || (getArguments().containsKey("savedObject") && ((BaseProduct)getArguments().getSerializable("savedObject")).getId().intValue() != Integer.parseInt(getArguments().getString(ARG_ITEM_ID)))){
					this.tProd = getArguments().getInt(AntonConstants.TPROD);
					switch (tProd) {
					case AntonConstants.MATERIA_PRIMA:
						mpDao = new MateriaPrimaDAO(this);
						mpDao.getProductDetail(getArguments().getString(ARG_ITEM_ID));
						break;
					case AntonConstants.BACHAS:
						baDao = new BachasDAO(this);
						baDao.getProductDetail(getArguments().getString(ARG_ITEM_ID));						
						break;
					case AntonConstants.INSUMOS:
						insuDao = new InsumosDAO(this);
						insuDao.getProductDetail(getArguments().getString(ARG_ITEM_ID));
						break;	
					case AntonConstants.SERVICIOS:
						prodDao = new ProductDAO(this);
						prodDao.getProductDetail(getArguments().getString(ARG_ITEM_ID));
						break;						
						

					}					
				}
		}	
	}
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
		
		this.photoContainer = (ImageView) view.findViewById(R.id.productImg);
		if(!OpenErpHolder.getInstance().getmOConn().isManager()){
			((Button) view.findViewById(R.id.takePhotoBtn)).setVisibility(View.GONE);
		}

		// Show the dummy content as text in a TextView.
		if (this.pData != null) {
			((TextView) view.findViewById(R.id.nombreProd)).setText(this.pData.getNombre());
			((TextView) view.findViewById(R.id.precioVal)).setText(this.pData.getPrice().toString());
			((TextView) view.findViewById(R.id.stockVal)).setText(this.pData.getCantidad().toString());
			((TextView) view.findViewById(R.id.codigoVal)).setText(this.pData.getCodigo());
			((TextView) view.findViewById(R.id.eanVal)).setText(this.pData.getEan13());		
			if((this.pData.getProductImg() != null) && (this.pData.getProductImg().trim() != "")){
				byte[] decodedString = Base64.decode(this.pData.getProductImg().trim(), Base64.DEFAULT);
				Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0,decodedString.length);
				this.photoContainer.setImageBitmap(decodedByte);
			}else{
				if(this.savedThumb != null){
					this.photoContainer.setImageBitmap(this.savedThumb);
				}
			}

			switch (this.tProd) {
			case AntonConstants.MATERIA_PRIMA:
				MateriaPrima prod = (MateriaPrima) this.pData;
				View.inflate(getActivity(), R.layout.fragment_detail_mp, (ViewGroup) getActivity().findViewById(R.id.productDetail));
				((TextView) view.findViewById(R.id.material)).setText(prod.getMaterial().getName());
				((TextView) view.findViewById(R.id.color)).setText(prod.getColor().getName());
				((TextView) view.findViewById(R.id.acabado)).setText(prod.getFinished().getName());				
				break;
			case AntonConstants.BACHAS:
				Bacha bacha = (Bacha) this.pData;
				View.inflate(getActivity(), R.layout.fragment_detail_bacha, (ViewGroup) getActivity().findViewById(R.id.productDetail));
				((TextView) view.findViewById(R.id.material)).setText(bacha.getTipoMaterial().getName());
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
				View.inflate(getActivity(), R.layout.fragment_detail_insumo, (ViewGroup) getActivity().findViewById(R.id.productDetail));
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
		View rootView = inflater.inflate(R.layout.fragment_detail_product,	container, false);
		
		return rootView;
	}


	@Override
	public void setMateriaPrima() {
		List<MateriaPrima> prods = this.mpDao.getMateriaPrimasList();
		this.pData = prods.get(0);	
		this.onViewCreated(getView(), getArguments());		
	}

	@Override
	public void setBachas() {
		List<Bacha> prods = this.baDao.getBachasList();
		this.pData = prods.get(0);	
		this.onViewCreated(getView(), getArguments());		
	}

	@Override
	public void setInsumos() {
		List<Insumo> prods = this.insuDao.getInsumosList();
		this.pData = prods.get(0);	
		this.onViewCreated(getView(), getArguments());		
	}


	public void takePhoto() {
		getArguments().putSerializable("savedObject", this.pData);
	    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
	    if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
	        startActivityForResult(takePictureIntent, AntonConstants.REQUEST_IMAGE_CAPTURE);
	    }			
	}

	public BaseProduct getProductSelected(){
		return this.pData;
	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
	    if (requestCode == AntonConstants.REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
	        Bundle extras = data.getExtras();
	        this.savedThumb = (Bitmap) extras.get("data");
	        this.saveData = new WriteAsyncTask(this.getActivity());
	        OpenErpHolder.getInstance().setmModelName("product.product");
	        HashMap[] values = new HashMap[2];
	        values[0] = new HashMap<String, Object>();
	        values[0].put("id",getArguments().getString(ARG_ITEM_ID));
	        values[1] = new HashMap<String, Object>();
	        ByteArrayOutputStream baos=new  ByteArrayOutputStream();
	        this.savedThumb.compress(Bitmap.CompressFormat.PNG,100, baos);
            byte [] b=baos.toByteArray();
            String encodedBitmap=Base64.encodeToString(b, Base64.DEFAULT);

	        values[1].put("image", encodedBitmap);
	        this.saveData.execute(values);
	        //Bitmap imageBitmap = (Bitmap) extras.get("data");
	    }
	}

	@Override
	public void setProductDetail() {
		List<Servicio> prods = this.prodDao.getServiciosList();
		this.pData = prods.get(0);	
		this.onViewCreated(getView(), getArguments());			
	}

	@Override
	public void setProductOthers() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setProductsSelection() {
		// TODO Auto-generated method stub
		
	}


}
