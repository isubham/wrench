package io.github.isubham.astra.adminUser;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import io.github.isubham.astra.R;
import io.github.isubham.astra.databinding.AdminHomeScreenBinding;
import io.github.isubham.astra.generalUser.CreateGeneralUser;
import io.github.isubham.astra.model.CreateLog;
import io.github.isubham.astra.tools.ApplicationController;
import io.github.isubham.astra.tools.Constants;
import io.github.isubham.astra.tools.CustomSnackbar;
import io.github.isubham.astra.tools.Endpoints;
import io.github.isubham.astra.tools.Errors;
import io.github.isubham.astra.tools.Headers;


public class AdminHomeScreen extends AppCompatActivity {

    private String TAG = "AdminHomeScreen";

    private AdminHomeScreenBinding adminHomeScreenBinding;
    private ProgressBar progressBar;
    private boolean backPressedToExitOnce = false;

    //Activity use
    private Gson gson;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        adminHomeScreenBinding = AdminHomeScreenBinding.inflate(getLayoutInflater());
        setContentView(adminHomeScreenBinding.getRoot());

        findViewByIds();
        toolbarSetup();
        //showProgressBar();
        getBundleData();
        hideProgressBar();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.admin_home_screen_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.register_user:
                startActivity(new Intent(this, CreateGeneralUser.class).putExtra(Constants.USER_TYPE, Constants.USER_TYPE_ADMIN));
                return true;
            case R.id.logout:
                sendStatusForLogout();
                startActivity(new Intent(this, AdminSignIn.class));
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() != null && TextUtils.isEmpty(result.getContents())) {
                // Log.d("SCAN_FORMAT_NAME", result.getFormatName().trim());
                sendScannedDetailsToCreateLog(result.getContents().trim());
            } else {
                new CustomSnackbar(this, getString(R.string.admin_home_screen_wrong_scan_text), getString(R.string.admin_home_screen_retry_action_text), adminHomeScreenBinding.layoutContainer) {
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

    private void getBundleData() {
    }

    private void findViewByIds() {
        progressBar = findViewById(R.id.progressBar);
    }


    /**
     * TODO - to send status flag for user Logout action
     */
    private void sendStatusForLogout() {
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
        if (!TextUtils.isEmpty(adminHomeScreenBinding.adminHomeInputId.getText())) {
            hideKeyboard();
            getUserDetailsFromServerFor(String.valueOf(adminHomeScreenBinding.adminHomeInputId.getText()));
        }
    }

    private void getUserDetailsFromServerFor(final String userName) {

        parseResponseAndSendToVerify(userName);
//
//        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, Endpoints.SEARCH_BY_USER_NAME + userName, null, new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//                hideProgressBar();
//                //parse Response
//                if (!String.valueOf(response).equals(Constants.EMPTY_JSON)) {
//                    adminHomeScreenBinding.adminHomeInputId.setText(null);
//                    parseResponseAndSendToVerify(userName);
//                } else {
//                    invalidUserName();
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                hideProgressBar();
//                Errors.handleVolleyError(error, TAG, AdminHomeScreen.this);
//            }
//        }) {
//            @Override
//            public Map<String, String> getHeaders() {
//                HashMap<String, String> headers = new HashMap<>();
//                headers.put("Authorization", "Basic eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpZCI6Mn0.1HCAwj7aXeFFAjUJXDATBBUYsWy2-8c01chWoISVPP4");
//                return headers;
//            }
//        };
//
//        ApplicationController.getInstance().addToRequestQueue(request);

    }

    private void parseResponseAndSendToVerify(String userName) {

        startActivity(new Intent(AdminHomeScreen.this, AdminVerifyDoc.class).putExtra(Constants.USER_NAME, userName));
    }

    private void invalidUserName() {
        new CustomSnackbar(this, Constants.INVALID_USERNAME, null, adminHomeScreenBinding.layoutContainer) {
            @Override
            public void onActionClick(View view) {
            }
        }.show();
    }


    private void sendScannedDetailsToCreateLog(String scannedDetails) {
        CreateLog createLog = new CreateLog(null, null, null);

        gson = new Gson();
        String createLogJson = gson.toJson(createLog);
        try {
            apiRequestForCreateLog(new JSONObject(createLogJson));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void apiRequestForCreateLog(JSONObject jsonObject) {
        showProgressBar();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, Endpoints.CREATE_LOG, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                hideProgressBar();
                Toast.makeText(AdminHomeScreen.this, response.toString(), Toast.LENGTH_SHORT).show();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideProgressBar();
                Errors.handleVolleyError(error, TAG, AdminHomeScreen.this);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<>();
                headers.put(Headers.AUTHORIZATION, "Basic eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpZCI6Mn0.1HCAwj7aXeFFAjUJXDATBBUYsWy2-8c01chWoISVPP4");
                return headers;
            }
        };

        ApplicationController.getInstance().addToRequestQueue(request);

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
}
