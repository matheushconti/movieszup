package br.zup.matheusconti.movieszup.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.Locale;
import java.util.Scanner;

import br.zup.matheusconti.movieszup.MainActivity;

public abstract class Bancodedados {
    private static final int VERSION = 1;
    private static DBHelperPrincipal dbhelper;
    private static SQLiteDatabase db;
    private static Context ctx = MainActivity.getCtx();

    public static SQLiteDatabase initDB() {
        if (dbhelper == null)
            dbhelper = new DBHelperPrincipal(MainActivity.getCtx(), "movieszup", VERSION);

        if (db == null) {
            db = dbhelper.getReadableDatabase();
            configDB(db);
        }

        return db;
    }

    private static void configDB(SQLiteDatabase db) {
        db.setLocale(Locale.getDefault());
        db.execSQL("PRAGMA foreign_keys = ON;");
    }

    public static SQLiteDatabase getDB(Context _ctx) {
        ctx = _ctx;

        initDB();
        return db;
    }

    public static void closeDB() {
        if (db != null) {
            db.close();
            db = null;
        }

        if (dbhelper != null) {
            dbhelper.close();
            dbhelper = null;
        }
    }

    private static void execSqlFile(SQLiteDatabase db, String filename) throws IOException {
        Log.d("Banco de dados", "Executando arquivo: " + filename);

//        String script = new Scanner(ctx.getAssets().getClass().getResourceAsStream("scripts/" + filename), "UTF-8").useDelimiter("\\AA").next();
//        String script = IOUtils.toString(ctx.getAssets().open("scripts/" + filename));

//        String script = CharStreams.toString(new InputStreamReader(ctx.getAssets().open("scripts/" + filename), Charsets.UTF_8));

        Scanner s = new Scanner(ctx.getAssets().open("scripts/" + filename)).useDelimiter("\\A");
        String script = s.hasNext() ? s.next() : "";

        Log.d("script",script);
        String lines[] = script.split(";");
        for (String line : lines)
            try {
                line = StringUtils.strip(line, "\r\n").trim();
                if (!line.isEmpty())
                    db.execSQL(line);

            } catch (Exception e) {
                Log.e("MoviesZup", line, e);
            }

    }



    private static class DBHelperPrincipal extends SQLiteOpenHelper {
        private static final String TAG = "GenericDBHelper";

        DBHelperPrincipal(Context context, String dbname, int version) {
            super(context, dbname, null, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.d(TAG, "Criando banco de dados");
            Bancodedados.db = db;
            try {
                execSqlFile(db, "criacaodb.sql");
            } catch (IOException e) {
                Log.e(TAG, "Erro ao criar banco de dados!", e);
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Bancodedados.db = db;
            Log.d(TAG, "Atualizando da versão " + oldVersion + " para " + newVersion + ".");
        }

        @Override
        public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.d(TAG, "Downgrade da versão " + oldVersion + " para " + newVersion + ".");
        }
    }
}
