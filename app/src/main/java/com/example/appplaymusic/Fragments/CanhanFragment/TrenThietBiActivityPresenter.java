package com.example.appplaymusic.Fragments.CanhanFragment;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appplaymusic.Adapters.PlaylistCanhanAdapter;
import com.example.appplaymusic.Database.Database;
import com.example.appplaymusic.Helper.HelpTools;
import com.example.appplaymusic.Models.Playlist;
import com.example.appplaymusic.Models.PlaylistSong;
import com.example.appplaymusic.Models.Song;
import com.example.appplaymusic.R;
import com.example.appplaymusic.Services.PlayMusicService;

import java.util.ArrayList;
import java.util.List;

public class TrenThietBiActivityPresenter {
    TrenThietBiActivityView view;
    Context context;
    Song song;
    public static final int RUNTIME_PERMISSION_CODE = 7;
    private boolean isPlaying;

    List<Song> songList=new ArrayList<>();

    ContentResolver contentResolver;

    Cursor cursor;

    Uri uri;

    Context mContext;
    List<Playlist> playlistsLocal;
    Button button;
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
    public TrenThietBiActivityPresenter(TrenThietBiActivityView view) {
        this.view = view;
        mContext= view.getContext();
        LocalBroadcastManager.getInstance(mContext).registerReceiver(broadcastReceiver,new IntentFilter("send_data_to_activity"));
    }

    public void setAdapterData() {
        GetAllMediaMp3Files();
    }
    public void GetAllMediaMp3Files(){
        //view.setAdapterData(HelpTools.GetMediaMp3FilesByIdPlaylist(mContext,null,true));
    }

    public void playSong() {
        sendAction2Service(PlayMusicService.ACTION_RESUME);
    }
    public void pauseSong() { sendAction2Service(PlayMusicService.ACTION_PAUSE); }
    public void playNextSong(Song song) {
        startServiceNewSong(song);
    }
    private void sendAction2Service(int action) {
        Intent intent=new Intent(mContext, PlayMusicService.class);
        intent.putExtra("action_music_service",action);
        mContext.startService(intent);
    }
    private void startServiceNewSong(com.example.appplaymusic.Models.Song song){
        this.song=song;
        Intent intent=new Intent(mContext, PlayMusicService.class);
        Bundle bundle=new Bundle();
        bundle.putSerializable("song_obj",song);
        intent.putExtras(bundle);
        mContext.startService(intent);
    }
    private void updateSongInfo(Boolean isPlaying, com.example.appplaymusic.Models.Song  song, int action) {
        this.isPlaying=isPlaying;
        switch (action){
            case PlayMusicService.ACTION_START:
                view.updateFooterSong();
                break;
            case PlayMusicService.ACTION_CLEAR:
                Log.d("updateSongInfo","ACTION_CLEAR ");
                break;
            case PlayMusicService.ACTION_PAUSE:
                if(!isPlaying) view.updateIconPlay(true);
                break;
            case PlayMusicService.ACTION_RESUME:
                if(isPlaying) view.updateIconPlay(false);
                break;
            case PlayMusicService.ACTION_COMPLETE:
//                if(loop) return;
//                else if(random) {
//                    startServiceNewSong(ActivityPlaylist.getRandomSong());
//                }
//                else {
//                    song=ActivityPlaylist.getNextSong();
//                    updateAllFragmentChild(song);
//                    startServiceNewSong(song);
//                }
                break;
            case PlayMusicService.ACTION_RECONNECT:
//                showSongInfo();
//                updateHeaderInfor();
                break;
        }
    }


    @SuppressLint("ResourceType")
    public void showMyPlaylistToChoose(String idSongg) {
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
            public void onClick(String idPlaylist,Playlist playlist) {
                addSongToPlaylist(idPlaylist,idSongg);
            }


        });
        RelativeLayout.LayoutParams recyclerviewParam=new RelativeLayout.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT);
        recyclerviewParam.addRule(RelativeLayout.BELOW,1);
        recyclerViewPlaylistLocal.setLayoutParams(recyclerviewParam);

        relativeLayout.addView(button);
        relativeLayout.addView(recyclerViewPlaylistLocal);
        view.addView(relativeLayout);
    }
    private void addSongToPlaylist(String idPlaylist,String idSong) {
        PlaylistSong playlistSong=new PlaylistSong(idSong,idPlaylist);
        Database.getInstance(mContext).playlistSongDAO().insertPlaylistSong(playlistSong);
        Toast.makeText(mContext, "Thêm vào playlist thành công!", Toast.LENGTH_SHORT).show();
    }
    private void showCreatePlaylist() {
        view.goToCreatePlaylist();
    }
}
