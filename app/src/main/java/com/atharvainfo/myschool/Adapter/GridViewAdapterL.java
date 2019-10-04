package com.atharvainfo.myschool.Adapter;

import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.atharvainfo.myschool.R;
import com.atharvainfo.myschool.others.ImageItem;
import com.google.android.gms.analytics.ecommerce.Product;
import com.squareup.picasso.Picasso;
import com.atharvainfo.myschool.Adapter.GridItem;

import java.util.ArrayList;
import java.util.List;

public class GridViewAdapterL extends BaseAdapter {

    private Context mContext;
    private int layoutResourceId;
    int imageTotal =10;
    public static String[] mThumbIds = {
            "http://192.168.1.20/android/1.jpg",
            "http://192.168.1.20/android/2.jpg",
            "http://192.168.1.20/android/3.jpg",
            "http://192.168.1.20/android/4.jpg",
            "http://192.168.1.20/android/5.jpg",
            "http://192.168.1.20/android/6.jpg",
            "http://192.168.1.20/android/7.jpg",
    };


    public GridViewAdapterL(Context mContext) {

        mContext = mContext;

    }
    public int getCount() {
        return imageTotal;
    }

    @Override
    public String getItem(int position) {
        return mThumbIds[position];
    }

    public long getItemId(int position) {
        return 0;
    }



    @Override

    public View getView(int position, View convertView, ViewGroup parent) {

        ImageView imageView;
        if (convertView == null) {
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(480, 480));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
        } else {
            imageView = (ImageView) convertView;
        }
        String url = getItem(position);
        Picasso.with(mContext)
                .load(url)
                .placeholder(R.drawable.splash_loading)
                .fit()
                .centerCrop().into(imageView);
        return imageView;


    }


}
