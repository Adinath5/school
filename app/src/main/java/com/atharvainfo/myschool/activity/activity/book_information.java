package com.atharvainfo.myschool.activity.activity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.atharvainfo.myschool.R;
import com.atharvainfo.myschool.others.ApiInterface;
import com.atharvainfo.myschool.others.PSDialogMsg;
import com.atharvainfo.myschool.others.ServerResponse;
import com.google.android.gms.vision.barcode.Barcode;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class book_information extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    EditText txtbookno, txtbookname, txtbookauthor, txtsuppliername, txtpublisher, txtprice;
    TextView txtbarcode;
    public static TextView tvresult;
    private  Button btn;
    Spinner spinnersubject;
    Button btnsubmit;
    String Bookno, Bookname, Bookauther, Bookdateofissued, Booksuppliername, Bookpublisher, Bookprice, Booksubject,Bookbarcode;
    private String jsonURL = "http://www.atharvainfosolutions.com/myschool/";
    private final int jsoncode = 1;
    PSDialogMsg psDialogMsg;
    EditText datechoice1;
    String dateformat = "yyyy-MM-dd";
    SimpleDateFormat dateform = new SimpleDateFormat ( dateformat, Locale.US );
    Calendar mycal = Calendar.getInstance ( );
    String[] ListElements = new String[]{

    };
    String[] unit = {"C and C++ Programming", "Relational Database", "Extreme Python", "Advanced Maths", "Statistics I and II"};
    private DatePickerDialog.OnDateSetListener date;
    private Object TextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_book_information );
        txtbookno = findViewById ( R.id.bookno );
        txtbookname = findViewById ( R.id.bookname );
        txtbookauthor = findViewById ( R.id.bookauthor );
        txtsuppliername = findViewById ( R.id.suppliername );
        txtpublisher = findViewById ( R.id.publishername );
        txtprice = findViewById ( R.id.price );
        spinnersubject.setOnItemSelectedListener ( this );
        ArrayAdapter aa = new ArrayAdapter ( this, android.R.layout.simple_spinner_item, unit );
        aa.setDropDownViewResource ( android.R.layout.simple_spinner_dropdown_item );
        txtbarcode=findViewById(R.id.tvresult);
        spinnersubject.setAdapter ( aa );

        tvresult = (TextView) findViewById(R.id.tvresult);

        btn = (Button) findViewById(R.id.btn);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(book_information.this, scanactivity.class);
                startActivity(intent);
            }
        });
        btnsubmit = findViewById ( R.id.btnsubmit );
        btnsubmit.setOnClickListener ( new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {

                // doUpdate();

            }
        } );
        psDialogMsg = new PSDialogMsg ( book_information.this );
        datechoice1 = findViewById ( R.id.datechoice1 );
        Long currentdate = System.currentTimeMillis ( );
        String datestring = dateform.format ( currentdate );
        datechoice1.setText ( datestring );
        date = new DatePickerDialog.OnDateSetListener ( ) {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int monthofyear, int dateofmonth) {
                mycal.set ( Calendar.YEAR, year );
                mycal.set ( Calendar.MONTH, monthofyear );
                mycal.set ( Calendar.DAY_OF_MONTH, dateofmonth );
                updateDate ( );


            }
        };
        datechoice1.setOnClickListener ( new View.OnClickListener ( ) {
            @Override
            public void onClick(View view) {
                new DatePickerDialog ( book_information.this, date, mycal.get ( Calendar.YEAR ), mycal.get ( Calendar.MONTH ), mycal.get ( Calendar.DAY_OF_MONTH ) ).show ( );

            }
        } );
    }

    private void updateDate() {

        datechoice1.setText ( dateform.format ( mycal.getTime ( ) ) );

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void doUpdate() {
        final ProgressDialog progressDialog = new ProgressDialog ( book_information.this );
        progressDialog.setCancelable ( false ); // set cancelable to false
        progressDialog.setMessage ( "Please Wait" ); // set message
        progressDialog.show ( ); // show progress dialog
        Calendar c = Calendar.getInstance ( );
        System.out.println ( "Current time => " + c.getTime ( ) );
        SimpleDateFormat df = new SimpleDateFormat ( "yyyy-MM-dd" );
        String formattedDate = df.format ( c.getTime ( ) );
        Bookno = txtbookno.getText ( ).toString ( );
        Bookname = txtbookname.getText ( ).toString ( );
        Bookauther = txtbookauthor.getText ( ).toString ( );
        Bookdateofissued =datechoice1.getText ( ).toString ( );
        Booksuppliername=txtsuppliername.getText ().toString ();
        Bookpublisher=txtpublisher.getText ().toString ();
        Bookprice=   txtprice.getText ().toString ();
        Booksubject = spinnersubject.getSelectedItem ( ).toString ( );
        Bookbarcode=txtbarcode.getText().toString();
        int response = Log.d ( "Response", Bookno.toString ( ) + " " + Bookname.toString ( ) + " " + Bookauther.toString ( ) + " " + Bookdateofissued.toString ( ) + " " + Booksuppliername.toString ( ) + " " + Bookpublisher.toString ( ) + " " + Bookprice.toString ( ) + " " + Booksubject.toString ( ) + "  " + Bookbarcode.toString ( ) );
        Retrofit retrofit = new Retrofit.Builder ( )
                .baseUrl ( jsonURL )
                .addConverterFactory ( GsonConverterFactory.create ( ) )
                .build ( );
        ApiInterface apiInterface = retrofit.create ( ApiInterface.class );
        Call<ServerResponse> call = apiInterface.addbookmaster (Bookno.toString ( ), Bookname.toString ( ), Bookauther.toString ( ), Bookdateofissued.toString ( ), Booksuppliername.toString ( ), Bookpublisher.toString ( ),Bookprice.toString (),Booksubject.toString (),Bookbarcode );
        call.enqueue ( new Callback<ServerResponse> ( ) {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                Log.i ( "Responsestring", response.body ( ).toString ( ) );
                progressDialog.dismiss ( );
                ServerResponse resp = response.body ( );
                Log.d ( "Response", resp.getResult ( ) );
                if (resp.getResult ( ).equals ( "success" )) {
                    //prefManager.setUserName(user.getUsername().toString());
                    //prefManager.setPContact(user.getPhone().toString());
                    //goToProfile();
                    psDialogMsg.showInfoDialog ( "Staff Name Added Successfully.", "Ok" );
                    psDialogMsg.show ( );
                } else {
                    psDialogMsg.showInfoDialog ( "Staff Name Not Added Successfully.", "Ok" );
                    psDialogMsg.show ( );
                }
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
            }
        } );

    }
}

