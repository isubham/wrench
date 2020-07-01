package com.pitavya.astra.astra_gen;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.pitavya.astra.astra_common.anim.TypeWriterEffect;
import com.pitavya.astra.astra_common.tools.ScreenshotPreventor;
import com.pitavya.astra.astra_gen.databinding.SplashGenBinding;

public class SplashGen extends AppCompatActivity {
    private SplashGenBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ScreenshotPreventor.preventScreenshot(SplashGen.this);

        binding = SplashGenBinding.inflate(getLayoutInflater());
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
                startActivity(new Intent(SplashGen.this, GeneralUserHomeScreen.class));
                finish();
            }
        }, 3000);

    }

    private void setVersionForTheApp() {

        binding.appVersion.setText("Version " + BuildConfig.VERSION_NAME);

    }
}