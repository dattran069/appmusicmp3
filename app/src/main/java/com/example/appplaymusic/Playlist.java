package com.example.appplaymusic;

import com.example.appplaymusic.Models.Song;

import java.io.Serializable;
import java.util.List;

public class Playlist implements Serializable {
    private String ngheSi;

    public String getPath() {
        return imgPath;
    }

    public void setPath(String path) {
        this.imgPath = path;
    }

    private String imgPath;
    private  int imgResId;
    public Playlist(int imgResId, String theLoai, float capNhat, List<com.example.appplaymusic.Models.Song> songList, String moTa) {
        this.imgResId = imgResId;
        this.theLoai = theLoai;
        this.capNhat = capNhat;
        this.songList = songList;
        this.moTa = moTa;
    }



    public List<com.example.appplaymusic.Models.Song> getSongList() {
        return songList;
    }

    public void setSongList(List<Song> songList) {
        this.songList = songList;
    }

    private String theLoai;
    private float capNhat;
    private List<com.example.appplaymusic.Models.Song> songList;


    public String getTheLoai() {
        return theLoai;
    }

    public void setTheLoai(String theLoai) {
        this.theLoai = theLoai;
    }

    public float getCapNhat() {
        return capNhat;
    }

    public void setCapNhat(float capNhat) {
        this.capNhat = capNhat;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    public int getImgResId() {
        return imgResId;
    }

    public void setImgResId(int imgResId) {
        this.imgResId = imgResId;
    }

    public Playlist(int imgResId, String theLoai, float capNhat, String moTa) {
        this.imgResId = imgResId;
        this.theLoai = theLoai;
        this.capNhat = capNhat;
        this.moTa = moTa;
    }

    private String moTa;

}
