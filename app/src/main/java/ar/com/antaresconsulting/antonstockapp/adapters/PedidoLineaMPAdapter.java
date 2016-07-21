package ar.com.antaresconsulting.antonstockapp.adapters;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import ar.com.antaresconsulting.antonstockapp.R;
import ar.com.antaresconsulting.antonstockapp.model.BaseProduct;
import ar.com.antaresconsulting.antonstockapp.model.Dimension;
import ar.com.antaresconsulting.antonstockapp.model.MateriaPrima;
import ar.com.antaresconsulting.antonstockapp.model.PedidoLinea;

public class PedidoLineaMPAdapter extends BaseAdapter {
	List<PedidoLinea> datos = new ArrayList<PedidoLinea>();
	Activity context;	

	static class ViewHolder {
		public TextView lblNombre;
		public TextView cantPlacas;
		public TextView dimHeight;
		public TextView dimWidth;
		public TextView dimThick;
		public TextView dimHeight2;
		public TextView dimWidth2;
		public TextView superficie;
		public TextView supTotal;
		public TextView titleCant;
	}
	
	public PedidoLineaMPAdapter(Fragment context,List<PedidoLinea> prods) {
		this.context = context.getActivity();
		this.datos = prods;
	}
	public PedidoLineaMPAdapter(Activity context) {
		this.context = context;
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		View rowView = convertView;

		if (rowView == null) {
			LayoutInflater inflater = context.getLayoutInflater();
			rowView = inflater.inflate(R.layout.listitem_lp_mp, null);

			ViewHolder viewHolder = new ViewHolder();

			viewHolder.lblNombre = (TextView) rowView.findViewById(R.id.nombreProd);
			viewHolder.cantPlacas = (TextView) rowView.findViewById(R.id.cantPlacasDispo);
			viewHolder.dimHeight = (TextView) rowView.findViewById(R.id.dimH);
			viewHolder.dimWidth = (TextView) rowView.findViewById(R.id.dimW);
			viewHolder.dimThick = (TextView) rowView.findViewById(R.id.dimT);
			viewHolder.dimHeight2 = (TextView) rowView.findViewById(R.id.dimH2);
			viewHolder.dimWidth2 = (TextView) rowView.findViewById(R.id.dimW2);
			viewHolder.superficie = (TextView) rowView.findViewById(R.id.superficie);
			viewHolder.supTotal = (TextView) rowView.findViewById(R.id.suptotal);
			viewHolder.titleCant = (TextView) rowView.findViewById(R.id.titleCant);

			rowView.setTag(viewHolder);
		}
		ViewHolder holder = (ViewHolder) rowView.getTag();
		PedidoLinea registro = (PedidoLinea) datos.get(position);
		holder.lblNombre.setText(registro.getNombre());
		holder.cantPlacas.setText(String.valueOf(registro.getCant().intValue()));
		Dimension dim = (Dimension) registro.getDimension()[0];
		if(((dim.getDimH() !=null) && !dim.getDimH().isEmpty())){
			holder.dimHeight.setText(dim.getDimH());
			holder.dimWidth.setText(dim.getDimW());
			holder.dimThick.setText(dim.getDimT());
			holder.dimHeight2.setText(dim.getDimH());
			holder.dimWidth2.setText(dim.getDimW());
			float sup = Float.parseFloat(dim.getDimW()) * Float.parseFloat(dim.getDimH());
			holder.superficie.setText(" = "+String.valueOf(sup)+" m2");
			holder.supTotal.setText(String.valueOf(sup*registro.getCant().doubleValue())+" m2");
		}
		return rowView;

	}

	@Override
	public int getCount() {
		return this.datos.size();
	}

	@Override
	public PedidoLinea getItem(int arg0) {
		return this.datos.get(arg0);
	}

	@Override
	public long getItemId(int position) {
		 return position;
	}
	
	public void addLinea(PedidoLinea prod) {
		this.datos.add(prod);
	}
	public void delLinea(PedidoLinea prod) {
		this.datos.remove(prod);
	}		
}
