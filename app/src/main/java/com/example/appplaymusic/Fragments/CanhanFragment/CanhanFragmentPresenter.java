package com.example.appplaymusic.Fragments.CanhanFragment;

import android.util.Log;

import com.example.appplaymusic.Models.Playlist;
import com.example.appplaymusic.Services.APIService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CanhanFragmentPresenter {
    CanhanFragmentView view;
    public CanhanFragmentPresenter(CanhanFragmentView view) {
        this.view=view;
    }

    public void getPlaylistLocalFromInternet(int userId) {
        Call<List<Playlist>> callback=APIService.getService().getPlaylistsLocalByUserId(userId);
        callback.enqueue(new Callback<List<Playlist>>() {
            @Override
            public void onResponse(Call<List<Playlist>> call, Response<List<Playlist>> response) {
                List<Playlist> playlists=response.body();
                view.receivePlaylist(playlists);
            }

            @Override
            public void onFailure(Call<List<Playlist>> call, Throwable t) {
                Log.d("receivePlaylistFailed",t.getMessage());
            }
        });
    }
}
