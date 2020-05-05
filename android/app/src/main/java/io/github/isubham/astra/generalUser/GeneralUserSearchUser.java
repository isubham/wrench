package io.github.isubham.astra.generalUser;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import io.github.isubham.astra.R;

public class GeneralUserSearchUser extends AppCompatActivity {
    TextInputLayout til_name ,til_dob,til_father_name;
    GeneralUserSearchUserBinding binding;
//    TextInputEditText et_name,et_dob,et_father_name;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general_user_search);
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
        return s.length() > 3;
    }
    public void searchUser(View view){
        hideKeyboard();
        String username = til_name.getEditText().getText().toString();
        String userdob = til_dob.getEditText().getText().toString();
        String userfathername = til_father_name.getEditText().getText().toString();

        // string validation for username
        if (!validateString(username)) {
            til_name.setError("Please enter full name as your ID!");
        }
        else {
            til_name.setErrorEnabled(false);
        }

        // string validation for dob
        if (!validateString(userdob)) {
            til_dob.setError("Please enter DOB as your ID!");
        }
        else {
            til_dob.setErrorEnabled(false);
        }

        // string validation for username
        if (!validateString(userfathername)) {
            til_father_name.setError("Please enter father name as your ID!");
        }
        else {
            til_father_name.setErrorEnabled(false);
        }

        Toast. makeText(getApplicationContext(),username+' '+userdob+' '+userfathername,Toast. LENGTH_SHORT).show();
    }
}
