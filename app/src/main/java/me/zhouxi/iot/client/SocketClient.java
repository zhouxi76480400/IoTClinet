package me.zhouxi.iot.client;

import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Created by zhouxi on 6/10/2017.
 */

public class SocketClient {



    private static String DEBUG_TAG = "SocketClient";

    /**
     * single Instance of SocketClient
     */
    private static SocketClient singleInstance;

    /**
     *  wait and read time out
     */
    private static int timeout = 10000;

    /**
     * a remote server address
     */
    private static String address = "192.168.1.53";

    /**
     * a port connect to remote server
     */
    private static int port = 20000;

    /**
     *
     */
    private boolean socketConnected;

    private Thread socketThread;

    /**
     * use SingleInstance
     * @return
     */
    public static SocketClient getInstance(){
        if(singleInstance == null) {
            singleInstance = new SocketClient();
        }
        //auto connect to server
        singleInstance.startClient();
        return singleInstance;
    }

    private SocketClient(){
        super();
    }


    public void startClient(){
        if(socketThread == null){
            socketThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Socket socket = new Socket(address,port);
                        socket.setSoTimeout(timeout);
                        OutputStream outputStream = socket.getOutputStream();
                        InputStream inputStream = socket.getInputStream();


                        String test = "0test\0";
                        outputStream.write(test.getBytes());
                        outputStream.flush();
                        socketConnected = true;




//                        bufferedReader.close();
//                        inputStream.close();
////                        printWriter.close();
//                        outputStream.close();
//                        socket.close();

                    } catch (IOException e) {
                        socketConnected = false;
                        //destroy socketThread
                        socketThread = null;
                        e.printStackTrace();
                    }
                }
            });
            socketThread.start();
        }else {
            Log.d(DEBUG_TAG,"socket is already connected");
        }
    }

    public boolean isSocketConnected(){
        return socketConnected;
    }

}
