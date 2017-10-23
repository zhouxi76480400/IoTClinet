package me.zhouxi.iot.nfc;

import android.nfc.cardemulation.HostApduService;
import android.os.Bundle;

/**
 * Created by zhouxi on 23/10/2017.
 */

public class MyHostApduService extends HostApduService {

    @Override
    public byte[] processCommandApdu(byte[] commandApdu, Bundle extras) {
        return new byte[0];
    }

    @Override
    public void onDeactivated(int reason) {

    }
}
