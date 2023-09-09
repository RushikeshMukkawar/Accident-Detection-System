package com.ameycorporates.ascr.allservices;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.telephony.SmsManager;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.location.LocationListenerCompat;

import com.ameycorporates.ascr.database.DatabaseHandler;
import com.ameycorporates.ascr.database.LocalDatabase;

import java.util.List;

public class SMSService extends Service {
    public SMSService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

    String msg;
        Location location=getlastLocation();
        if(location==null)
        {
                msg="Accident Detected ..!!";
        }
        else
        {
            msg="Accident Detected \n Near by Location : https://maps.google.com/maps?q="+location.getLatitude()+","+location.getLongitude();
        }

        DatabaseHandler db=new DatabaseHandler();
        Cursor cr=db.getData("select * from contact_master;");


        Log.e("Message","In Message sending code");
        SmsManager sms=SmsManager.getDefault();
        while(cr.moveToNext())
        {
            sms.sendTextMessage(cr.getString(2).toString(),null,msg,null,null);

        }

        return START_STICKY;
    }

    private Location getlastLocation()
    {

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        List<String> providers=locationManager.getAllProviders();

        Location best=null;

        for(String provider : providers)
        {
            Location l=locationManager.getLastKnownLocation(provider);
            if(l==null)
            {
                continue;
            }
            else
            {
                if(best == null  || l.getAccuracy() <best.getAccuracy())
                {
                    best=l;
                }
            }
        }
    return best;
    }


    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}