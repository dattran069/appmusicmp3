package com.example.appplaymusic.Fragments.PlaylistFragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.appplaymusic.Adapters.NewSongAdapter;
import com.example.appplaymusic.Adapters.PlaylistPagerAdapter;
import com.example.appplaymusic.Commons.Common;
import com.example.appplaymusic.Fragments.CanhanFragment.CanhanFragment;
import com.example.appplaymusic.Fragments.KhamphaFragment;
import com.example.appplaymusic.Fragments.PlaylistFrontFragment;
import com.example.appplaymusic.Helper.HelpTools;
import com.example.appplaymusic.ILoadmore;
import com.example.appplaymusic.MainActivity.MainActivity;
import com.example.appplaymusic.Models.Playlist;
import com.example.appplaymusic.Models.Song;
import com.example.appplaymusic.PlaySongActivity;
import com.example.appplaymusic.R;
import com.example.appplaymusic.Services.APIService;
import com.example.appplaymusic.SingerActivity.SingerActivity;

import java.util.List;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;
import me.relex.circleindicator.CircleIndicator;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PlaylistFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

public class PlaylistFragment extends Fragment implements PlaylistFragmentView{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private static final int PLAY_SONG = 123;
    private Toolbar toolbar;
    private ViewPager viewPager;
    private CircleIndicator circleIndicator;
    private PlaylistPagerAdapter playlistPagerAdapter;
    private RecyclerView recyclerViewSong;
    private NewSongAdapter newSongAdapter;
    private int numSongs=0;
    int page=1;
    CircleImageView song_avar;
    private PlaylistFragmentPresenter playlistFragmentPresenter;
    private static List<Song> songList;
    private static int  currentSong=-1;
    private static com.example.appplaymusic.Models.Playlist playlist = null;

    public static Playlist getPlaylist() {
        return playlist;
    }

    public static void setPlaylist(Playlist playlist) {
        PlaylistFragment.playlist = playlist;
    }

    private static boolean isLocal=false;

    public static boolean isLocal() {
        return isLocal;
    }

    public void setLocal(boolean local) {
        isLocal = local;
    }



    @Override
    public void onResume() {
        Log.d("resume", String.valueOf(currentSong));
        if(newSongAdapter!=null)newSongAdapter.notifyDataSetChanged();

        super.onResume();
    }
    public void savePlaylistSonglist(int currentindex){
        Common.savePlaylist_ListSongSPS(this.songList,this.playlist,currentindex);
    }

    @Override
    public void setSongAdapterData(List<Song> songList) {
        this.songList=songList;
        this.newSongAdapter.setData(this.songList,getActivity());
        Common.setSongList(songList);
        playlistPagerAdapter.setData(songList,songList.size());
    }

    public void setSongList(List<Song> songList) {this.songList=songList;}
    public com.example.appplaymusic.Models.Song getCurrentSong(){
        return songList.get(currentSong);
    }

    @Override
    public void add(List<Song> songList2) {
        this.songList.addAll(songList2);
        this.newSongAdapter.setData(this.songList,getActivity());
    }


    @Override
    public void setSongAdapterLoader() {
        if(recyclerViewSong.getLayoutManager().getItemCount()-1==playlistFragmentPresenter.getTotalSongs(playlistPagerAdapter)){
            //Toast.makeText(this, "Hết rồi á!", Toast.LENGTH_SHORT).show();
            playlistFragmentPresenter.removeLoadMoreBtn();
        }
        newSongAdapter.setLoader();
    }

    @Override
    public void removeLoadMoreBtn() {
        newSongAdapter.setMoreable(false);
        newSongAdapter.removeLoadMoreBtn();
    }
    //kham pha fragment
    public PlaylistFragment(Playlist playlist) {
        isLocal=false;
        this.playlist=playlist;
    }
    public PlaylistFragment(Playlist playlist,int page) {
        this.page=page;
        isLocal=false;
        this.playlist=playlist;
    }
    //ca nhan fragment
    public PlaylistFragment(Playlist playlist,List<Song>songList2,boolean isLocal,int numSongs) {
        this.numSongs=numSongs;
        this.isLocal=isLocal;
        this.playlist=playlist;
        songList=songList2;
        Common.setSongList(this.songList);
    }
    public String getPlaylistId(){
        return this.playlist.getIdPlaylist();
    }

    public PlaylistFragment(){
        this.playlist=Common.getPlaylist();
    }
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PlaylistFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PlaylistFragment newInstance(String param1, String param2) {
        PlaylistFragment fragment = new PlaylistFragment(Common.getPlaylist());
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
            int index=bundle.getInt("currentSongIndex");
            updateSongCurrent();
        }
    };

    private void updateSongCurrent() {
        if(playlistPagerAdapter!=null) newSongAdapter.updateSongCurrent();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        toolbar = view.findViewById(R.id.toolbar);
        viewPager=view.findViewById(R.id.viewPager);
        circleIndicator=view.findViewById(R.id.CircleIndicator_playlist);
        recyclerViewSong=view.findViewById(R.id.rcv_playlist);
        com.example.appplaymusic.DividerItemDecoration dividerItemDecoration = new com.example.appplaymusic.DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL,false);
        recyclerViewSong.addItemDecoration(dividerItemDecoration);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity());
        recyclerViewSong.setHasFixedSize(true);
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(broadcastReceiver,new IntentFilter("send_data_to_activity"));
        recyclerViewSong.setLayoutManager(linearLayoutManager);
        View view2=view.findViewById(R.id.mainPage);
        newSongAdapter=new NewSongAdapter(isLocal);
        newSongAdapter.setMainPage(view);
        recyclerViewSong.setAdapter(newSongAdapter);
        toolbar.setTitle(playlist.getNamePlaylist());
        if(page==Common.MAINACTIVITY_PAGE){
            ((MainActivity)getActivity()).setSupportActionBar(toolbar);
            ((MainActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            ((MainActivity)getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        else {
            ((SingerActivity)getActivity()).setSupportActionBar(toolbar);
            ((SingerActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            ((SingerActivity)getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        playlistFragmentPresenter=new PlaylistFragmentPresenter(this,getActivity(),playlist);
        playlistPagerAdapter=new PlaylistPagerAdapter(playlist,getActivity());
        playlistPagerAdapter.setNumSongs(numSongs);
        viewPager.setAdapter(playlistPagerAdapter);
        viewPager.setCurrentItem(0);
        circleIndicator.setViewPager(viewPager);
        playlistPagerAdapter.registerDataSetObserver(circleIndicator.getDataSetObserver());
        if(!isLocal) playlistFragmentPresenter.getSongListData();
        else {
            newSongAdapter.setData(this.songList,getActivity());
        }
        //else playlistPagerAdapter.setDataLocalPlaylist();
        newSongAdapter.setiClickListener(new NewSongAdapter.IClickListener() {
            @Override
            public void onClickListener(com.example.appplaymusic.Models.Song song, int position) {
                //save vao playlist vao Common
                savePlaylistSonglist(position);
                Log.d("save",playlist.getNamePlaylist());
                //save current song => index of song
                Common.setCurrentIndex(position);
                Intent intent=new Intent(getActivity(),PlaySongActivity.class);
                Bundle bundle=new Bundle();
                bundle.putSerializable("song_obj",song);
                intent.putExtras(bundle);
                startActivity(intent);
            }

            @Override
            public void onClickMoreListener(ImageView imageView,String id) {
                playlistFragmentPresenter.loadMoreSongData();
            }

            @Override
            public void onClickAddPlaylistListener(com.example.appplaymusic.Models.Song song,int position) {

            }

            @Override
            public void onClickRemovePlaylistListener(Song song, int position) {
                APIService.getService().deleteSongFromPlaylistLocal(song.getIdSong(),playlist.getIdPlaylist());
                //Toast.makeText(getActivity(), "", Toast.LENGTH_SHORT).show();
                Log.d("xoa",song+" "+playlist.getIdPlaylist());
            }

            @Override
            public void onClickXemThemListener(Song song, int position) {

            }
        });



        newSongAdapter.setLoadMore(new ILoadmore() {
            @Override
            public void onLoadMore() {
                playlistFragmentPresenter.loadMoreSongData();
                newSongAdapter.notifyDataSetChanged();
                newSongAdapter.setLoader();
            }
        });

        Button btnPhatNgauNhien=view.findViewById(R.id.btn_phatNgauNhien);
        btnPhatNgauNhien.setOnClickListener(View->{
            Toast.makeText(getActivity(), String.valueOf(songList.size()), Toast.LENGTH_SHORT).show();
            //Log.d("songlist",songList.get(0).toString());
            //Log.d("playlist",playlist.toString());
            Common.savePlaylist_ListSongSPS(songList,playlist,0);
            Song song=Common.getRandomSong();
            //Log.d("randomSong", String.valueOf(Common.getCurrentIndex()));
            Intent intent=new Intent(getActivity(),PlaySongActivity.class);
            Bundle bundle=new Bundle();
            bundle.putSerializable("song_obj",song);
            intent.putExtras(bundle);
            startActivity(intent);
        });
    }

    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true);
        view=inflater.inflate(R.layout.fragment_playlist, container, false);
        return view;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case android.R.id.home:
                if(!isLocal){
                    ((MainActivity) getActivity()).replaceFragment(new KhamphaFragment(),1);
                    isLocal=false;
                }else{
                    ((MainActivity) getActivity()).replaceFragment(new CanhanFragment(),0);

                }
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}