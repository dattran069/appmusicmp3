package com.example.appplaymusic.Models;

import java.io.Serializable;
import java.util.List;

public class PlaylistSongList implements Serializable {
    private Playlist playlist;
    private List<Song> songList;
    public PlaylistSongList(Playlist playlist, List<Song> songList ) {
        this.playlist = playlist;
        this.songList = songList;
    }

    public Playlist getPlaylist() {
        return playlist;
    }

    @Override
    public String toString() {
        return "PlaylistSongList{" +
                "playlist.name=" + playlist.getNamePlaylist() +
                ", songList.size=" + songList.size() +
                '}';
    }

    public void setPlaylist(Playlist playlist) {
        this.playlist = playlist;
    }

    public List<Song> getSongList() {
        return songList;
    }

    public void setSongList(List<Song> songList) {
        this.songList = songList;
    }
}
