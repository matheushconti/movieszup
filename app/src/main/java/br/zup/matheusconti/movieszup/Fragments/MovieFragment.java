package br.zup.matheusconti.movieszup.Fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;

import java.util.HashMap;

import br.zup.matheusconti.movieszup.Database.Persistencia;
import br.zup.matheusconti.movieszup.MainActivity;
import br.zup.matheusconti.movieszup.Models.MovieModel;
import br.zup.matheusconti.movieszup.R;
import br.zup.matheusconti.movieszup.Retrofit.Interface;
import br.zup.matheusconti.movieszup.Retrofit.RetrofitAPI;
import br.zup.matheusconti.movieszup.Util.Util;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieFragment extends Fragment {

    public MovieFragment() {
        // Required empty public constructor
    }

    public String imdbid = "";
    public MovieFragment(String imdbid) {
        this.imdbid = imdbid;
    }
    public MovieModel movie;
    public MovieFragment(MovieModel movie) {
        this.movie = movie;
    }

    public ImageView imv_back,imv_filme,imv_assistido_filme,imv_add_filme;
    public TextView txt_titulo_filme,txt_year_filme,txt_genre_filme,txt_country_filme,txt_plot,txt_director,
            txt_actors,txt_awards,txt_metascore,txt_rating,txt_producion,txt_website,txt_dvd,txt_screenplay;
    public Context ctx;
    public Interface ws;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_movie, container, false);
        ctx = getContext();
        ws = new RetrofitAPI().create(Interface.class);
        imv_back = (ImageView) v.findViewById(R.id.imv_back);
        imv_filme = (ImageView) v.findViewById(R.id.imv_filme);
        imv_add_filme = (ImageView) v.findViewById(R.id.imv_add_filme);
        imv_assistido_filme = (ImageView) v.findViewById(R.id.imv_assistido_filme);

        txt_titulo_filme = (TextView) v.findViewById(R.id.txt_titulo_filme);
        txt_year_filme = (TextView) v.findViewById(R.id.txt_year_filme);
        txt_genre_filme = (TextView) v.findViewById(R.id.txt_genre_filme);
        txt_country_filme = (TextView) v.findViewById(R.id.txt_country_filme);

        txt_plot = (TextView) v.findViewById(R.id.txt_plot);
        txt_director = (TextView) v.findViewById(R.id.txt_director);
        txt_screenplay = (TextView) v.findViewById(R.id.txt_screenplay);
        txt_actors = (TextView) v.findViewById(R.id.txt_actors);
        txt_awards = (TextView) v.findViewById(R.id.txt_awards);
        txt_metascore = (TextView) v.findViewById(R.id.txt_metascore);
        txt_rating = (TextView) v.findViewById(R.id.txt_rating);
        txt_producion = (TextView) v.findViewById(R.id.txt_producion);
        txt_website = (TextView) v.findViewById(R.id.txt_website);
        txt_dvd = (TextView) v.findViewById(R.id.txt_dvd);



        if(movie == null){
            MainActivity.setProgressState(true);
            HashMap<String, String> params = new HashMap<String, String>();
            params.put("i", imdbid);
            params.put("plot", "full");
            ws.movie(params).enqueue(new Callback<MovieModel>() {
                @Override
                public void onResponse(Call<MovieModel> call, Response<MovieModel> response) {
                    MainActivity.setProgressState(false);
                    try {
                        MovieModel body = response.body();
                        if (response.isSuccessful() && body != null) {
                            if (body.isResponse()) {
                                loadInfos(body);
                            } else {
                                Util.showDialog("Erro!","Não foi possivel encontrar o filme, verifique sua conexão com a internet!", ctx);
                            }
                        } else {
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                @Override
                public void onFailure(Call<MovieModel> call, Throwable t) {
                    MainActivity.setProgressState(false);
                    t.printStackTrace();
                }
            });
        }else{
            loadInfos(movie);
        }

        return v;
    }

    public void loadInfos(final MovieModel movieinfo){
        if(movieinfo.getAssistido().equals("Sim")){
            imv_assistido_filme.setImageResource(R.drawable.success);
        }
        if(movieinfo.getAdicionado().equals("Sim")){
            imv_add_filme.setImageResource(R.drawable.list_success);
        }
        Util.imgLoader(movieinfo.getPoster(), imv_filme);

        txt_titulo_filme.setText(movieinfo.getTitle());
        txt_year_filme.setText(movieinfo.getReleased());
        txt_genre_filme.setText(movieinfo.getGenre());
        txt_country_filme.setText(movieinfo.getCountry());

        txt_plot.setText(movieinfo.getPlot());
        txt_director.setText(movieinfo.getDirector());
        txt_screenplay.setText(movieinfo.getWriter());
        txt_actors.setText(movieinfo.getActors());
        txt_awards.setText(movieinfo.getAwards());
        txt_metascore.setText(movieinfo.getMetascore());
        txt_rating.setText(movieinfo.getImdbRating());
        txt_producion.setText(movieinfo.getProduction());
        txt_website.setText(movieinfo.getWebsite());
        txt_dvd.setText(movieinfo.getDVD());

        imv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().popBackStackImmediate();
            }
        });
        imv_assistido_filme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Util.setAdicionado(movieinfo,imv_add_filme);
                Util.setAssistido(movieinfo.getImdbID(),imv_assistido_filme);
            }
        });
        imv_add_filme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Util.setAdicionado(movieinfo,imv_add_filme);
            }
        });
    }
}
