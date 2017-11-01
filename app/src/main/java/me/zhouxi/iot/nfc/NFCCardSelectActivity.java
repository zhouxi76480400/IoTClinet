package me.zhouxi.iot.nfc;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import me.zhouxi.iot.R;
import me.zhouxi.iot.client.nfc.object.NFCKeyObject;
import me.zhouxi.iot.nfc.adapter.NFCCardAdapter;
import me.zhouxi.iot.ui.MyActivity;

/**
 * Created by zhouxi on 23/10/2017.
 */

public class NFCCardSelectActivity extends MyActivity implements NFCCardAdapter.NFCCardAdapterListener{

    private Toolbar toolbar;

    private AlertDialog noCardAlertDialog;

    private List<NFCKeyObject> nfcKeyObjects;

    private NFCCardAdapter nfcCardAdapter;

    private RecyclerView recyclerView;

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
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        nfcKeyObjects = new ArrayList<>();
        nfcCardAdapter = new NFCCardAdapter(nfcKeyObjects);
        nfcCardAdapter.setShowModify(false);
        nfcCardAdapter.setNFCCardAdapterListener(this);
        recyclerView.setAdapter(nfcCardAdapter);
        readCardsFromData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_nfc_card_select,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_add_card:
                gotoAddCardActivity();
                break;
            case R.id.all_records:
                openAllRecordsActivity();
                break;
        }
        return true;
    }

    /**
     * read signed card
     */
    private void readCardsFromData(){
        List<NFCKeyObject> card = NFCCardUtil.getNFCDataObject();
        if(card.size() == 0){
            noCardAlert();
            return;
        }
        nfcKeyObjects.clear();
        nfcKeyObjects.addAll(card);
        nfcCardAdapter.notifyDataSetChanged();
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
        if(requestCode == AddNFCCardActivity.CONTEXT_INCLUDE_CODE && resultCode == RESULT_OK){
            if(noCardAlertDialog != null && noCardAlertDialog.isShowing())
                noCardAlertDialog.dismiss();
            readCardsFromData();
        }
    }

    private void closeActivity(){
        finish();
    }

    @Override
    public void onNFCCardAdapterPress(int position) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("key",nfcKeyObjects.get(position));
        Intent intent = new Intent(this,OpenDoorActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void openAllRecordsActivity(){
        Intent intent = new Intent(this,AllOpenDoorRecordActivity.class);
        startActivity(intent);
    }
}
