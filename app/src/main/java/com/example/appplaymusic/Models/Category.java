package com.example.appplaymusic.Models;

import android.net.Uri;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@Generated("jsonschema2pojo")
public class Category implements Serializable {
    Category(){

    }
    @SerializedName("IdCategory")
    @Expose
    private String idCategory;
    @SerializedName("IdPlaylist")
    @Expose
    private String idPlaylist;
    public String getIdCategory() {
        return idCategory;
    }

    public void setIdCategory(String idCategory) {
        this.idCategory = idCategory;
    }

    public String getIdPlaylist() {
        return idPlaylist;
    }

    public void setIdPlaylist(String idPlaylist) {
        this.idPlaylist = idPlaylist;
    }




}