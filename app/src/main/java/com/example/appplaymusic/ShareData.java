package com.example.appplaymusic;

import com.example.appplaymusic.Models.Song;

import java.util.List;

public class ShareData {
    static List<Song> songList;

    public static List<Song> getSongList() {
        return songList;
    }

    public static void setSongList(List<Song> songList) {
        ShareData.songList = songList;
    }
}
