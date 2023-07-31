package com.example.appplaymusic.Fragments.PlaylistFragment;

import com.example.appplaymusic.Models.Song;

import java.util.List;

public interface PlaylistFragmentView {


    void setSongAdapterData(List<Song> songList);

    void add(List<Song> songList);

    void setSongAdapterLoader();

    void removeLoadMoreBtn();
}
