package com.example.appplaymusic.Database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.appplaymusic.Models.Playlist;
import com.example.appplaymusic.Models.PlaylistSong;
import com.example.appplaymusic.Models.User;

@androidx.room.Database(entities = {User.class,Playlist.class,PlaylistSong.class},version = 1)

public abstract class Database extends RoomDatabase {
    private static final String DATABASE_NAME="mp3LocalDatabase.db";
    static Migration migration_1_to_2=new Migration(1,2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("ALTER table Playlist ADD COLUMN isLocal INTEGER");
        }
    };
    private static Database instance;
    public static synchronized Database getInstance(Context context){
if(instance ==null) instance= Room.databaseBuilder(context.getApplicationContext(), Database.class,DATABASE_NAME)
        .allowMainThreadQueries()
        .build();
return  instance;
    }
    public  abstract UserDAO userDAO();
    public  abstract PlaylistDAO playlistDAO();
    public  abstract PlaylistSongDAO playlistSongDAO();

}
