package com.example.appplaymusic.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.appplaymusic.Commons.Common;
import com.example.appplaymusic.Helper.HelpTools;
import com.example.appplaymusic.Models.Playlist;
import com.example.appplaymusic.Models.Singer;
import com.example.appplaymusic.R;
import com.example.appplaymusic.ThemBaihatActivity.ThemBaiHatActivity;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class SingerAdapter extends RecyclerView.Adapter<SingerAdapter.PlaylistCanhanViewHolder>{
    private List<Singer> singers;
    private Context mContext;
    public void setData(List<Singer> singers, Context mContext, IonClick ionClick){
        this.ionClick=ionClick;
        this.mContext=mContext;
        this.singers=singers;
        notifyDataSetChanged();
    }
    public void setData(List<Singer> singers, Context mContext){
        this.mContext=mContext;
        this.singers=singers;
        notifyDataSetChanged();
    }
    private IonClick ionClick;
    public interface IonClick{
        void onClick(String id, Singer singer);
    }
    public  SingerAdapter(int type){
        this.type=type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public  SingerAdapter(){

    }
    int type=Common.TYPE_HORI;
    @Override
    public int getItemViewType(int position) {
        if(type== Common.TYPE_HORI) return Common.TYPE_HORI;
        return Common.TYPE_VERTICAL;
    }

    @NonNull
    @Override
    public PlaylistCanhanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=null;
        if(viewType==Common.TYPE_HORI){
            view= LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_nghesi,parent,false);
            Log.d("bindSinger", "onCreateViewHolder tren");
        } else {
            view= LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_singer_item_anhtron_vertical,parent,false);
            Log.d("bindSinger", "onCreateViewHolder !tren");
        }
        return new PlaylistCanhanViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull PlaylistCanhanViewHolder holder, int position) {
        Log.d("bindSinger", String.valueOf(position));
        holder.linearLayoutItem.setOnClickListener(view -> {
            ionClick.onClick(singers.get(position).getId(),this.singers.get(position));
        });
        Singer singer=singers.get(position);
        Log.d("bindSinger",singer.getSingerName());
        if(singer==null) return;
        //if(singer.getImage()==null) bitmap=HelpTools.getBitmapRandomLocal(mContext);
        Glide.with(mContext).load(singer.getSingerImage()).into(holder.imageView);
        holder.txtSingerName.setText(singer.getSingerName());
    }

    @Override
    public int getItemCount() {
        if(singers==null) return 0;
        Log.d("bindSinger", String.valueOf(this.singers.size()));
        return this.singers.size();
    }

    class PlaylistCanhanViewHolder extends RecyclerView.ViewHolder{
        LinearLayout linearLayoutItem;
        CircleImageView imageView;
        TextView txtSingerName;
        //TextView txtCreatorName;
        //ImageView img_more;
        public PlaylistCanhanViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.img_singer);
            txtSingerName=itemView.findViewById(R.id.txt_singer_name);
            //txtCreatorName=itemView.findViewById(R.id.txt_creator_name);
            linearLayoutItem=itemView.findViewById(R.id.linearLayoutItem);
            //img_more=itemView.findViewById(R.id.img_playlist_action);
        }
    }
}
