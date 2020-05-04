package io.github.isubham.astra.generalUser;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import io.github.isubham.astra.R;
import io.github.isubham.astra.databinding.CreateGeneralUserBinding;

public class CreateGeneralUser extends AppCompatActivity {

    private CreateGeneralUserBinding createGeneralUserBinding;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        createGeneralUserBinding = CreateGeneralUserBinding.inflate(getLayoutInflater());
        setContentView(createGeneralUserBinding.getRoot());

        findViewByIds();
        toolbarSetup();
        //showProgressBar();
        getBundleData();
        hideProgressBar();


    }


    public void getUserPic(View view) {
    }

    private void getBundleData() {
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
