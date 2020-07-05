package com.pitavya.astra.astra_gen;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.pitavya.astra.astra_common.GeneralUserViewQr;
import com.pitavya.astra.astra_common.model.GeneralUser;
import com.pitavya.astra.astra_common.tools.ApplicationController;
import com.pitavya.astra.astra_common.tools.Constants;
import com.pitavya.astra.astra_common.tools.CustomDatePickerFragment;
import com.pitavya.astra.astra_common.tools.Endpoints;
import com.pitavya.astra.astra_common.tools.Errors;
import com.pitavya.astra.astra_common.tools.Headers;
import com.pitavya.astra.astra_common.tools.LoginPersistance;
import com.pitavya.astra.astra_common.tools.ScreenshotPreventor;
import com.pitavya.astra.astra_gen.databinding.GeneralUserSearchUserBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;



public class GeneralUserSearchUser extends AppCompatActivity implements CustomDatePickerFragment.TheListener {
    private String TAG = "GeneralUserSearchUser";
    private String TOOLBAR_TITLE = "Search User";

    private Gson gson;
    private GeneralUserSearchUserBinding binding;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ScreenshotPreventor.preventScreenshot(GeneralUserSearchUser.this);

        binding = GeneralUserSearchUserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        findViewByIds();
        hideProgressBar();
        toolbarSetup();
        setupDatePicket();
    }

    private void setupDatePicket() {
        binding.generalUserEtDob.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                selectDate(v);
            }
        });
    }


    public boolean validateFields(String s) {
        return s.length() > 0;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private String getUserName() {
        return Objects.requireNonNull(binding.generalUserTilName.getEditText()).getText().toString().trim();
    }

    private void resetErrors() {
        binding.generalUserTilDob.setErrorEnabled(false);
        binding.generalUserTilFatherName.setErrorEnabled(false);
        binding.generalUserTilName.setErrorEnabled(false);
    }

    private void populateServerErrors() {
        binding.generalUserTilDob.setError("Incorrect Details");
        binding.generalUserTilFatherName.setError("Incorrect Details");
        binding.generalUserTilName.setError("Incorrect Details");
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void searchUser(View view) throws JSONException {
        resetErrors();
        boolean allFieldsValidated = validateFields(getUserName(), getDob(), getFatherName());

        if (allFieldsValidated) {

            showProgressBar();

            GeneralUser generalUser = new GeneralUser();
            generalUser.setName(getUserName());
            generalUser.setDob(getDob());
            generalUser.setFather_name(getFatherName());

            final JsonObjectRequest searchUserRequest = new JsonObjectRequest(Request.Method.POST,
                    Endpoints.SEARCH_EXISTING_USER, new JSONObject(new Gson().toJson(generalUser)),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            hideProgressBar();
                            Log.e(TAG, response.toString());

                            if(response.has(Constants.CODE)) {
                                populateServerErrors();
                            }

                            else{
                                parseResponse(String.valueOf(response));
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            hideProgressBar();
                            Errors.handleVolleyError(error, TAG, GeneralUserSearchUser.this,true);
                        }

                    }) {

                @Override
                public Map<String, String> getHeaders() {
                    HashMap<String, String> headers = new HashMap<>();
                    headers.put(Headers.CONTENT_TYPE, Headers.APPLICATION_JSON);
                    return headers;
                }
            };
            searchUserRequest.setRetryPolicy(new DefaultRetryPolicy(
                    10000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
            ));

            ApplicationController.getInstance().addToRequestQueue(searchUserRequest);

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private String getFatherName() {
        return Objects.requireNonNull(binding.generalUserTilFatherName.getEditText()).getText().toString().trim();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private String getDob() {
        return Objects.requireNonNull(binding.generalUserTilDob.getEditText()).getText().toString().trim();
    }

    private boolean validateFields(String username, String userdob, String userfathername) {

        boolean usernameValidated = validateUsername(username);
        boolean dobValidated = validateDob(userdob);
        boolean fatherNameValidated = validateFatherName(userfathername);
        return usernameValidated && dobValidated && fatherNameValidated;
    }

    private boolean validateFatherName(String userfathername) {
        if (!validateFields(userfathername)) {
            binding.generalUserTilFatherName.setError("Please enter father name as your ID!");
            return false;
        } else {
            binding.generalUserTilFatherName.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validateDob(String userdob) {
        if (!validateFields(userdob)) {
            binding.generalUserTilDob.setError("Please enter DOB as your ID!");
            return false;
        } else {
            binding.generalUserTilDob.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validateUsername(String username) {
        if (!validateFields(username)) {
            binding.generalUserTilName.setError("Please enter full name as your ID!");
            return false;
        } else {
            binding.generalUserTilName.setErrorEnabled(false);
            return true;
        }
    }

    private void parseResponse(String response) {
        gson = new Gson();
        GeneralUser generalUser = gson.fromJson(response, GeneralUser.class);

        LoginPersistance.Iupdate(generalUser.getUsername(), generalUser.getToken(), generalUser.getProfile_pic(), generalUser.getId_front(), generalUser.getId_back(), this);
        startActivity(new Intent(GeneralUserSearchUser.this, GeneralUserViewQr.class)
                .putExtra(Constants.USER_NAME, generalUser.getUsername()).putExtra(Constants.USER_TYPE, Constants.USER_TYPE_GENERAL));
        finish();
    }

    public void selectDate(View view) {
        DialogFragment fragment = new CustomDatePickerFragment();
        fragment.show(getSupportFragmentManager(), "date picker");
    }

    @Override
    public void returnDate(String date) {
        binding.generalUserEtDob.setText(date);
    }


    // app bar

    private void toolbarSetup() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            toolbar.setTitleTextColor(getResources().getColor(R.color.colorPrimaryDark));
            getSupportActionBar().setTitle(TOOLBAR_TITLE);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                this.finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void findViewByIds() {
        progressBar = findViewById(R.id.progressBar);
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
