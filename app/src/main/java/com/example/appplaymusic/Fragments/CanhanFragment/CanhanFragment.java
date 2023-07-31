package com.example.appplaymusic.Fragments.CanhanFragment;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appplaymusic.Adapters.PlaylistCanhanAdapter;
import com.example.appplaymusic.Commons.Common;
import com.example.appplaymusic.Database.Database;
import com.example.appplaymusic.Fragments.PlaylistFragment.PlaylistFragment;
import com.example.appplaymusic.Fragments.TaiKhoanFragment.TaiKhoanFragment;
import com.example.appplaymusic.Fragments.TimKiemFragment.TimKiemFragment;
import com.example.appplaymusic.Helper.HelpTools;
import com.example.appplaymusic.MainActivity.MainActivity;
import com.example.appplaymusic.Models.Playlist;
import com.example.appplaymusic.Models.PlaylistSong;
import com.example.appplaymusic.Models.PlaylistSongResponse;
import com.example.appplaymusic.Models.Song;
import com.example.appplaymusic.PlaylistNew.PlaylistNewActivity;
import com.example.appplaymusic.R;
import com.example.appplaymusic.Services.APIService;
import com.example.appplaymusic.ShareData;
import com.google.android.material.textfield.TextInputEditText;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Response;
/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CanhanFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CanhanFragment extends Fragment implements CanhanFragmentView {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    CanhanFragmentPresenter presenter;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CanhanFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CanhanFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CanhanFragment newInstance(String param1, String param2) {
        CanhanFragment fragment = new CanhanFragment();
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
        presenter=new CanhanFragmentPresenter(this);
    }
    View view;
    RecyclerView recyclerViewCanhan;
    List<Playlist> playlists;
    PlaylistCanhanAdapter playlistCanhanAdapter;
    Button button;
    TextInputEditText textInputEditText;
    @Override
    public void onResume() {
        //if(playlistCanhanAdapter!=null) playlistCanhanAdapter.set
        super.onResume();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.fragment_canhan, container, false);
        LinearLayout linelayoutTrenThietBi=view.findViewById(R.id.linelayoutTrenThietBi);
        linelayoutTrenThietBi.setOnClickListener(view -> {
            TrenThietBiFragment trenThietBiFragment=new TrenThietBiFragment();
            ((MainActivity)getActivity()).replaceFragment(trenThietBiFragment,0);
        });
        recyclerViewCanhan=view.findViewById(R.id.rcv_playlist_canhan);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity());
        recyclerViewCanhan.setLayoutManager(linearLayoutManager);
        //NestedScrollView nestedScrollView=view.findViewById(R.id.NestedScrollView);
        playlistCanhanAdapter=new PlaylistCanhanAdapter();
        recyclerViewCanhan.setAdapter(playlistCanhanAdapter);
        playlistCanhanAdapterLoadData();
        button=view.findViewById(R.id.btnInsertPlaylist);
        button.setOnClickListener(view -> {
            Intent intent=new Intent(getActivity(), PlaylistNewActivity.class);
            startActivity(intent);
        });
        CircleImageView circleImageViewAvar=view.findViewById(R.id.avar_canhan);
        circleImageViewAvar.setOnClickListener(view -> {
            TaiKhoanFragment taiKhoanFragment=new TaiKhoanFragment();
            ((MainActivity)getActivity()).replaceFragment(taiKhoanFragment,0);
        });
        textInputEditText=view.findViewById(R.id.input_search_edt);

        textInputEditText.setFocusable(false);
        textInputEditText.setFocusableInTouchMode(false);
        textInputEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)getActivity()).replaceFragment(new TimKiemFragment(true),0);
            }
        });
        return view;
    }

    private void playlistCanhanAdapterLoadData() {
        //playlists= Database.getInstance(getActivity()).playlistDAO().getListPlaylist();
        //lay tu internet

        int userId=Common.getUserId(getActivity());
        if(userId!=-1) presenter.getPlaylistLocalFromInternet(userId);
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void receivePlaylist(List<Playlist> playlists) {
        //Log.d("receivePlaylist", String.valueOf(playlists.size()));
        //playlists.forEach(p->Log.d("plist",p.toString()));
        playlistCanhanAdapter.setData(playlists, getActivity(), new PlaylistCanhanAdapter.IonClick() {
            @Override
            public void onClick(String idPlaylist, Playlist playlist) {
                //List<PlaylistSong> playlistSongs=Database.getInstance(getActivity()).playlistSongDAO().getListPlaylistSong();
                //List<Song> songList=new ArrayList<>();
                //for (int i = 0; i < playlistSongs.size(); i++) {
                //   songList.add(GetMediaMp3FilesByIdPlaylist(playlistSongs.get(i).getIdSong()));
                //}
                List<Song> songList = new ArrayList<>();
                Call<PlaylistSongResponse> call = APIService.getService().getIdSongsByPlaylistSongId(Integer.parseInt(idPlaylist));
                try {
                    Response<PlaylistSongResponse> response = call.execute();
                    PlaylistSongResponse playlistSongResponse=response.body();
                    Toast.makeText(CanhanFragment.this.getActivity(), "Load playlist content ok", Toast.LENGTH_SHORT).show();
                    List<String> idSongs = playlistSongResponse.getLocalSongIds();
//                    idSongs.forEach(id -> {
//                        songList.add(CanhanFragment.this.GetMediaMp3FilesByIdPlaylist(id));
//                    });
                    List<Song> localSongs=GetMediaMp3FilesByIdPlaylist(idSongs);
                    if(localSongs!=null) songList.addAll(localSongs);
                    else Log.d("localPlaylist","null");
                    List<Song> songInternet=playlistSongResponse.getInternetSongs();
                    if(songInternet!=null)
                    {
                        songList.addAll(songInternet);
                        Log.d("songInternet", String.valueOf(songInternet.size()));
                    }
                    else {

                    }

                } catch (IOException e) {
                    Log.d("ly do failed",e.getMessage());
                    Toast.makeText(CanhanFragment.this.getActivity(), "Load Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
                ShareData.setSongList(songList);
                //MainActivity.setPlaylistt(playlist);
                //Common.savePlaylist_ListSongSPS(songList,playlist);
                Fragment playlistFragment = new PlaylistFragment(playlist, songList, true, songList.size());
                ((PlaylistFragment) playlistFragment).setSongList(songList);
                ((MainActivity) CanhanFragment.this.getActivity()).replaceFragment(playlistFragment, 0);
            }
        });
    }
    public List<Song> GetMediaMp3FilesByIdPlaylist(List<String> ids){
        //Log.d("GetMedia_id: ",id);
        List<Song> songList=new ArrayList<>();

        ContentResolver contentResolver;

        Cursor cursor;

        Uri uri;
        contentResolver = getActivity().getContentResolver();

        uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.DATA,    // filepath of the audio file
                MediaStore.Audio.Playlists.ALBUM    // context id/ uri id of the file
        };


        String strIds="";
        for (int i = 0; i < ids.size(); i++) {
            if(i!=ids.size()-1) strIds+=ids.get(i)+",";
            else strIds+=(ids.get(i));
        }
        Log.d("strIds",strIds);
        String selection =" _id IN ("+strIds+")";
        cursor = contentResolver.query(
                uri, // Uri
                projection,
                selection,
                null,
                null
        );

        if (cursor == null) {
        } else if (!cursor.moveToFirst()) {
        }
        else {
            do {
                String queryTtile = cursor.getString(0);
                String queryArtist = cursor.getString(1);
                String queryAlbum = cursor.getString(2);
                String queryDuration = cursor.getString(3);
                String filepath = cursor.getString(4);
                String _id = cursor.getString(5);
                songList.add(new Song(_id,queryTtile,queryArtist,queryAlbum,filepath,1));
                Log.d("slist sizeId:", queryTtile);
                Log.d("slist sizeId:", queryArtist);
                Log.d("slist sizeId:", queryAlbum);
                Log.d("slist sizeId:", filepath);
                Log.d("slist sizeId:", _id);

            } while (cursor.moveToNext());
        }
        Log.d("slist size:", String.valueOf(songList.size()));
        return songList;
    }


}