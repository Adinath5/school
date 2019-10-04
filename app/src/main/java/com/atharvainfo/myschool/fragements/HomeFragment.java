package com.atharvainfo.myschool.fragements;


import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

import com.atharvainfo.myschool.Adapter.GridViewAdapter;
import com.atharvainfo.myschool.R;
import com.atharvainfo.myschool.others.ImageItem;

import java.util.ArrayList;


public class HomeFragment extends Fragment {
    private GridView gridView;
    private GridViewAdapter gridAdapter;
    private View view;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =inflater.inflate(R.layout.fragment_home, container, false);

        gridView = (GridView) view.findViewById(R.id.gridView);
        gridAdapter = new GridViewAdapter(getContext(), R.layout.grid_item_layout, getData());
        gridView.setAdapter(gridAdapter);

        return view;
    }


    private ArrayList<ImageItem> getData() {
        final ArrayList<ImageItem> imageItems = new ArrayList<>();
        TypedArray imgs = getResources().obtainTypedArray(R.array.image_ids);
        //TypedArray imgname = getResources().obtainTypedArray(R.array.menuname);
        String defaultInputText[] = getResources().getStringArray(R.array.menuname);

        TextView[] textViewArray = new TextView[10];
        for (int i = 0; i < imgs.length(); i++) {
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), imgs.getResourceId(i, -1));
            String title = defaultInputText[i];
            imageItems.add(new ImageItem(bitmap, title));
        }
        return imageItems;
    }
}
