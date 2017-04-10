package br.zup.matheusconti.movieszup;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import br.zup.matheusconti.movieszup.Database.Bancodedados;
import br.zup.matheusconti.movieszup.Fragments.InicioFragment;
import br.zup.matheusconti.movieszup.Util.Util;

public class MainActivity extends AppCompatActivity {

    private static LinearLayout progress_bg;
    public static MainActivity ctx;
    private static boolean progressstate = false;;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ctx = this;
        Bancodedados.initDB();
        progress_bg = (LinearLayout) findViewById(R.id.progress_bg);

        Util.setFragment(ctx, new InicioFragment(), true, null);

        setProgressState(false);
    }

    public static Context getCtx(){
        return ctx;
    }

    @Override
    public void onBackPressed() {

        if (progressstate) {
            setProgressState(false);
        } else {
            super.onBackPressed();
        }
    }

//    Metodo para disparar o Carregando
    public static void setProgressState(boolean on) {
        progressstate = on;
        if (on) {
            progress_bg.setVisibility(View.VISIBLE);
            ctx.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        } else {
            progress_bg.setVisibility(View.GONE);
            ctx.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }
    }
}
