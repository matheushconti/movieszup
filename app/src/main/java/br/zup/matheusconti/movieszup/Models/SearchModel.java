package br.zup.matheusconti.movieszup.Models;

import java.util.ArrayList;

public class SearchModel {
    private boolean Response;
    private Integer totalResults;
    private ArrayList<MovieModel> Search;

    public boolean isResponse() {
        return Response;
    }

    public void setResponse(boolean response) {
        Response = response;
    }

    public Integer getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(Integer totalResults) {
        this.totalResults = totalResults;
    }

    public ArrayList<MovieModel> getSearch() {
        return Search;
    }

    public void setSearch(ArrayList<MovieModel> search) {
        Search = search;
    }
}
