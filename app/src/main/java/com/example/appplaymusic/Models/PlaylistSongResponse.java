package com.example.appplaymusic.Models;

import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class PlaylistSongResponse {

    @SerializedName("localSongIds")
    @Expose
    private List<String> localSongIds = null;
    @SerializedName("internetSongs")
    @Expose
    private List<Song> internetSongs = null;

    public List<String> getLocalSongIds() {
        return localSongIds;
    }

    public void setLocalSongIds(List<String> localSongIds) {
        this.localSongIds = localSongIds;
    }

    public List<Song> getInternetSongs() {
        return internetSongs;
    }

    public void setInternetSongs(List<Song> internetSongs) {
        this.internetSongs = internetSongs;
    }

}
