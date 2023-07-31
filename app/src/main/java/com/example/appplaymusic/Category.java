package com.example.appplaymusic;

import com.example.appplaymusic.Commons.Common;
import com.example.appplaymusic.Models.Playlist;
import com.example.appplaymusic.Models.Singer;

import java.util.List;

public class Category {
    public int getCategory_code() {
        return category_code;
    }

    public int getShowType() {
        return showType;
    }

    public void setShowType(int showType) {
        this.showType = showType;
    }

    public void setPlaylists(List<Playlist> playlists) {
        this.playlists = playlists;
    }

    public int showType;
    public void setCategory_code(int category_code) {
        this.category_code = category_code;
    }

    private  int category_code;
    private String name;
    List<com.example.appplaymusic.Models.Song> songList;
    List<com.example.appplaymusic.Models.Playlist> playlists;

    public List<Singer> getSingers() {
        return singers;
    }

    public void setSingers(List<Singer> singers) {
        this.singers = singers;
    }

    List<Singer> singers;
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<com.example.appplaymusic.Models.Song> getSongList() {
        return songList;
    }
    public List<com.example.appplaymusic.Models.Playlist> getPlaylists() {
        return playlists;
    }


    public Category(String name) {
        this.name = name; this.showType= Common.TYPE_VERTICAL;
    }
    public Category(String name,List<com.example.appplaymusic.Models.Playlist> playlists) {
        this.name = name;
        this.playlists=playlists;
    }
    public Category(String name,int code,int showType) {
        this.name = name;
        this.category_code=code;
        this.showType=showType;
    }
    public void setSongList(List<com.example.appplaymusic.Models.Song> songList){
        this.songList=songList;
    }
    public void setPlayList(List<com.example.appplaymusic.Models.Playlist> playlists){
        this.playlists=playlists;
    }
    public Playlist getPlaylistById(int id){
        if(this.playlists!=null) return this.playlists.get(id);
        return null;
    }

    @Override
    public String toString() {
        return "Category{" +
                "showType=" + showType +
                ", category_code=" + category_code +
                ", name='" + name + '\'' +
                ", songList=" + songList +
                ", playlists=" + playlists +
                '}';
    }
}
