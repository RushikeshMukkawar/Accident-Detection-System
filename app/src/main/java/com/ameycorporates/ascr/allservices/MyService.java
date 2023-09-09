package com.ameycorporates.ascr.allservices;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;
import android.provider.Settings;

import com.ameycorporates.ascr.R;
import com.ameycorporates.ascr.maingui.DashboardActivity;

public class MyService extends Service {

    public MyService() {

    }


    MediaPlayer mp=null;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if(Settings.System.canWrite(getApplicationContext())) {
            int flag=1;
            int counter=0;
            mp=MediaPlayer.create(this.getApplicationContext(),R.raw.finalaudio);
            mp.start();
            while(true) {
                if(counter>2)
                {
                    break;
                }
                else if(!mp.isPlaying()) {
                    mp.start();
                    counter++;
                }
            }
        }
        else
        {
            // permission not granted
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {

        if (mp != null)
        {
            mp.stop();
        }
    }

    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }
}