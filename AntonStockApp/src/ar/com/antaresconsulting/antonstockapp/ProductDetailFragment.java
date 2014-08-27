package ar.com.antaresconsulting.antonstockapp;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;

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
import ar.com.antaresconsulting.antonstockapp.model.BaseProduct;

/**
 * A fragment representing a single Product detail screen. This fragment is
 * either contained in a {@link ProductListActivity} in two-pane mode (on
 * tablets) or a {@link ProductDetailActivity} on handsets.
 */
public class ProductDetailFragment extends Fragment {
	/**
	 * The fragment argument representing the item ID that this fragment
	 * represents.
	 */
	public static final String ARG_ITEM_ID = "item_id";
	public static final String ARG_PHOTO_SAVE = "photo_saved";
	
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
				if(!getArguments().containsKey("savedObject") || (getArguments().containsKey("savedObject") && ((BaseProduct)getArguments().getSerializable("savedObject")).getId().intValue() != ((BaseProduct)getArguments().getSerializable(ARG_ITEM_ID)).getId().intValue())){					
					this.tProd = getArguments().getInt(AntonConstants.TPROD);
					pData = (BaseProduct) getArguments().getSerializable(ARG_ITEM_ID);					
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
			
			if(OpenErpHolder.getInstance().getmOConn().isManager()){
				((TextView) view.findViewById(R.id.precioVal)).setText(this.pData.getPrice().toString());
			}else{
				view.findViewById(R.id.precioRow).setVisibility(View.GONE);
			}
			
			if(this.pData.getCantidadReal().doubleValue() > 0)
				((TextView) view.findViewById(R.id.stockRVal)).setText(this.pData.getCantidadReal().toString());
			else
				view.findViewById(R.id.stockRealRow).setVisibility(View.GONE);
			
			if(this.pData.getCantidadForecast().doubleValue() > 0)
				((TextView) view.findViewById(R.id.stockFVal)).setText(this.pData.getCantidadForecast().toString());
			else
				view.findViewById(R.id.stockFRow).setVisibility(View.GONE);
			
			if(this.pData.getCantidadIncome().doubleValue() > 0)
				((TextView) view.findViewById(R.id.stockIVal)).setText(this.pData.getCantidadIncome().toString());
			else
				view.findViewById(R.id.stockIRow).setVisibility(View.GONE);
			
			((TextView) view.findViewById(R.id.codigoVal)).setText(this.pData.getCodigo());
		
			if((this.pData.getProductImg() != null) && (this.pData.getProductImg().trim() != "")){
				byte[] decodedString = Base64.decode(this.pData.getProductImg().trim(), Base64.DEFAULT);
				Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0,decodedString.length);
				this.photoContainer.setImageBitmap(decodedByte);
			}else{
				if(this.savedThumb != null){
					this.photoContainer.setImageBitmap(this.savedThumb);
				}
			}

		}		
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_detail_product,	container, false);
		
		return rootView;
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
	        values[0].put("id",((BaseProduct)getArguments().getSerializable(ARG_ITEM_ID)).getId().toString());
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

}
