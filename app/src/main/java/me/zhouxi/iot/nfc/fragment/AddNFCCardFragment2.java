package me.zhouxi.iot.nfc.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import me.zhouxi.iot.R;

/**
 * Created by zhouxi on 24/10/2017.
 */

public class AddNFCCardFragment2 extends Fragment implements TextWatcher, View.OnClickListener {

    public interface AddNFCCardFragment2Listener{

        void onAddNFCCardFragment2PressNext(String pwd);

    }

    private AddNFCCardFragment2Listener listener;

    public void setAddNFCCardFragment2Listener(AddNFCCardFragment2Listener listener){
        this.listener = listener;
    }

    private boolean isPasswordExist;

    public void setPasswordExist(boolean isPasswordExist){
        Log.e("test","setPasswordExist");
        this.isPasswordExist = isPasswordExist;
        if(this.isVisible()){
            setContent();
        }
    }

    private LinearLayout ll_top;

    private TextView tv_title;

    private TextView tv_desc;

    private TextInputLayout textInputLayout;

    public void setText(String str){
        textInputLayout.getEditText().setText(str);
    }

    private Button btn_next;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.view_add_nfc_card_fragment2, container, false);
        ll_top = (LinearLayout) view.findViewById(R.id.ll_top);
        int all_height = getHeightPixel();
        ll_top.getLayoutParams().height = all_height * 45 / 100;
        tv_title = (TextView) view.findViewById(R.id.tv_title);
        tv_desc = (TextView) view.findViewById(R.id.tv_desc);
        textInputLayout = (TextInputLayout) view.findViewById(R.id.textInputLayout);
        textInputLayout.getEditText().addTextChangedListener(this);
        btn_next = (Button) view.findViewById(R.id.btn_next);
        btn_next.setOnClickListener(this);
        setContent();
        return view;
    }

    private int getHeightPixel() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }

    private void setContent(){
        if(isPasswordExist){
            tv_title.setText(getString(R.string.press_your_password));
            tv_desc.setText(getString(R.string.press_password_to_add_nfc_card));
        }else{
            tv_title.setText(getString(R.string.set_your_password));
            tv_desc.setText(getString(R.string.press_password_to_add_nfc_card));
        }
        textInputLayout.getEditText().setText("");
        enableButton(false);
    }


    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    }

    @Override
    public void afterTextChanged(Editable editable) {
        int length = editable.toString().length();
        if(length < 6){
            enableButton(false);
        }else{
            enableButton(true);
        }
    }

    private void enableButton(boolean isEnable){
        if(isEnable){
            btn_next.setTextColor(ContextCompat.getColor(getContext(),R.color.colorPrimaryDark));
            btn_next.setEnabled(true);
        }else{
            btn_next.setTextColor(Color.parseColor("#d6d8d7"));
            btn_next.setEnabled(false);
        }
    }

    @Override
    public void onClick(View view) {
        if(listener != null)
            listener.onAddNFCCardFragment2PressNext(textInputLayout.getEditText().getText().toString());
    }

}
