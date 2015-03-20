package ar.com.antaresconsulting.antonstockapp.outgoing;

import java.util.List;

import com.openerp.SubmitAsyncTask;

import android.support.v4.app.NavUtils;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import ar.com.antaresconsulting.antonstockapp.AntonLauncherActivity;
import ar.com.antaresconsulting.antonstockapp.R;
import ar.com.antaresconsulting.antonstockapp.model.StockMove;
import ar.com.antaresconsulting.antonstockapp.model.StockPicking;
import ar.com.antaresconsulting.antonstockapp.model.dao.PedidoDAO;
import ar.com.antaresconsulting.antonstockapp.util.AntonConstants;

public class SubmitProducts extends Activity implements OnItemSelectedListener,PedidoDAO.PedidosCallbacks, PedidoDAO.OrdenesDeEntregaCallbacks{
	private PedidoDAO pedDao;

	private Spinner pedidos;
	private TextView cliente;
	private StockPicking selectPed;

	private ListView productosDispo;

	ArrayAdapter<StockMove> prodBuscaAdapter;
	
	protected boolean isScanSearch = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_submit_products);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		this.productosDispo = (ListView) findViewById(R.id.productosDispo);
		this.productosDispo.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);			
		this.prodBuscaAdapter = new ArrayAdapter<StockMove>(this,android.R.layout.simple_list_item_multiple_choice);
		this.productosDispo.setAdapter(this.prodBuscaAdapter);

	
		this.cliente = (TextView) findViewById(R.id.clienteVal);

		this.pedidos = (Spinner) findViewById(R.id.pedidosCombo);
		this.pedidos.setOnItemSelectedListener(this);	
		
		this.pedDao = new PedidoDAO(this);
		this.pedDao.getOrdenesDeEntregaReady();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.submit_products, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == android.R.id.home) {
			NavUtils.navigateUpTo(this, new Intent(this,
					AntonLauncherActivity.class));
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public void submitProducts(MenuItem view) {
		SubmitAsyncTask sub = new SubmitAsyncTask(this);
		sub.setModelStockPicking("stock.move");
		SparseBooleanArray checked = this.productosDispo.getCheckedItemPositions();
		if(checked.size() <= 0){
			Toast tt = Toast.makeText(this, "Debe seleccionar almenos una linea", Toast.LENGTH_SHORT);
			tt.show();
			return;
		}		
		int cant = 0;
		for (int i = 0; i < checked.size(); i++)if (checked.valueAt(i))cant++;
		StockMove[] plsVals = new StockMove[cant]; 				
		for (int i = 0; i < checked.size(); i++) {
			int position = checked.keyAt(i);
			if (checked.valueAt(i)){
				plsVals[i] = (StockMove) this.productosDispo.getAdapter().getItem(position);
			}
		}
		sub.execute(plsVals);
	}


	@Override
	public void setOrdenes() {
		this.pedidos.setAdapter(new ArrayAdapter<StockPicking>(this,android.R.layout.simple_list_item_1,this.pedDao.getPedidoList()));		
		
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int pos,
			long arg3) {
		selectPed = (StockPicking) arg0.getItemAtPosition(pos);
		this.cliente.setText((String) selectPed.getPartner()[1]);
		this.pedDao = new PedidoDAO(this);
		this.pedDao.getMoveAll(selectPed.getId());		
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setPedidos() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setPedidosLineas() {
		List<StockMove> pls = this.pedDao.getPedidoLineaList();
		this.prodBuscaAdapter.clear();
		this.prodBuscaAdapter.addAll(pls);
		int i = 0;
		for (StockMove pedidoLinea : pls) {
			if(pedidoLinea.getEstado().equalsIgnoreCase(AntonConstants.ESTADO_FIN))
				this.productosDispo.setItemChecked(i, true);
			i++;
		}
		this.prodBuscaAdapter.notifyDataSetChanged();
		
	}

}
