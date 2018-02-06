package com.socialbeat.influencer;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SocialMedia extends AppCompatActivity {
    private MultiSelectionSpinner multiSelectionSpinner;

    Button save;
    EditText blogername, twittername, instagramname,facebookname,youtubename;
    boolean flg = true;
    String cid,bloggername,twitternme, instagramnme, facebooknme, youtubenme,response,message,facebookpage,instagram,twitter,bloglink,youtubehandle;
    Boolean isInternetPresent = false;
    ConnectionDetector cd;
    ProgressDialog pdialog;
    long totalSize = 0;
    public static final String KEY_CID = "cid";
    public static final String KEY_FACEBOOKPAGE = "facebookpage";
    public static final String KEY_INSTAGRAMHANDLE = "instagram";
    public static final String KEY_TWITTERHANDLE = "twitter";
    public static final String KEY_BLOGLINK = "bloglink";
     public static final String KEY_YOUTUBEHANDLE = "youtubehandle";

    private CoordinatorLayout coordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.socialmedianew);
        ActionBar bar = getSupportActionBar();
        bar.setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Social Media Handles");

        //getting customer cid value in shared preference
        SharedPreferences prfs = getSharedPreferences("CID_VALUE", Context.MODE_PRIVATE);
        cid = prfs.getString("valueofcid", "");

        save = (Button) findViewById(R.id.save_button);

        facebookname = (EditText) findViewById(R.id.facebooklink);
        instagramname = (EditText) findViewById(R.id.instagramlink);
        twittername = (EditText) findViewById(R.id.twitterlink);
        blogername = (EditText) findViewById(R.id.bloglink);
        youtubename = (EditText) findViewById(R.id.youtubelink);

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        cd = new ConnectionDetector(getApplicationContext());
        isInternetPresent = cd.isConnectingToInternet();

        if(cid.length()!=0){
            if (isInternetPresent) {
                MyApplication.getInstance().trackEvent("MyProfileDummy Screen", "OnClick", "Track MyProfileDummy Event");
                profileFunction();
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
        }else{
            Toast.makeText(getApplicationContext(), "CID value is Empty", Toast.LENGTH_SHORT).show();
        }

        save.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isInternetPresent) {
                    flg = true;
                    final String facebooknme1 = facebookname.getText().toString();
                    if (facebooknme1.length()>0) {
                        if (!isValidFacebook(facebooknme1)) {
                            flg = false;
                            facebookname.setError("Your link is not in the required format. Can you pls try again ");
                            return;
                        }
                    }
                    final String instagramnme1 = instagramname.getText().toString();
                    if (instagramnme1.length()>0) {
//                        if (!isValidHandle(instagramnme1)) {
//                            flg = false;
//                            instagramname.setError("Your handle is not in the required format. Can you pls try again ");
//                            return;
//                        }
                    }
                    final String twitternme1 = twittername.getText().toString();
                    if (twitternme1.length()>0) {
//                        if (!isValidHandle(twitternme1)) {
//                            flg = false;
//                            twittername.setError("Your handle is not in the required format. Can you pls try again ");
//                            return;
//                        }
                    }
                    final String bloggername1 = blogername.getText().toString();
                    if (bloggername1.length()>0) {
                        if (!isValidFacebook(bloggername1)) {
                            flg = false;
                            blogername.setError("Your link is not in the required format. Can you pls try again ");
                            return;
                        }
                    }
                    final String youtubenme1 = youtubename.getText().toString();
                    if (youtubenme1.length()>0) {
                        if (!isValidFacebook(youtubenme1)) {
                            flg = false;
                            youtubename.setError("Your link is not in the required format. Can you pls try again ");
                            return;
                        }
                    }
                    if (flg) {
                        addhandles();
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
        public void addhandles(){
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            if (blogername.getText().toString().length()>0 && twittername.getText().toString().length()>0 && instagramname.getText().toString().length()>0 &&
                    facebookname.getText().toString().length()>0 && youtubename.getText().toString().length()>0) {
                alertDialogBuilder.setMessage("Save Changes?");
            }else
            {
                alertDialogBuilder.setMessage("Your FB/Blog/Twitter/Instagram Handle is missing. Save anyway?");
            }
            alertDialogBuilder.setCancelable(false);
            alertDialogBuilder.setPositiveButton("SAVE",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            bloggername = blogername.getText().toString();
                            twitternme = twittername.getText().toString();
                            instagramnme = instagramname.getText().toString();
                            facebooknme = facebookname.getText().toString();
                            youtubenme = youtubename.getText().toString();
                            new RegisterFileToServer().execute();
                        }
                    });

            alertDialogBuilder.setNegativeButton("SKIP",new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }
        private boolean isValidFacebook(String website) {
            Boolean cond = Patterns.WEB_URL.matcher(website).matches();
            return cond;
        }
        private boolean isValidHandle(String socialhandle) {
            String HANDLE_PATTERN = "^[A-Za-z][A-Za-z0-9]*([._]?[A-Za-z0-9])";
            Pattern pattern = Pattern.compile(HANDLE_PATTERN);
            Matcher matcher = pattern.matcher(socialhandle);
            return matcher.matches();
        }
        private void profileFunction() {
            pdialog = new ProgressDialog(SocialMedia.this);
            pdialog.setMessage("Loading...");
            pdialog.setCancelable(false);
            pdialog.show();
            String PROFILE_URL = getResources().getString(R.string.base_url) + getResources().getString(R.string.profile_url);
            Log.v("PROFILE_URL : ", PROFILE_URL);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, PROFILE_URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String success) {
                    try {
                        JSONObject json = new JSONObject(success);
                        response = json.getString("success");
                        message = json.getString("message");
                        facebookpage = json.getString("facebookpage");
                        instagram = json.getString("instagram");
                        twitter = json.getString("twitter");
                        bloglink = json.getString("bloglink");
                        youtubehandle = json.getString("youtubehandle");

                        //set value to edittext box
                        facebookname.setText(facebookpage);
                        instagramname.setText(instagram);
                        twittername.setText(twitter);
                        blogername.setText(bloglink);
                        youtubename.setText(youtubehandle);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    pdialog.dismiss();
                }
            },

                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(SocialMedia.this, error.toString(), Toast.LENGTH_LONG).show();
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
        // code here to show dialog
        super.onBackPressed();  // optional depending on your needs
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
//            case R.id.nav_add:
//                Intent intentgo = new Intent(SocialMedia.this, SocialMediaAuth.class);
//                startActivity(intentgo);
////                Toast.makeText(MainActivity.this, "Notification Clicked", Toast.LENGTH_LONG).show();
//                return true;
            case android.R.id.home:
                Intent intent = new Intent(SocialMedia.this, MyProfileDummy.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @SuppressLint("StaticFieldLeak")
    class RegisterFileToServer extends AsyncTask<Void, Integer, String> {
        @Override
        protected void onPreExecute() {
            // setting progress bar to zero
            super.onPreExecute();
            pdialog = new ProgressDialog(SocialMedia.this);
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
                entity.addPart(KEY_FACEBOOKPAGE, new StringBody(facebooknme));
                entity.addPart(KEY_YOUTUBEHANDLE, new StringBody(youtubenme));
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

                    Toast.makeText(SocialMedia.this, message, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SocialMedia.this, MyProfileDummy.class);
                    startActivity(intent);
                } else if (success == "false") {
                    Toast.makeText(SocialMedia.this, message, Toast.LENGTH_SHORT).show();
                }
            } catch (org.json.JSONException e) {
                e.printStackTrace();
            }
        }
    }
}