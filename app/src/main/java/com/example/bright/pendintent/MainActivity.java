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
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.textView2);
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

        /*predictButton.setOnClickListener(
                (View v) -> {
                    doProcess(null);
                    System.out.print(macAddress);
                    //client.locate(macAddress,rSsi);
                    resultTextView = findViewById(R.id.textView);
                });*/

        /*predictButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                boolean b = ((WifiManager) getSystemService(WIFI_SERVICE)).startScan();
                List<ScanResult> scanResultList = wifiManager.getScanResults();
                for (int i = 0; i < scanResultList.size(); i++) {
                    if (scanResultList.get(i).frequency < 2600) {
                        macAddress.add(scanResultList.get(i).BSSID + "\n");
                        rSsi.add(scanResultList.get(i).level + "\n");
                    }
                }
                System.out.print(macAddress);
                client.locate(macAddress,rSsi);
                resultTextView = findViewById(R.id.textView);
            }
        });*/


        //resultTextView = findViewById(R.id.textView);
    }
    static Map<String, List> apNamebssid = new HashMap<String, List>();
    static Map<String, List<Integer>> bSsidrssi = new HashMap<String, List<Integer>>();
    static StringBuilder data = new StringBuilder();

    public void doProcess(View view) {
        if (scannedResult.size() > 0) {
            scannedResult.clear();
        }
        data.setLength(0);
        mAddr.clear();
        apName.clear();
        macAddress.clear();
        rSsi.clear();
        boolean b = ((WifiManager) getSystemService(WIFI_SERVICE)).startScan();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    1);

            if (wifiManager.isWifiEnabled()) {
                Toast.makeText(getApplicationContext(), "on", Toast.LENGTH_SHORT).show();
                new AsyncTask<Void, String, String>() {
                    @Override
                    protected String doInBackground(Void... voids) {
                        List<ScanResult> scanResultList = wifiManager.getScanResults();
                        for (int i = 0; i < scanResultList.size(); i++) {

                            scannedResult.add(scanResultList.get(i).SSID + "\n");
                            scannedResult.add(scanResultList.get(i).BSSID + "\n");
                            scannedResult.add(scanResultList.get(i).level + "\n");

                        }
                        return scannedResult.toString();
                    }

                    @Override
                    protected void onPostExecute(String s) {
                        textView.setText(s);
                    }
                }.execute();
            } else {
                wifiManager.setWifiEnabled(false);
                new AsyncTask<Void, String, String>() {
                    @Override
                    protected String doInBackground(Void... voids) {
                        List<ScanResult> scanResultList = wifiManager.getScanResults();
                        for (int i = 0; i < scanResultList.size(); i++) {
                            scannedResult.add(scanResultList.get(i).SSID + "\n");
                            scannedResult.add(scanResultList.get(i).BSSID + "\n");
                            scannedResult.add(scanResultList.get(i).level + "\n");
                            System.out.print("I am here ");
                        }
                        return scannedResult.toString();
                    }

                    @Override
                    protected void onPostExecute(String s) {
                        textView.setText(s);
                    }
                }.execute();
            }
        } else {
            if (wifiManager.isWifiEnabled()) {
                Toast.makeText(getApplicationContext(), "Scan Is Done!!", Toast.LENGTH_SHORT).show();
                new AsyncTask<Void, String, String>() {
                    @Override
                    protected String doInBackground(Void... voids) {

                        List<ScanResult> scanResultList = wifiManager.getScanResults();
                        for (int i = 0; i < scanResultList.size(); i++) {
                            scannedResult.add(scanResultList.get(i).SSID + "\n");
                            scannedResult.add(scanResultList.get(i).BSSID + "\n");
                            scannedResult.add(scanResultList.get(i).level + "\n");
                            macAddress.add(scanResultList.get(i).BSSID);
                            rSsi.add(scanResultList.get(i).level);
                        }

                        System.out.print(macAddress);
                        System.out.print(rSsi);

                        //List brssi = bSsidrssi.keySet();

                        List brssi = new ArrayList();
                        //System.out.print(brssi + "I am here345    ");
                        brssi.addAll(bSsidrssi.keySet());
                        //System.out.print("\n"+bSsidrssi+"\n");
                        //TRYING COMPARING THE LIST
                        //System.out.print(brssi.get(0) + "  nigga \n");
                        List sAme = new ArrayList();
                        for (int i = 0; i < brssi.size(); i++) {
                            boolean srL = mAddr.contains(brssi.get(i)) == false;
                            boolean uk = false;
                            System.out.print("\n" + srL + "," + uk + "\n");
                            //System.out.print(brssi.get(i));
                            //The problem here is it keeps invoking function of if statement even when false
                            if ((mAddr.contains(brssi.get(i)) == false)) {
                                bSsidrssi.get(brssi.get(i)).add(-150);
                                sAme.add(brssi.get(i));
                                //return scannedResult.toString();
                                //System.out.print("\n I am hereeeeee \n");
                            }


                        }

                        //I am running the model over here
                        client.load();
                        //client.locate(macAddress, rSsi);
                        System.out.print("\n"+k +"\n");
                        System.out.print("\n"+bSsidrssi +"\n");
                        outPut+="1";
                        System.out.print(outPut+"\n");
                        return scannedResult.toString();

                    }

                    @Override
                    protected void onPostExecute(String s) {
                        textView.setText(s);
                        //System.out.print(scannedResult);
                    }
                }.execute();
            } else {
                wifiManager.setWifiEnabled(true);
                new AsyncTask<Void, String, String>() {
                    @Override
                    protected String doInBackground(Void... voids) {
                        List<ScanResult> scanResultList = wifiManager.getScanResults();
                        for (int i = 0; i < scanResultList.size(); i++) {
                            scannedResult.add(scanResultList.get(i).SSID + "\n");
                            scannedResult.add(scanResultList.get(i).BSSID + "\n");
                            scannedResult.add(scanResultList.get(i).level + "\n");
                        }
                        return scannedResult.toString();
                    }

                    @Override
                    protected void onPostExecute(String s) {
                        textView.setText(s);
                    }
                }.execute();
            }
        }
    }
    //public void

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (requestCode == 1
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (wifiManager.isWifiEnabled()) {
                textView.setText("");
                new AsyncTask<Void, String, String>() {
                    @Override
                    protected String doInBackground(Void... voids) {
                        List<ScanResult> scanResultList = wifiManager.getScanResults();
                        for (int i = 0; i < scanResultList.size(); i++) {
                            scannedResult.add(scanResultList.get(i).SSID + "\n");
                        }
                        return scannedResult.toString();
                    }

                    @Override
                    protected void onPostExecute(String s) {
                        textView.setText(s);
                    }
                }.execute();
            } else {
                wifiManager.setWifiEnabled(true);
                new AsyncTask<Void, String, String>() {
                    @Override
                    protected String doInBackground(Void... voids) {
                        List<ScanResult> scanResultList = wifiManager.getScanResults();
                        for (int i = 0; i < scanResultList.size(); i++) {
                            scannedResult.add(scanResultList.get(i).SSID + "\n");
                        }
                        return scannedResult.toString();
                    }

                    @Override
                    protected void onPostExecute(String s) {
                        textView.setText(s);
                    }
                }.execute();
            }
        }

    }




    @Override
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

    /*private void classify(final String text) {
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














