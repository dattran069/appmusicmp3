package com.example.appplaymusic.Fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.bumptech.glide.Glide;
import com.example.appplaymusic.Commons.Common;
import com.example.appplaymusic.IfragmentPlaySong;
import com.example.appplaymusic.MainActivity.MainActivity;
import com.example.appplaymusic.Models.Song;
import com.example.appplaymusic.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PlaySongLyricFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PlaySongLyricFragment extends Fragment implements IfragmentPlaySong {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    TextView txtLoiBaiHat;
    Context mContext;
    public PlaySongLyricFragment(Context mContext) {
        this.mContext=mContext;
    }
    public PlaySongLyricFragment() {
        // Required empty public constructor
    }

    public static PlaySongLyricFragment newInstance(String param1, String param2) {
        PlaySongLyricFragment fragment = new PlaySongLyricFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }
    BroadcastReceiver broadcastReceiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle=intent.getExtras();
            //isPlaying= (Boolean) bundle.getBoolean("is_playing");
            //song= (com.example.appplaymusic.Models.Song) bundle.get("song_obj");
            //int action = bundle.getInt("action");
            update(Common.getCurrentSong());
        }
    };
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_play_song_lyric, container, false);
        txtLoiBaiHat=view.findViewById(R.id.txt_current_lyric);
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(broadcastReceiver,new IntentFilter("send_data_to_activity"));
        update(null);
        return view;
    }

    public void update(Song song){
        txtLoiBaiHat.setText(Common.getCurrentSong().getSongName());
    }
}