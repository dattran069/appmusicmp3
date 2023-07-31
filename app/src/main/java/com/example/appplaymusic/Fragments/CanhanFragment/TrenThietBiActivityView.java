package com.example.appplaymusic.Fragments.CanhanFragment;

import android.app.Activity;
import android.content.Context;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.appplaymusic.Models.Song;

import java.util.List;

public interface TrenThietBiActivityView {
    int checkSelfPermission(String readExternalStorage);

    boolean shouldShowRequestPermissionRationale(String readExternalStorage);

    Context getContext();

    Activity getActivity();


    void updateFooterSong();

    void updateIconPlay(boolean b);

    void addView(RelativeLayout rootView);

    void goToCreatePlaylist();
    void setAdapterData(List<Song> songList);
}
