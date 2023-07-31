package com.example.appplaymusic.XemThemActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.appplaymusic.Adapters.NewSongAdapter;
import com.example.appplaymusic.Adapters.PlaylistAdapter;
import com.example.appplaymusic.Commons.Common;
import com.example.appplaymusic.Fragments.CanhanFragment.CanhanFragment;
import com.example.appplaymusic.Fragments.KhamphaFragment;
import com.example.appplaymusic.MainActivity.MainActivity;
import com.example.appplaymusic.Models.Playlist;
import com.example.appplaymusic.Models.Singer;
import com.example.appplaymusic.Models.Song;
import com.example.appplaymusic.PlaySongActivity;
import com.example.appplaymusic.R;

import java.util.ArrayList;
import java.util.List;

public class XemThemActivity extends AppCompatActivity implements XemThemActivityView{
    Toolbar toolbar;
    RecyclerView recyclerView;
    NewSongAdapter newSongAdapter;
    PlaylistAdapter playlistAdapter;
    List<Playlist> playlistList;
    List<Song> songList;
    LinearLayoutManager linearLayoutManager;
    GridLayoutManager gridLayoutManager;
    Button btnNgauNhien;
    int type;
    Singer singer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xem_them);
        if(getIntent().getExtras()!=null){
            type= (int) getIntent().getExtras().get("xemthem_type");
            singer= (Singer) getIntent().getExtras().get("singer_obj");
            anhXa(type,singer);
        }
        btnNgauNhien.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Common.savePlaylist_ListSongSPS(songList,null,0);
                Song song=Common.getRandomSong();
                Intent intent=new Intent(XemThemActivity.this, PlaySongActivity.class);
                Bundle bundle=new Bundle();
                bundle.putSerializable("song_obj",song);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    private void anhXa(int type,Singer singer) {
        String singerName="";
        if(singer!=null){
            singerName=singer.getSingerName();
        }
        btnNgauNhien=findViewById(R.id.btn_phatNgauNhien);
        toolbar=findViewById(R.id.toolbar);
        recyclerView=findViewById(R.id.rcv_xemthem);
        com.example.appplaymusic.DividerItemDecoration dividerItemDecoration = new com.example.appplaymusic.DividerItemDecoration(this, DividerItemDecoration.VERTICAL,false);
        recyclerView.addItemDecoration(dividerItemDecoration);
        if(type== Common.XEMTHEM_SONG){
            toolbar.setTitle("Bài hát nổi bật "+singerName);
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            List<Song> songList= (List<Song>) getIntent().getExtras().get("list_data");
//            Log.d("datanewSongAdapter", String.valueOf(songList.size()));
            this.songList=songList;
            LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
            NewSongAdapter newSongAdapter=new NewSongAdapter();
            recyclerView.setAdapter(newSongAdapter);
            recyclerView.setLayoutManager(linearLayoutManager);
            newSongAdapter.setData(this.songList,getApplicationContext(),false,false);
            newSongAdapter.setiClickListener(new NewSongAdapter.IClickListener() {
                @Override
                public void onClickListener(Song song, int position) {
                    Common.savePlaylistSonglist(position,songList,null);
                    //save current song => index of song
                    Common.setCurrentIndex(position);
                    Intent intent=new Intent(XemThemActivity.this, PlaySongActivity.class);
                    Bundle bundle=new Bundle();
                    bundle.putSerializable("song_obj",song);
                    intent.putExtras(bundle);
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
        } else {
            toolbar.setTitle("Playlists "+singerName);
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            List<com.example.appplaymusic.Models.Playlist> playlists=(List<com.example.appplaymusic.Models.Playlist>) getIntent().getExtras().get("list_data");
            playlistAdapter=new PlaylistAdapter(getApplicationContext());
            this.playlistList=playlists;
            playlistAdapter.setData(this.playlistList);
            GridLayoutManager gridLayoutManager=new GridLayoutManager(getApplicationContext(),2);
            recyclerView.setAdapter(playlistAdapter);
            recyclerView.setLayoutManager(gridLayoutManager);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case android.R.id.home:
                finish();
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}