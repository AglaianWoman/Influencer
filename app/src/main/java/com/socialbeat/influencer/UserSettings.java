package com.socialbeat.influencer;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class UserSettings extends AppCompatActivity {

    RelativeLayout editprofile,socialmedia,paymenthistory,paymentmethods,shareapp,rateapp,terms,policy;
    String cid,success,message,profileprogress,name,emailid,mobileno,twitter,twitterFollowers,bloglink,blogTraffic,
            instagram,instaFollowers,foi,city,mozrank,overallscore,userimage,overallreach,response;
    TextView pname, pemailid, pcity, poverallreach,pmozrank,poverallscore;
    ImageView puserimage;
    private CoordinatorLayout coordinatorLayout;
    Boolean isInternetPresent = false;
    ConnectionDetector cd;
    public static final String KEY_CID = "cid";
    private ProgressDialog pdialog;
    private String TAG;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_settings);
        //getting customer cid value in shared preference
        SharedPreferences prfs = getSharedPreferences("CID_VALUE", Context.MODE_PRIVATE);
        cid = prfs.getString("valueofcid", "");
        System.out.println("cid :"+cid);

        ActionBar bar = getSupportActionBar();
        assert bar != null;
        bar.setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Settings");

        pname = findViewById(R.id.pusername);
        pemailid = findViewById(R.id.pemail);
        pcity = findViewById(R.id.pcity);
        poverallreach = findViewById(R.id.overallreachstatus);
        pmozrank = findViewById(R.id.mozrankstatus);
        poverallscore = findViewById(R.id.overallscorestatus);
        puserimage = findViewById(R.id.profileimage);

        coordinatorLayout = findViewById(R.id.coordinatorLayout);
        editprofile = findViewById(R.id.settings_edit_profile);
        socialmedia = findViewById(R.id.settings_social_media);
        paymenthistory = findViewById(R.id.settings_payment_history);
        paymentmethods = findViewById(R.id.settings_payment_method);
        policy = findViewById(R.id.setting_policy);
        terms = findViewById(R.id.settings_terms);
        shareapp = findViewById(R.id.settings_share_app);
        rateapp = findViewById(R.id.settings_rate_app);

        cd = new ConnectionDetector(this);
        isInternetPresent = cd.isConnectingToInternet();

        if(cid.length()!=0){
            if (isInternetPresent) {
                profileShownFunction();
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
                TextView textView = sbView.findViewById(android.support.design.R.id.snackbar_text);
                textView.setTextColor(Color.YELLOW);
                snackbar.show();
            }
        }else{
            Toast.makeText(getApplicationContext(), "CID value is Empty", Toast.LENGTH_SHORT).show();
        }

        editprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserSettings.this, MyProfileNew.class);
                startActivity(intent);
            }
        });

        socialmedia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserSettings.this, SocialMediaAuthentication.class);
                startActivity(intent);
            }
        });

        paymentmethods.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserSettings.this, WalletOld.class);
                startActivity(intent);
            }
        });

        paymenthistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserSettings.this, TransactionHistory.class);
                startActivity(intent);
            }
        });

        policy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(UserSettings.this, PrivatePolicy.class);
                startActivity(intent);
            }
        });

        terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserSettings.this, TermsCondition.class);
                startActivity(intent);
            }
        });

        shareapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent i = new Intent(Intent.ACTION_SEND);
                    i.setType("text/plain");
                    i.putExtra(Intent.EXTRA_SUBJECT, "Influencer India");
                    String sAux = "\nInfluencer Indiais an exclusive app for bloggers and content creators who are invited to collaborate and work with leading brands across India.\n\n";
                    sAux = sAux + "https://play.google.com/store/apps/details?id=com.socialbeat.influencer \n\n";
                    i.putExtra(Intent.EXTRA_TEXT, sAux);
                    startActivity(Intent.createChooser(i, "choose one"));
                } catch(Exception e) {
                    //e.toString();
                }
            }
        });

        rateapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Uri uri = Uri.parse("market://details?id=com.socialbeat.influencer");
                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                // To count with Play market backstack, After pressing back button,
                // to taken back to our application, we need to add following flags to intent.
                goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_NEW_DOCUMENT | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                try {
                    startActivity(goToMarket);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://play.google.com/store/apps/details?id=com.socialbeat.influencer")));
                }
            }
        });
    }

    private void profileShownFunction() {
        pdialog = new ProgressDialog(UserSettings.this);
        pdialog.setMessage("Loading...");
        pdialog.setCancelable(false);
        pdialog.show();
        String PROFILE_URL = getResources().getString(R.string.base_url)+getResources().getString(R.string.profile_url);
        Log.v("PROFILE_URL : ",PROFILE_URL);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, PROFILE_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String success) {
                try {
                    JSONObject json = new JSONObject(success);
                    response = json.getString("success");
                    message = json.getString("message");
                    profileprogress = json.getString("profileProgress");
                    name = json.getString("name");
                    emailid = json.getString("emailid");
                    mobileno = json.getString("mobileno");
                    twitter = json.getString("twitter");
                    twitterFollowers = json.getString("twitterFollowers");
                    bloglink = json.getString("bloglink");
                    blogTraffic = json.getString("blogTraffic");
                    instagram = json.getString("instagram");
                    instaFollowers = json.getString("instaFollowers");
                    foi = json.getString("foi");
                    city = json.getString("city");
                    mozrank = json.getString("mozrank");
                    overallreach = json.getString("overallreach");
                    overallscore = json.getString("overallscore");
                    userimage = json.getString("userimage");

                    pname.setText(name);
                    pemailid.setText(emailid);
                    pcity.setText(city);
                    poverallreach.setText(overallreach);
                    pmozrank.setText(mozrank);
                    poverallscore.setText(overallscore);
                    Glide.with(getApplicationContext()).load(userimage)
                            .thumbnail(0.5f)
                            .crossFade()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(puserimage);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                pdialog.dismiss();
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(UserSettings.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put(KEY_CID, cid);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    @Override
    public void onBackPressed() {
        Intent intent  = new Intent(this, NewHomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent  = new Intent(this, NewHomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
