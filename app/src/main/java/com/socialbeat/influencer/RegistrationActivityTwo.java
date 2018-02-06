package com.socialbeat.influencer;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class RegistrationActivityTwo extends AppCompatActivity {
    private MultiSelectionSpinner multiSelectionSpinner;

    Button save;
    TextView skip;
    EditText blogername, twittername, instagramname, blogtrafic, field;
    Spinner fieldofinterest;
    boolean flg = true;
    String cid,bloggername,message, twitternme, instagramnme, blogtraffic, fieldinterest;
    String spinnervalue;
    ImageView infoicon;
    Boolean isInternetPresent = false;
    ConnectionDetector cd;
    ProgressDialog pdialog;
    long totalSize = 0;
    public static final String KEY_CID = "cid";
    public static final String KEY_BLOGLINK = "bloglink";
    public static final String KEY_TWITTERHANDLE = "twitter";
    public static final String KEY_INSTAGRAMHANDLE = "instagram";
    public static final String KEY_BLOGTRAFRIC = "blogtraffic";
    public static final String KEY_FOI = "foi";

    private CoordinatorLayout coordinatorLayout;
    private String[] fieldinterestvalue =
            {
                    "Lifestyle", "Travel", "Food", "Fashion", "Technology", "Investment", "Sports", "Fitness", "Entertainment", "Management", "Parenting", "Other"
            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration_screen2_new);

        multiSelectionSpinner = (MultiSelectionSpinner) findViewById(R.id.fieldinterest);
        multiSelectionSpinner.setItems(fieldinterestvalue);
        multiSelectionSpinner.setSelection(new int[]{0});
        multiSelectionSpinner.setPrompt("Select Field of Interest");

        save = (Button) findViewById(R.id.update_button);
        skip = (TextView) findViewById(R.id.skip_button);
        blogername = (EditText) findViewById(R.id.blogname);
        twittername = (EditText) findViewById(R.id.twitter);
        instagramname = (EditText) findViewById(R.id.instagram);
        blogtrafic = (EditText) findViewById(R.id.blogtrafic);
        field = (EditText) findViewById(R.id.dummy);
        infoicon = (ImageView) findViewById(R.id.infoicon);
        fieldofinterest = (Spinner) findViewById(R.id.fieldinterest);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        cd = new ConnectionDetector(getApplicationContext());
        isInternetPresent = cd.isConnectingToInternet();

        Typeface myFont = Typeface.createFromAsset(getAssets(), "font/headfont.ttf");
        blogername.setTypeface(myFont);
        Typeface myFont1 = Typeface.createFromAsset(getAssets(), "font/headfont.ttf");
        twittername.setTypeface(myFont1);
        Typeface myFont2 = Typeface.createFromAsset(getAssets(), "font/headfont.ttf");
        instagramname.setTypeface(myFont2);
        Typeface myFont3 = Typeface.createFromAsset(getAssets(), "font/headfont.ttf");
        blogtrafic.setTypeface(myFont3);
        Typeface myFont4 = Typeface.createFromAsset(getAssets(), "font/headfont.ttf");
        field.setTypeface(myFont4);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            cid = extras.getString("CID");
        }

        skip.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(RegistrationActivityTwo.this, "User details registered successfully", Toast.LENGTH_SHORT).show();
                Intent in = new Intent(RegistrationActivityTwo.this, NewHomeActivity.class);
                startActivity(in);
            }
        });

        infoicon.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(RegistrationActivityTwo.this, "Select Your Field of Interest",true);

            }
        });
        save.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isInternetPresent) {
                    flg = true;

                    if (flg) {

                        spinnervalue = multiSelectionSpinner.getSelectedItemsAsString();
                        field.setText(spinnervalue);
                        bloggername = blogername.getText().toString();
                        twitternme = twittername.getText().toString();
                        instagramnme = instagramname.getText().toString();
                        blogtraffic = blogtrafic.getText().toString();
                        fieldinterest = field.getText().toString();
                        new RegisterFileToServer().execute();
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
                    TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                    textView.setTextColor(Color.YELLOW);
                    snackbar.show();
                }
            }

        });
    }
    public void showDialog(Context context, String message, Boolean status) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

            }

        });
        builder.show();
    }

    @Override
    public void onBackPressed() {
        // code here to show dialog
        super.onBackPressed();  // optional depending on your needs
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

        class RegisterFileToServer extends AsyncTask<Void, Integer, String> {
            @Override
            protected void onPreExecute() {
                // setting progress bar to zero
                super.onPreExecute();
                pdialog = new ProgressDialog(RegistrationActivityTwo.this);
                pdialog.setMessage("Loading...");
                pdialog.setCancelable(false);
                pdialog.show();
            }

            @Override
            protected String doInBackground(Void... params) {
                return uploadFile();
            }

            @SuppressWarnings("deprecation")
            private String uploadFile() {
                String responseString = null;
                String REGISTER_URL = getResources().getString(R.string.base_url) + getResources().getString(R.string.updateprofile_url);
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(REGISTER_URL);
                try {
                    AndroidMultiPartEntity entity = new AndroidMultiPartEntity(new AndroidMultiPartEntity.ProgressListener() {
                        @Override
                        public void transferred(long num) {
                            publishProgress((int) ((num / (float) totalSize) * 100));
                        }
                    });

                    // Extra parameters if you want to pass to server
                    entity.addPart(KEY_CID, new StringBody(cid));
                    entity.addPart(KEY_BLOGLINK, new StringBody(bloggername));
                    entity.addPart(KEY_TWITTERHANDLE, new StringBody(twitternme));
                    entity.addPart(KEY_INSTAGRAMHANDLE, new StringBody(instagramnme));
                    entity.addPart(KEY_BLOGTRAFRIC, new StringBody(blogtraffic));
                    entity.addPart(KEY_FOI, new StringBody(fieldinterest));
                    totalSize = entity.getContentLength();
                    httppost.setEntity(entity);
                    // Making server call
                    HttpResponse response = httpclient.execute(httppost);
                    HttpEntity r_entity = response.getEntity();
                    int statusCode = response.getStatusLine().getStatusCode();
                    if (statusCode == 200) {
                        // Server response
                        responseString = EntityUtils.toString(r_entity);
                    } else {
                        responseString = "Error occurred! Http Status Code: " + statusCode;
                    }
                } catch (ClientProtocolException e) {
                    responseString = e.toString();

                } catch (IOException e) {
                    responseString = e.toString();
                }
                return responseString;
            }

            @Override
            public void onPostExecute(String success) {
                try {
                    org.json.JSONObject json = new org.json.JSONObject(success);
                    success = json.getString("success");
                    message = json.getString("message");
                    Log.v("success", success);
                    Log.v("message", message);
                    pdialog.dismiss();

                    if (success == "true") {

                        Toast.makeText(RegistrationActivityTwo.this, message, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(RegistrationActivityTwo.this, NewHomeActivity.class);
                        startActivity(intent);
                    } else if (success == "false") {
                        Toast.makeText(RegistrationActivityTwo.this, message, Toast.LENGTH_SHORT).show();
                    }
                } catch (org.json.JSONException e) {
                    e.printStackTrace();
                }
            }
        }
}