package me.zhouxi.iot.nfc;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import me.zhouxi.iot.R;
import me.zhouxi.iot.client.nfc.GetAllRecordDataThread;
import me.zhouxi.iot.client.nfc.object.OpenDoorRecordObject;
import me.zhouxi.iot.nfc.adapter.NFCRecordAdapter;
import me.zhouxi.iot.ui.MyActivity;

/**
 * Created by zhouxi on 17年11月1日.
 */

public class AllOpenDoorRecordActivity extends MyActivity implements
        GetAllRecordDataThread.GetAllRecordDataThreadListener{

    private ProgressBar progressBar;

    private List<OpenDoorRecordObject> openDoorRecordObjects;

    private NFCRecordAdapter nfcRecordAdapter;

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_open_door_record);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        openDoorRecordObjects = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        nfcRecordAdapter = new NFCRecordAdapter(openDoorRecordObjects,recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(nfcRecordAdapter);
        getData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_all_open_door_record_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_refresh:
                getData();
                break;
        }
        return true;
    }

    private void getData(){
        progressBar.setVisibility(View.VISIBLE);
        GetAllRecordDataThread getAllRecordDataThread = new GetAllRecordDataThread();
        getAllRecordDataThread.setGetAllRecordDataThreadListener(this);
        getAllRecordDataThread.start();
    }

    @Override
    public void onGetAllRecordDataThreadSuccess(String data) {
        List<OpenDoorRecordObject> openDoorRecordObjectList = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(data);
            for(int i = 0 ; i < jsonArray.length() ; i ++ ){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                OpenDoorRecordObject openDoorRecordObject = new OpenDoorRecordObject();
                openDoorRecordObject.time = jsonObject.getLong("time");
                openDoorRecordObject.device = jsonObject.getString("device");
                openDoorRecordObject.name = jsonObject.getString("name");
                openDoorRecordObjectList.add(openDoorRecordObject);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        openDoorRecordObjects.clear();
        openDoorRecordObjects.addAll(openDoorRecordObjectList);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.GONE);
                nfcRecordAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onGetAllRecordDataThreadFailed() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.GONE);
            }
        });
    }
}
