package com.nitish.nfcqrapp;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.nitish.nfcqrapp.data.ProductDetail;
import com.nitish.nfcqrapp.data.ProductRequest;

public class HomeActivity extends AppCompatActivity {
    private View nfcButton;
    private View qrButton;
    private String LOGGING_TAG = "HomeActivity";
    private static final int RC_HANDLE_CAMERA_PERM = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        nfcButton = findViewById(R.id.nfcButton);
        qrButton = findViewById(R.id.qrButton);

        int rc = ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        if (rc == PackageManager.PERMISSION_GRANTED) {

        } else {
            requestCameraPermission();
        }

        nfcButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), NfcActivity.class);
                startActivity(intent);

            }
        });
        qrButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), BarCodeActivity.class);
                startActivity(intent);
            }
        });


    }

    private void requestCameraPermission() {
        Log.w(LOGGING_TAG, "Camera permission is not granted. Requesting permission");

        final String[] permissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_PHONE_STATE, Manifest.permission.INTERNET, Manifest.permission.NFC, Manifest.permission.WRITE_EXTERNAL_STORAGE};

        if (!ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.CAMERA)) {
            ActivityCompat.requestPermissions(this, permissions, RC_HANDLE_CAMERA_PERM);
            return;
        }

        final Activity thisActivity = this;

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityCompat.requestPermissions(thisActivity, permissions,
                        RC_HANDLE_CAMERA_PERM);
            }
        };


    }
}
