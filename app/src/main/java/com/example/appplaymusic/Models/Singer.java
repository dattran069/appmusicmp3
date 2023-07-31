package com.example.appplaymusic.Models;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@Generated("jsonschema2pojo")
public class Singer implements Serializable {
    @SerializedName("Id")
    @Expose
    private String id;
    @SerializedName("SingerName")
    @Expose
    private String singerName;
    @SerializedName("Name")
    @Expose
    private String name;
    @SerializedName("SingerImage")
    @Expose
    private String singerImage;
    @SerializedName("Birthdate")
    @Expose
    private String birthdate;
    @SerializedName("Info")
    @Expose
    private String info;
    @SerializedName("BackgroundImage")
    @Expose
    private String backgroundImage;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSingerName() {
        return singerName;
    }

    public void setSingerName(String singerName) {
        this.singerName = singerName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSingerImage() {
        return singerImage;
    }

    public void setSingerImage(String singerImage) {
        this.singerImage = singerImage;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getBackgroundImage() {
        return backgroundImage;
    }

    public void setBackgroundImage(String backgroundImage) {
        this.backgroundImage = backgroundImage;
    }

    @Override
    public String toString() {
        return "Singer{" +
                "id='" + id + '\'' +
                ", singerName='" + singerName + '\'' +
                ", name='" + name + '\'' +
                ", singerImage='" + singerImage + '\'' +
                ", birthdate='" + birthdate + '\'' +
                ", info='" + info + '\'' +
                ", backgroundImage='" + backgroundImage + '\'' +
                '}';
    }
}

