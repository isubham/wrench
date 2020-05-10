package io.github.isubham.astra.adminUser;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.github.isubham.astra.R;
import io.github.isubham.astra.adapters.ViewPagerAdapter;
import io.github.isubham.astra.databinding.AdminVerifyDocBinding;
import io.github.isubham.astra.model.GeneralUser;
import io.github.isubham.astra.tools.ApplicationController;
import io.github.isubham.astra.tools.CameraUtils;
import io.github.isubham.astra.tools.Constants;
import io.github.isubham.astra.tools.Endpoints;
import io.github.isubham.astra.tools.Errors;

public class AdminVerifyDoc extends AppCompatActivity {


    private String TAG = "AdminVerifyDoc";
    private AdminVerifyDocBinding adminVerifyDocBinding;
    private ViewPagerAdapter pagerAdapter;
    private List<String> imageUrls;
    private ProgressBar progressBar;


    // For Slider Dots
    private int dotsCount;
    private ImageView[] dotImages;

    //dataFromServer

    //dataFromBundle
    private String userName;

    private Gson gson;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adminVerifyDocBinding = AdminVerifyDocBinding.inflate(getLayoutInflater());
        setContentView(adminVerifyDocBinding.getRoot());

        imageUrls = new ArrayList<>();

        findViewById();
        setupPagerAdapter();
        setBundleData();

    }

    private void findViewById() {
        progressBar = findViewById(R.id.progressBar);

    }

    private void setBundleData() {

        if (getIntent().getExtras() != null) {
            userName = getIntent().getExtras().getString(Constants.USER_NAME);
            fetchUserForUserName(userName);
        }

    }

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
        adminVerifyDocBinding.userName.setText(generalUser.getFirst_name());
        adminVerifyDocBinding.uniqueId.setText(generalUser.getUsername());
        if (!TextUtils.isEmpty(generalUser.getProfile_pic())) {
            // Glide.with(this).load(generalUser.getProfile_pic()).centerCrop().into(adminVerifyDocBinding.profilePic);
            adminVerifyDocBinding.profilePic.setImageBitmap(CameraUtils.getBitmapFromBase64ImageString(generalUser.getProfile_pic()));
        }


        imageUrls = Arrays.asList(generalUser.getId_front(), generalUser.getId_back());
        pagerAdapter.notifyDataSetChanged();


    }


    private void setupPagerAdapter() {

        pagerAdapter = new ViewPagerAdapter(this, imageUrls);
        adminVerifyDocBinding.docViewer.setAdapter(pagerAdapter);
        // slider dots configuration

        dotsCount = pagerAdapter.getCount();
        dotImages = new ImageView[dotsCount];

        for (int i = 0; i < dotsCount; i++) {
            dotImages[i] = new ImageView(this);
            dotImages[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.non_active_dot));


            // setting layout params
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(0, 0, 0, 0);
            adminVerifyDocBinding.sliderDots.addView(dotImages[i]);
        }
        // By Default active dot will be as 0th if images are not null
        if (imageUrls.size() > 0)
            dotImages[0].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot));

        setupSliderMovement();
    }

    private void setupSliderMovement() {
        adminVerifyDocBinding.docViewer.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
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

    public void verifyUser(View view) {
    }

    public void closeActivity(View view) {
        finish();
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
