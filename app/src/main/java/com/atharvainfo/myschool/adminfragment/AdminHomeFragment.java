package com.atharvainfo.myschool.adminfragment;


import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.atharvainfo.myschool.Adapter.GridBaseAdapter;
import com.atharvainfo.myschool.Adapter.GridViewAdapter;
import com.atharvainfo.myschool.R;
import com.atharvainfo.myschool.others.ImageItem;
import com.atharvainfo.myschool.others.ImageModel;

import java.util.ArrayList;

public class AdminHomeFragment extends Fragment {
    private GridView gridView;
    private GridViewAdapter gridAdapter;

    private View view;
    private GridView gvGallery;
    private GridBaseAdapter gridBaseAdapter;
    private ArrayList<ImageModel> imageModelArrayList;
    private int[] myImageList = new int[]{R.drawable.newclass, R.drawable.fees,
            R.drawable.account,R.drawable.students
            ,R.drawable.subject,R.drawable.staff,
            R.drawable.event,R.drawable.notification, R.drawable.homework, R.drawable.gallary,R.drawable.video,R.drawable.transportation,
            R.drawable.setng,R.drawable.awards,R.drawable.sctc};
    private String[] myImageNameList = new String[]{"Class", "Fees",
            "Account","Students"
            ,"Subject","Staff",
            "Events","Notification","HomeWork","Gallary","Video","Transportation","Settings","Awaards","Science & Technology"};

    public AdminHomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_admin_home, container, false);

        /*gridView = (GridView) view.findViewById(R.id.gridView);
        gridAdapter = new GridViewAdapter(getContext(), R.layout.grid_item_layout, getData());
        gridView.setAdapter(gridAdapter);*/
        gvGallery = view.findViewById(R.id.gridView);

        imageModelArrayList = populateList();

        gridBaseAdapter = new GridBaseAdapter(getActivity().getApplicationContext(),imageModelArrayList);
        gvGallery.setAdapter(gridBaseAdapter);

        gvGallery.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        NewClassFragment classFragment = new NewClassFragment();
                        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                                android.R.anim.fade_out);
                        fragmentTransaction.add(R.id.frame, classFragment, "AdminHome");
                        fragmentTransaction.addToBackStack("AdminHome");
                        fragmentTransaction.commitAllowingStateLoss();
                }
                Toast.makeText(getContext(), myImageNameList[position]+" Clicked", Toast.LENGTH_SHORT).show();
            }
        });

        /*gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String menuitem = parent.getItemAtPosition(position).toString();

                }
            }
        });*/

        return view;
    }

    /*private ArrayList<ImageItem> getData() {
        final ArrayList<ImageItem> imageItems = new ArrayList<>();
        TypedArray imgs = getResources().obtainTypedArray(R.array.adminimage_ids);
        //TypedArray imgname = getResources().obtainTypedArray(R.array.menuname);
        String defaultInputText[] = getResources().getStringArray(R.array.adminmenuname);

        //TextView[] textViewArray = new TextView[10];
        for (int i = 0; i < imgs.length(); i++) {
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), imgs.getResourceId(i, -1));
            String title = defaultInputText[i];
            imageItems.add(new ImageItem(bitmap, title));
        }
        return imageItems;
    }*/

    private ArrayList<ImageModel> populateList(){

        ArrayList<ImageModel> list = new ArrayList<>();

        for(int i = 0; i < 8; i++){
            ImageModel imageModel = new ImageModel();
            imageModel.setName(myImageNameList[i]);
            imageModel.setImage_drawable(myImageList[i]);
            list.add(imageModel);
        }

        return list;
    }
}
