package me.zhouxi.iot;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import me.zhouxi.iot.client.find_server.FindServerUtil;
import me.zhouxi.iot.ui.MyActivity;

/**
 * Created by zhouxi on 10/10/2017.
 */

public class ModeSwitchActivity extends MyActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkAndGotoActivity();
    }

    private void checkAndGotoActivity(){
        boolean isServerFileExist = FindServerUtil.isServerFileExist();
        if(isServerFileExist){
            gotoMainActivity();
        }else{
            gotoFindServerActivity();
        }
    }

    private void gotoFindServerActivity(){
        Intent intent = new Intent(this,FindServerActivity.class);
        startActivity(intent);
        finish();
    }

    private void gotoMainActivity(){
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
        finish();
    }
}
