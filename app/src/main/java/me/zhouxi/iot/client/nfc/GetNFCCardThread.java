package me.zhouxi.iot.client.nfc;

import me.zhouxi.iot.client.APIList;
import me.zhouxi.iot.client.APIs;

/**
 * Created by zhouxi on 24/10/2017.
 */

public class GetNFCCardThread implements Runnable ,APIList.APIListListener {

    public interface GetNFCCardThreadListener{

        void onGetNFCCardThreadSuccess(String string);

        void onGetNFCCardThreadFailed(boolean networkException);

    }

    private GetNFCCardThreadListener listener;

    public void setGetNFCCardThreadListener(GetNFCCardThreadListener listener){
        this.listener = listener;
    }

    public GetNFCCardThread(GetNFCCardThreadListener listener){
        super();
        this.listener = listener;
    }

    private Thread thread;

    private String pwd;

    private long time;

    public void start(String pwd,long time){
        this.pwd = pwd;
        this.time = time;
        if(thread == null){
            thread = new Thread(this);
            thread.start();
        }
    }

    @Override
    public void run() {
        APIList.getInstance().GetNFCCard(this,pwd,time);
        thread = null;
    }

    @Override
    public void apiListListenerOnDataReceived(APIs type, String data) {
        if(type == APIs.GetNFCCard){
            if(listener != null)
                listener.onGetNFCCardThreadSuccess(data);
        }
    }

    @Override
    public void apiListListenerOnError(APIs type, Exception e) {
        if(type == APIs.GetNFCCard){
            if(listener != null)
                listener.onGetNFCCardThreadFailed(true);
        }
    }
}
