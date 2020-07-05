package com.pitavya.astra.astra_admin.adminUser;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.play.core.install.model.ActivityResult;
import com.google.gson.Gson;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.pitavya.astra.astra_admin.AdminAppUpdate;
import com.pitavya.astra.astra_admin.R;
import com.pitavya.astra.astra_admin.databinding.AdminHomeScreenBinding;
import com.pitavya.astra.astra_common.CreateGeneralUser;
import com.pitavya.astra.astra_common.model.GeneralUser;
import com.pitavya.astra.astra_common.model.LicenseValidate;
import com.pitavya.astra.astra_common.tools.ApplicationController;
import com.pitavya.astra.astra_common.tools.Constants;
import com.pitavya.astra.astra_common.tools.CustomSnackbar;
import com.pitavya.astra.astra_common.tools.Endpoints;
import com.pitavya.astra.astra_common.tools.Errors;
import com.pitavya.astra.astra_common.tools.FileChooser;
import com.pitavya.astra.astra_common.tools.LoginPersistance;
import com.pitavya.astra.astra_common.tools.PermissionActivity;
import com.pitavya.astra.astra_common.tools.ScreenshotPreventor;
import com.pitavya.astra.astra_common.tools.SendMail;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class AdminHomeScreen extends AppCompatActivity {

    boolean isLicenseNotExpired = true;
    //BundleData
    private String TAG = "AdminHomeScreen";
    private AdminHomeScreenBinding binding;
    private ProgressBar progressBar;
    private boolean backPressedToExitOnce = false;
    //Activity use
    private Gson gson;
    private int requestUpdateCode = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ScreenshotPreventor.preventScreenshot(AdminHomeScreen.this);

        binding = AdminHomeScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        findViewByIds();
        toolbarSetup();
        //showProgressBar();
        setBundleData();
        hideProgressBar();

        if (!PermissionActivity.checkStoragePermissions(this))
            PermissionActivity.requestStoragePermission(this);

        checkForAppLatestTheUpdate();

        makeRequestToServerForLicenseValidity();
    }

    @Override
    protected void onResume() {
        super.onResume();
        new AdminAppUpdate(AdminHomeScreen.this, AdminHomeScreen.this).checkForUpdateProgress(requestUpdateCode);

        if (!isLicenseNotExpired) {
            new AlertDialog.Builder(this)
                    .setTitle(getString(R.string.app_name))
                    .setCancelable(false)
                    .setMessage("Your license is expired. Please renew it.")
                    .setPositiveButton("REQUEST RENEWAL", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            sendMailToRenewLicense();
                        }
                    }).setNegativeButton("EXIT", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    System.exit(0);
                }
            }).show();
        }

    }

    private void makeRequestToServerForLicenseValidity() {
        showProgressBar();
        final LicenseValidate[] licenseValidate = {null};
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, Endpoints.LICENSE_VALIDITY_CHECK, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                hideProgressBar();
                Log.e(TAG, "response for license check" + response);

                try {
                    if (!String.valueOf(response).equals(Constants.EMPTY_JSON)) {
                        actionOnLicenseResponse(String.valueOf(response));
                    }

                } catch (Exception e) {
                    Errors.createErrorLog(e, TAG, AdminHomeScreen.this, true, Thread.currentThread().getStackTrace()[2]);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideProgressBar();
                Errors.handleVolleyError(error, TAG, AdminHomeScreen.this, true);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Basic " + LoginPersistance.GetToken(AdminHomeScreen.this));
                return headers;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));

        ApplicationController.getInstance().addToRequestQueue(request);

    }

    private LicenseValidate parseLicenseCheckResponse(String licenseValidationRepose) {
        return new Gson().fromJson(licenseValidationRepose, LicenseValidate.class);
    }

    private void actionOnLicenseResponse(String licenseValidationRepose) {

        LicenseValidate licenseValidation = parseLicenseCheckResponse(licenseValidationRepose);
        int noOfDaysLeft = 0;

        if (licenseValidation != null) {
            noOfDaysLeft = licenseValidation.getDay_left();
            isLicenseNotExpired = licenseValidation.isValid();

        }
        if (noOfDaysLeft == 0) {
            new AlertDialog.Builder(this)
                    .setTitle(getString(R.string.app_name))
                    .setCancelable(false)
                    .setMessage("Your license is expired. Please renew it.")
                    .setPositiveButton("REQUEST RENEWAL", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            sendMailToRenewLicense();
                        }
                    }).setNegativeButton("EXIT", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    System.exit(0);
                }
            }).show();
        } else if (noOfDaysLeft > 0 && noOfDaysLeft < 10) {
            new AlertDialog.Builder(this)
                    .setTitle(getString(R.string.app_name))
                    .setCancelable(false)
                    .setMessage("Your license will be expired in " + noOfDaysLeft + " days . Please renew it.")
                    .setPositiveButton("REQUEST RENEWAL", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            sendMailToRenewLicense();
                        }
                    }).setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                    customMessageSnackBar("Welcome Back Admin");
                }
            }).show();
        } else {
            customMessageSnackBar("Welcome Back Admin");
        }

    }


    private void sendMailToRenewLicense() {
        SendMail.toRenewLicense(AdminHomeScreen.this);
    }

    private void checkForAppLatestTheUpdate() {
        new AdminAppUpdate(AdminHomeScreen.this, AdminHomeScreen.this).checkForUpdate(requestUpdateCode);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.admin_home_screen_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.logout:
                sendStatusForLogout();
                Intent toSignInWithoutHistory = new Intent(this, AdminSignIn.class);
                startActivity(toSignInWithoutHistory);
                finishAffinity();
                return true;

            case R.id.downloadReport:
                startActivity(new Intent(this, AdminViewReportDialog.class));
                return true;

            case R.id.contactUs:
                SendMail.toContactUs(AdminHomeScreen.this);
                return true;

            case R.id.reportBug:
                // ContactUs.reportBug(AdminHomeScreen.this);
                FileChooser.chooseAndShareBugFile(AdminHomeScreen.this, TAG);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == requestUpdateCode) {
            Log.e("RESULT CODE", "" + resultCode);
            switch (resultCode) {
                case ActivityResult.RESULT_IN_APP_UPDATE_FAILED:
                    new CustomSnackbar(AdminHomeScreen.this, "Update Failed. Please try again", "", binding.layoutContainer) {
                        @Override
                        public void onActionClick(View view) {

                        }
                    }.show();
                    break;
                case RESULT_CANCELED:
                    new CustomSnackbar(AdminHomeScreen.this, "Update Cancelled. Please try again", "", binding.layoutContainer) {
                        @Override
                        public void onActionClick(View view) {

                        }
                    }.show();
                    break;
                case RESULT_OK:
                    new CustomSnackbar(AdminHomeScreen.this, "Successfully Updated", "", binding.layoutContainer) {
                        @Override
                        public void onActionClick(View view) {

                        }
                    }.show();
                    break;

            }
        }


        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            Log.e("REQUEST CODE", "" + requestCode);
            Log.e("RESULT CODE", "" + resultCode);

            if (result.getContents() != null) {
                sendScannedDetailsToCreateLog(result.getContents().trim());
            } else {
                new CustomSnackbar(this, getString(R.string.admin_home_screen_wrong_scan_text), getString(R.string.admin_home_screen_retry_action_text), binding.layoutContainer) {
                    @Override
                    public void onActionClick(View view) {
                        scanCode(view);
                    }
                }.showWithAction();
            }
        }


    }

    @Override
    public void onBackPressed() {
        if (backPressedToExitOnce) {
            super.onBackPressed();
        } else {
            this.backPressedToExitOnce = true;
            Toast.makeText(this, "Press again to exit", Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    backPressedToExitOnce = false;
                }
            }, 2000);
        }
    }


    private void toolbarSetup() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            toolbar.setTitleTextColor(getResources().getColor(R.color.colorPrimaryDark));
        }
    }

    private void setBundleData() {

    }

    private void findViewByIds() {
        progressBar = findViewById(R.id.progressBar);
    }


    //TODO - to send status flag for user Logout action
    private void sendStatusForLogout() {
        LoginPersistance.Delete(AdminHomeScreen.this);
    }

    public void scanCode(View view) {
        IntentIntegrator intentIntegrator = new IntentIntegrator(AdminHomeScreen.this);
        intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        intentIntegrator.setCameraId(0);
        intentIntegrator.setOrientationLocked(false);
        intentIntegrator.setPrompt(getString(R.string.scan_alert_msg));
        intentIntegrator.setBeepEnabled(true);
        intentIntegrator.setBarcodeImageEnabled(true);
        intentIntegrator.initiateScan();

    }

    public void searchForUser(View view) {
        if (!TextUtils.isEmpty(binding.adminHomeInputUsername.getText())) {
            hideKeyboard();
            getUserDetailsFromServerFor(String.valueOf(binding.adminHomeInputUsername.getText()));
        }
    }


    private void getUserDetailsFromServerFor(final String userName) {
        showProgressBar();

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, Endpoints.SEARCH_BY_USER_NAME + userName, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                hideProgressBar();

                try {

                    if (response.optString(Constants.MESSAGE).equals(Constants.TOKEN_INVALID)) {
                        customMessageSnackBar(Constants.TOKEN_EXPIRED);
                        return;
                    }

                    if (!String.valueOf(response).equals(Constants.EMPTY_JSON)) {
                        binding.adminHomeInputUsername.setText(null);
                        parseResponse(String.valueOf(response), userName);
                    } else {
                        customMessageSnackBar(Constants.INVALID_USERNAME);
                    }

                } catch (Exception e) {
                    Errors.createErrorLog(e, TAG, AdminHomeScreen.this, true, Thread.currentThread().getStackTrace()[2]);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideProgressBar();
                Errors.handleVolleyError(error, TAG, AdminHomeScreen.this, true);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Basic " + LoginPersistance.GetToken(AdminHomeScreen.this));
                return headers;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));

        ApplicationController.getInstance().addToRequestQueue(request);

    }

    private void parseResponse(String response, String userName) {

        GeneralUser generalUser = new Gson().fromJson(response, GeneralUser.class);
        //update SharedPref
        LoginPersistance.update(generalUser.getUsername(), generalUser.getToken(), generalUser.getProfile_pic(), generalUser.getId_front(),
                generalUser.getId_back(), AdminHomeScreen.this);

        sendToVerify(userName, generalUser.getName(), generalUser.getUser_id());
    }

    private void sendToVerify(String userName, String name, int userId) {
        startActivity(new Intent(AdminHomeScreen.this, AdminVerifyDoc.class)
                .putExtra(Constants.USER_NAME, userName)
                .putExtra(Constants.NAME, name)
                .putExtra(Constants.ID, userId)
                .putExtra(Constants.ACTION_IN_OUT, binding.adminHomeSwitch.isChecked() ? Constants.ACTION_OUT : Constants.ACTION_IN)

        );
    }

    private void customMessageSnackBar(String message) {
        new CustomSnackbar(this, message, null, binding.layoutContainer) {
            @Override
            public void onActionClick(View view) {
            }
        }.show();
    }


    private void sendScannedDetailsToCreateLog(String scannedDetails) {
        if (!TextUtils.isEmpty(scannedDetails))
            getUserDetailsFromServerFor(scannedDetails);
    }


    public void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        assert imm != null;
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
    }

    public void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

    }

    public void hideProgressBar() {
        progressBar.setVisibility(View.INVISIBLE);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    public void registerNewUser(View view) {
        startActivity(new Intent(this, CreateGeneralUser.class).putExtra(Constants.USER_TYPE, Constants.USER_TYPE_ADMIN)
        );
    }


}
