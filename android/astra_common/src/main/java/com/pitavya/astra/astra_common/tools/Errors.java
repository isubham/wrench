package com.pitavya.astra.astra_common.tools;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.VolleyError;

public class Errors {

    public static void handleVolleyError(VolleyError error, String TAG, Context context) {

        Log.e(TAG, error.toString());

        if (error.toString().contains(Constants.WEAK_INTERNET))
            Toast.makeText(context, Constants.WEAK_INTERNET_CONNECTION, Toast.LENGTH_SHORT).show();
        if (error.toString().contains(Constants.NO_INTERNET))
            Toast.makeText(context, Constants.NO_INTERNET_CONNECTION, Toast.LENGTH_SHORT).show();
        if (error.toString().contains(Constants.SERVER_ERROR))
            Toast.makeText(context, Constants.INTERNAL_SERVER_ERROR, Toast.LENGTH_SHORT).show();


    }

}
