package com.socialbeat.influencer;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.Settings.Secure;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.*;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class FirstActivity extends AppCompatActivity {

    GoogleCloudMessaging gcm;
    String REG_ID,TAG;
    public static final String PROPERTY_REG_ID = "registration_id";
    private static final String PREFS_NAME = "FirstActivity";
    
    public static final String LOGIN_NAME="LoginFile";
    SharedPreferences.Editor editor;
    String open="haslogin";
    private CoordinatorLayout coordinatorLayout;
    Boolean isInternetPresent = false;
    // Connection detector class
    ConnectionDetector cd;
    Button register,login;
    SharedPreferences pref;
    ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.firstscreen_new);

        if (checkPlayServices()) {
            gcm = GoogleCloudMessaging.getInstance(this);
            REG_ID = getRegistrationId(FirstActivity.this);
            if (REG_ID.isEmpty()) {
                registerInBackground();
            }else {
                //Log.v("SPlash_Device_Key :", REG_ID);
            }
        } else {
            Log.i("checkPlayServices ELSE", "No valid Google Play Services APK found.");
        }
        
        MessageDigest md = null;
        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
            }
        } catch (PackageManager.NameNotFoundException e) {
        } catch (NoSuchAlgorithmException e) {
        }
        Log.i("SecretKey = ", android.util.Base64.encodeToString(md.digest(), android.util.Base64.DEFAULT));
        String android_id = Secure.getString(this.getContentResolver(), Secure.ANDROID_ID);
        System.out.println("Android Device id: "+android_id);

        SharedPreferences prefernce=getSharedPreferences(LOGIN_NAME,MODE_PRIVATE);
        editor =  prefernce.edit();

        register = (Button) findViewById(R.id.signup);
        login = (Button) findViewById(R.id.signin);
        cd = new ConnectionDetector(getApplicationContext());
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        pref = getPreferences(0);
        /**
         * Check Internet status button click event
         * */
        isInternetPresent = cd.isConnectingToInternet();

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isInternetPresent) {
                    editor.putBoolean(open, true);
                    editor.commit();
                    Intent in = new Intent(FirstActivity.this, RegistrationActivityOne.class);
                    startActivity(in);
                } else {
                    Snackbar snackbar = Snackbar
                            .make(coordinatorLayout, "No internet connection!", Snackbar.LENGTH_INDEFINITE)
                            .setAction("SETTINGS", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    startActivity(new Intent(Settings.ACTION_SETTINGS));
                                }
                            });
                    // Changing message text color
                    snackbar.setActionTextColor(Color.RED);
                    // Changing action button text color
                    View sbView = snackbar.getView();
                    TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                    textView.setTextColor(Color.YELLOW);
                    snackbar.show();
                }
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isInternetPresent) {
                    editor.putBoolean(open, true);
                    editor.commit();
                    Intent in = new Intent(FirstActivity.this, LoginActivity.class);
                    startActivity(in);
                } else {
                    Snackbar snackbar = Snackbar
                            .make(coordinatorLayout, "No internet connection!", Snackbar.LENGTH_INDEFINITE)
                            .setAction("SETTINGS", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    startActivity(new Intent(Settings.ACTION_SETTINGS));
                                }
                            });
                    // Changing message text color
                    snackbar.setActionTextColor(Color.RED);
                    // Changing action button text color
                    View sbView = snackbar.getView();
                    TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                    textView.setTextColor(Color.YELLOW);
                    snackbar.show();
                }
            }
        });

        boolean haslogin=prefernce.getBoolean(open,false);
        if(haslogin){
            Intent i = new Intent(FirstActivity.this,NewHomeActivity.class);
            startActivity(i);
        }
    }

    @SuppressLint("StaticFieldLeak")
    private void registerInBackground() {
        new AsyncTask<Void,Void,String>() {
            @Override
            public String doInBackground(Void... params) {
                String msg = "";
                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(FirstActivity.this);
                    }
                    REG_ID = gcm.register(getString(R.string.sender_id));
                    //Log.e("Push Token First", REG_ID);
                    PreferenceManager.setPushCatID(REG_ID, FirstActivity.this);
                    msg = "Device registered, registration ID=" + REG_ID;
                    RegisterPushToken();
                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();
                }
                return msg;
            }
            @Override
            protected void onProgressUpdate(Void... values) {
                super.onProgressUpdate(values);
            }
            @Override
            protected void onPostExecute(String msg) {
                Log.d("msg", "" + msg);
            }
        }.execute(null, null, null);
    }

    private void RegisterPushToken() {
        try {
            final String devicename = Build.BRAND;
            final String devicemodel = Build.MODEL;
            String deviceserial = Build.SERIAL;
            if(deviceserial==null || deviceserial.length()==0) deviceserial = ""+System.currentTimeMillis();
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            final String appversion = pInfo.versionName;
            final String deviceGCMServerkey =  PreferenceManager.getPushCatID(FirstActivity.this);
            SharedPreferences preferences1 = getSharedPreferences("USER_DEVICE_VALUE", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor1 = preferences1.edit();
            editor1.putString("devicename",devicename);
            editor1.putString("devicemodel",devicemodel);
            editor1.putString("deviceserial",deviceserial);
            editor1.putString("appversion",appversion);
            editor1.putString("deviceGCMServerkey",deviceGCMServerkey);
            editor1.apply();


        }catch (Exception e){
            Log.e(TAG, "Exception: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private String getRegistrationId(Context context) {
        final SharedPreferences prefs = getGCMPreferences(context);
        String registrationId = prefs.getString(PROPERTY_REG_ID, "");
        if (registrationId.isEmpty()) {
            Log.i("ELSE", "Registration not found.");
            return "";
        }
        return registrationId;
    }


    private SharedPreferences getGCMPreferences(Context context) {
        // This sample app persists the registration ID in shared preferences, but
        // how you store the registration ID in your app is up to you.
        return getSharedPreferences(FirstActivity.class.getSimpleName(), Context.MODE_PRIVATE);
    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
            } else {
                Log.i("Else", "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }
    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}
