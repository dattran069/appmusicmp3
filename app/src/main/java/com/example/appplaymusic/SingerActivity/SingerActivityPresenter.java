package com.example.appplaymusic.SingerActivity;

import android.content.Context;
import android.util.Log;

import com.example.appplaymusic.Models.Singer;
import com.example.appplaymusic.Services.APIService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SingerActivityPresenter {
    Context mContext;
    Singer singer;
    private SingerActivityView view;
    public SingerActivityPresenter(Context mContext,SingerActivityView view){
        this.mContext=mContext;
        this.view=view;
    }

    public void loadInfoSinger(int singerId) {
        Call<List<Singer>> call=APIService.getService().getSingerBySingerId(singerId);
        call.enqueue(new Callback<List<Singer>>() {
            @Override
            public void onResponse(Call<List<Singer>> call, Response<List<Singer>> response) {
                view.receiveSinger(((List<Singer>)response.body()).get(0));
                List<Singer> list=response.body();
                //Log.d("receiveSinger", String.valueOf(list.size()));
                Log.d("receiveSinger", response.toString());
            }
            @Override
            public void onFailure(Call<List<Singer>> call, Throwable t) {
            }
        });
    }
}
