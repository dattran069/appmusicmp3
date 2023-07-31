package com.example.appplaymusic.MainActivity;

import android.app.Activity;
import android.content.Context;
import android.widget.RelativeLayout;

import com.example.appplaymusic.Models.Song;

import java.util.List;

public interface MainActivityView {
    public Context getContext();
    Activity getActivity();

    void setAdapterData(List<Song> songList);
    void updateFooterSong(boolean isPlaying);

    void updateIconPlay(boolean b);

    void addView(RelativeLayout rootView);

    void goToCreatePlaylist();

    void clearSong();
}
