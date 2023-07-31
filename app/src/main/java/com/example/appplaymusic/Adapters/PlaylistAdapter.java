package com.example.appplaymusic.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.appplaymusic.Models.Playlist;
import com.example.appplaymusic.Models.Song;
import com.example.appplaymusic.R;

import java.util.List;

public class PlaylistAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private static final int TYPE_ITEM = 1;
    private static final int TYPE_BUTTON = 2;
    private Context mContext;
    private List<com.example.appplaymusic.Models.Playlist> playlists;

    public List<com.example.appplaymusic.Models.Playlist> getData() {
        return this.playlists;
    }

    public interface  IClickListener{
        public  void onClickListener(com.example.appplaymusic.Models.Playlist playlist);
    };
    IClickListener iClickListener;
    public PlaylistAdapter(Context mContext){
        this.mContext=mContext;
    }
    public IClickListener getiClickListener() {
        return iClickListener;
    }

    public void setiClickListener(IClickListener iClickListener) {
        this.iClickListener = iClickListener;
    }

    public void setData(List<com.example.appplaymusic.Models.Playlist> playlists){
        this.playlists=playlists;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return position==playlists.size()?TYPE_BUTTON:TYPE_ITEM;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType==TYPE_ITEM)
        return new PlaylistViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item,parent,false));
        else return new ButtonViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_button,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if(position==this.playlists.size()){
            return;
        }
        com.example.appplaymusic.Models.Playlist playlist=this.playlists.get(position);
        if(playlist==null) return;
        PlaylistViewHolder songViewHolder= (PlaylistViewHolder) holder;
        Glide.with(mContext).load(playlist.getImage()).into(songViewHolder.imgView);
        songViewHolder.txtName.setText(playlist.getNamePlaylist());
        Log.d("getSingerName()",playlist.toString());
        songViewHolder.txtSingerName.setText(playlist.getSingerName());
        songViewHolder.imgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iClickListener.onClickListener(playlist);
            }
        });
    }


    @Override
    public int getItemCount() {
       return (this.playlists==null)?0:this.playlists.size();
    }

    public  class PlaylistViewHolder extends RecyclerView.ViewHolder {
        ImageButton btnPlay;
        ImageView imgView;
        TextView txtName;
        TextView txtSingerName;
        public PlaylistViewHolder(@NonNull View itemView) {
            super(itemView);
            btnPlay=itemView.findViewById(R.id.btn_play);
            imgView=itemView.findViewById(R.id.img_item);
            txtName=itemView.findViewById(R.id.txt_subContainer_title);
            txtSingerName=itemView.findViewById(R.id.txt_subContainer_singerName);
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
