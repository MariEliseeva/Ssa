package alekhina_eliseeva.ssa.controller;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by mari on 06.11.17.
 */

public class DBHelper extends SQLiteOpenHelper {
    private int type;
    public DBHelper(Context context, String name) {
       super(context, name, null, 1);
       if (name == "songsDB") {
           type = 1;
       } else if (name == "usersDB") {
            type = 0;
       } else {
           type = 2;
       }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("DB", "--- onCreate database ---");
        // создаем таблицу с полями
        if (type == 0) {
            db.execSQL("create table logins ("
                    + "login text,"
                    + "score," + ");");
        } else if (type == 1){
            db.execSQL("create table songs ("
                    + "song text" + ");");
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}