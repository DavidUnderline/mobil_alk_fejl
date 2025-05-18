package com.example.allaskereso_portal;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class NotificationHelper {

    private static final String CHANNEL_ID = "my_channel_id";
    private static final String LOG = RegisterActivity.class.getName();

    public static void createNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Log.d(LOG, "--- INSIDE CREATE NOTIFICATION ----");
            CharSequence name = "My Channel";
            String description = "Description of My Channel";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            if (notificationManager != null) {
//                Log.d(LOG, "notification manager created: " + CHANNEL_ID);
                notificationManager.createNotificationChannel(channel);
            }
        }
    }

    public static void showNotification(Context context, String title, String message) {
        Log.v(LOG, "--- INSIDE SHOW NOTIFICATION ----");
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        try {
            notificationManager.notify(123, builder.build());
        } catch (SecurityException e) {
            Log.e(LOG, "SecurityException: " + e.getMessage());
        }
    }


}
