package com.example.appplaymusic.Services;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.appplaymusic.Commons.Common;
import com.example.appplaymusic.MainActivity.MainActivity;
import com.example.appplaymusic.Models.Song;
import com.example.appplaymusic.MyApplication;
import com.example.appplaymusic.R;
import com.squareup.picasso.Picasso;

import java.io.IOException;

public class PlayMusicService extends Service {
    private static final int SONG_LOCAL = 1;
    private MediaPlayer mediaPlayer;
    private static boolean isPlaying;
    Notification notificationShowSongPlay;
    private com.example.appplaymusic.Models.Song mSong=null;

    public  static boolean isPlaying() {
        return isPlaying;
    }

    public void setPlaying(boolean playing) {
        isPlaying = playing;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    private int progress;
    public PlayMusicService() {

    }
    public static final int ACTION_PAUSE=1;
    public static final int ACTION_RESUME=2;
    public static final int ACTION_CLEAR=3;
    public static final int ACTION_START=4;
    public static final int ACTION_SEEKTO=5;
    public static final int ACTION_COMPLETE=6;
    public static final int ACTION_RECONNECT=7;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(intent.getExtras()!=null){
            progress=intent.getIntExtra("progress",-1);
            com.example.appplaymusic.Models.Song song= (com.example.appplaymusic.Models.Song) intent.getExtras().get("song_obj");

            if(song!=null){
                mSong=song;
                startSong(song);
                sendNotification(song);
            }

            int action=intent.getIntExtra("action_music_service",0);
            if(action!=ACTION_SEEKTO&&song!=null){
                if(mSong!=null) {
                    stopMedia();
                    startSong(song);
                }
            }else handleActionMusic(action);
        }

        return START_NOT_STICKY;
    }

    private void startSong(com.example.appplaymusic.Models.Song song) {
    if(mediaPlayer==null) //mediaPlayer=MediaPlayer.create(getApplicationContext(),song.getSongLink());
    {
         mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            if(song.getType()==SONG_LOCAL)mediaPlayer.setDataSource(this,Uri.parse(song.getSongLink()));
            else mediaPlayer.setDataSource(song.getSongLink());
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ; // might take long! (for buffering, etc)
    }
    mediaPlayer.start();
    mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                handleActionMusic(ACTION_COMPLETE);
            }
        });
    isPlaying=true;
    handleActionMusic(ACTION_START);
    }
    //chua cap nhat vi tri hien tai cua runable

    private  void handleActionMusic(int acton){
        switch(acton){
            case  ACTION_CLEAR:
                stopMedia();
                stopSelf();
                stopForeground( true );
                sendAction2Activity(ACTION_CLEAR);
                break;
            case  ACTION_PAUSE:
                pauseSong();
                sendAction2Activity(ACTION_PAUSE);
                break;
            case  ACTION_RESUME:
                resumeSong();
                sendAction2Activity(ACTION_RESUME);
                break;
            case  ACTION_START:
                sendAction2Activity(ACTION_START);
                Log.d("sendAction2Activity","ACTION_START");
                break;
            case  ACTION_RECONNECT:
                Log.d("sendAction2Activity","ACTION_RECONNECT");
                Log.d("sendAction2Activity",getSongName());
                sendAction2Activity(ACTION_RECONNECT);
                break;
            case  ACTION_SEEKTO:
                seekTo(progress);
                sendAction2Activity(ACTION_SEEKTO);
                Log.d("sendAction2Activity","ACTION_SEEKTO");
                break;
            case  ACTION_COMPLETE:
                sendAction2Activity(ACTION_COMPLETE);
                Log.d("sendAction2Activity","ACTION_COMPLETE");
                break;
            default:
                break;
        }
    }

    private void stopMedia() {
        if(mediaPlayer!=null){
            mediaPlayer.release();
            mediaPlayer=null;
        }
    }

    private void seekTo(int progress) {
        this.mediaPlayer.seekTo(progress * 1000);
        this.progress=-1;
    }

    private void sendNotification(Song song) {
        //NotificationManager mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent intent=new Intent(this, MainActivity.class);
        PendingIntent pendingIntent=PendingIntent.getService(this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        final  RemoteViews remoteView=new RemoteViews(getPackageName(),R.layout.layout_song_item_foreground);
        remoteView.setTextViewText(R.id.txt_song_name,song.getSongName());
        remoteView.setTextViewText(R.id.txt_singer_name,song.getSingerName());


        remoteView.setImageViewResource(R.id.icon_play_or_pause,R.drawable.pause);
        if(isPlaying) {
            remoteView.setOnClickPendingIntent(R.id.icon_play_or_pause,getPendingIntent(this,ACTION_PAUSE));
            remoteView.setImageViewResource(R.id.icon_play_or_pause,R.drawable.pause);
        }else{
            remoteView.setOnClickPendingIntent(R.id.icon_play_or_pause,getPendingIntent(this,ACTION_RESUME));
            remoteView.setImageViewResource(R.id.icon_play_or_pause,R.drawable.play);
        }

        remoteView.setOnClickPendingIntent(R.id.icon_close,getPendingIntent(this,ACTION_CLEAR));



         Notification notification= new NotificationCompat.Builder(this, MyApplication.CHANNEL_ID)
                .setSmallIcon(R.drawable.chat)
                .setContentIntent(pendingIntent)
                .setCustomContentView(remoteView)
                .setSound(null)
                .build();
         if(song.getType()!=1){
             Picasso.get()
                     .load(song.getSongImage())
                     .into(remoteView, R.id.img_song, 1, notification);
         }

        startForeground(1,notification);
    }
    private  PendingIntent getPendingIntent(Context context, int action){
        Intent intent2=new Intent(this,PlayMusicService.class);
        intent2.putExtra("action_music_service",action);
        return PendingIntent.getService(context.getApplicationContext(),action,intent2,PendingIntent.FLAG_UPDATE_CURRENT);
    }
    void sendAction2Activity(int action){
        Intent intent=new Intent("send_data_to_activity");
        Bundle bundle=new Bundle();
        bundle.putInt("currentSongIndex",MainActivity.getCurrentIndexSong());
        bundle.putSerializable("song_obj",mSong);
        bundle.putBoolean("is_playing",isPlaying);
        bundle.putInt("action",action);
        if(action==ACTION_START){
            bundle.putInt("max_progress",getMaxProgress());
            bundle.putInt("song_duration",getSongDuration());
        }
        intent.putExtras(bundle);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }
    @Override
    public void onCreate() {
        super.onCreate();
    }
    private void resumeSong() {
        Log.d("resumeSong","resumeSong");

        Log.d("resumeSong",mediaPlayer!=null?"me!=null":"me null");
        Log.d("resumeSong",isPlaying?"pl true":"pl false");
        if(mediaPlayer!=null&&!isPlaying) {
            Log.d("resumeSong"," ok resu");
            mediaPlayer.start();
            isPlaying=true;
            sendNotification(mSong);
        } else {
            Log.d("resumeSong","recreate");
            Song song= Common.getCurrentSong();
            mSong=song;
            startSong(song);
            sendNotification(song);
        }
    }
    private void pauseSong() {
        Log.d("pauseSong","pauseSong");
        if(mediaPlayer!=null&&isPlaying){
            mediaPlayer.pause();
            isPlaying=false;
            sendNotification(mSong);
        }

    }
    @Override
    public void onDestroy() {
        Log.d("ondestroyService","true");
        super.onDestroy();
        if(mediaPlayer!=null){
            mediaPlayer.release();
            mediaPlayer=null;
        }
    }


    private final IBinder mBinder = new LocalBinder();

    @Override
    public IBinder onBind(Intent intent) {
        Log.d("connection","service bind");
        return mBinder;
    }

    public class LocalBinder extends Binder {
        public PlayMusicService getService() {
            // Return this instance of LocalService so clients can call public
            // methods
            return PlayMusicService.this;
        }
    }
    public int getCurrentProgress(){
        if(mediaPlayer==null) return 0;
        return mediaPlayer.getCurrentPosition();
    }
    public int getMaxProgress(){
        if(mediaPlayer==null) return 0;
        return mediaPlayer.getDuration();
    }
    public void setLooping(boolean isLoop){
        mediaPlayer.setLooping(isLoop);
    }
    public  int getSongDuration(){
        if(mediaPlayer==null) return 0;
        return this.mediaPlayer.getDuration();
    }
    public String getSongName(){
        return mSong.getSongName();
    }
    public String getSingerName(){
        return mSong.getSingerName();
    }
}