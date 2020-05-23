package io.github.isubham.astra.generalUser;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;

import android.view.View;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import io.github.isubham.astra.R;
import io.github.isubham.astra.databinding.GeneralUserViewQrBinding;
import io.github.isubham.astra.tools.CameraUtils;
import io.github.isubham.astra.tools.Constants;
import io.github.isubham.astra.tools.LoginPersistance;

public class GeneralUserViewQr extends AppCompatActivity {
    private GeneralUserViewQrBinding binding;
    private ProgressBar progressBar;
    private String userName;
    private String userType;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = GeneralUserViewQrBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        findViewByIds();
//        toolbarSetup();
        if(LoginPersistance.GetProfilePic(this)!=null) {
            binding.profilePic.setImageBitmap(CameraUtils.getBitmapFromBase64ImageString(LoginPersistance.GetProfilePic(this)));

        }
        else{
            binding.profilePic.setImageBitmap(CameraUtils.getBitmapFromBase64ImageString(LoginPersistance.GetIGeneralProfilePic(this)));

        }
        setBundleData();

    }



//
//    @Override
//    protected void onResume() {
//        super.onResume();
//
//        binding.profilePic.setImageBitmap(CameraUtils.getBitmapFromBase64ImageString(LoginPersistance.GetProfilePic(this)));
//        setBundleData();
//    }

    private void setBundleData() {
        if (getIntent().getExtras() != null) {

            String userName = getIntent().getExtras().getString(Constants.USER_NAME);
            int userType = getIntent().getExtras().getInt(Constants.USER_TYPE);

//            formQrCode(userName);

            if(LoginPersistance.GetGeneralUserName(this)!=null) {
                formQrCode(LoginPersistance.GetGeneralUserName(this));
            }
            else{
                formQrCode(userName);
            }
            assert userType != Constants.USER_TYPE_GENERAL;

            showHideSaveButton(userType);
        }

    }

    private void showHideSaveButton(int userType) {


        if (userType==Constants.USER_TYPE_ADMIN || LoginPersistance.GetGeneralUserName(this)!=null){
               binding.saveqr.setVisibility(View.INVISIBLE);
        }
    }
    public void saveQR(View v){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure this is your QR You want to SAVE?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        LoginPersistance.update(LoginPersistance.GetIGeneralUserName(GeneralUserViewQr.this),LoginPersistance.GetIGeneralUserToken(GeneralUserViewQr.this),LoginPersistance.GetIGeneralProfilePic(GeneralUserViewQr.this),LoginPersistance.GetIdFront(GeneralUserViewQr.this),LoginPersistance.GetIdBack(GeneralUserViewQr.this),GeneralUserViewQr.this);
                        binding.saveqr.setVisibility(View.INVISIBLE);
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
