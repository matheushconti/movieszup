package br.zup.matheusconti.movieszup.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;

import java.util.ArrayList;
import java.util.HashMap;

import br.zup.matheusconti.movieszup.Models.MovieModel;
import br.zup.matheusconti.movieszup.R;
import br.zup.matheusconti.movieszup.Retrofit.Interface;
import br.zup.matheusconti.movieszup.Retrofit.RetrofitAPI;
import br.zup.matheusconti.movieszup.Util.Util;

public class MoviesAdapter extends BaseAdapter {
    private Context ctx;
    public ArrayList<MovieModel> movies;
    public MovieModel obj;

    public MoviesAdapter(Context ctx, ArrayList<MovieModel> movies) {
        this.ctx = ctx;
        this.movies = movies;
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

        final ViewHolder holder;

        try {
            if (convertView == null) {
                convertView = ((LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.adapter_movie, parent, false);

                holder = new ViewHolder();
                assert convertView != null;

                holder.imv_movie_adapter = (ImageView) convertView.findViewById(R.id.imv_movie_adapter);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            obj = movies.get(position);

            Util.imgLoader(obj.getPoster(),holder.imv_movie_adapter);

        } catch (Exception e) {
            Log.e("MoviesZup", "Erro getView", e);
        }
        return convertView;
    }

    private static class ViewHolder {
        private ImageView imv_movie_adapter;
        private LinearLayout ll_empty_movie;
    }
}
