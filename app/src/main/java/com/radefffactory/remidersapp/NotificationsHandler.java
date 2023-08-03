package com.radefffactory.remidersapp;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class NotificationsHandler {

    private final Context context;

    public NotificationsHandler(Context context) {
        this.context = context;
    }

    public void createNotification(String text) {
        String channelId = "channel_reminders";

        //create notification channel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager notificationManager =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            CharSequence channelName = "Reminders channel";
            String channelDescription = "Notification channel for the reminders";
            int channelImportance = NotificationManager.IMPORTANCE_HIGH; // sound and popup
            NotificationChannel notificationChannel = new NotificationChannel(channelId, channelName, channelImportance);
            notificationChannel.setDescription(channelDescription);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(notificationChannel);
            }
        }

        //show notification
        NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle();
        bigTextStyle.bigText(text);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.drawable.ic_notif)
                .setContentTitle("Reminder")
                .setContentText(text)
                .setStyle(bigTextStyle);
        notificationBuilder.setDefaults(Notification.DEFAULT_SOUND);
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        notificationManagerCompat.notify(1000, notificationBuilder.build());
    }
}
