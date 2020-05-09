package io.github.isubham.astra.adminUser;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;

import java.util.Arrays;
import java.util.List;

import io.github.isubham.astra.R;
import io.github.isubham.astra.adapters.ViewPagerAdapter;
import io.github.isubham.astra.databinding.AdminVerifyDocBinding;
import io.github.isubham.astra.tools.Constants;

public class AdminVerifyDoc extends AppCompatActivity {

    private AdminVerifyDocBinding adminVerifyDocBinding;
    private ViewPagerAdapter pagerAdapter;
    private List<String> imageUrls;

    // For Slider Dots
    private int dotsCount;
    private ImageView[] dotImages;

    //dataFromServer

    //dataFromBundle
    private String userName, name;
    private String profilePicUrl;
    private String idFrontUrl, idBackUrl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adminVerifyDocBinding = AdminVerifyDocBinding.inflate(getLayoutInflater());
        setContentView(adminVerifyDocBinding.getRoot());

        setBundleData();

        setupPagerAdapter();
    }

    private void setBundleData() {

        Bundle b = getIntent().getExtras();
        if (b != null) {
            userName = b.getString(Constants.USER_NAME);
            profilePicUrl = b.getString(Constants.PROFILE_PIC_URL);
            name = b.getString(Constants.NAME);

            //set To Ui Elements
            imageUrls = Arrays.asList(b.getString(Constants.ID_FRONT_URL), b.getString(Constants.ID_BACK_URL));
            adminVerifyDocBinding.userName.setText(name);
            adminVerifyDocBinding.uniqueId.setText(userName);

            if (!TextUtils.isEmpty(profilePicUrl))
                Glide.with(this).load(profilePicUrl).centerCrop().into(adminVerifyDocBinding.profilePic);
        }

    }

    private void setupPagerAdapter() {

        //imageUrls = Arrays.asList("https://resize.indiatvnews.com/en/resize/newbucket/715_-/2019/04/pjimage-1-1556514034.jpg","https://www.mwallpapers.com/download-image/22526/576x768","https://st1.photogallery.ind.sh/wp-content/uploads/indiacom/anushka-sharma-flaunts-her-cleavage-in-hot-swimsuit-201610-1475585677-650x510.jpg","https://i.pinimg.com/originals/66/ce/4e/66ce4ee1543d9ffa2fb26a58a9acacd2.png","https://i.ytimg.com/vi/2eHyRLwQZmw/maxresdefault.jpg","https://www.quirkybyte.com/trendz/wp-content/uploads/sites/4/2018/09/Anushka-Sharma-flaunts-her-hot-boobs-1.jpg","https://i.pinimg.com/originals/7e/c6/99/7ec6995ab5b5cae7016eab0a4f24468e.jpg");
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


    public void loadProfilePic(View view) {
        if (profilePicUrl != null)
            Glide.with(this).load(profilePicUrl).centerCrop().into(adminVerifyDocBinding.profilePic);

    }
}
