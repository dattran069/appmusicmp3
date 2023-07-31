package com.example.appplaymusic;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.appplaymusic.Models.Song;

import java.util.List;

public class SongAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private static final int TYPE_ITEM = 1;
    private static final int TYPE_BUTTON = 2;
    private Context mContext;
    private List<com.example.appplaymusic.Models.Song> songList;
    public interface  IClickListener{
        public  void onClickListener(Song song);
    };
    IClickListener iClickListener;
    public SongAdapter(Context mContext){
        this.mContext=mContext;
    }
    public IClickListener getiClickListener() {
        return iClickListener;
    }

    public void setiClickListener(IClickListener iClickListener) {
        this.iClickListener = iClickListener;
    }

    public void setData(List<com.example.appplaymusic.Models.Song> songList){

        this.songList=songList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return position==songList.size()?TYPE_BUTTON:TYPE_ITEM;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType==TYPE_ITEM)
        return new SongViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item,parent,false));
        else return new ButtonViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_button,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(position==this.songList.size()){
            return;
        }
        com.example.appplaymusic.Models.Song song=this.songList.get(position);
        if(song==null) return;
        SongViewHolder songViewHolder= (SongViewHolder) holder;
        Glide.with(mContext).load(song.getSongImage()).into(songViewHolder.imgView);
        songViewHolder.imgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iClickListener.onClickListener(song);
            }
        });
    }


    @Override
    public int getItemCount() {
       return (this.songList==null)?0:this.songList.size()+1;
    }

    public  class SongViewHolder extends RecyclerView.ViewHolder {
        ImageButton btnPlay;
        ImageView imgView;
        public SongViewHolder(@NonNull View itemView) {
            super(itemView);
            btnPlay=itemView.findViewById(R.id.btn_play);
            imgView=itemView.findViewById(R.id.img_item);
        }
    }
    public  class ButtonViewHolder extends RecyclerView.ViewHolder {
        ImageView imgView;
        ImageButton btn;
        public ButtonViewHolder(@NonNull View itemView) {
            super(itemView);
            imgView=itemView.findViewById(R.id.img_item);
            btn=itemView.findViewById(R.id.btn_add);
        }
    }
}
