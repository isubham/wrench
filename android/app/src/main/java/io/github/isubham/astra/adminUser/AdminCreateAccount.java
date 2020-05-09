package io.github.isubham.astra.adminUser;
import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import io.github.isubham.astra.databinding.AdminCreateAccountBinding;
import io.github.isubham.astra.model.User;
import io.github.isubham.astra.tools.Constants;
import io.github.isubham.astra.tools.LoginPersistance;
import io.github.isubham.astra.tools.validators;

public class AdminCreateAccount extends AppCompatActivity {

    /* TODO
        - [x] on lose focus of any element
        - [x] check if password and confirm password match
        - [ ] button states of [disable, active, in progress, green, red]
     */
    AdminCreateAccountBinding binding;
    StatefulButton statefulButton;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = AdminCreateAccountBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        addFocusChangeListers();
        // binding = DataBindingUtil.setContentView(this, R.layout.admin_create_account);
        // user = new User("", "", "");
        // binding.setUser(user);

        statefulButton = new StatefulButton(binding.adminCreateAccountSignupButtonActive,
                binding.adminCreateAccountSignupButtonLoading,
                binding.adminCreateAccountSignupButtonSuccess,
                binding.adminCreateAccountSignupButtonDanger,
                binding.adminCreateAccountSignupButtonGotoPanel);

    }


    public void signUp(View V) {

        if (!validateFields()) {
            Toast.makeText(this, "Please Correct Errors", Toast.LENGTH_SHORT).show();
        } else {

            statefulButton.setLoading();

            String url = "https://aastra-stag.herokuapp.com/auth/signup/";
            HashMap<String, String> signupDetails = new HashMap<>();
            signupDetails.put("email", getEmail());
            signupDetails.put("password", getPassword());
            signupDetails.put("license_key", getLicenseKey());


            JsonObjectRequest signUpRequest = new JsonObjectRequest(url, new JSONObject(signupDetails),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            statefulButton.setSuccess();
                            User createdUser = new Gson().fromJson(response.toString(), User.class);
                            LoginPersistance.Save(getEmail(), createdUser.getToken(), AdminCreateAccount.this);
                            // startActivity(new Intent(AdminCreateAccount.this, AdminHomeScreen.class));

                            final Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    //Do something after 100ms
                                    statefulButton.setNext();
                                }
                            }, 2000);

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            statefulButton.setFailure();


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

            Volley.newRequestQueue(AdminCreateAccount.this).add(signUpRequest);
        }

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
        boolean emailValid = validateEmail() ;
        boolean passwordValid = validatePassword();
        boolean confirmPasswordValid = validateConfirmPassword();
        boolean licenseTextValid = validateLicenseText();
        return emailValid && passwordValid && confirmPasswordValid && licenseTextValid;
    }

    private boolean validateLicenseText() {
        String licenseValidationErrors =
                validators.licenseHasErrors(AdminCreateAccount.this, getLicenseKey());
        if (!licenseValidationErrors.equals("")) {
            binding.adminCreateAccountLicenseText.setError(licenseValidationErrors);
        }
        return licenseValidationErrors.equals("");
    }

    private boolean validateConfirmPassword() {
        String confirmPasswordValidationErrors =
                validators.passwordHasErrors(AdminCreateAccount.this, getConfirmPassword());
        if (!confirmPasswordValidationErrors.equals("")) {
            binding.adminCreateAccountConfirmPassword.setError(confirmPasswordValidationErrors);
        }
        if (!getPassword().equals(getConfirmPassword())) {
            binding.adminCreateAccountConfirmPassword.setError(Constants.password_confirm_password_dont_match);
            confirmPasswordValidationErrors = Constants.password_confirm_password_dont_match;
        }
        return confirmPasswordValidationErrors.equals("");
    }

    private boolean validatePassword() {
        String passwordValidationErrors =
                validators.passwordHasErrors(AdminCreateAccount.this, getPassword());
        if (!passwordValidationErrors.equals("")) {
            binding.adminCreateAccountPassword.setError(passwordValidationErrors);
        }
        return passwordValidationErrors.equals("");
    }

    private boolean validateEmail() {
        String emailValidationErrors =
                validators.emailHasErrors(AdminCreateAccount.this, getEmail());
        if (!emailValidationErrors.equals("")) {
            binding.adminCreateAccountEmail.setError(emailValidationErrors);
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

    public void showDisableToast(View view) {
    }

    public void gotoPanel(View view) {

    }

    /* Button state based model
     * */

    class StatefulButton {
        private Button active, loading, success, failure, next;

        public StatefulButton(Button active, Button loading, Button success, Button failure, Button next) {
            this.active = active;
            this.loading = loading;
            this.success = success;
            this.failure = failure;
            this.next = next;
            setActive();
        }

        private void hideAll() {
            active.setVisibility(View.GONE);
            loading.setVisibility(View.GONE);
            success.setVisibility(View.GONE);
            failure.setVisibility(View.GONE);
            next.setVisibility(View.GONE);
        }


        public void setActive() {
            hideAll();
            active.setVisibility(View.VISIBLE);
        }

        public void setLoading() {
            hideAll();
            loading.setVisibility(View.VISIBLE);
        }

        public void setSuccess() {
            hideAll();
            success.setVisibility(View.VISIBLE);
        }

        public void setFailure() {
            hideAll();
            failure.setVisibility(View.VISIBLE);
        }

        public void setNext() {
            hideAll();
            next.setVisibility(View.VISIBLE);
        }

    }


}
