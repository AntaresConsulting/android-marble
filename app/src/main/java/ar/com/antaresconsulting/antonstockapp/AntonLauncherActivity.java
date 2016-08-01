package ar.com.antaresconsulting.antonstockapp;

import com.openerp.OpenErpHolder;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import ar.com.antaresconsulting.antonstockapp.model.User;

public class AntonLauncherActivity extends BaseActivity {

	private Toolbar appbar;
	private DrawerLayout drawerLayout;
	private NavigationView navView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_launcher);
		appbar = (Toolbar)findViewById(R.id.appbar);
		setSupportActionBar(appbar);

		getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_nav_drawer);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
		NavigationView navigationView = (NavigationView)findViewById(R.id.navview);

		View headerView = navigationView.getHeaderView(0);

		User usr = OpenErpHolder.getInstance().getmOConn().getUsrObj();
		if(usr.getPartnerImg().trim() != ""){
			byte[] decodedString = Base64.decode(usr.getPartnerImg().trim(), Base64.DEFAULT);
			Bitmap decodedByte = getRoundedShape(BitmapFactory.decodeByteArray(decodedString, 0,decodedString.length));
			((ImageView) headerView.findViewById(R.id.usuarioImg)).setImageBitmap(decodedByte);
		}else{
			((ImageView) headerView.findViewById(R.id.usuarioImg)).setImageDrawable(this.getResources().getDrawable(R.drawable.ic_no_photo));
		}
		((TextView) headerView.findViewById(R.id.usuarioNom)).setText(usr.getNombre());
		GridLauncher gl = new GridLauncher();
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.content_frame, gl)
				.commit();

//		menuItem.setChecked(true);
//		getSupportActionBar().setTitle(menuItem.getTitle());

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.

		switch(item.getItemId()) {
			case android.R.id.home:
				drawerLayout.openDrawer(GravityCompat.START);
				return true;
		}

		return super.onOptionsItemSelected(item);
	}


	public void getProducts(View v) {
		Intent inProds = new Intent(this, ProductTypeActivity.class);
		startActivity(inProds);
	}

	public void getPartners(View v) {
		Intent inProds = new Intent(this, PartnerListActivity.class);
		startActivity(inProds);

	}

	public void getInCome(View v) {
		Intent inProds = new Intent(this, InProducts.class);
		startActivity(inProds);

	}

	public void getRInCome(View v) {
		Intent inProds = new Intent(this, ReEnterProducts.class);
		startActivity(inProds);

	}

	public void getOutCome(View v ) {
		Intent outProds = new Intent(this, MoveProducts.class);
		startActivity(outProds);

	}
	public void getDispatch( View v) {
		Intent outProds = new Intent(this, SubmitProducts.class);
		startActivity(outProds);

	}
	public void genPM( View v) {
		Intent outProds = new Intent(this, AddPMActivity.class);
		startActivity(outProds);

	}
	public void genOE(View v ) {
		Intent outProds = new Intent(this, AddOEActivity.class);
		startActivity(outProds);

	}
	public void getInsumos(View v ) {
		Intent outProds = new Intent(this, ConsumeInsumo.class);
		startActivity(outProds);

	}

	private Bitmap getRoundedShape(Bitmap bitmap) {
		final Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Bitmap.Config.ARGB_8888);
		final Canvas canvas = new Canvas(output);

		final int color = Color.RED;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawOval(rectF, paint);

		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);

		bitmap.recycle();

		return output;
	}
}

