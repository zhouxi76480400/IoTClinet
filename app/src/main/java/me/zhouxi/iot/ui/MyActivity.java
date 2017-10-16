package me.zhouxi.iot.ui;

import android.support.v7.app.AppCompatActivity;

import me.zhouxi.iot.client.APIList;
import me.zhouxi.iot.client.APIs;

/**
 * Created by zhouxi on 6/10/2017.
 */

public class MyActivity extends AppCompatActivity implements APIList.APIListListener {

    @Override
    public void apiListListenerOnDataReceived(final APIs type, final String data) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                apiListListenerOnDataReceivedMainThread(type,data);
            }
        });
    }

    @Override
    public void apiListListenerOnError(final APIs type, final Exception e) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                apiListListenerOnErrorMainThread(type,e);
            }
        });
    }

    public void apiListListenerOnDataReceivedMainThread(APIs type, String data) {

    }

    public void apiListListenerOnErrorMainThread(APIs type, Exception e) {

    }

}
