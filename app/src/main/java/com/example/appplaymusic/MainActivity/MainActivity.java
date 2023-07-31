package com.example.appplaymusic.MainActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.appplaymusic.Adapters.ViewPagerAdapter;
import com.example.appplaymusic.Category;
import com.example.appplaymusic.CategoryAdapter;
import com.example.appplaymusic.Commons.Common;
import com.example.appplaymusic.Fragments.CanhanFragment.CanhanFragment;
import com.example.appplaymusic.Fragments.KhamphaFragment;
import com.example.appplaymusic.Helper.HelpTools;
import com.example.appplaymusic.Models.Playlist;
import com.example.appplaymusic.Models.Song;
import com.example.appplaymusic.PlaySongActivity;
import com.example.appplaymusic.R;
import com.example.appplaymusic.Services.PlayMusicService;
import com.example.appplaymusic.SingerActivity.SingerActivity;
import com.example.appplaymusic.SpinActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MainActivityView{
    ViewPager viewPager;
    ViewPagerAdapter viewPagerAdapter;
    BottomNavigationView bottomNavigationView;
    private TextView song_name;
    private TextView sing_name;
    private ImageView icon_footer_heart;
    private ImageView icon_footer_play;
    private ImageView icon_footer_pause;
    private ImageView icon_footer_next;
    private ImageView song_avar;
    private RelativeLayout RelativeLayout_footer;
    private MainActivityPresenter mainActivityPresenter;
    private static int currentSong=-1;


    @Override
    protected void onResume() {
        Log.d("MainActivity","onResume"+currentSong);
        updateFooterSong(PlaySongActivity.Playing);
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mainActivityPresenter.sendAction2Service(PlayMusicService.ACTION_CLEAR);
    }

    @Override
    protected void onRestart() {
        Log.d("MainActivity","onRestart"+currentSong);
        if(Common.getCurrentIndex()>=0){
            RelativeLayout_footer.setVisibility(View.VISIBLE);
        }
        super.onRestart();

    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("MainActivity","onPause"+currentSong);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewPager=findViewById(R.id.viewPager_container);
        viewPagerAdapter=new ViewPagerAdapter(getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        viewPager.setAdapter(viewPagerAdapter);
        bottomNavigationView=findViewById(R.id.BottomNavigationView);
        RelativeLayout_footer=findViewById(R.id.layout_footer_song);
        if(Common.getCurrentIndex()>=0) RelativeLayout_footer.setVisibility(View.VISIBLE);
        else RelativeLayout_footer.setVisibility(View.GONE);
        song_avar=findViewById(R.id.img_song_avar_footer);
        song_name=findViewById(R.id.txt_song_name_footer);
        sing_name=findViewById(R.id.txt_singer_name_footer);
        icon_footer_heart=findViewById(R.id.icon_heart_footer);
        icon_footer_play=findViewById(R.id.icon_play_footer);
        icon_footer_pause=findViewById(R.id.icon_pause_footer);
        icon_footer_next=findViewById(R.id.icon_next_footer);
        mainActivityPresenter=new MainActivityPresenter(this);
        RelativeLayout_footer.setOnClickListener(view -> {
            Song song=Common.getCurrentSong();
            boolean isMiniSong=true;
//            Intent intent=new Intent(MainActivity.this,PlaySongActivity.class);
//            intent.putExtra("song_obj",song);
//            intent.putExtra("miniSongBottom",isMiniSong);

            Intent intent=new Intent(MainActivity.this,PlaySongActivity.class);
            Bundle bundle=new Bundle();
            bundle.putSerializable("song_obj",song);
            bundle.putBoolean("miniSongBottom",true);
            intent.putExtras(bundle);
            startActivity(intent);
        });
        icon_footer_pause.setOnClickListener(view -> mainActivityPresenter.pauseSong());
        icon_footer_play.setOnClickListener(view -> mainActivityPresenter.playSong());
        icon_footer_next.setOnClickListener(view -> mainActivityPresenter.playNextSong(Common.getNextSong()));
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position){
                    case 0:
                        bottomNavigationView.getMenu().findItem(R.id.ca_nhan).setChecked(true);
                        break;
                    case 1:
                        bottomNavigationView.getMenu().findItem(R.id.kham_pha).setChecked(true);
                        break;
                    case 2:
                        bottomNavigationView.getMenu().findItem(R.id.xu_huong).setChecked(true);
                        break;
                    case 3:
                        bottomNavigationView.getMenu().findItem(R.id.theo_doi).setChecked(true);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.ca_nhan:
                        replaceFragment(new KhamphaFragment(),1);
                        viewPager.setCurrentItem(0);
                        break;
                    case R.id.kham_pha:
                        replaceFragment(new CanhanFragment(),0);
                        viewPager.setCurrentItem(1);
//                        Intent intent=new Intent(MainActivity.this, SpinActivity.class);
//                        startActivity(intent);
                        break;
                    case R.id.xu_huong:
//                        viewPager.setCurrentItem(2);
                     Intent intent=new Intent(MainActivity.this, SingerActivity.class);
                     startActivity(intent);
                        break;
                    case R.id.theo_doi:
                        viewPager.setCurrentItem(3);
                        break;
                }
                return true;
            }
        });
        Common.getPlaylist_ListSongSPS(this);
        updateFooterSong(false);
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

    }
    private void updateFooterSongIcon(ImageView icon_play, ImageView icon_pause,boolean isPlaying) {

        if(isPlaying)
        {
            startAnimation(song_avar);
            icon_play.setVisibility(View.GONE);
            icon_pause.setVisibility(View.VISIBLE);
        } else{
            stopAnimation(song_avar);
            icon_play.setVisibility(View.VISIBLE);
            icon_pause.setVisibility(View.GONE);
        }
    }
    @Override
    public void updateFooterSong(boolean isPlaying) {
        if(Common.getCurrentSong()==null) {
            RelativeLayout_footer.setVisibility(View.GONE);
            return;
        }
        Log.d("currentSong",Common.getCurrentSong().getSongName());
        RelativeLayout_footer.setVisibility(View.VISIBLE);
        song_name.setText(Common.getCurrentSong().getSongName());
        sing_name.setText(Common.getCurrentSong().getSingerName());
        if (Common.getCurrentSong().getType()==1) {
            song_avar.setImageBitmap(HelpTools.getBitmapImgSongLocal(Common.getCurrentSong(),getContext()));
        }
        else Glide.with(getApplicationContext()).load(Common.getCurrentSong().getSongImage()).into(song_avar);
        updateFooterSongIcon(icon_footer_play,icon_footer_pause,isPlaying);
    }

    public static int getCurrentIndexSong(){
        return currentSong;
    }
    @Override
    public void updateIconPlay(boolean isPlaying) {
        if(isPlaying) {
            startAnimation(song_avar);
            icon_footer_pause.setVisibility(View.VISIBLE);
            icon_footer_play.setVisibility(View.GONE);
        }
        else {
            stopAnimation(song_avar);
            icon_footer_play.setVisibility(View.VISIBLE);
            icon_footer_pause.setVisibility(View.GONE);
        }
    }

    @Override
    public void addView(RelativeLayout rootView) {

    }

    @Override
    public void goToCreatePlaylist() {

    }

    @Override
    public void clearSong() {
        RelativeLayout_footer.setVisibility(View.GONE);
        //Common.removeSongCurrent(getContext());
    }

    public void replaceFragment(Fragment playlistFragment,int pos) {
//        switch (pos){
//            case 0:
//                break;
//            case 1:
//                viewPager.getAdapter().
//                break;
//        }
        viewPagerAdapter.replaceFragment(playlistFragment,pos);
    }
    private void startAnimation(ImageView avar) {
        Runnable runnable=new Runnable() {
            @Override
            public void run() {
                avar.animate().rotationBy(360).withEndAction(this).setDuration(35000)
                        .setInterpolator(new LinearInterpolator()).start();
            }
        };
        avar.animate().rotationBy(360).withEndAction(runnable).setDuration(35000)
                .setInterpolator(new LinearInterpolator()).start();
    }
    private void stopAnimation(ImageView avar) {
        avar.animate().cancel();
    }

}