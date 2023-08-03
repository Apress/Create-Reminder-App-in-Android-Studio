package com.radefffactory.remidersapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        //show notification when the time comes
        NotificationsHandler notificationsHandler = new NotificationsHandler(context);
        notificationsHandler.createNotification(intent.getAction());
    }
}
