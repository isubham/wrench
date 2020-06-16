package com.pitavya.astra.astra_common.tools;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.lang.reflect.Method;
import java.util.Objects;

public class NetStat {
    private static final String TAG = "NetStat";

    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

    @SuppressLint({"MissingPermission", "HardwareIds"})
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static void setMobileDataState(Context context, boolean mobileDataEnabled) {
        try {
            TelephonyManager telephonyService = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            Method setMobileDataEnabledMethod = Objects.requireNonNull(telephonyService).getClass().getDeclaredMethod("setDataEnabled", boolean.class);

            Log.e("DEvice Id", "" + telephonyService.getDeviceSoftwareVersion() + ":" + telephonyService.getDeviceId());

            setMobileDataEnabledMethod.invoke(telephonyService, mobileDataEnabled);
        } catch (Exception ex) {
            Log.e(TAG + ":" + Thread.currentThread().getStackTrace()[2].getLineNumber(), "Error setting mobile data state", ex);
            Errors.createErrorLog(ex, TAG, context, true, Thread.currentThread().getStackTrace()[2]);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static boolean getMobileDataState(Context context) {
        try {
            TelephonyManager telephonyService = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            Method getMobileDataEnabledMethod = Objects.requireNonNull(telephonyService).getClass().getDeclaredMethod("getDataEnabled");

            return (boolean) (Boolean) getMobileDataEnabledMethod.invoke(telephonyService);
        } catch (Exception ex) {
            Log.e(TAG, "Error getting mobile data state", ex.getCause());
            Errors.createErrorLog(ex, TAG, context, true, Thread.currentThread().getStackTrace()[2]);

        }
        return false;
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private static void networksConnected(Context context) {

        ConnectivityManager connMgr =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        boolean isWifiConn = false;
        boolean isMobileConn = false;
        for (Network network : connMgr.getAllNetworks()) {
            NetworkInfo networkInfo = connMgr.getNetworkInfo(network);
            if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                isWifiConn |= networkInfo.isConnected();
            }
            if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                isMobileConn |= networkInfo.isConnected();
            }
        }
        Log.d(TAG, "Wifi connected: " + isWifiConn);
        Log.d(TAG, "Mobile connected: " + isMobileConn);

    }

    public static boolean switchWifiOn(Context context) {

        WifiManager wifi = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        if (!wifi.isWifiEnabled()) {
            wifi.setWifiEnabled(true);
            return true;
        }
        return false;
    }


}
