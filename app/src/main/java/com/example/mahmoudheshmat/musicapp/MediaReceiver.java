package com.example.mahmoudheshmat.musicapp;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.util.Log;
import android.view.KeyEvent;

public class MediaReceiver  extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        final String intentAction = intent.getAction();
        if (AudioManager.ACTION_AUDIO_BECOMING_NOISY.equals(intentAction)) {
            NavigationUtils.head_playingsongService(context);
        }
        if (Intent.ACTION_MEDIA_BUTTON.equals(intentAction)){
            final KeyEvent event = intent.getParcelableExtra(Intent.EXTRA_KEY_EVENT);
            if (event == null) {
                return;
            }

            final int keycode = event.getKeyCode();

            switch (keycode) {
                case KeyEvent.KEYCODE_MEDIA_STOP:
                    Log.d("response", "KEYCODE_MEDIA_STOP");
                    break;
                case KeyEvent.KEYCODE_HEADSETHOOK:
                case KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE:
                    Log.d("response", "KEYCODE_MEDIA_PLAY_PAUSE");
                    break;
                case KeyEvent.KEYCODE_MEDIA_NEXT:
                    Log.d("response", "KEYCODE_MEDIA_NEXT");
                    break;
                case KeyEvent.KEYCODE_MEDIA_PREVIOUS:
                    Log.d("response", "KEYCODE_MEDIA_PREVIOUS");
                    break;
                case KeyEvent.KEYCODE_MEDIA_PAUSE:
                    Log.d("response", "KEYCODE_MEDIA_PAUSE");
                    break;
                case KeyEvent.KEYCODE_MEDIA_PLAY:
                    Log.d("response", "KEYCODE_MEDIA_PLAY");
                    break;
            }
        }
    }
}
