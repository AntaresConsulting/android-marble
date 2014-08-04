package ar.com.antaresconsulting.antonstockapp.adapters;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import ar.com.antaresconsulting.antonstockapp.R;
import ar.com.antaresconsulting.antonstockapp.model.Empleado;

public class EmpleadosAdapter extends BaseAdapter {
	List<Empleado> datos = new ArrayList<Empleado>();
	private int mSelectedVariation;

	Activity context;

	static class ViewHolder {
		public TextView lblNombre;
		public TextView lblDpto;
		public TextView lblCargo;
		public ImageView imagen;
		public RadioButton radioB;
	}

	public EmpleadosAdapter(Fragment context, List<Empleado> prods) {
		this.context = context.getActivity();
		this.datos = prods;
	}

	public EmpleadosAdapter(Activity context) {
		this.context = context;
	}

	public EmpleadosAdapter(Fragment context) {
		this.context = context.getActivity();
	}

	public void addEmple(Empleado prod) {
		this.datos.add(prod);
	}

	public void delEmple(Empleado prod) {
		this.datos.remove(prod);
	}

	public void addAll(List prods) {
		this.datos.addAll(prods);
	}

	@Override
	public int getCount() {
		return this.datos.size();
	}

	@Override
	public Empleado getItem(int arg0) {
		return this.datos.get(arg0);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		View rowView = convertView;

		if (rowView == null) {
			LayoutInflater inflater = context.getLayoutInflater();
			rowView = inflater.inflate(R.layout.listitem_emple, null);

			ViewHolder viewHolder = new ViewHolder();

			viewHolder.lblNombre = (TextView) rowView
					.findViewById(R.id.nombreEmple);
			viewHolder.lblDpto = (TextView) rowView
					.findViewById(R.id.dptoEmpleVal);
			viewHolder.lblCargo = (TextView) rowView
					.findViewById(R.id.cargoEmpleVal);
			viewHolder.imagen = (ImageView) rowView.findViewById(R.id.empleImg);
			viewHolder.radioB = (RadioButton) rowView
					.findViewById(R.id.RadioButton1);

			rowView.setTag(viewHolder);
		}
		ViewHolder holder = (ViewHolder) rowView.getTag();

		Empleado emple = datos.get(position);
		holder.lblNombre.setText(emple.getNombre());
		if (emple.getDepartamento().length > 0)
			holder.lblDpto.setText(emple.getDepartamento()[1].toString());
		if (emple.getCargo().length > 0)
			holder.lblCargo.setText(emple.getCargo()[1].toString());
		if ((emple.getImagen() != null) && (emple.getImagen().trim() != "")) {
			byte[] decodedString = Base64.decode(emple.getImagen().trim(),
					Base64.DEFAULT);
			Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString,
					0, decodedString.length);
			holder.imagen.setImageBitmap(decodedByte);
		} else {
			holder.imagen.setImageDrawable(context.getResources().getDrawable(
					R.drawable.ic_user_employee));
		}

		if (position == mSelectedVariation)
			holder.radioB.setChecked(true);
		else
			holder.radioB.setChecked(false);

		rowView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mSelectedVariation = position;
				EmpleadosAdapter.this.notifyDataSetChanged();
			}
		});

		return rowView;

	}
}
