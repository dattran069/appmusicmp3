package com.example.appplaymusic.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.example.appplaymusic.Photo;
import com.example.appplaymusic.R;

import java.util.List;

public class SliderImageAdpater extends PagerAdapter {
    private Context mContext;
    private List<Photo> photoList;
    public void setData(List<Photo> photoList,Context mContext){
        this.mContext=mContext;
        this.photoList=photoList;
        notifyDataSetChanged();
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view= LayoutInflater.from(container.getContext()).inflate(R.layout.layout_slider_item,container,false);
        Photo photo=this.photoList.get(position);
        ImageView imageView=view.findViewById(R.id.img_view_silder);
        if(photo!=null) Glide.with(mContext).load(photo.getIdRes()).into(imageView);
        container.addView(view);
        return view;
    }

    @Override
    public int getCount() {
        return photoList==null?0:this.photoList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view==object;
    }
}
