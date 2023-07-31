package com.example.appplaymusic;

import java.io.Serializable;

public class Song implements Serializable {
    private  String name;
    private  int idResImg;
    private  int idResMp3;
    private  boolean isSameSong;

    public boolean isSameSong() {
        return isSameSong;
    }

    public void setSameSong(boolean sameSong) {
        isSameSong = sameSong;
    }

    public Song(String name, int idResImg, int idResMp3, String singer_name) {
        this.name = name;
        this.idResImg = idResImg;
        this.idResMp3 = idResMp3;
        this.singer_name = singer_name;
    }

    public int getIdResMp3() {
        return idResMp3;
    }

    public void setIdResMp3(int idResMp3) {
        this.idResMp3 = idResMp3;
    }

    public String getSinger_name() {
        return singer_name;
    }

    public void setSinger_name(String singer_name) {
        this.singer_name = singer_name;
    }

    private  String singer_name;
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIdResImg() {
        return idResImg;
    }

    public void setIdResImg(int idResImg) {
        this.idResImg = idResImg;
    }

    public Song(String name, int idResImg) {
        this.name = name;
        this.idResImg = idResImg;
    }
}
