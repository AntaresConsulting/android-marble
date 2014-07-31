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

public class PedidoLineaAdapter extends BaseAdapter {
	List<PedidoLinea> datos = new ArrayList<PedidoLinea>();
	Activity context;	

	static class ViewHolder {
		public TextView lblNombre;
		public TextView cantidad;
		public TextView unidades;
		public TextView dimHeight;
		public TextView dimWidth;
		public TextView dimThick;
		public TextView dimHeight2;
		public TextView dimWidth2;
		public TextView superficie;
		public TextView superficieTotal;
		public RelativeLayout dimContainer;

	}
	
	public PedidoLineaAdapter(Fragment context,List<PedidoLinea> prods) {
		this.context = context.getActivity();
		this.datos = prods;
	}
	public PedidoLineaAdapter(Activity context) {
		this.context = context;
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		View rowView = convertView;

		if (rowView == null) {
			LayoutInflater inflater = context.getLayoutInflater();
			rowView = inflater.inflate(R.layout.listitem_lp, null);

			ViewHolder viewHolder = new ViewHolder();

			viewHolder.lblNombre = (TextView) rowView.findViewById(R.id.nombreProd);
			viewHolder.cantidad = (TextView) rowView.findViewById(R.id.cantidadInfo);
			viewHolder.unidades = (TextView) rowView.findViewById(R.id.unidades);
			
			viewHolder.dimContainer = (RelativeLayout) rowView.findViewById(R.id.dimFrmContainer);
			viewHolder.dimHeight = (TextView) rowView.findViewById(R.id.dimH);
			viewHolder.dimHeight2 = (TextView) rowView.findViewById(R.id.dimH2);
			viewHolder.dimWidth = (TextView) rowView.findViewById(R.id.dimW);
			viewHolder.dimWidth2 = (TextView) rowView.findViewById(R.id.dimW2);
			viewHolder.dimThick = (TextView) rowView.findViewById(R.id.dimT);
			viewHolder.superficie = (TextView) rowView.findViewById(R.id.superficie);
			viewHolder.superficieTotal = (TextView) rowView.findViewById(R.id.superficieTotal);

			rowView.setTag(viewHolder);
		}
		ViewHolder holder = (ViewHolder) rowView.getTag();
		PedidoLinea registro = (PedidoLinea) datos.get(position);
		holder.lblNombre.setText(registro.getNombre());
		holder.cantidad.setText(String.valueOf(registro.getCant()));
		holder.unidades.setText((String)registro.getUom()[1]);
		if((registro.getDimension() != null) && (registro.getDimension().length > 0)){
			holder.dimContainer.setVisibility(View.VISIBLE);
			Dimension dim = (Dimension) registro.getDimension()[0];
			holder.cantidad.setText(String.valueOf(registro.getCantDim().intValue()));
			Double width = Double.valueOf(dim.getDimW());
			Double hight = Double.valueOf(dim.getDimH());
			int cant = registro.getCantDim().intValue();
			holder.dimHeight.setText(dim.getDimH());
			holder.dimHeight2.setText(dim.getDimH());
			holder.dimWidth.setText(dim.getDimW());
			holder.dimWidth2.setText(dim.getDimW());
			holder.dimThick.setText(dim.getDimT());
			holder.superficie.setText(String.valueOf(width.doubleValue() * hight.doubleValue()));
			holder.superficieTotal.setText(String.valueOf(width.doubleValue() * hight.doubleValue()*cant));
			holder.unidades.setText(dim.getDimTipo().getName());

		}else {
			holder.dimContainer.setVisibility(View.GONE);
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
	public void addAll(List<PedidoLinea> lineas) {
		this.datos.addAll(lineas);
	}
	
	public void addLinea(PedidoLinea prod) {
		this.datos.add(prod);
	}
	public void delLinea(PedidoLinea prod) {
		this.datos.remove(prod);
	}
	public void delProduct(PedidoLinea item) {
		this.datos.remove(item);
	}		
}
