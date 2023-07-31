package com.example.appplaymusic.Models;

import android.net.Uri;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@Generated("jsonschema2pojo")
public class Song implements Serializable {
    public Song(String songName) {
        this.songName = songName;
    }

    @SerializedName("IdSong")
@Expose
private String idSong;
@SerializedName("SongName")
@Expose
private String songName;
@SerializedName("IdPlaylist")
@Expose
private String idPlaylist;
@SerializedName("singerName")
@Expose
private String singerName;
@SerializedName("SongImage")
@Expose
private String songImage;
@SerializedName("SongLink")
@Expose
private String songLink;
private int type=0;
    public Song(String id, String queryTtile, String queryArtist, String queryAlbum, String filepath,int type) {
        this.idSong=id;
        this.songName=queryTtile;
        this.singerName=queryArtist;
        this.songLink=filepath;
        this.type=type;
    }

    public String getIdSong() {
return idSong;
}

public void setIdSong(String idSong) {
this.idSong = idSong;
}

public String getSongName() {
return songName;
}

public void setSongName(String songName) {
this.songName = songName;
}

public String getIdPlaylist() {
return idPlaylist;
}

public void setIdPlaylist(String idPlaylist) {
this.idPlaylist = idPlaylist;
}

public String getSingerName() {
return singerName;
}

public void setSingerName(String singerName) {
this.singerName = singerName;
}

public String getSongImage() {
return songImage;
}

public void setSongImage(String songImage) {
this.songImage = songImage;
}

public String getSongLink() {
return songLink;
}

public void setSongLink(String songLink) {
this.songLink = songLink;
}

    public int getType() {
        return type;
    }

    @Override
    public String toString() {
        return "Song{" +
                "idSong='" + idSong + '\'' +
                ", songName='" + songName + '\'' +
                ", idPlaylist='" + idPlaylist + '\'' +
                ", singerName='" + singerName + '\'' +
                ", songImage='" + songImage + '\'' +
                ", songLink='" + songLink + '\'' +
                ", type=" + type +
                '}';
    }
}