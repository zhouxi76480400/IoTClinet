package me.zhouxi.iot;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import me.zhouxi.iot.client.APIList;
import me.zhouxi.iot.client.APIs;
import me.zhouxi.iot.client.SocketClient;
import me.zhouxi.iot.nfc.NFCCardSelectActivity;
import me.zhouxi.iot.nfc.OpenDoorActivity;
import me.zhouxi.iot.object.ItemDetailObject;
import me.zhouxi.iot.ui.MyActivity;
import me.zhouxi.iot.ui.adapter.MainActivityRecyclerViewAdapter;

public class MainActivity extends MyActivity implements
        Toolbar.OnMenuItemClickListener, SwipeRefreshLayout.OnRefreshListener ,
        MainActivityRecyclerViewAdapter.MainActivityRecyclerViewAdapterOnItemPressListener {

    // used as recycler view
    private List<ItemDetailObject> itemDetailObjects;

    private SwipeRefreshLayout swipeRefreshLayout;

    private RecyclerView recyclerView;
    private MainActivityRecyclerViewAdapter adapter;

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setOnMenuItemClickListener(this);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent,R.color.colorPrimary);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setEnabled(false);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(recyclerView.getContext());
        recyclerView.setLayoutManager(layoutManager);
        itemDetailObjects = new ArrayList<>();
        adapter = new MainActivityRecyclerViewAdapter(recyclerView,itemDetailObjects);
        adapter.setMainActivityRecyclerViewAdapterOnItemPressListener(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        swipeRefreshLayout.setRefreshing(true);
        getAllDevice();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_main,menu);
        return true;
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()){
            case R.id.main_activity_setting:
                gotoSettingsActivity();
                break;
        }
        return true;
    }

    @Override
    public void onRefresh() {
        getAllDevice();
    }

    /**
     * call this method to get all devices information
     */
    private void getAllDevice(){
        swipeRefreshLayout.setEnabled(true);
        APIList.getInstance().getAllDevices(this);
    }

    // caution! this not main thread method
    @Override
    public void apiListListenerOnDataReceived(APIs type, String data) {
        List<ItemDetailObject> allData = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(data);
            for(int i = 0 ; i < jsonArray.length() ; i ++ ){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                ItemDetailObject itemDetailObject = new ItemDetailObject();
                itemDetailObject.id = jsonObject.getInt("id");
                itemDetailObject.desc = jsonObject.getString("desc");
                itemDetailObject.name = jsonObject.getString("name");
                allData.add(itemDetailObject);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // sort
        Collections.sort(allData, new Comparator<ItemDetailObject>() {
            @Override
            public int compare(ItemDetailObject o1, ItemDetailObject o2) {

                return Integer.compare(o1.id,o2.id);
            }
        });
        // update data
        IoTApplication.getInstance().cleanAndResetItemDetailObjectList(allData);
        refreshMainActivity();
    }

    /**
     * refresh
     */
    private void refreshMainActivity(){
        itemDetailObjects.clear();
        itemDetailObjects.addAll(IoTApplication.getInstance().getItemDetailObjectList());



        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(false);
                swipeRefreshLayout.setEnabled(true);
                adapter.notifyDataSetChanged();
                setIsOnline(true);
            }
        });
    }

    @Override
    public void apiListListenerOnError(APIs type, Exception e) {
        super.apiListListenerOnError(type, e);
        final List<ItemDetailObject> itemDetailObjects = IoTApplication.getInstance().getSavedIndexData();
        Log.e("zhouxi",itemDetailObjects.toString());
        if(itemDetailObjects.size() > 0){
            IoTApplication.getInstance().cleanAndResetItemDetailObjectList(itemDetailObjects);;
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(false);
                swipeRefreshLayout.setEnabled(true);
                setIsOnline(false);
                if(itemDetailObjects.size() > 0){
                    refreshMainActivity();
                }
            }
        });
    }

    private void setIsOnline(boolean isOnline){
        String online_str = null;
        if(isOnline){
            online_str = getString(R.string.online);
        }else{
            online_str = getString(R.string.offline);
        }
        toolbar.setSubtitle(String.format(getString(R.string.status),online_str));
    }

    /**
     * goto settings
     */
    private void gotoSettingsActivity() {
        Intent intent = new Intent(this,SettingActivity.class);
        startActivity(intent);
    }

    @Override
    public void onMainActivityRecyclerViewAdapterOnItemPressListener
            (MainActivityRecyclerViewAdapter adapter, int position) {
        ItemDetailObject itemDetailObject = itemDetailObjects.get(position);
        Log.e("test",itemDetailObject.name);
        switch (itemDetailObject.id){
            case 0://
                gotoNFCCardSelectActivity();
                break;
        }
    }

    /**
     * select card to open door
     */
    private void gotoNFCCardSelectActivity(){
        Intent intent = new Intent(this, NFCCardSelectActivity.class);
        startActivity(intent);
    }

}
