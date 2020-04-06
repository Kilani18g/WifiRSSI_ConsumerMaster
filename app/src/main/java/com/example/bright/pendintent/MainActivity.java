package com.example.bright.pendintent;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.support.annotation.WorkerThread;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import android.util.Log;


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.PriorityQueue;

import android.net.Uri;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Map;

import org.tensorflow.lite.Interpreter;
import static android.net.wifi.WifiManager.*;



public class MainActivity extends AppCompatActivity {


    WifiManager wifiManager;
    List scannedResult, apName, mAddr, missingrSsi, rSsi, brssi, timeStamp, xlist, ylist, macAddress, modelInput;
    int x, y, k, lvl, size;
    String xcoordinate, ycoordinate, outPut;
    TextView textView;

    private static final String TAG = "indoorLocatorDemo";
    private indoorLocatorClient client;

    private TextView resultTextView;
    private EditText inputEditText;
    private Handler handler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        client.load();
        setContentView(R.layout.activity_main);
        wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        scannedResult = new ArrayList();
        apName = new ArrayList();
        mAddr = new ArrayList();
        missingrSsi = new ArrayList();
        rSsi = new ArrayList();
        brssi = new ArrayList();
        timeStamp = new ArrayList();
        xlist = new ArrayList();
        ylist = new ArrayList();
        macAddress = new ArrayList();
        outPut = "";
        modelInput = new ArrayList();
        int k = 0;

        Log.v(TAG, "onCreate");


        client = new indoorLocatorClient(getApplicationContext());
        handler = new Handler();
        Button predictButton = findViewById(R.id.button);
        predictButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                boolean b = ((WifiManager) getSystemService(WIFI_SERVICE)).startScan();
                List<ScanResult> scanResultList = wifiManager.getScanResults();

                for (int i = 0; i < scanResultList.size(); i++) {
                    if (scanResultList.get(i).frequency < 2600) {
                        macAddress.add(scanResultList.get(i).BSSID + "\n");
                        rSsi.add(scanResultList.get(i).level + "\n");
                        System.out.print("I am here ");
                    }
                }
                client.locate(macAddress,rSsi);
                resultTextView = findViewById(R.id.textView);
            }
        });


        resultTextView = findViewById(R.id.textView);
    }


    /*@Override
    protected void onStart() {
        super.onStart();
        Log.v(TAG, "onStart");
        handler.post(
                () -> {
                    client.load();
                });
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.v(TAG, "onStop");
        handler.post(
                () -> {
                    client.unload();
                });
    }

    private void classify(final String text) {
        handler.post(
                () -> {
                    // Run text classification with TF Lite.
                    List<Result> results = client.locate(macAddress,rSsi);

                    // Show classification result on screen
                    showResult(macAddress, results);
                });
    }

    /** Show classification result on the screen. */
    /*private void showResult(final String inputText, final List<Result> results) {
        // Run on UI thread as we'll updating our app UI
        runOnUiThread(
                () -> {
                    String textToShow = "Input: " + inputText + "\nOutput:\n";
                    for (int i = 0; i < results.size(); i++) {
                        Result result = results.get(i);
                        textToShow +=
                                String.format("    %s: %s\n", result.getTitle(), result.getConfidence());
                    }
                    textToShow += "---------\n";

                    // Append the result to the UI.
                    resultTextView.append(textToShow);

                    // Clear the input text.
                    inputEditText.getText().clear();

                    // Scroll to the bottom to show latest entry's classification result.
                    scrollView.post(() -> scrollView.fullScroll(View.FOCUS_DOWN));
                });
    }*/
}














