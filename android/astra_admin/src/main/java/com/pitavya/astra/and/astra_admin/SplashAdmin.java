package com.pitavya.astra.android.astra_admin;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.pitavya.astra.android.astra_admin.adminUser.AdminSignIn;
import com.pitavya.astra.astra_admin.R;
import com.pitavya.astra.astra_admin.databinding.SplashAdminBinding;
import com.pitavya.astra.astra_common.anim.TypeWriterEffect;
import com.pitavya.astra.astra_common.tools.ScreenshotPreventor;

public class SplashAdmin extends AppCompatActivity {

    private SplashAdminBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ScreenshotPreventor.preventScreenshot(SplashAdmin.this);

        binding = SplashAdminBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setVersionForTheApp();
        // animate the app name
        final TypeWriterEffect tw = findViewById(R.id.appName);
        tw.setText("");
        tw.animateText(getResources().getText(R.string.app_name) + " ");
        tw.setAnimDelay(500);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                manageTheRoute();
                finish();
            }
        }, 3000);

    }

    private void manageTheRoute() {
        startActivity(new Intent(SplashAdmin.this, AdminSignIn.class));
        finish();

    }

    private void setVersionForTheApp() {

         binding.appVersion.setText("Version " + BuildConfig.VERSION_NAME);

    }
}