package me.zhouxi.iot.client.nfc;

import me.zhouxi.iot.client.APIList;
import me.zhouxi.iot.client.APIs;

/**
 * Created by zhouxi on 24/10/2017.
 */

public class PasswordAuthenticationThread implements Runnable, APIList.APIListListener {

    public interface PasswordAuthenticationThreadListener{

        void onPasswordAuthenticationSuccess();

        void onPasswordAuthenticationFailed(boolean isNetworkException);
    }

    private PasswordAuthenticationThreadListener listener;

    private String pwd;

    private Thread thread;

    public PasswordAuthenticationThread(PasswordAuthenticationThreadListener listener){
        super();
        this.listener = listener;
    }

    public void start(String pwd){
        this.pwd = pwd;
        if(thread == null){
            thread = new Thread(this);
            thread.start();
        }
    }

    @Override
    public void run() {
        APIList.getInstance().PasswordAuthentication(this,pwd);
        thread = null;
    }

    @Override
    public void apiListListenerOnDataReceived(APIs type, String data) {
        if(type == APIs.PasswordAuthentication){
            if(data.equalsIgnoreCase("true")){
                if(listener != null)
                    listener.onPasswordAuthenticationSuccess();
            }else if(data.equalsIgnoreCase("false")){
                if(listener != null)
                    listener.onPasswordAuthenticationFailed(false);
            }else{
                if(listener != null)
                    listener.onPasswordAuthenticationFailed(false);
            }
        }
    }

    @Override
    public void apiListListenerOnError(APIs type, Exception e) {
        if(type == APIs.PasswordAuthentication){
            if(listener != null)
                listener.onPasswordAuthenticationFailed(true);
        }
    }
}
