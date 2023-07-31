package com.example.appplaymusic.SingerActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.appplaymusic.Adapters.NewSongAdapter;
import com.example.appplaymusic.Adapters.PlaylistAdapter;
import com.example.appplaymusic.Category;
import com.example.appplaymusic.CategoryAdapter;
import com.example.appplaymusic.CategoryNgheSiAdapter;
import com.example.appplaymusic.Commons.Common;
import com.example.appplaymusic.Fragments.CanhanFragment.CanhanFragment;
import com.example.appplaymusic.Fragments.KhamphaFragment;
import com.example.appplaymusic.Fragments.PlaylistFragment.PlaylistFragment;
import com.example.appplaymusic.MainActivity.MainActivity;
import com.example.appplaymusic.Models.Playlist;
import com.example.appplaymusic.Models.Singer;
import com.example.appplaymusic.Models.Song;
import com.example.appplaymusic.PlaySongActivity;
import com.example.appplaymusic.R;
import com.example.appplaymusic.XemThemActivity.XemThemActivity;
import com.example.appplaymusic.XemThemActivity.XemThemActivityView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SingerActivity extends AppCompatActivity implements SingerActivityView{
    CategoryNgheSiAdapter categoryAdapter;
    RecyclerView recyclerViewCategory;
    SingerActivityPresenter presenter;
    ImageView imgSingerBack;
    ImageView imgSingerAvar;
    TextView txtSingerName;
    TextView txtSingerBirthday;
    TextView txtSingerCountry;
    TextView txtSingerStyle;
    TextView txtSingerInfo;
    LinearLayout linear_thongtin_singer,linear_singer_content;
    Toolbar toolbar;
    Singer singer;
    int singerCode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_singer);
        anhXa();

        if(getIntent().getExtras()!=null){
            singer= (Singer) getIntent().getExtras().get("singer_obj");
            receiveSinger(singer);
            toolbar.setTitle("");
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        categoryAdapter=new CategoryNgheSiAdapter();
        categoryAdapter.setIcSonglickListener(new CategoryNgheSiAdapter.ICSonglickListener() {
            @Override
            public void onSongClickListener(Song song,int position,List<Song> songList,Playlist playlist) {

                Common.savePlaylistSonglist(position,songList,playlist);
                //save current song => index of song
                //Common.setCurrentIndex(position);
                Intent intent=new Intent(SingerActivity.this, PlaySongActivity.class);
                Bundle bundle=new Bundle();
                bundle.putSerializable("song_obj",song);
                intent.putExtras(bundle);
                startActivity(intent);
            }

            @Override
            public void onPlaylistClickListener(Playlist playlist) {
                linear_singer_content.setVisibility(View.GONE);
                getSupportFragmentManager().beginTransaction().add(R.id.fragment_container,new PlaylistFragment(playlist,Common.SINGER_PAGE)).commit();
            }

            @Override
            public void onSingerClickListener(String id, Singer singer) {
                Intent intent =new Intent(SingerActivity.this, SingerActivity.class);
                Bundle bundle=new Bundle();
                bundle.putSerializable("singer_obj",singer);
                intent.putExtras(bundle);
                startActivity(intent);
            }

            @Override
            public void onXemThemClickListener(int categoryId, NewSongAdapter newSongAdapter, PlaylistAdapter playlistAdapter) {
                switch (categoryId){
                    case Common.CATEGORY_NGHESI_BAIHATNOIBAT:
                        //Toast.makeText(SingerActivity.this, "CATEGORY_NGHESI_BAIHATNOIBAT", Toast.LENGTH_SHORT).show();
                        //Log.d("datanewSongAdapter", String.valueOf(newSongAdapter.getData().size()));
                        Intent intent=new Intent(SingerActivity.this, XemThemActivity.class);
                        Bundle bundle=new Bundle();
                        bundle.putSerializable("list_data", (Serializable) newSongAdapter.getData());
                        bundle.putInt("xemthem_type",Common.XEMTHEM_SONG);
                        bundle.putSerializable("singer_obj",singer);
                        intent.putExtras(bundle);
                        startActivity(intent);
                        break;
                    case Common.CATEGORY_NGHESI_PLAYLIST:
                        Intent intent2=new Intent(SingerActivity.this, XemThemActivity.class);
                        Bundle bundle2=new Bundle();
                        bundle2.putSerializable("list_data", (Serializable) playlistAdapter.getData());
                        bundle2.putInt("xemthem_type",Common.XEMTHEM_PLAYLIST);
                        bundle2.putSerializable("singer_obj",singer);
                        intent2.putExtras(bundle2);
                        startActivity(intent2);
                        break;
                    case Common.CATEGORY_NGHESI_COTHEBANSETHICH:
                        Toast.makeText(SingerActivity.this, "CATEGORY_NGHESI_COTHEBANSETHICH", Toast.LENGTH_SHORT).show();

                        break;
                }
            }
        });
        recyclerViewCategory.setAdapter(categoryAdapter);
        recyclerViewCategory.setLayoutManager(linearLayoutManager);
        presenter=new SingerActivityPresenter(getApplicationContext(),this);
        //presenter.loadInfoSinger(1);
        //set anh

        getCategoryData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        linear_singer_content.setVisibility(View.VISIBLE);
    }

    private void anhXa() {
        recyclerViewCategory=findViewById(R.id.rcv_category_nghesi);
        imgSingerAvar=findViewById(R.id.img_singer_avar);
        imgSingerBack=findViewById(R.id.img_singer_back);
        txtSingerName=findViewById(R.id.txt_singer_name_singerPage);
        txtSingerBirthday=findViewById(R.id.txt_singer_birthday_singerPage);
        txtSingerCountry=findViewById(R.id.txt_singer_country_singerPage);
        txtSingerStyle=findViewById(R.id.txt_singer_style_singerPage);
        linear_thongtin_singer=findViewById(R.id.linear_thongtin_singer);
        txtSingerInfo=findViewById(R.id.txt_thongtin);
        linear_thongtin_singer.setVisibility(View.GONE);
        linear_singer_content=findViewById(R.id.linear_singer_content);
        toolbar=findViewById(R.id.toolbar);
    }

    private void getCategoryData() {
        List<com.example.appplaymusic.Category> categoryList=new ArrayList<>();
        categoryList.add(new Category("Bài hát nổi bật", Common.CATEGORY_NGHESI_BAIHATNOIBAT,Common.TYPE_VERTICAL));
        categoryList.add(new Category("Playlist", Common.CATEGORY_NGHESI_PLAYLIST,Common.TYPE_HORI));
        categoryList.add(new Category("Có thể bạn sẽ thích", Common.CATEGORY_NGHESI_COTHEBANSETHICH,Common.TYPE_HORI));
        categoryAdapter.setData(categoryList,getApplicationContext(), Integer.parseInt(singer.getId()));
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

    @Override
    public void receiveSinger(Singer singer) {

        linear_thongtin_singer.setVisibility(View.VISIBLE);
        this.singer=singer;
        Log.d("receiveSinger",singer.toString());
        Glide.with(this).load(singer.getBackgroundImage()).into(imgSingerBack);
        Glide.with(this).load(singer.getSingerImage()).into(imgSingerAvar);
        txtSingerName.setText(singer.getSingerName());
        String birthdayrev=singer.getBirthdate();
        String[] str=birthdayrev.split("-");
        txtSingerBirthday.setText(str[2]+"/"+str[1]+"/"+str[0]);
        txtSingerCountry.setText("Việt Nam");
        txtSingerStyle.setText("Trữ tình, Hip Hop");
        txtSingerInfo.setText(singer.getInfo());
    }

}