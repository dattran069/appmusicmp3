package com.example.appplaymusic.Adapters;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.appplaymusic.Commons.Common;
import com.example.appplaymusic.Helper.HelpTools;
import com.example.appplaymusic.Models.Playlist;
import com.example.appplaymusic.Models.Song;
import com.example.appplaymusic.R;
import com.example.appplaymusic.Services.APIService;
import com.example.appplaymusic.ThemBaihatActivity.ThemBaiHatActivity;
import com.google.firebase.installations.Utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
public class SongAddAdapter extends RecyclerView.Adapter<SongAddAdapter.SongViewHolder>{
    private List<Song> songList;
    private List<Song> songList2;
    private Playlist playlist;
    ThemBaiHatActivity activity;

    public List<Song> getSongList() {
        return songList;
    }

    public void setSongList(List<Song> songList) {
        this.songList = songList;
    }

    public List<Song> getSongList2() {
        return songList2;
    }

    public void setSongList2(List<Song> songList2) {
        this.songList2 = songList2;
    }

    private int type;
    private Map<Integer,Song> songListHashMap=new HashMap<>();
    public static final int ACTION_ADD=0;
    public static final int ACTION_DELETE=1;
    public static final int RUNTIME_PERMISSION_CODE = 7;
    Context mContext;
    @NonNull
    @Override
    public SongViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_song_item_add,parent,false);
        return new SongViewHolder(view);
    }
    public void setData(Playlist playlist, int type, List<Song> songList, List<Song> songList2, Context mContext, Context act){
        this.playlist=playlist;
        this.type=type;
        this.songList=songList;
        this.songList2=songList2;
        for (int i=0;i<songList.size();i++){
            Log.d("songlist"+ String.valueOf(i),songList.get(i).toString());
        }
        for (int i=0;i<songList2.size();i++){
            Log.d("songlist2"+ String.valueOf(i),songList2.get(i).toString());
        }
        for (int i = 0; i < songList.size(); i++) {
            Song songTrung=checkContain(songList.get(i));
            if(songTrung!=null)
                songListHashMap.put(i,songTrung);
            else songListHashMap.put(i,null);
        }
        for (int i = 0; i < songListHashMap.size(); i++) {
            Log.d("hashmap",songList.get(i).getSongName()+" "+(songListHashMap.get(i)!=null?"true":"false"));
        }
        this.mContext=mContext;
        activity=(ThemBaiHatActivity) act;
        notifyDataSetChanged();
    }

    private Song checkContain(Song song) {
        for (int i = 0; i < songList2.size(); i++) {
            if(songList2.get(i).getIdSong().equals(song.getIdSong()))
                return songList2.get(i);
        }
        return null;
    }



    @Override
    public void onBindViewHolder(@NonNull SongViewHolder holder, int position) {
        Song song=songList.get(position);
        if(song==null) return;
        if(type==0) Glide.with(mContext).load(song.getSongImage()).into(holder.imgSong);
        else holder.imgSong.setImageBitmap(HelpTools.getBitmapImgSongLocal(song,mContext));
        holder.txt_song_name.setText(song.getSongName());
        holder.txt_song_singer_name.setText(song.getSingerName());
        if(songListHashMap.get(holder.getAdapterPosition())!=null){
            holder.btnAddtoPlaylist.setImageDrawable(mContext.getDrawable(R.drawable.ic_baseline_cancel_24));
        } else holder.btnAddtoPlaylist.setImageDrawable(mContext.getDrawable(R.drawable.add));
        holder.RelativeLayout_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(songListHashMap.get(holder.getAdapterPosition())!=null)
                {
                    //removeSong
                    if(removeSongFromPlaylist(playlist.getIdPlaylist(),song.getIdSong(),type)){
                        Song oldSong=songListHashMap.get(holder.getAdapterPosition());
                        songListHashMap.put(holder.getAdapterPosition(),null);
                        songList2.remove(oldSong);
                        Toast.makeText(mContext, "Xóa thành công khỏi playlist", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    addSongToPlaylist(playlist.getIdPlaylist(),song.getIdSong(),type);
                    if(activity.checkPermissions())  {
                        Log.d("del","false");
                        downloadMp3FromInternet(song);
                        songListHashMap.put(holder.getAdapterPosition(),song);
                        songList2.add(song);
                    } else {
                        Log.d("del","true");
                    }
                }
                notifyDataSetChanged();
            }
        });
    }



    private void downloadMp3FromInternet(Song song) {
        HelpTools.DownloadingTask dlT=new HelpTools.DownloadingTask(mContext);
        dlT.setSong(song);
        dlT.execute();
    }


    private boolean removeSongFromPlaylist(String idPlaylist, String idSong, int type) {
        Call<Void> deleteCall=APIService.getService().deleteSongFromPlaylistLocal(idSong,idPlaylist);
        try {
            Response response=deleteCall.execute();
            if(response.isSuccessful()) return  true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void addSongToPlaylist(String idPlaylist,String idSong,int type) {
        //PlaylistSong playlistSong=new PlaylistSong(idSong,idPlaylist);
        ////Database.getInstance(mContext).playlistSongDAO().insertPlaylistSong(playlistSong);
        //co le phai tao playlistSong because playlist song n-n
        Call<Integer> call = APIService.getService().createPlaylistSong(Integer.parseInt(idPlaylist),Integer.parseInt(idSong),type);
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

    @Override
    public int getItemCount() {
        if(songList==null) return 0;
        return songList.size();
    }

    public  class SongViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout RelativeLayout_item;
        TextView txt_song_name;
        TextView txt_song_singer_name;
        ImageView btnAddtoPlaylist;
        ImageView imgSong;
        public SongViewHolder(@NonNull View itemView) {
            super(itemView);
            imgSong=itemView.findViewById(R.id.img_item);
            RelativeLayout_item=itemView.findViewById(R.id.RelativeLayout_item);
            txt_song_name=itemView.findViewById(R.id.txt_song_name);
            txt_song_singer_name=itemView.findViewById(R.id.txt_singer_name);
            btnAddtoPlaylist=itemView.findViewById(R.id.img_addTo);
        }
    }
}
