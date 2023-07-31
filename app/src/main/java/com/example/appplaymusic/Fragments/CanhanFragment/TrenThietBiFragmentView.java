package com.example.appplaymusic.Fragments.CanhanFragment;

import android.content.Context;
import android.widget.RelativeLayout;

import com.example.appplaymusic.Models.Song;

import java.util.List;

public interface TrenThietBiFragmentView {
    void addView(RelativeLayout rootView);

    void goToCreatePlaylist();
    void setAdapterData(List<Song> songList);

    Context getContext();
}
