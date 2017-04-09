package br.zup.matheusconti.movieszup.Retrofit;

import java.util.Map;

import br.zup.matheusconti.movieszup.Models.MovieModel;
import br.zup.matheusconti.movieszup.Models.SearchModel;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

public interface Interface {
    @GET("/")
    Call<SearchModel> search(@QueryMap Map<String, String> params);

    @GET("/")
    Call<MovieModel> movie(@QueryMap Map<String, String> params);
}
