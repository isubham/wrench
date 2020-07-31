package com.pitavya.astra.android.astra_admin;

import android.app.Activity;
import android.content.Context;
import android.content.IntentSender;

import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.OnSuccessListener;
import com.pitavya.astra.astra_common.tools.Errors;

public class AdminAppUpdate {

    public static final String TAG = AdminAppUpdate.class.getName();

    private AppUpdateManager appUpdateManager;

    private Context context;
    private Activity activity;

    public AdminAppUpdate(Activity activity, Context context) {
        this.context = context;
        this.activity = activity;
    }

    public void checkForUpdate(final int requestUpdate) {

        appUpdateManager = AppUpdateManagerFactory.create(context);
        appUpdateManager.getAppUpdateInfo().addOnSuccessListener(new OnSuccessListener<AppUpdateInfo>() {
            @Override
            public void onSuccess(AppUpdateInfo result) {

                if (result.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                        // For a flexible update, use AppUpdateType.FLEXIBLE
                        && result.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                    // Request the update.
                    try {
                        appUpdateManager.startUpdateFlowForResult(result, AppUpdateType.IMMEDIATE, activity, requestUpdate);
                    } catch (IntentSender.SendIntentException e) {
                        Errors.createErrorLog(e, TAG, context, true, Thread.currentThread().getStackTrace()[2]);
                    }


                }
            }
        });

    }


    public void checkForUpdateProgress(final int requestUpdateCode) {
        appUpdateManager = AppUpdateManagerFactory.create(context);
        appUpdateManager.getAppUpdateInfo().addOnSuccessListener(new OnSuccessListener<AppUpdateInfo>() {
            @Override
            public void onSuccess(AppUpdateInfo result) {

                if (result.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS
                        // For a flexible update, use AppUpdateType.FLEXIBLE
                        && result.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                    // Request the update.
                    try {
                        appUpdateManager.startUpdateFlowForResult(result, AppUpdateType.IMMEDIATE, activity, requestUpdateCode);
                    } catch (IntentSender.SendIntentException e) {
                        Errors.createErrorLog(e, TAG, context, true, Thread.currentThread().getStackTrace()[2]);
                    }


                }
            }
        });

    }
}
