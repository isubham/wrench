package com.pitavya.astra.astra_gen;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.play.core.install.model.ActivityResult;
import com.pitavya.astra.astra_common.CreateGeneralUser;
import com.pitavya.astra.astra_common.GeneralUserViewQr;
import com.pitavya.astra.astra_common.tools.Constants;
import com.pitavya.astra.astra_common.tools.SendMail;
import com.pitavya.astra.astra_common.tools.CustomSnackbar;
import com.pitavya.astra.astra_common.tools.FileChooser;
import com.pitavya.astra.astra_common.tools.LoginPersistance;
import com.pitavya.astra.astra_common.tools.ScreenshotPreventor;
import com.pitavya.astra.astra_gen.databinding.GeneralUserHomeScreenBinding;

public class GeneralUserHomeScreen extends AppCompatActivity {

    private GeneralUserHomeScreenBinding binding;
    private ProgressBar progressBar;
    private String TAG = GeneralUserHomeScreen.class.getName();
    private int requestUpdateCode = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ScreenshotPreventor.preventScreenshot(GeneralUserHomeScreen.this);

        binding = GeneralUserHomeScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        findViewByIds();
        toolbarSetup();
        hideProgressBar();

        checkForAppLatestTheUpdate();
    }

    private void checkForAppLatestTheUpdate() {
        new GenAppUpdate(GeneralUserHomeScreen.this, GeneralUserHomeScreen.this).checkForUpdate(requestUpdateCode);
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
                    startActivity(new Intent(this, GeneralUserViewQr.class).putExtra(Constants.USER_NAME,
                            LoginPersistance.GetGeneralUserName(this)).putExtra(Constants.USER_TYPE, Constants.USER_TYPE_ADMIN));
                    return true;
                } else {
                    Toast.makeText(this, "Please save your QR code by searching through existing user", Toast.LENGTH_LONG).show();
                    return true;
                }
            case R.id.removemyqr:
                LoginPersistance.update(null, null, null, null, null, this);
                Toast.makeText(this, "Saved QR removed successfully", Toast.LENGTH_LONG).show();
                item.setVisible(false);
//                this.onCreate(null);
                return true;

            case R.id.contactUsMenu:
                SendMail.toContactUs(GeneralUserHomeScreen.this);
                return true;

            case R.id.reportBugMenu:
                // ContactUs.reportBug(AdminHomeScreen.this);
                FileChooser.chooseAndShareBugFile(GeneralUserHomeScreen.this, TAG);
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
        startActivity(new Intent(GeneralUserHomeScreen.this, CreateGeneralUser.class).putExtra(Constants.USER_TYPE, Constants.USER_TYPE_GENERAL)
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == requestUpdateCode) {
            switch (resultCode) {
                case ActivityResult.RESULT_IN_APP_UPDATE_FAILED:
                    new CustomSnackbar(GeneralUserHomeScreen.this, "Update Failed. Please try again", "", binding.rootContainer) {
                        @Override
                        public void onActionClick(View view) {

                        }
                    }.show();
                    break;
                case RESULT_CANCELED:
                    new CustomSnackbar(GeneralUserHomeScreen.this, "Update Cancelled. Please try again", "", binding.rootContainer) {
                        @Override
                        public void onActionClick(View view) {

                        }
                    }.show();
                    break;
                case RESULT_OK:
                    new CustomSnackbar(GeneralUserHomeScreen.this, "Successfully Updated", "", binding.rootContainer) {
                        @Override
                        public void onActionClick(View view) {

                        }
                    }.show();
                    break;

            }
        }

    }
}
