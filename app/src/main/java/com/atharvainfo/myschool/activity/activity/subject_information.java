package com.atharvainfo.myschool.activity.activity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import com.atharvainfo.myschool.R;
import com.atharvainfo.myschool.others.ApiInterface;
import com.atharvainfo.myschool.others.PSDialogMsg;
import com.atharvainfo.myschool.others.ServerResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
public class subject_information extends AppCompatActivity {
    EditText txtsubjectname;
    Button btnadd;
    String subjectname;
    private String jsonURL = "http://www.atharvainfosolutions.com/myschool/";
    private final int jsoncode = 1;
    PSDialogMsg psDialogMsg;
    private String TAG = subject_information.class.getSimpleName ( );
    private ListView lv;
    ArrayList<HashMap<String, String>> contactList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView (R.layout.activity_subject_information);
        txtsubjectname = findViewById ( R.id.subjectname );
        btnadd = findViewById ( R.id.btnadd );
        btnadd.setOnClickListener ( new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {
                doUpdate ( );

            }
        } );
        psDialogMsg = new PSDialogMsg ( subject_information.this );
        contactList = new ArrayList<> ( );
        lv = (ListView) findViewById ( R.id.list );
    }
    public void doUpdate() {
        final ProgressDialog progressDialog = new ProgressDialog ( subject_information.this );
        progressDialog.setCancelable ( false );
        progressDialog.setMessage ( "Please Wait" );
        progressDialog.show ( );
        Calendar c = Calendar.getInstance ( );
        System.out.println ( "Current time => " + c.getTime ( ) );
        SimpleDateFormat df = new SimpleDateFormat ( "yyyy-MM-dd" );
        String formattedDate = df.format ( c.getTime ( ) );
        subjectname = txtsubjectname.getText ( ).toString ( );
        Log.d ( "Response", subjectname.toString ( ) );
        Retrofit retrofit = new Retrofit.Builder ( )
                .baseUrl ( jsonURL )
                .addConverterFactory ( GsonConverterFactory.create ( ) )
                .build ( );
        ApiInterface apiInterface = retrofit.create ( ApiInterface.class );
        Call<ServerResponse> call = apiInterface.addnewsubjectname ( subjectname.toString ( ) );
        call.enqueue ( new Callback<ServerResponse> ( ) {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                Log.i ( "Responsestring", response.body ( ).toString ( ) );
                progressDialog.dismiss ( );
                ServerResponse resp = response.body ( );
                Log.d ( "Response", resp.getResult ( ) );
                if (resp.getResult ( ).equals ( "success" )) {
                    psDialogMsg.showInfoDialog ( "Subject Name Added Successfully.", "Ok" );
                    psDialogMsg.show ( );
                } else {
                    psDialogMsg.showInfoDialog ( "Subject Name Not Added Successfully.", "Ok" );
                    psDialogMsg.show ( );
                }
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
            }
        } );
    }

    private class GetContacts extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {

            HttpHandler sh = null;
            try {
                sh = new HttpHandler ( );
            } catch (IOException e) {
                e.printStackTrace ( );
            }
            String jsonStr = sh.makeServiceCall ( jsonURL );

            Log.e ( TAG, "Response from url: " + jsonStr );

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject ( jsonStr );

                    // Getting JSON Array node
                    JSONArray contacts = jsonObj.getJSONArray ("contacts" );

                    // looping through All Contacts
                    for (int i = 0; i < contacts.length ( ); i++) {
                        JSONObject c = contacts.getJSONObject ( i );

                        String subjectname = c.getString ( "subjectname" );
                        HashMap<String, String> contact = new HashMap<> ( );

                        // adding each child node to HashMap key => value
                        contact.put ( "subjectname", subjectname );
                        // adding contact to contact list
                        contactList.add ( contact );
                    }
                } catch (final JSONException e) {
                    Log.e ( TAG, "Json parsing error: " + e.getMessage ( ) );
                    runOnUiThread ( new Runnable ( ) {
                        @Override
                        public void run() {
                            Toast.makeText ( getApplicationContext ( ),
                                    "Json parsing error: " + e.getMessage ( ),
                                    Toast.LENGTH_LONG )
                                    .show ( );
                        }
                    } );

                }
            } else {
                Log.e ( TAG, "Couldn't get json from server." );
                runOnUiThread ( new Runnable ( ) {
                    @Override
                    public void run() {
                        Toast.makeText ( getApplicationContext ( ),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG )
                                .show ( );
                    }
                } );

            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute ( );
            // Showing progress dialog
            psDialogMsg = new PSDialogMsg ( subject_information.this );
            psDialogMsg.setMessage ( "Please wait..." );
            psDialogMsg.setCancelable ( false );
            psDialogMsg.show ( );
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute ( result );
            // Dismiss the progress dialog
            if (psDialogMsg.isShowing ( ))
                psDialogMsg.dismiss ( );
            /**
             * Updating parsed JSON data into ListView
             * */
            ListAdapter adapter = new SimpleAdapter (
                    subject_information.this, contactList,
                    R.layout.subject_list, new String[]{"subjectname"}
                    , new int[]{R.id.subjectname,} );

            lv.setAdapter ( adapter );
        }
    }}





