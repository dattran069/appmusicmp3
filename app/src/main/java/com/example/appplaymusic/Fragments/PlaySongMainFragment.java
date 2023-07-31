package com.example.appplaymusic.Fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.bumptech.glide.Glide;
import com.example.appplaymusic.Commons.Common;
import com.example.appplaymusic.Helper.HelpTools;
import com.example.appplaymusic.IfragmentPlaySong;
import com.example.appplaymusic.MainActivity.MainActivity;
import com.example.appplaymusic.Models.Song;
import com.example.appplaymusic.PlaySongActivity;
import com.example.appplaymusic.R;
import com.example.appplaymusic.Services.PlayMusicService;

import de.hdodenhof.circleimageview.CircleImageView;


public class PlaySongMainFragment extends Fragment implements IfragmentPlaySong {
    static Context mContext;
    public void update(Song song){
        Glide.with(mContext).load(song.getSongImage()).into(circleImageView);
    }
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private CircleImageView circleImageView;
    private PlaySongActivity playSongActivity;
    public PlaySongMainFragment(Context mContext) {
        // Required empty public constru0ctor
    this.mContext=mContext;
    }
    public PlaySongMainFragment() {
        // Required empty public constru0ctor
        this.mContext=mContext;
    }
    void setFragmentResultListener(String key) {

    }
    BroadcastReceiver broadcastReceiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle=intent.getExtras();
//            //isPlaying= (Boolean) bundle.getBoolean("is_playing");
//            Song song= (com.example.appplaymusic.Models.Song) bundle.get("song_obj");
//            Glide.with(playSongActivity.getApplicationContext()).load(song.getSongImage()).into(circleImageView);
            int action = bundle.getInt("action");
            update(Common.getCurrentSong());
            if(action== PlayMusicService.ACTION_PAUSE||action==PlayMusicService.ACTION_CLEAR){
                stopAnimation();
            } else
                if(action== PlayMusicService.ACTION_START){
                    startAnimation();
                }
            else if(action== PlayMusicService.ACTION_RESUME){
                startAnimation();
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    private void startAnimation() {
        if(PlayMusicService.isPlaying()){
            Runnable runnable=new Runnable() {
                @Override
                public void run() {
                    circleImageView.animate().rotationBy(360).withEndAction(this).setDuration(35000)
                            .setInterpolator(new LinearInterpolator()).start();
                }
            };
            circleImageView.animate().rotationBy(360).withEndAction(runnable).setDuration(35000)
                    .setInterpolator(new LinearInterpolator()).start();
        }
    }
    private void stopAnimation() {
        circleImageView.animate().cancel();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        startAnimation();
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        playSongActivity= (PlaySongActivity) getActivity();
        View view= inflater.inflate(R.layout.fragment_play_song_main, container, false);
        circleImageView=view.findViewById(R.id.img_circle_song);
        //if(PlaySongActivity.getSong().getType()==1)
            //circleImageView.setImageBitmap(HelpTools.getBitmapImgSongLocal(playSongActivity.getSong().getSongLink()));
        //elses
        Glide.with(playSongActivity.getApplicationContext()).load(Common.getCurrentSong().getSongImage()).into(circleImageView);
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(broadcastReceiver,new IntentFilter("send_data_to_activity"));
        return  view;
    }

    @Override
    public void onDestroy() {
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(broadcastReceiver);
        super.onDestroy();
    }
}