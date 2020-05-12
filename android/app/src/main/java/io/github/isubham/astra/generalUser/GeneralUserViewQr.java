package io.github.isubham.astra.generalUser;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import io.github.isubham.astra.R;

public class GeneralUserViewQr extends AppCompatActivity {
    ImageView profilepic,qrcode;

    public static final String MyPREFERENCES = "MyPrefs" ;
//    public static final String username;
//    public static final String name = "subham";
//    public static final String dob = "03-03-1996";
//    public static final String father_name = "emailKey";

    SharedPreferences sharedpreferences;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout. activity_general_user_view_qr_code);
        profilepic = findViewById(R.id.general_user_view_qr_profile_pic);
        qrcode = findViewById(R.id.general_user_view_qr_placeholder);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        String username="default";
        String profile_pic;
        try {
           final JSONObject res = new JSONObject(getIntent().getStringExtra("response"));

            username = res.getString("username");
            profile_pic= res.getString("profile_pic");
            Log.e("item", "Example Item: " + res.getString("username"));
            Log.e("profile_pic", "Example Item: " + res.getString("profile_pic"));

        } catch (JSONException e) {
            e.printStackTrace();
        }//        editor.putString(username, <>);
         qrGenerator(qrcode,username);

        // code to show profile pic
//        byte[] decodedString = Base64.decode(res.getString("profile_pic"),Base64.NO_WRAP);
//        InputStream inputStream  = new ByteArrayInputStream(decodedString);
//        Bitmap bitmap  = BitmapFactory.decodeStream(inputStream);
//        profilepic.setImageBitmap(bitmap);
        // code to show QR code
    }
    void qrGenerator(ImageView v,String text){
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            BitMatrix bitMatrix = multiFormatWriter.encode(text, BarcodeFormat.QR_CODE,800,800);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            v.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }
}
