package me.zhouxi.iot;

import android.app.Application;

import java.util.ArrayList;
import java.util.List;

import me.zhouxi.iot.client.UDPSocketReceiver;
import me.zhouxi.iot.client.find_server.FindServerUtil;
import me.zhouxi.iot.object.ItemDetailObject;

/**
 * Created by zhouxi on 7/10/2017.
 */

public class IoTApplication extends Application {

    private static IoTApplication application;

    @Override
    public void onCreate() {
        super.onCreate();
        IoTApplication.application = this;
        init();
    }

    public static IoTApplication getInstance(){
        return application;
    }

    private List<ItemDetailObject> itemDetailObjectList;

    /**
     * get all of devices
     * @return
     */
    public List<ItemDetailObject> getItemDetailObjectList(){
        if(itemDetailObjectList == null)
            itemDetailObjectList = new ArrayList<>();
        return itemDetailObjectList;
    }

    /**
     * reset data
     * @param newList
     */
    public void cleanAndResetItemDetailObjectList(List<ItemDetailObject> newList){
        if(itemDetailObjectList == null)
            itemDetailObjectList = new ArrayList<>();
        itemDetailObjectList.clear();
        itemDetailObjectList.addAll(newList);
    }

    private void init(){
        UDPSocketReceiver.getInstance();
        setServerAddress();
    }

    private void setServerAddress(){
        boolean isServerFileExist = FindServerUtil.isServerFileExist();
        if(isServerFileExist){
            FindServerUtil.initServerAddress();
        }
    }

}
