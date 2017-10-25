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
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import me.zhouxi.iot.IoTApplication;
import me.zhouxi.iot.R;
import me.zhouxi.iot.client.nfc.GetNFCCardThread;
import me.zhouxi.iot.client.nfc.PasswordAuthenticationThread;
import me.zhouxi.iot.client.nfc.SetPasswordThread;
import me.zhouxi.iot.client.nfc.sql.NFCKeyDBAPI;

/**
 * Created by zhouxi on 24/10/2017.
 */

public class AddNFCCardFragment3 extends Fragment implements SetPasswordThread.SetPasswordThreadListener,
        PasswordAuthenticationThread.PasswordAuthenticationThreadListener , GetNFCCardThread.GetNFCCardThreadListener{

    public interface AddNFCCardFragment3Listener{

        void onAddNFCCardFragment3SetPasswordFailed(boolean networkException);

        void onAddNFCCardFragment3AuthFailed(boolean networkException);

        void finishToAddCard();

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

    private ProgressBar progressBar;


    private String pwdTmp;
    private boolean isHavePassword;

    private boolean isSuccessful;

    public boolean isSuccessful(){
        return isSuccessful;
    }

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
        btn_finish.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(listener != null)
                    listener.finishToAddCard();
            }
        });
        tv_desc = (TextView) view.findViewById(R.id.tv_desc);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
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
        GetNFCCardThread getNFCCardThread = new GetNFCCardThread(this);
        getNFCCardThread.start(pwdTmp, System.currentTimeMillis());
    }


    @Override
    public void onGetNFCCardThreadSuccess(String string) {
        if(string.equalsIgnoreCase("false")){
            onGetNFCCardThreadFailed(false);
        }else{
            try {
                JSONObject jsonObject = new JSONObject(string);
                String time = String.valueOf(jsonObject.getLong("time"));
                String key = jsonObject.getString("key");
                NFCKeyDBAPI nfcKeyDBAPI = NFCKeyDBAPI.getInstance();
                nfcKeyDBAPI.insertAData(time,key);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        successToGetKey();
                    }
                });
            } catch (JSONException e) {
                onGetNFCCardThreadFailed(false);
            }
        }
    }

    @Override
    public void onGetNFCCardThreadFailed(boolean networkException) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(listener != null)
                    listener.onAddNFCCardFragment3AuthFailed(false);
            }
        });
    }

    private void successToGetKey(){
        tv_title.setText(getString(R.string.get_nfc_card_ok));
        tv_desc.setText(getString(R.string.get_nfc_card_ok_desc));
        btn_finish.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
        isStartRequest = false;
        isSuccessful = true;
    }

}
