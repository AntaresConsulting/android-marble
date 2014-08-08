package ar.com.antaresconsulting.antonstockapp;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;

import com.openerp.CreateAsyncTask;
import com.openerp.OpenErpHolder;
import com.openerp.WriteAsyncTask;

import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import ar.com.antaresconsulting.antonstockapp.model.BaseProduct;

public class AddProductActivity extends ActionBarActivity implements WriteAsyncTask.WriteAsyncTaskCallbacks, AddProductsCallbacks, CreateAsyncTask.CreateAsyncTaskCallbacks {
	private Bitmap savedThumb;
	private ImageView containerPhoto;
	private String encodedImage;
	private CreateAsyncTask saveData;
	private WriteAsyncTask updateData;
	private BaseProduct idProd = null; 

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_product);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		Bundle b = getIntent().getExtras();
		int tprod = b.getInt(AntonConstants.TPROD);
		idProd = (BaseProduct) b.getSerializable(AntonConstants.ARG_ITEM_ID);

		this.containerPhoto = (ImageView) findViewById(R.id.productImg);

		switch (tprod) {
		case AntonConstants.MATERIA_PRIMA:
			getFragmentManager().beginTransaction().add(R.id.datailDataProduct, AddProductMPFragment.newInstance(idProd)).commit();	
			break;
		case AntonConstants.BACHAS:
			getFragmentManager().beginTransaction().add(R.id.datailDataProduct, AddProductBachaFragment.newInstance(idProd)).commit();
			break;
		case AntonConstants.INSUMOS:
			getFragmentManager().beginTransaction().add(R.id.datailDataProduct, AddProductInsumoFragment.newInstance(idProd)).commit();
			break;
		case AntonConstants.SERVICIOS:
			getFragmentManager().beginTransaction().add(R.id.datailDataProduct, AddProductServicioFragment.newInstance(idProd)).commit();
			break;
		default:
			break;
		}


	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_product, menu);
		return true;
	}
	public void addProductSave(MenuItem view) {

		OpenErpHolder.getInstance().setmModelName(AntonConstants.PRODUCT_MODEL);

		HashMap<String, Object> dataToSave = new HashMap<String, Object>();			
		dataToSave.put("name", ((EditText)findViewById(R.id.prodNombre)).getText().toString());
		if(this.encodedImage != null && !this.encodedImage.trim().equalsIgnoreCase(""))
			dataToSave.put("image_medium", this.encodedImage);
		//dataToSave.put("ean13", ((EditText)findViewById(R.id.eanVal)).getText().toString());
		dataToSave.put("price", ((EditText)findViewById(R.id.precioVal)).getText().toString());
		dataToSave.put("list_price", ((EditText)findViewById(R.id.precioVal)).getText().toString());
		dataToSave.put("standard_price", ((EditText)findViewById(R.id.precioVal)).getText().toString());	
		dataToSave.put("default_code", ((EditText)findViewById(R.id.codigoVal)).getText().toString());

		dataToSave = ((AddProductInterface)getFragmentManager().findFragmentById(R.id.datailDataProduct)).addProduct(dataToSave);
		if(idProd != null){
			HashMap[] dataToUpdate = new HashMap[2];
			dataToUpdate[0] = new HashMap<String, Object>();
			dataToUpdate[0].put("id", idProd.getId() );
			dataToUpdate[1] = dataToSave;

			this.updateData = new WriteAsyncTask(this);
			this.updateData.execute(dataToUpdate);
		}else{
			this.saveData = new CreateAsyncTask(this);
			this.saveData.execute(dataToSave);			
		}
	}


	public void takePhoto(View view) {
		Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		if (takePictureIntent.resolveActivity(this.getPackageManager()) != null) {
			startActivityForResult(takePictureIntent, AntonConstants.REQUEST_IMAGE_CAPTURE);
		}		
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == AntonConstants.REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
			Bundle extras = data.getExtras();
			this.savedThumb = (Bitmap) extras.get("data");
			ByteArrayOutputStream baos=new  ByteArrayOutputStream();
			this.savedThumb.compress(Bitmap.CompressFormat.PNG,100, baos);
			byte [] b=baos.toByteArray();
			this.encodedImage=Base64.encodeToString(b, Base64.DEFAULT);
			this.containerPhoto.setImageBitmap(this.savedThumb);
		}
	}

	@Override
	public void setResultCreate(Long res) {
		Toast tt =Toast.makeText(getApplicationContext(), "Se creado el producto en forma satisfactoria con el id = "+res.toString(), Toast.LENGTH_SHORT);
		tt.show();
		finish();
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == android.R.id.home) {
            Intent upIntent = NavUtils.getParentActivityIntent(this);
            NavUtils.navigateUpTo(this, upIntent);             
			return true;
		}		
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void setProductDetail(BaseProduct prod) {
		((EditText)findViewById(R.id.prodNombre)).setText(prod.getNombre());
		((EditText)findViewById(R.id.precioVal)).setText(prod.getPrice().toString());
		((EditText)findViewById(R.id.codigoVal)).setText(prod.getCodigo());
		if(prod.getProductImg() != null){
			byte[] decodedString = Base64.decode(prod.getProductImg().trim(), Base64.DEFAULT);
			Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0,decodedString.length);
			((ImageView)findViewById(R.id.productImg)).setImageBitmap(decodedByte);
		}
	}

	@Override
	public void setResultCreate(Boolean res) {
		Toast tt =Toast.makeText(getApplicationContext(), "Se ha actualizado el producto en forma satisfactoria ", Toast.LENGTH_SHORT);
		tt.show();
		finish();		
	}
}

