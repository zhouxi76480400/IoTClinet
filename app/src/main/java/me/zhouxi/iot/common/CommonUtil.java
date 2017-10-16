package me.zhouxi.iot.common;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.net.ConnectivityManagerCompat;

import me.zhouxi.iot.IoTApplication;

/**
 * Created by zhouxi on 11/10/2017.
 */

public class CommonUtil {

    public static NetworkInfo getNetworkInfo(){
        Context context = IoTApplication.getInstance();
        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager.getActiveNetworkInfo();
    }
}
