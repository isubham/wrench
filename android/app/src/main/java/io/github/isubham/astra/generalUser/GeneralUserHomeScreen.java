package io.github.isubham.astra.generalUser;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import io.github.isubham.astra.R;
import io.github.isubham.astra.adminUser.AdminSignIn;
import io.github.isubham.astra.tools.Constants;
import io.github.isubham.astra.tools.LoginPersistance;

public class GeneralUserHomeScreen extends AppCompatActivity {

    Button existing_user,register_user;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.general_user_home_screen);
        existing_user = (Button)findViewById(R.id.button_existing_user);
        register_user = (Button)findViewById(R.id.button_register_user);


        findViewByIds();
        toolbarSetup();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.general_user_home_screen_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.myqr:
                if(LoginPersistance.GetGeneralUserName(this)!=null) {
                    startActivity(new Intent(this, GeneralUserViewQr.class).putExtra(Constants.USER_TYPE, Constants.USER_TYPE_ADMIN));
                    return true;
                }
                else{
                    Toast.makeText(this, "Please Save your QR Code by searching through existing user", Toast.LENGTH_LONG).show();
                    return true;
                }
            case R.id.logout:
                LoginPersistance.update(null,null,null,null,null,this);
//                sendStatusForLogout();
//                Intent toSignInWithoutHistory = new Intent(this, AdminSignIn.class);
//                startActivity(toSignInWithoutHistory);
//                finishAffinity();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }


    public void ExistingUser(View view){
        // switch to general user search page
//        showProgressBar();
        Intent i = new Intent(GeneralUserHomeScreen.this, GeneralUserSearchUser.class);
        startActivity(i);
//        hideProgressBar();

    }

    public void RegisterUser(View view){
//        showProgressBar();
        Intent i = new Intent(GeneralUserHomeScreen.this, CreateGeneralUser.class);
        i.putExtra(Constants.USER_TYPE,Constants.USER_TYPE_GENERAL);
        startActivity(i);
//        hideProgressBar();
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
