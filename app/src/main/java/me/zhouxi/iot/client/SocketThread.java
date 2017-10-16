package me.zhouxi.iot.client;

import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Created by zhouxi on 7/10/2017.
 */

public class SocketThread implements Runnable{

    private static final String DEBUG_TAG = "SocketThread";

    private Thread thread;

    private String api;

    public APIs apiEnum;

    private APIList.APIListListener listener;

    public SocketThread(String api,APIs apiEnum , APIList.APIListListener listener){
        super();
        this.api = api;
        this.apiEnum = apiEnum;
        this.listener = listener;
        this.thread = new Thread(this);
    }

    public void startRequest(){
        this.thread.start();
    }

    @Override
    public void run() {
        Socket socket = null;
        StringBuilder stringBuilder = null;
        try {
            socket = new Socket(APIList.address,APIList.port);
            socket.setSoTimeout(APIList.timeout);
            socket.setKeepAlive(true);
            OutputStream outputStream = socket.getOutputStream();
            InputStream inputStream = socket.getInputStream();
            outputStream.write(api.getBytes());
            outputStream.flush();

            stringBuilder = new StringBuilder();
            byte[] buffer = new byte[2048];
            int readBytes;
            while((readBytes = inputStream.read(buffer)) > 0){
                stringBuilder.append(new String(buffer, 0, readBytes));
            }

            inputStream.close();
            outputStream.close();
            socket.close();

            String finalString = stringBuilder.toString();

            // remove end char
            if(finalString.endsWith("\0")){
                finalString = finalString.substring(0,finalString.length() - 1);
            }

            Log.d(DEBUG_TAG,api +" :"+finalString);

            if(listener != null) {
                listener.apiListListenerOnDataReceived(apiEnum,finalString);
            }
        } catch (IOException e) {
            e.printStackTrace();
            if (listener != null) {
                listener.apiListListenerOnError(apiEnum, e);
            }
        }
    }

}
