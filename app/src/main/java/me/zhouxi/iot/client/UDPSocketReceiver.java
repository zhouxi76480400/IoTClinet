package me.zhouxi.iot.client;

import android.util.Log;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhouxi on 8/10/2017.
 */

public class UDPSocketReceiver implements Runnable{

    private static final String TAG = "UDPSocketReceiver";

    public interface UDPSocketReceiverListener{

        void onListen(DatagramPacket datagramPacket);

    }

    private static UDPSocketReceiver udpSocketReceiver;

    private UDPSocketReceiver(){
        super();
        init();
    }

    private List<UDPSocketReceiverListener> listenerList;

    public static UDPSocketReceiver getInstance(){
        if(udpSocketReceiver == null){
            udpSocketReceiver = new UDPSocketReceiver();
        }
        return udpSocketReceiver;
    }

    /**
     * add listener
     * @param listener
     */
    public void addListener(UDPSocketReceiverListener listener){
        if(listener != null){
            if(!listenerList.contains(listener)){
                listenerList.add(listener);
            }
        }
    }

    /**
     * remove listener
     * @param listener
     */
    public void removeListener(UDPSocketReceiverListener listener){
        if(listener != null){
            if(listenerList.contains(listener)){
                listenerList.remove(listener);
            }
        }
    }

    /**
     * clear all listener
     */
    public void clearListener(){
        if(listenerList.size() > 0){
            listenerList.clear();
        }
    }

    private void init(){
        listenerList = new ArrayList<>();
        startThread();
    }

    private void startThread(){
        Thread thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run() {
        Log.d(TAG,"start udp socket receiver");
        DatagramSocket multicastSocket = null;
        try {
            InetAddress address = InetAddress.getByName("0.0.0.0");
            multicastSocket = new DatagramSocket(APIList.udp_receive_port,address);

            byte[] buf = new byte[1024];

            while (true) {
                DatagramPacket datagramPacket = new DatagramPacket(buf, buf.length);
                multicastSocket.receive(datagramPacket);
                Log.d(TAG,"udp package received");
                sendDatagramPacketToListener(datagramPacket);
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.d(TAG,e.toString());
        }
        Log.d(TAG,"stop udp broadcast receive");
        startThread();
    }

    private void sendDatagramPacketToListener(DatagramPacket datagramPacket){
        if(listenerList.size() > 0){
            for(int i = listenerList.size() - 1 ; i >= 0 ; i --){
                UDPSocketReceiverListener listener = listenerList.get(i);
                if(listener != null){
                    // send to listener
                    listenerList.get(i).onListen(datagramPacket);
                }else {
                    // remove null pointer
                    listenerList.remove(i);
                }
            }
        }
    }

}
