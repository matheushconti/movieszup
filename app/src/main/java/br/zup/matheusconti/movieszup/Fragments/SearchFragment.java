package br.zup.matheusconti.movieszup.Fragments;

import android.content.Context;
import android.media.Image;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;

import br.zup.matheusconti.movieszup.Adapter.RecyclerViewMovieAdapter;
import br.zup.matheusconti.movieszup.MainActivity;
import br.zup.matheusconti.movieszup.Models.MovieModel;
import br.zup.matheusconti.movieszup.Models.SearchModel;
import br.zup.matheusconti.movieszup.R;
import br.zup.matheusconti.movieszup.Retrofit.Interface;
import br.zup.matheusconti.movieszup.Retrofit.RetrofitAPI;
import br.zup.matheusconti.movieszup.Util.NoScrollLayoutManager;
import br.zup.matheusconti.movieszup.Util.Util;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchFragment extends Fragment {

    private TextView txt_search,txt_semfilmes;
    private ImageView imv_search;
    private RecyclerView rcv_filmes;
    private Interface ws;
    private LinearLayout semfilmes;
    public Context ctx;

    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_search, container, false);
        ctx = getContext();
        ws = new RetrofitAPI().create(Interface.class);
        txt_search = (TextView) v.findViewById(R.id.txt_search);
        txt_semfilmes = (TextView) v.findViewById(R.id.txt_semfilmes);
        imv_search = (ImageView) v.findViewById(R.id.imv_search);
        rcv_filmes = (RecyclerView) v.findViewById(R.id.rcv_filmes);
        semfilmes = (LinearLayout) v.findViewById(R.id.semfilmes);

        imv_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String s = txt_search.getText().toString();
                if(s.equals("")){
                    Util.showDialog("Erro","Digite algo para podermos encontrar um filme!",ctx);
                }else{
                    Busca(s);
                }
            }
        });
        return v;
    }

    private void Busca(String s){
        MainActivity.setProgressState(true);
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("s", s.toString());
        ws.search(params).enqueue(new Callback<SearchModel>() {
            @Override
            public void onResponse(Call<SearchModel> call, Response<SearchModel> response) {
                MainActivity.setProgressState(false);
                try {
                    RecyclerViewMovieAdapter adapter = null;
                    SearchModel body = response.body();
                    if (response.isSuccessful() && body != null) {
                        if (body.isResponse()) {

                            RecyclerView.LayoutManager mLayoutManager = new NoScrollLayoutManager(getActivity()) {
                                @Override
                                public boolean canScrollVertically() {
                                    return false;
                                }
                            };
                            adapter = new RecyclerViewMovieAdapter(body.getSearch(),ctx);
                            rcv_filmes.setLayoutManager(mLayoutManager);
                            rcv_filmes.setItemAnimator(new DefaultItemAnimator());
                            rcv_filmes.setAdapter(adapter);
                        } else {
//                            Util.showDialog("Erro!","Não foi possivel adicionar o filme, verifique sua conexão com a internet!", ctx);
                        }
                    } else {
                    }
                    if(adapter != null){
                        semfilmes.setVisibility(View.GONE);
                        rcv_filmes.setVisibility(View.VISIBLE);
                    }else{
                        semfilmes.setVisibility(View.VISIBLE);
                        rcv_filmes.setVisibility(View.GONE);
                        txt_semfilmes.setText("Nenhum filme encontrado com esses paramêtros, tente novamente outro titulo.");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<SearchModel> call, Throwable t) {
                MainActivity.setProgressState(false);
                t.printStackTrace();
            }
        });

    }
}
