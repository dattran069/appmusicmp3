package com.example.appplaymusic;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSeekBar;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.appplaymusic.Adapters.PlaySongPagerAdapter;
import com.example.appplaymusic.Adapters.PlaylistCanhanAdapter;
import com.example.appplaymusic.Commons.Common;
import com.example.appplaymusic.Fragments.CanhanFragment.TrenThietBiActivity;
import com.example.appplaymusic.Fragments.PlaySongLyricFragment;
import com.example.appplaymusic.Fragments.PlaySongMainFragment;
import com.example.appplaymusic.Fragments.PlaySongPlaylistFragment;
import com.example.appplaymusic.Fragments.PlaylistFragment.PlaylistFragment;
import com.example.appplaymusic.Helper.HelpTools;
import com.example.appplaymusic.MainActivity.MainActivity;
import com.example.appplaymusic.Models.Playlist;
import com.example.appplaymusic.Models.Song;
import com.example.appplaymusic.Services.APIService;
import com.example.appplaymusic.Services.PlayMusicService;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import me.relex.circleindicator.CircleIndicator3;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlaySongActivity extends AppCompatActivity {
    private  boolean isRuningSeekbar=false;
    public static boolean Playing=false;
    private static final int RANDOM_TYPE = 1;
    private static final int LOOP_TYPE = 2;
    static boolean firstSong=true;
    View view;
    static com.example.appplaymusic.Models.Song song=null;
    static SeekBar textThumbSeekBar;
    Boolean isPlaying=true;
    static PlayMusicService mService;
    boolean isServiceConnected = false;
    ImageView icon_more;
    ServiceConnection serviceConnection=new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            Log.d("connectionMP3","onServiceConnected");
            PlayMusicService.LocalBinder localBinder= (PlayMusicService.LocalBinder) iBinder;
            mService=localBinder.getService();
            check_update_seekbar();
            isServiceConnected=true;
            updateHeaderInfor();
            firstSong=false;
            phat();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            Log.d("connection","onServiceDisconnected");
            mService=null;
            isServiceConnected=false;
        }
    };
    ImageView btnPlayOrPause,btnLeft,btnRight,btnLoop,btnRandom;



    public static com.example.appplaymusic.Models.Song getSong() {
    return song;
    }

    public static boolean isLoop() {
    return loop;}

    public static boolean isRandom() {
    return random;}

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("PlaySongActivity","onResume");
    }

    @Override
    protected void onStart() {
        super.onStart();

        Log.d("PlaySongActivity","onStart");
    }

    @Override
    protected void onDestroy() {
        if(!isPlaying) mhandler.removeCallbacks(runnable);
        //stop_service();
        super.onDestroy();
        Log.d("PlaySongActivity","onDestroy");
    }

    PopupWindow popupWindow=null;
    PopupWindow popupWindowPL=null;
    public void setSong(com.example.appplaymusic.Models.Song song ) {
        this.song = song;
    }
    ImageView icon_left;
    static TextView txtCurrentTime;
    static TextView txtTotalTime;
    TextView txt_song_name; TextView txt_singer_name;
    static Handler mhandler=new Handler();
    static MediaPlayer player;
    static boolean random=false;
    static boolean loop=false;
    LinearLayout linearBottom;
    ViewPager2 viewPager2;
    CircleIndicator3 circleIndicator3;
    PlaySongPagerAdapter playSongPagerAdapter;
    public static void stopSong() {
        if(player!=null){
            player.reset();
        }
    }
    BroadcastReceiver broadcastReceiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle=intent.getExtras();
            isPlaying= (Boolean) bundle.getBoolean("is_playing");
            song= (com.example.appplaymusic.Models.Song) bundle.get("song_obj");
            int action = bundle.getInt("action");
            if(action==PlayMusicService.ACTION_START) {
                int max=bundle.getInt("max_progress");
                int songDuration=bundle.getInt("song_duration");
                updateTimeTextAndSeekbar(max,songDuration);
            }
            updateSongInfo(isPlaying,song,action);
        }
    };
    private void start_service(com.example.appplaymusic.Models.Song song ) {

        Log.d("start_service","true");
        Intent intent=new Intent(this, PlayMusicService.class);
        Bundle bundle=new Bundle();
        bundle.putSerializable("song_obj",song);
        intent.putExtras(bundle);
        startService(intent);
        bindService(intent,serviceConnection,BIND_AUTO_CREATE);
    }
    private void bind_Service(){
        Log.d("connection","service running, re bind");
        Intent intent=new Intent(this, PlayMusicService.class);
        startService(intent);
        bindService(intent,serviceConnection,BIND_AUTO_CREATE);
    }
    private void stop_service() {
        Intent intent=new Intent(this,PlayMusicService.class);
        stopService(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("PlaySongActivity","onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_song);
        view=findViewById(R.id.view_playsong);
        isPlaying=PlayMusicService.isPlaying();
        linearBottom=findViewById(R.id.linearBottom);
        textThumbSeekBar=findViewById(R.id.TextThumbSeekBar);
        txtCurrentTime=findViewById(R.id.txt_current_time);
        txtTotalTime=findViewById(R.id.txt_total_time);
        viewPager2=findViewById(R.id.ViewPager2PlaySong);
        circleIndicator3=findViewById(R.id.CircleIndicator_playsong);
        btnPlayOrPause=findViewById(R.id.icon_play_or_pause);
        btnLeft=findViewById(R.id.icon_left_song);
        btnRight=findViewById(R.id.icon_right);
        btnLoop=findViewById(R.id.icon_loop);
        icon_left=findViewById(R.id.icon_left);
        btnRandom=findViewById(R.id.icon_random);
        txt_song_name=findViewById(R.id.txt_song_name);
        txt_singer_name=findViewById(R.id.txt_singer_name);
        playSongPagerAdapter=new PlaySongPagerAdapter(this);
        playSongPagerAdapter.setContext(this);
        icon_more=findViewById(R.id.icon_more);
        viewPager2.setAdapter(playSongPagerAdapter);
        circleIndicator3.setViewPager(viewPager2);
        playSongPagerAdapter.registerAdapterDataObserver(circleIndicator3.getAdapterDataObserver());
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver,new IntentFilter("send_data_to_activity"));

        icon_left.setOnClickListener(view -> {
            Playing=isPlaying;
            onBackPressed();
        });
        icon_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(popupWindow!=null&&popupWindow.isShowing()) popupWindow.dismiss();
                //iClickListener.onClickMoreListener(songViewHolder.btnMore,song.getIdSong());
                LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View customView = layoutInflater.inflate(R.layout.layout_song_option,null);

                ImageView btnClose=customView.findViewById(R.id.imgClose);
                ImageView songAvar=customView.findViewById(R.id.imgSongAvar);
                TextView txtSongName=customView.findViewById(R.id.txt_song_name);
                TextView txtSingerName=customView.findViewById(R.id.txt_singer_name);
                Glide.with(getApplicationContext()).load(song.getSongImage()).into(songAvar);
                txtSongName.setText(song.getSongName());
                txtSingerName.setText(song.getSingerName());
                LinearLayout li_addToPlaylist=customView.findViewById(R.id.li_addToPlaylist);
                LinearLayout li_addToListPlay=customView.findViewById(R.id.li_addToListPlay);
                LinearLayout li_removeFromThisPlaylist=customView.findViewById(R.id.li_removeFromThisPlaylist);
                //if(inLocalPlaylist) li_removeFromThisPlaylist.setVisibility(View.VISIBLE);
                li_addToPlaylist.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(popupWindow!=null&&popupWindow.isShowing()) popupWindow.dismiss();
                        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        View customView = layoutInflater.inflate(R.layout.layout_playlist_ca_nhan,null);
                        ImageView btnClose=customView.findViewById(R.id.imgClose);
                        btnClose.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                popupWindowPL.dismiss();
                                if(popupWindow!=null&&popupWindow.isShowing()) popupWindow.dismiss();
                            }
                        });
                        RecyclerView recyclerViewPL=customView.findViewById(R.id.RecyclerViewPlaylist);
                        PlaylistCanhanAdapter playlistCanhanAdapter=new PlaylistCanhanAdapter();
                        recyclerViewPL.setAdapter(playlistCanhanAdapter);
                        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getApplicationContext());
                        recyclerViewPL.setLayoutManager(linearLayoutManager);
                        List<com.example.appplaymusic.Models.Playlist> playlistsLocal=new ArrayList<>();
                        //playlistsLocal= Database.getInstance(mContext).playlistDAO().getListPlaylist();
                        int userId=Common.getUserId(getApplicationContext());
                        if(userId!=-1){
                            Call<List<com.example.appplaymusic.Models.Playlist>> callback= APIService.getService().getPlaylistsLocalByUserId(userId);
                            try {
                                Response<List<com.example.appplaymusic.Models.Playlist>> response=callback.execute();
                                playlistsLocal=response.body();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                        playlistCanhanAdapter.setData(playlistsLocal, getApplicationContext(), new PlaylistCanhanAdapter.IonClick() {
                            @Override
                            public void onClick(String idPlaylist, Playlist playlist) {
                                addSongToPlaylist(song,idPlaylist,song.getIdSong(),song.getType());
                            }
                        });

                        popupWindowPL = new PopupWindow(customView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        popupWindowPL.setAnimationStyle(R.style.popup_window_animation);
                        popupWindowPL.showAtLocation(view, Gravity.BOTTOM, 0, 0);
                    }
                });
                li_addToListPlay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(getApplicationContext(), "Add to lp", Toast.LENGTH_SHORT).show();
                    }
                });
                popupWindow = new PopupWindow(customView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                popupWindow.setAnimationStyle(R.style.popup_window_animation);
//                    popupWindow.setBackgroundDrawable(new BitmapDraw able(mContext.getResources(),
//                            ""));
                popupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
//                    popupWindow.setOutsideTouchable(true);
                btnClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        popupWindow.dismiss();
                    }
                });
            }
        });
        RelativeLayout relativeLayout=linearBottom.findViewById(R.id.rll_heart_playlist);
        LinearLayout recyclerViewComment=linearBottom.findViewById(R.id.linear_comment);
        viewPager2.setCurrentItem(1);
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                // android:animateLayoutChanges="true"
                switch (position){
                    case 0:
                    case 2:
                       relativeLayout.setVisibility(View.GONE);
                       recyclerViewComment.setVisibility(View.GONE);
                        break;
                    case 1:
                        relativeLayout.setVisibility(View.VISIBLE);
                        recyclerViewComment.setVisibility(View.VISIBLE);
                        break;
                }
                super.onPageSelected(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }
        });



        if(getIntent().getExtras()!=null){
            Log.d("extraData","has");
            song= (com.example.appplaymusic.Models.Song) getIntent().getExtras().get("song_obj");
            boolean isMiniSongBottom=false;
            if(getIntent().getExtras().get("miniSongBottom")!=null) {
                Log.d("extraData","has miniSongBottom");
                isMiniSongBottom = (boolean) getIntent().getExtras().get("miniSongBottom");
            }
            if(isMiniSongBottom==false) start_service(song);
            updateHeaderInfor();
        }
        else
        {
            if(HelpTools.isMyServiceRunning(PlayMusicService.class,getApplicationContext())){
                //sendAction2Service(PlayMusicService.ACTION_RECONNECT);
                Intent intent=new Intent(this, PlayMusicService.class);
                intent.putExtra("action_music_service",PlayMusicService.ACTION_RECONNECT);
                startService(intent);
                bindService(intent,serviceConnection,BIND_AUTO_CREATE);
                Log.d("PlaySongActivity","service is playing");
            } else  Log.d("PlaySongActivity","service isn't playing");
        }

        btnPlayOrPause.setOnClickListener(view -> {
            if(isPlaying) {
                sendAction2Service(PlayMusicService.ACTION_PAUSE);
            }
            else {
                sendAction2Service(PlayMusicService.ACTION_RESUME);
            }
        });
        btnLeft.setOnClickListener(view -> {
            Song song2;
            song2=Common.getPreSong();
            startServiceNewSong(song2);
        });
        btnRight.setOnClickListener(view ->
            {
                Song song2;
                song2=Common.getNextSong();
                startServiceNewSong(song2);
                //updateAllFragmentChild(song2);
            });
        btnLoop.setOnClickListener(view ->
            {
               loop=!loop;
               if(mService!=null) mService.setLooping(loop);
               setIconState(LOOP_TYPE);
            });
        setIconState(RANDOM_TYPE);
        setIconState(LOOP_TYPE);
        btnRandom.setOnClickListener(view -> {
            random=!random;
            setIconState(RANDOM_TYPE);
        });
        if(isPlaying) btnPlayOrPause.setImageResource(R.drawable.pause);
        else btnPlayOrPause.setImageResource(R.drawable.play);
        //if(HelpTools.isMyServiceRunning(PlayMusicService.class,this)) bind_Service();
        bind_Service();

    }
    private void sendAction2Service(int action) {
        Intent intent=new Intent(this,PlayMusicService.class);
        intent.putExtra("action_music_service",action);
        intent.putExtra("progress",textThumbSeekBar.getProgress());
        startService(intent);
    }
    private void addSongToPlaylist(Song song,String idPlaylist,String idSong,int type) {
        //PlaylistSong playlistSong=new PlaylistSong(idSong,idPlaylist);
        ////Database.getInstance(mContext).playlistSongDAO().insertPlaylistSong(playlistSong);
        //co le phai tao playlistSong because playlist song n-n
        Call<Integer> call =APIService.getService().createPlaylistSong(Integer.parseInt(idPlaylist),Integer.parseInt(idSong),type);
        call.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                int id=response.body();
                if(id!=-1) {
                    Toast.makeText(getApplicationContext(), "Thêm vào playlist thành công!", Toast.LENGTH_SHORT).show();
                    downloadMp3FromInternet(song);
                }
                else Toast.makeText(getApplicationContext(), "Lỗi, không thêm được!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Lỗi, "+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
    private void downloadMp3FromInternet(Song song) {
        HelpTools.DownloadingTask dlT=new HelpTools.DownloadingTask(getApplicationContext());
        dlT.setSong(song);
        dlT.execute();
    }
    @Override
    protected void onStop() {
        super.onStop();
        // Unbind from the service
        if (isServiceConnected) {
            unbindService(serviceConnection);
            isServiceConnected = false;
        }
    }

    private void updateSongInfo(Boolean isPlaying, com.example.appplaymusic.Models.Song  rsong, int action) {
        this.isPlaying=isPlaying;
        switch (action){
            case PlayMusicService.ACTION_START:
                Log.d("conntection","ACTION_START");
                showSongInfo();
                updateHeaderInfor();
                break;
            case PlayMusicService.ACTION_CLEAR:
                Log.d("updateSongInfo","ACTION_CLEAR ");
                if(this.isPlaying) {
                    btnPlayOrPause.setImageResource(R.drawable.play);
                    this.isPlaying=false;
                }

                break;
            case PlayMusicService.ACTION_PAUSE:
                if(!isPlaying) btnPlayOrPause.setImageResource(R.drawable.play);
                break;
            case PlayMusicService.ACTION_RESUME:
                if(isPlaying) btnPlayOrPause.setImageResource(R.drawable.pause);
                break;
            case PlayMusicService.ACTION_COMPLETE:
                if(loop) return;
                else if(random) {
                    startServiceNewSong(Common.getRandomSong());
                }
                else {
                    song= Common.getNextSong();
                    //updateAllFragmentChild(song);
                    startServiceNewSong(song);
                }
                break;
            case PlayMusicService.ACTION_RECONNECT:
                showSongInfo();
                updateHeaderInfor();
                break;
        }
    }

    private void startServiceNewSong(com.example.appplaymusic.Models.Song song){
        PlaySongActivity.song=song;
        Intent intent=new Intent(this, PlayMusicService.class);
        Bundle bundle=new Bundle();
        bundle.putSerializable("song_obj",song);
        intent.putExtras(bundle);
        startService(intent);
    }
    private void updateTimeTextAndSeekbar(int maxProgress,int songDuration){
        textThumbSeekBar.setMax(maxProgress/1000);
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("mm:ss");
        txtTotalTime.setText(simpleDateFormat.format(songDuration));
    }
    private void check_update_seekbar(){
        if(HelpTools.isMyServiceRunning(PlayMusicService.class,this)&&mService!=null) updateTimeTextAndSeekbar(mService.getMaxProgress(),mService.getSongDuration());
        else{
            Log.d("conntection",isServiceConnected==false?"connected false":"connected true");
            Log.d("conntection",mService==null?"service null":"service not null");
        }
    }
    private void showSongInfo() {
        txt_song_name.setText(song.getSongName());
        txt_singer_name.setText(song.getSingerName());
        check_update_seekbar();
        if(isPlaying) btnPlayOrPause.setImageResource(R.drawable.pause);
        else btnPlayOrPause.setImageResource(R.drawable.play);


        btnPlayOrPause.setOnClickListener(view -> {
            if(isPlaying) {
                sendAction2Service(PlayMusicService.ACTION_PAUSE);
            }
            else {
                sendAction2Service(PlayMusicService.ACTION_RESUME);
            }
        });
//        btn_clear.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                sendAction2Service(MyService.ACTION_CLEAR);
//            }
//        });
    }


    private void setIconState(int type) {
        switch (type){
            case RANDOM_TYPE:
                if(random)
                btnRandom.setColorFilter(getResources().getColor(R.color.active_btn_pink));
                else btnRandom.clearColorFilter();
                break;
            case LOOP_TYPE:
                if(loop)btnLoop.setColorFilter(getResources().getColor(R.color.active_btn_pink));
                else btnLoop.clearColorFilter();
                break;
        }
    }

    private void updateHeaderInfor() {
        if(song!=null){
            txt_song_name.setText(song.getSongName());
            txt_singer_name.setText(song.getSingerName());
        } else{
            if(isServiceConnected&&mService!=null){
                txt_song_name.setText(mService.getSongName());
                txt_singer_name.setText(mService.getSingerName());
            }
        }
    }
    //Tao unable chay seekbar
    Runnable runnable=new Runnable() {
        @Override
        public void run() {
            if(mService!=null){
            int mCurrentPosition =  mService.getCurrentProgress()/ 1000;
            textThumbSeekBar.setProgress(mCurrentPosition);
            SimpleDateFormat simpleDateFormat=new SimpleDateFormat("mm:ss");
            txtCurrentTime.setText(simpleDateFormat.format(mCurrentPosition*1000));
            Log.d("runnable", String.valueOf(mCurrentPosition));
            }
            else{
                Log.d("runnable","null");
            }
            mhandler.postDelayed(this, 1000);
        }
    };



    private void phat() {
        PlaySongActivity.this.runOnUiThread(runnable);
        textThumbSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser){
                    sendAction2Service(PlayMusicService.ACTION_SEEKTO);
                }
            }
        });
    }


}