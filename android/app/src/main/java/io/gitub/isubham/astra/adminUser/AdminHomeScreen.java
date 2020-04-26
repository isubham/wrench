package io.gitub.isubham.astra.adminUser;

import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import io.gitub.isubham.astra.R;

public class AdminHomeScreen extends AppCompatActivity {

    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home_screen);

        //General Ids
        findViewByIds();
        //ProgressBar
        showProgressBar();
        //get the Data from Previous screen before setting things up .
        getBundleData();
        //To setup tool bar
        toolbarSetup();


    }

    private void toolbarSetup() {

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("ASTRA");
            toolbar.setTitleTextColor(getResources().getColor(R.color.appbarTextColor));
        }
    }

    /**
     * Get BundleData From Previous Screen
     **/
    private void getBundleData() {
    }

    private void findViewByIds() {
        progressBar = findViewById(R.id.progressBar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
         super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.admin_home_screen_menu, menu);
        return true;
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
