package br.zup.matheusconti.movieszup.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
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

public class MoviesSearchAdapter extends BaseAdapter {

    private Context ctx;
    private Interface ws;
    private ArrayList<MovieModel> movies;
    private LayoutInflater mInflater;

    public MoviesSearchAdapter(Context ctx, ArrayList<MovieModel> movies) {
        mInflater = LayoutInflater.from(ctx);
        this.ctx = ctx;
        this.movies = movies;
        ws = new RetrofitAPI().create(Interface.class);
    }
    private static class ViewHolder {
        TextView txt_titulo_listafilme,txt_desc_listafilme,txt_ano_listafilme;
        LinearLayout ll_filme;
        ImageView imv_add_listafilme, imv_assistido_listafilme,imv_listafilme;
    }

    @Override
    public int getCount() {
        return movies.size();
    }

    @Override
    public MovieModel getItem(int position) {
        try {
            return movies.get(position);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final MovieModel obj = movies.get(position);
        convertView = mInflater.inflate(R.layout.adapter_filmes, null);
        final ViewHolder lv = new ViewHolder();

        lv.txt_titulo_listafilme = (TextView) convertView.findViewById(R.id.txt_titulo_listafilme);
        lv.txt_desc_listafilme = (TextView) convertView.findViewById(R.id.txt_desc_listafilme);
        lv.txt_ano_listafilme = (TextView) convertView.findViewById(R.id.txt_ano_listafilme);
        lv.ll_filme = (LinearLayout) convertView.findViewById(R.id.ll_filme);
        lv.imv_listafilme = (ImageView) convertView.findViewById(R.id.imv_listafilme);
        lv.imv_add_listafilme = (ImageView) convertView.findViewById(R.id.imv_add_listafilme);
        lv.imv_assistido_listafilme = (ImageView) convertView.findViewById(R.id.imv_assistido_listafilme);

        if (Persistencia.hasMovie(obj.getImdbID())) {
            if (Persistencia.getMovie(obj.getImdbID()).getAdicionado().equals("Sim")) {
                lv.imv_add_listafilme.setImageResource(R.drawable.list_success);
            }
            if (Persistencia.getMovie(obj.getImdbID()).getAssistido().equals("Sim")) {
                lv.imv_assistido_listafilme.setImageResource(R.drawable.success);
            }
        }

        try {

            lv.txt_titulo_listafilme.setText(obj.getTitle());
            lv.txt_desc_listafilme.setText(obj.getPlot());
            lv.txt_ano_listafilme.setText(obj.getYear());

            if (!obj.getPoster().equals("N/A")) {
                Util.imgLoader(obj.getPoster(), lv.imv_listafilme);
            }
        } catch (Exception e) {
            Log.e("MoviesZup", "Erro getView", e);
        }

        lv.imv_add_listafilme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gravarFilme(obj.getImdbID(),(ImageView)view,false,(ImageView)view.getTag());
            }
        });

        lv.imv_assistido_listafilme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!Persistencia.hasMovie(obj.getImdbID())) {
                    gravarFilme(obj.getImdbID(),(ImageView)view.getTag(),true,(ImageView)view);
                }else{
                    Util.setAssistido(obj.getImdbID(),(ImageView)view);
                }
            }
        });

        lv.imv_assistido_listafilme.setTag(lv.imv_add_listafilme);
        lv.imv_add_listafilme.setTag(lv.imv_assistido_listafilme);

        return convertView;
    }

    private void gravarFilme(final String imdbid, final ImageView imv, final boolean assistido, final ImageView imv_assistido){
        MainActivity.setProgressState(true);
        HashMap<String, String> params = new HashMap<>();
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
                            Util.setAdicionado(body,imv);
                            if(assistido && imv_assistido!= null){
                                Util.setAssistido(body.getImdbID(),imv_assistido);
                            }else if(!Persistencia.hasMovie(imdbid) && imv_assistido != null) {
                                imv_assistido.setImageResource(R.drawable.success_white);
                            }
                        } else {
                            Util.showDialog("Erro!","Não foi possivel adicionar o filme, verifique sua conexão com a internet!", ctx);
                        }
                    } else {
                        Util.showDialog("Erro!","Ocorreu um erro, verifique sua conexão com a internet!", ctx);
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
    }
}
