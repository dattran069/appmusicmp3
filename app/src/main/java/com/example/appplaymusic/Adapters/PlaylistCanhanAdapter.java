package com.example.appplaymusic.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import com.example.appplaymusic.Helper.HelpTools;
import com.example.appplaymusic.Models.Playlist;
import com.example.appplaymusic.R;
import com.example.appplaymusic.ThemBaihatActivity.ThemBaiHatActivity;

import java.util.List;

public class PlaylistCanhanAdapter extends RecyclerView.Adapter<PlaylistCanhanAdapter.PlaylistCanhanViewHolder>{
    private List<com.example.appplaymusic.Models.Playlist> playlists;
    private  boolean isOnline=false;
    public void setIsOnline(boolean b){
        this.isOnline=b;
    }
    private Context mContext;
    public void setData(List<com.example.appplaymusic.Models.Playlist> playlists,Context mContext,IonClick ionClick){
        this.ionClick=ionClick;
        this.mContext=mContext;
        this.playlists=playlists;
        notifyDataSetChanged();
    }
    public void setData(boolean isOnline,List<com.example.appplaymusic.Models.Playlist> playlists,Context mContext,IonClick ionClick){
        this.ionClick=ionClick;
        this.mContext=mContext;
        this.playlists=playlists;
        this.isOnline=isOnline;
        notifyDataSetChanged();
    }
    private IonClick ionClick;
    public interface IonClick{
        void onClick(String id, Playlist playlist);
    }
    @NonNull
    @Override
    public PlaylistCanhanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.playlist_hori,parent,false);
        return new PlaylistCanhanViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaylistCanhanViewHolder holder, int position) {
        holder.linearLayoutItem.setOnClickListener(view -> {
            ionClick.onClick(playlists.get(position).getIdPlaylist(),this.playlists.get(position));
        });
        com.example.appplaymusic.Models.Playlist playlist=playlists.get(position);
        if(playlist==null) return;
        PlaylistCanhanViewHolder playlistCanhanViewHolder=(PlaylistCanhanViewHolder) holder;
        Bitmap bitmap=null;
        if(!isOnline){
            if(playlist.getImage()==null) bitmap=HelpTools.getBitmapRandomLocal(mContext);
            holder.imageView.setImageBitmap(bitmap);
        } else Glide.with(mContext).load(playlist.getImage()).into(holder.imageView);

        holder.txtPlaylistName.setText(playlist.getNamePlaylist());
        holder.txtCreatorName.setText(playlist.getCreaterNamePlaylist());
        holder.img_more.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                PopupMenu poup=new PopupMenu(holder.img_more.getContext(),holder.linearLayoutItem);
                poup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()){
                            case R.id.delete:
                                Toast.makeText(mContext, "Xóa", Toast.LENGTH_SHORT).show();
                            return true;
                            case R.id.sua:
                                Toast.makeText(mContext, "Sửa tên", Toast.LENGTH_SHORT).show();
                                return true;
                            case R.id.addToPlaylist:
                                Intent intent=new Intent(mContext, ThemBaiHatActivity.class);
                                Bundle bundle=new Bundle();
                                bundle.putSerializable("playlist_obj",playlist);
                                intent.putExtras(bundle);
                                mContext.startActivity(intent);
                                return true;
                            default :return false;
                        }
                    }
                });
                poup.inflate(R.menu.menu_playlist_canhan);
                poup.setGravity(Gravity.RIGHT);
                poup.show();
            }
        });

    }

    @Override
    public int getItemCount() {
        if(playlists==null) return 0;
        return this.playlists.size();
    }

    class PlaylistCanhanViewHolder extends RecyclerView.ViewHolder{
        LinearLayout linearLayoutItem;
        ImageView imageView;
        TextView txtPlaylistName;
        TextView txtCreatorName;
        ImageView img_more;
        public PlaylistCanhanViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.img_playlist_canhan);
            txtPlaylistName=itemView.findViewById(R.id.txt_playlist_name);
            txtCreatorName=itemView.findViewById(R.id.txt_creator_name);
            linearLayoutItem=itemView.findViewById(R.id.linearLayoutItem);
            img_more=itemView.findViewById(R.id.img_playlist_action);
        }
    }
}
