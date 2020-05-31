package com.pitavya.astra.astra_admin.adminUser;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.pitavya.astra.astra_admin.databinding.AdminSignInBinding;
import com.pitavya.astra.astra_common.model.ErrorResponse;
import com.pitavya.astra.astra_common.model.User;
import com.pitavya.astra.astra_common.tools.Constants;
import com.pitavya.astra.astra_common.tools.Endpoints;
import com.pitavya.astra.astra_common.tools.Errors;
import com.pitavya.astra.astra_common.tools.Headers;
import com.pitavya.astra.astra_common.tools.LoginPersistance;
import com.pitavya.astra.astra_common.tools.StatefulButton;
import com.pitavya.astra.astra_common.tools.Validators;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;



public class AdminSignIn extends AppCompatActivity {

    AdminSignInBinding binding;
    StatefulButton statefulButton;
    private boolean backPressedToExitOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = AdminSignInBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        if (LoginPersistance.GetToken(AdminSignIn.this) != null) {
            startActivity(new Intent(AdminSignIn.this, AdminHomeScreen.class));
        }

        // (Button active, Button loading, Button next, TextView successMessage) {
        statefulButton = new StatefulButton(
                binding.adminSignInActiveButton,
                binding.adminSignInLoadingButton,
                binding.adminSignInContinueButton,
                binding.adminSignInSuccessMessage
        );

        addFocusChangeListers();

    }

    public void gotoSignUp(View view) {
        startActivity(new Intent(AdminSignIn.this, AdminCreateAccount.class));
    }

    public void signIn(View view) {
        if (!validateFields()) {
            Toast.makeText(this, "Please Correct Errors", Toast.LENGTH_SHORT).show();
        } else {
            statefulButton.setLoading();

            HashMap<String, String> signInDetails = new HashMap<>();
            signInDetails.put("email", getEmail());
            signInDetails.put("password", getPassword());


            JsonObjectRequest signInRequest = new JsonObjectRequest(Endpoints.SIGN_IN, new JSONObject(signInDetails),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            if (response.has(Constants.CODE)) {
                                statefulButton.setActive();
                                ErrorResponse error = new Gson().fromJson(response.toString(), ErrorResponse.class);
                                binding.adminSignInEmail.setError(error.message);
                                binding.adminSignInPassword.setError(error.message);
                            } else {
                                hideAllFields();
                                User createdUser = new Gson().fromJson(response.toString(), User.class);
                                LoginPersistance.Save(getEmail(), createdUser.getToken(), AdminSignIn.this);
                                statefulButton.setNext("Welcome Back " + getEmail());
                            }

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Errors.handleVolleyError(error, "admin signin", AdminSignIn.this);
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    return super.getParams();
                }

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<>();
                    headers.put(Headers.CONTENT_TYPE, Headers.APPLICATION_JSON);
                    // headers.put("Authorization", "Basic " + "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpZCI6ODN9.YgHTISz_lJxtltFpBa1slcjcdxZoFS26b7T-QqbDMuc");
                    return headers;
                }

            };

            signInRequest.setRetryPolicy(new DefaultRetryPolicy(
                    10000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
            ));
            Volley.newRequestQueue(AdminSignIn.this).add(signInRequest);
        }
    }

    private void hideAllFields() {
        binding.adminSignInEmailLayout.setVisibility(View.GONE);
        binding.adminSignInPasswordLayout.setVisibility(View.GONE);
        binding.adminSignInEmail.setVisibility(View.GONE);
        binding.adminSignInPassword.setVisibility(View.GONE);
        binding.adminSignInCreateAccountLink.setVisibility(View.GONE);

    }

    private boolean validateFields() {
        boolean emailValid = validateEmail();
        boolean passwordValid = validatePassword();
        return emailValid && passwordValid;
    }

    private void addFocusChangeListers() {
        binding.adminSignInEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    validateEmail();
                }
            }
        });

        binding.adminSignInPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    validatePassword();
                }
            }
        });
    }

    private boolean validatePassword() {
        String passwordValidationErrors =
                Validators.passwordHasErrors(AdminSignIn.this, getPassword());
        if (!passwordValidationErrors.equals("")) {
            binding.adminSignInPassword.setError(passwordValidationErrors);
        }
        return passwordValidationErrors.equals("");
    }

    private boolean validateEmail() {
        String emailValidationErrors =
                Validators.emailHasErrors(AdminSignIn.this, getEmail());
        if (!emailValidationErrors.equals("")) {
            binding.adminSignInEmail.setError(emailValidationErrors);
        }
        return emailValidationErrors.equals("");
    }

    @SuppressLint("NewApi")
    private String getEmail() {
        return Objects.requireNonNull(binding.adminSignInEmail).getText().toString().trim();
    }

    @SuppressLint("NewApi")
    private String getPassword() {
        return Objects.requireNonNull(binding.adminSignInPassword).getText().toString().trim();
    }

    @Override
    public void onBackPressed() {
        if (backPressedToExitOnce) {
//          super.onBackPressed();
            System.exit(0);
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

    public void gotoPanel(View view) {
        startActivity(new Intent(AdminSignIn.this, AdminHomeScreen.class));
        finish();
    }
}
