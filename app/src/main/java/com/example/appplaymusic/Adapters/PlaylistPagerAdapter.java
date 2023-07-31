package com.example.appplaymusic.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.example.appplaymusic.Commons.Common;
import com.example.appplaymusic.Fragments.PlaylistFragment.PlaylistFragment;
import com.example.appplaymusic.Helper.HelpTools;
import com.example.appplaymusic.MainActivity.MainActivity;
import com.example.appplaymusic.Models.Playlist;
import com.example.appplaymusic.Models.Song;
import com.example.appplaymusic.Photo;
import com.example.appplaymusic.R;
import com.example.appplaymusic.Services.APIService;
import com.example.appplaymusic.Services.DataService;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlaylistPagerAdapter extends PagerAdapter {
    private Playlist playlist;
    private Context mContext;
    private int numSongs=0;
    public PlaylistPagerAdapter(com.example.appplaymusic.Models.Playlist playlist, Context context) {
        this.playlist=playlist;
        mContext=context;
    }
    private List<com.example.appplaymusic.Models.Song> songList;
    public  void setData(List<com.example.appplaymusic.Models.Song> songList,int numSongs){this.songList=songList;
        this.numSongs=numSongs;
    notifyDataSetChanged();}
    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = null;
        if(position==0){
            view= LayoutInflater.from(container.getContext()).inflate(R.layout.fragment_playlist_front,container,false);
            ImageView imageView=view.findViewById(R.id.img_playlist);
            if(!PlaylistFragment.isLocal()&&playlist!=null) Glide.with(mContext).load(playlist.getImage()).into(imageView);
            else  if(songList!=null) {
                Log.d("dothis","ok");
                //Bitmap bitmap=getFirstSongImage();
                //imageView.setImageBitmap(bitmap);
                imageView.setImageBitmap(HelpTools.getBitmapImgSongLocal(songList.get(0),mContext));
            }
            TextView txt_playlist_name=view.findViewById(R.id.txt_playlist_name);
            txt_playlist_name.setText(playlist!=null?playlist.getNamePlaylist():"Unknown playlist");
            setSongNums(view);
        }else{
            view= LayoutInflater.from(container.getContext()).inflate(R.layout.fragment_playlist_behind,container,false);
            TextView mota=view.findViewById(R.id.txt_playlist_mo_ta);
            mota.setText("chua co mo ta");
        }
        container.addView(view);
        return view;
    }
//    public  Bitmap getFirstSongImage() {
//        List<Song> songList2= Common.getSongList();
//        Bitmap bitmap=null;
//        int id=0;
//        if(songList2.size()==0) return null;
//        do{
//            bitmap= HelpTools.getBitmapImgSongLocal(songList2.get(id).getSongLink());
//            id++;
//            Log.d("idImagePlaylist", (bitmap==null?"bitmapnull":"bitmapNotnull")+songList2.get(id-1).getSongImage());
//        } while(bitmap==null&&id<songList2.size());
//        Log.d("idImagePlaylist", String.valueOf(id));
//        return bitmap;
//    }
    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view==object;
    }
    public int getSongNums(){
        int num=0;
        DataService dataService= APIService.getService();
        Call<String> getNumSongCallback= dataService.countSongByPlaylistId(Integer.parseInt(playlist.getIdPlaylist()));
        try {
            Response<String> response=getNumSongCallback.execute();
            num= Integer.parseInt(response.body());
        } catch (IOException e) {
            e.printStackTrace();
        }
return  num;
    }
    private void setSongNums(View view) {
        TextView txt_playlist_num_songs=view.findViewById(R.id.txt_total_songs);
           txt_playlist_num_songs.setText(String.valueOf(this.numSongs));
            Log.d("setSongNums", String.valueOf(this.numSongs));
    }

    public void setNumSongs(int numSongs) {
    this.numSongs=numSongs;
    }

}
