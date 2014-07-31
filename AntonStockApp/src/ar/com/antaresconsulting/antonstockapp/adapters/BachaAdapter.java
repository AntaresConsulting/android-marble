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
import ar.com.antaresconsulting.antonstockapp.model.Bacha;
import ar.com.antaresconsulting.antonstockapp.model.MateriaPrima;

public class BachaAdapter extends BaseProductAdapter {
	

	public BachaAdapter(Activity context) {
		super(context);
	}

	public BachaAdapter(Fragment productListFragment,
			List<Bacha> bachas) {
		super(productListFragment);
		this.addAll(bachas);
	}

	static class ViewHolder {
		public TextView lblNombre;
		public TextView cantidad;
		public TextView marcaBacha;
		public TextView tipoBacha;
	}
	
	public View getView(int position, View convertView, ViewGroup parent) {

		View rowView = convertView;

		if (rowView == null) {
			LayoutInflater inflater = context.getLayoutInflater();
			rowView = inflater.inflate(R.layout.listitem_bacha_move, null);

			ViewHolder viewHolder = new ViewHolder();

			viewHolder.lblNombre = (TextView) rowView.findViewById(R.id.nombreProd);
			viewHolder.cantidad = (TextView) rowView.findViewById(R.id.cantidadProducto);
			viewHolder.marcaBacha = (TextView) rowView.findViewById(R.id.marcaBacha);			
			viewHolder.tipoBacha = (TextView) rowView.findViewById(R.id.tipoBacha);			

			rowView.setTag(viewHolder);
		}
		ViewHolder holder = (ViewHolder) rowView.getTag();
		Bacha registro = (Bacha) datos.get(position);
		holder.lblNombre.setText(registro.getNombre());
		holder.cantidad.setText(registro.getCantidad().toString());
		holder.marcaBacha.setText(registro.getMarca().getName());
		holder.tipoBacha.setText(registro.getTipoMaterial().getName());

		return rowView;

	}
}
