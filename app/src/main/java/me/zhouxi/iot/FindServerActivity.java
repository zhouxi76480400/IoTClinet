package me.zhouxi.iot;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;

import me.zhouxi.iot.client.find_server.FindServerClass;
import me.zhouxi.iot.client.find_server.FindServerUtil;
import me.zhouxi.iot.client.find_server.ServerObject;
import me.zhouxi.iot.common.CommonUtil;
import me.zhouxi.iot.ui.MyActivity;
import me.zhouxi.iot.ui.adapter.FindServerRecyclerViewAdapter;

/**
 * Created by zhouxi on 10/10/2017.
 */

public class FindServerActivity extends MyActivity implements DialogInterface.OnClickListener
        , FindServerClass.OnFindServerListener, Toolbar.OnMenuItemClickListener
        , FindServerRecyclerViewAdapter.FindServerRecyclerViewAdapterListener {

    private boolean isFindServerNow; // default is false

    private ProgressBar progressBar;

    private AlertDialog noWifiNetworkDialog;

    private AlertDialog noAnyServerDialog;

    private FindServerClass findServerClass;

    private RecyclerView recyclerView;

    private List<ServerObject> serverObjectList;

    private FindServerRecyclerViewAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_server);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setOnMenuItemClickListener(this);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        serverObjectList = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        adapter = new FindServerRecyclerViewAdapter(recyclerView, serverObjectList);
        adapter.setListener(this);
        recyclerView.setAdapter(adapter);
        startFindServer();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_find_server, menu);
        return true;
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_refresh:
                onMenuRefreshButtonPress();
                break;
        }
        return true;
    }

    private void onMenuRefreshButtonPress() {
        if (!isFindServerNow) {
            startFindServer();
        }
    }

    /**
     * prepare for find server
     */
    private void startFindServer() {
        NetworkInfo networkInfo = CommonUtil.getNetworkInfo();
        if (networkInfo != null && networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
            findServer();
        } else {
            // not wifi network or now any network , check wifi is open ?
            showOpenWifiDialog();
        }
    }

    private void findServer() {
        isFindServerNow = true;
        getSupportActionBar().setSubtitle("");
        serverObjectList.clear();
        adapter.notifyDataSetChanged();
        progressBar.setVisibility(View.VISIBLE);
        if (findServerClass == null) {
            findServerClass = new FindServerClass(this);
        }
        findServerClass.startFindServer();
    }

    @Override
    public void onServerFounded(List<ServerObject> serverList) {
        handleServers(serverList);
        isFindServerNow = false;
    }

    private void handleServers(List<ServerObject> serverObjects) {
        serverObjectList.clear();
        serverObjectList.addAll(serverObjects);
        // is not main thread now , must goto main thread to refresh UI
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                getSupportActionBar().setSubtitle(getString(R.string.select_a_server));
                progressBar.setVisibility(View.GONE);
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void findServerRecyclerViewAdapterListenerOnItemPress(int position) {
        ServerObject serverObject = serverObjectList.get(position);
        setNowServer(serverObject);
    }

    @Override
    public void onServerNotFound() {
        isFindServerNow = false;
        // is not main thread now , must goto main thread to refresh UI
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                openNoAnyServerDialog();
            }
        });
    }

    private void showOpenWifiDialog() {
        if (noWifiNetworkDialog == null) {
            String hint = null;
            if (CommonUtil.getNetworkInfo() != null) {
                hint = getString(R.string.not_in_wifi_hint);
            } else {
                hint = getString(R.string.no_any_network_hint);
            }
            AlertDialog alertDialog = new AlertDialog.Builder(this)
                    .setTitle(R.string.hint)
                    .setMessage(hint)
                    .setPositiveButton(R.string.retry, this)
                    .setNegativeButton(R.string.exit, this)
                    .setNeutralButton(R.string.open_wifi_settings, this)
                    .create();
            alertDialog.setCancelable(false);
            alertDialog.setCanceledOnTouchOutside(false);
            noWifiNetworkDialog = alertDialog;
        }
        noWifiNetworkDialog.show();
    }

    private void openNoAnyServerDialog() {
        if (noAnyServerDialog == null) {
            AlertDialog alertDialog = new AlertDialog.Builder(this)
                    .setTitle(R.string.hint)
                    .setMessage(R.string.no_any_server_hint)
                    .setPositiveButton(R.string.retry, this)
                    .setNegativeButton(R.string.exit, this)
                    .create();
            alertDialog.setCancelable(false);
            alertDialog.setCanceledOnTouchOutside(false);
            noAnyServerDialog = alertDialog;
        }
        noAnyServerDialog.show();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        if (dialog == noWifiNetworkDialog) {
            switch (which) {
                case DialogInterface.BUTTON_POSITIVE:
                    delayToStartFindServer();
                    break;
                case DialogInterface.BUTTON_NEGATIVE:
                    System.exit(0);
                    break;
                case DialogInterface.BUTTON_NEUTRAL:
                    openWifiSettings();
                    delayToStartFindServer();
                    break;
            }
        } else if (dialog == noAnyServerDialog) {
            switch (which) {
                case Dialog.BUTTON_POSITIVE:
                    delayToStartFindServer();
                    break;
                case DialogInterface.BUTTON_NEGATIVE:
                    System.exit(0);
                    break;
            }
        }
    }

    private void delayToStartFindServer() {
        progressBar.getRootView().postDelayed(new Runnable() {
            @Override
            public void run() {
                startFindServer();
            }
        }, 100);
    }

    private void openWifiSettings() {
        startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
    }

    /**
     * server now server
     * @param server
     */
    private void setNowServer(ServerObject server) {
        FindServerUtil.setNowServer(server);
        FindServerUtil.initServerAddress();
        gotoMainActivity();
        finish();
    }

    private void gotoMainActivity(){
        startActivity(new Intent(this,MainActivity.class));
    }
}
