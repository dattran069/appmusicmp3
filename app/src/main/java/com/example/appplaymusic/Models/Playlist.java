package com.example.appplaymusic.Models;
import android.graphics.Bitmap;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Arrays;

@Generated("jsonschema2pojo")
@Entity(tableName = "playlist")
public class Playlist implements Serializable {
    public Playlist(String idPlaylist, String namePlaylist, String image, String imageIcon) {
        this.idPlaylist = idPlaylist;
        this.namePlaylist = namePlaylist;
        this.image = image;
        this.imageIcon = imageIcon;
    }

    @PrimaryKey()
    @NonNull
    @SerializedName("IdPlaylist")
    @Expose
    private String idPlaylist;
    @SerializedName("NamePlaylist")
    @Expose
    private String namePlaylist;
    @SerializedName("Image")
    @Expose
    private String image;
    @SerializedName("ImageIcon")
    @Expose
    private String imageIcon;

    public String getSingerName() {
        return singerName;
    }

    public void setSingerName(String ingerName) {
        this.singerName = ingerName;
    }
    @Ignore
    public Playlist(@NonNull String idPlaylist, String namePlaylist, String image, String imageIcon, String ingerName, int type) {
        this.idPlaylist = idPlaylist;
        this.namePlaylist = namePlaylist;
        this.image = image;
        this.imageIcon = imageIcon;
        this.singerName = ingerName;
        this.type = type;
    }

    @SerializedName("SingerName")
    @Expose
    private String singerName;
    @SerializedName("Type")
    @Expose
    private int type;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getIdPlaylist() {
        return idPlaylist;
    }

    public void setIdPlaylist(String idPlaylist) {
        this.idPlaylist = idPlaylist;
    }

    public String getNamePlaylist() {
        return namePlaylist;
    }

    public void setNamePlaylist(String namePlaylist) {
        this.namePlaylist = namePlaylist;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImageIcon() {
        return imageIcon;
    }

    public void setImageIcon(String imageIcon) {
        this.imageIcon = imageIcon;
    }

    @Override
    public String toString() {
        return "Playlist{" +
                "idPlaylist='" + idPlaylist + '\'' +
                ", namePlaylist='" + namePlaylist + '\'' +
                ", image='" + image + '\'' +
                ", imageIcon='" + imageIcon + '\'' +
                ", type=" + type +
                ", singerName=" + singerName +
                '}';
    }

    public byte[] getLocalBitmap() {
        return localBitmap;
    }

    public void setLocalBitmap(byte[] localBitmap) {
        this.localBitmap = localBitmap;
    }

    private  byte[] localBitmap;



    public String getCreaterNamePlaylist() {
        return "tui";
    }
}
