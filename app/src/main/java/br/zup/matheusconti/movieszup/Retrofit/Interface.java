package br.zup.matheusconti.movieszup.Retrofit;

import android.graphics.Movie;

import com.google.gson.JsonElement;

import java.util.ArrayList;
import java.util.Map;

import br.zup.matheusconti.movieszup.Models.MovieModel;
import br.zup.matheusconti.movieszup.Models.SearchModel;
import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface Interface {
    @FormUrlEncoded
    @GET
    Call<ArrayList<SearchModel>> search(@Url String url, @FieldMap Map<String, String> params);

    @FormUrlEncoded
    @GET
    Call<ArrayList<MovieModel>> movie(@Url String url, @FieldMap Map<String, String> params);
}
