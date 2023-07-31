package com.example.appplaymusic.Fragments;

import static com.example.appplaymusic.Helper.HelpTools.intDayWeekText;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.appplaymusic.Adapters.NewSongAdapter;
import com.example.appplaymusic.Adapters.PlaylistAdapter;
import com.example.appplaymusic.Adapters.SliderImageAdpater;
import com.example.appplaymusic.Category;
import com.example.appplaymusic.CategoryAdapter;
import com.example.appplaymusic.Commons.Common;
import com.example.appplaymusic.Fragments.PlaylistFragment.PlaylistFragment;
import com.example.appplaymusic.Helper.HelpTools;
import com.example.appplaymusic.MainActivity.MainActivity;
import com.example.appplaymusic.Models.Playlist;
import com.example.appplaymusic.Models.Singer;
import com.example.appplaymusic.Models.Song;
import com.example.appplaymusic.Photo;
import com.example.appplaymusic.PlaySongActivity;
import com.example.appplaymusic.R;
import com.example.appplaymusic.Services.APIService;
import com.example.appplaymusic.Services.DataService;
import com.example.appplaymusic.SingerActivity.SingerActivity;
import com.example.appplaymusic.XemThemActivity.XemThemActivity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import me.relex.circleindicator.CircleIndicator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link KhamphaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class KhamphaFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static final int CATEGORY_COTHEBANMUONNGHE_1 = 1;
    public static final int CATEGORY_MOIPHATHANH_2 = 2;
    public static final int CATEGORY_TOP100_3 = 3;
    public static final int CATEGORY_GOIY_4 = 4;

    ViewPager viewPager;
    CircleIndicator circleIndicator;
    SliderImageAdpater photoAdapter;
    List<Photo> photoList;
    Timer timer;
    View mView;
    MainActivity mainActivity;
    RecyclerView rcv_category;
    CategoryAdapter categoryAdapter;
    Dialog dialog;
    public KhamphaFragment() {
        // Required empty public constructor
    }
    public static KhamphaFragment newInstance(String param1, String param2) {
        KhamphaFragment fragment = new KhamphaFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dialog = new Dialog(getActivity());
        dialog.setCancelable(true);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(mView!=null){
            viewPager=mView.findViewById(R.id.viewPager);
            circleIndicator=mView.findViewById(R.id.cirleIndicator);;
            photoAdapter=new SliderImageAdpater();
            photoList=getData();
            photoAdapter.setData(photoList,mainActivity);
            viewPager.setAdapter(photoAdapter);
            circleIndicator.setViewPager(viewPager);
            photoAdapter.registerDataSetObserver(circleIndicator.getDataSetObserver());

            rcv_category=mView.findViewById(R.id.rcv_category_khampha);
            categoryAdapter=new CategoryAdapter();

            categoryAdapter.setData(getCategoryData(),mainActivity);
            categoryAdapter.setIcSonglickListener(new CategoryAdapter.ICSonglickListener() {
                @Override
                public void onSongClickListener(Song song,int position,List<Song> songList,Playlist playlist) {
//                    Log.d("onSongClickListener",song.toString()+" position: "+position);
                    Common.savePlaylistSonglist(position,songList,playlist);
                    //save current song => index of song
                    Common.setCurrentIndex(position);
                    Intent intent=new Intent(getActivity(), PlaySongActivity.class);
                    Bundle bundle=new Bundle();
                    bundle.putSerializable("song_obj",song);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }

                @Override
                public void onPlaylistClickListener(Playlist playlist) {
                    Common.setPlaylist(playlist);
                    Fragment playlistFragment = new PlaylistFragment(playlist);
                    ((MainActivity)getActivity()).replaceFragment(playlistFragment,1);
                }

                @Override
                public void onSingerClickListener(String id, Singer singer) {
                    Intent intent =new Intent(getActivity(), SingerActivity.class);
                    Bundle bundle=new Bundle();
                    bundle.putSerializable("singer_obj",singer);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }

                @Override
                public void onXemThemClickListener(int categoryId, NewSongAdapter newSongAdapter, PlaylistAdapter playlistAdapter) {
                    switch (categoryId){
                        case KhamphaFragment.CATEGORY_GOIY_4:
                            //Toast.makeText(SingerActivity.this, "CATEGORY_NGHESI_BAIHATNOIBAT", Toast.LENGTH_SHORT).show();
                            //Log.d("datanewSongAdapter", String.valueOf(newSongAdapter.getData().size()));
                            Intent intent=new Intent(getActivity(), XemThemActivity.class);
                            Bundle bundle=new Bundle();
                            bundle.putSerializable("list_data", (Serializable) newSongAdapter.getData());
                            bundle.putInt("xemthem_type",Common.XEMTHEM_SONG);
                           // bundle.putSerializable("singer_obj",singer);
                            intent.putExtras(bundle);
                            startActivity(intent);
                            break;
//                        case Common.CATEGORY_NGHESI_PLAYLIST:
//                            Intent intent2=new Intent(SingerActivity.this, XemThemActivity.class);
//                            Bundle bundle2=new Bundle();
//                            bundle2.putSerializable("list_data", (Serializable) playlistAdapter.getData());
//                            bundle2.putInt("xemthem_type",Common.XEMTHEM_PLAYLIST);
//                            bundle2.putSerializable("singer_obj",singer);
//                            intent2.putExtras(bundle2);
//                            startActivity(intent2);
//                            break;
                        case Common.CATEGORY_NGHESI_COTHEBANSETHICH:
                            Toast.makeText(getActivity(), "CATEGORY_NGHESI_COTHEBANSETHICH", Toast.LENGTH_SHORT).show();

                            break;
                    }
                }
            });
            rcv_category.setAdapter(categoryAdapter);
            LinearLayoutManager linearLayoutManager=new LinearLayoutManager(mainActivity);
            rcv_category.setLayoutManager(linearLayoutManager);
            initialTimer();

        }
    }
    private void initialTimer() {
        if(viewPager==null||photoList==null||photoList.isEmpty()) return;
        if(timer==null) timer=new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        int curTime=viewPager.getCurrentItem();
                        int totalTime=photoList.size();
                        if(curTime==totalTime-1) curTime=0;
                        else curTime+=1;
                        viewPager.setCurrentItem(curTime);
                    }
                });
            }
        },2000,5000);
    }
    private List<Category> getCategoryData() {
        List<Category> categoryList=new ArrayList<>();
        categoryList.add(new Category("Có thể bạn muốn nghe",CATEGORY_COTHEBANMUONNGHE_1,Common.TYPE_HORI));
        String nameOfDayWeek="";
        switch (intDayWeekText()){
            case 2:nameOfDayWeek="Thứ hai"; break;
            case 3:nameOfDayWeek="Thứ ba"; break;
            case 4:nameOfDayWeek="Thứ tư"; break;
            case 5:nameOfDayWeek="Thứ năm"; break;
            case 6:nameOfDayWeek="Thứ sáu"; break;
            case 7:nameOfDayWeek="Thứ bảy"; break;
            case 8:nameOfDayWeek="Chủ nhật"; break;
        }
        categoryList.add(new Category(nameOfDayWeek+" thật High",CATEGORY_MOIPHATHANH_2,Common.TYPE_HORI));
        categoryList.add(new Category("Top 100",CATEGORY_TOP100_3,Common.TYPE_HORI));
        categoryList.add(new Category("Gợi ý",CATEGORY_GOIY_4,Common.TYPE_VERTICAL));
        categoryList.add(new Category("Nghệ sĩ yêu thích",Common.CATEGORY_NGHESI_COTHEBANSETHICH,Common.TYPE_HORI));
        return categoryList;
    }

    private List<com.example.appplaymusic.Models.Playlist> getPlaylist(int categoryType) {
        List<com.example.appplaymusic.Models.Playlist> playlists = new ArrayList<>();
        DataService dataService = APIService.getService();
        Call<List<com.example.appplaymusic.Models.Category>> callback = dataService.getCategoryById(categoryType);
//        Response<List<com.example.appplaymusic.Models.Category>> response = null;
//        try {
//            response = callback.execute();
//            List<com.example.appplaymusic.Models.Category> categoryList = response.body();
//            for (int i = 0; i < categoryList.size(); i++) {
//                Log.d("Playlist_cate", categoryList.get(i).getIdPlaylist());
//                int id = Integer.parseInt(categoryList.get(i).getIdPlaylist());
//                Call<com.example.appplaymusic.Models.Playlist> callback2 = dataService.getPlaylistById(id);
//                Response<com.example.appplaymusic.Models.Playlist> response2 = null;
//                response2 = callback2.execute();
//                com.example.appplaymusic.Models.Playlist playlist = (com.example.appplaymusic.Models.Playlist) response2.body();
//                playlists.add(playlist);
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        callback.enqueue(new Callback<List<com.example.appplaymusic.Models.Category>>() {
            @Override
            public void onResponse(Call<List<com.example.appplaymusic.Models.Category>> call, Response<List<com.example.appplaymusic.Models.Category>> response) {
                List<com.example.appplaymusic.Models.Category> categoryList=response.body();
                for (int i = 0; i < categoryList.size(); i++) {
                    Log.d("Playlist_cate",categoryList.get(i).getIdPlaylist());
                    int id=Integer.parseInt(categoryList.get(i).getIdPlaylist());
                    Call<com.example.appplaymusic.Models.Playlist> callback2=dataService.getPlaylistById(id);

//                    try {
//                        Response<com.example.appplaymusic.Models.Playlist> response2 = null;
//                        response2 = callback2.execute();
//                        com.example.appplaymusic.Models.Playlist playlist = (com.example.appplaymusic.Models.Playlist) response2.body();
//                        playlists.add(playlist);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                        Log.d("Playlist",e.getMessage());
//                    }
                    callback2.enqueue(new Callback<com.example.appplaymusic.Models.Playlist>() {
                        @Override
                        public void onResponse(Call<com.example.appplaymusic.Models.Playlist> call, Response<com.example.appplaymusic.Models.Playlist> response2) {
                            com.example.appplaymusic.Models.Playlist playlist= response2.body();
                            playlists.add(playlist);
                        }

                        @Override
                        public void onFailure(Call<com.example.appplaymusic.Models.Playlist> call, Throwable t) {
                            Log.d("Playlist",t.getMessage());
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<List<com.example.appplaymusic.Models.Category>> call, Throwable t) {
                Log.d("category",t.getMessage());
            }
        });
        return playlists;
    }

        private List<Photo> getData() {
        List<Photo> photoList=new ArrayList<>();
        photoList.add(new Photo(R.drawable.nhi2));
        photoList.add(new Photo(R.drawable.nhi1));
        photoList.add(new Photo(R.drawable.nhi4));
        photoList.add(new Photo(R.drawable.nhi3));
        return photoList;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mainActivity= (MainActivity) getActivity();
        mView= inflater.inflate(R.layout.fragment_khampha, container, false);
        if (android.os.Build.VERSION.SDK_INT > 9)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        return mView;
    }
}