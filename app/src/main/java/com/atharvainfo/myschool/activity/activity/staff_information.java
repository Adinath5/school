package com.atharvainfo.myschool.activity.activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
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
import android.widget.Toast;
import com.atharvainfo.myschool.R;
import com.atharvainfo.myschool.others.ApiInterface;
import com.atharvainfo.myschool.others.PSDialogMsg;
import com.atharvainfo.myschool.others.ServerResponse;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class staff_information extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    EditText txtstaffname,txtstaffemail,txtstaffaddress,txtstaffmobileno;
    Spinner spinnedesignation;
    Button btnsubmit;
    String staffname,staffemail,staffaddress,staffmobileno,designation,dateofjoining;
    private String jsonURL = "http://www.atharvainfosolutions.com/myschool/";
    private final int jsoncode = 1;
    PSDialogMsg psDialogMsg;
    EditText datechoice1;
    DatePickerDialog.OnDateSetListener date;
    String dateformat = "yyyy-MM-dd";
    SimpleDateFormat dateform = new SimpleDateFormat(dateformat, Locale.US);
    Calendar mycal = Calendar.getInstance();
    String[] ListElements = new String[]{

    };
    String[] unit = {"Principle","HOD","Teacher","Peon"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_staff_information );
        txtstaffname = findViewById ( R.id.staffname );
        txtstaffemail = findViewById ( R.id.email );
        txtstaffaddress = findViewById ( R.id.address );
        txtstaffmobileno = findViewById ( R.id.mobileno );
        spinnedesignation = findViewById ( R.id.spinner );
        spinnedesignation.setOnItemSelectedListener ( this );
        ArrayAdapter aa = new ArrayAdapter ( this, android.R.layout.simple_spinner_item, unit );
        aa.setDropDownViewResource ( android.R.layout.simple_spinner_dropdown_item );
        spinnedesignation.setAdapter ( aa );
        btnsubmit = findViewById ( R.id.btnsubmit );
        btnsubmit.setOnClickListener ( new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {

                doUpdate();

            }
        } );
        psDialogMsg = new PSDialogMsg ( staff_information.this );
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
                new DatePickerDialog ( staff_information.this, date, mycal.get ( Calendar.YEAR ), mycal.get ( Calendar.MONTH ), mycal.get ( Calendar.DAY_OF_MONTH ) ).show ( );

            }
        } );
    }
    private void updateDate () {

        datechoice1.setText ( dateform.format ( mycal.getTime ( ) ) );

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String item = parent.getItemAtPosition(position).toString();
        Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();

    }
    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void doUpdate() {
        final ProgressDialog progressDialog = new ProgressDialog( staff_information.this);
        progressDialog.setCancelable(false); // set cancelable to false
        progressDialog.setMessage("Please Wait"); // set message
        progressDialog.show(); // show progress dialog
        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = df.format(c.getTime());
        staffname = txtstaffname.getText().toString();
        staffemail=txtstaffemail.getText ().toString ();
        staffaddress=txtstaffaddress.getText ().toString ();
        staffmobileno=txtstaffmobileno.getText().toString();
        designation=  spinnedesignation.getSelectedItem().toString();
        dateofjoining=datechoice1.getText ().toString ();
        Log.d("Response", staffname.toString ()+ " " +staffemail.toString () + " " +  staffaddress.toString () + " " + staffmobileno.toString () + " " + designation.toString ()+ " " + dateofjoining.toString () );
       // Toast.makeText(this, "staffname: + \" \" +staffemail: + \" \" +  staffaddress: + \" \" + staffmobileno + \" \" + designation:+ \" \" + dateofjoining:", Toast.LENGTH_LONG).show();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(jsonURL)
                .addConverterFactory( GsonConverterFactory.create())
                .build();
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);
        Call<ServerResponse> call = apiInterface.addnewstaffname(staffname.toString (),staffemail.toString (),staffaddress.toString (),staffmobileno.toString (), designation.toString (), dateofjoining.toString ());
        call.enqueue(new Callback<ServerResponse> () {
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
                    psDialogMsg.showInfoDialog("Staff Name Added Successfully.", "Ok");
                    psDialogMsg.show();
                }
                else {
                    psDialogMsg.showInfoDialog("Staff Name Not Added Successfully.", "Ok");
                    psDialogMsg.show();
                } }

                @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
            }
        });


    }}

