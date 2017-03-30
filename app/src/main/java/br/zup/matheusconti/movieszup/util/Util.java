package br.zup.matheusconti.movieszup.util;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

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
}
