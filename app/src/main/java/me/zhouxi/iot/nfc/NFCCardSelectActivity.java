package me.zhouxi.iot.nfc;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.List;

import me.zhouxi.iot.R;
import me.zhouxi.iot.nfc.object.NFCCardDataObject;
import me.zhouxi.iot.ui.MyActivity;

/**
 * Created by zhouxi on 23/10/2017.
 */

public class NFCCardSelectActivity extends MyActivity {

    private Toolbar toolbar;

    private AlertDialog noCardAlertDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfc_card_select);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeActivity();
            }
        });




        readCardsFromData();
    }

    /**
     * read signed card
     */
    private void readCardsFromData(){
        List<NFCCardDataObject> card = NFCCardUtil.getNFCDataObject();
        if(card.size() == 0){
            noCardAlert();
            return;
        }

    }

    /**
     * no one card
     */
    private void noCardAlert(){
        if(noCardAlertDialog == null){
            DialogInterface.OnClickListener onClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which){
                        case DialogInterface.BUTTON_NEUTRAL:
                            gotoAddCardActivity();
                        case DialogInterface.BUTTON_POSITIVE:
                            toolbar.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    readCardsFromData();
                                }
                            },100);
                            break;
                        case DialogInterface.BUTTON_NEGATIVE:
                            closeActivity();
                            break;
                    }
                }
            };
            AlertDialog alertDialog = new AlertDialog.Builder(this)
                    .setTitle(getString(R.string.hint))
                    .setMessage(getString(R.string.no_one_nfc_card_message))
                    .setPositiveButton(getString(R.string.retry),onClickListener)
                    .setNegativeButton(getString(R.string.back),onClickListener)
                    .setNeutralButton(getString(R.string.add_card),onClickListener)
                    .create();
            alertDialog.setCancelable(false);
            alertDialog.setCanceledOnTouchOutside(false);
            noCardAlertDialog = alertDialog;
        }
        noCardAlertDialog.show();
    }

    private void gotoAddCardActivity(){
        Intent intent = new Intent(this,AddNFCCardActivity.class);
        startActivityForResult(intent,AddNFCCardActivity.CONTEXT_INCLUDE_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == AddNFCCardActivity.CONTEXT_INCLUDE_CODE){

        }
    }

    private void closeActivity(){
        finish();
    }
}
