package io.github.isubham.astra.adminUser;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import io.github.isubham.astra.databinding.AdminCreateAccountBinding;
import io.github.isubham.astra.tools.ApplicationController;
import io.github.isubham.astra.tools.Constants;
import io.github.isubham.astra.tools.validators;

public class AdminCreateAccount extends AppCompatActivity {

    /* TODO
        - [x] on lose focus of any element
        - [x] check if password and confirm password match
        - [ ] button states of [disable, active, in progress, green, red]
     */
    AdminCreateAccountBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = AdminCreateAccountBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        addFocusChangeListers();

    }


    public void signUp(View V) {
        String url = "https://aastra-stag.herokuapp.com/auth/signup/";
        HashMap<String, String> signupDetails = new HashMap<>();
        signupDetails.put("email", getEmail());
        signupDetails.put("password", getPassword());

        JsonObjectRequest signUpRequest = new JsonObjectRequest(url, new JSONObject(signupDetails),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        // parse response

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return super.getParams();
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                // headers.put("Authorization", "Basic " + "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpZCI6ODN9.YgHTISz_lJxtltFpBa1slcjcdxZoFS26b7T-QqbDMuc");
                return headers;
            }

        };

        ApplicationController.getInstance().addToRequestQueue(signUpRequest);

    }


    public void gotoSignIn(View V) {
        startActivity(new Intent(AdminCreateAccount.this, AdminSignIn.class));
    }

    private void addFocusChangeListers() {
        binding.adminCreateAccountEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    String emailValidationErrors =
                            validators.emailHasErrors(AdminCreateAccount.this, getEmail());
                    if (!emailValidationErrors.equals("")) {
                        binding.adminCreateAccountEmail.setError(emailValidationErrors);
                    }
                }
            }
        });

        binding.adminCreateAccountPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    String passwordValidationErrors =
                            validators.passwordHasErrors(AdminCreateAccount.this, getPassword());
                    if (!passwordValidationErrors.equals("")) {
                        binding.adminCreateAccountPassword.setError(passwordValidationErrors);
                    }
                }
            }
        });

        binding.adminCreateAccountConfirmPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    String confirmPasswordValidationErrors =
                            validators.passwordHasErrors(AdminCreateAccount.this, getConfirmPassword());
                    if (!confirmPasswordValidationErrors.equals("")) {
                        binding.adminCreateAccountConfirmPassword.setError(confirmPasswordValidationErrors);
                    }
                    if (!getPassword().equals(getConfirmPassword())) {
                        binding.adminCreateAccountConfirmPassword.setError(Constants.password_confirm_password_dont_match);
                    }
                }
            }
        });


        binding.adminCreateAccountLicenseText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    String licenseValidationErrors =
                            validators.licenseHasErrors(AdminCreateAccount.this, getLicenseText());
                    if (!licenseValidationErrors.equals("")) {
                        binding.adminCreateAccountLicenseText.setError(licenseValidationErrors);
                    }
                }
            }
        });


    }

    @SuppressLint("NewApi")
    private String getEmail() {
        return Objects.requireNonNull(binding.adminCreateAccountEmail).getText().toString();
    }

    @SuppressLint("NewApi")
    private String getPassword() {
        return Objects.requireNonNull(binding.adminCreateAccountPassword).getText().toString();
    }

    @SuppressLint("NewApi")
    private String getConfirmPassword() {
        return Objects.requireNonNull(binding.adminCreateAccountConfirmPassword).getText().toString();
    }

    @SuppressLint("NewApi")
    private String getLicenseText() {
        return Objects.requireNonNull(binding.adminCreateAccountLicenseText).getText().toString();
    }

}
