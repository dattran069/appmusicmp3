package com.example.appplaymusic.ThemBaihatActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.viewpager2.widget.ViewPager2;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.example.appplaymusic.Adapters.PlaySongPagerAdapter;
import com.example.appplaymusic.Adapters.ThemBaiHatPagerAdapter;
import com.example.appplaymusic.Fragments.CaNhanTBHFragment;
import com.example.appplaymusic.Fragments.CanhanFragment.TrenThietBiActivity;
import com.example.appplaymusic.Helper.HelpTools;
import com.example.appplaymusic.Models.Playlist;
import com.example.appplaymusic.Models.PlaylistSongResponse;
import com.example.appplaymusic.Models.Song;
import com.example.appplaymusic.R;
import com.example.appplaymusic.Services.APIService;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ThemBaiHatActivity extends AppCompatActivity {
    ImageView icon_close_thembaihat;
    TextInputEditText textInputEditText;
    ImageView icon_close_search;
    BottomNavigationView bottomNavigationView;
    ViewPager2 viewPager2;
    public static final int RUNTIME_PERMISSION_CODE = 7;
    public static com.example.appplaymusic.Models.Playlist playlist;
    ThemBaiHatPagerAdapter themBaiHatPagerAdapter;
    public static Playlist getPlaylist(){return playlist;}


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_them_bai_hat);
        if(getIntent().getExtras()!=null){
            playlist= (com.example.appplaymusic.Models.Playlist) getIntent().getExtras().get("playlist_obj");
            Log.d("Playlist",playlist.toString());
        }
        icon_close_thembaihat=findViewById(R.id.icon_close_thembaihat);
        textInputEditText=findViewById(R.id.TextInputEditText_themBaiHat);
        icon_close_search=findViewById(R.id.iconDelete_thembaihat);

        bottomNavigationView=findViewById(R.id.BottomNavigationView_themBaiHat);
        viewPager2=findViewById(R.id.ViewPager2_themBaiHat);
        themBaiHatPagerAdapter=new ThemBaiHatPagerAdapter(this);
        themBaiHatPagerAdapter.setContext(getApplicationContext());
        themBaiHatPagerAdapter.setPlaylist(playlist);
        themBaiHatPagerAdapter.setSongList(getSongList());
        viewPager2.setAdapter(themBaiHatPagerAdapter);
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                switch (position){
                    case 0:
                        bottomNavigationView.getMenu().findItem(R.id.online).setChecked(true);
                        break;
                    case 1:
                        bottomNavigationView.getMenu().findItem(R.id.canhan).setChecked(true);
                        break;
                }
            }
        });
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.canhan:
                        viewPager2.setCurrentItem(1);
                        break;
                    case R.id.online:
                        viewPager2.setCurrentItem(0);
                        break;
                }
                return true;
            }
        });
        icon_close_thembaihat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        checkPermissions();

    }

    private List<Song> getSongList() {
            //id user -> tru ra nhung bai them roi,
            // co playlist data co id roi, 1 function check in list thi hien dau tich v kh can id playlist
            //APIService.getService()
            Log.d("thisPlaylistSongSizeIDP",playlist.getIdPlaylist());
            List<Song> songList = new ArrayList<>();
            Call<PlaylistSongResponse> call2 = APIService.getService().getIdSongsByPlaylistSongId(Integer.parseInt(playlist.getIdPlaylist()));
            List<String> idSongsLocal=new ArrayList<>();
        try {
            Response<PlaylistSongResponse> response=call2.execute();
            PlaylistSongResponse playlistSongResponse= response.body();
            idSongsLocal.addAll(playlistSongResponse.getLocalSongIds()) ;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                idSongsLocal.forEach(id -> {
//                    Song tmp= HelpTools.GetMediaMp3FilesByIdPlaylist(id,ThemBaiHatActivity.this);
//                    if(tmp!=null) songList.add(tmp);
//                });
               List<Song> songlocals=HelpTools.GetMediaMp3FilesByIdPlaylist(idSongsLocal,ThemBaiHatActivity.this);
                songList.addAll(songlocals);
            }
            songList.addAll(playlistSongResponse.getInternetSongs());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return songList;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.d("RequestPermissionsR","ok");
        switch (requestCode) {

            case RUNTIME_PERMISSION_CODE: {

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED&& grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    Log.d("onRequestPermissions","ok");
                } else {
                    finish();
                }
            }
        }
    }

    public boolean checkPermissions(){

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            if((checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED)&&(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED)){
                return true;
            }
            if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED||checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){

                if(shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)||shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)){

                    AlertDialog.Builder alert_builder = new AlertDialog.Builder(ThemBaiHatActivity.this);
                    alert_builder.setMessage("External Storage Permission is Required.");
                    alert_builder.setTitle("Please Grant Permission.");
                    alert_builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            ActivityCompat.requestPermissions(
                                    ThemBaiHatActivity.this,
                                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE},
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
                            ThemBaiHatActivity.this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            RUNTIME_PERMISSION_CODE
                    );
                }
            }else {

            }
        }
        return  false;
    }
}