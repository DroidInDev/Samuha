package com.stgobain.samuha.network;

import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;

/**
 * Created by vignesh on 22-06-2017.
 */

public class NetworkServiceResultReceiver  extends ResultReceiver {
    private Receiver mReceiver;

    public interface Receiver {
        void onReceiveResult(int i, Bundle bundle);
    }

    public NetworkServiceResultReceiver(Handler handler) {
        super(handler);
    }

    public void setReceiver(Receiver receiver) {
        this.mReceiver = receiver;
    }

    protected void onReceiveResult(int resultCode, Bundle resultData) {
        if (this.mReceiver != null) {
            this.mReceiver.onReceiveResult(resultCode, resultData);
        }
    }
}