package me.zhouxi.iot;

import android.app.Application;
import android.content.ClipData;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
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
        writeToFile((Serializable) newList);
    }

    private static final String index_file = "index_file";

    private void writeToFile(Serializable serializable){
        File file = new File(getFilesDir(),index_file);
        if(file.exists())
            file.delete();
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(serializable);
            objectOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<ItemDetailObject> getSavedIndexData(){
        List<ItemDetailObject> list = new ArrayList<>();
        File file = new File(getFilesDir(),index_file);
        if(file.exists()){
            try {
                FileInputStream fileInputStream = new FileInputStream(file);
                ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
                Object o = objectInputStream.readObject();
                if(o instanceof List){
                    list.addAll((Collection<? extends ItemDetailObject>) o);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    private void init(){
        setServerAddress();
        UDPSocketReceiver.getInstance();
    }

    private void setServerAddress(){
        boolean isServerFileExist = FindServerUtil.isServerFileExist();
        if(isServerFileExist){
            FindServerUtil.initServerAddress();
        }
    }

}
