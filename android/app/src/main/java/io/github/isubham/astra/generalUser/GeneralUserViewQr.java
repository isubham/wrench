package io.github.isubham.astra.generalUser;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import io.github.isubham.astra.R;

public class GeneralUserViewQr extends AppCompatActivity {
    ImageView profilepic,qrcode;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout. activity_general_user_view_qr_code);
        profilepic = findViewById(R.id.profile_pic);
        qrcode = findViewById(R.id.qr_placeholder);


        // code to show profile pic

        // code to show QR code
    }
}
