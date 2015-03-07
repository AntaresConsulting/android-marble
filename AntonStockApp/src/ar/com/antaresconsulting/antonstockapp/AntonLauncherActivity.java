package ar.com.antaresconsulting.antonstockapp;

import com.openerp.OpenErpHolder;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;
import ar.com.antaresconsulting.antonstockapp.incoming.AddPMActivity;
import ar.com.antaresconsulting.antonstockapp.incoming.InProducts;
import ar.com.antaresconsulting.antonstockapp.internal.ConsumeInsumo;
import ar.com.antaresconsulting.antonstockapp.internal.MoveProducts;
import ar.com.antaresconsulting.antonstockapp.internal.ReEnterProducts;
import ar.com.antaresconsulting.antonstockapp.outgoing.AddOEActivity;
import ar.com.antaresconsulting.antonstockapp.outgoing.SubmitProducts;
import ar.com.antaresconsulting.antonstockapp.partner.PartnerListActivity;
import ar.com.antaresconsulting.antonstockapp.product.ProductTypeActivity;

public class AntonLauncherActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_launcher);
		ImageButton btPart =  (ImageButton) findViewById(R.id.partnerButton);
		btPart.setVisibility(View.GONE);
		ImageButton btPRoduct =  (ImageButton) findViewById(R.id.prodButton);
		btPRoduct.setVisibility(View.GONE);
		ImageButton btOE =  (ImageButton) findViewById(R.id.genOEButton);
		btOE.setVisibility(View.GONE);
		ImageButton btPM =  (ImageButton) findViewById(R.id.genPMButton);
		btPM.setVisibility(View.GONE);
		ImageButton btInsu =  (ImageButton) findViewById(R.id.insumoButton);
		btInsu.setVisibility(View.GONE);
		ImageButton btIncome =  (ImageButton) findViewById(R.id.incomeButton);
		btIncome.setVisibility(View.GONE);
		ImageButton btRemit =  (ImageButton) findViewById(R.id.remitoButton);
		btRemit.setVisibility(View.GONE);
		ImageButton btReincome =  (ImageButton) findViewById(R.id.reincomeButton);
		btReincome.setVisibility(View.GONE);
		ImageButton btProduction =  (ImageButton) findViewById(R.id.productionButton);
		btProduction.setVisibility(View.GONE);
		
		if(OpenErpHolder.getInstance().getmOConn().isManager()){
			btPart.setVisibility(View.VISIBLE);
			btPRoduct.setVisibility(View.VISIBLE);
			btPM.setVisibility(View.VISIBLE);
			btOE.setVisibility(View.VISIBLE);
		}
		if(OpenErpHolder.getInstance().getmOConn().isCutter()){
			btPRoduct.setVisibility(View.VISIBLE);			
			btReincome.setVisibility(View.VISIBLE);
			btProduction.setVisibility(View.VISIBLE);
		}
		if(OpenErpHolder.getInstance().getmOConn().isResponsable()){
			btPRoduct.setVisibility(View.VISIBLE);			
			btInsu.setVisibility(View.VISIBLE);
			btIncome.setVisibility(View.VISIBLE);
			btRemit.setVisibility(View.VISIBLE);
		}		
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
	public void getDispatch(View v ) {
		Intent outProds = new Intent(this, SubmitProducts.class);
		startActivity(outProds);
		
	}	
	public void genPM(View v ) {
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
}
