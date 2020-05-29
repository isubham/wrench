package io.github.isubham.astra.tools;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.VolleyError;

public class Errors {

    public static void handleVolleyError(VolleyError error, String TAG, Context context) {

        Log.e(TAG, error.toString());

        if (error.toString().contains(Constants.WEAK_INTERNET))
            Toast.makeText(context, "Weak Internet Connection", android.widget.Toast.LENGTH_SHORT).show();
        if (error.toString().contains(Constants.NO_INTERNET))
            Toast.makeText(context, "No Internet Connection", Toast.LENGTH_SHORT).show();
        if (error.toString().contains(Constants.SERVER_ERROR))
            Toast.makeText(context, "Internal Server Error", Toast.LENGTH_SHORT).show();


    }

}
