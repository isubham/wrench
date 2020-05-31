package com.pitavya.astra.astra_admin.adminUser;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.pitavya.astra.astra_admin.R;
import com.pitavya.astra.astra_admin.databinding.AdminVerifyDocBinding;
import com.pitavya.astra.astra_common.model.CreateLog;
import com.pitavya.astra.astra_common.tools.CameraUtils;
import com.pitavya.astra.astra_common.tools.Constants;
import com.pitavya.astra.astra_common.tools.Endpoints;
import com.pitavya.astra.astra_common.tools.Errors;
import com.pitavya.astra.astra_common.tools.Headers;
import com.pitavya.astra.astra_common.tools.LoginPersistance;
import com.pitavya.astra.astra_common.tools.ResponseCode;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.pitavya.astra.astra_admin.adapters.ViewPagerAdapter;


public class AdminVerifyDoc extends AppCompatActivity {


    private static final int REQUEST_LOCATION = 1;
    LocationManager locationManager;
    String latitude, longitude;
    int personId;
    private String TAG = "AdminVerifyDoc";
    private AdminVerifyDocBinding binding;
    private ViewPagerAdapter pagerAdapter;

    //dataFromServer
    private List<String> imageUrls;
    private ProgressBar progressBar;
    // For Slider Dots
    private int dotsCount;
    private ImageView[] dotImages;
    //dataFromBundle
    private String userName;
    private String name;
    private int action_in_out;

    private Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = AdminVerifyDocBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        imageUrls = new ArrayList<>();

        findViewById();
        // setupPagerAdapter();
        setBundleData();
        requestLocationPermission();
        hideProgressBar();

    }


    private boolean requestLocationPermission() {
        final boolean[] granted = {false};
        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            granted[0] = true;
                        }
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            // show alert dialog navigating to Settings
                            showSettingsDialog();
                            granted[0] = false;

                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).
                withErrorListener(new PermissionRequestErrorListener() {
                    @Override
                    public void onError(DexterError error) {
                        Toast.makeText(getApplicationContext(), "Error occurred , while acquiring permission .", Toast.LENGTH_SHORT).show();
                    }
                })
                .onSameThread()
                .check();

        return granted[0];
    }

    private void showSettingsDialog() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Need Permissions").
                setMessage("This app needs permission to use this feature . You can enable them in settings").setCancelable(false).setPositiveButton("SETTINGS", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

    @SuppressLint("MissingPermission")
    private void getLocation() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        assert locationManager != null;
        Location locationGPS = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (locationGPS != null) {
            double lat = locationGPS.getLatitude();
            double longi = locationGPS.getLongitude();
            latitude = String.valueOf(lat);
            longitude = String.valueOf(longi);
        } else {
            Toast.makeText(this, "Unable to find location.", Toast.LENGTH_SHORT).show();
        }
    }


    private void findViewById() {
        progressBar = findViewById(R.id.progressBar);

    }

    private void setBundleData() {

        if (getIntent().getExtras() != null) {
            userName = getIntent().getExtras().getString(Constants.USER_NAME);
            name = getIntent().getExtras().getString(Constants.NAME);
            personId = getIntent().getExtras().getInt(Constants.ID);
            action_in_out = getIntent().getExtras().getInt(Constants.ACTION_IN_OUT);

            setUI();

            // fetchUserForUserName(userName);
        }

    }

    private void setUI() {
        binding.adminVerifyDocBackDoc.setImageBitmap(CameraUtils.getBitmapFromBase64ImageString(LoginPersistance.GetIdBack(AdminVerifyDoc.this)));
        binding.adminVerifyDocFrontDoc.setImageBitmap(CameraUtils.getBitmapFromBase64ImageString(LoginPersistance.GetIdFront(AdminVerifyDoc.this)));
        binding.profilePic.setImageBitmap(CameraUtils.getBitmapFromBase64ImageString(LoginPersistance.GetProfilePic(AdminVerifyDoc.this)));

        binding.userName.setText(name);
        binding.uniqueId.setText(userName);
    }

    public void LogActivity(View view) throws JSONException {
        if (!requestLocationPermission())
            return;

        getLocation();

        showProgressBar();
        CreateLog newCreateLog = new CreateLog(personId, latitude + "," + longitude, action_in_out, !TextUtils.isEmpty(binding.purpose.getText()) ? binding.purpose.getText().toString() : Constants.EMPTY_STRING);
        try {

            JsonObjectRequest createLogRequest = new JsonObjectRequest(Endpoints.CREATE_LOG,
                    new JSONObject(new Gson().toJson(newCreateLog)),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            hideProgressBar();

                            if (response.optString(Constants.MESSAGE).equals(Constants.TOKEN_INVALID)) {
                                Toast.makeText(AdminVerifyDoc.this, Constants.TOKEN_EXPIRED, Toast.LENGTH_SHORT).show();
                                return;
                            }
                            parseCreateLogResponse(response);

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            hideProgressBar();
                            Errors.handleVolleyError(error, TAG, AdminVerifyDoc.this);
                        }
                    }) {

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<>();
                    headers.put(Headers.CONTENT_TYPE, Headers.APPLICATION_JSON);
                    headers.put(Headers.AUTHORIZATION, "Basic " + LoginPersistance.GetToken(AdminVerifyDoc.this));
                    return headers;
                }

            };

            createLogRequest.setRetryPolicy(new DefaultRetryPolicy(
                    10000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
            ));

            Volley.newRequestQueue(AdminVerifyDoc.this).add(createLogRequest);
        } catch (JsonSyntaxException e) {
            Log.e("error", "json exception");
        }
    }

    private void parseCreateLogResponse(JSONObject response) {

        if (response.optString(Constants.CODE).equals(ResponseCode.CREATE_LOG_SUCCESS_CODE)) {
            Toast.makeText(AdminVerifyDoc.this, Constants.VERIFICATION_SUCCESSFUL, Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, Constants.TRY_AGAIN, Toast.LENGTH_LONG).show();
        }

        //producing a delay of 2sec so that result is viewable
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }, Constants.DELAYED_CLOSE_FOR_RESULT);

    }


    /*
    *
    *
     private void fetchUserForUserName(String userName) {
        showProgressBar();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, Endpoints.SEARCH_BY_USER_NAME + userName, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                hideProgressBar();
                //parse Response
                if (!String.valueOf(response).equals(Constants.EMPTY_JSON)) {
                    parseResponseAndSetUi(String.valueOf(response));
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideProgressBar();
                Errors.handleVolleyError(error, TAG, AdminVerifyDoc.this);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Basic eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpZCI6Mn0.1HCAwj7aXeFFAjUJXDATBBUYsWy2-8c01chWoISVPP4");
                return headers;
            }
        };

        ApplicationController.getInstance().addToRequestQueue(request);

    }


    private void parseResponseAndSetUi(String response) {
        gson = new Gson();
        GeneralUser generalUser = gson.fromJson(response, GeneralUser.class);

        //set To Ui Elements
        binding.userName.setText(generalUser.getFirst_name());
        binding.uniqueId.setText(generalUser.getUsername());
        if (!TextUtils.isEmpty(generalUser.getProfile_pic())) {
            // Glide.with(this).load(generalUser.getProfile_pic()).centerCrop().into(adminVerifyDocBinding.profilePic);

            binding.profilePic.setImageBitmap(CameraUtils.getBitmapFromBase64ImageString(generalUser.getProfile_pic()));

        }


        imageUrls = Arrays.asList(generalUser.getId_front(), generalUser.getId_back());
        pagerAdapter.notifyDataSetChanged();


    }

    private void setupPagerAdapter() {

        pagerAdapter = new ViewPagerAdapter(this, imageUrls);
        binding.docViewer.setAdapter(pagerAdapter);
        // slider dots configuration

        dotsCount = pagerAdapter.getCount();
        dotImages = new ImageView[dotsCount];

        for (int i = 0; i < dotsCount; i++) {
            dotImages[i] = new ImageView(this);
            dotImages[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.non_active_dot));


            // setting layout params
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(0, 0, 0, 0);
            binding.sliderDots.addView(dotImages[i]);
        }
        // By Default active dot will be as 0th if images are not null
        if (imageUrls.size() > 0)
            dotImages[0].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot));

        setupSliderMovement();
    }

    private void setupSliderMovement() {
        binding.docViewer.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {

                for (int i = 0; i < dotsCount; i++)
                    dotImages[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.non_active_dot));

                dotImages[position].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot));
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }


    * */
    public void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);


    }

    public void hideProgressBar() {
        progressBar.setVisibility(View.INVISIBLE);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

}
