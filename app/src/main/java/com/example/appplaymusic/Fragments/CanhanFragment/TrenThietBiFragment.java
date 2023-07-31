package com.example.appplaymusic.Fragments.CanhanFragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.appplaymusic.Adapters.NewSongAdapter;
import com.example.appplaymusic.Commons.Common;
import com.example.appplaymusic.Fragments.KhamphaFragment;
import com.example.appplaymusic.MainActivity.MainActivity;
import com.example.appplaymusic.Models.Song;
import com.example.appplaymusic.PlaySongActivity;
import com.example.appplaymusic.PlaylistNew.PlaylistNewActivity;
import com.example.appplaymusic.R;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TrenThietBiFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TrenThietBiFragment extends Fragment implements TrenThietBiFragmentView{
    public static final int RUNTIME_PERMISSION_CODE = 7;
    NewSongAdapter newSongAdapter;
    RecyclerView recyclerView;
    private static int currentSong=-1;
    TrenThietBiFragmentPresenter trenThietBiFragmentPresenter;
    private Toolbar toolbar;
    private static List<Song> songList;
    private RelativeLayout relativeLayoutMain;
    private  boolean isNotFocus;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public TrenThietBiFragment() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TrenThietBiFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TrenThietBiFragment newInstance(String param1, String param2) {
        TrenThietBiFragment fragment = new TrenThietBiFragment();
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
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        trenThietBiFragmentPresenter=new TrenThietBiFragmentPresenter(this);
        relativeLayoutMain= (RelativeLayout) view.findViewById(R.id.RelativeLayout);
        recyclerView=view.findViewById(R.id.rvc_trenThietBi);
        com.example.appplaymusic.DividerItemDecoration dividerItemDecoration = new com.example.appplaymusic.DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL,false);
        recyclerView.addItemDecoration(dividerItemDecoration);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        newSongAdapter=new NewSongAdapter();
        newSongAdapter.setLimit(false);
        newSongAdapter.setiClickListener(new NewSongAdapter.IClickListener() {
            @Override
            public void onClickListener(Song song, int position) {
                if(isNotFocus) {
                    recyclerView.setAlpha(1);
                    removeChoosePlaylistView();
                    isNotFocus=false;
                    return;
                }
                //currentSong=position;
                Common.setCurrentIndex(position);
                Common.setSongList(TrenThietBiFragment.songList);
                Intent intent=new Intent(getActivity(), PlaySongActivity.class);
                Bundle bundle=new Bundle();
                bundle.putSerializable("song_obj",song);
                intent.putExtras(bundle);
                startActivity(intent);
            }

            @Override
            public void onClickMoreListener(ImageView imageView,String idSong) {
                if(isNotFocus) {
                    recyclerView.setAlpha(1);
                    removeChoosePlaylistView();
                    isNotFocus=false;
                    return;
                }
                PopupMenu popupMenu = new PopupMenu(getActivity(), imageView);

                // Inflating popup menu from popup_menu.xml file
                popupMenu.getMenuInflater().inflate(R.menu.popup_menu_trenthietbi, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()){
                            case R.id.addToPlaylist:
                                trenThietBiFragmentPresenter.showMyPlaylistToChoose(idSong);
                        }
                        return true;
                    }
                });
                // Showing the popup menu
                popupMenu.show();
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
        toolbar = view.findViewById(R.id.toolbar);
        toolbar.setTitle("Trên thiết bị");


        recyclerView.setAdapter(newSongAdapter);
        RelativeLayout.LayoutParams params=new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(0,120,0,0);
        recyclerView.setLayoutParams(params);
        recyclerView.setHasFixedSize(true);
        AndroidRuntimePermission();
    }

    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true);
        view= inflater.inflate(R.layout.fragment_tren_thiet_bi, container, false);
        Log.d("TrenThietBiActivity","onCreate");

        return  view;
    }
    private void removeChoosePlaylistView() {
        @SuppressLint("ResourceType") RelativeLayout relativeLayout=view.findViewById(274);
        if(relativeLayout==null) Log.d("removeChoose","null");
        else Log.d("removeChoose","can");
        relativeLayoutMain.removeView(relativeLayout);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case android.R.id.home:
                ((MainActivity) getActivity()).replaceFragment(new CanhanFragment(),0);
                return true;
            default:break;
        }
        return super.onOptionsItemSelected(item);
    }
    @SuppressLint("ResourceType")
    @Override
    public void addView(RelativeLayout rootView) {
        rootView.setId(274);
        relativeLayoutMain.addView(rootView);
        recyclerView.setAlpha(0.3f);
        isNotFocus=true;
        recyclerView.requestDisallowInterceptTouchEvent(false);
    }

    @Override
    public void goToCreatePlaylist() {
        Intent intent=new Intent(getActivity(), PlaylistNewActivity.class);
        startActivity(intent);
    }

    @Override
    public void setAdapterData(List<Song> songList) {
        toolbar.setTitle("Trên thiết bị"+" ("+songList.size()+")");
        ((MainActivity)getActivity()).setSupportActionBar(toolbar);
        ((MainActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((MainActivity)getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
            this.newSongAdapter.setData(songList,getContext());
            this.songList=songList;
    }

    private Song getCurrentSong() {
        return songList.get(currentSong);
    }

    public void AndroidRuntimePermission(){

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            if(((MainActivity)getActivity()).checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED){
                trenThietBiFragmentPresenter.setAdapterData();
            }
            if(((MainActivity)getActivity()).checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){

                if(shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)){

                    AlertDialog.Builder alert_builder = new AlertDialog.Builder(getActivity());
                    alert_builder.setMessage("External Storage Permission is Required.");
                    alert_builder.setTitle("Please Grant Permission.");
                    alert_builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            ActivityCompat.requestPermissions(
                                    getActivity(),
                                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                    RUNTIME_PERMISSION_CODE

                            );
                        }
                    });

                    alert_builder.setNeutralButton("Cancel",null);

                    AlertDialog dialog = alert_builder.create();

                    dialog.show();

                }
                else {

                    ActivityCompat.requestPermissions(
                            getActivity(),
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            RUNTIME_PERMISSION_CODE
                    );
                }
            }else {

            }
        }
    }
}