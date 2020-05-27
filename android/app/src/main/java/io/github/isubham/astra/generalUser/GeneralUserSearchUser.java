package io.github.isubham.astra.generalUser;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import io.github.isubham.astra.R;
import io.github.isubham.astra.databinding.GeneralUserSearchUserBinding;
import io.github.isubham.astra.model.GeneralUser;
import io.github.isubham.astra.tools.ApplicationController;
import io.github.isubham.astra.tools.Constants;
import io.github.isubham.astra.tools.CustomDatePickerFragment;
import io.github.isubham.astra.tools.Endpoints;
import io.github.isubham.astra.tools.Errors;
import io.github.isubham.astra.tools.Headers;
import io.github.isubham.astra.tools.LoginPersistance;

public class GeneralUserSearchUser extends AppCompatActivity implements CustomDatePickerFragment.TheListener {
    private String TAG = "GeneralUserSearchUser";
    private Gson gson;
    private GeneralUserSearchUserBinding binding;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = GeneralUserSearchUserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        findViewByIds();
        hideProgressBar();
        toolbarSetup();

        //DatePicker
        binding.generalUserEtDob.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                selectDate(v);
            }
        });
    }


    public boolean validateString(String s) {
        return s.length() > 2;
    }

    public void searchUser(View view) {

        final String username = binding.generalUserTilName.getEditText().getText().toString().toUpperCase().trim();
        final String userdob = binding.generalUserTilDob.getEditText().getText().toString().trim();
        final String userfathername = binding.generalUserTilFatherName.getEditText().getText().toString().toUpperCase().trim();


        // string validation for username
        if (!validateString(username)) {
            binding.generalUserTilName.setError("Please enter full name as your ID!");
        } else {
            binding.generalUserTilName.setErrorEnabled(false);
        }

        // string validation for dob
        if (!validateString(userdob)) {
            binding.generalUserTilDob.setError("Please enter DOB as your ID!");
        } else {
            binding.generalUserTilDob.setErrorEnabled(false);
        }

        // string validation for username
        if (!validateString(userfathername)) {
            binding.generalUserTilFatherName.setError("Please enter father name as your ID!");
        } else {
            binding.generalUserTilFatherName.setErrorEnabled(false);
        }


        if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(userdob) && !TextUtils.isEmpty(userfathername)) {

            Log.i("variables : ", username + ' ' + userdob + ' ' + userfathername);
            showProgressBar();

            HashMap<String, String> searchDetails = new HashMap<>();
            searchDetails.put("name", username);
            searchDetails.put("dob", userdob);
            searchDetails.put("father_name", userfathername);

            final JsonObjectRequest searchUserRequest = new JsonObjectRequest(Request.Method.POST, Endpoints.SEARCH_EXISTING_USER, new JSONObject(searchDetails), new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    hideProgressBar();
                    parseResponse(String.valueOf(response));
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    hideProgressBar();
                    Errors.handleVolleyError(error, TAG, GeneralUserSearchUser.this);
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
        }
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
