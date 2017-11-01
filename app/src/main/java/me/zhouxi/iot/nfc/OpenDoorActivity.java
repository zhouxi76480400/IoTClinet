package me.zhouxi.iot.nfc;

import android.content.DialogInterface;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcEvent;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.nfc.tech.TagTechnology;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.Serializable;
import java.nio.charset.Charset;

import me.zhouxi.iot.R;
import me.zhouxi.iot.client.nfc.object.NFCKeyObject;
import me.zhouxi.iot.ui.MyActivity;

/**
 * Created by zhouxi on 23/10/2017.
 */

public class OpenDoorActivity extends MyActivity implements NfcAdapter.OnNdefPushCompleteCallback,
        NfcAdapter.CreateNdefMessageCallback{

    private AlertDialog not_nfc_dialog;

    private AlertDialog nfc_not_open_dialog;

    private NFCKeyObject nfcKeyObject;

    private boolean isCallbackCreated;

    private Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_door);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if(getIntent().getExtras() != null){
            Serializable serializable = getIntent().getExtras().getSerializable("key");
            if(serializable != null && serializable instanceof NFCKeyObject){
                nfcKeyObject = (NFCKeyObject) serializable;
                toolbar.setTitle(getString(R.string.open_door_title)+":"+nfcKeyObject.create_time);
            }else {
                finish();
            }
        }else{
            finish();
        }
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        configNFC();
    }

    private void configNFC(){
        if(nfc_not_open_dialog != null)
            if(nfc_not_open_dialog.isShowing())
                nfc_not_open_dialog.dismiss();
        if(not_nfc_dialog != null)
            if(not_nfc_dialog.isShowing())
                not_nfc_dialog.dismiss();

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
        if(!isCallbackCreated){
            NfcAdapter adapter = NfcAdapter.getDefaultAdapter(this);
            adapter.setNdefPushMessageCallback(this,this);
            adapter.setOnNdefPushCompleteCallback(this,this);
            isCallbackCreated  = true;
        }
    }

    /**
     * not open nfc
     */
    private void needToOpenNFC(){
        if(nfc_not_open_dialog == null){
            DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which){
                        case DialogInterface.BUTTON_NEUTRAL:
                            NFCUtil.gotoNFCSettings();
                        case DialogInterface.BUTTON_POSITIVE:
                            toolbar.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    configNFC();
                                }
                            },100);
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
            nfc_not_open_dialog = alertDialog;
        }
        nfc_not_open_dialog.show();
    }

    /**
     * not nfc alert
     */
    private void deviceNotNFC(){
        if(not_nfc_dialog == null){
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
            not_nfc_dialog = alertDialog;
        }
        not_nfc_dialog.show();
    }

    /**
     * close this activity
     */
    private void closeThisActivity(){
        finish();
    }

    @Override
    public NdefMessage createNdefMessage(NfcEvent event) {
        byte[] mimeBytes = String .format("application/%s",getPackageName())
                .getBytes(Charset.forName("US-ASCII"));
        byte[] id = String.valueOf(Build.MODEL).getBytes(Charset.forName("US-ASCII"));
        byte[] payload = String.format("%s,%s",nfcKeyObject.create_time,nfcKeyObject.key).getBytes(Charset.forName("US-ASCII"));
        NdefMessage message = new NdefMessage(new NdefRecord[] {
                new NdefRecord(NdefRecord.TNF_MIME_MEDIA, mimeBytes, id, payload)});
        return message;
    }

    @Override
    public void onNdefPushComplete(NfcEvent event) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(OpenDoorActivity.this,getString(R.string.success_to_open_door),Toast.LENGTH_SHORT).show();
                closeThisActivity();
            }
        });
    }
}
