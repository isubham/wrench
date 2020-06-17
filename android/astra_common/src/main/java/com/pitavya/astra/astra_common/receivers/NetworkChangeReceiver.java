package com.pitavya.astra.astra_common.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Vibrator;
import android.util.Log;
import android.widget.Toast;

import com.pitavya.astra.astra_common.tools.NetStat;

public class NetworkChangeReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        Log.e("intent", intent.getAction());
        Log.e("intent", "" + intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false));

        //intent used here are  :
        // intent: android.intent.action.AIRPLANE_MODE or Intent.ACTION_AIRPLANE_MODE_CHANGED
        //intent: android.net.conn.CONNECTIVITY_CHANGE or ConnectivityManager.CONNECTIVITY_ACTION

        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(100);
        if (NetStat.isNetworkConnected(context)) {
            Toast.makeText(context, "Connected", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Disconnected", Toast.LENGTH_SHORT).show();
        }

    }
}
