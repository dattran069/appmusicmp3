package com.example.appplaymusic.Models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import javax.annotation.Generated;

@Entity(tableName = "user")
@Generated("jsonschema2pojo")
public class User implements Serializable {
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    @SerializedName("Id")
    @PrimaryKey(autoGenerate = true)
    private int id;
    @SerializedName("UserName")
    @Expose
    private String userName;
    @SerializedName("Password")
    @Expose
    private String password;

    public User(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
