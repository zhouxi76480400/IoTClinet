package me.zhouxi.iot.nfc;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;

import me.zhouxi.iot.IoTApplication;
import me.zhouxi.iot.R;
import me.zhouxi.iot.client.nfc.CheckIsHavePasswordThread;
import me.zhouxi.iot.common.CommonUtil;
import me.zhouxi.iot.nfc.fragment.AddNFCCardFragment1;
import me.zhouxi.iot.nfc.fragment.AddNFCCardFragment2;
import me.zhouxi.iot.nfc.fragment.AddNFCCardFragment3;
import me.zhouxi.iot.ui.MyActivity;

/**
 * Created by zhouxi on 23/10/2017.
 */

public class AddNFCCardActivity extends MyActivity implements
        AddNFCCardFragment1.AddNFCCardFragment1Listener,CheckIsHavePasswordThread.CheckIsHavePasswordThreadListener,
        AddNFCCardFragment2.AddNFCCardFragment2Listener,AddNFCCardFragment3.AddNFCCardFragment3Listener {

    /**
     * use as add card
     */
    public static final String TAG_ADD_CARD_INTENT = "TAG_ADD_CARD_INTENT";

    private FrameLayout frameLayout;

    private AddNFCCardFragment1 fragment1;

    private AddNFCCardFragment2 fragment2;

    private AddNFCCardFragment3 fragment3;

    private AlertDialog noWifiNetworkDialog;

    private CheckIsHavePasswordThread checkIsHavePasswordThread;

    private boolean isHavePassword;

    private String tmpPwd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        transparentStatusBar();
        setContentView(R.layout.activity_add_nfc_card);
        frameLayout = (FrameLayout) findViewById(R.id.frameLayout);
        fragment1 = new AddNFCCardFragment1();
        fragment1.setAddNFCCardFragment1Listener(this);
        fragment2 = new AddNFCCardFragment2();
        fragment2.setAddNFCCardFragment2Listener(this);
        fragment3 = new AddNFCCardFragment3();
        fragment3.setAddNFCCardFragment3Listener(this);
        gotoView1(false);
        frameLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                checkServerIsHavePassword();
            }
        },100);
    }

    private void transparentStatusBar(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }

    @Override
    public void onNextPress() {
        gotoView2();
    }

    private void gotoView1(boolean showAnim){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if(showAnim){
            transaction.setCustomAnimations(R.anim.fragment_in_reverse,R.anim.fragment_out_reverse);
        }
        transaction.replace(frameLayout.getId(),fragment1).commit();
    }

    private void gotoView2(){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.fragment_in,R.anim.fragment_out);
        transaction.replace(frameLayout.getId(),fragment2).commit();
    }

    private void backtoView2(){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.fragment_in_reverse,R.anim.fragment_out_reverse);
        transaction.replace(frameLayout.getId(),fragment2).commit();
    }

    private void gotoView3(){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.fragment_in,R.anim.fragment_out);
        transaction.replace(frameLayout.getId(),fragment3).commit();
    }

    @Override
    public void onBackPressed() {
        if(fragment3.isSuccessful()){
            finishToAddCard();
        }else{
            if(fragment2.isVisible()){
                gotoView1(true);
            }else if(fragment1.isVisible()){
                showExitAlert();
            }else if(fragment3.isVisible()){
                if(!fragment3.isStartRequest())
                    backtoView2();
                else
                    showExitAlert();
            }
        }
    }

    private void checkServerIsHavePassword(){
        fragment1.progressBar.setVisibility(View.VISIBLE);
        fragment1.enableButton(false);
        if(CommonUtil.getNetworkInfo() == null){
            networkDown(false,false);
            return;
        }
        if(checkIsHavePasswordThread == null)
            checkIsHavePasswordThread = new CheckIsHavePasswordThread(this);
        checkIsHavePasswordThread.startCheck();
    }

    @Override
    public void serverHavePassword() {
        isHavePassword = true;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                canDoNext();
            }
        });
    }

    @Override
    public void serverNotHavePassword() {
        isHavePassword = false;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                canDoNext();
            }
        });
    }

    private void canDoNext(){
        fragment1.progressBar.setVisibility(View.GONE);
        fragment1.enableButton(true);
        fragment2.setPasswordExist(isHavePassword);

    }

    @Override
    public void networkIsDown() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                networkDown(true,false);
            }
        });
    }

    private void networkDown(boolean exception, final boolean sw){
        if (noWifiNetworkDialog == null) {
            DialogInterface.OnClickListener onClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    switch (i){
                        case DialogInterface.BUTTON_NEUTRAL:
                            startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                        case DialogInterface.BUTTON_POSITIVE:
                            if(!sw){
                                frameLayout.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        checkServerIsHavePassword();
                                    }
                                },100);
                            }else{
                                frameLayout.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        onAddNFCCardFragment2PressNext(tmpPwd);
                                    }
                                },100);
                            }
                            break;
                        case DialogInterface.BUTTON_NEGATIVE:
                            superOnBackPressed();
                            break;
                    }
                }
            };
            AlertDialog alertDialog = new AlertDialog.Builder(this)
                    .setTitle(R.string.hint)
                    .setPositiveButton(R.string.retry, onClickListener)
                    .setNegativeButton(R.string.exit, onClickListener)
                    .setNeutralButton(R.string.open_wifi_settings, onClickListener)
                    .create();
            alertDialog.setCancelable(false);
            alertDialog.setCanceledOnTouchOutside(false);
            noWifiNetworkDialog = alertDialog;
        }
        String hint = null;
        if(exception){
            hint = getString(R.string.cannot_connect_to_server);
        }else {
            if (CommonUtil.getNetworkInfo() != null) {
                hint = getString(R.string.not_in_wifi_hint);
            } else {
                hint = getString(R.string.no_any_network_hint);
            }
        }
        noWifiNetworkDialog.setMessage(hint);
        noWifiNetworkDialog.show();
    }

    private void superOnBackPressed(){
        super.onBackPressed();
    }

    private void showExitAlert(){
        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(i == DialogInterface.BUTTON_POSITIVE){
                    superOnBackPressed();
                }
            }
        };
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setTitle(getString(R.string.hint))
                .setMessage(getString(R.string.want_exit_add_new_card))
                .setPositiveButton(getString(R.string.ok),listener)
                .setNegativeButton(getString(R.string.cancel),listener)
                .create();
        alertDialog.show();
    }

    @Override
    public void onAddNFCCardFragment2PressNext(String pwd) {
        tmpPwd = pwd;
        if(CommonUtil.getNetworkInfo() == null){
            networkDown(false,true);
            return;
        }
        gotoView3();
        frameLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                 fragment3.setOrAuthPassword(tmpPwd,isHavePassword);
            }
        },300);
    }

    private void showRetryAlertDialog(final boolean isPwdWrong){
        DialogInterface.OnClickListener onClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                switch (i){
                    case DialogInterface.BUTTON_POSITIVE:
                        if(!isPwdWrong)
                            fragment3.setOrAuthPassword(tmpPwd,isHavePassword);
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        backtoView2();
                        break;
                }
            }
        };
        int message = R.string.set_password_fail;
        int pos = R.string.retry;
        if(isPwdWrong){
            message = R.string.auth_failed;
            pos = R.string.ok;
        }
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setTitle(R.string.hint)
                .setMessage(message)
                .setPositiveButton(pos, onClickListener)
                .create();
        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(false);
        if(!isPwdWrong){
            alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE,getString(R.string.back), onClickListener);
        }
        alertDialog.show();
    }

    @Override
    public void onAddNFCCardFragment3SetPasswordFailed(final boolean networkException) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showRetryAlertDialog(false);
            }
        });
    }

    @Override
    public void onAddNFCCardFragment3AuthFailed(boolean networkException) {
        backtoView2();
        frameLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                fragment2.setText(tmpPwd);
                showRetryAlertDialog(true);
            }
        },300);
    }

    @Override
    public void finishToAddCard(){
        setResult(RESULT_OK);
        finish();
    }
}
