package com.example.appplaymusic.MainActivity;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appplaymusic.Adapters.PlaylistCanhanAdapter;
import com.example.appplaymusic.Commons.Common;
import com.example.appplaymusic.Database.Database;
import com.example.appplaymusic.Fragments.CanhanFragment.TrenThietBiActivityView;
import com.example.appplaymusic.Models.Playlist;
import com.example.appplaymusic.Models.Song;
import com.example.appplaymusic.PlaySongActivity;
import com.example.appplaymusic.R;
import com.example.appplaymusic.Services.PlayMusicService;

import java.util.ArrayList;
import java.util.List;

public class MainActivityPresenter {
    MainActivityView view;
    Song song;
    private boolean isPlaying;

    List<Song> songList=new ArrayList<>();

    Context mContext;
    List<Playlist> playlistsLocal;
    BroadcastReceiver broadcastReceiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle=intent.getExtras();
            isPlaying= (Boolean) bundle.getBoolean("is_playing");
            song= (com.example.appplaymusic.Models.Song) bundle.get("song_obj");
            int action = bundle.getInt("action");
            updateSongInfo(isPlaying,song,action);
        }
    };
    public MainActivityPresenter(MainActivityView view) {
        this.view = view;
        mContext= view.getContext();
        LocalBroadcastManager.getInstance(mContext).registerReceiver(broadcastReceiver,new IntentFilter("send_data_to_activity"));
    }


    public void playSong() {
        sendAction2Service(PlayMusicService.ACTION_RESUME);
    }
    public void pauseSong() { sendAction2Service(PlayMusicService.ACTION_PAUSE); }
    public void playNextSong(Song song) {
        startServiceNewSong(song);
    }
    public void sendAction2Service(int action) {
        Intent intent=new Intent(mContext, PlayMusicService.class);
        intent.putExtra("action_music_service",action);
        mContext.startService(intent);
    }
    private void startServiceNewSong(com.example.appplaymusic.Models.Song song){
        this.song=song;
        Intent intent=new Intent(mContext, PlayMusicService.class);
        Bundle bundle=new Bundle();
        bundle.putSerializable("song_obj",song);
        int index=bundle.getInt("currentSongIndex");
        Log.d("current index", String.valueOf(index));
        intent.putExtras(bundle);
        mContext.startService(intent);
    }
    private void updateSongInfo(Boolean isPlaying, com.example.appplaymusic.Models.Song  song, int action) {
        this.isPlaying=isPlaying;
        switch (action){
            case PlayMusicService.ACTION_START:
                view.updateFooterSong(true);
                break;
            case PlayMusicService.ACTION_CLEAR:
                Log.d("updateSongInfo","ACTION_CLEAR ");
                view.clearSong();
                break;
            case PlayMusicService.ACTION_PAUSE:
                if(!isPlaying) view.updateIconPlay(false);
                break;
            case PlayMusicService.ACTION_RESUME:
                if(isPlaying) view.updateIconPlay(true);
                break;
            case PlayMusicService.ACTION_COMPLETE:
                Song targetSong;
                if(PlaySongActivity.isLoop()) {
                    targetSong=Common.getCurrentSong();
                }
                else if(PlaySongActivity.isRandom()) {
                    targetSong= Common.getRandomSong();
                }
                else {
                    targetSong=Common.getNextSong();
                }
                startServiceNewSong(targetSong);
                //updateAllFragmentChild(song);
                break;
            case PlayMusicService.ACTION_RECONNECT:
//                showSongInfo();
//                updateHeaderInfor();
                break;
        }
    }


    @SuppressLint("ResourceType")
    public void showMyPlaylistToChoose() {
        RelativeLayout relativeLayout = new RelativeLayout(mContext);
        RelativeLayout.LayoutParams relativeLayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.FILL_PARENT);
        relativeLayoutParams.height=700;
        relativeLayout.setBackgroundColor(mContext.getResources().getColor(R.color.active_btn_red));
        relativeLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, 1);
        relativeLayout.setLayoutParams(relativeLayoutParams);
        LinearLayout linearLayout= (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.playlist_hori,null,false);
        Button button=new Button(mContext);
        button.setText("Tạo playlist mới");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCreatePlaylist();
            }
        });
        RelativeLayout.LayoutParams layoutParamsBtn=new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        button.setId(1);
        button.setLayoutParams(layoutParamsBtn);
        button.setBackgroundColor(mContext.getResources().getColor(R.color.teal_700));
        RecyclerView recyclerViewPlaylistLocal=new RecyclerView(mContext);
        PlaylistCanhanAdapter playlistCanhanAdapter=new PlaylistCanhanAdapter();
        recyclerViewPlaylistLocal.setAdapter(playlistCanhanAdapter);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(mContext);
        recyclerViewPlaylistLocal.setLayoutManager(linearLayoutManager);
        playlistsLocal= Database.getInstance(mContext).playlistDAO().getListPlaylist();
        playlistCanhanAdapter.setData(playlistsLocal, mContext, new PlaylistCanhanAdapter.IonClick() {
            @Override
            public void onClick(String id,Playlist playlist) {
                Toast.makeText(mContext, ""+id, Toast.LENGTH_SHORT).show();
            }
        });
        RelativeLayout.LayoutParams recyclerviewParam=new RelativeLayout.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT);
        recyclerviewParam.addRule(RelativeLayout.BELOW,1);
        recyclerViewPlaylistLocal.setLayoutParams(recyclerviewParam);

        relativeLayout.addView(button);
        relativeLayout.addView(recyclerViewPlaylistLocal);
        //view.addView(relativeLayout);
    }

    private void showCreatePlaylist() {
        view.goToCreatePlaylist();
    }
}
