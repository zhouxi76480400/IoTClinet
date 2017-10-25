package me.zhouxi.iot.client.nfc.sql;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import me.zhouxi.iot.IoTApplication;
import me.zhouxi.iot.client.nfc.object.NFCKeyObject;

/**
 * Created by zhouxi on 25/10/2017.
 */

public class NFCKeyDBAPI{

    private Context context;

    private NFCKeyDBAPI(Context context){
        super();
        this.context = context;
    }

    private static NFCKeyDBAPI nfcKeyDBAPI;

    public static NFCKeyDBAPI getInstance(){
        if(nfcKeyDBAPI == null)
            nfcKeyDBAPI = new NFCKeyDBAPI(IoTApplication.getInstance());
        return nfcKeyDBAPI;
    }

    public List<NFCKeyObject> readAllData(){
        NFCKeyDBOpenHelper nfcKeyDBOpenHelper = new NFCKeyDBOpenHelper(context);
        SQLiteDatabase database = nfcKeyDBOpenHelper.getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM " + NFCKeyDBOpenHelper.TABLE_NAME ,null);
        List<NFCKeyObject> objects = new ArrayList<>();
        while (cursor.moveToNext()){
            long pk = cursor.getLong(0);
            String data = cursor.getString(1);
            NFCKeyObject nfcKeyObject = new NFCKeyObject();
            nfcKeyObject.create_time = pk;
            nfcKeyObject.key = data;
            objects.add(nfcKeyObject);
        }
        cursor.close();
        database.close();
        nfcKeyDBOpenHelper.close();
        return objects;
    }

    public void insertAData(String pk,String data){
        NFCKeyDBOpenHelper nfcKeyDBOpenHelper = new NFCKeyDBOpenHelper(context);
        SQLiteDatabase database = nfcKeyDBOpenHelper.getWritableDatabase();
        database.beginTransaction();
        String sql = "INSERT INTO " + NFCKeyDBOpenHelper.TABLE_NAME + " (" + NFCKeyDBOpenHelper.PK +
                ", " + NFCKeyDBOpenHelper.KEY_NFC_KEY + ") VALUES ('" + pk +"', '" + data + "')";
//        Log.e("test",sql);
        database.execSQL(sql);
        database.setTransactionSuccessful();
        database.endTransaction();
        database.close();
        nfcKeyDBOpenHelper.close();
    }
}
