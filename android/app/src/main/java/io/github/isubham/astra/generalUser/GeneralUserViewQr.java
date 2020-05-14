package io.github.isubham.astra.generalUser;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import io.github.isubham.astra.databinding.GeneralUserViewQrBinding;
import io.github.isubham.astra.tools.CameraUtils;
import io.github.isubham.astra.tools.Constants;
import io.github.isubham.astra.tools.LoginPersistance;

public class GeneralUserViewQr extends AppCompatActivity {
    private GeneralUserViewQrBinding binding;

    private String userName;
    private String userType;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = GeneralUserViewQrBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.profilePic.setImageBitmap(CameraUtils.getBitmapFromBase64ImageString(LoginPersistance.GetProfilePic(this)));
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
            formQrCode(userName);
            showHideSaveButton(userType);
        }

    }

    private void showHideSaveButton(int userType) {
        if (userType == (Constants.USER_TYPE_GENERAL)) {
            //   binding.buttonId.setVisibility(View.INVISIBLE);
        }
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


}
