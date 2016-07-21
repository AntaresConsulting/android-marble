package ar.com.antaresconsulting.antonstockapp.adapters;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import ar.com.antaresconsulting.antonstockapp.R;
import ar.com.antaresconsulting.antonstockapp.model.Partner;

public class PartnerAdapter extends ArrayAdapter<Partner> {
	private Partner[] datos;
	Activity context;


	static class ViewHolder {
		public TextView lblNombre;
		public TextView lblDomicilio;
		public TextView lblPhone;
		public TextView lblEmail;
		public ImageView imagen;
	}

	public PartnerAdapter(Fragment context) {
		super(context.getActivity(), R.layout.listitem_partner);
		this.context = context.getActivity();
	}
	public PartnerAdapter(Fragment context,Partner[] datosL) {
		super(context.getActivity(), R.layout.listitem_partner);
		this.context = context.getActivity();
		this.datos = datosL;
		this.addAll(this.datos);
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		View rowView = convertView;

		if (rowView == null) {
			LayoutInflater inflater = context.getLayoutInflater();
			rowView = inflater.inflate(R.layout.listitem_partner, null);

			ViewHolder viewHolder = new ViewHolder();

			viewHolder.lblNombre = (TextView) rowView.findViewById(R.id.nombrePart);
			viewHolder.lblDomicilio = (TextView) rowView.findViewById(R.id.domicilioPart);
			viewHolder.lblPhone = (TextView) rowView.findViewById(R.id.phonePart);
			viewHolder.lblEmail = (TextView) rowView.findViewById(R.id.emailPart);
			viewHolder.imagen = (ImageView) rowView.findViewById(R.id.partnerImg);

			rowView.setTag(viewHolder);
		}
		ViewHolder holder = (ViewHolder) rowView.getTag();

		holder.lblNombre.setText(datos[position].getNombre());
		holder.lblDomicilio.setText(datos[position].getDomicilio());
		holder.lblPhone.setText(datos[position].getPhone());
		holder.lblEmail.setText(datos[position].getEmail());

		if(datos[position].getPartnerImg().trim() != ""){
			byte[] decodedString = Base64.decode(datos[position].getPartnerImg().trim(), Base64.DEFAULT);
			Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0,decodedString.length);
			holder.imagen.setImageBitmap(decodedByte);
		}else{
			holder.imagen.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_no_photo));
		}

		return rowView;

	}
}
