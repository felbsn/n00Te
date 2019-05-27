package com.felbsn.a16011075proj;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

import java.util.Date;


/**
 * Created by klogi !!! extended by fatih
 *
 *
 */
public class NotificationIntentService extends IntentService {

    private static final int NOTIFICATION_ID = 1;
    private static final String ACTION_START = "ACTION_START";
    private static final String ACTION_DELETE = "ACTION_DELETE";

    public NotificationIntentService() {
        super(NotificationIntentService.class.getSimpleName());
    }

    public static Intent createIntentStartNotificationService(Context context) {
        Intent intent = new Intent(context, NotificationIntentService.class);
        intent.setAction(ACTION_START);
        return intent;
    }

    public static Intent createIntentDeleteNotification(Context context) {
        Intent intent = new Intent(context, NotificationIntentService.class);
        intent.setAction(ACTION_DELETE);
        return intent;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(getClass().getSimpleName(), "onHandleIntent, started handling a notification event");
        try {
            String action = intent.getAction();
            if (ACTION_START.equals(action)) {

                int targetID =  intent.getIntExtra("targetAlarmID" , 0);



                processStartNotification(targetID);
            }
        } finally {
            WakefulBroadcastReceiver.completeWakefulIntent(intent);
        }
    }

    private void processDeleteNotification(Intent intent) {
        Log.i(getClass().getSimpleName(), "processDeleteNotification  some delete occurs");
    }

    private void processStartNotification(int targetID) {
        // Do something. For example, fetch fresh data from backend to create a rich notification?

         int noteID =targetID & 0xFF;
         int reminderID = targetID >> 16;

         NoteHandler handler = NoteHandler.getInstance(this);

         NoteItem item = handler.getNoteByID(noteID);


         String title ="Vakit Geldi :" +noteID + " xID" +(targetID & 0xFF)  ;

        String Message ="Invalid Note...";

         if(item != null)
         {

             title = item.getLine1();

             Message = item.mNoteText;

         }


        final NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setContentTitle(title)
                .setAutoCancel(true)
                .setColor(getResources().getColor(R.color.colorAccent))
                .setContentText(Message)
                .setSmallIcon(R.drawable.ficon);

        Intent mainIntent = new Intent(this, NoteView.class);
        mainIntent.putExtra("targetNoteID" , noteID);


        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                NOTIFICATION_ID,
                mainIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);
        builder.setDeleteIntent(NotificationEventRecv.getDeleteIntent(this));

        final NotificationManager manager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(NOTIFICATION_ID, builder.build());
    }
}
