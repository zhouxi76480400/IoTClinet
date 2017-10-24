package me.zhouxi.iot.nfc.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import me.zhouxi.iot.R;
import me.zhouxi.iot.client.nfc.PasswordAuthenticationThread;
import me.zhouxi.iot.client.nfc.SetPasswordThread;

/**
 * Created by zhouxi on 24/10/2017.
 */

public class AddNFCCardFragment3 extends Fragment implements SetPasswordThread.SetPasswordThreadListener,
        PasswordAuthenticationThread.PasswordAuthenticationThreadListener{

    public interface AddNFCCardFragment3Listener{

        void onAddNFCCardFragment3SetPasswordFailed(boolean networkException);

        void onAddNFCCardFragment3AuthFailed(boolean networkException);

    }

    private AddNFCCardFragment3Listener listener;

    public void setAddNFCCardFragment3Listener(AddNFCCardFragment3Listener listener){
        this.listener = listener;
    }

    private boolean isStartRequest;

    public boolean isStartRequest(){
        return isStartRequest;
    }

    private LinearLayout ll_top;

    private TextView tv_title;

    private TextView tv_desc;

    private Button btn_finish;


    private String pwdTmp;
    private boolean isHavePassword;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.view_add_nfc_card_fragment3, container, false);
        ll_top = (LinearLayout) view.findViewById(R.id.ll_top);
        int all_height = getHeightPixel();
        ll_top.getLayoutParams().height = all_height * 45 / 100;
        tv_title = (TextView) view.findViewById(R.id.tv_title);
        btn_finish = (Button) view.findViewById(R.id.btn_finish);
        btn_finish.setVisibility(View.GONE);
        tv_desc = (TextView) view.findViewById(R.id.tv_desc);

        return view;
    }

    private int getHeightPixel() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }

    public void setOrAuthPassword(String pwd, boolean isHavePassword){
        isStartRequest = true;
        this.pwdTmp = pwd;
        this.isHavePassword = isHavePassword;
        changeText();
        if(isHavePassword){
            PasswordAuthenticationThread passwordAuthenticationThread = new PasswordAuthenticationThread(this);
            passwordAuthenticationThread.start(pwdTmp);
        }else{
            SetPasswordThread setPasswordThread = new SetPasswordThread(this);
            setPasswordThread.start(pwdTmp);
        }
    }

    private void changeText(){
        if(isHavePassword){
            tv_title.setText(getString(R.string.auth));
            tv_desc.setText(getString(R.string.auth_desc));
        }else {
            tv_title.setText(getString(R.string.set_your_password));
            tv_desc.setText(getString(R.string.set_your_password_now));
        }
    }

    @Override
    public void onSetPasswordSuccess() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                getNFCCard();
            }
        });
    }

    @Override
    public void onSetPasswordFailed(boolean isNetworkException) {
        if(listener != null){
            listener.onAddNFCCardFragment3SetPasswordFailed(isNetworkException);
        }
    }

    @Override
    public void onPasswordAuthenticationSuccess() {
        onSetPasswordSuccess();
    }

    @Override
    public void onPasswordAuthenticationFailed(final boolean isNetworkException) {
        isStartRequest = false;
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(listener != null)
                    listener.onAddNFCCardFragment3AuthFailed(isNetworkException);
            }
        });
    }

    private void getNFCCard(){
        tv_title.setText(getString(R.string.get_nfc_card_now));
        tv_desc.setText(getString(R.string.get_nfc_card_now_desc));
    }
}
