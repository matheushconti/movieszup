package br.zup.matheusconti.movieszup.Fragments;

import android.media.Image;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;



import java.util.ArrayList;

import br.zup.matheusconti.movieszup.Adapter.MoviesAdapter;
import br.zup.matheusconti.movieszup.Database.Persistencia;
import br.zup.matheusconti.movieszup.Models.MovieModel;
import br.zup.matheusconti.movieszup.R;
import br.zup.matheusconti.movieszup.Util.ExpandableHeightGridView;
import br.zup.matheusconti.movieszup.Util.Util;

public class InicioFragment extends Fragment {

    private ImageView imv_ultimofilme, imv_assistido_ultimofilme;
    private TextView txt_titulo_ultimofilme,txt_desc_ultimofilme;
    private LinearLayout ll_ultimofilme, semfilmes, meusfilmes;
    private ExpandableHeightGridView grid_movies;
    public ScrollView scroll;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_inicio, container, false);
        ImageView imv_busca = (ImageView) v.findViewById(R.id.imv_busca);
        semfilmes = (LinearLayout) v.findViewById(R.id.semfilmes);
        meusfilmes = (LinearLayout) v.findViewById(R.id.meusfilmes);
        grid_movies = (ExpandableHeightGridView) v.findViewById(R.id.grid_movies);
        scroll = (ScrollView) v.findViewById(R.id.scroll);
        scroll.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                scroll.fullScroll(ScrollView.FOCUS_UP);
                scroll.getViewTreeObserver().removeOnScrollChangedListener(this);

            }
        });

        ll_ultimofilme = (LinearLayout) v.findViewById(R.id.ll_ultimofilme);
        imv_ultimofilme = (ImageView) v.findViewById(R.id.imv_ultimofilme);
        imv_assistido_ultimofilme = (ImageView) v.findViewById(R.id.imv_assistido_ultimofilme);
        txt_titulo_ultimofilme = (TextView) v.findViewById(R.id.txt_titulo_ultimofilme);
        txt_desc_ultimofilme = (TextView) v.findViewById(R.id.txt_desc_ultimofilme);


        imv_busca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Util.setFragment(getActivity(), new SearchFragment(), false, null);
            }
        });
        semfilmes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Util.setFragment(getActivity(), new SearchFragment(), false, null);
            }
        });


        GridMovies();

        ll_ultimofilme.requestFocus();


        return v;
    }

    public void GridMovies(){
        final ArrayList<MovieModel> movies = Persistencia.showMovies();

        if(movies != null && movies.size() > 0){
            meusfilmes.setVisibility(View.VISIBLE);
            semfilmes.setVisibility(View.GONE);


            final MovieModel firstmovie = movies.get(0);

            Util.imgLoader(firstmovie.getPoster(),imv_ultimofilme);
            txt_titulo_ultimofilme.setText(firstmovie.getTitle());
            txt_desc_ultimofilme.setText(firstmovie.getYear() + " - " +firstmovie.getRuntime());
            if(firstmovie.getAssistido().equals("Sim")){
                imv_assistido_ultimofilme.setImageResource(R.drawable.success);
            }
            imv_assistido_ultimofilme.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Util.setAssistido(firstmovie.getImdbID(),imv_assistido_ultimofilme);
                }
            });

            ll_ultimofilme.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Util.setFragment(getActivity(), new MovieFragment(firstmovie.getImdbID()), false, null);
                }
            });

            movies.remove(0);

            final MoviesAdapter adapter = new MoviesAdapter(getActivity(), movies);
            grid_movies.setAdapter(adapter);

            grid_movies.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    MovieModel movie = movies.get(i);
                    Util.setFragment(getActivity(), new MovieFragment(movie.getImdbID()), false,null);
                }
            });

        }else{
            meusfilmes.setVisibility(View.GONE);
            semfilmes.setVisibility(View.VISIBLE);
        }
    }
}
