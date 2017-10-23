package me.zhouxi.iot.nfc;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;

import me.zhouxi.iot.R;
import me.zhouxi.iot.nfc.fragment.AddNFCCardFragment1;
import me.zhouxi.iot.nfc.fragment.AddNFCCardFragment2;
import me.zhouxi.iot.ui.MyActivity;

/**
 * Created by zhouxi on 23/10/2017.
 */

public class AddNFCCardActivity extends MyActivity implements
        AddNFCCardFragment1.AddNFCCardFragment1Listener{

    /**
     * use as add card
     */
    public static final String TAG_ADD_CARD_INTENT = "TAG_ADD_CARD_INTENT";

    private FrameLayout frameLayout;

    private AddNFCCardFragment1 fragment1;

    private AddNFCCardFragment2 fragment2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        transparentStatusBar();
        setContentView(R.layout.activity_add_nfc_card);
        frameLayout = (FrameLayout) findViewById(R.id.frameLayout);
        fragment1 = new AddNFCCardFragment1();
        fragment1.setAddNFCCardFragment1Listener(this);
        fragment2 = new AddNFCCardFragment2();
        gotoView1(false);
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

    @Override
    public void onBackPressed() {
        if(!fragment1.isVisible()){
            gotoView1(true);
        }else {
            super.onBackPressed();
        }
    }
}
