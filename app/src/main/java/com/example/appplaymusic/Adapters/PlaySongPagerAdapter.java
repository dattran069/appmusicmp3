package com.example.appplaymusic.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.bumptech.glide.Glide;
import com.example.appplaymusic.Fragments.PlaySongLyricFragment;
import com.example.appplaymusic.Fragments.PlaySongMainFragment;
import com.example.appplaymusic.Fragments.PlaySongPlaylistFragment;
import com.example.appplaymusic.Playlist;
import com.example.appplaymusic.R;

import java.util.HashMap;
import java.util.Map;

public class PlaySongPagerAdapter extends FragmentStateAdapter {
    private  Context mContext;
    private static int NUM_ITEMS = 3;
    private Map<Integer, String> mFragmentTags;
    public PlaySongPagerAdapter(@NonNull FragmentActivity fragmentActivity) {

        super(fragmentActivity);
    }
    public void setContext(Context mContext){this.mContext=mContext;};
//    private boolean isRunning=false;
//    public  void setRunning(boolean isRun){this.isRunning=isRun;}

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return  new PlaySongPlaylistFragment();
            case 1:
                return  new PlaySongMainFragment(mContext);
            case 2:
                return  new PlaySongLyricFragment(mContext);
        }
        return  new PlaySongMainFragment(mContext);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return 3;
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
