package com.atharvainfo.myschool.adminfragment;


import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.atharvainfo.myschool.R;
import com.atharvainfo.myschool.others.ClassMast;
import com.atharvainfo.myschool.others.ConnectionDetector;
import com.atharvainfo.myschool.others.HttpRequest;
import com.atharvainfo.myschool.others.PSDialogMsg;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;


public class NewClassFragment extends Fragment {
    private View view;
    FloatingActionButton cadd;
    private ArrayList<ClassMast> ClassArrayList;
    ArrayList<HashMap<String, String>> ClassList;
    ConnectionDetector connectivity;
    PSDialogMsg psDialogMsg;
    private String jsonURL = "http://www.atharvainfosolutions.com/myschool/api.php?apicall=getClassList";
    private final int jsoncode = 1;
    int NoofStudents= 0;
    private static ProgressDialog mProgressDialog;
    private SimpleAdapter adapter;
    private ListView classnamelist;
    TextView totalstudent;
    private PopupWindow window;
    String classid;
    public NewClassFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_new_class, container, false);

        connectivity = new ConnectionDetector(getContext());
        psDialogMsg = new PSDialogMsg(getActivity(),false);
        ClassArrayList = new ArrayList<>();
        ClassList = new ArrayList<HashMap<String, String>>();
        classnamelist = view.findViewById(R.id.news_list);
        totalstudent = view.findViewById(R.id.txttotal);


            cadd = view.findViewById(R.id.fab);
            cadd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    AddClassFragment addClassFragment = new AddClassFragment();
                    FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                            android.R.anim.fade_out);
                    fragmentTransaction.add(R.id.frame, addClassFragment, "NewClassFragment");
                    fragmentTransaction.addToBackStack("NewClassFragment");
                    fragmentTransaction.commitAllowingStateLoss();

                }
            });

            fetchJSON();
        // Inflate the layout for this fragment
        classnamelist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                classid =((TextView)view.findViewById(R.id.txtclassid)).getText().toString();

                ShowPopupWindow();
            }
        });

        return view;
    }


    @SuppressLint("StaticFieldLeak")
    private void fetchJSON(){

        showSimpleProgressDialog(getContext(), "Loading...","Downloading Class List",false);

        new AsyncTask<Void, Void, String>(){
            protected String doInBackground(Void[] params) {
                String response="";
                HashMap<String, String> map=new HashMap<>();
                try {
                    HttpRequest req = new HttpRequest(jsonURL);
                    response = req.prepare(HttpRequest.Method.POST).withData(map).sendAndReadString();
                } catch (Exception e) {
                    response=e.getMessage();
                }
                return response;
            }
            protected void onPostExecute(String result) {
                //do something with response
                Log.d("newwwss",result);
                try {
                    onTaskCompleted(result,jsoncode);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }.execute();
    }

    @SuppressLint("WrongConstant")
    public void onTaskCompleted(String response, int serviceCode) throws JSONException {
        Log.d("responsejson", response.toString());
        switch (serviceCode) {
            case jsoncode:

                if (isSuccess(response)) {

                    Log.d("ResponseList", response);
                    ClassArrayList = getInfo(response);
                    adapter = new SimpleAdapter(getContext(), ClassList, R.layout.class_list, new String[]{"A", "B", "C"}, new int[]{R.id.txtclassname, R.id.txtstdno, R.id.txtclassid});
                    classnamelist.setAdapter(null);
                    classnamelist.setAdapter(adapter);
                    totalstudent.setText("Total :" + String.valueOf(NoofStudents));

                    //eAdapter.setClickListener(this);

                }else {
                    Toast.makeText(getContext(), getErrorCode(response), Toast.LENGTH_SHORT).show();
                }
        }
    }

    public ArrayList<ClassMast> getInfo(String response) {

        try {
            JSONObject jsonObject = new JSONObject(response);
            if (jsonObject.getString("status").equals("true")) {
                JSONArray dataArray = jsonObject.getJSONArray("classlist");


                for (int i = 0; i < dataArray.length(); i++) {

                    ClassMast classMast = new ClassMast();
                    JSONObject dataobj = dataArray.getJSONObject(i);
                    classMast.setClassid(dataobj.getString("csrno"));
                    classMast.setClassname(dataobj.getString("classname"));
                    classMast.setNoofstrudents(dataobj.getString("noofstudents"));
                    ClassArrayList.add(classMast);

                    HashMap<String, String> classl = new HashMap<>();
                    classl.put("A", dataobj.getString("classname"));
                    classl.put("B", dataobj.getString("noofstudents"));
                    classl.put("C", dataobj.getString("csrno"));
                    ClassList.add(classl);
//                    NoofStudents = NoofStudents + Integer.valueOf(dataobj.getString("noofstudents"));

                    // return id;

                }
                Log.i("PrintClassList", ClassArrayList.toString());
                removeSimpleProgressDialog();  //will remove progress dialog
            } else if (jsonObject.getString("status").equals("false")) {
                removeSimpleProgressDialog();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return ClassArrayList;
    }


    public boolean isSuccess(String response) {

        try {
            JSONObject jsonObject = new JSONObject(response);
            if (jsonObject.optString("status").equals("true")) {
                return true;
            } else {
                removeSimpleProgressDialog();
                return false;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    public String getErrorCode(String response) {

        try {
            JSONObject jsonObject = new JSONObject(response);
            return jsonObject.getString("message");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "No data";
    }


    public static void removeSimpleProgressDialog() {
        try {
            if (mProgressDialog != null) {
                if (mProgressDialog.isShowing()) {
                    mProgressDialog.dismiss();
                    mProgressDialog = null;
                }
            }
        } catch (IllegalArgumentException ie) {
            ie.printStackTrace();

        } catch (RuntimeException re) {
            re.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void showSimpleProgressDialog(Context context, String title,
                                                String msg, boolean isCancelable) {
        try {
            if (mProgressDialog == null) {
                mProgressDialog = ProgressDialog.show(context, title, msg);
                mProgressDialog.setCancelable(isCancelable);
            }

            if (!mProgressDialog.isShowing()) {
                mProgressDialog.show();
            }

        } catch (IllegalArgumentException ie) {
            ie.printStackTrace();
        } catch (RuntimeException re) {
            re.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private JSONArray getJSonData(String fileName){
        JSONArray jsonArray=null;
        try {
            InputStream is = getResources().getAssets().open(fileName);
            int size = is.available();
            byte[] data = new byte[size];
            is.read(data);
            is.close();
            String json = new String(data, "UTF-8");
            jsonArray=new JSONArray(json);
        }catch (IOException e){
            e.printStackTrace();
        }catch (JSONException je){
            je.printStackTrace();
        }
        return jsonArray;
    }

    private ArrayList<JSONObject> getArrayListFromJSONArray(JSONArray jsonArray){
        ArrayList<JSONObject> aList=new ArrayList<JSONObject>();
        try {
            if (jsonArray != null) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    aList.add(jsonArray.getJSONObject(i));
                }
            }
        }catch (JSONException je){je.printStackTrace();}
        return  aList;

    }

    private void ShowPopupWindow(){
        try {
            ImageView btnadddivision, btnrename, btncamera, btnvideo,btngallary,btnwrite;

            LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout = inflater.inflate(R.layout.popupwindow, null);
            window = new PopupWindow(layout, 1000, 450, true);

            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            window.setOutsideTouchable(true);
            window.showAtLocation(layout, Gravity.CENTER, 40, 60);
            //  window.showAtLocation(layout, 17, 100, 100);

            btnadddivision = (ImageView) layout.findViewById(R.id.btncall);
            btnrename = (ImageView) layout.findViewById(R.id.btnsound);
            btnadddivision.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("New Class", " Add Division");
                    window.dismiss();
                }

            });
            btnrename.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Log.e("Rename Class", " Rename Class Name");
                    window.dismiss();

                }

            });


        }catch (Exception e){

        }
    }
}
