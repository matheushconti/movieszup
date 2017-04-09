package br.zup.matheusconti.movieszup.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import br.zup.matheusconti.movieszup.Models.MovieModel;
import br.zup.matheusconti.movieszup.R;
import br.zup.matheusconti.movieszup.Util.Util;

public class MoviesAdapter extends BaseAdapter {
    private ArrayList<MovieModel> movies;
    private LayoutInflater mInflater;

    public MoviesAdapter(Context ctx, ArrayList<MovieModel> movies) {
        mInflater = LayoutInflater.from(ctx);
        this.movies = movies;
    }
    private static class ViewHolder {
        private ImageView imv_movie_adapter, imv_assistido_adapter;
        private TextView txt_titulo_adapter;
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
                convertView = mInflater.inflate(R.layout.adapter_movie, null);

                holder = new ViewHolder();
                assert convertView != null;

                holder.imv_movie_adapter = (ImageView) convertView.findViewById(R.id.imv_movie_adapter);
                holder.imv_assistido_adapter = (ImageView) convertView.findViewById(R.id.imv_assistido_adapter);
                holder.txt_titulo_adapter = (TextView) convertView.findViewById(R.id.txt_titulo_adapter);
                holder.imv_assistido_adapter.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Util.setAssistido((String) view.getTag(),(ImageView)view);
                    }
                });
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            final MovieModel obj;
            obj = movies.get(position);

            Util.imgLoader(obj.getPoster(),holder.imv_movie_adapter);
            holder.txt_titulo_adapter.setText(obj.getTitle());
            if(obj.getAssistido().equals("Sim")){
                holder.imv_assistido_adapter.setImageResource(R.drawable.success);
            }
            holder.imv_assistido_adapter.setTag(obj.getImdbID());


        } catch (Exception e) {
            Log.e("MoviesZup", "Erro getView", e);
        }
        return convertView;
    }


}
