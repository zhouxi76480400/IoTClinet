package me.zhouxi.iot.client;

import android.util.Log;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;

/**
 * Created by zhouxi on 7/10/2017.
 */

public class UDPSocketThread implements Runnable{

    public UDPSocketThread(){
        super();
    }


    public void startSocket(){
        Thread thread = new Thread(this);
        thread.start();
    }


    @Override
    public void run() {
        try {
            MulticastSocket multicastSocket = new MulticastSocket(APIList.udp_port);
            multicastSocket.setTimeToLive(2);
            InetAddress groupAddress = InetAddress.getByName("224.0.0.1");
            multicastSocket.joinGroup(groupAddress);

            String text = "test";

            DatagramPacket packet = new DatagramPacket(text.getBytes(), text.getBytes().length,groupAddress,APIList.udp_port);


            multicastSocket.send(packet);
            Log.e("teset","test");
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
