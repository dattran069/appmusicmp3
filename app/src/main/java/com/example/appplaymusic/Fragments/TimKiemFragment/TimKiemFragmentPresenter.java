package com.example.appplaymusic.Fragments.TimKiemFragment;

import android.content.Context;
import android.util.Log;

import com.example.appplaymusic.Models.SearchResponse;
import com.example.appplaymusic.Services.APIService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TimKiemFragmentPresenter {
    private Context mContext;
    private  TimKiemFragmentView view;
    public TimKiemFragmentPresenter(Context mContext, TimKiemFragmentView view){
        this.mContext=mContext;
        this.view=view;
    }

    public void loadSearch(String searchKey) {
        Call<SearchResponse> callback= APIService.getService().searchByKey(searchKey);
        callback.enqueue(new Callback<SearchResponse>() {
            @Override
            public void onResponse(Call<SearchResponse> call, Response<SearchResponse> response) {
                if(response.isSuccessful()){
                    SearchResponse searchResponse=response.body();
                    Log.d("SearchResponse",searchResponse.toString());
                    view.show(searchResponse.getSongs(),searchResponse.getPlaylists(),searchResponse.getSingers());
                }
            }

            @Override
            public void onFailure(Call<SearchResponse> call, Throwable t) {
                Log.d("SearchResponse","failed "+t.getMessage());
            }
        });
    }
}
