package com.example.appplaymusic.PlaylistNew;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.appplaymusic.Commons.Common;
import com.example.appplaymusic.Database.Database;
import com.example.appplaymusic.Helper.HelpTools;
import com.example.appplaymusic.MainActivity.MainActivity;
import com.example.appplaymusic.Models.Playlist;
import com.example.appplaymusic.R;
import com.google.android.material.textfield.TextInputEditText;

public class PlaylistNewActivity extends AppCompatActivity implements PlaylistNewActivityView{
    Button btnTaoPlaylist;
    ImageView btnClose;
    TextInputEditText textInputEditTextPlaylistName;
    PlaylistNewActivityPresenter presenter;
    SharedPreferences sharedpreferences ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist_new);
        presenter=new PlaylistNewActivityPresenter(this);
        btnClose=findViewById(R.id.img_playlist_new_close);
        btnTaoPlaylist=findViewById(R.id.btnTaoPlaylist);
        textInputEditTextPlaylistName=findViewById(R.id.TextInputEditTextPlaylistName);
        textInputEditTextPlaylistName.requestFocus();
        sharedpreferences= getSharedPreferences(Common.SHARED_PREFS, MODE_PRIVATE);
        HelpTools.showKEyBoard(this);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        textInputEditTextPlaylistName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.length()>0) {
                    btnTaoPlaylist.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        btnTaoPlaylist.setEnabled(false);
        btnTaoPlaylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 String playlistName=textInputEditTextPlaylistName.getText().toString();
                 createPlaylistLocal(playlistName);
            }
        });

    }

    private void createPlaylistLocal(String playlistName) {
        //int count= Database.getInstance(this).playlistDAO().count();
        //Database.getInstance(this).playlistDAO().insertPlaylist(playlist);
        int userId=sharedpreferences.getInt(Common.USERID_KEY, -1);
        if(userId!=-1) presenter.saveLocalPlaylist(userId,playlistName);
        Intent intent=new Intent(PlaylistNewActivity.this, MainActivity.class);
        startActivity(intent);
    }
}