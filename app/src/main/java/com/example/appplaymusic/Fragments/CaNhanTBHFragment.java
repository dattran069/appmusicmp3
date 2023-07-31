package com.example.appplaymusic.Fragments;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.appplaymusic.Adapters.SongAddAdapter;
import com.example.appplaymusic.Adapters.ThemBaiHatPagerAdapter;
import com.example.appplaymusic.Commons.Common;
import com.example.appplaymusic.Models.Song;
import com.example.appplaymusic.R;
import com.example.appplaymusic.Services.PlayMusicService;
import com.example.appplaymusic.ThemBaihatActivity.ThemBaiHatActivity;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CaNhanTBHFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CaNhanTBHFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private  Context mContext;
    private List<Song> songList;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CaNhanTBHFragment() {
        // Required empty public constructor
    }

    public CaNhanTBHFragment(Context mContext,List<Song> songList) {
        this.mContext=mContext;
        this.songList=songList;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CaNhanTBHFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CaNhanTBHFragment newInstance(String param1, String param2) {
        CaNhanTBHFragment fragment = new CaNhanTBHFragment();
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

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView=view.findViewById(R.id.RecyclerViewOfflineSongs);
        songAddAdapter=new SongAddAdapter();
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(songAddAdapter);
        songAddAdapter.setData(ThemBaiHatActivity.getPlaylist(),1,getAllSongOffline(),songList,mContext,getActivity());
    }

    private List<Song> getAllSongOffline() {
        List<Song> songList=new ArrayList<>();
        ContentResolver contentResolver;
        contentResolver = mContext.getContentResolver();
        Cursor cursor;
//        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

        File apkStorage = new File(
                Environment.getExternalStorageDirectory() + "/"
                        + Common.DIRECTORY_SAVE_MP3);
        String pattern = Pattern.quote(apkStorage.getAbsolutePath()) + "/[^/]*";
        Uri uri = MediaStore.Audio.Media.getContentUriForPath(Environment.getExternalStorageDirectory().getPath());

        String[] projection = {
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.DATA,    // filepath of the audio file
                MediaStore.Audio.Media._ID,     // context id/ uri id of the file
                MediaStore.Audio.Media.DISPLAY_NAME,
                MediaStore.Audio.Playlists.ALBUM
        };
        cursor = contentResolver.query(
                uri, // Uri
                projection,
                null,
                null,
                null
        );

        if (cursor == null) {


        } else if (!cursor.moveToFirst()) {


        }
        else {


            do {
                String filepath = cursor.getString(4);
                if(filepath.matches(pattern)){
                    String queryTtile = cursor.getString(0);
                    String queryArtist = cursor.getString(1);
                    String queryAlbum = cursor.getString(2);
                    String fakeId = cursor.getString(7);

                    songList.add(new Song(fakeId,queryTtile,queryArtist,queryAlbum,filepath,1));
                    Log.d("GetAllMediaMp3FilesPath",filepath);
                    Log.d("GetAllMediaMp3FilesDis",cursor.getString(6));
                    Log.d("GetAllMediaMp3FilesFID",cursor.getString(7));
                }

                Log.d("filepath",filepath);

            } while (cursor.moveToNext());
        }
        Log.d("GetAllMediaMp3Files", String.valueOf(songList.size()));
    return songList;
    }

    RecyclerView recyclerView;
    SongAddAdapter songAddAdapter;
    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //LocalBroadcastManager.getInstance(getActivity()).registerReceiver(broadcastReceiver,new IntentFilter("send_listSong"));
        return inflater.inflate(R.layout.fragment_ca_nhan_t_b_h, container, false);
    }

}