package me.zhouxi.iot.nfc.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import me.zhouxi.iot.R;

/**
 * Created by zhouxi on 24/10/2017.
 */

public class AddNFCCardFragment2 extends Fragment {

    private LinearLayout ll_top;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.view_add_nfc_card_fragment2, container, false);
        ll_top = (LinearLayout) view.findViewById(R.id.ll_top);
        int all_height = getHeightPixel();
        ll_top.getLayoutParams().height = all_height * 45 / 100;

        return view;
    }

    private int getHeightPixel() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }

}
