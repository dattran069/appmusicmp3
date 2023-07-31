package com.example.appplaymusic.Fragments.TaiKhoanFragment;

import android.util.Log;

import com.example.appplaymusic.Commons.Common;
import com.example.appplaymusic.Models.Playlist;
import com.example.appplaymusic.Models.User;
import com.example.appplaymusic.Services.APIService;
import com.example.appplaymusic.Services.DataService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TaiKhoanFragmentPresenter {
    TaiKhoanFragmentView view;

    public TaiKhoanFragmentPresenter(TaiKhoanFragmentView view) {
        this.view = view;
    }


    public void loadPlaylistCanhan(User user) {
        int userId=user.getId();
        Call<List<Playlist>> callback=APIService.getService().getPlaylistsLocalByUserId(userId);
        callback.enqueue(new Callback<List<Playlist>>() {
            @Override
            public void onResponse(Call<List<Playlist>> call, Response<List<Playlist>> response) {
                List<Playlist> playlists=response.body();
                Log.d("load local playlist: ", String.valueOf(playlists.size()));
            }

            @Override
            public void onFailure(Call<List<Playlist>> call, Throwable t) {
                Log.d("loadlocalpfalied:", t.getMessage());

            }
        });
    }
}
