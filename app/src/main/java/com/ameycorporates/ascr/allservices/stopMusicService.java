package com.ameycorporates.ascr.allservices;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class stopMusicService extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

            Log.e("broadcast reiver","in action screen on reciever");
        if(intent.getAction().equals(Intent.ACTION_SCREEN_ON))
        {
            context.stopService(new Intent(context,MyService.class));
            Log.e("success","in screen on intent");
            context.unregisterReceiver(this);

        }


    }
}
