package me.zhouxi.iot.client.nfc;

import me.zhouxi.iot.client.APIList;
import me.zhouxi.iot.client.APIs;

/**
 * Created by zhouxi on 24/10/2017.
 */

public class SetPasswordThread implements Runnable ,APIList.APIListListener {

    private String pwdTmp;

    public interface SetPasswordThreadListener{

        void onSetPasswordSuccess();

        void onSetPasswordFailed(boolean isNetworkException);

    }

    private SetPasswordThreadListener listener;

    private Thread thread;

    public SetPasswordThread(SetPasswordThreadListener listener){
        super();
        this.listener = listener;
    }

    public void start(String pwd){
        pwdTmp = pwd;
        if(thread == null){
            thread = new Thread(this);
            thread.start();
        }
    }

    @Override
    public void run() {
        APIList.getInstance().SetServersPassword(this,pwdTmp);
        thread = null;
    }

    @Override
    public void apiListListenerOnDataReceived(APIs type, String data) {
        if(type == APIs.SetServersPassword){
            if(data.equalsIgnoreCase("true")){
                if(listener != null)
                    listener.onSetPasswordSuccess();
            }else if(data.equalsIgnoreCase("false")){
                if(listener != null)
                    listener.onSetPasswordFailed(false);
            }else {
                apiListListenerOnError(type,null);
            }
        }
    }

    @Override
    public void apiListListenerOnError(APIs type, Exception e) {
        if(type == APIs.SetServersPassword){
            if(listener != null)
                listener.onSetPasswordFailed(true);
        }
    }
}
