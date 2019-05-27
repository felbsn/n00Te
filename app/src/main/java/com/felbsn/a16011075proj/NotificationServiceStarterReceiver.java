package com.felbsn.a16011075proj;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import static android.support.v4.content.WakefulBroadcastReceiver.startWakefulService;

public final class NotificationServiceStarterReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Intent serviceIntent = null;

        serviceIntent = NotificationIntentService.createIntentStartNotificationService(context);

        if (serviceIntent != null) {
            // Start the service, keeping the device awake while it is launching.

            serviceIntent.putExtra("targetAlarmID" , intent.getIntExtra("targetAlarmID" , 0));

            Log.i(getClass().getSimpleName(), "On Broad cast i received ..... : ");


            startWakefulService(context, serviceIntent);

        }
    }
}