package com.atharvainfo.myschool.activity.activity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.atharvainfo.myschool.R;
import com.atharvainfo.myschool.others.ApiInterface;
import com.atharvainfo.myschool.others.PSDialogMsg;
import com.atharvainfo.myschool.others.ServerResponse;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class book_issued extends AppCompatActivity {

    EditText txtbookno, txtbookname, txtstudentname;
    Button btnsubmit;
    String Bookno, Bookname, Studentname, Dateofissued;
    private String jsonURL = "http://www.atharvainfosolutions.com/myschool/";
    private final int jsoncode = 1;
    PSDialogMsg psDialogMsg;
    EditText datechoice1;
    String dateformat = "yyyy-MM-dd";
    SimpleDateFormat dateform = new SimpleDateFormat(dateformat, Locale.US);
    Calendar mycal = Calendar.getInstance();
    private DatePickerDialog.OnDateSetListener date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_issued);
        txtbookno = findViewById(R.id.bookno);
        txtbookname = findViewById(R.id.bookname);
        txtstudentname = findViewById(R.id.studentname);
        datechoice1 = findViewById(R.id.datechoice1);
        btnsubmit = findViewById ( R.id.btnsubmit );
        btnsubmit.setOnClickListener ( new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {

                doUpdate();

            }
        } );

        psDialogMsg = new PSDialogMsg(book_issued.this);

        Long currentdate = System.currentTimeMillis();
        String datestring = dateform.format(currentdate);
        datechoice1.setText(datestring);
        date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int monthofyear, int dateofmonth) {
                mycal.set(Calendar.YEAR, year);
                mycal.set(Calendar.MONTH, monthofyear);
                mycal.set(Calendar.DAY_OF_MONTH, dateofmonth);
                updateDate();


            }
        };
        datechoice1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(book_issued.this, date, mycal.get(Calendar.YEAR), mycal.get(Calendar.MONTH), mycal.get(Calendar.DAY_OF_MONTH)).show();

            }
        });

    }

    private void updateDate() {

        datechoice1.setText(dateform.format(mycal.getTime()));

    }

    public void doUpdate() {


        final ProgressDialog progressDialog = new ProgressDialog(book_issued.this);
        progressDialog.setCancelable(false); // set cancelable to false
        progressDialog.setMessage("Please Wait"); // set message
        progressDialog.show(); // show progress dialog


        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = df.format(c.getTime());

        Bookno = txtbookno.getText().toString();
        Bookname= txtbookname.getText().toString();
        Studentname=txtstudentname.getText().toString();
        Dateofissued= datechoice1.getText().toString();

        Log.d("Response", Bookno + " " + Bookname + " " + Studentname+ " " + Dateofissued);

        Retrofit retrofit = new Retrofit.Builder()

                .baseUrl(jsonURL)

                .addConverterFactory(GsonConverterFactory.create())

                .build();
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);

        Call<ServerResponse> call = apiInterface.addbookissued (Bookno,Bookname,Studentname, Dateofissued);


        call.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                Log.i("Responsestring", response.body().toString());
                progressDialog.dismiss();

                ServerResponse resp = response.body();
                Log.d("Response", resp.getResult() );
                if (resp.getResult().equals("success")) {
                    //prefManager.setUserName(user.getUsername().toString());
                    //prefManager.setPContact(user.getPhone().toString());
                    //goToProfile();

                    psDialogMsg.showInfoDialog("Book Issued...", "Ok");
                    psDialogMsg.show();


                } else {
                    psDialogMsg.showInfoDialog("Book Not Issued.....", "Ok");
                    psDialogMsg.show();
                }


            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {

            }


        });

    }


}
