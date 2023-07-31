package com.example.appplaymusic.Models;

import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class SearchResponse {

    @SerializedName("songs")
    @Expose
    private List<Song> songs = null;
    @SerializedName("singers")
    @Expose
    private List<Singer> singers = null;
    @SerializedName("playlists")
    @Expose
    private List<Playlist> playlists = null;

    public List<Song> getSongs() {
        return songs;
    }

    public void setSongs(List<Song> songs) {
        this.songs = songs;
    }

    public List<Singer> getSingers() {
        return singers;
    }

    public void setSingers(List<Singer> singers) {
        this.singers = singers;
    }

    public List<Playlist> getPlaylists() {
        return playlists;
    }

    public void setPlaylists(List<Playlist> playlists) {
        this.playlists = playlists;
    }

    @Override
    public String toString() {
        return "SearchResponse{" +
                "songs=" + songs +
                ", singers=" + singers +
                ", playlists=" + playlists +
                '}';
    }
}