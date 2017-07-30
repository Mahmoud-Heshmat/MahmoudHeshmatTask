package com.example.mahmoudheshmat.mahmoudheshmattask;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class utilits {

    //Internet Connection
    boolean isConnected;

    // Function check internet connection
    public Boolean internetConnection(Context context){
        Boolean check = false;
        ConnectivityManager cm =
                (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        if(isConnected){
            check = true;
        }
        return  check;
    }

}
