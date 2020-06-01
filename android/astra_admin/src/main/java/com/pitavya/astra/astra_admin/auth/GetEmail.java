package com.pitavya.astra.astra_admin.auth;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.pitavya.astra.astra_admin.R;


public class GetEmail extends Activity {

    /*
        Stargting of Auth Module will come from Feature Page
        ====================================================
        able to get email from user
        post the email to api to check if email exists
        if exists
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.get_email);
    }
}
