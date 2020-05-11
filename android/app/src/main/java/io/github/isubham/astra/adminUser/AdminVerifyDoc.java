package io.github.isubham.astra.adminUser;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.github.isubham.astra.R;
import io.github.isubham.astra.adapters.ViewPagerAdapter;
import io.github.isubham.astra.databinding.AdminVerifyDocBinding;
import io.github.isubham.astra.model.Activity;
import io.github.isubham.astra.tools.CameraUtils;
import io.github.isubham.astra.tools.Constants;
import io.github.isubham.astra.tools.LoginPersistance;

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
        setLocation();
        hideProgressBar();

    }

    private void setLocation() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            OnGPS();
        } else {
            getLocation();
        }
    }

    private void OnGPS() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Enable GPS").setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
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

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(
                AdminVerifyDoc.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                AdminVerifyDoc.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        } else {
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
    }

    private void findViewById() {
        progressBar = findViewById(R.id.progressBar);

    }

    private void setBundleData() {

        if (getIntent().getExtras() != null) {
            userName = getIntent().getExtras().getString(Constants.USER_NAME);
            name = getIntent().getExtras().getString(Constants.NAME);
            personId = getIntent().getExtras().getInt(Constants.ID);
            setUI();

            // fetchUserForUserName(userName);
        }

    }

    private void setUI() {
        CameraUtils.setImage(binding.adminVerifyDocBackDoc, LoginPersistance.GetIdBack(AdminVerifyDoc.this));
        CameraUtils.setImage(binding.adminVerifyDocFrontDoc, LoginPersistance.GetIdFront(AdminVerifyDoc.this));
        CameraUtils.setImage(binding.profilePic, LoginPersistance.GetProfilePic(AdminVerifyDoc.this));
        binding.userName.setText(name);
        binding.uniqueId.setText(userName);
    }

    public void LogActivity(View view) throws JSONException {
        showProgressBar();
        String url = "https://aastra-stag.herokuapp.com/activity/";

        // TODO handle the in out
        Activity newActivity = new Activity(personId, latitude + "," + longitude, 1);

        try {

            JsonObjectRequest signUpRequest = new JsonObjectRequest(url,
                    new JSONObject(new Gson().toJson(newActivity)),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            hideProgressBar();
                            Toast.makeText(AdminVerifyDoc.this, "Saved", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    return super.getParams();
                }

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<>();
                    headers.put("Content-Type", "application/json");
                    headers.put("Authorization", "Basic " + LoginPersistance.GetToken(AdminVerifyDoc.this));
                    return headers;
                }

            };

            Volley.newRequestQueue(AdminVerifyDoc.this).add(signUpRequest);
        } catch (JsonSyntaxException e) {
            Log.e("error", "json exception");
        }
    }

    public void closeActivity(View view) {
        finish();
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
