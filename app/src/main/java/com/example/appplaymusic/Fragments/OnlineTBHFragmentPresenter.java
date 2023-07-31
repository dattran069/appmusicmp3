package com.example.appplaymusic.Fragments;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.example.appplaymusic.Fragments.CanhanFragment.CanhanFragment;
import com.example.appplaymusic.Helper.HelpTools;
import com.example.appplaymusic.Models.Playlist;
import com.example.appplaymusic.Models.PlaylistSongResponse;
import com.example.appplaymusic.Models.Song;
import com.example.appplaymusic.Services.APIService;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OnlineTBHFragmentPresenter {
    public OnlineTBHFragmentPresenter() {
    }
    private OnlineTBHFragmentView view;
    public OnlineTBHFragmentPresenter(OnlineTBHFragmentView view) {
    this.view=view;
    }

    public void loadSonglistData(Playlist playlist, Context mContext) {
        //id user -> tru ra nhung bai them roi,
        // co playlist data co id roi, 1 function check in list thi hien dau tich v kh can id playlist
        //APIService.getService()
        Log.d("thisPlaylistSongSizeIDP",playlist.getIdPlaylist());
        List<Song> allSong=new ArrayList<>();
        List<Song> songList = new ArrayList<>();
        Call<List<Song>> call1=APIService.getService().getSongs();
        call1.enqueue(new Callback<List<Song>>() {
            @Override
            public void onResponse(Call<List<Song>> call, Response<List<Song>> response) {
                if(response.isSuccessful()){
                    allSong.addAll(response.body());
                    Log.d("thisPlaylistSongSizeAS", String.valueOf(allSong.size()));
                    if(songList.size()>0){
                        view.receiveSongList(allSong,songList,false);
                        return;
                    }
                }
            }
            @Override
            public void onFailure(Call<List<Song>> call, Throwable t) {
            }
        });

      Call<PlaylistSongResponse> call2 = APIService.getService().getIdSongsByPlaylistSongId(Integer.parseInt(playlist.getIdPlaylist()));
        List<String> idSongsLocal=new ArrayList<>();
        List<String> idSongsInternet=new ArrayList<>();
        call2.enqueue(new Callback<PlaylistSongResponse>() {
            @Override
            public void onResponse(Call<PlaylistSongResponse> call, Response<PlaylistSongResponse> response) {
                PlaylistSongResponse playlistSongResponse= response.body();
                 idSongsLocal.addAll(playlistSongResponse.getLocalSongIds()) ;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    idSongsLocal.forEach(id -> {
                        Song tmp=HelpTools.GetMediaMp3FilesByIdPlaylist(id,mContext);
                        if(tmp!=null) songList.add(tmp);
                    });
                }
                songList.addAll(playlistSongResponse.getInternetSongs());
                Log.d("thisPlaylistSongSize", String.valueOf(songList.size()));
                if(allSong.size()>0){
                    view.receiveSongList(allSong,songList,false);
                    return;
                }
            }
            @Override
            public void onFailure(Call<PlaylistSongResponse> call, Throwable t) {

            }
        });
        //view.receiveSongList(allSong,songList);
    }
}
