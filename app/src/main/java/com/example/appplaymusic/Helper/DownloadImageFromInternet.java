package com.example.appplaymusic.Helper;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RemoteViews;

import com.example.appplaymusic.R;

import java.io.InputStream;

public class DownloadImageFromInternet extends AsyncTask<String, Void, Bitmap> {
    private  TaskDelegate taskDelegate;
    public DownloadImageFromInternet(TaskDelegate taskDelegate) {
        this.taskDelegate=taskDelegate;
    }
    public  interface TaskDelegate{
        void onTaskEndWithResult(Bitmap success);
        void onTaskFinishGettingData(Bitmap result);
    }
    protected Bitmap doInBackground(String... urls) {
        String imageURL=urls[0];
        Bitmap bimage=null;
        try {
            InputStream in=new java.net.URL(imageURL).openStream();
            bimage= BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            Log.e("Error Message", e.getMessage());
            e.printStackTrace();
        }
        return bimage;
    }
    protected void onPostExecute(Bitmap result) {
        this.taskDelegate.onTaskEndWithResult(result);
    }




}
