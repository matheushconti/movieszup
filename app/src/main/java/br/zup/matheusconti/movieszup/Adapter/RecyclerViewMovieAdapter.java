package br.zup.matheusconti.movieszup.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import br.zup.matheusconti.movieszup.Database.Persistencia;
import br.zup.matheusconti.movieszup.Fragments.MovieFragment;
import br.zup.matheusconti.movieszup.MainActivity;
import br.zup.matheusconti.movieszup.Models.MovieModel;
import br.zup.matheusconti.movieszup.R;
import br.zup.matheusconti.movieszup.Retrofit.Interface;
import br.zup.matheusconti.movieszup.Retrofit.RetrofitAPI;
import br.zup.matheusconti.movieszup.Util.Util;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecyclerViewMovieAdapter extends RecyclerView.Adapter<RecyclerViewMovieAdapter.ListItemViewHolder> {

    public ArrayList<MovieModel> items;
    public Context ctx;
    public Interface ws;

    public RecyclerViewMovieAdapter(ArrayList<MovieModel> modelData, final Context ctx) {
        if (modelData == null) {
            throw new IllegalArgumentException("modelData must not be null");
        }
        items = modelData;
        this.ctx = ctx;
    }

    public void addData(MovieModel new_obj, int position) {
        if (position > -1)
            items.add(position, new_obj);
        else
            items.add(new_obj);

        notifyItemInserted(position);
    }

    public MovieModel getItem(int position) {
        return items.get(position);
    }

    @Override
    public ListItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adapter_filmes, viewGroup, false);
        return new ListItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ListItemViewHolder lv, int position) {
        try {
            final MovieModel obj = items.get(position);

            ws = new RetrofitAPI().create(Interface.class);
            lv.txt_titulo_listafilme.setText(obj.getTitle());
            lv.txt_desc_listafilme.setText(obj.getPlot());
            lv.txt_ano_listafilme.setText(obj.getYear());

            lv.imv_add_listafilme.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    gravarFilme(obj.getImdbID(),lv.imv_add_listafilme);
                }
            });
            lv.imv_assistido_listafilme.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MovieModel movie = Persistencia.getMovie(obj.getImdbID());
                    if(movie==null){
                        gravarFilme(obj.getImdbID(),lv.imv_add_listafilme);
                    }
                    Util.setAssistido(obj.getImdbID(),lv.imv_assistido_listafilme);
                }
            });
            lv.ll_filme.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Util.setFragment((MainActivity) ctx, new MovieFragment(obj.getImdbID()), false,null);
                }
            });

            Util.imgLoader(obj.getPoster(),lv.imv_listafilme);



        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public final static class ListItemViewHolder extends RecyclerView.ViewHolder {

        TextView txt_titulo_listafilme,txt_desc_listafilme,txt_ano_listafilme;
        LinearLayout ll_filme;
        ImageView imv_add_listafilme, imv_assistido_listafilme,imv_listafilme;

        public ListItemViewHolder(View itemView) {
            super(itemView);
            txt_titulo_listafilme = (TextView) itemView.findViewById(R.id.txt_titulo_listafilme);
            txt_desc_listafilme = (TextView) itemView.findViewById(R.id.txt_desc_listafilme);
            txt_ano_listafilme = (TextView) itemView.findViewById(R.id.txt_ano_listafilme);
            ll_filme = (LinearLayout) itemView.findViewById(R.id.ll_filme);
            imv_add_listafilme = (ImageView) itemView.findViewById(R.id.imv_add_listafilme);
            imv_listafilme = (ImageView) itemView.findViewById(R.id.imv_listafilme);
            imv_assistido_listafilme = (ImageView) itemView.findViewById(R.id.imv_assistido_listafilme);
        }
    }

    private void gravarFilme(String imdbid, final ImageView imv){
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
                            Util.setAdicionado(body,imv);
                        } else {
                            Util.showDialog("Erro!","Não foi possivel adicionar o filme, verifique sua conexão com a internet!", ctx);
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
    }
}
