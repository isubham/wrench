package com.pitavya.astra.astra_common.tools;

import android.os.Environment;
import android.util.Log;

import java.io.File;

public class AppDirectories {

    private static final String CLASS_TAG = "AppDirectories";

    /**
     * @return On success full check , it will return the appLog Directory
     * @taskProcess checks for main folder creation and then inner App Log folder creation . if it fails in between ,return null Or else returns the file for appLog Directory.
     **/
    public static File checkForAppLogDirectoryExistence() {
        // Create the storage directory if it does not exist
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory(), Constants.APP_DIRECTORY);
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.e(Constants.APP_DIRECTORY, "Oops! Failed to create "
                        + Constants.APP_DIRECTORY + " directory");
                return null;
            }
        }
        File appLogDirectory = new File(Environment.getExternalStorageDirectory() + "/" + Constants.APP_DIRECTORY, Constants.APP_LOGS);
        if (!appLogDirectory.exists()) {
            if (!appLogDirectory.mkdirs()) {
                Log.e(Constants.APP_LOGS, "Oops! Failed to create " + Constants.APP_LOGS + " directory");
                return null;
            }
        }

        return appLogDirectory;

    }

    public static File checkForAppImagesDirectoryExistence() {

        File mediaStorageDir = new File(Environment.getExternalStorageDirectory(), Constants.APP_DIRECTORY);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.e(Constants.APP_DIRECTORY, "Oops! Failed to create "
                        + Constants.APP_DIRECTORY + " directory");
                return null;
            }
        }
        //App Folder
        // Log.e("mediaStorageDir", mediaStorageDir.getAbsolutePath());


        File subFolderForImages = new File(Environment.getExternalStorageDirectory() + "/" + Constants.APP_DIRECTORY, Constants.APP_IMAGES);

        if (!subFolderForImages.exists()) {
            if (!subFolderForImages.mkdirs()) {
                Log.e(Constants.APP_DIRECTORY, "Oops! Failed to create "
                        + Constants.APP_DIRECTORY + "/" + Constants.APP_IMAGES + " directory");
                return null;
            }
        }

        //AppImage Path
        //  Log.e("subFolderForImages", subFolderForImages.toString());

        return subFolderForImages;
    }

    public static boolean checkForAppLogFileExistence(String TAG, File logFile) {
        try {
            if (!logFile.exists()) {

                if (!logFile.createNewFile()) {
                    Log.e(TAG, "Oops! Failed to create " + Constants.APP_LOG_FILE + " file");
                    return false;
                }

            }
        } catch (Exception e) {
            Log.e("Error", "Exception Occured while creating Bug file . Contact Team");
        }
        return true;
    }

    /**
     * @return On success full check , it will return the base Directory
     **/
    public static File checkForAppDirectoryExistence() {
        // Create the storage directory if it does not exist
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory(), Constants.APP_DIRECTORY);
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.e(Constants.APP_DIRECTORY, "Oops! Failed to create "
                        + Constants.APP_DIRECTORY + " directory");
                return null;
            }
        }
        return mediaStorageDir;
    }

}
