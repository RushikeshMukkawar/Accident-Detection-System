package com.ameycorporates.ascr.maingui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.ameycorporates.ascr.R;
import com.ameycorporates.ascr.database.DatabaseHandler;
import com.ameycorporates.ascr.database.LocalDatabase;
import com.ameycorporates.ascr.userloginprocess.LoginActivity;
import com.ameycorporates.ascr.userloginprocess.RegisterActivity;

public class MainActivity extends AppCompatActivity {
    int flag=1;
    int userid=-1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();


        LocalDatabase.localDatabase=new LocalDatabase(this);

        DatabaseHandler db=new DatabaseHandler();

        Cursor cr=db.getData("select * from user_master;");

        while(cr.moveToNext())
        {
            flag=2;
            userid=cr.getInt(0);
            Log.e("error","in while loop");
            Log.e("data","Data is "+cr.getString(0)+" : "+cr.getString(1)+" ");

        }

           Handler hd=new Handler();
           hd.postDelayed(new Runnable() {
               @Override
               public void run() {
                        if(flag==1) {
                            Intent it = new Intent(MainActivity.this, RegisterActivity.class);
                            startActivity(it);
                            finish();
                        }
                        else
                        {
                            Intent it=new Intent(MainActivity.this, DashboardActivity.class);
                            it.putExtra("myid",""+userid);
                            startActivity(it);
                            finish();
                        }
               }
           },1000);

    }
}