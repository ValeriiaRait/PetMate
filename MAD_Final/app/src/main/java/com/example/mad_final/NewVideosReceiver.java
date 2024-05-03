package com.example.mad_final;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class NewVideosReceiver extends BroadcastReceiver {
    public static final String ACTION_NEW_VIDEOS_AVAILABLE = "com.example.mad_final.NEW_VIDEOS_AVAILABLE";


    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action.equals(ACTION_NEW_VIDEOS_AVAILABLE)) {
            // Show a notification or update UI to indicate new videos
            Toast.makeText(context, "New videos are available!", Toast.LENGTH_SHORT).show();
        }
    }



}