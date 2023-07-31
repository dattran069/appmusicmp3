package com.example.appplaymusic.Adapters;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.appplaymusic.Commons.Common;
import com.example.appplaymusic.Fragments.PlaylistFragment.PlaylistFragment;
import com.example.appplaymusic.Helper.HelpTools;
import com.example.appplaymusic.ILoadmore;
import com.example.appplaymusic.Models.Playlist;
import com.example.appplaymusic.Models.Song;
import com.example.appplaymusic.R;
import com.example.appplaymusic.Services.APIService;

import org.w3c.dom.Text;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewSongAdapter  extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private int limit=5;
    private boolean hideItem=false;
    public int getLimit() {
        return limit;
    }
    private boolean isLimit=true;
    public void setLimit(int limit) {
        this.limit = limit;
    }

    private static final int TYPE_ITEM = 1;
    private static final int TYPE_BUTTON = 2;
    private static final int TYPE_ITEM_SAME_SONG = 3;
    private final int VIEW_TYPE_ITEM = 0, VIEW_TYPE_LOADING = 1;
    ILoadmore loadMore;
    boolean isLoading;
    PopupWindow popupWindow=null;
    PopupWindow popupWindowPL=null;
    boolean isShowCurrentSong=false;
    private View mainPage;
    private List<com.example.appplaymusic.Models.Song> songList;
    private Context mContext;
    private boolean moreable=true;
    private boolean inLocalPlaylist=false;

    public NewSongAdapter(boolean inLocalPlaylist) {
        this.inLocalPlaylist=inLocalPlaylist;
    }

    public void removeLoadMoreBtn() {

    }
    public void setMoreable(boolean can){
        this.moreable=can;
    }

    public void updateSongCurrent() {
        notifyDataSetChanged();
    }

    public void setMainPage(View view) {
        mainPage=view;
    }

    public List<Song> getData() {
        return this.songList;
    }

    public interface  IClickListener{
        public  void onClickListener(com.example.appplaymusic.Models.Song song, int position);
        public  void onClickMoreListener(ImageView imageView,String idSong);
        public  void onClickAddPlaylistListener(com.example.appplaymusic.Models.Song song,int position);
        public  void onClickRemovePlaylistListener(com.example.appplaymusic.Models.Song song,int position);
        public  void onClickXemThemListener(com.example.appplaymusic.Models.Song song,int position);
    };
    IClickListener iClickListener;

    public IClickListener getiClickListener() {
        return iClickListener;
    }

    public void setiClickListener(IClickListener iClickListener) {
        this.iClickListener = iClickListener;
    }

    public NewSongAdapter() {
    }

    public void setData(List<com.example.appplaymusic.Models.Song> songList, Context mContext){
        this.mContext=mContext;
        this.songList=songList;
        notifyDataSetChanged();
    }

    public boolean isLimit() {
        return isLimit;
    }

    public void setLimit(boolean limit) {
        isLimit = limit;
    }

    public void setData(List<com.example.appplaymusic.Models.Song> songList, Context mContext, boolean isLimit, boolean isShowCurrentSong){
        this.isLimit=isLimit;
        this.mContext=mContext;
        this.songList=songList;
        this.isShowCurrentSong=isShowCurrentSong;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        //return position==songList.size()?TYPE_BUTTON:this.songList.get(position).isSameSong()?TYPE_ITEM_SAME_SONG:TYPE_ITEM;
        if(!isLimit) return TYPE_ITEM;
        if(songList.size()<=limit) return TYPE_ITEM;
        if(position==limit) return TYPE_BUTTON;
//        return position==songList.size()?TYPE_BUTTON:TYPE_ITEM;
        return TYPE_ITEM;
    }

    public void setLoadMore(ILoadmore loadMore) {
        this.loadMore = loadMore;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM)
            return new NewSongViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_song_item, parent, false));
        else if (viewType == TYPE_BUTTON) return new ButtonViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_button_more_song, parent, false));
        else
            return new NewSameSongViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_playsong_song_item, parent, false));
        }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if(isLimit&&position==limit){
            ButtonViewHolder buttonViewHolder=(ButtonViewHolder) holder;
            buttonViewHolder.linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    iClickListener.onClickXemThemListener(songList.get(position), holder.getAdapterPosition());
                }
            });
            return;
        }
        com.example.appplaymusic.Models.Song song=this.songList.get(position);
        if(song==null) return;

        if(holder instanceof NewSongViewHolder){
            NewSongViewHolder songViewHolder= (NewSongViewHolder) holder;
            if(position== Common.getCurrentIndex()&&isShowCurrentSong){
                songViewHolder.RelativeLayout_item.setBackgroundColor(mContext.getResources().getColor(R.color.active_btn_pink));
                //songViewHolder.txt_song_name.setText("dang phat");
            } else songViewHolder.RelativeLayout_item.setBackgroundColor(mContext.getResources().getColor(R.color.white));
            songViewHolder.txt_song_name.setText(song.getSongName());
            if (song.getType()==1) {
                songViewHolder.imgSong.setImageBitmap(HelpTools.getBitmapImgSongLocal(song,mContext));
            }
            else Glide.with(mContext).load(song.getSongImage()).into( songViewHolder.imgSong);

            songViewHolder.txt_song_singer_name.setText(song.getSingerName());
            songViewHolder.RelativeLayout_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(popupWindow!=null&&popupWindow.isShowing()) popupWindow.dismiss();
                    iClickListener.onClickListener(song,holder.getAdapterPosition());
                }
            });
            songViewHolder.btnMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(popupWindow!=null&&popupWindow.isShowing()) popupWindow.dismiss();
                    //iClickListener.onClickMoreListener(songViewHolder.btnMore,song.getIdSong());
                    LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View customView = layoutInflater.inflate(R.layout.layout_song_option,null);

                    ImageView btnClose=customView.findViewById(R.id.imgClose);
                    ImageView songAvar=customView.findViewById(R.id.imgSongAvar);
                    TextView txtSongName=customView.findViewById(R.id.txt_song_name);
                    TextView txtSingerName=customView.findViewById(R.id.txt_singer_name);
                    Glide.with(mContext).load(song.getSongImage()).into(songAvar);
                    txtSongName.setText(song.getSongName());
                    txtSingerName.setText(song.getSingerName());
                    LinearLayout li_addToPlaylist=customView.findViewById(R.id.li_addToPlaylist);
                    LinearLayout li_addToListPlay=customView.findViewById(R.id.li_addToListPlay);
                    LinearLayout li_removeFromThisPlaylist=customView.findViewById(R.id.li_removeFromThisPlaylist);
                    if(inLocalPlaylist) li_removeFromThisPlaylist.setVisibility(View.VISIBLE);
                    li_addToPlaylist.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(popupWindow!=null&&popupWindow.isShowing()) popupWindow.dismiss();
                            LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                            View customView = layoutInflater.inflate(R.layout.layout_playlist_ca_nhan,null);
                            ImageView btnClose=customView.findViewById(R.id.imgClose);
                            btnClose.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    popupWindowPL.dismiss();
                                    if(popupWindow!=null&&popupWindow.isShowing()) popupWindow.dismiss();
                                }
                            });
                            RecyclerView recyclerViewPL=customView.findViewById(R.id.RecyclerViewPlaylist);
                            PlaylistCanhanAdapter playlistCanhanAdapter=new PlaylistCanhanAdapter();
                            recyclerViewPL.setAdapter(playlistCanhanAdapter);
                            LinearLayoutManager linearLayoutManager=new LinearLayoutManager(mContext);
                            recyclerViewPL.setLayoutManager(linearLayoutManager);
                            List<Playlist> playlistsLocal=new ArrayList<>();
                            //playlistsLocal= Database.getInstance(mContext).playlistDAO().getListPlaylist();
                            int userId=Common.getUserId(mContext);
                            if(userId!=-1){
                                Call<List<Playlist>> callback= APIService.getService().getPlaylistsLocalByUserId(userId);
                                try {
                                    Response<List<Playlist>> response=callback.execute();
                                    playlistsLocal=response.body();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }

                            playlistCanhanAdapter.setData(playlistsLocal, mContext, new PlaylistCanhanAdapter.IonClick() {
                                @Override
                                public void onClick(String idPlaylist,Playlist playlist) {
                                    addSongToPlaylist(song,idPlaylist,song.getIdSong(),song.getType());
                                }
                            });

                            popupWindowPL = new PopupWindow(customView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                            popupWindowPL.setAnimationStyle(R.style.popup_window_animation);
                            popupWindowPL.showAtLocation(songViewHolder.imgSong, Gravity.BOTTOM, 0, 0);
                        }
                    });
                    li_addToListPlay.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Toast.makeText(mContext, "Add to lp", Toast.LENGTH_SHORT).show();
                        }
                    });
                    li_removeFromThisPlaylist.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Call<Void> call=APIService.getService().deleteSongFromPlaylistLocal(song.getIdSong(), PlaylistFragment.getPlaylist().getIdPlaylist());
                            call.enqueue(new Callback<Void>() {
                                @Override
                                public void onResponse(Call<Void> call, Response<Void> response) {
                                    if(response.isSuccessful()){
                                        iClickListener.onClickRemovePlaylistListener(song,holder.getAdapterPosition());
                                        Toast.makeText(mContext, "Đã xóa khỏi playlist", Toast.LENGTH_SHORT).show();
                                        songList.remove(position);
                                        notifyDataSetChanged();
                                    }

                                }

                                @Override
                                public void onFailure(Call<Void> call, Throwable t) {
                                    Toast.makeText(mContext, "Không xóa được", Toast.LENGTH_SHORT).show();
                                }
                            });
                            if(popupWindow!=null&&popupWindow.isShowing()) popupWindow.dismiss();
                        }

                    });
                    popupWindow = new PopupWindow(customView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    popupWindow.setAnimationStyle(R.style.popup_window_animation);
//                    popupWindow.setBackgroundDrawable(new BitmapDraw able(mContext.getResources(),
//                            ""));
                   popupWindow.showAtLocation(songViewHolder.imgSong, Gravity.BOTTOM, 0, 0);
//                    popupWindow.setOutsideTouchable(true);
                    btnClose.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            popupWindow.dismiss();
                        }
                    });
                }
            });
        }
        else if(holder instanceof NewSameSongViewHolder){
            NewSameSongViewHolder songViewHolder= (NewSameSongViewHolder) holder;
            Glide.with(mContext).load(song.getSongImage()).into(songViewHolder.imgSong);
            songViewHolder.txt_song_name.setText(song.getSongName());
            songViewHolder.txt_song_singer_name.setText(song.getSingerName());
            songViewHolder.btnAddtoPlaylist.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    iClickListener.onClickAddPlaylistListener(song,holder.getAdapterPosition());
                }
            });
            songViewHolder.RelativeLayout_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    iClickListener.onClickListener(song,holder.getAdapterPosition());
                }
            });
            //songViewHolder.btnMore.setOnClickListener(view -> iClickListener.onClickMoreListener(song,holder.getAdapterPosition()));
        }
        else if (holder instanceof LoadingViewHolder) {
            LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
            loadingViewHolder.progressBar.setIndeterminate(true);
        }


    }
    public void setLoader() {
        isLoading = false;
    }


    @Override
    public int getItemCount() {
        //return (this.songList==null)?0:this.moreable?this.songList.size()+1:this.songList.size();
        if(this.songList==null) return 0;
        if(!isLimit) return this.songList.size();
        if(this.songList.size()<=limit) return this.songList.size();
        hideItem=true;
        Log.d("limit","true");
        Log.d("limit","song list: "+songList.size()+" limit: "+limit);
        return limit+1;
    }

    public  class NewSongViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout RelativeLayout_item;
        TextView txt_song_name;
        TextView txt_song_singer_name;
        ImageView btnMore;
        ImageView imgSong;
        public NewSongViewHolder(@NonNull View itemView) {
            super(itemView);
            imgSong=itemView.findViewById(R.id.img_item);
            btnMore=itemView.findViewById(R.id.img_more);
            RelativeLayout_item=itemView.findViewById(R.id.RelativeLayout_item);
            txt_song_name=itemView.findViewById(R.id.txt_song_name);
            txt_song_singer_name=itemView.findViewById(R.id.txt_singer_name);
        }
    }
    public  class NewSameSongViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout RelativeLayout_item;
        TextView txt_song_name;
        TextView txt_song_singer_name;
        ImageView btnMore;
        ImageView btnAddtoPlaylist;
        ImageView imgSong;
        public NewSameSongViewHolder(@NonNull View itemView) {
            super(itemView);
            imgSong=itemView.findViewById(R.id.img_item);
            btnMore=itemView.findViewById(R.id.img_more);
            RelativeLayout_item=itemView.findViewById(R.id.RelativeLayout_item);
            txt_song_name=itemView.findViewById(R.id.txt_song_name);
            txt_song_singer_name=itemView.findViewById(R.id.txt_singer_name);
            btnAddtoPlaylist=itemView.findViewById(R.id.img_addToPlaylist);
        }
    }
    class LoadingViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;


        public LoadingViewHolder(View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progressBar);
        }
    }
    public  class ButtonViewHolder extends RecyclerView.ViewHolder {
        LinearLayout linearLayout;
        public ButtonViewHolder(@NonNull View itemView) {
            super(itemView);
            linearLayout=itemView.findViewById(R.id.LinearLayout_more);
        }
    }
    private void addSongToPlaylist(Song song,String idPlaylist,String idSong,int type) {
        //PlaylistSong playlistSong=new PlaylistSong(idSong,idPlaylist);
        ////Database.getInstance(mContext).playlistSongDAO().insertPlaylistSong(playlistSong);
        //co le phai tao playlistSong because playlist song n-n
        Call<Integer> call =APIService.getService().createPlaylistSong(Integer.parseInt(idPlaylist),Integer.parseInt(idSong),type);
        call.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                int id=response.body();
                if(id!=-1) {
                    Toast.makeText(mContext, "Thêm vào playlist thành công!", Toast.LENGTH_SHORT).show();
                    downloadMp3FromInternet(song);
                }
                else Toast.makeText(mContext, "Lỗi, không thêm được!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                Toast.makeText(mContext, "Lỗi, "+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
    private void downloadMp3FromInternet(Song song) {
        HelpTools.DownloadingTask dlT=new HelpTools.DownloadingTask(mContext);
        dlT.setSong(song);
        dlT.execute();
    }
}
