package com.pitavya.astra.astra_common.tools;

import android.app.Activity;
import android.content.Context;
import android.view.Window;
import android.view.WindowManager;

public class ScreenshotPreventor {

    public static void preventScreenshot(Activity activity){
        Window window = activity.getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
    }

}
