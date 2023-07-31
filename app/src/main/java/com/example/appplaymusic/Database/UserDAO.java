package com.example.appplaymusic.Database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.appplaymusic.Models.User;

import java.util.List;

@Dao
public interface UserDAO {
    @Insert
    void insertUser(User user);
    @Query("SELECT * from user")
    List<User> getListUser();
    @Update
    void update(User user);
    @Query("delete from user")
    void deleteAll();
    @Delete
    void delete(User user);
    @Query("select * from user where userName= :name")
    User searchByName(String name);
}
