package me.zhouxi.iot.nfc;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOError;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import me.zhouxi.iot.IoTApplication;
import me.zhouxi.iot.nfc.object.NFCCardDataObject;

/**
 * Created by zhouxi on 23/10/2017.
 */

public class NFCCardUtil {

    public static final String CARD_FILE_TAG = "NFC_TAGS";

    public static List<NFCCardDataObject> getNFCDataObject(){
        List<NFCCardDataObject> objects = new ArrayList<>();
        IoTApplication application = IoTApplication.getInstance();
        File dataDir = application.getFilesDir();
        File cardsFile = new File(dataDir,CARD_FILE_TAG);
        if(!cardsFile.exists()){
            try {
                cardsFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        // read file
        String fileContent = null;
        try {
            FileInputStream inputStream = new FileInputStream(cardsFile);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = inputStream.read(buffer)) != -1){
                byteArrayOutputStream.write(buffer,0,len);
            }
            fileContent = byteArrayOutputStream.toString();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(fileContent != null){
            try {
                JSONArray array = new JSONArray(fileContent);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return objects;
    }




}
