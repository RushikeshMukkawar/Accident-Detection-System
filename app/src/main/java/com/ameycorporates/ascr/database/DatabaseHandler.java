package com.ameycorporates.ascr.database;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DatabaseHandler {
    SQLiteDatabase sql;
    public DatabaseHandler()
    {
        Log.e("message","in constructor");
    }
    public void insertData(String query)
    {
        sql=LocalDatabase.localDatabase.getWritableDatabase();
        sql.execSQL(query);
        sql.close();
    }
    public Cursor getData(String query)
    {

        Log.e("message","in get data");
        sql=LocalDatabase.localDatabase.getReadableDatabase();
        Log.e("message","got the data");
       Cursor cr=sql.rawQuery(query,null);

        return cr;
    }
}
