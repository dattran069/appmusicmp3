package com.example.appplaymusic.Models;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(primaryKeys = {"idSong","idPlaylist"},tableName ="playlistsong")
public class PlaylistSong implements Serializable {
    @NonNull
    private String idSong;
    @NonNull
    private String idPlaylist;
    public PlaylistSong(String idSong,String idPlaylist){
        this.idPlaylist=idPlaylist;
        this.idSong=idSong;
    }

    public String getIdSong() {
        return idSong;
    }

    public void setIdSong(String idSong) {
        this.idSong = idSong;
    }

    public String getIdPlaylist() {
        return idPlaylist;
    }

    public void setIdPlaylist(String idPlaylist) {
        this.idPlaylist = idPlaylist;
    }
}
