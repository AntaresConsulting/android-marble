package ar.com.antaresconsulting.antonstockapp;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import ar.com.antaresconsulting.antonstockapp.model.Partner;
import ar.com.antaresconsulting.antonstockapp.model.dao.PartnerDAO;

/**
 * A fragment representing a single Partner detail screen. This fragment is
 * either contained in a {@link PartnerListActivity} in two-pane mode (on
 * tablets) or a {@link PartnerDetailActivity} on handsets.
 */
public class PartnerDetailFragment extends Fragment implements PartnerDAO.ClientsCallbacks{
	/**
	 * The fragment argument representing the item ID that this fragment
	 * represents.
	 */
	public static final String ARG_ITEM_ID = "item_id";
	private PartnerDAO pDao;
	private Partner pData;
	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public PartnerDetailFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (getArguments().containsKey(ARG_ITEM_ID)) {
			// Load the dummy content specified by the fragment
			// arguments. In a real-world scenario, use a Loader
			// to load content from a content provider.
			this.pDao = new PartnerDAO(this);
			this.pDao.getClientDetail(getArguments().getString(	ARG_ITEM_ID));
		}
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		if (this.pData != null) {
			((TextView) view.findViewById(R.id.nombrePart)).setText(this.pData.getNombre());
			((TextView) view.findViewById(R.id.webPart)).setText(this.pData.getWeb());
			((TextView) view.findViewById(R.id.domicilioPart)).setText(this.pData.getDomicilio());
			((TextView) view.findViewById(R.id.phonePart)).setText(this.pData.getPhone());
			((TextView) view.findViewById(R.id.emailPart)).setText(this.pData.getEmail());
			((TextView) view.findViewById(R.id.cuitPart)).setText(this.pData.getCuit());
			
			if(this.pData.getPartnerImg().trim() != ""){
				byte[] decodedString = Base64.decode(this.pData.getPartnerImg().trim(), Base64.DEFAULT);
				Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0,decodedString.length);
				((ImageView) view.findViewById(R.id.partnerImg)).setImageBitmap(decodedByte);
			}else{
				((ImageView) view.findViewById(R.id.partnerImg)).setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_no_photo));
			}
			
		}		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_detail_partner,
				container, false);
		return rootView;
	}

	@Override
	public void setClients() {

	}

	@Override
	public void setClientDetail() {
		Partner[] partners = this.pDao.getPartnersArray();
		this.pData = partners[0];	
		this.onViewCreated(getView(), getArguments());
		
	}

}
