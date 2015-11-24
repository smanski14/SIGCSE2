package com.example.owen.sigcsevolunteer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

//This class is called from TaskActivity by an intent related to notifications
public class MyReceiver extends BroadcastReceiver
{

    @Override
    public void onReceive(Context context, Intent intent)
    {
        //MyAlarmService is called here to start a new intent, which is then called to start a service, and finally
        //passed to Utils.java
        Intent service1 = new Intent(context, MyAlarmService.class);
        context.startService(service1);
        Log.i("App", "called receiver method");
        try{
            Utils.generateNotification(context);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

}
