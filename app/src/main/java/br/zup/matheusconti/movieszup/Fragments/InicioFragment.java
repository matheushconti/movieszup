package br.zup.matheusconti.movieszup.Fragments;

import android.app.Dialog;
import android.media.Image;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;


import org.w3c.dom.Text;

import java.util.ArrayList;

import br.zup.matheusconti.movieszup.Adapter.MoviesAdapter;
import br.zup.matheusconti.movieszup.Database.Persistencia;
import br.zup.matheusconti.movieszup.MainActivity;
import br.zup.matheusconti.movieszup.Models.MovieModel;
import br.zup.matheusconti.movieszup.R;
import br.zup.matheusconti.movieszup.Util.ExpandableHeightGridView;
import br.zup.matheusconti.movieszup.Util.Util;

public class InicioFragment extends Fragment {

    private ImageView imv_ultimofilme, imv_assistido_ultimofilme;
    private TextView txt_titulo_ultimofilme,txt_desc_ultimofilme, txt_filter_movies, txt_filter_order;
    private LinearLayout ll_ultimofilme, semfilmes, meusfilmes, filter_movies, filter_order;
    private ExpandableHeightGridView grid_movies;
    public ScrollView scroll;
    public String order;
    public String filter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_inicio, container, false);
        ImageView imv_busca = (ImageView) v.findViewById(R.id.imv_busca);
        semfilmes = (LinearLayout) v.findViewById(R.id.semfilmes);
        meusfilmes = (LinearLayout) v.findViewById(R.id.meusfilmes);
        filter_movies = (LinearLayout) v.findViewById(R.id.filter_movies);
        filter_movies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogMovies("filter");
            }
        });
        filter_order = (LinearLayout) v.findViewById(R.id.filter_order);
        filter_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogMovies("order");
            }
        });
        txt_filter_movies = (TextView) v.findViewById(R.id.txt_filter_movies);
        txt_filter_order = (TextView) v.findViewById(R.id.txt_filter_order);
        grid_movies = (ExpandableHeightGridView) v.findViewById(R.id.grid_movies);
        scroll = (ScrollView) v.findViewById(R.id.scroll);


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


        GridMovies(null,null);
        scroll.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                scroll.fullScroll(ScrollView.FOCUS_UP);
                Log.e("Pasou","subiu");
                scroll.getViewTreeObserver().removeOnScrollChangedListener(this);
            }
        });
        ll_ultimofilme.requestFocus();


        return v;
    }

    public void GridMovies(String filtro, String order){
        final MoviesAdapter adapter;
        final ArrayList<MovieModel> movies = Persistencia.showMovies(filtro,order);

        if(movies != null && movies.size() > 0){
            meusfilmes.setVisibility(View.VISIBLE);
            semfilmes.setVisibility(View.GONE);


            final MovieModel firstmovie = movies.get(0);

            Util.imgLoader(firstmovie.getPoster(),imv_ultimofilme);
            txt_titulo_ultimofilme.setText(firstmovie.getTitle());
            txt_desc_ultimofilme.setText(firstmovie.getYear() + " - " +firstmovie.getRuntime());
            if(firstmovie.getAssistido().equals("Sim")){
                imv_assistido_ultimofilme.setImageResource(R.drawable.success);
            }else{
                imv_assistido_ultimofilme.setImageResource(R.drawable.success_white);
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

            adapter = new MoviesAdapter(getActivity(), movies);
            grid_movies.setAdapter(adapter);

            grid_movies.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    MovieModel movie = movies.get(i);
                    Util.setFragment(getActivity(), new MovieFragment(movie.getImdbID()), false,null);
                }
            });

        }else if(filtro != null  || order != null){
            Util.showDialog("OPS!","Nenhum filme nesse filtro!",MainActivity.getCtx());
        }else{
            meusfilmes.setVisibility(View.GONE);
            semfilmes.setVisibility(View.VISIBLE);
        }
    }
    public void dialogMovies(String type) {
        // custom dialog
        final Dialog dialog = new Dialog(MainActivity.getCtx(), R.style.customDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); //before
        dialog.setCanceledOnTouchOutside(true);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dialog.getWindow();
        if(window != null) {
            lp.copyFrom(window.getAttributes());
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            window.setAttributes(lp);
        }


        if(type.equals("order")) {
            dialogComponentsOrder(dialog);
        }else{
            dialogComponentsFilter(dialog);
        }


        dialog.show();
    }

    public void dialogComponentsOrder(final Dialog dialog){
        dialog.setContentView(R.layout.dialog_order);

        TextView cadastro_order = (TextView) dialog.findViewById(R.id.cadastro_order);
        TextView alfabetica_asc_order = (TextView) dialog.findViewById(R.id.alfabetica_asc_order);
        TextView alfabetica_desc_order = (TextView) dialog.findViewById(R.id.alfabetica_desc_order);
        if(txt_filter_order.getText().equals("Data de Cadastro")){
            cadastro_order.setBackgroundResource(R.color.whiteopac);
        }else if(txt_filter_order.getText().equals("Ordem Alfabética asc")){
            alfabetica_asc_order.setBackgroundResource(R.color.whiteopac);
        }else if(txt_filter_order.getText().equals("Ordem Alfabética desc")){
            alfabetica_desc_order.setBackgroundResource(R.color.whiteopac);
        }

        View.OnClickListener escolha = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch (v.getId()) {
                    case R.id.cadastro_order:
                        order = " dataInsert desc";
                        break;
                    case R.id.alfabetica_asc_order:
                        order = " Title asc";
                        break;
                    case R.id.alfabetica_desc_order:
                        order = " Title desc";
                        break;
                    default:
                        order = " dataInsert desc";
                }

                TextView holder = (TextView) v;
                holder.setBackgroundResource(R.color.whiteopac);
                txt_filter_order.setText(holder.getText().toString());
                dialog.dismiss();
                GridMovies(filter,order);
            }
        };

        cadastro_order.setOnClickListener(escolha);
        alfabetica_asc_order.setOnClickListener(escolha);
        alfabetica_desc_order.setOnClickListener(escolha);
    }
    public void dialogComponentsFilter(final Dialog dialog){
        dialog.setContentView(R.layout.dialog_filtro);
        TextView todos_filtrar = (TextView) dialog.findViewById(R.id.todos_filtrar);
        TextView assistidos_filtrar = (TextView) dialog.findViewById(R.id.assistidos_filtrar);
        TextView naoassistidos_filtrar = (TextView) dialog.findViewById(R.id.naoassistidos_filtrar);

        if(txt_filter_movies.getText().equals("Todos os filmes")){
            todos_filtrar.setBackgroundResource(R.color.whiteopac);
        }else if(txt_filter_movies.getText().equals("Filmes assistidos")){
            assistidos_filtrar.setBackgroundResource(R.color.whiteopac);
        }else if(txt_filter_movies.getText().equals("Filmes não assistidos")){
            naoassistidos_filtrar.setBackgroundResource(R.color.whiteopac);
        }

        View.OnClickListener escolha = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.todos_filtrar:
                        filter = "todos";
                        break;
                    case R.id.assistidos_filtrar:
                        filter = "assistidos";
                        break;
                    case R.id.naoassistidos_filtrar:
                        filter = "naoassistidos";
                        break;
                    default:
                        filter = "todos";
                }

                TextView holder = (TextView) v;
                txt_filter_movies.setText(holder.getText().toString());
                holder.setBackgroundResource(R.color.whiteopac);
                dialog.dismiss();
                GridMovies(filter,order);
            }
        };

        todos_filtrar.setOnClickListener(escolha);
        assistidos_filtrar.setOnClickListener(escolha);
        naoassistidos_filtrar.setOnClickListener(escolha);
    }
}
