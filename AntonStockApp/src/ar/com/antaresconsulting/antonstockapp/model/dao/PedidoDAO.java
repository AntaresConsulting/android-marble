package ar.com.antaresconsulting.antonstockapp.model.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.app.Fragment;
import ar.com.antaresconsulting.antonstockapp.model.Dimension;
import ar.com.antaresconsulting.antonstockapp.model.Pedido;
import ar.com.antaresconsulting.antonstockapp.model.PedidoLinea;
import ar.com.antaresconsulting.antonstockapp.util.AntonConstants;

import com.openerp.ReadAsyncTask;

public class PedidoDAO extends ReadAsyncTask {


	private String[] baseFields = new String[] { "id", "name", "move_type", "partner_id", "note", "origin","move_lines","min_date","move_prod_type"};
	private String[] stockMoveFields = new String[] { "id", "state", "name", "product_id", "product_uom","product_qty",AntonConstants.STOCK_MOVE_DIM_ID,AntonConstants.STOCK_MOVE_DIM_QTY};
	

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
	
	public void getMoveByPedMP(Integer id,String tpdrod) {
		this.extraData =  true;				
		this.setmFilters(new Object[] { new Object[] { "picking_id", "=",id },new Object[] { "state", "=","confirmed" },new Object[] { "product_id.prod_type", "ilike",tpdrod} });
		this.mModel = "stock.move";
		this.execute(this.stockMoveFields);
		this.dataToSet = AntonConstants.PEDIDOSLINEAS;
	}
	public void getMoveByPed(Integer id,String tpdrod) {
		this.setmFilters(new Object[] { new Object[] { "picking_id", "=",id },new Object[] { "state", "=","confirmed" },new Object[] { "product_id.prod_type", "ilike",tpdrod} });
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
		this.extraData =  true;				
		this.setmFilters(new Object[] { new Object[] { "picking_id", "=",id },new Object[] { "state", "=","assigned" } });
		this.mModel = "stock.move";
		this.execute(this.stockMoveFields);
		this.dataToSet = AntonConstants.PEDIDOSLINEAS;
	}
	
	public void getPedidosPend(String tPedidos) {
		this.setmFilters(new Object[] { new Object[] { "picking_type_id.code", "=",AntonConstants.IN_PORDUCT_TYPE }, new Object[] { "state", "=","assigned" },new Object[] { "move_prod_type", "=",tPedidos} });
		this.mModel = "stock.picking";
		this.execute(this.baseFields);
		this.dataToSet = AntonConstants.PEDIDOS;
	}
	
	public void getPedidosPorEstadoyTipo(String estado,String tipo) {
		this.setmFilters(new Object[] { new Object[] { "picking_type_id", "=",new Integer(tipo) }, new Object[] { "state", "=",estado }});
		this.mModel = "stock.picking";
		this.execute(this.baseFields);
		this.dataToSet = AntonConstants.PEDIDOS;
	}
	
	public void getOrdenesDeEntregaPend() {
		this.setmFilters(new Object[] { "|",new Object[] { "state", "=","partially_available" },new Object[] { "state", "=","confirmed" },new Object[] { "picking_type_id.code", "=",AntonConstants.OUT_PORDUCT_TYPE }});
		this.mModel = "stock.picking";
		this.execute(this.baseFields);
		this.dataToSet = AntonConstants.ORDENESDEENTREGA;
	}	
	public void getOrdenesDeEntregaReady() {
		this.setmFilters(new Object[] { new Object[] { "state", "=","assigned" },new Object[] { "picking_type_id.code", "=",AntonConstants.OUT_PORDUCT_TYPE }});
		this.mModel = "stock.picking";
		this.execute(this.baseFields);
		this.dataToSet = AntonConstants.ORDENESDEENTREGA;
	}		
	public List<PedidoLinea> getPedidoLineaList() {
		List<PedidoLinea> datosProds = new ArrayList<PedidoLinea>();
		int i = 0;
		for (HashMap<String, Object> obj : this.mData) {
			PedidoLinea resp = new PedidoLinea();

			Object[] product_id = obj.get("product_id") instanceof Boolean ? new Object[0]: (Object[]) obj.get("product_id");
			Object[] dimension_id = obj.get(AntonConstants.STOCK_MOVE_DIM_ID) instanceof Boolean ? new Object[0]: (Object[]) obj.get(AntonConstants.STOCK_MOVE_DIM_ID);
			Object[] product_uom = obj.get("product_uom") instanceof Boolean ? new Object[0]: (Object[]) obj.get("product_uom");
			if(this.extraData){
				if(!(obj.get(AntonConstants.STOCK_MOVE_DIM_ID) instanceof Boolean)){
					dimension_id = new Object[1];
					dimension_id[0] = new Dimension((HashMap<String, Object>) ((ArrayList)this.mExtraData.get(i).get(AntonConstants.STOCK_MOVE_DIM_ID)).get(0));
				}
			}
			i++;
			Double product_qty = obj.get("product_qty") instanceof Boolean ? 0: (Double) obj.get("product_qty");
			String nombre = obj.get("name") instanceof Boolean ? "": (String) obj.get("name");
			String estado = obj.get("state") instanceof Boolean ? "": (String) obj.get("state");

			Integer dimension_qty = obj.get(AntonConstants.STOCK_MOVE_DIM_QTY) instanceof Boolean ? 0: (Integer) obj.get(AntonConstants.STOCK_MOVE_DIM_QTY);

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
			String arrDate = obj.get("min_date") instanceof Boolean ? "": (String) obj.get("min_date");
			String movType = obj.get("move_prod_type") instanceof Boolean ? "": (String) obj.get("move_prod_type");

			resp.setId((Integer) obj.get("id"));
			resp.setNota(nota);
			resp.setPartner(partner);
			resp.setTipo(mt);
			resp.setOrigen(origin);
			resp.setNombre(nombre);	
			resp.setLineas(move_lines);
			resp.setArrivalDate(arrDate);
			resp.setTipoMov(movType);
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
			if(this.activityPart == null)
				((PedidosCallbacks) this.activity).setPedidos();
			else
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
