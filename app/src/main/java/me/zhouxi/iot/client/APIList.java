package me.zhouxi.iot.client;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import me.zhouxi.iot.IoTApplication;

/**
 * Created by zhouxi on 6/10/2017.
 */

public class APIList {

    /**
     * a remote server address
     */
    public static String address = "192.168.1.52";// for test

    /**
     * a port connect to remote server
     */
    public static int port = 20000;

    /**
     * udp port
     */
    public static int udp_port = port + 1;

    public static int udp_receive_port = udp_port + 1;

    /**
     * udp broad cast address
     */
    public static String broadcast_address = "224.0.0.1";

    /**
     * time out
     */
    public static int timeout = 10000;

    /**
     * receive data from server
     */
    public interface APIListListener{

        /**
         * received data
         * @param type
         * @param data
         */
        void apiListListenerOnDataReceived(APIs type ,String data);

        /**
         * handle error
         * @param type
         * @param e
         */
        void apiListListenerOnError(APIs type ,Exception e);

    }

    /**
     *  use this variable to find request name
     */
    private HashMap<String,String> apiDictionary;

    // static single instance
    private static APIList apiList;

    /**
     * get instance for this class
     * @return
     */
    public static APIList getInstance(){
        if(apiList == null){
            apiList = new APIList();
        }
        return apiList;
    }

    /**
     * hidden constructor
     */
    private APIList(){
        super();
        readJSON();
    }

    private void readJSON(){
        if(apiDictionary == null)
            apiDictionary = new HashMap<>();
        apiDictionary.clear();
        IoTApplication application = IoTApplication.getInstance();
        try {
            InputStream inputStream = application.getAssets().open("api.json");
            StringBuilder stringBuilder = new StringBuilder();
            byte[] buffer = new byte[2048];
            int readBytes;
            while((readBytes = inputStream.read(buffer)) > 0){
                stringBuilder.append(new String(buffer, 0, readBytes));
            }
            JSONArray jsonArray = new JSONArray(stringBuilder.toString());
            for(int i = 0 ; i < jsonArray.length() ; i ++ ){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String key = jsonObject.getString("key");
                String value = jsonObject.getString("value");
                apiDictionary.put(key,value);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void commonShortRequest(APIListListener listener,APIs type,String param){
        Set<String> keySet = apiDictionary.keySet();
        Iterator<String> iterator = keySet.iterator();
        String apiName = null;
        while (iterator.hasNext()){
            String key = iterator.next();
            String value = apiDictionary.get(key);
            if(value.equals(String.valueOf(type))){
                apiName = key;
                break;
            }
        }
        if(apiName != null){
            String requestString = generateARequestString(apiName,param);
            SocketThread socketThread = new SocketThread(requestString,type,listener);
            socketThread.startRequest();
        }
    }

    private String generateARequestString(String api,String param){
        if(param == null)
            param = "";
        return String.format("%s:%s\0",api,param);
    }


    /**
     * get all devices
     * @param listener
     */
    public void getAllDevices(APIListListener listener){
        commonShortRequest(listener,APIs.GetAllDevicesList,null);
    }

    /**
     * check server is have password
     * @param listListener
     */
    public void IsServerHavePassword(APIListListener listListener){
        commonShortRequest(listListener,APIs.IsServerHavePassword,null);
    }

    /**
     * set pwd
     * @param listListener
     * @param params
     */
    public void SetServersPassword(APIListListener listListener,String params){
        commonShortRequest(listListener,APIs.SetServersPassword,params);
    }

    /**
     * check pwd
     * @param listListener
     * @param params
     */
    public void PasswordAuthentication(APIListListener listListener,String params){
        commonShortRequest(listListener,APIs.PasswordAuthentication,params);
    }

}
