package scionoftech.socketservice.Message;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;

import scionoftech.socketservice.Db.DataHandler;
import scionoftech.socketservice.Activity.Message;
import scionoftech.socketservice.R;

/**
 * Created by sky on 9/2/16.
 */
public class NotificationHandler extends BroadcastReceiver {


    Context context;
    DataHandler handler;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        handler = new DataHandler(context);
        String message = (intent.getStringExtra("message") == null) ? "failed" : intent.getStringExtra("message");
        Log.d("message", message);
        if (!message.equals("failed")) {
            createNotification(message);
        }
    }

    private void createNotification(String message) {
        int numMessagesOne = Integer.parseInt(GenerateRandomNumber(3));
        // Invoking the default notification service

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);

        mBuilder.setContentTitle("New message");
        mBuilder.setContentText(message);
        mBuilder.setTicker("New message from Server");
        mBuilder.setSmallIcon(R.mipmap.ic_launcher);
        mBuilder.setWhen(System.currentTimeMillis());
        mBuilder.setAutoCancel(true);
        // Increase notification number every time a new notification arrives
        // mBuilder.setNumber(numMessagesOne);
        mBuilder.setPriority(Notification.PRIORITY_DEFAULT);

        handler.open();
        JSONObject settings = new JSONObject();

        Cursor cursor = handler.ReturnNotifications();
        try {
            if (cursor.moveToFirst()) {
                settings = new JSONObject(cursor.getString(1));
            }
            handler.close();
            boolean nstatus = false;
            if (settings.get("status").toString().equals("on")) {
                nstatus = true;
                if (settings.get("tone").toString().equals("on")) {
                    Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                    mBuilder.setSound(sound);
                }
                if (settings.get("vibrate").toString().equals("on")) {
                    //Vibration
                    mBuilder.setVibrate(new long[]{1000, 1000});
                }
                if (settings.get("light").toString().equals("on")) {
                    //LED
                    mBuilder.setLights(Color.BLUE, 3000, 3000);
                }
            }


            Intent resultIntent = new Intent(context, Message.class);
            resultIntent.putExtra("message", message);
            resultIntent.putExtra("notificationId", numMessagesOne);

            PendingIntent contentintent = PendingIntent.getActivity(context, numMessagesOne, resultIntent, 0);
            // start the activity when the user clicks the notification text
            mBuilder.setContentIntent(contentintent);
            // mBuilder.setFullScreenIntent(contentintent,true);
            NotificationManager myNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            if (nstatus) {
                myNotificationManager.notify(numMessagesOne, mBuilder.build());
            }

        /* //This ensures that navigating backward from the Activity leads out of the app to Home page
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
            // Adds the back stack for the Intent
           // stackBuilder.addParentStack(aClass);
            // Adds the Intent that starts the Activity to the top of the stack
            stackBuilder.addNextIntent(resultIntent);
            PendingIntent resultPendingIntent =
                    stackBuilder.getPendingIntent(
                            0,
                            PendingIntent.FLAG_ONE_SHOT //can only be used once
                    );*/
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    String GenerateRandomNumber(int charLength) {
        return String.valueOf(charLength < 1 ? 0 : new Random()
                .nextInt((9 * (int) Math.pow(10, charLength - 1)) - 1)
                + (int) Math.pow(10, charLength - 1));
    }
}
