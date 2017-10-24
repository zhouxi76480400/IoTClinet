package me.zhouxi.iot.client.nfc;

/**
 * Created by zhouxi on 24/10/2017.
 */

public class CheckIsHavePasswordThread implements Runnable {

    public interface CheckIsHavePasswordThreadListener {

        void serverHavePassword();

        void serverNotHavePassword();

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
        if(thread != null){
            thread = new Thread(this);
            thread.start();
        }
    }

    @Override
    public void run() {

    }
}
