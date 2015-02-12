package ar.com.antaresconsulting.antonstockapp.model.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Fragment;
import ar.com.antaresconsulting.antonstockapp.model.DimensionBalance;
import com.openerp.ReadAsyncTask;

public class DimensionDAO extends ReadAsyncTask {


	private Fragment activityPart;

	public interface DimsCallbacks {
		void setDimensions();

	}

	public DimensionDAO(Fragment frag) {
		super(frag);
		this.extraData =  true;
		this.activityPart = frag;
		this.mModel = "product.marble.dimension.balance";
		this.mFields = new String[] { "product_id", "dimension_id", "qty_unit", "qty_m2"};
	}
	public void getAllDims(Integer productId,boolean _extraData) {
		this.extraData = _extraData;
		this.setmFilters(new Object[] { new Object[] { "product_id", "=", productId },new Object[] { "qty_unit", ">", 0 } });
		this.execute(this.mFields);
	}
	public void getAllDims(Integer productId) {
		this.setmFilters(new Object[] { new Object[] { "product_id", "=", productId },new Object[] { "qty_unit", ">", 0 } });
		this.execute(this.mFields);
	}
	
	@Override
	public void dataFetched() {
			((DimsCallbacks) this.activityPart).setDimensions();
	}

	public List<DimensionBalance> getDimensionList() {
		List<DimensionBalance> datosProds = new ArrayList<DimensionBalance>();
		int i = 0 ;
		for (HashMap<String, Object> obj : this.mData) {
			DimensionBalance resp = new DimensionBalance();
			Object[] marble_id = obj.get("product_id") instanceof Boolean ? new Object[0]: (Object[]) obj.get("product_id");
			Object[] dimension_id = obj.get("dimension_id") instanceof Boolean ? new Object[0]: (Object[]) obj.get("dimension_id");
			Double qty_unit = obj.get("qty_unit") instanceof Boolean ? 0: (Double) obj.get("qty_unit");
			Double qty_m2 = obj.get("qty_m2") instanceof Boolean ? 0: (Double) obj.get("qty_m2");

			resp.setDimId(dimension_id);
			resp.setProdId(marble_id);
			resp.setQtyM2(qty_m2);
			resp.setQtyUnits(qty_unit);
			if(extraData)
				resp.setDimensionVals((HashMap<String, Object>) this.mExtraData.get(i).get("dimension_id"));
			datosProds.add(resp);
			i++;
		}
		return datosProds;

	}

	public void getAllDims(Integer productId, String alto, String ancho) {
		this.setmFilters(new Object[] { new Object[] { "product_id", "=", productId },new Object[] { "qty_unit", ">", 0 },new Object[] { "dimension_id.hight", ">", alto },new Object[] { "dimension_id.width", ">", ancho } });
		this.execute(this.mFields);		
	}	
	
}
