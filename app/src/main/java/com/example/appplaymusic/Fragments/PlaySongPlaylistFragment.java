package com.example.appplaymusic.Fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.appplaymusic.Adapters.NewSongAdapter;
import com.example.appplaymusic.Commons.Common;
import com.example.appplaymusic.Helper.HelpTools;
import com.example.appplaymusic.IfragmentPlaySong;
import com.example.appplaymusic.MainActivity.MainActivity;
import com.example.appplaymusic.Models.Song;
import com.example.appplaymusic.PlaySongActivity;
import com.example.appplaymusic.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PlaySongPlaylistFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PlaySongPlaylistFragment extends Fragment implements IfragmentPlaySong {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private ViewPager viewPager;
    private NewSongAdapter newSongAdapter;
    private RecyclerView recyclerViewSameSong;
    private PlaySongActivity playSongActivity;
    private TextView txtSingerName;
    private TextView txtSongName;
    private TextView txtPlaylistName;



    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PlaySongPlaylistFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PlaySongPlaylistFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PlaySongPlaylistFragment newInstance(String param1, String param2) {
        PlaySongPlaylistFragment fragment = new PlaySongPlaylistFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    BroadcastReceiver broadcastReceiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle=intent.getExtras();
            //isPlaying= (Boolean) bundle.getBoolean("is_playing");
            Song song= (com.example.appplaymusic.Models.Song) bundle.get("song_obj");
            //int action = bundle.getInt("action");
            txtSongName.setText(song.getSongName());
            int index=bundle.getInt("currentSongIndex");
            if(newSongAdapter!=null) newSongAdapter.updateSongCurrent();
            txtSingerName.setText(song.getSingerName());

        }
    };
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        playSongActivity= (PlaySongActivity) getActivity();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewPager=view.findViewById(R.id.viewPager);
        recyclerViewSameSong=view.findViewById(R.id.RecyclerViewSameSong);
        newSongAdapter=new NewSongAdapter();
        txtSingerName=view.findViewById(R.id.txt_song_singer);
        txtSongName=view.findViewById(R.id.txt_song_name);
        txtPlaylistName=view.findViewById(R.id.txt_song_playlist);
        newSongAdapter.setData(Common.getSongList(),getContext(),false,true);
        if(Common.getSongList()!=null) {
            Log.d("Songlistsize: ", String.valueOf(Common.getSongList().size()));
            Common.getSongList().forEach(song -> {Log.d("Song "+song.getIdSong(),song.getSongName());});
            Song song=Common.getCurrentSong();
            txtSongName.setText(song.getSongName());
            if(Common.getPlaylist()!=null) txtPlaylistName.setText(Common.getPlaylist().getNamePlaylist());
            txtSingerName.setText(song.getSingerName());
        }

        recyclerViewSameSong.setAdapter(newSongAdapter);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(playSongActivity);
        recyclerViewSameSong.setLayoutManager(linearLayoutManager);
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(broadcastReceiver,new IntentFilter("send_data_to_activity"));
        newSongAdapter.setiClickListener(new NewSongAdapter.IClickListener() {
            @Override
            public void onClickListener(Song song, int position) {
                Common.setCurrentIndex(position);
                HelpTools.startServiceNewSong(song,getActivity());
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

            }
        });
    }

    View view;
    RecyclerView recyclerView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_play_song_playlist, container, false);
        return view;
    }

    @Override
    public void update(Song song) {
        Log.d("PlaySongPlaylist","update");
    }
}