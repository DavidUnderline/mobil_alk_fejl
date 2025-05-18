package com.example.allaskereso_portal;
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class AlarmHelper {

    private static final String TAG = "AlarmHelper";
    public static final String REMINDER_CHANNEL_ID = "reminder_channel";

    public static final int REMINDER_REQUEST_CODE = 100;

    public static void createReminderChannel(Context context) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            CharSequence name = "Reminder";
            String description = "Message for reminder";
            android.app.NotificationChannel channel = new android.app.NotificationChannel(REMINDER_CHANNEL_ID, name, NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(description);
            android.app.NotificationManager notificationManager = context.getSystemService(android.app.NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
    }

    public static void setReminderAlarm(Context context, long delayMillis, String message) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, ReminderBroadcastReceiver.class);
        intent.putExtra("message", message);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(context, REMINDER_REQUEST_CODE, intent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);

        long triggerAtMillis = SystemClock.elapsedRealtime() + delayMillis;

        alarmManager.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtMillis, alarmIntent);

        Log.i(TAG, "Reminder set " + (delayMillis / 1000) + " " + message);
    }

    public static void cancelReminderAlarm(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, ReminderBroadcastReceiver.class);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(context, REMINDER_REQUEST_CODE, intent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.cancel(alarmIntent);
        Log.i(TAG, "Reminder removed!.");
    }
}