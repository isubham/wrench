package io.github.isubham.astra.adminUser;

import android.app.Activity;
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
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import io.github.isubham.astra.R;
import io.github.isubham.astra.databinding.AdminHomeScreenBinding;
import io.github.isubham.astra.generalUser.CreateGeneralUser;
import io.github.isubham.astra.model.GeneralUser;
import io.github.isubham.astra.tools.ApplicationController;
import io.github.isubham.astra.tools.Constants;
import io.github.isubham.astra.tools.CustomSnackbar;
import io.github.isubham.astra.tools.Endpoints;
import io.github.isubham.astra.tools.Errors;
import io.github.isubham.astra.tools.LoginPersistance;

public class AdminHomeScreen extends AppCompatActivity {

    private String TAG = "AdminHomeScreen";

    private AdminHomeScreenBinding binding;
    private ProgressBar progressBar;
    private boolean backPressedToExitOnce = false;

    //Activity use
    private Gson gson;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = AdminHomeScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

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

    private void getBundleData() {
    }

    private void findViewByIds() {
        progressBar = findViewById(R.id.progressBar);
    }


    //TODO - to send status flag for user Logout action
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
                headers.put("Authorization", "Basic " + LoginPersistance.GetToken(AdminHomeScreen.this));
                return headers;
            }
        };

        ApplicationController.getInstance().addToRequestQueue(request);

    }

    private void parseResponse(String response, String userName) {

        GeneralUser generalUser = new Gson().fromJson(response, GeneralUser.class);
        //update SharedPref
        LoginPersistance.update(generalUser.getProfile_pic(), generalUser.getId_front(),
                generalUser.getId_back(), AdminHomeScreen.this);

        sendToVerify(userName, generalUser.getName(), generalUser.getUser_id());
    }

    private void sendToVerify(String userName, String name, int userId) {
        startActivity(new Intent(AdminHomeScreen.this, AdminVerifyDoc.class)
                .putExtra(Constants.USER_NAME, userName)
                .putExtra(Constants.NAME, name)
                .putExtra(Constants.ID, userId)
                .putExtra(Constants.ACTION_IN_OUT, binding.adminHomeSwitch.isChecked() ? Constants.ACTION_IN : Constants.ACTION_OUT)

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
}
