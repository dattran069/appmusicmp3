package com.example.appplaymusic.Fragments.TimKiemFragment;

import com.example.appplaymusic.Models.Playlist;
import com.example.appplaymusic.Models.Singer;
import com.example.appplaymusic.Models.Song;

import java.util.List;

public interface TimKiemFragmentView {

    void show(List<Song> songs, List<Playlist> playlists, List<Singer> singers);
}
