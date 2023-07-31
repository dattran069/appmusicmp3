package com.example.appplaymusic.Fragments.CanhanFragment;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
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
import com.example.appplaymusic.Helper.HelpTools;
import com.example.appplaymusic.Models.Playlist;
import com.example.appplaymusic.Models.PlaylistSong;
import com.example.appplaymusic.Models.Song;
import com.example.appplaymusic.R;
import com.example.appplaymusic.Services.APIService;
import com.example.appplaymusic.Services.PlayMusicService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TrenThietBiFragmentPresenter {
    TrenThietBiFragmentView view;
    Song song;
    public static final int RUNTIME_PERMISSION_CODE = 7;
    private boolean isPlaying;

    List<Song> songList=new ArrayList<>();

    ContentResolver contentResolver;

    Cursor cursor;

    Uri uri;

    Context mContext;
    List<Playlist> playlistsLocal;
    public TrenThietBiFragmentPresenter(TrenThietBiFragmentView view) {
        this.view = view;
        mContext= view.getContext();
    }

    public void setAdapterData() {
        GetAllMediaMp3Files();
    }
    public void GetAllMediaMp3Files(){
        view.setAdapterData(HelpTools.getAllSongOffline(mContext));
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
        //playlistsLocal= Database.getInstance(mContext).playlistDAO().getListPlaylist();
        int userId=Common.getUserId(mContext);
        if(userId!=-1){
            Call<List<Playlist>> callback=APIService.getService().getPlaylistsLocalByUserId(userId);
            try {
                Response<List<Playlist>> response=callback.execute();
                playlistsLocal=response.body();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

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
        //PlaylistSong playlistSong=new PlaylistSong(idSong,idPlaylist);
        ////Database.getInstance(mContext).playlistSongDAO().insertPlaylistSong(playlistSong);
        //co le phai tao playlistSong because playlist song n-n
        Call<Integer> call =APIService.getService().createPlaylistSong(Integer.parseInt(idPlaylist),Integer.parseInt(idSong),1);
        call.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                int id=response.body();
                if(id!=-1) Toast.makeText(mContext, "Thêm vào playlist thành công!", Toast.LENGTH_SHORT).show();
                else Toast.makeText(mContext, "Lỗi, không thêm được!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                Toast.makeText(mContext, "Lỗi, "+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
    private void showCreatePlaylist() {
        view.goToCreatePlaylist();
    }
}
