package com.example.appplaymusic.PlaylistNew;

import android.content.Context;
import android.util.Log;

import com.example.appplaymusic.Models.Playlist;
import com.example.appplaymusic.Services.APIService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlaylistNewActivityPresenter {
    PlaylistNewActivityView view;
    public PlaylistNewActivityPresenter(PlaylistNewActivityView view) {
        this.view=view;
    }

    public void saveLocalPlaylist(int userId, String playlistName) {
        Call<String> cb=APIService.getService().createLocalPlaylist(userId,playlistName);
        cb.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.d("createLocalPlaylist",response.body());
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("createLocalPFailed",t.getMessage());
            }
        });
    }
}
