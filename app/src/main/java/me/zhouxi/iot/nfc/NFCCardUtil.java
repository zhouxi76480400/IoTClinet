package me.zhouxi.iot.nfc;

import java.util.List;

import me.zhouxi.iot.IoTApplication;
import me.zhouxi.iot.client.nfc.object.NFCKeyObject;
import me.zhouxi.iot.client.nfc.sql.NFCKeyDBAPI;

/**
 * Created by zhouxi on 23/10/2017.
 */

public class NFCCardUtil {

    public static List<NFCKeyObject> getNFCDataObject(){
        NFCKeyDBAPI nfcKeyDBAPI = NFCKeyDBAPI.getInstance();
        return nfcKeyDBAPI.readAllData();
    }



}
