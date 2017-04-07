package br.zup.matheusconti.movieszup.Fragments;

import android.content.Context;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import br.zup.matheusconti.movieszup.MainActivity;
import br.zup.matheusconti.movieszup.R;
import br.zup.matheusconti.movieszup.Util.Util;

public class InicioFragment extends Fragment {

    ImageView imv_busca;
    LinearLayout ll_ultimofilme,ll_ordenar;

    public InicioFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_inicio, container, false);

        imv_busca = (ImageView) v.findViewById(R.id.imv_busca);
        imv_busca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Util.setFragment(getActivity(), new SearchFragment(), false, null);
            }
        });

        ll_ultimofilme = (LinearLayout) v.findViewById(R.id.ll_ultimofilme);
        ll_ultimofilme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Util.setFragment(getActivity(), new MovieFragment(), false, null);
            }
        });

        return v;
    }
}
