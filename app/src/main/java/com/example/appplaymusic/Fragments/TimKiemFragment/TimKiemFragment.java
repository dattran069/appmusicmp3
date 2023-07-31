package com.example.appplaymusic.Fragments.TimKiemFragment;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.example.appplaymusic.Adapters.NewSongAdapter;
import com.example.appplaymusic.Adapters.PlaylistCanhanAdapter;
import com.example.appplaymusic.Adapters.SingerAdapter;
import com.example.appplaymusic.Fragments.CanhanFragment.CanhanFragment;
import com.example.appplaymusic.Fragments.KhamphaFragment;
import com.example.appplaymusic.MainActivity.MainActivity;
import com.example.appplaymusic.Models.Playlist;
import com.example.appplaymusic.Models.Singer;
import com.example.appplaymusic.Models.Song;
import com.example.appplaymusic.PlaySongActivity;
import com.example.appplaymusic.R;
import com.example.appplaymusic.SingerActivity.SingerActivity;
import com.google.android.material.textfield.TextInputEditText;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TimKiemFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TimKiemFragment extends Fragment implements TimKiemFragmentView{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public TimKiemFragment() {
        // Required empty public constructor
        isLocal=false;
    }
    public TimKiemFragment(boolean b) {
        // Required empty public constructor
        isLocal=b;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TimKiemFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TimKiemFragment newInstance(String param1, String param2) {
        TimKiemFragment fragment = new TimKiemFragment();
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
    RecyclerView recyclerViewGanday;
    RecyclerView recyclerViewResultSongs;
    RecyclerView recyclerViewResultPlaylists;
    RecyclerView recyclerViewResultNghesis;
    NewSongAdapter newSongAdapter;
    NewSongAdapter newSongAdapterSongs;
    PlaylistCanhanAdapter playlistCanhanAdapter;
    SingerAdapter singerAdapter;
    LinearLayout linear_search_introduce;
    LinearLayout linear_search_result;
    Toolbar toolbar;
    TextView deleteHistory;
    TextInputEditText inputEditText;
    ImageView deleteSearch;
    TimKiemFragmentPresenter presenter;
    TextView txtSearchSong;
    TextView txtSearcPlaylist;
    TextView txtSearchSinger;
    private static boolean isLocal=false;
    TextView txtSearchMessage;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        recyclerViewGanday=view.findViewById(R.id.RecyclerView_searchGanday);
        recyclerViewResultSongs=view.findViewById(R.id.RecyclerView_search_songs);
        recyclerViewResultPlaylists=view.findViewById(R.id.RecyclerView_search_playlist);
        recyclerViewResultNghesis=view.findViewById(R.id.RecyclerView_search_nghesi);
        toolbar=view.findViewById(R.id.search_toolbar);
        ((MainActivity)getActivity()).setSupportActionBar(toolbar);
        ((MainActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((MainActivity)getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        newSongAdapter=new NewSongAdapter();
        newSongAdapterSongs=new NewSongAdapter();
        playlistCanhanAdapter=new PlaylistCanhanAdapter();
        singerAdapter=new SingerAdapter();
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity()){
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        LinearLayoutManager linearLayoutManagerS=new LinearLayoutManager(getActivity()){
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };;
        LinearLayoutManager linearLayoutManagerP=new LinearLayoutManager(getActivity()){
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };;
        LinearLayoutManager linearLayoutManagerN=new LinearLayoutManager(getActivity()){
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        recyclerViewGanday.setAdapter(newSongAdapter);
        recyclerViewGanday.setLayoutManager(linearLayoutManager);
        recyclerViewResultSongs.setAdapter(newSongAdapterSongs);
        recyclerViewResultSongs.setLayoutManager(linearLayoutManagerS);
        recyclerViewResultPlaylists.setAdapter(playlistCanhanAdapter);
        recyclerViewResultPlaylists.setLayoutManager(linearLayoutManagerP);
        recyclerViewResultNghesis.setAdapter(singerAdapter);
        recyclerViewResultNghesis.setLayoutManager(linearLayoutManagerN);
        deleteSearch=view.findViewById(R.id.iconDelete_search);
        deleteSearch.setVisibility(View.GONE);
        inputEditText=view.findViewById(R.id.TextInputEditText_search);
        txtSearchSong=view.findViewById(R.id.txt_search_songs);
        txtSearchSinger=view.findViewById(R.id.txt_search_nghesi);
        txtSearcPlaylist=view.findViewById(R.id.txt_search_playlists);
        txtSearchMessage=view.findViewById(R.id.txt_search_message);
        deleteHistory=view.findViewById(R.id.delete_historySearch);
        linear_search_introduce=view.findViewById(R.id.linear_search_introduce);
        linear_search_result=view.findViewById(R.id.linear_search_result);
        presenter=new TimKiemFragmentPresenter(getContext(),this);
        inputEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.length()!=0) {
                    presenter.loadSearch(charSequence.toString());

                }else {
                    txtSearchMessage.setVisibility(View.GONE);
                    linear_search_introduce.setVisibility(View.VISIBLE);
                    linear_search_result.setVisibility(View.GONE);
                    deleteSearch.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        deleteSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inputEditText.setText("");
            }
        });
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tim_kiem, container, false);
    }

    @Override
    public void show(List<Song> songs, List<Playlist> playlists, List<Singer> singers) {
        txtSearchMessage.setVisibility(View.VISIBLE);
        deleteSearch.setVisibility(View.VISIBLE);
        linear_search_introduce.setVisibility(View.GONE);
        linear_search_result.setVisibility(View.VISIBLE);
        if(songs.size()==0&&playlists.size()==0&&singers.size()==0){
           txtSearchMessage.setText("Không tìm thấy..");
        } else txtSearchMessage.setText("Kết quả tìm kiếm");
        if(songs.size()>0) {
            txtSearchSong.setVisibility(View.VISIBLE);
            recyclerViewResultSongs.setVisibility(View.VISIBLE);
            newSongAdapterSongs.setData(songs,getActivity());
        } else {
            recyclerViewResultSongs.setVisibility(View.GONE);
            txtSearchSong.setVisibility(View.GONE);
        }
        if(playlists.size()>0) {
            txtSearcPlaylist.setVisibility(View.VISIBLE);
            recyclerViewResultPlaylists.setVisibility(View.VISIBLE);
            playlistCanhanAdapter.setData(true,playlists, getActivity(), new PlaylistCanhanAdapter.IonClick() {
                @Override
                public void onClick(String id, Playlist playlist) {

                }
            });
        } else {
            recyclerViewResultPlaylists.setVisibility(View.GONE);
            txtSearcPlaylist.setVisibility(View.GONE);
        }
        if(singers.size()>0) {
            txtSearchSinger.setVisibility(View.VISIBLE);
            recyclerViewResultNghesis.setVisibility(View.VISIBLE);
            singerAdapter.setData( singers, getContext(),new SingerAdapter.IonClick() {
                @Override
                public void onClick(String id, Singer singer) {
                    Intent intent =new Intent(getActivity(), SingerActivity.class);
                    Bundle bundle=new Bundle();
                    bundle.putSerializable("singer_obj",singer);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });
        } else {
            recyclerViewResultNghesis.setVisibility(View.GONE);
            txtSearchSinger.setVisibility(View.GONE);
        }
        newSongAdapterSongs.setiClickListener(new NewSongAdapter.IClickListener() {
            @Override
            public void onClickListener(Song song, int position) {
                Intent intent=new Intent(getActivity(), PlaySongActivity.class);
                intent.putExtra("song_obj",song);
                startActivity(intent);
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

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                Toast.makeText(getActivity(), "Thoat coi dcm", Toast.LENGTH_SHORT).show();
                if(!isLocal){
                    Toast.makeText(getActivity(), "Thoat coi dcm del local", Toast.LENGTH_SHORT).show();
                    ((MainActivity) getActivity()).replaceFragment(new KhamphaFragment(),1);
                    isLocal=false;
                }else{                    Toast.makeText(getActivity(), "Thoat coi dcm  local", Toast.LENGTH_SHORT).show();

                    ((MainActivity) getActivity()).replaceFragment(new CanhanFragment(),0);

                }
                return true;

            default:
                Toast.makeText(getActivity(), "Thoat coi dcm de", Toast.LENGTH_SHORT).show();
                break;

        }
        return super.onOptionsItemSelected(item);
    }
}