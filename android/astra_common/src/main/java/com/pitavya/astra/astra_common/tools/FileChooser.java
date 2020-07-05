package com.pitavya.astra.astra_common.tools;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import androidx.core.content.FileProvider;

import java.io.File;
import java.util.Arrays;

public class FileChooser {
    static File[] filesInDir;

    public static void chooseAndShareBugFile(Context context, String TAG) {
        File appLogDirectory = AppDirectories.checkForAppLogDirectoryExistence();
        //for testing
        if (appLogDirectory != null) {
            filesInDir = appLogDirectory.listFiles();
        }
        Log.e("fileList", Arrays.toString(filesInDir));
        //
        String logFileName = Constants.APP_LOG_FILE + Constants.TXT_EXTENSION;
        File logFile = new File(appLogDirectory, logFileName);
        if (!AppDirectories.checkForAppLogFileExistence(TAG, logFile))
            return;
        Uri fileUri = FileProvider.getUriForFile(context, context.getPackageName() + ".provider", logFile);
       // created the fileUri to be shared and share it using below static method
        ContactUs.reportBug(context, fileUri);
    }

}
