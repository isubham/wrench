package com.pitavya.astra.astra_common.tools;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.pitavya.astra.astra_common.model.CreateErrorLog;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class Errors {

    private static final String TAG = "Errors";

    public static void handleVolleyError(VolleyError error, String TAG, Context context, boolean doLog) {

        Log.e(TAG, error.toString());

        if (error.toString().contains(Constants.WEAK_INTERNET))
            Toast.makeText(context, Constants.WEAK_INTERNET_CONNECTION, Toast.LENGTH_SHORT).show();
        if (error.toString().contains(Constants.NO_INTERNET))
            Toast.makeText(context, Constants.NO_INTERNET_CONNECTION, Toast.LENGTH_SHORT).show();
        if (error.toString().contains(Constants.SERVER_ERROR))
            Toast.makeText(context, Constants.INTERNAL_SERVER_ERROR, Toast.LENGTH_SHORT).show();

//        if (doLog)
//            logTheErrorInLocalStorageErrorLogFile(error, TAG, context);

    }

    public static void createErrorLog(Exception error, String TAG, Context context, boolean doLog, StackTraceElement stackTraceElement) {
        Toast.makeText(context, "Error Occurred . Please Contact Pitavya Team .", Toast.LENGTH_SHORT).show();

        Log.e(TAG, error.toString());
        if (doLog)
            logTheErrorInLocalStorageErrorLogFile(error, TAG, context, stackTraceElement);

    }


    private static void logTheErrorInLocalStorageErrorLogFile(Exception error, String error_activity_tag, Context context, StackTraceElement stackTraceElement) {

        try {
            File appLogDirectory = AppDirectories.checkForAppLogDirectoryExistence();
            if (appLogDirectory == null) return;

            String logFileName = Constants.APP_LOG_FILE + Constants.TXT_EXTENSION;
            File logFile = new File(appLogDirectory, logFileName);

            // read the file //StringBuffer fileContent = readTextFromFile(logFile);

            writeTextInLogFile(context, logFile, error, error_activity_tag, stackTraceElement);

        } catch (Exception e) {
            Log.e(TAG, "Not able to create directory :" + e.toString());
            Toast.makeText(context, "Not able to create directory :" + e.toString(), Toast.LENGTH_SHORT).show();
        }

    }

    //appends the error in a file
    private static void writeTextInLogFile(Context context, File logFile, Exception error, String error_activity_tag, StackTraceElement stackTraceElement) {
        FileOutputStream fileOutputStream = null;
        try {

            if (!AppDirectories.checkForAppLogFileExistence(TAG, logFile))
                return;

            fileOutputStream = new FileOutputStream(logFile, true);
            fileOutputStream.write(("\n" + formErrorObject(error, error_activity_tag, stackTraceElement)).getBytes());

        } catch (IOException e) {
            Log.e(TAG, e.toString());
            Toast.makeText(context, "Error while writing " + e.toString(), Toast.LENGTH_SHORT).show();
        } finally {
            try {
                if (fileOutputStream != null)
                    fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

    private static String formErrorObject(Exception error, String error_activity_tag, StackTraceElement stackTraceElement) {
        return new CreateErrorLog(error_activity_tag, error.toString(), DateUtils.getCurrentTimestamp().toString(), ErrorStackTrace.toString(stackTraceElement)).toString();
    }

    private static StringBuffer readTextFromFile(File logFile) {
        StringBuffer fileContent = new StringBuffer();

        int c;
        FileInputStream inputStream = null;
        try {
            if (logFile.exists()) {
                inputStream = new FileInputStream(logFile);
                while ((c = inputStream.read()) != -1) {
                    fileContent = fileContent.append((char) c);
                }
            }
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        } finally {
            try {
                if (inputStream != null) {
                    Log.e("print Input Strweam", inputStream.toString());
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return fileContent;
    }
}
