package io.github.isubham.astra.generalUser;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.github.isubham.astra.R;
import io.github.isubham.astra.databinding.CreateGeneralUserBinding;
import io.github.isubham.astra.model.GeneralUser;
import io.github.isubham.astra.tools.ApplicationController;
import io.github.isubham.astra.tools.CameraUtils;
import io.github.isubham.astra.tools.Constants;
import io.github.isubham.astra.tools.Endpoints;
import io.github.isubham.astra.tools.Errors;
import io.github.isubham.astra.tools.Headers;

public class CreateGeneralUser extends AppCompatActivity {

    public static final String TAG = "CreateGeneralUser";

    private static String imageStoragePath = Constants.EMPTY_STRING;
    private CreateGeneralUserBinding binding;
    private ProgressBar progressBar;
    //camera related
    private Bitmap bitmap, bitmap_front_doc, bitmap_back_doc, bitmap_profile_pic;
    private Uri fileUri;
    private int powerOf2;

    //Data From Bundle
    private String createdById;

    private Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = CreateGeneralUserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        findViewByIds();
        toolbarSetup();
        //showProgressBar();
        setBundleData();

        //check For Permission
        if (!CameraUtils.checkPermissions(CreateGeneralUser.this))
            requestCameraPermission();


        hideProgressBar();

    }

    /**
     * Requesting permissions using Dexter library
     */
    private void requestCameraPermission() {
        Dexter.withActivity(this)
                .withPermissions(Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        //if (report.areAllPermissionsGranted()) {} else
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            showPermissionsAlert();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }

    public void getUserPic(View view) {
        if (CameraUtils.checkPermissions(CreateGeneralUser.this))
            captureImage(Constants.PROFILE_PIC);
        else
            showPermissionsAlert();
    }

    private void setBundleData() {
        Bundle b = getIntent().getExtras();
        if (b != null) {
            createdById = b.getString(Constants.USER_TYPE);
        }
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

    public void frontDocScan(View view) {
        if (CameraUtils.checkPermissions(CreateGeneralUser.this))
            captureImage(Constants.FRONT_DOC);
        else
            showPermissionsAlert();
    }

    public void backDocScan(View view) {
        if (CameraUtils.checkPermissions(CreateGeneralUser.this))
            captureImage(Constants.BACK_DOC);
        else
            showPermissionsAlert();
    }

    /*TODO For Camera Stuff ***/

    /**
     * Alert dialog to navigate to app settings
     * to enable necessary permissions
     */
    private void showPermissionsAlert() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Permissions required!")
                .setMessage("Camera needs few permissions to work properly. Grant them in settings.")
                .setPositiveButton("GOTO SETTINGS", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        CameraUtils.openSettings(CreateGeneralUser.this);
                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }

    /**
     * Capturing Camera Image will launch camera app requested image capture
     */
    private void captureImage(String side) {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        File file = CameraUtils.getOutputMediaFile(Constants.MEDIA_TYPE_IMAGE, side);
        if (file != null) {
            imageStoragePath = file.getAbsolutePath();
            Log.i("Image Storage path", imageStoragePath);
        }

        fileUri = CameraUtils.getOutputMediaFileUri(getApplicationContext(), file);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

        // start the image capture Intent
        startActivityForResult(intent, Constants.CAMERA_CAPTURE_IMAGE_REQUEST_CODE);

    }

    /**
     * Activity result method will be called after closing the camera
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // if the result is capturing Image
        if (requestCode == Constants.CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // Refreshing the gallery
                CameraUtils.refreshGallery(getApplicationContext(), imageStoragePath);
                Log.e("ImageStoragePath", imageStoragePath);

                // process And View The Image
                previewCapturedImage(imageStoragePath, fileUri);

            } else if (resultCode == RESULT_CANCELED) {
                // user cancelled Image capture
                Toast.makeText(getApplicationContext(),
                        "User cancelled image capture", Toast.LENGTH_SHORT)
                        .show();
            } else {
                // failed to capture image
                Toast.makeText(CreateGeneralUser.this,
                        "Sorry! Failed to capture image. Try Again!!", Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }

    /**
     * Display image from gallery
     */
    private void previewCapturedImage(String imageStoragePath, Uri fileUri) {
        try {
            // For Bulk Load this should be done in additional Service
            bitmap = CameraUtils.optimizeBitmap(Constants.BITMAP_SAMPLE_SIZE, imageStoragePath);
            // image_bitmap = CameraUtils.getBitMapFromUri(this, uri);   //just another way to use fileUri to bitmap
            loadImage(imageStoragePath, bitmap, fileUri);

        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    private void loadImage(String imageStoragePath, Bitmap bitmap, Uri fileUri) {
        if (CameraUtils.readBitmapInfo(Constants.BITMAP_SAMPLE_SIZE, imageStoragePath) > CameraUtils.megaBytesFree()) {
            bitmap = subSampleBitmap(32, imageStoragePath);
        }

        if (bitmap != null) {
            if (fileUri.toString().contains(Constants.FRONT_DOC)) {
                binding.frontDoc.setRotation(90);
                binding.frontDoc.setImageBitmap(bitmap);
                bitmap_front_doc = bitmap;
            } else if (fileUri.toString().contains(Constants.BACK_DOC)) {
                binding.backDoc.setRotation(90);
                binding.backDoc.setImageBitmap(bitmap);
                bitmap_back_doc = bitmap;
            } else {
                // for ProfilePic
                binding.profilePic.setRotation(-90);
                binding.profilePic.setImageBitmap(bitmap);
                bitmap_profile_pic = bitmap;
            }
            clearGlobalsForNextUse();
        }
    }

    private void clearGlobalsForNextUse() {
        bitmap = null;
        imageStoragePath = Constants.EMPTY_STRING;
        fileUri = null;
    }

    private Bitmap subSampleBitmap(int optionSize, String imageStoragePath) {
        if (powerOf2 < 1 || powerOf2 > 32) {
            return null;
        }

        final Resources res = this.getResources();
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = false;
        options.inSampleSize = powerOf2;

        return BitmapFactory.decodeFile(imageStoragePath, options);

    }

    /* TODO Camera Stuff Over */

    public void saveGeneralUser(View view) {

        //bitmap_front_doc, bitmap_back_doc, bitmap_profile_pic  ,createdById  has the updated value for respective images in view
        GeneralUser generalUser = new GeneralUser(CameraUtils.getBase64StringFromBitmap(bitmap_profile_pic, Constants.HIGH_QUALITY), String.valueOf(binding.userName.getText()), String.valueOf(binding.fullName.getText()),
                String.valueOf(binding.fatherName.getText()), String.valueOf(binding.email.getText()), String.valueOf(binding.dob.getText()), String.valueOf(binding.contact.getText()),
                String.valueOf(binding.aadhar.getText()), String.valueOf(binding.address.getText()), String.valueOf(binding.pincode.getText()), CameraUtils.getBase64StringFromBitmap(bitmap_front_doc,
                Constants.HIGH_QUALITY), CameraUtils.getBase64StringFromBitmap(bitmap_back_doc, Constants.HIGH_QUALITY), createdById);

        gson = new Gson();
        String generalUserJson = gson.toJson(generalUser);
        try {
            apiRequestToSaveGeneralUser(new JSONObject(generalUserJson));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void apiRequestToSaveGeneralUser(JSONObject generalUserJson) {
        showProgressBar();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, Endpoints.CREATE_GENERAL_USER, generalUserJson, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                hideProgressBar();
                Toast.makeText(CreateGeneralUser.this, "" + response.toString(), Toast.LENGTH_SHORT).show();
                Log.e("response", response.toString());

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideProgressBar();
                Errors.handleVolleyError(error, TAG, CreateGeneralUser.this);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<>();
                headers.put(Headers.CONTENT_TYPE, Headers.APPLICATION_JSON);
                return headers;
            }
        };

        ApplicationController.getInstance().addToRequestQueue(request);

    }


}
