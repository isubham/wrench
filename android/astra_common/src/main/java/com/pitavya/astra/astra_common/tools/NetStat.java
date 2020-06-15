package com.pitavya.astra.astra_common.tools;

import android.content.Context;
import android.net.ConnectivityManager;
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

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static void setMobileDataState(Context context, boolean mobileDataEnabled) {
        try {
            TelephonyManager telephonyService = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            Method setMobileDataEnabledMethod = Objects.requireNonNull(telephonyService).getClass().getDeclaredMethod("setDataEnabled", boolean.class);
            setMobileDataEnabledMethod.invoke(telephonyService, mobileDataEnabled);
        } catch (Exception ex) {
            Log.e(TAG + ":" + Thread.currentThread().getStackTrace()[2].getLineNumber(), "Error setting mobile data state", ex);
            Errors.createErrorLog(ex, TAG , context, true , Thread.currentThread().getStackTrace()[2]);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static boolean getMobileDataState(Context context) {
        try {
            TelephonyManager telephonyService = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            Method getMobileDataEnabledMethod = Objects.requireNonNull(telephonyService).getClass().getDeclaredMethod("getDataEnabled");
            return (boolean) (Boolean) getMobileDataEnabledMethod.invoke(telephonyService);
        } catch (Exception ex) {
            Log.e(TAG, "Error getting mobile data state", ex);
            Errors.createErrorLog(ex, TAG, context, true, Thread.currentThread().getStackTrace()[2]);

        }
        return false;
    }

}
