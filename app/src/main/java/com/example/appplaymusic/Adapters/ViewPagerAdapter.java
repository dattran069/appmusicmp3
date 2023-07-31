package com.example.appplaymusic.Adapters;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.appplaymusic.Fragments.CanhanFragment.CanhanFragment;
import com.example.appplaymusic.Fragments.KhamphaFragment;
import com.example.appplaymusic.Fragments.TheodoiFragment;
import com.example.appplaymusic.Fragments.XuhuongFragment;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    private  List<Fragment> mFragmentList = new ArrayList<>();
    public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
        mFragmentList.add(new CanhanFragment());
        mFragmentList.add(new KhamphaFragment());
        mFragmentList.add(new XuhuongFragment());
        mFragmentList.add(new TheodoiFragment());
    }
    public void replaceFragment(Fragment fragment,int pos){
        Log.d("ViewPagerAdapter","replaceFragment");
        mFragmentList.remove(pos);
        mFragmentList.add(pos,fragment);
        notifyDataSetChanged();
//        mFragmentList.get(pos).onResume();
    }
    @Override
    public int getItemPosition(Object object) {
        // Causes adapter to reload all Fragments when
        // notifyDataSetChanged is called
        return POSITION_NONE;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return  mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return 4;
    }
}
