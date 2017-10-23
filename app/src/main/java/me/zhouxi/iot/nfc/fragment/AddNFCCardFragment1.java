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

import me.zhouxi.iot.R;

/**
 * Created by zhouxi on 23/10/2017.
 */

public class AddNFCCardFragment1 extends Fragment implements View.OnClickListener{

    public interface AddNFCCardFragment1Listener{

        void onNextPress();

    }

    private AddNFCCardFragment1Listener listener;

    public void setAddNFCCardFragment1Listener(AddNFCCardFragment1Listener listener){
        this.listener = listener;
    }

    private LinearLayout ll_top;

    private Button btn_next;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.view_add_nfc_card_fragment,container,false);
        ll_top = (LinearLayout) view.findViewById(R.id.ll_top);
        int all_height = getHeightPixel();
        ll_top.getLayoutParams().height = all_height * 45 / 100;
        btn_next = (Button) view.findViewById(R.id.btn_next);
        btn_next.setOnClickListener(this);
        return view;
    }

    private int getHeightPixel(){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_next:
                if(listener != null)
                    listener.onNextPress();
                break;
        }
    }
}
