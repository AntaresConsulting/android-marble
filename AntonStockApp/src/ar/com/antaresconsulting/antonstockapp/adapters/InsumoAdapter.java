package ar.com.antaresconsulting.antonstockapp.adapters;

import java.util.List;

import android.app.Activity;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import ar.com.antaresconsulting.antonstockapp.R;
import ar.com.antaresconsulting.antonstockapp.model.Insumo;
import ar.com.antaresconsulting.antonstockapp.model.MateriaPrima;

public class InsumoAdapter extends BaseProductAdapter {
	

	public InsumoAdapter(Activity context) {
		super(context);
	}

	public InsumoAdapter(Fragment productListFragment,
			List<Insumo> insumos) {
			super(productListFragment);
			this.datos.addAll(insumos);	
	}

	static class ViewHolder {
		public TextView lblNombre;
		public TextView cantidad;
	}
	
	public View getView(int position, View convertView, ViewGroup parent) {

		View rowView = convertView;

		if (rowView == null) {
			LayoutInflater inflater = context.getLayoutInflater();
			rowView = inflater.inflate(R.layout.listitem_insumo_move, null);

			ViewHolder viewHolder = new ViewHolder();

			viewHolder.lblNombre = (TextView) rowView.findViewById(R.id.nombreProd);
			viewHolder.cantidad = (TextView) rowView.findViewById(R.id.cantidadProducto);

			rowView.setTag(viewHolder);
		}
		ViewHolder holder = (ViewHolder) rowView.getTag();
		Insumo registro = (Insumo) datos.get(position);
		holder.lblNombre.setText(registro.getNombre());
		holder.cantidad.setText(registro.getCantidad().toString());
		return rowView;

	}
}
