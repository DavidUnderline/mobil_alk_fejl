package com.example.allaskereso_portal;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class ReminderBroadcastReceiver extends BroadcastReceiver {

    private static final String TAG = "ReminderReceiver";

    private static final String LOG = RegisterActivity.class.getName();

    @Override
    public void onReceive(Context context, Intent intent) {
        String message = intent.getStringExtra("message");
        Log.i(TAG, "Reminder retrieved: " + message);
        showReminderNotification(context, message);
    }

    private void showReminderNotification(Context context, String message) {
        AlarmHelper.createReminderChannel(context);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, AlarmHelper.REMINDER_CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle("Reminder!")
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        try {
            notificationManager.notify(AlarmHelper.REMINDER_REQUEST_CODE, builder.build());
        } catch (SecurityException e) {
            Log.e(LOG, "SecurityException: " + e.getMessage());
        }
    }
}