package com.example.appplaymusic.Helper;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.ActivityManager;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.example.appplaymusic.Commons.Common;
import com.example.appplaymusic.Models.Song;
import com.example.appplaymusic.R;
import com.example.appplaymusic.Services.PlayMusicService;
import com.google.android.material.textfield.TextInputEditText;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class HelpTools {
//    public static Bitmap getBitmapImgSongLocal(String path){
//        if(path==null) return null;
//       Bitmap image = null;
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
//            image = ThumbnailUtils.createAudioThumbnail(path, MediaStore.Images.Thumbnails.MICRO_KIND);
//
//        }
//        return image;
//    }
public static Bitmap getBitmapImgSongLocal(Song song, Context mContext){
    Bitmap bitmap=null;
    File directory = new File(
            Environment.getExternalStorageDirectory() + "/"
                    + Common.DIRECTORY_SAVE_MP3+"/"+Common.DIRECTORY_SAVE_IMG_MP3);
    File file = new File(directory, song.getIdSong() + ".png");
    Log.d("fildechild",file.getPath());
    FileInputStream streamIn = null;
    try {
        streamIn = new FileInputStream(file);


        bitmap= BitmapFactory.decodeStream(streamIn); //This gets the image
    streamIn.close();
    }
    catch (FileNotFoundException e) {
        e.printStackTrace();
    } catch (IOException e) {
        e.printStackTrace();
    }
   // bitmap= BitmapFactory.decodeFile(Environment.getExternalStorageDirectory() + "/"
       //             + Common.DIRECTORY_SAVE_MP3+"/"+Common.DIRECTORY_SAVE_IMG_MP3+"/"+song.getIdSong()+".png");
return bitmap;

}
    public static Bitmap getBitmapRandomLocal(Context mContext) {
        Bitmap bitmap1 = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.p1);
        Bitmap bitmap2 = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.p2);
        Bitmap bitmap3 = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.p3);
        Bitmap bitmap4 = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.p4);
        List<Bitmap> bitmapList=new ArrayList<>();
        bitmapList.add(bitmap1);
        bitmapList.add(bitmap2);
        bitmapList.add(bitmap3);
        bitmapList.add(bitmap4);
    return bitmapList.get((int) (Math.random()*(3 - 0 + 1)+0));
    }

    public static byte[] getByteFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    public static Bitmap getBitmapFromByte(byte[] localBitmap) {
        if(localBitmap==null) return null;
        return BitmapFactory.decodeByteArray(localBitmap, 0, localBitmap.length);
    }
    public static void showKEyBoard(Context mContext){
        InputMethodManager imm = (InputMethodManager)mContext.getSystemService(mContext.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,InputMethodManager.HIDE_IMPLICIT_ONLY);
    }
    public static void hideKEyBoard(Context mContext, TextInputEditText textInputEditText){
        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(mContext.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(textInputEditText.getWindowToken(), 0);
    }
    public static byte[] getSHA(String input) throws NoSuchAlgorithmException
    {
        /* MessageDigest instance for hashing using SHA256 */
        MessageDigest md = MessageDigest.getInstance("SHA-256");

        /* digest() method called to calculate message digest of an input and return array of byte */
        return md.digest(input.getBytes(StandardCharsets.UTF_8));
    }
    public static boolean isSDCardPresent() {
        if (Environment.getExternalStorageState().equals(

                Environment.MEDIA_MOUNTED)) {
            return true;
        }
        return false;
    }
    public static String toHexString(byte[] hash)
    {
        /* Convert byte array of hash into digest */
        BigInteger number = new BigInteger(1, hash);

        /* Convert the digest into hex value */
        StringBuilder hexString = new StringBuilder(number.toString(16));

        /* Pad with leading zeros */
        while (hexString.length() < 32)
        {
            hexString.insert(0, '0');
        }

        return hexString.toString();
    }
    public static  void viewGoneAnimator(final View view) {

        view.animate()
                .alpha(0f)
                .setDuration(500)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        view.setVisibility(View.GONE);
                    }
                });

    }

    public static void viewVisibleAnimator(final View view) {

        view.animate()
                .alpha(1f)
                .setDuration(500)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        view.setVisibility(View.VISIBLE);
                    }
                });

    }
    public static List<Song> GetMediaMp3FilesByIdPlaylist(List<String> ids,Context  mContext){
        //Log.d("GetMedia_id: ",id);
        List<Song> songList=new ArrayList<>();

        ContentResolver contentResolver;

        Cursor cursor;

        Uri uri;
        contentResolver = mContext.getContentResolver();

        uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.DATA,    // filepath of the audio file
                MediaStore.Audio.Playlists.ALBUM    // context id/ uri id of the file
        };


        String strIds="";
        for (int i = 0; i < ids.size(); i++) {
            if(i!=ids.size()-1) strIds+=ids.get(i)+",";
            else strIds+=(ids.get(i));
        }
        Log.d("strIds",strIds);
        String selection =" _id IN ("+strIds+")";
        cursor = contentResolver.query(
                uri, // Uri
                projection,
                selection,
                null,
                null
        );

        if (cursor == null) {
        } else if (!cursor.moveToFirst()) {
        }
        else {
            do {
                String queryTtile = cursor.getString(0);
                String queryArtist = cursor.getString(1);
                String queryAlbum = cursor.getString(2);
                String queryDuration = cursor.getString(3);
                String filepath = cursor.getString(4);
                String _id = cursor.getString(5);
                songList.add(new Song(_id,queryTtile,queryArtist,queryAlbum,filepath,1));

            } while (cursor.moveToNext());
        }
        Log.d("slist size:", String.valueOf(songList.size()));
        return songList;
    }
    public static  Song GetMediaMp3FilesByIdPlaylist(String id,Context mContext){
        Log.d("GetMedia_id: ",id);
        List<Song> songList=new ArrayList<>();

        ContentResolver contentResolver;

        Cursor cursor;

        Uri uri;
        contentResolver = mContext.getContentResolver();

        uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.DATA,    // filepath of the audio file
                MediaStore.Audio.Media._ID,     // context id/ uri id of the file
        };
        String selection = MediaStore.Audio.Media._ID + "="+id;
        cursor = contentResolver.query(
                uri, // Uri
                projection,
                selection,
                null,
                null
        );

        if (cursor == null) {
        } else if (!cursor.moveToFirst()) {
        }
        else {
            do {
                String queryTtile = cursor.getString(0);
                String queryArtist = cursor.getString(1);
                String queryAlbum = cursor.getString(2);
                String queryDuration = cursor.getString(3);
                String filepath = cursor.getString(4);
                String _id = cursor.getString(5);
                songList.add(new Song(_id,queryTtile,queryArtist,queryAlbum,filepath,1));

            } while (cursor.moveToNext());
        }
        Log.d("slist size:", String.valueOf(songList.size()));
        return songList.size()!=0?songList.get(0):null;
    }
    public static void scanFile(String path,Context mContext) {

        MediaScannerConnection.scanFile(mContext,
                new String[] { path }, null,
                new MediaScannerConnection.OnScanCompletedListener() {

                    public void onScanCompleted(String path, Uri uri) {
                        Log.i("TAG", "Finished scanning " + path);
                    }
                });
    }
    public static List<Song> getAllSongOffline(Context mContext) {
        List<Song> songList=new ArrayList<>();
        ContentResolver contentResolver;
        contentResolver = mContext.getContentResolver();
        Cursor cursor;
//        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

        File apkStorage = new File(
                Environment.getExternalStorageDirectory() + "/"
                        + Common.DIRECTORY_SAVE_MP3);
        String pattern = Pattern.quote(apkStorage.getAbsolutePath()) + "/[^/]*";
        Uri uri = MediaStore.Audio.Media.getContentUriForPath(Environment.getExternalStorageDirectory().getPath());

        String[] projection = {
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.DATA,    // filepath of the audio file
                MediaStore.Audio.Media._ID,     // context id/ uri id of the file
                MediaStore.Audio.Media.DISPLAY_NAME,
                MediaStore.Audio.Playlists.ALBUM
        };
        cursor = contentResolver.query(
                uri, // Uri
                projection,
                null,
                null,
                null
        );

        if (cursor == null) {


        } else if (!cursor.moveToFirst()) {


        }
        else {


            do {
                String filepath = cursor.getString(4);
                if(filepath.matches(pattern)){
                    String queryTtile = cursor.getString(0);
                    String queryArtist = cursor.getString(1);
                    String queryAlbum = cursor.getString(2);
                    String fakeId = cursor.getString(7);

                    songList.add(new Song(fakeId,queryTtile,queryArtist,queryAlbum,filepath,1));
                    Log.d("GetAllMediaMp3FilesPath",filepath);
                    Log.d("GetAllMediaMp3FilesDis",cursor.getString(6));
                    Log.d("GetAllMediaMp3FilesFID",cursor.getString(7));
                }

                Log.d("filepath",filepath);

            } while (cursor.moveToNext());
        }
        Log.d("GetAllMediaMp3Files", String.valueOf(songList.size()));
        return songList;
    }
    public static class DownloadingTask extends AsyncTask<Void, Void, Void> {
    private Context mContext;;
    public DownloadingTask(Context mContext){
        this.mContext=mContext;
    }
        File apkStorage = null;
        File outputFile = null;
        String TAG="hihiDownloadMP3";
        String downloadFileName;
        Song song;
        public void setSong(Song song){this.song=song;
            String songLink=song.getSongLink();
            this.downloadFileName=songLink.replace(Common.URLMP3,"");}
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //buttonText.setEnabled(false);
            //buttonText.setText(R.string.downloadStarted);//Set Button Text when download started
        }

        @Override
        protected void onPostExecute(Void result) {
            try {
                if (outputFile != null) {
                    //buttonText.setEnabled(true);
                    //buttonText.setText(R.string.downloadCompleted);//If Download completed then change button text
                } else {
                    //buttonText.setText(R.string.downloadFailed);//If download failed change button text
                    //new Handler().postDelayed(new Runnable() {
                    //    @Override
                    //   public void run() {
                    //buttonText.setEnabled(true);
                    //buttonText.setText(R.string.downloadAgain);//Change button text again after 3sec
                    //}
                    //}, 3000);

                    Log.e(TAG, "Download Failed");

                }
            } catch (Exception e) {
                e.printStackTrace();

                //Change button text if exception occurs
                // buttonText.setText(R.string.downloadFailed);
                //new Handler().postDelayed(new Runnable() {
                // @Override
                //public void run() {
                //   buttonText.setEnabled(true);
                //   buttonText.setText(R.string.downloadAgain);
                //}
                //}, 3000);
                Log.e(TAG, "Download Failed with Exception - " + e.getLocalizedMessage());

            }


            super.onPostExecute(result);
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            try {
                URL url = new URL(song.getSongLink());//Create Download URl
                HttpURLConnection c = (HttpURLConnection) url.openConnection();//Open Url Connection
                c.setRequestMethod("GET");//Set Request Method to "GET" since we are grtting data
                c.connect();//connect the URL Connection

                //If Connection response is not OK then show Logs
                if (c.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    Log.e(TAG, "Server returned HTTP " + c.getResponseCode()
                            + " " + c.getResponseMessage());

                }


                //Get File if SD card is present
                if (HelpTools.isSDCardPresent()) {

                    apkStorage = new File(
                            Environment.getExternalStorageDirectory() + "/"
                                    + Common.DIRECTORY_SAVE_MP3);
                } else
                    Toast.makeText(mContext, "Oops!! There is no SD Card.", Toast.LENGTH_SHORT).show();

                //If File is not present create directory
                if (!apkStorage.exists()) {
                    apkStorage.mkdir();
                    Log.e(TAG, "Directory Created.");
                }

                outputFile = new File(apkStorage, downloadFileName);//Create Output file in Main File

                //Create New File if not present
                if (!outputFile.exists()) {
                    outputFile.createNewFile();
                    Log.e(TAG, "File Created");
                }

                FileOutputStream fos = new FileOutputStream(outputFile);//Get OutputStream for NewFile Location

                InputStream is = c.getInputStream();//Get InputStream for connection

                byte[] buffer = new byte[1024];//Set buffer type
                int len1 = 0;//init length
                while ((len1 = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, len1);//Write new file
                }
                ContentValues values = new ContentValues();
                //values.put(MediaStore.MediaColumns._ID, song.getIdSong());
                values.put(MediaStore.MediaColumns.DATA, outputFile.getPath());
                values.put(MediaStore.MediaColumns.DISPLAY_NAME, song.getSongName());
                values.put(MediaStore.MediaColumns.TITLE, song.getSongName());
                values.put(MediaStore.MediaColumns.MIME_TYPE, "audio/mpeg");
                values.put(MediaStore.MediaColumns.SIZE, outputFile.length());
                values.put(MediaStore.Audio.Playlists.ALBUM, song.getIdSong());
                values.put(MediaStore.Audio.Media.ARTIST, song.getSingerName());
                values.put(MediaStore.Audio.Media.IS_RINGTONE, false);
                InputStream inputStream;
                Bitmap bitmap = null;
                try {
                    inputStream = new java.net.URL(song.getSongImage()).openStream();
                    bitmap = BitmapFactory.decodeStream(inputStream);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                File apkStorageImage = new File(
                        Environment.getExternalStorageDirectory() + "/"+Common.DIRECTORY_SAVE_MP3+"/"
                                + Common.DIRECTORY_SAVE_IMG_MP3);
                if (!apkStorageImage.exists()) {
                    apkStorageImage.mkdir();
                    Log.e(TAG, "Directory img Created.");
                }

                File file = new File(apkStorageImage, song.getIdSong() + ".png");
                FileOutputStream fOut = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.PNG, 85, fOut);

                System.out.println("Reading complete.");

                // Now set some extra features it depend on you
//                values.put(MediaStore.Audio.Media.IS_NOTIFICATION, false);
//                values.put(MediaStore.Audio.Media.IS_ALARM, false);

//
                Uri uri = MediaStore.Audio.Media.getContentUriForPath(outputFile.getPath());
                //Log.d("insert",outputFile.getAbsolutePath());
                //Log.d("insert",outputFile.getPath());
                //Log.d("insert",Environment.getExternalStorageDirectory() + "/"
                //       + Common.DIRECTORY_SAVE_MP3+"/"+song.getSongName());
                Uri uri2 = mContext.getContentResolver().insert(uri, values);
//
                if (uri2 == null || TextUtils.isEmpty(uri2.toString())) {
                    Log.w("inserting", "Something went wrong while inserting data to content resolver");
                }
                HelpTools.scanFile(apkStorage.getPath(),mContext);
                //Close all connection after doing task
                fos.close();
                is.close();

            } catch (Exception e) {

                //Read exception if something went wrong
                e.printStackTrace();
                outputFile = null;
                Log.e(TAG, "Download Error Exception " + e.getMessage());
            }

            return null;
        }
    }
    public static String dayWeekText(){
        Date date=new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
        System.out.println("Day of week in number:"+dayOfWeek);
        String dayWeekText = new SimpleDateFormat("EEEE").format(date);
        return  dayWeekText;
}
    public static int intDayWeekText(){
        Date date=new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
        return  dayOfWeek;
    }
    public static boolean isMyServiceRunning(Class<?> serviceClass,Context mContext) {
        ActivityManager manager = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
    public static void startServiceNewSong(com.example.appplaymusic.Models.Song song,Context mContext){
        Intent intent=new Intent(mContext, PlayMusicService.class);
        Bundle bundle=new Bundle();
        bundle.putSerializable("song_obj",song);
        intent.putExtras(bundle);
        mContext.startService(intent);
    }
}
