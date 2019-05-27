package com.felbsn.a16011075proj;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;


import java.util.Calendar;
import java.util.Date;

import static android.support.v4.content.WakefulBroadcastReceiver.startWakefulService;


public class NotificationEventRecv extends  WakefulBroadcastReceiver{


    private static final String ACTION_START_NOTIFICATION_SERVICE = "ACTION_START_NOTIFICATION_SERVICE";
    private static final String ACTION_DELETE_NOTIFICATION = "ACTION_DELETE_NOTIFICATION";


    public static void setupAlarm(Context context  , int alarmID   , Date date) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        //PendingIntent alarmIntent = getStartPendingIntent(context);

        Intent intent = new Intent(context, NotificationEventRecv.class);
        intent.putExtra("targetAlarmID" ,alarmID );
        intent.putExtra("test" ,"someTest" );
        intent.setAction(ACTION_START_NOTIFICATION_SERVICE);



        PendingIntent alarmIntent = PendingIntent.getBroadcast(context, alarmID, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        alarmManager.setExact( AlarmManager.RTC_WAKEUP, date.getTime() ,alarmIntent );

        /*alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
                getTriggerAt(new Date()),
                (long)(0.3 * AlarmManager.INTERVAL_FIFTEEN_MINUTES),
                alarmIntent);*/
    }

    public static void cancelAlarm(Context context , int AlarmID) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, NotificationEventRecv.class);
        intent.setAction(ACTION_START_NOTIFICATION_SERVICE);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(context, AlarmID, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.cancel(alarmIntent);
    }

    private static long getTriggerAt(Date now) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);
        calendar.add(Calendar.SECOND, 10);
        return calendar.getTimeInMillis();
    }

    private static PendingIntent getStartPendingIntent(Context context) {
        Intent intent = new Intent(context, NotificationEventRecv.class);
        intent.setAction(ACTION_START_NOTIFICATION_SERVICE);
        return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    public static PendingIntent getDeleteIntent(Context context) {
        Intent intent = new Intent(context, NotificationEventRecv.class);
        intent.setAction(ACTION_DELETE_NOTIFICATION);
        return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Intent serviceIntent = null;
        if (ACTION_START_NOTIFICATION_SERVICE.equals(action)) {
            Log.i(getClass().getSimpleName(), "onReceive from alarm, starting notification service");
            serviceIntent = NotificationIntentService.createIntentStartNotificationService(context);
        } else if (ACTION_DELETE_NOTIFICATION.equals(action)) {
             Log.i(getClass().getSimpleName(), "onReceive delete notification action, starting notification service to handle delete");
            serviceIntent = NotificationIntentService.createIntentDeleteNotification(context);
        }

        if (serviceIntent != null) {
            // Start the service, keeping the device awake while it is launching.

            serviceIntent.putExtra("targetAlarmID" , intent.getIntExtra("targetAlarmID" , 0));

            serviceIntent.putExtra("test" ,"Vaavv");


            startWakefulService(context, serviceIntent);

        }
    }


}
