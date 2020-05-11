package io.github.isubham.astra.tools;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import io.github.isubham.astra.BuildConfig;

public class CameraUtils {


    /**
     * Refreshes gallery on adding new image/video. Gallery won't be refreshed
     * on older devices until device is rebooted
     */
    public static void refreshGallery(Context context, String filePath) {
        // ScanFile so it will be appeared on Gallery
        MediaScannerConnection.scanFile(context,
                new String[]{filePath}, null,
                new MediaScannerConnection.OnScanCompletedListener() {
                    public void onScanCompleted(String path, Uri uri) {
                    }
                });
    }

    public static boolean checkPermissions(Context context) {
        return ActivityCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }


    public static Bitmap getBitMapFromUri(Context context, Uri fileUri) {
        Bitmap bitmap = null;

        if (fileUri != null) {
            try {
                bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), fileUri);
            } catch (IOException e) {
                Log.e("CameraUtils :", "getBitMapFromUri: Exception" + e.toString());
            }
        }


        return bitmap;
    }

    /**
     * Checks whether device has camera or not. This method not necessary if
     * android:required="true" is used in manifest file
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static boolean isDeviceSupportCamera(Context context) {
        // this device has a camera
        // no camera on this device
        return context.getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA_ANY);
    }

    /**
     * Open device app settings to allow user to enable permissions
     */
    public static void openSettings(Context context) {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.fromParts("package", BuildConfig.APPLICATION_ID, null));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static Uri getOutputMediaFileUri(Context context, File file) {
        return FileProvider.getUriForFile(context, context.getPackageName() + ".provider", file);
    }

    /**
     * Creates and returns the image or video file before opening the camera
     * create file directory , path , name formation
     */
    public static File getOutputMediaFile(int type, String side) {

        // External sdcard location
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                Constants.GALLERY_DIRECTORY_NAME);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.e(Constants.GALLERY_DIRECTORY_NAME, "Oops! Failed to create "
                        + Constants.GALLERY_DIRECTORY_NAME + " directory");
                return null;
            }
        }

        // Preparing media file naming convention
        // adds timestamp
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile;
        if (type == Constants.MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + side + "_" + timeStamp + "." + Constants.IMAGE_EXTENSION);
        } else {
            return null;
        }

        return mediaFile;
    }

    public static float megaBytesFree() {

        final Runtime rt = Runtime.getRuntime();
        final float bytesUsed = rt.totalMemory();
        final float mbUsed = bytesUsed / Constants.BYTES_IN_MB;
        final float mbFree = megaBytesAvailable() - mbUsed;
        return mbFree;

    }

    private static float megaBytesAvailable() {

        final Runtime rt = Runtime.getRuntime();
        final float bytesAvailable = rt.maxMemory();

        return bytesAvailable / Constants.BYTES_IN_MB;

    }


    public static float readBitmapInfo(int sampleSize, String filePath) {
        // bitmap factory
        BitmapFactory.Options options = new BitmapFactory.Options();

        // downsizing image as it throws OutOfMemory Exception for larger
        // images
        options.inJustDecodeBounds = true;
        options.inSampleSize = sampleSize;

        //Check for the below statement use case
        BitmapFactory.decodeFile(filePath, options);

        final float imageHeight = options.outHeight;
        final float imageWidth = options.outWidth;
        final String imageMimeType = options.outMimeType;

        BitmapFactory.decodeFile(filePath, options);

        Log.d("CameraUtils: ", "readBitmapInfo() -> Scale before load - w : h : type " + imageWidth + ":" + imageHeight + ":" + imageMimeType);
        Log.d("CameraUtils: ", "estimated Memory required to load" + imageWidth * imageHeight * Constants.BYTES_PER_PX / Constants.BYTES_IN_MB);


        return imageWidth * imageHeight * Constants.BYTES_PER_PX / Constants.BYTES_IN_MB;
    }

    /**
     * Downsizing the bitmap to avoid OutOfMemory exceptions
     */
    public static Bitmap optimizeBitmap(int sampleSize, String filePath) {
        // bitmap factory
        BitmapFactory.Options options = new BitmapFactory.Options();

        // downsizing image as it throws OutOfMemory Exception for larger
        // images
        options.inSampleSize = sampleSize;

        return BitmapFactory.decodeFile(filePath, options);
    }


    /* TODO Image Conversion Stuff*/
    public static String getBase64StringFromBitmap(Bitmap bitmap, int quality) {
        /* Bitmap Image is converted to byte Array  here   */

        if (bitmap != null) {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
            byte[] imageBytes = outputStream.toByteArray();
            return Base64.encodeToString(imageBytes, Base64.DEFAULT);
        }
        return Constants.EMPTY_STRING;
    }

    public static Bitmap getBitmapFromBase64ImageString(String base64Image) {
        /*Base64 String is converted to Bitmap Image here   */
        byte[] decodedString = Base64.decode(base64Image, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    }

    /* TODO Image Conversion Stuff Over*/

    public static void setImage(ImageView imageView, String encodedImage) {
        imageView.setImageBitmap(getBitmapFromBase64ImageString(encodedImage));
    }

}