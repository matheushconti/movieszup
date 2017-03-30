package br.zup.matheusconti.movieszup;

import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import br.zup.matheusconti.movieszup.Fragments.InicioFragment;
import br.zup.matheusconti.movieszup.util.Util;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";

    private static LinearLayout progress_bg;
    private static ImageView progress_img;
    private static MainActivity ctx;
    private static boolean progressstate = false;
    protected SharedPreferences prefs;
    private static final String PREFS_NAME = "PREFS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ctx = this;
        prefs = ctx.getSharedPreferences(PREFS_NAME, 0);

        progress_bg = (LinearLayout) findViewById(R.id.progress_bg);
        progress_img = (ImageView) findViewById(R.id.progress_img);

        Util.setFragment(MainActivity.this, new InicioFragment(), true, null);

        setProgressState(false);
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
            AnimationDrawable rocketAnimation = (AnimationDrawable) progress_img.getBackground();
            rocketAnimation.start();
            ctx.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        } else {
            progress_bg.setVisibility(View.GONE);
            ctx.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }
    }
}