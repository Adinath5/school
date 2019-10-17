package com.atharvainfo.myschool.activity.activity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

public class StudentInformation extends AppCompatActivity implements AdapterView.OnItemSelectedListener, RadioGroup.OnCheckedChangeListener {


    /*  private View view;*/
    EditText txtadmissionnumber, txtstudentname, txtfathername, txtmothername, txtemailaddress, txtmobilenof, txtmobilenom;
    Button btnsave;
    public String Admissionno, Spinner1, Gender, Studentname, Fathername, Mothername, Emailadress, Mobilenooffather, Mobilenoofmother, DateofRegistartion, Dateofeffectiveform;
    //PrefManager prefManager;
    private String jsonURL = "http://www.atharvainfosolutions.com/myschool/";
    private final int jsoncode = 1;
    PSDialogMsg psDialogMsg;
    Spinner spinnerstd;
    RadioGroup radiogroup;
    private RadioButton r1,r2;
    EditText datechoice1, datechoice2;
    DatePickerDialog.OnDateSetListener date;
    DatePickerDialog.OnDateSetListener date1;
    String dateformat = "yyyy-MM-dd";
    SimpleDateFormat dateform = new SimpleDateFormat(dateformat, Locale.US);
    Calendar mycal = Calendar.getInstance();
     public String op1, op2;
    private View edit;
    Context context;
    String[] ListElements = new String[]{

    };
    String[] unit = {"1st", "2nd","3rd","4th","5th","6th","7th","8th","9th","10th"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.activity_student_information);

        datechoice1 = findViewById( R.id.datechoice1);
        datechoice2 = findViewById( R.id.datechoice2);
        txtadmissionnumber = findViewById( R.id.admissionno);
        spinnerstd =findViewById( R.id.spinner);
        radiogroup =findViewById( R.id.radioGroup1);

        txtstudentname = findViewById( R.id.studentname);
        txtfathername = findViewById( R.id.fathername);
        txtmothername = findViewById( R.id.mothername);
        txtemailaddress = findViewById( R.id.email);
        txtmobilenof = findViewById( R.id.fmobile);
        txtmobilenom = findViewById( R.id.mmobile);
        r1= findViewById (R.id.radio0);
        r2= findViewById (R.id.radio1);
        /*actv(false);*/
        radiogroup.setOnCheckedChangeListener(this);
        spinnerstd.setOnItemSelectedListener(this);
        ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, unit);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerstd.setAdapter(aa);
        btnsave = findViewById( R.id.btnsubmit);
        //prefManager = new PrefManager(getContext());
        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                doUpdate();

            }
        });
        psDialogMsg = new PSDialogMsg ( StudentInformation.this );

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
                new DatePickerDialog( StudentInformation.this, date, mycal.get(Calendar.YEAR), mycal.get(Calendar.MONTH), mycal.get(Calendar.DAY_OF_MONTH)).show();

            }
        });

        datechoice2.setText(datestring);

        date1 = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int monthofyear, int dateofmonth) {
                mycal.set(Calendar.YEAR, year);
                mycal.set(Calendar.MONTH, monthofyear);
                mycal.set(Calendar.DAY_OF_MONTH, dateofmonth);
                updateDate1();


            }
        };
        datechoice2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog( StudentInformation.this, date1, mycal.get(Calendar.YEAR), mycal.get(Calendar.MONTH), mycal.get(Calendar.DAY_OF_MONTH)).show();

            }
        });



    }

    //  return view;
    private void updateDate() {

        datechoice1.setText(dateform.format(mycal.getTime()));

    }

    private void updateDate1() {

        datechoice2.setText(dateform.format(mycal.getTime()));

    }

    public void doUpdate() {
        

        final ProgressDialog progressDialog = new ProgressDialog( StudentInformation.this);
        progressDialog.setCancelable(false); // set cancelable to false
        progressDialog.setMessage("Please Wait"); // set message
        progressDialog.show(); // show progress dialog


        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = df.format(c.getTime());

        Admissionno = txtadmissionnumber.getText().toString();
        Spinner1 = spinnerstd.getSelectedItem().toString();
        Gender=op2.toString();
        Studentname = txtstudentname.getText().toString();
        Fathername = txtfathername.getText().toString();
        Mothername = txtmothername.getText().toString();
        Emailadress = txtemailaddress.getText().toString();
        Mobilenooffather = txtmobilenof.getText().toString();
        Mobilenoofmother = txtmobilenom.getText().toString();
        DateofRegistartion = datechoice1.getText().toString();
        Dateofeffectiveform = datechoice2.getText().toString();

        Log.d("Response", Admissionno + " " + Spinner1 + " " + Gender + " " + Studentname + " " + Fathername + " " + Mothername + " " + Emailadress + " " + Mobilenooffather + " " + Mobilenoofmother + " " + DateofRegistartion + " " + Dateofeffectiveform );

              Retrofit retrofit = new Retrofit.Builder()

                             .baseUrl(jsonURL)

                             .addConverterFactory(GsonConverterFactory.create())

                              .build();
               ApiInterface apiInterface = retrofit.create(ApiInterface.class);

        Call<ServerResponse> call = apiInterface.addnewstudent ( Admissionno, Spinner1, Gender, Studentname, Fathername, Mothername, Emailadress, Mobilenooffather, Mobilenoofmother, DateofRegistartion, Dateofeffectiveform );


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

                    psDialogMsg.showInfoDialog("Student Name Added Successfully.", "Ok");
                    psDialogMsg.show();


                } else {
                    psDialogMsg.showInfoDialog("Student Name Not Added Successfully.", "Ok");
                    psDialogMsg.show();
                }


            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {

            }


        });


    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String item = parent.getItemAtPosition(position).toString();
        Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    public void onRadioButtonClicked(View view) {


        boolean checked = ((RadioButton) view).isChecked();
        switch (view.getId()) {
            case R.id.radio0:
                if (checked)
                    op2 = "Male";
                break;
            case R.id.radio1:
                if (checked)
                    op2 = "Female";
                break;
        }
    }




    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {

    }



}





        


