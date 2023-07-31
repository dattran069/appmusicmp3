package com.example.appplaymusic.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appplaymusic.Adapters.NewSongAdapter;
import com.example.appplaymusic.Adapters.SongAddAdapter;
import com.example.appplaymusic.MainActivity.MainActivity;
import com.example.appplaymusic.Models.Playlist;
import com.example.appplaymusic.Models.Song;
import com.example.appplaymusic.R;
import com.example.appplaymusic.ThemBaihatActivity.ThemBaiHatActivity;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OnlineTBHFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OnlineTBHFragment extends Fragment implements OnlineTBHFragmentView{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private  Context mContext;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    static PlaylistData playlistData;
    public OnlineTBHFragment() {
        // Required empty public constructor
    }
    public class PlaylistData{
        public Playlist getPlaylist() {
            return playlist;
        }

        public void setPlaylist(Playlist playlist) {
            this.playlist = playlist;
        }

        private Playlist playlist;
        private  List<Song> allSongsOnline;
        public  List<Song> listSongThisPlaylist;

        public  List<Song> getAllSongsOnline() {
            return allSongsOnline;
        }

        public  void setAllSongsOnline(List<Song> allSongsOnline) {
            this.allSongsOnline = allSongsOnline;
        }

        public  List<Song> getListSongThisPlaylist() {
            return listSongThisPlaylist;
        }

        public  void setListSongThisPlaylist(List<Song> listSongThisPlaylist) {
            this.listSongThisPlaylist = listSongThisPlaylist;
        }

        @Override
        public String toString() {
            return "PlaylistData{" +
                    "playlistID=" + (playlist!=null?playlist.getIdPlaylist():"null") +
                    (allSongsOnline==null?"allSongsOnline":(", allSongsOnlineSize=" + allSongsOnline.size())) +
                    (listSongThisPlaylist==null?"listSongThisPlaylist":(", listSongThisPlaylist=" + listSongThisPlaylist)) +
                    '}';
        }
    }
    public OnlineTBHFragment(Context mContext) {
        this.mContext=mContext;
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
    public static OnlineTBHFragment newInstance(String param1, String param2) {
        OnlineTBHFragment fragment = new OnlineTBHFragment();
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
    RecyclerView recyclerView;
    OnlineTBHFragmentPresenter presenter;
    SongAddAdapter songAddAdapter;
    ProgressBar ProgressBar01;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView=view.findViewById(R.id.RecyclerViewOnlineSongs);
        NewSongAdapter newSongAdapter=new NewSongAdapter();
        presenter=new OnlineTBHFragmentPresenter(this);
        songAddAdapter=new SongAddAdapter();
        ProgressBar01=view.findViewById(R.id.ProgressBar01);
//        if(playlistData!=null&&playlistData.playlist!=null&&playlistData.playlist.getIdPlaylist().equals(ThemBaiHatActivity.getPlaylist().getIdPlaylist())&&playlistData.getAllSongsOnline()!=null&&playlistData.getListSongThisPlaylist()!=null){
//            receiveSongList(playlistData.getAllSongsOnline(),playlistData.getListSongThisPlaylist(),true);
//            Log.d("receiveSongList","Load cũ");
//
//        }
//        else {
//            Log.d("receiveSongList","Load mới");
//            presenter.loadSonglistData(ThemBaiHatActivity.getPlaylist(),mContext);
//        }
        ProgressBar01.getProgress();
        presenter.loadSonglistData(ThemBaiHatActivity.getPlaylist(),mContext);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(songAddAdapter);
    }

    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_online_t_b_h, container, false);
        return  view;
    }

    @Override
    public void onDestroy() {
//        if(playlistData==null) playlistData=new PlaylistData();
//        Log.d("receiveSongList","ok");
//        Log.d("receiveSongListallSongS", String.valueOf(allSong.size()));
//        Log.d("receiveSongListSize", String.valueOf(songList.size()));
        //playlistData.setAllSongsOnline(songAddAdapter.getSongList());
       // playlistData.setListSongThisPlaylist(songAddAdapter.getSongList2());
       // playlistData.setPlaylist(ThemBaiHatActivity.getPlaylist());
        super.onDestroy();
    }

    @Override
    public void receiveSongList(List<Song> allSong, List<Song> songList,boolean saved) {
           Log.d("receiveSongListallSongS", String.valueOf(allSong.size()));
            Log.d("receiveSongListSize", String.valueOf(songList.size()));
        ProgressBar01.setVisibility(View.GONE);
        //if(!saved)
//        {
//            if(playlistData==null) playlistData=new PlaylistData();
//            Log.d("receiveSongList","ok");
//            Log.d("receiveSongListallSongS", String.valueOf(allSong.size()));
//            Log.d("receiveSongListSize", String.valueOf(songList.size()));
//            playlistData.setAllSongsOnline(allSong);
//            playlistData.setListSongThisPlaylist(songList);
//            playlistData.setPlaylist(ThemBaiHatActivity.getPlaylist());
//        }
        songAddAdapter.setData(ThemBaiHatActivity.getPlaylist(),0,allSong,songList,mContext,getActivity());
    }
}