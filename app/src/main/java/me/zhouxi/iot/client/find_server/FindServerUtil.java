package me.zhouxi.iot.client.find_server;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;

import org.json.JSONException;
import org.json.JSONObject;

import me.zhouxi.iot.IoTApplication;
import me.zhouxi.iot.client.APIList;

/**
 * Created by zhouxi on 8/10/2017.
 */

public class FindServerUtil {

    private static final String shared_preferences_name = "server_preferences";

    private static final String shared_preferences_server_key = "server_address";

    private static final String shared_preferences_server_version_key = "server_version";

    private static final String shared_preferences_server_name_key = "server_name";

    public static final String broadcast_header = "findiot";

    public static String generateABroadcastPackageContent(){
        // simple findiot:{{packagename}:{version}}
        String packageName = IoTApplication.getInstance().getPackageName();
        String version_code = null;
        try {
            version_code = IoTApplication.getInstance().getPackageManager().getPackageInfo(packageName, 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("action",broadcast_header);
            jsonObject.put("package_name",packageName);
            jsonObject.put("version",version_code);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    public static boolean isServerFileExist(){
        String address = getServerSharedPreferences().getString(shared_preferences_server_key,null);
        return address != null;
    }

    public static void initServerAddress(){
        String address = getServerSharedPreferences().getString(shared_preferences_server_key,null);
        if(address != null)
            APIList.address = address;
    }

    public static void setNowServer(ServerObject serverObject){
        if(serverObject != null){
            SharedPreferences sharedPreferences = getServerSharedPreferences();
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(shared_preferences_server_key,serverObject.ip);
            editor.putString(shared_preferences_server_name_key,serverObject.name);
            editor.putString(shared_preferences_server_version_key,serverObject.version);
            editor.apply();
        }
    }

    private static SharedPreferences getServerSharedPreferences(){
        IoTApplication application = IoTApplication.getInstance();
        SharedPreferences sharedPreferences =
                application.getSharedPreferences(shared_preferences_name, Context.MODE_PRIVATE);
        return sharedPreferences;
    }

}
