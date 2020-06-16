package com.pitavya.astra.astra_common.recievers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.pitavya.astra.astra_common.tools.NetStat;

public class NetworkChangeReciever extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent == null || intent.getAction() == null)
            return;

        Log.e("ACTION", intent.getAction());

        switch (intent.getAction()) {
            case Intent.ACTION_POWER_CONNECTED:
                Toast.makeText(context, "POER CONNECTED", Toast.LENGTH_SHORT).show();
                break;
            case Intent.ACTION_POWER_DISCONNECTED:
                Toast.makeText(context, "POER DICONNECted CONNECTED", Toast.LENGTH_SHORT).show();
                break;
        }

        if (NetStat.isNetworkConnected(context)) {
            Toast.makeText(context, "Connected", Toast.LENGTH_SHORT).show();
        }

    }
}
