package com.example.appplaymusic.Receivers;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

public class BroadcastReceiver extends android.content.BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("isNetworkAvailable","onReceive");
        if(ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())){
            Log.d("isNetworkAvailable","equals");
            if (isNetworkAvailable(context)) {
                Toast.makeText(context, "Internet is connected", Toast.LENGTH_SHORT).show();
                Log.d("isNetworkAvailable","connected");
            }
            else{
                Toast.makeText(context, "Internet isn't connected", Toast.LENGTH_SHORT).show();

                Log.d("isNetworkAvailable","isn't connected");
            }

        }
    }

    private boolean isNetworkAvailable(Context context ) {
        ConnectivityManager connectivityManager= (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager==null)
            return false;
       if(Build.VERSION.SDK_INT>Build.VERSION_CODES.M){
           Network network=connectivityManager.getActiveNetwork();
           if(network==null) return false;
           NetworkCapabilities networkCapabilities= connectivityManager.getNetworkCapabilities(network);
           return networkCapabilities!=null&&networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI);
       }else{
           NetworkInfo networkInfo=connectivityManager.getActiveNetworkInfo();
           return networkInfo!=null&&networkInfo.isConnected();
       }
    }
}
