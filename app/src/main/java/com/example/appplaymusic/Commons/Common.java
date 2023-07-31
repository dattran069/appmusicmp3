package com.example.appplaymusic.Commons;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.appplaymusic.Database.Database;
import com.example.appplaymusic.MainActivity.MainActivity;
import com.example.appplaymusic.Models.Playlist;
import com.example.appplaymusic.Models.PlaylistSongList;
import com.example.appplaymusic.Models.Song;
import com.example.appplaymusic.Models.User;
import com.example.appplaymusic.Services.APIService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Random;

public class Common {
    public static final String SHARED_PREFS = "shared_prefs";
    public static final String USERNAME_KEY = "username_key";
    public static final int LOCAL_TYPE = 1;
    public static final int INTERNET_TYPE = 0;
    public static final String PASSWORD_KEY = "password_key";
    public static final String URLMP3="https://appmusic274.000webhostapp.com/mp3/";
    public static final String DIRECTORY_SAVE_MP3="AppPlayMusic Downloads";
    public static final String PLAYLIST_LISTSONG_KEY = "playlist_listsong_key";
    public static final String USERID_KEY = "userid_key";
    public static final String CURRENT_INDEX_KEY = "current_key";
    public static final String DIRECTORY_SAVE_IMG_MP3 ="MP3_IMAGES"   ;
    public static final int TYPE_HORI=5;
    public static final int TYPE_VERTICAL=6;
    public static final int CATEGORY_NGHESI_BAIHATNOIBAT = 10;
    public static final int CATEGORY_NGHESI_PLAYLIST = 11;
    public static final int CATEGORY_NGHESI_XUATHIENTRONG = 12;
    public static final int CATEGORY_NGHESI_COTHEBANSETHICH = 13;
    public static final int MAINACTIVITY_PAGE = 1;
    public static final int SINGER_PAGE = 2;
    public static final int XEMTHEM_SONG = 69;
    public static final int XEMTHEM_PLAYLIST = 27;
    public static User getCurrentUser() {
        return currentUser;
    }
    public static void setCurrentUser(User currentUser) {
        Common.currentUser = currentUser;
    }
    static SharedPreferences sharedPreferences;
    static User currentUser;
    static Playlist playlist;
    static List<Song> songList;
    static int currentIndex;

    public static int getCurrentIndex() {
        return currentIndex;
    }
    public static Song getRandomSong() {
        if(currentIndex==-1){
            currentIndex=0;
        }
        if(songList==null) return null;
        if(currentIndex>=songList.size()) return null;
        int size=songList.size();
        int randomIndex=currentIndex;
        if(songList.size()==1) {
            currentIndex=0;
            return songList.get(currentIndex);
        }
        while(randomIndex==currentIndex){
            Random random1=new Random();
            randomIndex=random1.nextInt(size);
        }
        setCurrentIndex(randomIndex);
        currentIndex=randomIndex;
        return songList.get(currentIndex);
    }
    public static void setCurrentIndex(int currentIndex) {
        Log.d("save","current index "+currentIndex);
        Common.currentIndex = currentIndex;
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(CURRENT_INDEX_KEY,currentIndex);
        editor.commit();
    }
    public static Song getPreSong() {
        if(currentIndex==0)
            setCurrentIndex(songList.size()-1); else setCurrentIndex(currentIndex-1);
        return  songList.get(getCurrentIndex());
    }
    public static Song getNextSong() {
        if(currentIndex==songList.size()-1)
            setCurrentIndex(0); else setCurrentIndex(currentIndex+1);
        return  songList.get(getCurrentIndex());
    }
    public static Song getCurrentSong() {
        if(currentIndex==-1){
            currentIndex=0;
        }
        if(songList==null) {
            Log.d("songlistCI", String.valueOf(currentIndex));
            Log.d("songlist","null");
            return null;
        }
        if(currentIndex>=songList.size()) {
            Log.d("songlist","out range");
            return null;
        }
        return songList.get(currentIndex);
    }
    public static Playlist getPlaylist() {
        return playlist;
    }

    public static void setPlaylist(Playlist playlist) {
        Common.playlist = playlist;
    }

    public static List<Song> getSongList() {
        return songList;
    }

    public static void setSongList(List<Song> songList) {
        Common.songList = songList;
    }

    public static void savePlaylist_ListSongSPS(List<Song> songList, Playlist playlist,int index){
        setPlaylist(playlist);
        setSongList(songList);
        setCurrentIndex(index);
        PlaylistSongList psl=new PlaylistSongList(playlist,songList);
        Gson  gson=new Gson();
        String json=gson.toJson(psl);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(PLAYLIST_LISTSONG_KEY,json);
        Log.d("save","p-songList: "+json);
        editor.commit();
    }
    public static PlaylistSongList getPlaylist_ListSongSPS(Context mContext){
        sharedPreferences= mContext.getSharedPreferences(SHARED_PREFS,Context.MODE_PRIVATE);
        Gson gson=new Gson();
        Type type= new TypeToken<PlaylistSongList>(){}.getType();
        PlaylistSongList pls=gson.fromJson(sharedPreferences.getString(PLAYLIST_LISTSONG_KEY,""),type);
        if(pls==null) return null;
        setPlaylist(pls.getPlaylist());
        setSongList(pls.getSongList());
        setCurrentIndex(sharedPreferences.getInt(CURRENT_INDEX_KEY,0));
        Log.d("PlaylistSongList",pls.getSongList().get(0).getSongName());
        //Log.d("PlaylistSongList",pls.toString());
        Log.d("Pls currentIndex", String.valueOf(sharedPreferences.getInt(CURRENT_INDEX_KEY,0)));
        return pls;
    }


    public static  int getUserId(Context mContext){
        sharedPreferences= mContext.getSharedPreferences(SHARED_PREFS,Context.MODE_PRIVATE);
        return sharedPreferences.getInt(USERID_KEY,-1);
    }
    public static  String getUserName(Context mContext){
        sharedPreferences= mContext.getSharedPreferences(SHARED_PREFS,Context.MODE_PRIVATE);
        return sharedPreferences.getString(USERNAME_KEY,null);
    }
    public static  String getPassword(Context mContext){
        sharedPreferences= mContext.getSharedPreferences(SHARED_PREFS,Context.MODE_PRIVATE);
        return sharedPreferences.getString(PASSWORD_KEY,null);
    }

    public static void removeSongCurrent(Context mContext) {
        sharedPreferences= mContext.getSharedPreferences(SHARED_PREFS,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.remove(CURRENT_INDEX_KEY);
        editor.remove(PLAYLIST_LISTSONG_KEY);
        editor.commit();
    }
    public static void savePlaylistSonglist(int currentindex,List<Song> songList,Playlist playlist){
        Common.savePlaylist_ListSongSPS(songList,playlist,currentindex);
    }
}
