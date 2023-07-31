package com.example.appplaymusic;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class MyApplication extends Application {
    public static final String CHANNEL_ID = "CHANNEL_EXAM";

    @Override
    public void onCreate() {
        super.onCreate();
        creatNoticationChanelId();
    }

    private void creatNoticationChanelId() {
    if(Build.VERSION.SDK_INT>Build.VERSION_CODES.O){
        NotificationChannel notificationChannel=new NotificationChannel(CHANNEL_ID,"My Channel Id", NotificationManager.IMPORTANCE_DEFAULT);
        NotificationManager manager=getSystemService(NotificationManager.class);
        if(manager!=null) manager.createNotificationChannel(notificationChannel);
    }
    }
}
