package com.example.appplaymusic.Fragments;

import com.example.appplaymusic.Models.Song;

import java.util.List;

public interface OnlineTBHFragmentView {
    void receiveSongList(List<Song> allSong, List<Song> songList,boolean saved);
}
