package com.example.mahmoudheshmat.musicapp;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class NotificationBroadcast extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Constant.NOTIFY_PLAY)) {
            NavigationUtils.playPause_playingsongService(context);
        } else if (intent.getAction().equals(Constant.NOTIFY_PAUSE)) {
            NavigationUtils.playPause_playingsongService(context);
        } else if (intent.getAction().equals(Constant.NOTIFY_NEXT)) {
            NavigationUtils.next_playingsongService(context);
        } else if (intent.getAction().equals(Constant.NOTIFY_PREVIOUS)) {
            NavigationUtils.previous_playingsongService(context);
        } else if (intent.getAction().equals(Constant.NOTIFY_CANCEL)) {
            NavigationUtils.playPause_playingsongService(context);
            NotificationManager notificationManager =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.cancel(1);
        }
    }
}
