package com.example.appplaymusic.Database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.appplaymusic.Models.Playlist;
import com.example.appplaymusic.Models.PlaylistSong;

import java.util.List;

@Dao
public interface PlaylistSongDAO {
    @Insert
    void insertPlaylistSong(PlaylistSong playlistSong);
    @Query("SELECT * from playlistsong")
    List<PlaylistSong> getListPlaylistSong();
    @Update
    void update(PlaylistSong playlistSong);
    @Query("delete from playlistsong")
    void deleteAll();
    @Delete
    void delete(PlaylistSong playlistSong);
    //@Query("select * from playlist where name like '%' || :name ||'%'")
    //List<User> searchByName(String name);
    @Query("select idSong from playlistsong where idPlaylist=:idplaylist")
    List<String> getSongByPlaylistId(String idplaylist);
}
