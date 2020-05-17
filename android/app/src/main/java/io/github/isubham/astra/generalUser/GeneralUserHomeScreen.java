package io.github.isubham.astra.generalUser;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import io.github.isubham.astra.R;
import io.github.isubham.astra.tools.Constants;

public class GeneralUserHomeScreen extends AppCompatActivity {

    Button existing_user,register_user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.general_user_home_screen);
        existing_user = (Button)findViewById(R.id.button_existing_user);
        register_user = (Button)findViewById(R.id.button_register_user);
    }
    public void ExistingUser(View view){
        // switch to general user search page
        Intent i = new Intent(GeneralUserHomeScreen.this, GeneralUserSearchUser.class);
        startActivity(i);

    }

    public void RegisterUser(View view){
        Intent i = new Intent(GeneralUserHomeScreen.this, CreateGeneralUser.class);
        i.putExtra(Constants.USER_TYPE,Constants.USER_TYPE_GENERAL);
        i.putExtra(Constants.ID,);
        startActivity(i);

    }




}
