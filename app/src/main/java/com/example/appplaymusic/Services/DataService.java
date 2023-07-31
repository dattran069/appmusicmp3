package com.example.appplaymusic.Services;

import com.example.appplaymusic.Models.CreateUserResponse;
import com.example.appplaymusic.Models.Playlist;
import com.example.appplaymusic.Models.PlaylistSongResponse;
import com.example.appplaymusic.Models.SearchResponse;
import com.example.appplaymusic.Models.SignInResponse;
import com.example.appplaymusic.Models.SignUpResponse;
import com.example.appplaymusic.Models.Singer;
import com.example.appplaymusic.Models.Song;
import com.example.appplaymusic.Models.User;
import com.google.android.gms.tasks.Task;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface DataService {
    @GET("song.php")
    Call<List<Song>> getSongs();
    @GET("categorybyid.php")
    public Call<List<com.example.appplaymusic.Models.Category>> getCategoryById(@Query("categoryId") int id);
    @GET("playlist.php")
    public Call<com.example.appplaymusic.Models.Playlist> getPlaylistById(@Query("playlistId") int id);
    @GET("songs.php")
    public Call<List<com.example.appplaymusic.Models.Song>> getListSongByPlaylistId(@Query("playlistId") int id);
    @GET("getAllPlaylistByCateId.php")
    public Call<List<com.example.appplaymusic.Models.Playlist>> getAllPlaylistByCateId(@Query("categoryId") int id);
    @GET("getAllPlaylistByCateId.php")
    public Call<List<com.example.appplaymusic.Models.Playlist>> getAllPlaylistBySingerId(@Query("singerId") int singerId);
    @GET("countSongByPlaylistId.php")
    public Call<String> countSongByPlaylistId(@Query("playlistId") int id);
    @GET("songsByPlaylistIdLimit.php")
    public Call<List<com.example.appplaymusic.Models.Song>> songsByPlaylistIdLimit(@Query("playlistId") int id,@Query("currentId") int idCur);
    @GET("playlistsLocalByUserId.php")
    public Call<List<com.example.appplaymusic.Models.Playlist>> getPlaylistsLocalByUserId(@Query("userId") int userId);

    @Headers({"Accept: application/json"})
    @FormUrlEncoded
    @POST("createUser.php")
    Call<Integer> createUser(@Field("userName") String userName, @Field("password") String password);
    @Headers({"Accept: application/json"})
    @FormUrlEncoded
    @POST("login.php")
    Call<SignInResponse> login(@Field("userName") String userName, @Field("password") String password);
    @Headers({"Accept: application/json"})
    @FormUrlEncoded
    @POST("createPlaylistLocal.php")
    Call<String> createLocalPlaylist(@Field("userId") int userId, @Field("playlistName") String playlistName);
    @Headers({"Accept: application/json"})
    @FormUrlEncoded
    @POST("createPlaylistSong.php")
    Call<Integer> createPlaylistSong(@Field("idPlaylist") int idPlaylist, @Field("idSong") int idSong,@Field("type") int type);
    @GET("idSongsByPlaylistSongId.php")
    Call<PlaylistSongResponse> getIdSongsByPlaylistSongId(@Query("playlistId") int id);
    @GET("songBySongId.php")
    Call<Song> getSongBySongId(@Query("songId") String songId);
    @GET("songByListSongId.php")
    Call<List<Song>> getSongsFromListId(@Query("listIdJson") String json);
    @GET("deleteSongFromPlaylistLocal.php")
    Call<Void> deleteSongFromPlaylistLocal(@Query("songId") String idSong, @Query("idPlaylist") String idPlaylist);
    @GET("searchByKey.php")
    Call<SearchResponse> searchByKey(@Query("searchKey")  String searchKey);
    @GET("song.php")
    Call<List<Song>> songsLimit(@Query("limit")  int limit);
    @GET("song.php")
    Call<List<Song>> getSongsBySingerId(@Query("singerId") int singerCode);
    @GET("singer.php")
    Call<List<Singer>> getSingerBySingerId(@Query("singerId") int singerId);
    @GET("singer.php")
    Call<List<Singer>> getSingersExceptSingerId(@Query("singerIdExcept") int singerCode);
}
