package br.zup.matheusconti.movieszup.Database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;


import br.zup.matheusconti.movieszup.MainActivity;
import br.zup.matheusconti.movieszup.Models.MovieModel;
import br.zup.matheusconti.movieszup.Util.Util;

public class Persistencia {

    public static MovieModel getMovie(String imdbId) {
        SQLiteDatabase db = Bancodedados.getDB(MainActivity.getCtx());
        db.beginTransaction();

        ArrayList<String> params = new ArrayList<>();

        String select = "select * from movies where imdbID = ?";
        params.add(imdbId);

        String[] arr_params = new String[params.size()];
        arr_params = params.toArray(arr_params);
        MovieModel movie = null;
        try {
            Cursor c = db.rawQuery(select, arr_params);

            if (c != null) {
                if (c.moveToFirst()) {
                    movie = new MovieModel();
                    movie.setImdbID(c.getString(c.getColumnIndex("imdbID")));
                    movie.setType(c.getString(c.getColumnIndex("Type")));
                    movie.setTitle(c.getString(c.getColumnIndex("Title")));
                    movie.setYear(c.getString(c.getColumnIndex("Year")));
                    movie.setRated(c.getString(c.getColumnIndex("Rated")));
                    movie.setReleased(c.getString(c.getColumnIndex("Released")));
                    movie.setRuntime(c.getString(c.getColumnIndex("Runtime")));
                    movie.setGenre(c.getString(c.getColumnIndex("Genre")));
                    movie.setDirector(c.getString(c.getColumnIndex("Director")));
                    movie.setWriter(c.getString(c.getColumnIndex("Writer")));
                    movie.setActors(c.getString(c.getColumnIndex("Actors")));
                    movie.setPlot(c.getString(c.getColumnIndex("Plot")));
                    movie.setLanguage(c.getString(c.getColumnIndex("Language")));
                    movie.setCountry(c.getString(c.getColumnIndex("Country")));
                    movie.setAwards(c.getString(c.getColumnIndex("Awards")));
                    movie.setPoster(c.getString(c.getColumnIndex("Poster")));
                    movie.setMetascore(c.getString(c.getColumnIndex("Metascore")));
                    movie.setImdbRating(c.getString(c.getColumnIndex("imdbRating")));
                    movie.setImdbVotes(c.getString(c.getColumnIndex("imdbVotes")));
                    movie.setDVD(c.getString(c.getColumnIndex("DVD")));
                    movie.setProduction(c.getString(c.getColumnIndex("Production")));
                    movie.setWebsite(c.getString(c.getColumnIndex("Website")));
                    movie.setAssistido(c.getString(c.getColumnIndex("Assistido")));
                    movie.setAdicionado(c.getString(c.getColumnIndex("Adicionado")));

                    c.close();
                }
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e("MoviesZup", "Erro banco get", e);
        } finally {
            db.endTransaction();
        }

        return movie;
    }

    public static boolean hasMovie(String imdbId) {
        SQLiteDatabase db = Bancodedados.getDB(MainActivity.getCtx());
        db.beginTransaction();

        ArrayList<String> params = new ArrayList<>();

        String select = "select imdbID from movies where imdbID = ?";
        params.add(imdbId);

        String[] arr_params = new String[params.size()];
        arr_params = params.toArray(arr_params);
        boolean movie = false;
        try {
            Cursor c = db.rawQuery(select, arr_params);

            if (c != null) {
                if (c.moveToFirst()) {
                    movie = true;
                    c.close();
                }
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e("MoviesZup", "Erro banco get", e);
        } finally {
            db.endTransaction();
        }

        return movie;
    }
   
    public static ArrayList<MovieModel> showMovies(CharSequence search) {
        SQLiteDatabase db = Bancodedados.getDB(MainActivity.getCtx());
        db.beginTransaction();

        ArrayList<String> params = new ArrayList<>();

        String select = "select * from movies";
        if(search != null && !search.toString().isEmpty()) {
            select += " where name glob ?";
            String glob = Util.toGlob(search.toString());
            params.add(glob);
        }
        select += " order by dataInsert desc";

        String[] arr_params = new String[params.size()];
        arr_params = params.toArray(arr_params);

        ArrayList<MovieModel> movies = new ArrayList<>();
        try {
            Cursor c = db.rawQuery(select, arr_params);

            if (c != null) {
                if (c.moveToFirst()) {
                    do {
                        MovieModel movie = new MovieModel();

                        movie = new MovieModel();
                        movie.setImdbID(c.getString(c.getColumnIndex("imdbID")));
                        movie.setType(c.getString(c.getColumnIndex("Type")));
                        movie.setTitle(c.getString(c.getColumnIndex("Title")));
                        movie.setYear(c.getString(c.getColumnIndex("Year")));
                        movie.setRated(c.getString(c.getColumnIndex("Rated")));
                        movie.setReleased(c.getString(c.getColumnIndex("Released")));
                        movie.setRuntime(c.getString(c.getColumnIndex("Runtime")));
                        movie.setGenre(c.getString(c.getColumnIndex("Genre")));
                        movie.setDirector(c.getString(c.getColumnIndex("Director")));
                        movie.setWriter(c.getString(c.getColumnIndex("Writer")));
                        movie.setActors(c.getString(c.getColumnIndex("Actors")));
                        movie.setPlot(c.getString(c.getColumnIndex("Plot")));
                        movie.setLanguage(c.getString(c.getColumnIndex("Language")));
                        movie.setCountry(c.getString(c.getColumnIndex("Country")));
                        movie.setAwards(c.getString(c.getColumnIndex("Awards")));
                        movie.setPoster(c.getString(c.getColumnIndex("Poster")));
                        movie.setMetascore(c.getString(c.getColumnIndex("Metascore")));
                        movie.setImdbRating(c.getString(c.getColumnIndex("imdbRating")));
                        movie.setImdbVotes(c.getString(c.getColumnIndex("imdbVotes")));
                        movie.setDVD(c.getString(c.getColumnIndex("DVD")));
                        movie.setProduction(c.getString(c.getColumnIndex("Production")));
                        movie.setWebsite(c.getString(c.getColumnIndex("Website")));
                        movie.setAssistido(c.getString(c.getColumnIndex("Assistido")));
                        movie.setAdicionado(c.getString(c.getColumnIndex("Adicionado")));

                        movies.add(movie);
                    }while (c.moveToNext());
                    c.close();
                }
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e("MoviesZup", "Erro banco get", e);
        } finally {
            db.endTransaction();
        }

        return movies;
    }

    public static ArrayList<MovieModel> showMovies() {
        SQLiteDatabase db = Bancodedados.getDB(MainActivity.getCtx());
        db.beginTransaction();

        String select = "select * from movies order by dataInsert desc";


        ArrayList<MovieModel> movies = new ArrayList<>();
        try {
            Cursor c = db.rawQuery(select,null);

            if (c != null) {
                if (c.moveToFirst()) {
                    do {
                        MovieModel movie = new MovieModel();

                        movie = new MovieModel();
                        movie.setImdbID(c.getString(c.getColumnIndex("imdbID")));
                        movie.setType(c.getString(c.getColumnIndex("Type")));
                        movie.setTitle(c.getString(c.getColumnIndex("Title")));
                        movie.setYear(c.getString(c.getColumnIndex("Year")));
                        movie.setRated(c.getString(c.getColumnIndex("Rated")));
                        movie.setReleased(c.getString(c.getColumnIndex("Released")));
                        movie.setRuntime(c.getString(c.getColumnIndex("Runtime")));
                        movie.setGenre(c.getString(c.getColumnIndex("Genre")));
                        movie.setDirector(c.getString(c.getColumnIndex("Director")));
                        movie.setWriter(c.getString(c.getColumnIndex("Writer")));
                        movie.setActors(c.getString(c.getColumnIndex("Actors")));
                        movie.setPlot(c.getString(c.getColumnIndex("Plot")));
                        movie.setLanguage(c.getString(c.getColumnIndex("Language")));
                        movie.setCountry(c.getString(c.getColumnIndex("Country")));
                        movie.setAwards(c.getString(c.getColumnIndex("Awards")));
                        movie.setPoster(c.getString(c.getColumnIndex("Poster")));
                        movie.setMetascore(c.getString(c.getColumnIndex("Metascore")));
                        movie.setImdbRating(c.getString(c.getColumnIndex("imdbRating")));
                        movie.setImdbVotes(c.getString(c.getColumnIndex("imdbVotes")));
                        movie.setDVD(c.getString(c.getColumnIndex("DVD")));
                        movie.setProduction(c.getString(c.getColumnIndex("Production")));
                        movie.setWebsite(c.getString(c.getColumnIndex("Website")));
                        movie.setAssistido(c.getString(c.getColumnIndex("Assistido")));
                        movie.setAdicionado(c.getString(c.getColumnIndex("Adicionado")));

                        movies.add(movie);
                    }while (c.moveToNext());
                    c.close();
                }
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e("MoviesZup", "Erro banco get", e);
        } finally {
            db.endTransaction();
        }

        return movies;
    }

    public static void deleteMovie(String imdbId) {
        SQLiteDatabase db = Bancodedados.getDB(MainActivity.getCtx());
        db.beginTransaction();
        try{
            db.delete("movies","imdbID==?",new String[]{imdbId});
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e("MoviesZup", "Erro banco get", e);
        } finally {
            db.endTransaction();
        }
    }

    public static String updateMovie(String imdbId) {
        SQLiteDatabase db = Bancodedados.getDB(MainActivity.getCtx());
        db.beginTransaction();

        ArrayList<String> params = new ArrayList<>();

        String select = "select * from movies where imdbID = "+imdbId;

        String assistido = "Não";
        try {
            Cursor c = db.rawQuery(select, null);
            if (c != null) {
                if (c.moveToFirst()) {
                    if(c.getString(c.getColumnIndex("Assistido")).equals("Sim")){
                        assistido = "Não";
                    }else{
                        assistido = "Sim";
                    }
                    ContentValues cv = new ContentValues();
                    cv.put("Assistido",assistido);

                    db.update("movies",cv,"imdbID="+imdbId,null);
                }
                c.close();
            }
        } catch (Exception e) {
            Log.e("MoviesZup", "Erro banco get", e);
        } finally {
            db.endTransaction();
        }

        return assistido;


    }

    public static void setMovie(MovieModel movie) {
        SQLiteDatabase db = Bancodedados.getDB(MainActivity.getCtx());
        db.beginTransaction();

        try {
            if(movie != null) {
                ContentValues values = new ContentValues();
                values.put("imdbID", movie.getImdbID());
                values.put("Type", movie.getType());
                values.put("Title", movie.getTitle());
                values.put("Year", movie.getYear());
                values.put("Rated", movie.getRated());
                values.put("Released", movie.getReleased());
                values.put("Runtime", movie.getRuntime());
                values.put("Genre", movie.getGenre());
                values.put("Director", movie.getDirector());
                values.put("Writer", movie.getWriter());
                values.put("Actors", movie.getActors());
                values.put("Plot", movie.getPlot());
                values.put("Language", movie.getLanguage());
                values.put("Country", movie.getCountry());
                values.put("Awards", movie.getAwards());
                values.put("Poster", movie.getPoster());
                values.put("Metascore", movie.getMetascore());
                values.put("imdbRating", movie.getImdbRating());
                values.put("imdbVotes", movie.getImdbVotes());
                values.put("DVD", movie.getDVD());
                values.put("Production", movie.getProduction());
                values.put("Website", movie.getWebsite());
                values.put("Assistido", movie.getAssistido());
                values.put("Adicionado", "Sim");

                db.insertWithOnConflict("movies", null, values, SQLiteDatabase.CONFLICT_REPLACE);
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e("MoviesZup", "Erro banco set", e);
        } finally {
            db.endTransaction();
        }
    }
}
