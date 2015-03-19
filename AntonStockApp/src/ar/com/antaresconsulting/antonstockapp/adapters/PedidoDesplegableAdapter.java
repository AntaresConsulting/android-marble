package ar.com.antaresconsulting.antonstockapp.adapters;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import ar.com.antaresconsulting.antonstockapp.R;
import ar.com.antaresconsulting.antonstockapp.model.BaseProduct;
import ar.com.antaresconsulting.antonstockapp.model.MateriaPrimaOut;
import ar.com.antaresconsulting.antonstockapp.model.Pedido;
import ar.com.antaresconsulting.antonstockapp.model.PedidoLinea;

public class PedidoDesplegableAdapter extends BaseExpandableListAdapter {
	Activity context;
	List<Pedido> datos = new ArrayList<Pedido>();

	public void addProduct(Pedido prod) {
		this.datos.add(prod);
	}
	public void delProduct(Pedido prod) {
		this.datos.remove(prod);
	}	

	public void addAll(List<Pedido> prods) {
		this.datos.addAll(prods);
	}

	public PedidoDesplegableAdapter(Activity context) {
		this.context = context;
	}

	public PedidoDesplegableAdapter(Fragment productListFragment,	List<Pedido> mp) {
		this.context = productListFragment.getActivity();
		this.datos.addAll(mp);		
	}

	static class ViewHolder {
		public TextView lblNombre;
		public LinearLayout dimsList;

	}
	
	@Override
	public Object getChild(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return this.datos.get(arg0).getLineas()[arg1];
	}
	@Override
	public long getChildId(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return childPosition;
	}
	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
        final PedidoLinea childText = (PedidoLinea) getChild(groupPosition, childPosition);
        
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.listitem_lp2, null);
        }
 
        TextView dim = (TextView) convertView.findViewById(R.id.dimensionVal);
 
        dim.setText((String) childText.getDimension()[1]);
        return convertView;
	}
	@Override
	public int getChildrenCount(int groupPosition) {
		// TODO Auto-generated method stub
		return this.datos.get(groupPosition).getLineas().length;
	}
	@Override
	public Object getGroup(int groupPosition) {
		// TODO Auto-generated method stub
		return this.datos.get(groupPosition);
	}
	@Override
	public int getGroupCount() {
		// TODO Auto-generated method stub
		return this.datos.size();
	}
	@Override
	public long getGroupId(int groupPosition) {
		// TODO Auto-generated method stub
		return groupPosition;
	}
	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
			MateriaPrimaOut prodOut = (MateriaPrimaOut) getGroup(groupPosition);
	        if (convertView == null) {
	            LayoutInflater infalInflater = (LayoutInflater) this.context
	                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	            convertView = infalInflater.inflate(R.layout.listitem_mp_out_move, null);
	        }
	 
	        TextView nombreProd = (TextView) convertView.findViewById(R.id.nombreProd);
	        nombreProd.setText(prodOut.getNombre());
	        TextView placa = (TextView) convertView.findViewById(R.id.placaSelec);
	        placa.setText((String) prodOut.getDim().getDimId()[1]);
	        TextView placaCant = (TextView) convertView.findViewById(R.id.placaSelecCant);
	        placaCant.setText(prodOut.getCant().toString());	        
	        return convertView;
	}
	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return false;
	}
}
