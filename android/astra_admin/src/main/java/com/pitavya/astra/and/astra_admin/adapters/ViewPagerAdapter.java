package com.pitavya.astra.android.astra_admin.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.pitavya.astra.astra_common.tools.CameraUtils;

import java.util.List;


public class ViewPagerAdapter extends PagerAdapter {

    private Context ctx;
    private List<String> imageUrls;

    public ViewPagerAdapter(Context ctx, List<String> imageUrls) {
        this.ctx = ctx;
        this.imageUrls = imageUrls;
    }

    @Override
    public int getCount() {
        return imageUrls.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        ImageView imageView = new ImageView(ctx);
        //Glide.with(ctx).load(imageUrls.get(position)).centerCrop().into(imageView);
        imageView.setImageBitmap(CameraUtils.getBitmapFromBase64ImageString(imageUrls.get(position)));
        Toast.makeText(ctx, "" + imageUrls.get(position), Toast.LENGTH_SHORT).show();
        container.addView(imageView);
        return imageView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

//    @Override
//    public int getItemPosition(@NonNull Object object) {
//        return POSITION_NONE;
//    }
}
