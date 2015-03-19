package ar.com.antaresconsulting.antonstockapp;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.Spinner;
import ar.com.antaresconsulting.antonstockapp.model.MateriaPrima;
import ar.com.antaresconsulting.antonstockapp.model.Pedido;
import ar.com.antaresconsulting.antonstockapp.model.SelectionObject;
import ar.com.antaresconsulting.antonstockapp.model.dao.PedidoDAO;

public class PedidoActivity extends ActionBarActivity implements PedidoDAO.PedidosCallbacks{
	private PedidoDAO pedDao;
	private Spinner tipoPedidos;
	private Spinner estadoPedidos;
	private ListView pedidos;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pedido);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.picking, menu);
		this.estadoPedidos = (Spinner) findViewById(R.id.estadoPedido);
		this.tipoPedidos = (Spinner) findViewById(R.id.tipoPedido);
		this.pedidos = (ListView) findViewById(R.id.pedidosList);
		this.tipoPedidos.setAdapter(new ArrayAdapter<SelectionObject>(this,android.R.layout.simple_list_item_1,SelectionObject.getTipoPedido()));
		this.estadoPedidos.setAdapter(new ArrayAdapter<SelectionObject>(this,android.R.layout.simple_list_item_1,SelectionObject.getEstadoPedido()));

		
		return true;
	}
	
	public void buscarPedidos(View v) {
		
		SelectionObject estado = (SelectionObject) this.estadoPedidos.getSelectedItem(); 
		SelectionObject tipo = (SelectionObject) this.tipoPedidos.getSelectedItem();
		pedDao =  new PedidoDAO(this);
		pedDao.getPedidosPorEstadoyTipo(estado.getId(),tipo.getId());	
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void setPedidos() {
		this.pedidos.setAdapter(new ArrayAdapter<Pedido>(this,android.R.layout.simple_list_item_1,this.pedDao.getPedidoList()));			
	}

	@Override
	public void setPedidosLineas() {
		// TODO Auto-generated method stub
		
	}
}
