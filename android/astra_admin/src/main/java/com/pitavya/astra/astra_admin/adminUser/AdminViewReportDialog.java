package com.pitavya.astra.astra_admin.adminUser;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.pitavya.astra.astra_admin.R;
import com.pitavya.astra.astra_admin.databinding.AdminViewReportDialogBinding;
import com.pitavya.astra.astra_common.tools.CustomDatePickerFragment;
import com.pitavya.astra.astra_common.tools.DateUtils;
import com.pitavya.astra.astra_common.tools.Endpoints;
import com.pitavya.astra.astra_common.tools.Errors;
import com.pitavya.astra.astra_common.tools.LoginPersistance;
import com.pitavya.astra.astra_common.tools.NetStat;
import com.pitavya.astra.astra_common.tools.PermissionActivity;

import java.util.ArrayList;
import java.util.List;


public class AdminViewReportDialog extends AppCompatActivity implements CustomDatePickerFragment.TheListener {

    BroadcastReceiver onComplete = new BroadcastReceiver() {
        public void onReceive(Context ctxt, Intent intent) {
            // TODO action on complete
            Toast.makeText(ctxt, "Report Downloaded", Toast.LENGTH_SHORT).show();
        }
    };
    BroadcastReceiver onNotificationClick = new BroadcastReceiver() {
        public void onReceive(Context ctxt, Intent intent) {
            // TODO action on notification click
        }
    };
    //Dates
    private String currentDate, firstDateOfCurrentMonth, firstDateOfLast3rdMonth, firstDateOfLast6thMonth;
    private AdminViewReportDialogBinding binding;
    private boolean settingFromDate = true;
    private ArrayAdapter<String> adapter;
    private int spinnerPosSelected = 0;
    private String TAG = "AdminViewReport";
    private DownloadManager downloadManager;
    private long lastDownload = -1L;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = AdminViewReportDialogBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupSpinner();
//        isWriteStoragePermissionGranted();

        if (!PermissionActivity.checkStoragePermissions(this))
            PermissionActivity.requestStoragePermission(this);

        if (!PermissionActivity.checkReadPhoneStatePermissions(this))
            PermissionActivity.readPhoneStatePermission(this);

        registerDownloadActions();
    }

    private void setupSpinner() {
        List<String> durationList = formDurationList();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, durationList);
        binding.durationSpinner.setAdapter(adapter);

        binding.durationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                binding.errorHint.setVisibility(View.INVISIBLE);
                spinnerPosSelected = i;
                if (i == 4) {
                    binding.selectDateRangeLayout.setVisibility(View.VISIBLE);
                } else {
                    binding.selectDateRangeLayout.setVisibility(View.GONE);
                    binding.reportStartDate.setText(null);
                    binding.reportEndDate.setText(null);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private List<String> formDurationList() {
        List<String> durationList = new ArrayList<>();

        currentDate = DateUtils.getDate();
        firstDateOfCurrentMonth = DateUtils.getFirstDateOfMonth(0);
        firstDateOfLast3rdMonth = DateUtils.getFirstDateOfMonth(-2);
        firstDateOfLast6thMonth = DateUtils.getFirstDateOfMonth(-5);


        durationList.add("Today  " + "(" + currentDate + ")");
        durationList.add("Current Month  " + "(" + firstDateOfCurrentMonth + " - " + currentDate + ")");
        durationList.add("Last 3 Month  " + "(" + firstDateOfLast3rdMonth + " - " + currentDate + ")");
        durationList.add("Last 6 Month  " + "(" + firstDateOfLast6thMonth + " - " + currentDate + ")");
        durationList.add("Select a date Range  ");
        return durationList;
    }

    @Override
    public void returnDate(String date) {
        if (settingFromDate)
            binding.reportStartDate.setText(date);
        else
            binding.reportEndDate.setText(date);
    }

    public void selectToDate(View view) {
        binding.errorHint.setVisibility(View.INVISIBLE);
        settingFromDate = false;
        DialogFragment fragment = new CustomDatePickerFragment();
        fragment.show(getSupportFragmentManager(), "date picker");
    }

    public void selectFromDate(View view) {
        binding.errorHint.setVisibility(View.INVISIBLE);
        settingFromDate = true;
        DialogFragment fragment = new CustomDatePickerFragment();
        fragment.show(getSupportFragmentManager(), "date picker");
    }

    /**
     * currentDate, firstDateOfCurrentMonth, firstDateOfLast3rdMonth, firstDateOfLast6thMonth;
     **/
    private List<String> getDatesForSelectedDuration(int spinnerPosSelected) {

        List<String> dates = new ArrayList<>();

        switch (spinnerPosSelected) {

            case 0:
                dates.add(currentDate);
                dates.add(currentDate);
                break;
            case 1:
                dates.add(firstDateOfCurrentMonth);
                dates.add(currentDate);
                break;
            case 2:
                dates.add(firstDateOfLast3rdMonth);
                dates.add(currentDate);
                break;
            case 3:
                dates.add(firstDateOfLast6thMonth);
                dates.add(currentDate);
                break;
            case 4:
                dates.add(binding.reportStartDate.getText().toString().trim());
                dates.add(binding.reportEndDate.getText().toString().trim());
                break;

        }

        return dates;
    }
//
//    public boolean isWriteStoragePermissionGranted() {
//        if (Build.VERSION.SDK_INT >= 23) {
//            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                    == PackageManager.PERMISSION_GRANTED) {
//                Log.v(TAG, "Permission is granted2");
//                return true;
//            } else {
//
//                Log.v(TAG, "Permission is revoked2");
//                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
//                return false;
//            }
//        } else { //permission is automatically granted on sdk<23 upon installation
//            Log.v(TAG, "Permission is granted2");
//            return true;
//        }
//    }

    public void downloadReport(View view) {

        if (!PermissionActivity.checkStoragePermissions(this)) {
            PermissionActivity.requestStoragePermission(this);
            return;
        }


        try {
            if (spinnerPosSelected == 4) {
                if (TextUtils.isEmpty(binding.reportStartDate.getText()) || TextUtils.isEmpty(binding.reportEndDate.getText())) {
                    binding.errorHint.setText(R.string.PLEASE_SELECT_THE_DATES);
                    binding.errorHint.setVisibility(View.VISIBLE);
                    return;
                }

                if (!DateUtils.isEndDateGreaterThanFromDate(binding.reportStartDate.getText().toString(), binding.reportEndDate.getText().toString())) {
                    binding.errorHint.setText(R.string.ERROR_INVALID_DATE_RANGE);
                    binding.errorHint.setVisibility(View.VISIBLE);
                    return;
                }
            }

            List<String> dates = getDatesForSelectedDuration(spinnerPosSelected);
            startDownload(dates.get(0), dates.get(1));
        } catch (Exception e) {
            Errors.createErrorLog(e, TAG, AdminViewReportDialog.this, true, Thread.currentThread().getStackTrace()[2]);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        unregisterReceiver(onComplete);
        unregisterReceiver(onNotificationClick);
    }

    private void registerDownloadActions() {

        downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        registerReceiver(onComplete,
                new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
        registerReceiver(onNotificationClick,
                new IntentFilter(DownloadManager.ACTION_NOTIFICATION_CLICKED));
    }

    private void startDownload(String startDate, String endDate) {
        // checking Network Stat
        if (!NetStat.isNetworkConnected(this)) {
            NetStat.connectToANetwork(AdminViewReportDialog.this,binding.rootLayout);
            return;
        }

        // INFO : If Data is on then we can do the processing
        String url = String.format(Endpoints.DOWNLOAD_REPORT, startDate, endDate);
        Uri uri = Uri.parse(url);

        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.addRequestHeader("Authorization", "Basic " + LoginPersistance.GetToken(AdminViewReportDialog.this));
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI |
                DownloadManager.Request.NETWORK_MOBILE);
        request.setTitle(getString(R.string.download_title));
        request.setDescription("Downloading " + startDate + "_" + endDate + ".xlsx");

        request.allowScanningByMediaScanner();
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,
                getString(R.string.download_template)
                        .concat(startDate)
                        .concat("_")
                        .concat(endDate)
                        .concat(".xlsx"));
        request.setMimeType(getString(R.string.excel_mime_type));
        assert downloadManager != null;
        lastDownload = downloadManager.enqueue(request);
        Toast.makeText(this, "File downloading. Check Notification area", Toast.LENGTH_SHORT).show();
    }


}
