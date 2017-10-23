package me.zhouxi.iot.nfc;

import android.content.DialogInterface;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.nfc.tech.TagTechnology;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import me.zhouxi.iot.R;
import me.zhouxi.iot.ui.MyActivity;

/**
 * Created by zhouxi on 23/10/2017.
 */

public class OpenDoorActivity extends MyActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_door);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);




        configNFC();
    }


    private void configNFC(){
        boolean isSupportNFC = NFCUtil.isSupportNFC();
        if(isSupportNFC){
            if(NFCUtil.isOpenNFC()){
                nfcSetting();
            }else{
                needToOpenNFC();
            }
        }else{
            deviceNotNFC();
        }
    }

    private void nfcSetting(){
//        Tag tag = Tag
//        Ndef.get()

    }

    /**
     * not open nfc
     */
    private void needToOpenNFC(){
        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_NEUTRAL:
                        NFCUtil.gotoNFCSettings();
                    case DialogInterface.BUTTON_POSITIVE:
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                configNFC();
                            }
                        });
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        closeThisActivity();
                        break;
                }
            }
        };
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setTitle(getString(R.string.hint))
                .setMessage(getString(R.string.not_open_nfc))
                .setPositiveButton(getString(R.string.retry),listener)
                .setNegativeButton(getString(R.string.back),listener)
                .setNeutralButton(getString(R.string.goto_settings),listener)
                .create();
        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
    }

    /**
     * not nfc alert
     */
    private void deviceNotNFC(){
        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(which == DialogInterface.BUTTON_POSITIVE){
                    closeThisActivity();
                }
            }
        };
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setTitle(getString(R.string.hint))
                .setMessage(getString(R.string.device_not_nfc))
                .setPositiveButton(getString(R.string.back),listener)
                .create();
        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
    }

    /**
     * close this activity
     */
    private void closeThisActivity(){
        finish();
    }

}
