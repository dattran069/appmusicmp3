package com.example.appplaymusic.Adapters;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.appplaymusic.Fragments.CaNhanTBHFragment;
import com.example.appplaymusic.Fragments.OnlineTBHFragment;
import com.example.appplaymusic.Fragments.PlaySongLyricFragment;
import com.example.appplaymusic.Fragments.PlaySongMainFragment;
import com.example.appplaymusic.Fragments.PlaySongPlaylistFragment;
import com.example.appplaymusic.Models.Playlist;
import com.example.appplaymusic.Models.Song;

import java.util.List;
import java.util.Map;

public class ThemBaiHatPagerAdapter extends FragmentStateAdapter {
    private  Context mContext;
    private static int NUM_ITEMS = 3;
    private Map<Integer, String> mFragmentTags;
    private Playlist playlist;
    private List<Song> songList;

    public List<Song> getSongList() {
        return songList;
    }

    public void setSongList(List<Song> songList) {
        this.songList = songList;
    }

    public ThemBaiHatPagerAdapter(@NonNull FragmentActivity fragmentActivity) {

        super(fragmentActivity);
    }
    public void setContext(Context mContext){this.mContext=mContext;};


    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return  new OnlineTBHFragment(mContext);
            case 1:
                return  new CaNhanTBHFragment(mContext,songList);
        }
        return  new OnlineTBHFragment();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return 2;
    }

    public void setPlaylist(Playlist playlist) {
        this.playlist=playlist;
    }


//    @NonNull
//    @Override
//    public Fragment getItem(int position) {
//       switch (position){
//           case 0:
//               return new PlaySongPlaylistFragment();
//           case 1:
//               return new PlaySongMainFragment(mContext);
//           case 2:
//               return new PlaySongMainFragment(mContext);
//           default:  return new PlaySongMainFragment(mContext);
//
//       }
//    }
//    public  void updateFragment(int position){
//        String tag = mFragmentTags.get(position);
//        if (tag != null) {
//            switch (position){
//                case 0:
//                    PlaySongMainFragment playSongMainFragment= (PlaySongMainFragment) mFragmentManager.findFragmentByTag(tag);
//                    playSongMainFragment.update();
//                    break;
//                case 1:
//                    Log.d("PlaySongMainFragment","updateFragment 1");
//                    ((PlaySongMainFragment)mFragmentManager.findFragmentByTag(tag)).update();
//                    break;
//                case 2:
//                    Log.d("PlaySongMainFragment","updateFragment 2");
//
//                    ((PlaySongMainFragment)mFragmentManager.findFragmentByTag(tag)).update();
//                    break;
//            }
//        }
//    }
//    @NonNull
//    @Override
//    public Object instantiateItem(@NonNull View container, int position) {
//        Log.d("instantiateItem", String.valueOf(position));
//        Object object = super.instantiateItem(container, position);
//        if (object instanceof Fragment) {
//            Fragment fragment = (Fragment) object;
//            String tag = fragment.getTag();
//            mFragmentTags.put(position, tag);
//        }
//        return object;
//    }
//
//    @Override
//    public int getCount() {
//        return 3;
//    }
//
//    public Fragment getFragment(int position) {
//        Fragment fragment = null;
//        String tag = mFragmentTags.get(position);
//        if (tag != null) {
//            fragment = mFragmentManager.findFragmentByTag(tag);
//        }
//        return fragment;
//    }
}
