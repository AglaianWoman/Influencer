package com.socialbeat.influencer;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

public class AllCampDetailsLive extends AppCompatActivity {

    private CoordinatorLayout coordinatorLayout;
    private CollapsingToolbarLayout collapsingToolbarLayout = null;
    final Context context = this;
    Boolean isInternetPresent = false;
    ConnectionDetector cd;
    String ssuccess,smessage,scampImg ,scampName,scampShortNote,scampLongNote,scampCat,scampGoal,scampDos,scampDont,scampBacklink,
            scampTag,scampid,scampApplyTill,scampRewards,scampRewardType,sfixedamount,scampAppliedStatus;
    Button applynow,alreadyapplied,campaignclosed,approved,notapproved,submit,close;
    TextView campName,campShortNote,campCat,campLongNote,campGoal,campDos,campDont,campBacklink,
            campTag,campid,campApplyTill,campRewards,campRewardType,fixedamount,conditiontext,cancel,infotext;
    ImageView campImg;
    EditText famtval;
    boolean flg = true;
    String famt,cid,cdcampid,cdcampRewardType,cdfixedamount,TAG;
    int first,second;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.allcampdetails);
        SharedPreferences prfs = getSharedPreferences("CID_VALUE", Context.MODE_PRIVATE);
        cid = prfs.getString("valueofcid", "");
        //bundle values from Campain Fragment
        Bundle extras = getIntent().getExtras();
        if (extras != null) {

            cdcampid = extras.getString("campid");
        }

        ActionBar bar = getSupportActionBar();
        bar.setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        cd = new ConnectionDetector(getApplicationContext());
        isInternetPresent = cd.isConnectingToInternet();

        SharedPreferences prfs1 = getSharedPreferences("CID_VALUE", Context.MODE_PRIVATE);
        cid = prfs1.getString("valueofcid", "");

        // Displaying all values on the screen

        campName = findViewById(R.id.campName);
        campShortNote = findViewById(R.id.campShortNote);
        campCat = findViewById(R.id.campCat);
        campLongNote = findViewById(R.id.campLongNote);
        campGoal = findViewById(R.id.campGoal);
        campDos = findViewById(R.id.campDos);
        campDont = findViewById(R.id.campDont);
        campBacklink = findViewById(R.id.campBacklink);
        campTag = findViewById(R.id.campTag);
        campid = findViewById(R.id.campid);
        campApplyTill = findViewById(R.id.campApplyTill);
        campRewards = findViewById(R.id.campRewards);
        campRewardType = findViewById(R.id.campRewardType);
        fixedamount = findViewById(R.id.fixedamount);
        coordinatorLayout = findViewById(R.id.coordinatorLayout);
        campImg = findViewById(R.id.campImg);
        campaignclosed = findViewById(R.id.campaignclosed);
        alreadyapplied = findViewById(R.id.alreadyapplied);
        applynow = findViewById(R.id.applynow);
        approved = findViewById(R.id.approved);
        notapproved = findViewById(R.id.notapproved);

        if(cid.length() != 0) {
            String APPLY_URL = getResources().getString(R.string.base_url) + getResources().getString(R.string.live_Camp_detail) + "?cid=" + cid + "&campid=" + cdcampid + "";
            Log.v("APPLY URL :", APPLY_URL);
            new JSONAsyncTask1(APPLY_URL).execute();
        }

        campaignclosed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AllCampDetailsLive.this, "This Campaign is closed already.", Toast.LENGTH_LONG).show();
            }
        });

        alreadyapplied.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AllCampDetailsLive.this, "You have already applied to this campaign", Toast.LENGTH_LONG).show();
            }
        });

        notapproved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AllCampDetailsLive.this, "You are Not Approved for this Campaign", Toast.LENGTH_LONG).show();
            }
        });

        approved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isInternetPresent) {
                    Intent in = new Intent(AllCampDetailsLive.this, CampBrief.class);
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
                        TextView textView = sbView.findViewById(android.support.design.R.id.snackbar_text);
                        textView.setTextColor(Color.YELLOW);
                        snackbar.show();
                    }
                    }
        });

        applynow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isInternetPresent) {
                    if (cid.length() != 0) {

                        if (cdcampRewardType.equalsIgnoreCase("probono")) {
                            Log.v("Campaign Type : ", "Probono");
                            String reqYesUrl = getResources().getString(R.string.base_url) + getResources().getString(R.string.apply_camp_without_quote_url) + "?cid=" + cid + "&campid=" + cdcampid + "&os=Android";
                            new JSONAsyncTaskNew(reqYesUrl).execute();

                        } else if (cdcampRewardType.equalsIgnoreCase("barter")) {
                            Log.v("Campaign Type : ", "Barter");
                            String reqYesUrl = getResources().getString(R.string.base_url) + getResources().getString(R.string.apply_camp_without_quote_url) + "?cid=" + cid + "&campid=" + cdcampid + "&os=Android";
                            new JSONAsyncTaskNew(reqYesUrl).execute();
                        } else if (cdcampRewardType.equalsIgnoreCase("quote")) {
                            Log.v("Campaign Type : ", "Quote");
                            final Dialog dialog = new Dialog(AllCampDetailsLive.this);
                            dialog.setContentView(R.layout.quoteamount);
                            dialog.setCancelable(false);
                            dialog.setTitle("Quote Amount");

                            submit = dialog.findViewById(R.id.okbutton);
                            famtval = dialog.findViewById(R.id.fquoteamt);
                            cancel = dialog.findViewById(R.id.cancel_button);
                            infotext = dialog.findViewById(R.id.infotext);

                            Typeface myFont = Typeface.createFromAsset(getAssets(), "font/rock.ttf");
                            infotext.setTypeface(myFont);
                            isInternetPresent = cd.isConnectingToInternet();

                            submit.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    flg = true;
                                    if (isInternetPresent) {
                                        famt = famtval.getText().toString();
                                        Log.v("Quoted Amount Value : ",famt);
                                        String reqUrl = getResources().getString(R.string.base_url) + getResources().getString(R.string.apply_camp_with_quote_url) + "?cid=" + cid + "&campid=" + cdcampid + "&quote=" + famt + "";
                                        new JSONAsyncTask(reqUrl).execute();
                                        Log.v("Quote URL : ", reqUrl);

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
                                }
                            });
                            cancel.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                }
                            });
                            dialog.show();
                        } else if(cdcampRewardType.equalsIgnoreCase("fixedamount") ){
                            Log.v("Campaign Type : ","Fixed Amount");
                            final Dialog dialog = new Dialog(AllCampDetailsLive.this);
                            dialog.setContentView(R.layout.quoteamounts);
                            dialog.setCancelable(false);
                            dialog.setTitle("Quote Amount");

                            submit = dialog.findViewById(R.id.okbutton);
                            famtval = dialog.findViewById(R.id.femailid);
                            cancel = dialog.findViewById(R.id.cancel_button);
                            infotext = dialog.findViewById(R.id.infotext);
                            conditiontext = dialog.findViewById(R.id.conditiontext);
                            Typeface myFont = Typeface.createFromAsset(getAssets(), "font/rock.ttf");
                            infotext.setTypeface(myFont);
                            Typeface myFont1 = Typeface.createFromAsset(getAssets(), "font/rock.ttf");
                            conditiontext.setTypeface(myFont1);

                            isInternetPresent = cd.isConnectingToInternet();

                            if (cdfixedamount.length()>0) {
                                conditiontext.setText("(Maximum Quote amount for this campaign is RS : " + cdfixedamount.toString()+" ,so give your quote lessthan maximum amount.)");
                            }else{
                                conditiontext.setText("(No Quote Amount Condition for this Campaign.)");
                            }

                            submit.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    flg = true;
                                    if (isInternetPresent) {
                                        famt = famtval.getText().toString();
                                        Log.v("quotevalue : ",famt);
                                        if (cdfixedamount != null && cdfixedamount.trim().length() != 0) {

                                            if (famt != null && famt.trim().length() != 0){

                                                first = Integer.parseInt(cdfixedamount);
                                                second = Integer.parseInt(famt);
                                                System.out.println("Fixed Amount value : "+ first);
                                                System.out.println("Quoted Amount value :"+ second);
                                                if (first >= second) {
                                                    String reqUrl = getResources().getString(R.string.base_url)+getResources().getString(R.string.apply_camp_with_quote_url)+"?cid=" + cid + "&campid=" + cdcampid + "&quote=" + famt + "";
                                                    new JSONAsyncTask(reqUrl).execute();
                                                    Log.v("Responsce execution : ",reqUrl);
                                                }else{
                                                    Toast.makeText(AllCampDetailsLive.this, "Your quote amount is greater than Maximum Amount,so give your quote lessthan maximum amount. ", Toast.LENGTH_LONG).show();
                                                }
                                            }else{
                                                Toast.makeText(AllCampDetailsLive.this, "Please enter the amount you would like to charge for this campaign.", Toast.LENGTH_LONG).show();
                                            }

                                        }else{
                                            //Toast.makeText(AllCampDetailsLive.this, "No minimum Value ", Toast.LENGTH_LONG).show();
                                        Log.v("Message : ","No minimum Value");
                                        }

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
                                }
                            });
                            cancel.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                }
                            });

                            dialog.show();
                        } else {
                            Toast.makeText(AllCampDetailsLive.this, "Campaign Categeory could not mention.", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(AllCampDetailsLive.this, "CID value is Empty", Toast.LENGTH_LONG).show();
                    }
                }else {
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
                }
        });

        }

    class JSONAsyncTask1 extends AsyncTask<String, Void, String> {

        String url;
        String response = "";
        JSONAsyncTask1(String url) {
            this.url = url;
        }

        public String excutePost(String targetURL, String urlParameters) {
            HttpURLConnection connection = null;
            try {
                //Create connection
                URL url = new URL(targetURL);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("Content-Type","application/json");
                connection.setUseCaches(false);
                connection.setDoOutput(true);
                //Get Response
                InputStream is = connection.getInputStream();
                BufferedReader rd = new BufferedReader(new InputStreamReader(is));
                StringBuilder response = new StringBuilder(); // or StringBuffer if not Java 5+
                String line;
                while ((line = rd.readLine()) != null) {
                    response.append(line);
                    response.append('\r');
                }
                rd.close();
                return response.toString();
            } catch (Exception e) {
                e.printStackTrace();
                MyApplication.getInstance().trackException(e);
                Log.e(TAG, "Exception: " + e.getMessage());
                return null;
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
        }
        private ProgressDialog pdia;

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            pdia = new ProgressDialog(AllCampDetailsLive.this);
            pdia.setIcon(R.mipmap.ac_icon);
            //pdia.setTitle(cdcampName+" Campaign");
            pdia.setMessage( "Loading, please wait...");
            pdia.setCancelable(false);
            pdia.show();
        }

        @Override
        protected String doInBackground(String... urls) {
            url = url.replaceAll(" ", "%20");
            return excutePost(url,"");
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pdia.dismiss();
            try {
                JSONObject json = new JSONObject(result);

                 ssuccess = json.getString("success");
                 smessage = json.getString("message");
                 scampImg = json.getString("campImg");
                 scampName = json.getString("campName");
                 scampShortNote = json.getString("campShortNote");
                 scampLongNote = json.getString("campLongNote");
                 scampCat = json.getString("campCat");
                 scampGoal = json.getString("campGoal");
                 scampDos = json.getString("campDos");
                 scampDont = json.getString("campDont");
                 scampBacklink = json.getString("campBacklink");
                 scampTag = json.getString("campTag");
                 scampid = json.getString("campid");
                 scampApplyTill = json.getString("campApplyTill");
                 scampRewards = json.getString("campRewards");
                 scampRewardType = json.getString("campRewardType");
                 sfixedamount = json.getString("fixedamount");
                 scampAppliedStatus = json.getString("campAppliedStatus");

                System.out.println(scampAppliedStatus);
                if(scampAppliedStatus.equalsIgnoreCase("new")){
                    applynow.setVisibility(View.VISIBLE);
                    campaignclosed.setVisibility(View.INVISIBLE);
                    alreadyapplied.setVisibility(View.INVISIBLE);
                    approved.setVisibility(View.INVISIBLE);
                    notapproved.setVisibility(View.INVISIBLE);
                }
                else if(scampAppliedStatus .equalsIgnoreCase("Applied")){
                    alreadyapplied.setVisibility(View.VISIBLE);
                    applynow.setVisibility(View.INVISIBLE);
                    campaignclosed.setVisibility(View.INVISIBLE);
                    approved.setVisibility(View.INVISIBLE);
                    notapproved.setVisibility(View.INVISIBLE);
                }
                else if(scampAppliedStatus .equalsIgnoreCase("Campaign closed")){
                    campaignclosed.setVisibility(View.VISIBLE);
                    alreadyapplied.setVisibility(View.INVISIBLE);
                    applynow.setVisibility(View.INVISIBLE);
                    approved.setVisibility(View.INVISIBLE);
                    notapproved.setVisibility(View.INVISIBLE);
                }
                else if(scampAppliedStatus .equalsIgnoreCase("Approved")){
                    approved.setVisibility(View.VISIBLE);
                    campaignclosed.setVisibility(View.INVISIBLE);
                    alreadyapplied.setVisibility(View.INVISIBLE);
                    applynow.setVisibility(View.INVISIBLE);
                    notapproved.setVisibility(View.INVISIBLE);
                }
                else if(scampAppliedStatus .equalsIgnoreCase("Not approved")){
                    notapproved.setVisibility(View.VISIBLE);
                    campaignclosed.setVisibility(View.INVISIBLE);
                    alreadyapplied.setVisibility(View.INVISIBLE);
                    applynow.setVisibility(View.INVISIBLE);
                    approved.setVisibility(View.INVISIBLE);
                }

        campid.setText(scampid);

        campImg.setImageResource(R.mipmap.influencerlistimg);
        new DownloadImageTask(campImg).execute(scampImg);

        Spanned spname = Html.fromHtml( scampName );
        campName.setText(spname);
        campName.setMovementMethod(LinkMovementMethod.getInstance());

        Spanned spcat = Html.fromHtml( scampCat );
        campCat.setText(spcat);
        campCat.setMovementMethod(LinkMovementMethod.getInstance());

        Spanned spsnote = Html.fromHtml( scampShortNote );
        campShortNote.setText(spsnote);
        campShortNote.setMovementMethod(LinkMovementMethod.getInstance());

        Spanned splnote = Html.fromHtml( scampLongNote );
        campLongNote.setText(splnote);
        campLongNote.setMovementMethod(LinkMovementMethod.getInstance());

        Spanned spgoal = Html.fromHtml( scampGoal );
        campGoal.setText(spgoal);
        campGoal.setMovementMethod(LinkMovementMethod.getInstance());

        Spanned spdos = Html.fromHtml( scampDos );
        campDos.setText(spdos);
        campDos.setMovementMethod(LinkMovementMethod.getInstance());

        Spanned spdonts = Html.fromHtml( scampDont );
        campDont.setText(spdonts);
        campDont.setMovementMethod(LinkMovementMethod.getInstance());


        Spanned spapply = Html.fromHtml( scampApplyTill );
        campApplyTill.setText(spapply);
        campApplyTill.setMovementMethod(LinkMovementMethod.getInstance());

        Spanned spreward = Html.fromHtml( scampRewards );
        campRewards.setText(spreward);
        campRewards.setMovementMethod(LinkMovementMethod.getInstance());

        Spanned sprtype = Html.fromHtml( scampRewardType );
        campRewardType.setText(sprtype);
        campRewardType.setMovementMethod(LinkMovementMethod.getInstance());

        Spanned sptag = Html.fromHtml( scampTag );
        campTag.setText(sptag);
        campTag.setMovementMethod(LinkMovementMethod.getInstance());

        String link ="<a href="+scampBacklink+">"+ scampBacklink +" </a>" ;
        Spanned splink = Html.fromHtml( link );
        campBacklink.setText(splink);
        campBacklink.setMovementMethod(LinkMovementMethod.getInstance());

        fixedamount.setText(sfixedamount);
        System.out.println("cfixedamount value  :"+ sfixedamount);
        if (sfixedamount.length()>0) {
            fixedamount.setText("Maximum amount Rs : " + sfixedamount);
        }else{
            fixedamount.setText("No Quote Amount Condition for this Campaign.");
        }
            } catch (JSONException e) {
                e.printStackTrace();
                MyApplication.getInstance().trackException(e);
                Log.e(TAG, "Exception: " + e.getMessage());
            }
        }
    }


    @SuppressLint("StaticFieldLeak")
    class JSONAsyncTask extends AsyncTask<String, Void, String> {

        String url;
        String response = "";
        JSONAsyncTask(String url) {
            this.url = url;
        }

        public String excutePost(String targetURL, String urlParameters) {
            HttpURLConnection connection = null;
            try {
                //Create connection
                URL url = new URL(targetURL);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("Content-Type","application/json");
                connection.setUseCaches(false);
                connection.setDoOutput(true);

                //Get Response
                InputStream is = connection.getInputStream();
                BufferedReader rd = new BufferedReader(new InputStreamReader(is));
                StringBuilder response = new StringBuilder(); // or StringBuffer if not Java 5+
                String line;
                while ((line = rd.readLine()) != null) {
                    response.append(line);
                    response.append('\r');
                }
                rd.close();
                return response.toString();
            } catch (Exception e) {
                e.printStackTrace();
                MyApplication.getInstance().trackException(e);
                Log.e(TAG, "Exception: " + e.getMessage());
                return null;
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
        }
        private ProgressDialog pdia;

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            pdia = new ProgressDialog(AllCampDetailsLive.this);
            pdia.setMessage("Please Wait...");
            pdia.show();
            pdia.setCancelable(false);
        }

        @Override
        protected String doInBackground(String... urls) {
            url = url.replaceAll(" ", "%20");
            return excutePost(url,"");
        }
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pdia.dismiss();
            try {
                JSONObject json = new JSONObject(result);
                String responsemessage = json.getString("message").toString();
                Toast.makeText(AllCampDetailsLive.this, responsemessage, Toast.LENGTH_LONG).show();
                finish();
            } catch (JSONException e) {
                e.printStackTrace();
                MyApplication.getInstance().trackException(e);
                Log.e(TAG, "Exception: " + e.getMessage());
            }
        }
    }

private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
    ImageView bmImage;
    public DownloadImageTask(ImageView bmImage) {
        this.bmImage = bmImage;
    }
    protected Bitmap doInBackground(String... urls) {
        String urldisplay = urls[0];
        Bitmap mIcon11 = null;
        try {
            InputStream in = new java.net.URL(urldisplay).openStream();
            mIcon11 = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
            MyApplication.getInstance().trackException(e);
            Log.e(TAG, "Exception: " + e.getMessage());
        }
        return mIcon11;
    }
    protected void onPostExecute(Bitmap result) {
        bmImage.setImageBitmap(result);
    }
}

    @Override
    public void onBackPressed() {
        // code here to show dialog
        // super.onBackPressed();  // optional depending on your needs
        Intent intent  = new Intent(this, NewHomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
                Intent intent  = new Intent(this, NewHomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    class JSONAsyncTaskNew extends AsyncTask<String, Void, String> {
        String url;
        String response = "";
        JSONAsyncTaskNew(String url) {
            this.url = url;
        }

        public String excutePost(String targetURL, String urlParameters) {
            HttpURLConnection connection = null;
            try {
                //Create connection
                URL url = new URL(targetURL);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("Content-Type","application/json");
                connection.setUseCaches(false);
                connection.setDoOutput(true);

                //Get Response
                InputStream is = connection.getInputStream();
                BufferedReader rd = new BufferedReader(new InputStreamReader(is));
                StringBuilder response = new StringBuilder(); // or StringBuffer if not Java 5+
                String line;
                while ((line = rd.readLine()) != null) {
                    response.append(line);
                    response.append('\r');
                }
                rd.close();
                return response.toString();
            } catch (Exception e) {
                e.printStackTrace();
                MyApplication.getInstance().trackException(e);
                Log.e(TAG, "Exception: " + e.getMessage());
                return null;
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
        }
        private ProgressDialog pdia;

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            pdia = new ProgressDialog(AllCampDetailsLive.this);
            pdia.setMessage("Please Wait...");
            pdia.show();
            pdia.setCancelable(false);
        }

        @Override
        protected String doInBackground(String... urls) {
            url = url.replaceAll(" ", "%20");
            return excutePost(url,"");
        }
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pdia.dismiss();
            try {
                JSONObject json = new JSONObject(result);
                String responsemessage = json.getString("message").toString();
                Toast.makeText(AllCampDetailsLive.this, responsemessage, Toast.LENGTH_LONG).show();
                finish();
            } catch (JSONException e) {
                e.printStackTrace();
                MyApplication.getInstance().trackException(e);
                Log.e(TAG, "Exception: " + e.getMessage());
            }
        }
    }
}
