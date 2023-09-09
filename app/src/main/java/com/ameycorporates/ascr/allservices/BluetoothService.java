package com.ameycorporates.ascr.allservices;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.ParcelUuid;
import android.telecom.TelecomManager;
import android.telephony.TelephonyManager;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import com.ameycorporates.ascr.R;
import com.ameycorporates.ascr.maingui.DashboardActivity;
import com.ameycorporates.ascr.maingui.MainActivity;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

public class BluetoothService extends Service {
    public BluetoothService() {
    }

    static int flag=1;
    static int falldetected=1;
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        final String channelid="1001";
        NotificationChannel channel=new NotificationChannel(channelid,channelid, NotificationManager.IMPORTANCE_HIGH);
        getSystemService(NotificationManager.class).createNotificationChannel(channel);


        Intent myintent=new Intent(this, MainActivity.class);
        myintent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        PendingIntent pendingIntent=PendingIntent.getActivity(this.getApplicationContext(),0,myintent,PendingIntent.FLAG_IMMUTABLE);


        Notification.Builder notification=new Notification.Builder(this,channelid);
        notification.setContentTitle("Accident Detection Service enable");
        notification.setContentText("Service is running..");
        notification.setPriority(Notification.PRIORITY_DEFAULT);
        notification.setContentIntent(pendingIntent);
        notification.setSmallIcon(R.drawable.bike_icon);

        long vibrate[]={100,500,100,500};
        notification.setVibrate(vibrate);
        notification.setVisibility(Notification.VISIBILITY_PUBLIC);

        startForeground(1001,notification.build());

        Bundle b = intent.getBundleExtra("mydata");
        BluetoothDevice device = (BluetoothDevice) b.getParcelable("data");

        if (device == null) {
            //can't process
        }

        ParcelUuid uuid[]=device.getUuids();

    Runnable r=new Runnable() {
    @Override
    public void run() {


        try {
            BluetoothSocket soc = device.createInsecureRfcommSocketToServiceRecord(uuid[0].getUuid());
            // while(!soc.isConnected()) {

            //   }
            soc.connect();
            Log.e("Connect","Connecting");
            InputStream is=soc.getInputStream();

            Log.e("Connect","input stream builded");
            passToActivity("connected");

            while(true)
            {
                    char ch=(char)is.read();
                    if(ch=='F')
                    {
                        Log.e("DETECTED","FALL DETECTED");
                        startService(new Intent(BluetoothService.this,MyService.class));

                        Runnable r=new Runnable() {
                            @Override
                            public void run() {
                                startService(new Intent(BluetoothService.this,SMSService.class));
                                 startService(new Intent(BluetoothService.this,PhoneCallHandler.class));
                                //IntentFilter it=new IntentFilter(Intent.ACTION_SCREEN_ON);
                                //BroadcastReceiver br=new stopMusicService();
                                //registerReceiver(br,it);

                            }
                        };
                        Thread th=new Thread(r);
                        th.start();

                        falldetected=2;
                    }
                    else if(ch=='a')
                    {
                        Log.e("DETECTED","trigger 1 detected..");
                    }
                    else if(ch=='b')
                    {
                        Log.e("DETECTED","trigger 2 detected");
                    }
                    else if(ch=='c')
                    {
                        Log.e("DETECTED","trigger 3 detected");
                    }
                    else if(ch=='A')
                    {
                        Log.e("DETECTED","trigger 1 deactivated");
                    }
                    else if(ch=='B')
                    {
                        Log.e("DETECTED","trigger 2 deactivated");
                    }
                    else if(ch=='C')
                    {
                        Log.e("DETECTED","trigger 3 deactivated");
                    }
                    if(flag==2)
                    {
                        flag=1;
                    }
            }

        }catch (Exception ex)
        {
            Log.e("ERROR","Can't able to create a socket with bluetooth device : "+ex);
            passToActivity("disconnected");


        }
    }
};
        Thread th=new Thread(r);
        th.start();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {

        stopService(new Intent(BluetoothService.this,PhoneCallHandler.class));
        stopService(new Intent(BluetoothService.this,SMSService.class));
        stopService(new Intent(BluetoothService.this,MyService.class));
        super.onDestroy();

    }

    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }

    public void  passToActivity(String msg)
    {
        Intent intent=new Intent();
        intent.setAction("pass");
        intent.putExtra("message",msg);
        sendBroadcast(intent);

    }

}