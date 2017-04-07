package br.zup.matheusconti.movieszup.Util;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;

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
}
