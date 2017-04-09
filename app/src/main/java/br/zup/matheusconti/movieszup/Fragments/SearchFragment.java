package br.zup.matheusconti.movieszup.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import br.zup.matheusconti.movieszup.Adapter.MoviesSearchAdapter;
import br.zup.matheusconti.movieszup.MainActivity;
import br.zup.matheusconti.movieszup.Models.MovieModel;
import br.zup.matheusconti.movieszup.Models.SearchModel;
import br.zup.matheusconti.movieszup.R;
import br.zup.matheusconti.movieszup.Retrofit.Interface;
import br.zup.matheusconti.movieszup.Retrofit.RetrofitAPI;
import br.zup.matheusconti.movieszup.Util.Util;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchFragment extends Fragment implements AdapterView.OnItemClickListener {

    private TextView txt_semfilmes;
    private EditText txt_search;
    private ListView rcv_filmes;
    private Interface ws;
    private LinearLayout semfilmes;
    public Context ctx;
    ArrayList<MovieModel> moviesarray;
    private InputMethodManager imm;

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
        txt_search = (EditText) v.findViewById(R.id.txt_search);
        txt_semfilmes = (TextView) v.findViewById(R.id.txt_semfilmes);
        ImageView imv_search = (ImageView) v.findViewById(R.id.imv_search);
        rcv_filmes = (ListView) v.findViewById(R.id.rcv_filmes);
        rcv_filmes.setOnItemClickListener(this);
        semfilmes = (LinearLayout) v.findViewById(R.id.semfilmes);
        txt_search.requestFocus();
        txt_search.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    Busca();
                    return true;
                }
                return false;
            }
        });

        imv_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Busca();

            }
        });

        imm =(InputMethodManager) ctx.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        return v;
    }

    private void Busca(){
        String s = txt_search.getText().toString();
        if(s.isEmpty()){
            Util.showDialog("Erro","Digite algo para podermos encontrar um filme!",ctx);
        }else {
            MainActivity.setProgressState(true);
            HashMap<String, String> params = new HashMap<>();
            params.put("s", s);
            ws.search(params).enqueue(new Callback<SearchModel>() {
                @Override
                public void onResponse(Call<SearchModel> call, Response<SearchModel> response) {
                    MainActivity.setProgressState(false);
                    try {
                        MoviesSearchAdapter adapter = null;
                        SearchModel body = response.body();
                        if (response.isSuccessful() && body != null) {
                            if (body.isResponse()) {
                                moviesarray = body.getSearch();
                                adapter = new MoviesSearchAdapter(ctx, moviesarray);
                                rcv_filmes.setAdapter(adapter);
                            } else {
                                Util.showDialog("Erro!","Não foi possivel encontrar o filme", ctx);
                            }
                        } else {
                            Util.showDialog("Erro!","Ocorreu um problema, verifique sua conexão com a internet!", ctx);
                        }
                        if (adapter != null) {
                            semfilmes.setVisibility(View.GONE);
                            rcv_filmes.setVisibility(View.VISIBLE);
                        } else {
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

            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        }
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        final String imdbid = moviesarray.get(i).getImdbID();
        Util.setFragment((MainActivity) ctx, new MovieFragment(imdbid), false,null);


    }

}
