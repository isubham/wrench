package com.pitavya.astra.astra_common;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.pitavya.astra.astra_common.databinding.GeneralUserViewQrBinding;
import com.pitavya.astra.astra_common.tools.CameraUtils;
import com.pitavya.astra.astra_common.tools.Constants;
import com.pitavya.astra.astra_common.tools.LoginPersistance;
import com.pitavya.astra.astra_common.tools.ScreenshotPreventor;


public class GeneralUserViewQr extends AppCompatActivity {
    private GeneralUserViewQrBinding binding;
    private String userName;
    private String userType;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ScreenshotPreventor.preventScreenshot(GeneralUserViewQr.this);
        binding = GeneralUserViewQrBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        toolbarSetup();
        //setProfilepic();
        setBundleData();

    }

    private void setProfilepic() {
        if (LoginPersistance.GetProfilePic(this) != null && LoginPersistance.GetGeneralUserName(this).equals(userName)) {
            binding.profilePic.setImageBitmap(CameraUtils.getBitmapFromBase64ImageString(LoginPersistance.GetProfilePic(this)));

        } else {
            binding.profilePic.setImageBitmap(CameraUtils.getBitmapFromBase64ImageString(LoginPersistance.GetIGeneralProfilePic(this)));

        }
    }

    private void setBundleData() {
        if (getIntent().getExtras() != null) {

            String userName = getIntent().getExtras().getString(Constants.USER_NAME);
            int userType = getIntent().getExtras().getInt(Constants.USER_TYPE);

            if (LoginPersistance.GetIGeneralUserName(this) != null && LoginPersistance.GetIGeneralUserName(this).equals(userName)) {
                formQrCode(LoginPersistance.GetIGeneralUserName(this));
                binding.username.setText(LoginPersistance.GetIGeneralUserName(this));
                binding.profilePic.setImageBitmap(CameraUtils.getBitmapFromBase64ImageString(LoginPersistance.GetIGeneralProfilePic(this)));

            } else {
                binding.profilePic.setImageBitmap(CameraUtils.getBitmapFromBase64ImageString(LoginPersistance.GetProfilePic(this)));
                binding.username.setText(userName);
                formQrCode(userName);

            }
            showHideSaveButton(userType);
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        LoginPersistance.Iupdate(null, null, null, null, null, GeneralUserViewQr.this);
    }

    private void showHideSaveButton(int userType) {


        if (userType == Constants.USER_TYPE_ADMIN || LoginPersistance.GetGeneralUserName(this) != null) {
            binding.saveqr.setVisibility(View.GONE);
            if (userType == Constants.USER_TYPE_ADMIN) {
                binding.usernameCopyBt.setVisibility(View.VISIBLE);
            }
        }

    }

    public void saveQR(View v) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Is it your QR. Save It ?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        LoginPersistance.update(LoginPersistance.GetIGeneralUserName(GeneralUserViewQr.this),
                                LoginPersistance.GetIGeneralUserToken(GeneralUserViewQr.this), LoginPersistance.GetIGeneralProfilePic(GeneralUserViewQr.this),
                                LoginPersistance.GetIIdFront(GeneralUserViewQr.this), LoginPersistance.GetIIdBack(GeneralUserViewQr.this),
                                GeneralUserViewQr.this);
                      //  LoginPersistance.Iupdate(null, null, null, null, null, GeneralUserViewQr.this);
                        binding.saveqr.setVisibility(View.GONE);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void formQrCode(String userName) {
        qrGenerator(userName);
    }

    private void qrGenerator(String text) {
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            BitMatrix bitMatrix = multiFormatWriter.encode(text, BarcodeFormat.QR_CODE, 800, 800);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            binding.qrCode.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    // app bar

    private void toolbarSetup() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            toolbar.setTitleTextColor(getResources().getColor(R.color.colorPrimaryDark));
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    public void copyText(View v) {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("TextView", binding.username.getText().toString());
        binding.username.setTextColor(getColor(R.color.colorPrimaryDark));
        clipboard.setPrimaryClip(clip);
        Toast.makeText(this, "Copied username: " + binding.username.getText().toString(), Toast.LENGTH_SHORT).show();
    }
}
