package com.example.appplaymusic.Fragments.CanhanFragment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.opengl.Visibility;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.appplaymusic.Adapters.NewSongAdapter;
import com.example.appplaymusic.Helper.HelpTools;
import com.example.appplaymusic.Models.Song;
import com.example.appplaymusic.PlaySongActivity;
import com.example.appplaymusic.PlaylistNew.PlaylistNewActivity;
import com.example.appplaymusic.R;

import java.util.List;
import java.util.Random;

public class TrenThietBiActivity extends AppCompatActivity implements TrenThietBiActivityView{
    public static final int RUNTIME_PERMISSION_CODE = 7;
    NewSongAdapter newSongAdapter;
    RecyclerView recyclerView;
    private static int currentSong=-1;
    TrenThietBiActivityPresenter trenThietBiActivityPresenter;
    private TextView song_name;
    private TextView sing_name;
    private ImageView icon_footer_heart;
    private ImageView icon_footer_play;
    private ImageView icon_footer_pause;
    private ImageView icon_footer_next;
    private Toolbar toolbar;
    private ImageView song_avar;
    private RelativeLayout RelativeLayout_footer;
    private static List<Song> songList;
    private RelativeLayout relativeLayoutMain;
    private  boolean isNotFocus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("TrenThietBiActivity","onCreate");
        trenThietBiActivityPresenter=new TrenThietBiActivityPresenter(this);
        relativeLayoutMain= (RelativeLayout) getLayoutInflater().inflate(R.layout.activity_tren_thiet_bi,null,false);
        setContentView(relativeLayoutMain);
        recyclerView=findViewById(R.id.rvc_trenThietBi);
        com.example.appplaymusic.DividerItemDecoration dividerItemDecoration = new com.example.appplaymusic.DividerItemDecoration(this, DividerItemDecoration.VERTICAL,false);
        recyclerView.addItemDecoration(dividerItemDecoration);
        RelativeLayout_footer=findViewById(R.id.layout_footer_song);
        RelativeLayout_footer.setVisibility(View.GONE);
        song_avar=findViewById(R.id.img_song_avar_footer);
        song_name=findViewById(R.id.txt_song_name_footer);
        sing_name=findViewById(R.id.txt_singer_name_footer);
        icon_footer_heart=findViewById(R.id.icon_heart_footer);
        icon_footer_play=findViewById(R.id.icon_play_footer);
        icon_footer_pause=findViewById(R.id.icon_pause_footer);
        icon_footer_next=findViewById(R.id.icon_next_footer);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
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
                currentSong=position;
                Intent intent=new Intent(TrenThietBiActivity.this, PlaySongActivity.class);
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
                PopupMenu popupMenu = new PopupMenu(TrenThietBiActivity.this, imageView);

                // Inflating popup menu from popup_menu.xml file
                popupMenu.getMenuInflater().inflate(R.menu.popup_menu_trenthietbi, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()){
                            case R.id.addToPlaylist:
                                trenThietBiActivityPresenter.showMyPlaylistToChoose(idSong);
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
        toolbar = findViewById(R.id.toolbar);
        //AppBarLayout.LayoutParams layoutParams= (AppBarLayout.LayoutParams) toolbar.getLayoutParams();
        //layoutParams.setMargins(0,0,0,100);
        //toolbar.setLayoutParams(layoutParams);
        toolbar.setTitle("co cc");

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        recyclerView.setAdapter(newSongAdapter);
        RelativeLayout.LayoutParams params=new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(0,120,0,0);
        recyclerView.setLayoutParams(params);
        recyclerView.setHasFixedSize(true);
        Log.d("TrenThietBiActivity","AndroidRuntimePermission");
        AndroidRuntimePermission();
        Log.d("TrenThietBiActivity","AndroidRuntimePermission");

        RelativeLayout_footer.setOnClickListener(view -> {
            Intent intent=new Intent(TrenThietBiActivity.this,PlaySongActivity.class);
            startActivity(intent);
        });
        icon_footer_pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                trenThietBiActivityPresenter.pauseSong();
            }
        });
        icon_footer_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                trenThietBiActivityPresenter.playSong();
            }
        });
        icon_footer_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                trenThietBiActivityPresenter.playNextSong(getNextSong());
            }
        });
    }

    private void removeChoosePlaylistView() {
        @SuppressLint("ResourceType") RelativeLayout relativeLayout=findViewById(274);
        if(relativeLayout==null) Log.d("removeChoose","null");
        else Log.d("removeChoose","can");
        relativeLayoutMain.removeView(relativeLayout);
    }

    private void updateFooterSongIcon(ImageView icon_play, ImageView icon_pause) {
        Log.d("ActivityPlaylist", PlaySongActivity.Playing?"playing":"not playing");
        if(PlaySongActivity.Playing)
        {
            icon_play.setVisibility(View.GONE);
            icon_pause.setVisibility(View.VISIBLE);
        } else{
            icon_play.setVisibility(View.VISIBLE);
            icon_pause.setVisibility(View.GONE);
        }
    }
    @Override
    protected void onRestart() {
        Log.d("TrenThietBiActivity","onRestart");
        if(currentSong>=0){
            RelativeLayout_footer.setVisibility(View.VISIBLE);
        }
        super.onRestart();

    }

    @Override
    protected void onResume() {
        Log.d("TrenThietBiActivity","onResume");
        updateFooterSong();
        super.onResume();

    }
    @Override
    public void updateFooterSong() {
        if(songList==null||songList.size()==0||currentSong<0) return;
        song_name.setText(getCurrentSong().getSongName());
        sing_name.setText(getCurrentSong().getSingerName());
        if (getCurrentSong().getType()==1) {
            song_avar.setImageBitmap(HelpTools.getBitmapImgSongLocal(getCurrentSong(),getContext()));
        }
        else Glide.with(getApplicationContext()).load(getCurrentSong().getSongImage()).into(song_avar);
        updateFooterSongIcon(icon_footer_play,icon_footer_pause);
    }

    @Override
    public void updateIconPlay(boolean isPlaying) {
        if(isPlaying) {
            icon_footer_play.setVisibility(View.VISIBLE);
            icon_footer_pause.setVisibility(View.GONE);
        }
        else {
            icon_footer_pause.setVisibility(View.VISIBLE);
            icon_footer_play.setVisibility(View.GONE);
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case android.R.id.home:
                onBackPressed();
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
        setContentView(relativeLayoutMain);
        recyclerView.setAlpha(0.3f);
        isNotFocus=true;
        recyclerView.requestDisallowInterceptTouchEvent(false);
    }

    @Override
    public void goToCreatePlaylist() {
        Intent intent=new Intent(TrenThietBiActivity.this, PlaylistNewActivity.class);
        startActivity(intent);
    }

    private Song getCurrentSong() {
        return songList.get(currentSong);
    }

    public void AndroidRuntimePermission(){
        Log.d("isGetAllLocalSong: ","AndroidRuntimePermission" );
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED){
                Log.d("isGetAllLocalSong: ","AndroidRuntimePermission" );
                trenThietBiActivityPresenter.setAdapterData();
            } else {
                Log.d("isGetAllLocalSong: ","cc" );
            }
            if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){

                if(shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)){

                    AlertDialog.Builder alert_builder = new AlertDialog.Builder(TrenThietBiActivity.this);
                    alert_builder.setMessage("External Storage Permission is Required.");
                    alert_builder.setTitle("Please Grant Permission.");
                    alert_builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            ActivityCompat.requestPermissions(
                                    TrenThietBiActivity.this,
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
                            TrenThietBiActivity.this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            RUNTIME_PERMISSION_CODE
                    );
                }
            }else {

            }
        }
    }
    @Override
    public Context getContext() {
        return getApplicationContext();
    }

    @Override
    public Activity getActivity() {
        return getActivity();
    }

    @Override
    public void setAdapterData(List<Song> songList) {
        this.newSongAdapter.setData(songList,getContext());
        this.songList=songList;
    }
    public static Song getPreviusSong() {
        if(currentSong==0)
            currentSong=songList.size()-1; else currentSong-=1;
        return  songList.get(currentSong);
    }
    public static Song getNextSong() {
        if(currentSong==songList.size()-1)
            currentSong=0; else currentSong+=1;
        return  songList.get(currentSong);
    }
    public static com.example.appplaymusic.Models.Song getRandomSong() {
        Random r=new Random();
        int randomId=r.nextInt(songList.size()-1);
        while(randomId==currentSong) randomId=r.nextInt(songList.size()-1);
        currentSong=randomId;
        return songList.get(randomId);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {

            case RUNTIME_PERMISSION_CODE: {

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d("onRequestPermissions","ok");
                    trenThietBiActivityPresenter.setAdapterData();

                } else {

                }
            }
        }
    }
}