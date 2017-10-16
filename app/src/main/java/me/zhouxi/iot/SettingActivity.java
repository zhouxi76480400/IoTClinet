package me.zhouxi.iot;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.List;

import me.zhouxi.iot.client.UDPSocketThread;
import me.zhouxi.iot.client.find_server.FindServerClass;
import me.zhouxi.iot.client.find_server.ServerObject;

/**
 * Created by zhouxi on 6/10/2017.
 */

public class SettingActivity extends AppCompatActivity{

    private Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activitiy_settting);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        final FindServerClass findServerClass = new FindServerClass(new FindServerClass.OnFindServerListener() {
            @Override
            public void onServerFounded(List<ServerObject> serverList) {
                Log.e("test","aaaaa"+serverList.size());
            }

            @Override
            public void onServerNotFound() {
                Log.e("test","onServerNotFound");
            }
        });
        findServerClass.startFindServer();

        Button btn = (Button) findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findServerClass.startFindServer();
            }
        });
    }

}
