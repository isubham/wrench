package com.pitavya.astra.astra_admin.adminUser;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
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
import com.pitavya.astra.astra_admin.databinding.AdminCreateAccountBinding;
import com.pitavya.astra.astra_common.CreateGeneralUser;
import com.pitavya.astra.astra_common.model.ErrorResponse;
import com.pitavya.astra.astra_common.model.User;
import com.pitavya.astra.astra_common.tools.Constants;
import com.pitavya.astra.astra_common.tools.Errors;
import com.pitavya.astra.astra_common.tools.LoginPersistance;
import com.pitavya.astra.astra_common.tools.ScreenshotPreventor;
import com.pitavya.astra.astra_common.tools.StatefulButton;
import com.pitavya.astra.astra_common.tools.Validators;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class AdminCreateAccount extends AppCompatActivity {

    AdminCreateAccountBinding binding;
    StatefulButton statefulButton;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ScreenshotPreventor.preventScreenshot(AdminCreateAccount.this);

        binding = AdminCreateAccountBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        addFocusChangeListers();
        // binding = DataBindingUtil.setContentView(this, R.layout.admin_create_account);
        // user = new User("", "", "");
        // binding.setUser(user);

        statefulButton = new StatefulButton(binding.adminCreateAccountSignupButtonActive,
                binding.adminCreateAccountSignupButtonLoading,
                binding.adminCreateAccountSignupButtonGotoPanel,
                binding.adminCreateAccountSuccessMessage);

    }


    public void signUp(View V) {

        if (!validateFields()) {
            Toast.makeText(this, "Please Correct Errors", Toast.LENGTH_SHORT).show();
        } else {

            statefulButton.setLoading();

            String url = "https://aastra-stag.herokuapp.com/auth/signup/";
            final HashMap<String, String> signupDetails = new HashMap<>();
            signupDetails.put("email", getEmail());
            signupDetails.put("password", getPassword());
            signupDetails.put("license_key", getLicenseKey());


            JsonObjectRequest signUpRequest = new JsonObjectRequest(url, new JSONObject(signupDetails),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            if (response.has("code")) {
                                statefulButton.setActive();
                                ErrorResponse error = new Gson().fromJson(response.toString(), ErrorResponse.class);
                                if (error.code > 7 && error.code < 13) {
                                    binding.adminCreateAccountLicenseText.setError(error.message);
                                }
                                if (error.code == 1) {
                                    binding.adminCreateAccountEmail.setError(error.message);
                                }
                            } else {
                                hideAllFields();
                                User createdUser = new Gson().fromJson(response.toString(), User.class);
                                LoginPersistance.Save(getEmail(), createdUser.getToken(), AdminCreateAccount.this);
                                statefulButton.setNext("Welcome " + LoginPersistance.GetEmail(AdminCreateAccount.this));
                            }

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Errors.handleVolleyError(error, "admin_c_account", AdminCreateAccount.this, true);
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

            signUpRequest.setRetryPolicy(new DefaultRetryPolicy(
                    10000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
            ));

            Volley.newRequestQueue(AdminCreateAccount.this).add(signUpRequest);
        }

    }

    private void hideAllFields() {
        binding.adminCreateAccountEmailTextInputLayout.setVisibility(View.GONE);
        binding.adminCreateAccountPasswordTextInputLayout.setVisibility(View.GONE);
        binding.adminCreateAccountConfirmPasswordTextInputLayout.setVisibility(View.GONE);
        binding.adminCreateAccountLicenseTextInputLayout.setVisibility(View.GONE);

        binding.adminCreateAccountEmail.setVisibility(View.GONE);
        binding.adminCreateAccountPassword.setVisibility(View.GONE);
        binding.adminCreateAccountConfirmPassword.setVisibility(View.GONE);
        binding.adminCreateAccountLicenseText.setVisibility(View.GONE);

        binding.adminCreateAccountSigninLink.setVisibility(View.GONE);
    }


    public void gotoSignIn(View V) {
        startActivity(new Intent(AdminCreateAccount.this, AdminSignIn.class));
    }


    private void addFocusChangeListers() {
        binding.adminCreateAccountEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    validateEmail();
                }

            }
        });

        binding.adminCreateAccountPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    validatePassword();
                }
            }
        });

        binding.adminCreateAccountConfirmPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    validateConfirmPassword();
                }
            }
        });


        binding.adminCreateAccountLicenseText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    validateLicenseText();
                }
            }
        });


    }

    private boolean validateFields() {
        boolean emailValid = validateEmail();
        boolean passwordValid = validatePassword();
        boolean confirmPasswordValid = validateConfirmPassword();
        boolean licenseTextValid = validateLicenseText();
        return emailValid && passwordValid && confirmPasswordValid && licenseTextValid;
    }

    private boolean validateLicenseText() {
        String licenseValidationErrors =
                Validators.licenseHasErrors(AdminCreateAccount.this, getLicenseKey());
        binding.adminCreateAccountLicenseTextInputLayout.setErrorEnabled(false);
        if (!licenseValidationErrors.equals("")) {
            binding.adminCreateAccountLicenseTextInputLayout.setError(licenseValidationErrors);
        }
        return licenseValidationErrors.equals("");
    }

    private boolean validateConfirmPassword() {
        String confirmPasswordValidationErrors =
                Validators.passwordHasErrors(AdminCreateAccount.this, getConfirmPassword());
        binding.adminCreateAccountConfirmPasswordTextInputLayout.setErrorEnabled(false);
        if (!confirmPasswordValidationErrors.equals("")) {
            binding.adminCreateAccountConfirmPasswordTextInputLayout.setError(confirmPasswordValidationErrors);
        }
        if (!getPassword().equals(getConfirmPassword())) {
            binding.adminCreateAccountConfirmPasswordTextInputLayout.setError(Constants.password_confirm_password_dont_match);
            confirmPasswordValidationErrors = Constants.password_confirm_password_dont_match;
        }
        return confirmPasswordValidationErrors.equals("");
    }

    private boolean validatePassword() {
        String passwordValidationErrors =
                Validators.passwordHasErrors(AdminCreateAccount.this, getPassword());
        binding.adminCreateAccountPasswordTextInputLayout.setErrorEnabled(false);
        if (!passwordValidationErrors.equals("")) {
            binding.adminCreateAccountPasswordTextInputLayout.setError(passwordValidationErrors);
        }
        return passwordValidationErrors.equals("");
    }

    private boolean validateEmail() {
        String emailValidationErrors =
                Validators.emailHasErrors(AdminCreateAccount.this, getEmail());
        binding.adminCreateAccountEmailTextInputLayout.setErrorEnabled(false);
        if (!emailValidationErrors.equals("")) {
            binding.adminCreateAccountEmailTextInputLayout.setError(emailValidationErrors);
        }
        return emailValidationErrors.equals("");
    }

    @SuppressLint("NewApi")
    private String getConfirmPassword() {
        return Objects.requireNonNull(binding.adminCreateAccountConfirmPassword).getText().toString().trim();
    }

    @SuppressLint("NewApi")
    private String getEmail() {
        return Objects.requireNonNull(binding.adminCreateAccountEmail).getText().toString().trim();
    }

    @SuppressLint("NewApi")
    private String getPassword() {
        return Objects.requireNonNull(binding.adminCreateAccountPassword).getText().toString().trim();
    }

    @SuppressLint("NewApi")
    private String getLicenseKey() {
        return Objects.requireNonNull(binding.adminCreateAccountLicenseText).getText().toString().trim();
    }

    public void gotoPanel(View view) {
        startActivity(new Intent(AdminCreateAccount.this, AdminHomeScreen.class));
    }

}
