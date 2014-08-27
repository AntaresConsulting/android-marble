package ar.com.antaresconsulting.antonstockapp.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;


public class PickingMove implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 637196335316350886L;
	private HashMap<String, Object> headerPicking;
	private List<HashMap<String,Object>>  moves;
	private String moveType;
	private Pedido pedidoOut;
	private boolean regBalanace = false;
	private boolean confirm = false;
	
	
	
	public boolean isConfirm() {
		return confirm;
	}
	public void setConfirm(boolean confirm) {
		this.confirm = confirm;
	}
	public boolean isRegBalanace() {
		return regBalanace;
	}
	public void setRegBalanace(boolean regBalanace) {
		this.regBalanace = regBalanace;
	}
	public Pedido getPedidoOut() {
		return pedidoOut;
	}
	public void setPedidoOut(Pedido pedidoOut) {
		this.pedidoOut = pedidoOut;
	}
	public HashMap<String, Object> getHeaderPicking() {
		return headerPicking;
	}
	public void setHeaderPicking(HashMap<String, Object> headerPicking) {
		this.headerPicking = headerPicking;
	}
	public List<HashMap<String,Object>>  getMoves() {
		return moves;
	}
	public void setMoves(List<HashMap<String,Object>>  moves) {
		this.moves = moves;
	}
	public String getMoveType() {
		return moveType;
	}
	public void setMoveType(String moveType) {
		this.moveType = moveType;
	}
	
	
	
}
