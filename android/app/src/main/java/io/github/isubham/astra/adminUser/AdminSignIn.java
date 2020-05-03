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

import io.github.isubham.astra.databinding.AdminSignInBinding;
import io.github.isubham.astra.tools.ApplicationController;
import io.github.isubham.astra.tools.validators;

public class AdminSignIn extends AppCompatActivity {

    AdminSignInBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = AdminSignInBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);


        addFocusChangeListers();

    }

    public void gotoSignUp(View view) {
        startActivity(new Intent(AdminSignIn.this, AdminCreateAccount.class));
    }

    public void signIn(View view) {

        String url = "https://aastra-stag.herokuapp.com/auth/signin/";
        HashMap<String, String> signInDetails = new HashMap<>();
        signInDetails.put("email", getEmail());
        signInDetails.put("password", getPassword());

        JsonObjectRequest signInRequest = new JsonObjectRequest(url, new JSONObject(signInDetails),
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
                }){
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

        ApplicationController.getInstance().addToRequestQueue(signInRequest);
    }

    private void addFocusChangeListers() {
        binding.adminSignInEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    String emailValidationErrors =
                            validators.emailHasErrors(AdminSignIn.this, getEmail());
                    if (!emailValidationErrors.equals("")) {
                        binding.adminSignInEmail.setError(emailValidationErrors);
                    }
                }
            }
        });

        binding.adminSignInPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    String passwordValidationErrors =
                            validators.passwordHasErrors(AdminSignIn.this, getPassword());
                    if (!passwordValidationErrors.equals("")) {
                        binding.adminSignInPassword.setError(passwordValidationErrors);
                    }
                }
            }
        });
    }

    @SuppressLint("NewApi")
    private String getEmail() {
        return Objects.requireNonNull(binding.adminSignInEmail).getText().toString();
    }

    @SuppressLint("NewApi")
    private String getPassword() {
        return Objects.requireNonNull(binding.adminSignInPassword).getText().toString();
    }
}
