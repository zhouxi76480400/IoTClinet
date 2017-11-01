package me.zhouxi.iot.client.nfc;

import android.util.Log;

import me.zhouxi.iot.client.APIList;
import me.zhouxi.iot.client.APIs;

/**
 * Created by zhouxi on 17年11月1日.
 */

public class GetAllRecordDataThread implements Runnable , APIList.APIListListener{

    public interface GetAllRecordDataThreadListener {

        void onGetAllRecordDataThreadSuccess(String data);

        void onGetAllRecordDataThreadFailed();

    }

    private GetAllRecordDataThreadListener listener;

    public void setGetAllRecordDataThreadListener(GetAllRecordDataThreadListener listener){
        this.listener = listener;
    }

    private Thread thread;

    public GetAllRecordDataThread(){
        super();
    }

    @Override
    public void run() {
        APIList.getInstance().GetAllNFCAuthOKData(this);
        thread = null;
    }

    public void start(){
        if (thread == null){
            thread = new Thread(this);
            thread.start();
        }
    }

    @Override
    public void apiListListenerOnDataReceived(APIs type, String data) {
        if(type == APIs.GetAllNFCAuthOKData){
            if(listener != null)
                listener.onGetAllRecordDataThreadSuccess(data);
        }
    }

    @Override
    public void apiListListenerOnError(APIs type, Exception e) {
        if(type == APIs.GetAllNFCAuthOKData){
            if(listener != null)
                listener.onGetAllRecordDataThreadFailed();
        }
    }
}
