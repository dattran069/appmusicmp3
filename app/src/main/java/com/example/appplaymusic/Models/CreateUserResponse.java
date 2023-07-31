package com.example.appplaymusic.Models;

import com.google.gson.annotations.SerializedName;

public class CreateUserResponse {
    @SerializedName("userName")
    public String name;
    @SerializedName("id")
    public int id;
    @SerializedName("password")
    public String password;
}
