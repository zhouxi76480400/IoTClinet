package me.zhouxi.iot.client.nfc;

import android.util.Log;

import me.zhouxi.iot.client.APIList;
import me.zhouxi.iot.client.APIs;

/**
 * Created by zhouxi on 24/10/2017.
 */

public class CheckIsHavePasswordThread implements Runnable , APIList.APIListListener{

    public interface CheckIsHavePasswordThreadListener {

        void serverHavePassword();

        void serverNotHavePassword();

        void networkIsDown();

    }

    private CheckIsHavePasswordThreadListener listener;

    public void setListener(CheckIsHavePasswordThreadListener listener){
        this.listener = listener;
    }

    public CheckIsHavePasswordThread (){
        super();
    }

    public CheckIsHavePasswordThread (CheckIsHavePasswordThreadListener listener){
        super();
        this.listener = listener;
    }

    private Thread thread;

    public void startCheck(){
        if(thread == null){
            thread = new Thread(this);
            thread.start();
        }
    }

    @Override
    public void run() {
        APIList.getInstance().IsServerHavePassword(this);
        thread = null;
    }

    @Override
    public void apiListListenerOnDataReceived(APIs type, String data) {
        if(type.equals(APIs.IsServerHavePassword)){
            if(data.equalsIgnoreCase("true")){
                if(listener != null)
                    listener.serverHavePassword();
            }else if(data.equalsIgnoreCase("false")){
                if(listener != null)
                    listener.serverNotHavePassword();
            }else {
                apiListListenerOnError(type, null);
            }
        }
    }

    @Override
    public void apiListListenerOnError(APIs type, Exception e) {
        if(listener != null)
            listener.networkIsDown();
    }
}
