package com.pitavya.astra.astra_gen;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.pitavya.astra.astra_common.CreateGeneralUser;
import com.pitavya.astra.astra_common.GeneralUserViewQr;
import com.pitavya.astra.astra_common.tools.Constants;
import com.pitavya.astra.astra_common.tools.LoginPersistance;
import com.pitavya.astra.astra_common.tools.ScreenshotPreventor;

public class GeneralUserHomeScreen extends AppCompatActivity {

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ScreenshotPreventor.preventScreenshot(GeneralUserHomeScreen.this);

        setContentView(R.layout.general_user_home_screen);

        findViewByIds();
        toolbarSetup();
        hideProgressBar();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.general_user_home_screen_menu, menu);
        if (LoginPersistance.GetGeneralUserName(this) == null) {
            menu.findItem(R.id.removemyqr).setVisible(false);
        }
        return true;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        finish();
        startActivity(getIntent());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.myqr:
                if (LoginPersistance.GetGeneralUserName(this) != null) {
                    startActivity(new Intent(this, GeneralUserViewQr.class).putExtra(Constants.USER_TYPE, Constants.USER_TYPE_ADMIN));
                    return true;
                } else {
                    Toast.makeText(this, "Please Save your QR Code by searching through existing user", Toast.LENGTH_LONG).show();
                    return true;
                }
            case R.id.removemyqr:
                LoginPersistance.update(null, null, null, null, null, this);
                Toast.makeText(this, "MyQR sucessfully removed", Toast.LENGTH_LONG).show();

//                sendStatusForLogout();
//                Intent toSignInWithoutHistory = new Intent(this, AdminSignIn.class);
//                startActivity(toSignInWithoutHistory);
//                finishAffinity();
                item.setVisible(false);
//                this.onCreate(null);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }


    public void ExistingUser(View view) {
        // switch to general user search page
        Intent i = new Intent(GeneralUserHomeScreen.this, GeneralUserSearchUser.class);
        startActivity(i);

    }

    public void RegisterUser(View view) {
        startActivity(new Intent(GeneralUserHomeScreen.this, CreateGeneralUser.class).putExtra(Constants.USER_TYPE, Constants.USER_TYPE_ADMIN)
        );

    }

    private void toolbarSetup() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            toolbar.setTitleTextColor(getResources().getColor(R.color.colorPrimaryDark));

        }
    }

    private void findViewByIds() {
        progressBar = findViewById(R.id.progressBar);
    }

    public void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

    }

    public void hideProgressBar() {
        progressBar.setVisibility(View.INVISIBLE);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }


}
