package com.example.appplaymusic.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appplaymusic.CategoryAdapter;
import com.example.appplaymusic.Models.Playlist;
import com.example.appplaymusic.Models.Song;
import com.example.appplaymusic.R;
import com.example.appplaymusic.Services.APIService;
import com.example.appplaymusic.Services.DataService;
import com.example.appplaymusic.SongAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HorizontalContainerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private int type;
    private static final int TYPE_PLAYLISTS_CONTAINER = 1;
    private List<Playlist> playlists;
    private Context mContext;
    public void setDataPlaylist(List<Playlist> playlist){
        this.playlists=playlist;
        notifyDataSetChanged();
    }
    HorizontalContainerAdapter(Context mContext){
        this.mContext=mContext;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_category,parent,false);
        return new CategoryViewHolder(view);
    }

    @Override
    public int getItemViewType(int position) {
        return getType();
    }

    private int getType() {
        return  type;
    }
    private void  setType(int type){
        this.type=type;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(type==TYPE_PLAYLISTS_CONTAINER){
            CategoryViewHolder categoryViewHolder=(CategoryViewHolder) holder;
            Playlist playlist=this.playlists.get(position);
            categoryViewHolder.subContainerName.setText(playlist.getNamePlaylist());
            SongAdapter songAdapter=new SongAdapter(mContext);
            songAdapter.setData(getListSongByPlaylist(playlist.getIdPlaylist()));
            categoryViewHolder.rcv_songs.setAdapter(songAdapter);
        }
    }

    private List<Song> getListSongByPlaylist(String idPlaylist) {
        List<Song> songList=new ArrayList<>();
        DataService dataService=APIService.getService();
        Call<List<Song>> callback=dataService.getListSongByPlaylistId(Integer.parseInt(idPlaylist));
        callback.enqueue(new Callback<List<Song>>() {
            @Override
            public void onResponse(Call<List<Song>> call, Response<List<Song>> response) {
                songList.addAll(response.body());
            }

            @Override
            public void onFailure(Call<List<Song>> call, Throwable t) {
                Log.d("getListSongByPlaylist",t.getMessage());
            }
        });
        return songList;
    }

    @Override
    public int getItemCount() {
        if((type==TYPE_PLAYLISTS_CONTAINER)&&this.playlists!=null){
            return this.playlists.size();
        }
        return 0;
    }
    public  class CategoryViewHolder extends RecyclerView.ViewHolder {
        TextView subContainerName;
        RecyclerView rcv_songs;
        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            rcv_songs=itemView.findViewById(R.id.rcv_songs);
            subContainerName=itemView.findViewById(R.id.txt_subContainer_title);
        }
    }
}
