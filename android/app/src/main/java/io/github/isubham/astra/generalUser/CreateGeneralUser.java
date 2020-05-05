package io.github.isubham.astra.generalUser;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.io.File;

import io.github.isubham.astra.R;
import io.github.isubham.astra.databinding.CreateGeneralUserBinding;
import io.github.isubham.astra.tools.CameraUtils;
import io.github.isubham.astra.tools.Constants;

public class CreateGeneralUser extends AppCompatActivity {

    private static String imageStoragePath = Constants.EMPTY_STRING;
    private CreateGeneralUserBinding createGeneralUserBinding;
    private ProgressBar progressBar;
    //camera related
    private Bitmap bitmap, bitmap_front_doc, bitmap_back_doc, bitmap_profile_pic;
    private Uri fileUri;
    private int powerOf2;

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
        if (CameraUtils.checkPermissions(CreateGeneralUser.this))
            captureImage(Constants.PROFILE_PIC);
        else
            showPermissionsAlert();
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

    public void saveGeneralUser(View view) {

        //bitmap_front_doc, bitmap_back_doc, bitmap_profile_pic    has the updated value for respective images in view

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
                createGeneralUserBinding.frontDoc.setRotation(90);
                createGeneralUserBinding.frontDoc.setImageBitmap(bitmap);
                bitmap_front_doc = bitmap;
            } else if (fileUri.toString().contains(Constants.BACK_DOC)) {
                createGeneralUserBinding.backDoc.setRotation(90);
                createGeneralUserBinding.backDoc.setImageBitmap(bitmap);
                bitmap_back_doc = bitmap;
            } else {
                // for ProfilePic
                createGeneralUserBinding.profilePic.setRotation(-90);
                createGeneralUserBinding.profilePic.setImageBitmap(bitmap);
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
}
