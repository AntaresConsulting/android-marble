package ar.com.antaresconsulting.antonstockapp.model.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.app.Fragment;
import ar.com.antaresconsulting.antonstockapp.AntonConstants;
import ar.com.antaresconsulting.antonstockapp.model.Pedido;
import ar.com.antaresconsulting.antonstockapp.model.PedidoLinea;

import com.openerp.ReadAsyncTask;

public class PedidoDAO extends ReadAsyncTask {


	private String[] baseFields = new String[] { "id", "name", "move_type", "partner_id", "note", "origin","move_lines"};
	private String[] stockMoveFields = new String[] { "id", "state", "name", "product_id", "product_uom","product_qty","dimension_id","dimension_qty"};
	

	private Fragment activityPart;
	private Activity activity;

	private int dataToSet;

	public interface PedidosCallbacks {
		void setPedidos();
		void setPedidosLineas();

	}	
	public interface OrdenesDeEntregaCallbacks {
		void setOrdenes();
	}	

	public PedidoDAO(Activity frag) {
		super(frag);
		this.activity = frag;
		this.mModel = "stock.picking";
	}
	
	public PedidoDAO(Fragment frag) {
		super(frag);
		this.activityPart = frag;
		this.mModel = "product.product";
	}
	
	public void getMoveByPed(Integer id,String tpdrod) {
		
		this.setmFilters(new Object[] { new Object[] { "picking_id", "=",id },new Object[] { "state", "=","confirmed" },new Object[] { "product_id.categ_name", "ilike",tpdrod} });
		this.mModel = "stock.move";
		this.execute(this.stockMoveFields);
		this.dataToSet = AntonConstants.PEDIDOSLINEAS;
	}
	public void getMoveAll(Integer id) {
		
		this.setmFilters(new Object[] { new Object[] { "picking_id", "=",id }});
		this.mModel = "stock.move";
		this.execute(this.stockMoveFields);
		this.dataToSet = AntonConstants.PEDIDOSLINEAS;
	}
	
	public void getMoveByPed(Integer id) {
		
		this.setmFilters(new Object[] { new Object[] { "picking_id", "=",id },new Object[] { "state", "=","confirmed" } });
		this.mModel = "stock.move";
		this.execute(this.stockMoveFields);
		this.dataToSet = AntonConstants.PEDIDOSLINEAS;
	}
	
	public void getPedidosPend(String tPedidos) {
		this.setmFilters(new Object[] { new Object[] { "state", "=","assigned" },new Object[] { "move_prod_type", "=",tPedidos} ,new Object[] { "type", "=",AntonConstants.IN_PORDUCT_TYPE }});
		this.mModel = "stock.picking.in";
		this.execute(this.baseFields);
		this.dataToSet = AntonConstants.PEDIDOS;
	}
	public void getOrdenesDeEntregaPend() {
		this.setmFilters(new Object[] { new Object[] { "state", "=","confirmed" },new Object[] { "type", "=",AntonConstants.OUT_PORDUCT_TYPE }});
		this.mModel = "stock.picking.out";
		this.execute(this.baseFields);
		this.dataToSet = AntonConstants.ORDENESDEENTREGA;
	}	
	public void getOrdenesDeEntregaReady() {
		this.setmFilters(new Object[] { new Object[] { "state", "=","assigned" },new Object[] { "type", "=",AntonConstants.OUT_PORDUCT_TYPE }});
		this.mModel = "stock.picking.out";
		this.execute(this.baseFields);
		this.dataToSet = AntonConstants.ORDENESDEENTREGA;
	}		
	public List<PedidoLinea> getPedidoLineaList() {
		List<PedidoLinea> datosProds = new ArrayList<PedidoLinea>();
		for (HashMap<String, Object> obj : this.mData) {
			PedidoLinea resp = new PedidoLinea();

			Object[] product_id = obj.get("product_id") instanceof Boolean ? new Object[0]: (Object[]) obj.get("product_id");
			Object[] dimension_id = obj.get("dimension_id") instanceof Boolean ? new Object[0]: (Object[]) obj.get("dimension_id");
			Object[] product_uom = obj.get("product_uom") instanceof Boolean ? new Object[0]: (Object[]) obj.get("product_uom");

			Double product_qty = obj.get("product_qty") instanceof Boolean ? 0: (Double) obj.get("product_qty");
			String nombre = obj.get("name") instanceof Boolean ? "": (String) obj.get("name");
			String estado = obj.get("state") instanceof Boolean ? "": (String) obj.get("state");

			Integer dimension_qty = obj.get("dimension_qty") instanceof Boolean ? 0: (Integer) obj.get("dimension_qty");

			resp.setId((Integer) obj.get("id"));
			resp.setProduct(product_id);
			resp.setCant(product_qty);
			resp.setCantDim(dimension_qty);
			resp.setDimension(dimension_id);
			resp.setUom(product_uom);
			resp.setNombre(nombre);
			resp.setEstado(estado);		
			datosProds.add(resp);
		}
		return datosProds;
	}
	public List<Pedido> getPedidoList() {
		List<Pedido> datosProds = new ArrayList<Pedido>();
		for (HashMap<String, Object> obj : this.mData) {
			Pedido resp = new Pedido();

			Object[] partner = obj.get("partner_id") instanceof Boolean ? new Object[0]: (Object[]) obj.get("partner_id");
			String mt = obj.get("move_type") instanceof Boolean ? "": (String) obj.get("move_type");
			String nombre = obj.get("name") instanceof Boolean ? "": (String) obj.get("name");
			String origin = obj.get("origin") instanceof Boolean ? "": (String) obj.get("origin");
			String nota = obj.get("note") instanceof Boolean ? "": (String) obj.get("note");
			Object[] move_lines = obj.get("move_lines") instanceof Boolean ? new Object[0]: (Object[]) obj.get("move_lines");

			resp.setId((Integer) obj.get("id"));
			resp.setNota(nota);
			resp.setPartner(partner);
			resp.setTipo(mt);
			resp.setOrigen(origin);
			resp.setNombre(nombre);	
			resp.setLineas(move_lines);
			datosProds.add(resp);
		}
		return datosProds;
	}

	@Override
	public void dataFetched() {
		switch (this.dataToSet) {
		case AntonConstants.ORDENESDEENTREGA:
			if(this.activityPart == null)
				((OrdenesDeEntregaCallbacks) this.activity).setOrdenes();
			else
				((OrdenesDeEntregaCallbacks) this.activityPart).setOrdenes();
			break;
		case AntonConstants.PEDIDOS:
			((PedidosCallbacks) this.activityPart).setPedidos();
			break;			
		case AntonConstants.PEDIDOSLINEAS:
			if(this.activityPart == null)
				((PedidosCallbacks) this.activity).setPedidosLineas();
			else
				((PedidosCallbacks) this.activityPart).setPedidosLineas();
			break;						
		default:
			break;
		}
	}

}
