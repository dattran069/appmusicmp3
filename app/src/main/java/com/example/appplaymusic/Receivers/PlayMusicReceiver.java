package com.example.appplaymusic.Receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.appplaymusic.Services.PlayMusicService;

public class PlayMusicReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
           int action =intent.getIntExtra("action_music",0);
            Log.d("MyReceiver action_music", String.valueOf(action));
           if(action!=0){
               Intent intent1=new Intent(context, PlayMusicService.class);
               intent1.putExtra("action_music_service",action);
               context.startService(intent1);
           }

    }
}
