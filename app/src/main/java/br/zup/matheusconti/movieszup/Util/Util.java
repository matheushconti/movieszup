package br.zup.matheusconti.movieszup.Util;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;

import br.zup.matheusconti.movieszup.Database.Persistencia;
import br.zup.matheusconti.movieszup.MainActivity;
import br.zup.matheusconti.movieszup.Models.MovieModel;
import br.zup.matheusconti.movieszup.R;

/**
 * Created by Matheus Conti on 29/03/2017.
 */

public class Util {

    public static void setFragment(FragmentActivity a, Fragment f, boolean pop, Bundle arguments) {

        if(arguments != null) {
            f.setArguments(arguments);
        }

        FragmentManager fm = a.getSupportFragmentManager();
        FragmentTransaction tr = fm.beginTransaction();
        tr.replace(R.id.content_frame, f);

        if (pop) {
            a.getSupportFragmentManager().popBackStack();
        } else {
            tr.addToBackStack(null);
        }
        tr.commit();
    }
    public static String toGlob(String stringFonte) {
        LinkedHashMap<String, String> trad = new LinkedHashMap<String, String>() {
            {
                put("A", "[ÂÀÁÄÃAâãàáäa]");
                put("a", "[ÂÀÁÄÃAâãàáäa]");
                put("E", "[ÊÈÉËEêèéëe]");
                put("e", "[ÊÈÉËEêèéëe]");
                put("I", "[ÎÍÌÏIîíìïi]");
                put("i", "[ÎÍÌÏIîíìïi]");
                put("O", "[ÔÕÒÓÖOôõòóöo]");
                put("o", "[ÔÕÒÓÖOôõòóöo]");
                put("U", "[ÛÙÚÜUûúùüu]");
                put("u", "[ÛÙÚÜUûúùüu]");
                put("C", "[ÇCçc]");
                put("c", "[ÇCçc]");
                put("y", "[ÝŸYýÿy]");
                put("Y", "[ÝŸYýÿy]");
                put("n", "[ÑNñn]");
                put("N", "[ÑNñn]");
            }
        };

        // prepara lista de tradução para caracteres não acentuados
        String charStr = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        // remove os caracteres que já estão no hash de tradução
        for (String key : trad.keySet())
            charStr = charStr.replace(key, "");

        // transforma string de caracteres em lista
        Character chars[] = ArrayUtils.toObject(charStr.toCharArray());
        ArrayList<Character> list = new ArrayList<>(Arrays.asList(chars));

        // cria as listas de tradução para caracteres não acentuados
        ArrayList<String> keyList = new ArrayList<>(), repList = new ArrayList<>();

        for (Character c : list) {
            keyList.add(c.toString());
            repList.add("[" + c.toString().toLowerCase() + c.toString().toUpperCase() + "]");
        }

        // retira os acentos para coincidir com as chaves do hash de tradução
        stringFonte = retiraAcentos(stringFonte);
        // troca caracteres não acentuados por padrão glob com case insensistive. Ex: S ou s por [Ss]
        stringFonte = StringUtils.replaceEach(stringFonte, keyList.toArray(new String[keyList.size()]), repList.toArray(new String[repList.size()]));
        // troca caracteres com possível acentuação (pois os acentos já foram removidos) por padrão glob ignorando case e acentuação. Ex: A ou a por [ÂÀÁÄÃAâãàáäa]
        stringFonte = StringUtils.replaceEach(stringFonte, trad.keySet().toArray(new String[trad.size()]), trad.values().toArray(new String[trad.size()]));

        return "*" + stringFonte + "*";
    }
    public static String retiraAcentos(String stringFonte) {
        String passa = stringFonte;
        passa = passa.replaceAll("[ÂÀÁÄÃ]", "A");
        passa = passa.replaceAll("[âãàáä]", "a");
        passa = passa.replaceAll("[ÊÈÉË]", "E");
        passa = passa.replaceAll("[êèéë]", "e");
        passa = passa.replaceAll("[ÎÍÌÏ]", "I");
        passa = passa.replaceAll("[îíìï]", "i");
        passa = passa.replaceAll("[ÔÕÒÓÖ]", "O");
        passa = passa.replaceAll("[ôõòóö]", "o");
        passa = passa.replaceAll("[ÛÙÚÜ]", "U");
        passa = passa.replaceAll("[ûúùü]", "u");
        passa = passa.replaceAll("Ç", "C");
        passa = passa.replaceAll("ç", "c");
        passa = passa.replaceAll("[ýÿ]", "y");
        passa = passa.replaceAll("Ý", "Y");
        passa = passa.replaceAll("ñ", "n");
        passa = passa.replaceAll("Ñ", "N");
        return passa;
    }

    public static DisplayImageOptions getUml(){
        DisplayImageOptions umlOptions = new DisplayImageOptions.Builder()
                .resetViewBeforeLoading(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .cacheInMemory(false)
                .cacheOnDisk(true)
//                .imageScaleType(ImageScaleType.EXACTLY)
                .build();
        return umlOptions;
    }

    public static void setAssistido(String imdbID, ImageView imv_assistido) {
        String assistidoretorno = Persistencia.updateMovie(imdbID);
        if(assistidoretorno.equals("Sim")){
            imv_assistido.setImageResource(R.drawable.success);
        }else{
            imv_assistido.setImageResource(R.drawable.success_white);
        }
    }
    public static void setAdicionado(MovieModel movie, ImageView imv_assistido) {
        if(!Persistencia.hasMovie(movie.getImdbID())){
            Persistencia.setMovie(movie);
            imv_assistido.setImageResource(R.drawable.list_success);
        }else{
            Persistencia.deleteMovie(movie.getImdbID());
            imv_assistido.setImageResource(R.drawable.list);
        }
    }

    public static void showDialog(String msg, String title, Context ctx) {
        // custom dialog
        final Dialog dialog = new Dialog(ctx, R.style.customDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); //before
        dialog.setContentView(R.layout.dialog);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dialog.getWindow();
        lp.copyFrom(window.getAttributes());

        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);

        TextView txt_titulo_dialog = (TextView) dialog.findViewById(R.id.txt_titulo_dialog);
        TextView txt_mensagem_dialog = (TextView) dialog.findViewById(R.id.txt_mensagem_dialog);

        txt_mensagem_dialog.setText(msg);
        txt_titulo_dialog.setText(title);

        Button btn_ok = (Button) dialog.findViewById(R.id.btn_dialog);

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public static void imgLoader(String url, final ImageView img){

        ImageLoader imageLoader = ImageLoader.getInstance();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(MainActivity.getCtx())
                .memoryCache(new WeakMemoryCache())
                .denyCacheImageMultipleSizesInMemory().threadPoolSize(5)
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .threadPoolSize(10)
                .build();
        if(!imageLoader.isInited()) {
            imageLoader.init(config);
        }
        imageLoader.displayImage(url, img, Util.getUml(), new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
//                img.setVisibility(View.GONE);
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                img.setVisibility(View.VISIBLE);
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {
            }
        }, new ImageLoadingProgressListener() {
            @Override
            public void onProgressUpdate(String imageUri, View view, int current, int total) {
            }
        });
    }
}
