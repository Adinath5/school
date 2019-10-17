package com.atharvainfo.myschool.adminfragment;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.atharvainfo.myschool.R;
import com.atharvainfo.myschool.others.ApiInterface;
import com.atharvainfo.myschool.others.PSDialogMsg;
import com.atharvainfo.myschool.others.PrefManager;
import com.atharvainfo.myschool.others.ServerResponse;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddClassFragment extends Fragment {

    private View view;
    EditText txtclassname;
    Button btnsave, btncancel;
    String ClassName, UserId;
    PrefManager prefManager;
    private String jsonURL = "http://www.atharvainfosolutions.com/myschool/";
    private final int jsoncode = 1;
    PSDialogMsg psDialogMsg;


    public AddClassFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view =inflater.inflate(R.layout.fragment_add_class, container, false);

        txtclassname = view.findViewById(R.id.editTextClassName);
        btnsave = view.findViewById(R.id.btnsave);
        btncancel = view.findViewById(R.id.btncancel);
        prefManager = new PrefManager(getContext());
        UserId = prefManager.getUserName().toString();
        psDialogMsg = new PSDialogMsg(getActivity() );


        btncancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(txtclassname.getText()!= null){
                    doUpdate();
                }
            }
        });

        // Inflate the layout for this fragment
        return view;
    }


    public void doUpdate(){

        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Saving Class Name Detail...");
        progressDialog.show();

        Calendar c = Calendar.getInstance();
        System.out.println("Current time => "+c.getTime());

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = df.format(c.getTime());

        ClassName = txtclassname.getText().toString();

        Log.d("Response", ClassName.toString()+ " "+ UserId.toString()+ " "+ formattedDate.toString());


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(jsonURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiInterface apiInterface = retrofit.create(ApiInterface.class);

        Call<ServerResponse> call = apiInterface.addnewclassname(ClassName.toString(), UserId.toString(), formattedDate.toString());


        call.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                Log.i("Responsestring", response.body().toString());
                progressDialog.dismiss();

                ServerResponse resp = response.body();
                Log.d("Response", resp.getResult().toString());
                if(resp.getResult().equals("success")){
                    //prefManager.setUserName(user.getUsername().toString());
                    //prefManager.setPContact(user.getPhone().toString());
                    //goToProfile();

                    psDialogMsg.showInfoDialog("Class Name Added Successfully.", "Ok");
                    psDialogMsg.show();


                } else {
                    psDialogMsg.showInfoDialog("Class Name Not Added Successfully.", "Ok");
                    psDialogMsg.show();
                }


            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {

            }


        });

    }
}
