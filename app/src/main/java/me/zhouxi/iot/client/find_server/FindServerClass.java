package me.zhouxi.iot.client.find_server;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import me.zhouxi.iot.client.APIList;
import me.zhouxi.iot.client.UDPSocketReceiver;

/**
 * Created by zhouxi on 8/10/2017.
 */

public class FindServerClass implements Runnable, UDPSocketReceiver.UDPSocketReceiverListener{

    private static final String TAG = "FindServerClass";

    /**
     * wait server response time
     */
//    public static final int find_wait_time = 10000;//ms

    public static final int find_wait_time = 6000;//ms

    private boolean isIgnored;

    public interface OnFindServerListener{

        void onServerFounded(List<ServerObject> serverList);

        void onServerNotFound();

    }

    private OnFindServerListener listener;

    public FindServerClass(){
        super();
    }

    public void setLisener(OnFindServerListener newListener){
        this.listener = newListener;
    }

    private List<ServerObject> serverObjectList;

    public FindServerClass(OnFindServerListener listener){
        super();
        this.listener = listener;
        serverObjectList = new ArrayList<>();
    }

    public void startFindServer(){
        isIgnored = false;
        serverObjectList.clear();
        UDPSocketReceiver.getInstance().addListener(this);
        Thread sendThread = new Thread(this);
        sendThread.start();
    }

    public void stopFind(){
        isIgnored = true;
        serverObjectList.clear();
        UDPSocketReceiver.getInstance().removeListener(this);
    }

    @Override
    public void run() {
        try {
            MulticastSocket multicastSocket = new MulticastSocket();
//            InetAddress address = InetAddress.getByName(APIList.broadcast_address);
            InetAddress address = InetAddress.getByName("224.0.0.1");
            multicastSocket.joinGroup(address);
            multicastSocket.setTimeToLive(1);
            boolean canSendPackage = true;
            boolean canSendTwice = true;
            long start_time = System.currentTimeMillis();
            Log.d(TAG,"start send package");
            while (!isIgnored) {
                long now_time = System.currentTimeMillis();
                long time_go_on = now_time - start_time;
                if(time_go_on > find_wait_time){
                    // end
                    break;
                }
                // send package two times
                if(time_go_on > find_wait_time / 2 && canSendTwice){
                    canSendPackage = true;
                    canSendTwice = false;
                }
                if(canSendPackage){
                    String packageContent = FindServerUtil.generateABroadcastPackageContent();
                    DatagramPacket datagramPacket =
                            new DatagramPacket(packageContent.getBytes(), packageContent.getBytes().length);
                    datagramPacket.setAddress(address);
                    datagramPacket.setPort(APIList.udp_port);
                    multicastSocket.send(datagramPacket);
                }
                canSendPackage = false;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        Log.d(TAG,"stop send package");
        UDPSocketReceiver.getInstance().removeListener(this);
        if(listener != null){
            if(serverObjectList.size() == 0){
                listener.onServerNotFound();
            }else{
                listener.onServerFounded(serverObjectList);
            }
        }
        serverObjectList.clear();
    }

    @Override
    public void onListen(DatagramPacket datagramPacket) {
        String string = new String(datagramPacket.getData(),0,datagramPacket.getLength());
        try {
            JSONObject jsonObject = new JSONObject(string);
            String action = jsonObject.getString("action");
            if(action.equals(FindServerUtil.broadcast_header)){
                // it is find server response and check server can connect ?
                boolean resp = jsonObject.getBoolean("resp");
                if(resp){
                    // can connect
                    Log.d(TAG,"find server:"+datagramPacket.getAddress().getHostAddress() + ":" +
                            String.valueOf(datagramPacket.getPort()));
                    ServerObject serverObject = new ServerObject();
                    serverObject.ip = datagramPacket.getAddress().getHostAddress();
                    serverObject.port = datagramPacket.getPort();
                    serverObject.version = jsonObject.getString("ver");
                    serverObject.name = jsonObject.getString("name");

                    boolean isEqual = false;
                    for(int i = serverObjectList.size() -1 ; i >= 0 ; i --){
                        ServerObject old = serverObjectList.get(i);
                        if(old.equals(serverObject)) {
                            isEqual = true;
                            break;
                        }
                    }
                    if(!isEqual){
                        serverObjectList.add(serverObject);
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
