package ar.com.antaresconsulting.antonstockapp.adapters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.openerp.OpenErpHolder;

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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;
import ar.com.antaresconsulting.antonstockapp.R;
import ar.com.antaresconsulting.antonstockapp.model.BaseProduct;

public class BaseProductAdapter extends BaseAdapter {
	List<BaseProduct> datos = new ArrayList<BaseProduct>();
	List<BaseProduct> checked = new ArrayList<BaseProduct>();
	
	Activity context;
	
	static class ViewHolder {
		public TextView lblNombre;
		public TextView lblCode;
		public TextView lblPrice;
		public TextView lblAtribs;
		public TextView lblCantOnHand;
		public TextView lblCantForecast;
		public TextView lblCantIncome;
		public ImageView imagen;
		public CheckBox checkB;
	}

	public BaseProductAdapter(Fragment context,List<BaseProduct> prods) {
		this.context = context.getActivity();
		this.datos = prods;
	}

	public BaseProductAdapter(Activity context) {
		this.context = context;
	}

	public BaseProductAdapter(Fragment context) {
		this.context = context.getActivity();
	}
	
	public void addProduct(BaseProduct prod) {
		this.datos.add(prod);
	}
	public void delProduct(BaseProduct prod) {
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
	public BaseProduct getItem(int arg0) {
		return this.datos.get(arg0);
	}

	@Override
	public long getItemId(int position) {
		 return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View rowView = convertView;

		if (rowView == null) {
			LayoutInflater inflater = context.getLayoutInflater();
			rowView = inflater.inflate(R.layout.listitem_product, null);

			ViewHolder viewHolder = new ViewHolder();

			viewHolder.lblNombre = (TextView) rowView.findViewById(R.id.nombreProd);
			viewHolder.lblCode = (TextView) rowView.findViewById(R.id.codigoVal);
			viewHolder.lblAtribs = (TextView) rowView.findViewById(R.id.atribs);
			viewHolder.lblPrice = (TextView) rowView.findViewById(R.id.precioVal);
			viewHolder.lblCantOnHand = (TextView) rowView.findViewById(R.id.stockRVal);
			viewHolder.lblCantForecast = (TextView) rowView.findViewById(R.id.stockFVal);
			viewHolder.lblCantIncome = (TextView) rowView.findViewById(R.id.stockIVal);
			viewHolder.imagen = (ImageView) rowView.findViewById(R.id.prodImg);
			viewHolder.checkB = (CheckBox) rowView.findViewById(R.id.checkBox1);

			rowView.setTag(viewHolder);
		}
		ViewHolder holder = (ViewHolder) rowView.getTag();

		BaseProduct prod =  datos.get(position);
		holder.lblNombre.setText(datos.get(position).getNombre());
		holder.lblCode.setText(datos.get(position).getCodigo());
		if(prod.getAtributos()!= null && !prod.getAtributos().trim().equalsIgnoreCase("")){
			holder.lblAtribs.setText(prod.getAtributos());
		}else{
			holder.lblAtribs.setVisibility(View.GONE);
		}
		if(OpenErpHolder.getInstance().getmOConn().isManager()){
			holder.lblPrice.setText(datos.get(position).getPrice().toString());
		}else{
			rowView.findViewById(R.id.precioRow).setVisibility(View.GONE);
		}
		if(datos.get(position).getCantidadReal().doubleValue() > 0)
			holder.lblCantOnHand.setText(datos.get(position).getCantidadReal().toString());
		else
			rowView.findViewById(R.id.stockRealRow).setVisibility(View.GONE);
		if(datos.get(position).getCantidadForecast().doubleValue() > 0)
			holder.lblCantForecast.setText(datos.get(position).getCantidadForecast().toString());
		else
			rowView.findViewById(R.id.stockFRow).setVisibility(View.GONE);
		if(datos.get(position).getCantidadIncome().doubleValue() > 0)
			holder.lblCantIncome.setText(datos.get(position).getCantidadIncome().toString());
		else
			rowView.findViewById(R.id.stockIRow).setVisibility(View.GONE);
		
		if((datos.get(position).getProductImg() != null) && (datos.get(position).getProductImg().trim() != "")){
			byte[] decodedString = Base64.decode(datos.get(position).getProductImg().trim(), Base64.DEFAULT);
			Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0,decodedString.length);
			holder.imagen.setImageBitmap(decodedByte);
		}else{
			holder.imagen.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_no_photo));
		}
		holder.checkB.setTag(prod);
		holder.checkB.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				CheckBox cb = (CheckBox) v;
				setCheckedItem((BaseProduct)cb.getTag());
			}
		});
		return rowView;

	}

	public void setCheckedItem(BaseProduct item) {
        if (checked.contains(item)){
            checked.remove(item);
        }
        else {
            checked.add(item);
        }
    }
	
    public List<BaseProduct> getCheckedItems(){
        return checked;
    }
}
