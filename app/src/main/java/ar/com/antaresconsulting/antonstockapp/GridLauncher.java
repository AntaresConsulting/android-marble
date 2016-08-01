package ar.com.antaresconsulting.antonstockapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.openerp.OpenErpHolder;

public class GridLauncher extends Fragment {

    public GridLauncher() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.grid_launcher, container, false);
        ImageButton btPart =  (ImageButton) rootView.findViewById(R.id.partnerButton);
        btPart.setVisibility(View.GONE);
        ImageButton btPRoduct =  (ImageButton) rootView.findViewById(R.id.prodButton);
        btPRoduct.setVisibility(View.GONE);
        ImageButton btOE =  (ImageButton) rootView.findViewById(R.id.genOEButton);
        btOE.setVisibility(View.GONE);
        ImageButton btPM =  (ImageButton) rootView.findViewById(R.id.genPMButton);
        btPM.setVisibility(View.GONE);
        ImageButton btInsu =  (ImageButton) rootView.findViewById(R.id.insumoButton);
        btInsu.setVisibility(View.GONE);
        ImageButton btIncome =  (ImageButton) rootView.findViewById(R.id.incomeButton);
        btIncome.setVisibility(View.GONE);
        ImageButton btRemit =  (ImageButton) rootView.findViewById(R.id.remitoButton);
        btRemit.setVisibility(View.GONE);
        ImageButton btReincome =  (ImageButton) rootView.findViewById(R.id.reincomeButton);
        btReincome.setVisibility(View.GONE);
        ImageButton btProduction =  (ImageButton) rootView.findViewById(R.id.productionButton);
        btProduction.setVisibility(View.GONE);

        if(OpenErpHolder.getInstance().getmOConn().isManager()){
            btPart.setVisibility(View.VISIBLE);
            btPRoduct.setVisibility(View.VISIBLE);
            btPM.setVisibility(View.VISIBLE);
            btOE.setVisibility(View.VISIBLE);
        }
        if(OpenErpHolder.getInstance().getmOConn().isCutter()){
            btPRoduct.setVisibility(View.VISIBLE);
            btReincome.setVisibility(View.VISIBLE);
            btProduction.setVisibility(View.VISIBLE);
        }
        if(OpenErpHolder.getInstance().getmOConn().isResponsable()){
            btPRoduct.setVisibility(View.VISIBLE);
            btInsu.setVisibility(View.VISIBLE);
            btIncome.setVisibility(View.VISIBLE);
            btRemit.setVisibility(View.VISIBLE);
        }

        return rootView;
    }


}
