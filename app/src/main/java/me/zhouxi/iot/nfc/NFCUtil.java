package me.zhouxi.iot.nfc;

import android.content.Intent;
import android.nfc.NfcAdapter;
import android.provider.Settings;

import me.zhouxi.iot.IoTApplication;

/**
 * Created by zhouxi on 23/10/2017.
 */

public class NFCUtil {

    /**
     * check is support nfc feature
     * @return
     */
    public static boolean isSupportNFC(){
        NfcAdapter nfcAdapter = NFCUtil.getNFCAdapter();
        if(nfcAdapter == null)
            return false;
        else
            return true;
    }

    /**
     * is nfc opened
     * @return
     */
    public static boolean isOpenNFC(){
        NfcAdapter nfcAdapter = NFCUtil.getNFCAdapter();
        return nfcAdapter.isEnabled();
    }

    /**
     * get nfc Adapter object
     * @return
     */
    public static NfcAdapter getNFCAdapter(){
        IoTApplication application = IoTApplication.getInstance();
        NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(application);
        return nfcAdapter;
    }

    /**
     * goto system nfc adapter settings
     */
    public static void gotoNFCSettings(){
        Intent intent = new Intent(Settings.ACTION_NFC_SETTINGS);
        IoTApplication application = IoTApplication.getInstance();
        application.startActivity(intent);
    }

    public static String getMyNFCTag(NfcAdapter nfcAdapter){



        return "";
    }

}
