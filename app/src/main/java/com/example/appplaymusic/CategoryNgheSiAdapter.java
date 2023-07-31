package com.example.appplaymusic;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appplaymusic.Adapters.NewSongAdapter;
import com.example.appplaymusic.Adapters.PlaylistAdapter;
import com.example.appplaymusic.Adapters.SingerAdapter;
import com.example.appplaymusic.Commons.Common;
import com.example.appplaymusic.MainActivity.MainActivity;
import com.example.appplaymusic.Models.Playlist;
import com.example.appplaymusic.Models.Singer;
import com.example.appplaymusic.Models.Song;
import com.example.appplaymusic.Services.APIService;
import com.example.appplaymusic.Services.DataService;
import com.example.appplaymusic.SingerActivity.SingerActivity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryNgheSiAdapter extends RecyclerView.Adapter<CategoryNgheSiAdapter.CategoryViewHolder> {
    private static final int TYPE_ITEM = 1;
    private static final int TYPE_BUTTON = 2;
    private static final int TYPE_LIST_VERTICAL = 4;
    private List<Category> categoryList;
    private Context mContext;
    private Map<Integer,Boolean> loadContentCate;
    private Map<Integer,Boolean> loading;
    private int singerCode;
    public interface ICSonglickListener {
        public void onSongClickListener(Song song,int pos,List<Song> songList,Playlist playlist);
        public void onPlaylistClickListener(Playlist playlist);
        public void onSingerClickListener(String id,Singer singer);
        public void onXemThemClickListener(int categoryId,NewSongAdapter newSongAdapter,PlaylistAdapter playlistAdapter);
    }

    ICSonglickListener icSonglickListener;

    public ICSonglickListener getIcSonglickListener() {
        return icSonglickListener;
    }

    public void setIcSonglickListener(ICSonglickListener icSonglickListener) {
        this.icSonglickListener = icSonglickListener;
    }

    public void setData(List<Category> categoryList, Context mContext,int singerCode) {
        this.mContext = mContext;
        this.singerCode=singerCode;
        this.categoryList = categoryList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return this.categoryList.get(position).getShowType();
    }


    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        loadContentCate=new HashMap<>();
        loading=new HashMap<>();
        View itemView=null;
        itemView= LayoutInflater.
                        from(parent.getContext()).
                        inflate(R.layout.layout_category, parent, false);
        return  new CategoryViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        Log.d("bindcategory","position: "+position);
        Category category = this.categoryList.get(position);
        DataService dataService = APIService.getService();
        CategoryViewHolder categoryHoriViewHolder=(CategoryViewHolder) holder;
        if (category == null) return;
        categoryHoriViewHolder.categoryName.setText(category.getName());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
        categoryHoriViewHolder.rcv_songs.setLayoutManager(linearLayoutManager);
        PlaylistAdapter playlistAdapter = new PlaylistAdapter(mContext);
        SingerAdapter singerAdapter=new SingerAdapter(Common.TYPE_VERTICAL);
        if(category.getCategory_code()!=Common.CATEGORY_NGHESI_COTHEBANSETHICH){
            categoryHoriViewHolder.rcv_songs.setAdapter(playlistAdapter);
            playlistAdapter.setData(category.getPlaylists());
            playlistAdapter.setiClickListener(playlist -> {
                icSonglickListener.onPlaylistClickListener(playlist);
            });

        }
        else {
            categoryHoriViewHolder.rcv_songs.setAdapter(singerAdapter);
            if(category.getSingers()!=null){
                singerAdapter.setData(category.getSingers(), mContext, new SingerAdapter.IonClick() {
                    @Override
                    public void onClick(String id, Singer singer) {
                        icSonglickListener.onSingerClickListener(id,singer);
                    }
                });
            }
        }
        categoryHoriViewHolder.xemThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(categoryHoriViewHolder.rcv_songs.getAdapter() instanceof  NewSongAdapter)
                icSonglickListener.onXemThemClickListener(category.getCategory_code(), (NewSongAdapter) categoryHoriViewHolder.rcv_songs.getAdapter(),null);
                else icSonglickListener.onXemThemClickListener(category.getCategory_code(),null, (PlaylistAdapter) categoryHoriViewHolder.rcv_songs.getAdapter());
            }
        });


        switch (holder.getItemViewType()){
            case Common.TYPE_HORI:
                switch (category.getCategory_code()){
                    case Common.CATEGORY_NGHESI_BAIHATNOIBAT:
                        //cac bai hat cua th nay
                        if(loading.get(category.getCategory_code())!=null) {
                            Log.d("Category","onbind+ "+category.getCategory_code()+" Load roi`");
                            return;
                        }

                        Call<List<Song>> songCallBack= dataService.getSongsBySingerId(singerCode);
                        categoryHoriViewHolder.categoryName.setText(category.getName());
                        if(loadContentCate.get(category.getCategory_code())==null&&loading.get(category.getCategory_code())==null)
                        {
                            //Log.d("Category",category.getName()+" chua load h load`");
                            holder.spinner.setVisibility(View.VISIBLE);
                            holder.spinner.getProgress();
                            loading.put(category.getCategory_code(),true);
                            loadContentCate.put(category.getCategory_code(),true);
                            Log.d("Category","onbind+ "+category.getCategory_code()+" chua load h load ne`");
                            songCallBack.enqueue(new Callback<List<Song>>() {
                                @Override
                                public void onResponse(Call<List<Song>> call, Response<List<Song>> response) {
                                    holder.spinner.setVisibility(View.GONE);
                                    loading.remove(category.getCategory_code());
                                    List<Song> songlist=response.body();
                                    category.setSongList(songlist);
                                    Log.d("Category",category.getCategory_code()+" load ok, set loading false");
                                    notifyDataSetChanged();
                                    playlistAdapter.setData(category.getPlaylists());
                                }

                                @Override
                                public void onFailure(Call<List<Song>> call, Throwable t) {
                                    Log.d("Category",category.getCategory_code()+" load failed");
                                    holder.spinner.setVisibility(View.GONE);
                                    loading.remove(category.getCategory_code());
                                    loadContentCate.remove(category.getCategory_code());
                                }
                            });
                        }
                        break;
                    case Common.CATEGORY_NGHESI_COTHEBANSETHICH:
                        //Cac nghe si khac
                        Log.d("CategoryThich","ok");
                        categoryHoriViewHolder.rcv_songs.setAdapter(singerAdapter);
                        if(loading.get(category.getCategory_code())!=null) {
                            Log.d("Category","onbind+ "+category.getCategory_code()+" Load roi`");
                            return;
                        }
                        Call<List<Singer>> singerCallBack= dataService.getSingersExceptSingerId(singerCode);
                        categoryHoriViewHolder.categoryName.setText(category.getName());

                        if(loadContentCate.get(category.getCategory_code())==null&&loading.get(category.getCategory_code())==null)
                        {
                            categoryHoriViewHolder.xemThem.setVisibility(View.GONE);
                            //Log.d("Category",category.getName()+" chua load h load`");
                            holder.spinner.setVisibility(View.VISIBLE);
                            holder.spinner.getProgress();
                            loading.put(category.getCategory_code(),true);
                            loadContentCate.put(category.getCategory_code(),true);
                            Log.d("Category","onbind+ "+category.getCategory_code()+" chua load h load ne`");
                            singerCallBack.enqueue(new Callback<List<Singer>>() {
                                @Override
                                public void onResponse(Call<List<Singer>> call, Response<List<Singer>> response) {
                                    holder.spinner.setVisibility(View.GONE);
                                    loading.remove(category.getCategory_code());
                                    List<Singer> singers=response.body();
                                    LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(mContext);
                                    holder.rcv_songs.setAdapter(singerAdapter);
                                    holder.rcv_songs.setLayoutManager(linearLayoutManager2);
                                    categoryList.get(position).setSingers(singers);
                                    Log.d("Category",category.getCategory_code()+" load ok, set loading false");
                                    notifyDataSetChanged();
                                    singerAdapter.setData(categoryList.get(position).getSingers(), mContext, new SingerAdapter.IonClick() {
                                        @Override
                                        public void onClick(String id, Singer singer) {
                                            Intent intent =new Intent(mContext, SingerActivity.class);
                                            Bundle bundle=new Bundle();
                                            bundle.putSerializable("singer_obj",singer);
                                            intent.putExtras(bundle);
                                            mContext.startActivity(intent);
                                        }
                                    });
                                    Log.d("CategorySinger",category.getCategory_code()+" load ok size:"+singers.size());
                                    categoryHoriViewHolder.xemThem.setVisibility(View.VISIBLE);
                                }

                                @Override
                                public void onFailure(Call<List<Singer>> call, Throwable t) {
                                    Log.d("Category",category.getCategory_code()+" load failed");
                                    holder.spinner.setVisibility(View.GONE);
                                    loading.remove(category.getCategory_code());
                                    loadContentCate.remove(category.getCategory_code());
                                }
                            });
                        }
                        break;
                    case Common.CATEGORY_NGHESI_PLAYLIST:
                        //Cac playlist cua th nay
                        if(loading.get(category.getCategory_code())!=null) {
                            Log.d("Category","onbind+ "+category.getCategory_code()+" Load roi`");
                            return;
                        }
                        Call<List<Playlist>> playlistCallBack= dataService.getAllPlaylistBySingerId(singerCode);
                        categoryHoriViewHolder.categoryName.setText(category.getName());

                        if(loadContentCate.get(category.getCategory_code())==null&&loading.get(category.getCategory_code())==null)
                        {
                            categoryHoriViewHolder.xemThem.setVisibility(View.GONE);
                            //Log.d("Category",category.getName()+" chua load h load`");
                            holder.spinner.setVisibility(View.VISIBLE);
                            holder.spinner.getProgress();
                            loading.put(category.getCategory_code(),true);
                            loadContentCate.put(category.getCategory_code(),true);
                            Log.d("Category","onbind+ "+category.getCategory_code()+" chua load h load ne`");
                            playlistCallBack.enqueue(new Callback<List<Playlist>>() {
                                @Override
                                public void onResponse(Call<List<Playlist>> call, Response<List<Playlist>> response) {
                                    holder.spinner.setVisibility(View.GONE);
                                    loading.remove(category.getCategory_code());
                                    List<Playlist> playlists1=response.body();
                                    categoryList.get(position).setPlayList(playlists1);
                                    Log.d("Category",category.getCategory_code()+" load ok, set loading false");
                                    notifyDataSetChanged();
                                    playlistAdapter.setData(categoryList.get(position).getPlaylists());
                                    playlistAdapter.setiClickListener(new PlaylistAdapter.IClickListener() {
                                        @Override
                                        public void onClickListener(Playlist playlist) {

                                        }
                                    });
                                    categoryHoriViewHolder.xemThem.setVisibility(View.VISIBLE);
                                }

                                @Override
                                public void onFailure(Call<List<Playlist>> call, Throwable t) {
                                    Log.d("Category",category.getCategory_code()+" load failed");
                                    holder.spinner.setVisibility(View.GONE);
                                    loading.remove(category.getCategory_code());
                                    loadContentCate.remove(category.getCategory_code());
                                }
                            });
                        }
                        break;
                    case Common.CATEGORY_NGHESI_XUATHIENTRONG:
                        //Cac playlist th nay co mat
                        break;
                }

                break;
            case Common.TYPE_VERTICAL:
                switch (category.getCategory_code()){
                    case Common.CATEGORY_NGHESI_BAIHATNOIBAT:
                        Log.d("voday","ok");
                        //cac bai hat cua th nay
                        CategoryViewHolder categoryVerViewHolder=(CategoryViewHolder) holder;
                        categoryVerViewHolder.xemThem.setVisibility(View.GONE);
                        //Log.d("category hover",categoryVerViewHolder.toString());
                        Call<List<Song>> songCallBack= dataService.getSongsBySingerId(singerCode);
                        songCallBack.enqueue(new Callback<List<Song>>() {
                            @Override
                            public void onResponse(Call<List<Song>> call, Response<List<Song>> response) {
                                List<Song> songList=response.body();
                                // category.setPlayList(playlists1);
                                //notifyDataSetChanged();
                                if (category == null) return;
                                categoryVerViewHolder.categoryName.setText(category.getName());
                                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
                                categoryVerViewHolder.rcv_songs.setLayoutManager(linearLayoutManager);
                                //PlaylistAdapter playlistAdapter = new PlaylistAdapter(mContext);
                                NewSongAdapter newSongAdapter=new NewSongAdapter();
                                categoryVerViewHolder.rcv_songs.setAdapter(newSongAdapter);
                                com.example.appplaymusic.DividerItemDecoration dividerItemDecoration = new com.example.appplaymusic.DividerItemDecoration(mContext, androidx.recyclerview.widget.DividerItemDecoration.VERTICAL,false);
                                categoryVerViewHolder.rcv_songs.addItemDecoration(dividerItemDecoration);
                                newSongAdapter.setData(songList,mContext);
                                newSongAdapter.setiClickListener(new NewSongAdapter.IClickListener() {
                                    @Override
                                    public void onClickListener(Song song, int position) {
                                        icSonglickListener.onSongClickListener(song, holder.getAdapterPosition(),songList,null);
                                    }

                                    @Override
                                    public void onClickMoreListener(ImageView imageView, String idSong) {

                                    }

                                    @Override
                                    public void onClickAddPlaylistListener(Song song, int position) {

                                    }

                                    @Override
                                    public void onClickRemovePlaylistListener(Song song, int position) {

                                    }

                                    @Override
                                    public void onClickXemThemListener(Song song, int position) {
                                        icSonglickListener.onXemThemClickListener(category.getCategory_code(),newSongAdapter,null);
                                    }
                                });
                            }

                            @Override
                            public void onFailure(Call<List<Song>> call, Throwable t) {

                            }
                        });
                        break;
                    case Common.CATEGORY_NGHESI_COTHEBANSETHICH:
                        //Cac nghe si khac
                        break;
                    case Common.CATEGORY_NGHESI_PLAYLIST:
                        //Cac playlist cua th nay
                        break;
                    case Common.CATEGORY_NGHESI_XUATHIENTRONG:
                        //Cac playlist th nay co mat
                        break;
                }




                //Log.d("category",category.toString());
                //Log.d("category pos", String.valueOf(position));

                break;
        }
    }


    @Override
    public int getItemCount() {
        if(this.categoryList!=null) return this.categoryList.size();
        return 0;
    }

    public  class CategoryViewHolder extends RecyclerView.ViewHolder {
        private ProgressBar spinner;

        @Override
        public String toString() {
            return "CategoryViewHolder{" +
                    "categoryName=" + categoryName +
                    '}';
        }

        TextView categoryName;
        RecyclerView rcv_songs;
        LinearLayout xemThem;
        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            rcv_songs=itemView.findViewById(R.id.rcv_songs);
            categoryName=itemView.findViewById(R.id.txv_name_category);
            spinner = itemView.findViewById(R.id.ProgressBar01);
            xemThem=itemView.findViewById(R.id.txt_xem_them);
        }
    }
}
