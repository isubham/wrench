package io.github.isubham.astra.generalUser;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import io.github.isubham.astra.R;
import io.github.isubham.astra.databinding.GeneralUserSearchUserBinding;
import io.github.isubham.astra.model.GeneralUser;
import io.github.isubham.astra.tools.ApplicationController;
import io.github.isubham.astra.tools.Constants;
import io.github.isubham.astra.tools.Errors;
import io.github.isubham.astra.tools.LoginPersistance;

public class GeneralUserSearchUser extends AppCompatActivity {
    TextInputLayout til_name, til_dob, til_father_name;
    private String TAG = "GeneralUserSearchUser";
    private Gson gson;
    private GeneralUserSearchUserBinding binding;

    //    TextInputEditText et_name,et_dob,et_father_name;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.general_user_search_user);
        til_name = findViewById(R.id.general_user_til_name);
        til_dob = findViewById(R.id.general_user_til_dob);
        til_father_name = findViewById(R.id.general_user_til_father_name);
//        et_name = findViewById(R.id.general_user_et_name);
//        et_dob = findViewById(R.id.general_user_et_dob);
//        et_father_name = findViewById(R.id.general_user_et_father_name);

    }

    private void hideKeyboard() {
        View view = getCurrentFocus();
        if (view != null) {
            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).
                    hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public boolean validateString(String s) {
        return s.length() > 2;
    }

    public void searchUser(View view) {
        hideKeyboard();
        final String username = til_name.getEditText().getText().toString().trim();
        final String userdob = til_dob.getEditText().getText().toString().trim();
        final String userfathername = til_father_name.getEditText().getText().toString().trim();

        // string validation for username
        if (!validateString(username)) {
            til_name.setError("Please enter full name as your ID!");
        } else {
            til_name.setErrorEnabled(false);
        }

        // string validation for dob
        if (!validateString(userdob)) {
            til_dob.setError("Please enter DOB as your ID!");
        } else {
            til_dob.setErrorEnabled(false);
        }

        // string validation for username
        if (!validateString(userfathername)) {
            til_father_name.setError("Please enter father name as your ID!");
        } else {
            til_father_name.setErrorEnabled(false);
        }

        Toast.makeText(getApplicationContext(), username + ' ' + userdob + ' ' + userfathername, Toast.LENGTH_SHORT).show();

        String url = "https://aastra-stag.herokuapp.com/person/fuzzy/";
        HashMap<String, String> searchDetails = new HashMap<>();
        searchDetails.put("name", username);
        searchDetails.put("dob", userdob);
        searchDetails.put("father_name", userfathername);

        JsonObjectRequest searchUserRequest = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(searchDetails), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                parseResponse(String.valueOf(response));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Errors.handleVolleyError(error, TAG, GeneralUserSearchUser.this);

            }
        }) {

            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };

        ApplicationController.getInstance().addToRequestQueue(searchUserRequest);

    }

    private void parseResponse(String response) {
        gson = new Gson();
        GeneralUser generalUser = gson.fromJson(response, GeneralUser.class);

        LoginPersistance.update(generalUser.getUsername(), generalUser.getToken(), generalUser.getProfile_pic(), generalUser.getId_front(), generalUser.getId_back(), this);
        startActivity(new Intent(GeneralUserSearchUser.this, GeneralUserViewQr.class)
                .putExtra(Constants.USER_NAME, generalUser.getUsername()).putExtra(Constants.USER_TYPE, Constants.USER_TYPE_GENERAL));

    }
}
