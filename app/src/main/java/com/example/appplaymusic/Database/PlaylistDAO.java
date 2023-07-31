package com.example.appplaymusic.Database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.appplaymusic.Models.Playlist;

import java.util.List;

@Dao
public interface PlaylistDAO {
    @Insert
    void insertPlaylist(Playlist playlist);
    @Query("SELECT * from playlist")
    List<Playlist> getListPlaylist();
    @Update
    void update(Playlist playlist);
    @Query("delete from playlist")
    void deleteAll();
    @Delete
    void delete(Playlist playlist);
    @Query("SELECT COUNT(*) from playlist")
    int count();
    //@Query("select * from playlist where name like '%' || :name ||'%'")
    //List<User> searchByName(String name);
}
