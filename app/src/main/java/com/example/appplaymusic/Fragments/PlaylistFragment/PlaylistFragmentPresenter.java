package com.example.appplaymusic.Fragments.PlaylistFragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.appplaymusic.Adapters.PlaylistPagerAdapter;
import com.example.appplaymusic.Fragments.PlaylistFrontFragment;
import com.example.appplaymusic.Models.Playlist;
import com.example.appplaymusic.Models.Song;
import com.example.appplaymusic.Services.APIService;
import com.example.appplaymusic.Services.DataService;
import com.example.appplaymusic.Services.PlayMusicService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlaylistFragmentPresenter {
    private int currentId=0;
    private Context mContext;
    private PlaylistFragmentView playlistView;
    private Playlist playlist;
    public PlaylistFragmentPresenter(PlaylistFragmentView playlistView,Context mContext,Playlist playlist) {
        this.playlist=playlist;
        this.playlistView = playlistView;
        this.mContext=mContext;
    }
    //getSonglistByIdPlaylist query limit 3 record by 1 time
    public void getSongListData() {
        if(playlist==null) return;
        List<Song> songList=new ArrayList<>();
        DataService dataService= APIService.getService();
        //Call<List<Song>> callback=dataService.songsByPlaylistIdLimit(Integer.parseInt(playlist.getIdPlaylist()),currentId);
        Call<List<Song>> callback=dataService.getListSongByPlaylistId(Integer.parseInt(playlist.getIdPlaylist()));
        callback.enqueue(new Callback<List<Song>>() {
            @Override
            public void onResponse(Call<List<com.example.appplaymusic.Models.Song>> call, Response<List<Song>> response) {
                ArrayList<com.example.appplaymusic.Models.Song> songList2= (ArrayList<com.example.appplaymusic.Models.Song>)  response.body();
                for(int i=0;i<songList2.size();i++){
                    songList.add(songList2.get(i));
                    Log.d("onResponse",songList2.get(i).getSongImage());
                    //if(currentId<Integer.parseInt(songList2.get(i).getIdSong())) currentId= Integer.parseInt(songList2.get(i).getIdSong());
                    //Log.d("onResponseCurrentSongId", String.valueOf(currentId));
                }
                playlistView.setSongAdapterData(songList);
            }

            @Override
            public void onFailure(Call<List<com.example.appplaymusic.Models.Song>> call, Throwable t) {
                Log.d("onResponse",t.getMessage());
            }
        });
    }


    public void loadMoreSongData() {
        List<com.example.appplaymusic.Models.Song> songList=new ArrayList<>();
        DataService dataService= APIService.getService();
        Call<List<Song>> callback=dataService.songsByPlaylistIdLimit(Integer.parseInt(playlist.getIdPlaylist()),currentId);
        callback.enqueue(new Callback<List<Song>>() {
            @Override
            public void onResponse(Call<List<com.example.appplaymusic.Models.Song>> call, Response<List<Song>> response) {

                ArrayList<com.example.appplaymusic.Models.Song> songList2= (ArrayList<com.example.appplaymusic.Models.Song>)  response.body();
                for(int i=0;i<songList2.size();i++){
                    songList.add(songList2.get(i));
                    Log.d("onResponse",songList2.get(i).getSongImage());
                    if(currentId<Integer.parseInt(songList2.get(i).getIdSong())) currentId= Integer.parseInt(songList2.get(i).getIdSong());
                    Log.d("onResponseCurrentSongId", String.valueOf(currentId) +" "+songList2.get(i).getIdSong());
                }
                //if(songList.size()==0) Toast.makeText(mContext, "Hết rồi á!", Toast.LENGTH_SHORT).show();
                //playlistView.removeLastItem();
                playlistView.add(songList);
                playlistView.setSongAdapterLoader();
            }

            @Override
            public void onFailure(Call<List<com.example.appplaymusic.Models.Song>> call, Throwable t) {
                Log.d("onResponse",t.getMessage());
            }
        });
    }

    public int getTotalSongs(PlaylistPagerAdapter playlistPagerAdapter) {
        return  playlistPagerAdapter.getSongNums();
    }

    public void removeLoadMoreBtn() {
        playlistView.removeLoadMoreBtn();
    }
}
