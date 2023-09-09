package com.ameycorporates.ascr.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class LocalDatabase extends SQLiteOpenHelper {
    private static final String localdbname="ascr.db";
    public static LocalDatabase localDatabase;
    public LocalDatabase(Context context)
    {
        super(context,localdbname,null,1);

    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table if not exists user_master(id interger, user_name text, phone_no text, email_id text , password text);");

        sqLiteDatabase.execSQL("create table if not exists contact_master(id interger , contact_name text, phone_no text, email_id text);");


    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("create table if not exists user_master(id interger, user_name text, phone_no text, email_id text , password text);");

        sqLiteDatabase.execSQL("create table if not exists contact_master(id interger, contact_name text, phone_no text, email_id text);");
    }




}
