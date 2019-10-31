package com.atharvainfo.myschool.activity.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.atharvainfo.myschool.R;

import javax.xml.transform.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class scanactivity extends AppCompatActivity {

    private ZXingScannerView mScannerView;

    public scanactivity(ZXingScannerView mScannerView) {
        this.mScannerView = mScannerView;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_scanactivity);
    }
    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler((ZXingScannerView.ResultHandler) this); // Register ourselves as a handler for scan results.
        mScannerView.startCamera();          // Start camera on resume
    }
    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();           // Stop camera on pause
    }
    public void handleResult(Result rawResult) {
        book_information.tvresult.setText((CharSequence) rawResult);
        onBackPressed();

    }


}
