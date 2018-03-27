package com.socialbeat.influencer;

/**
 * Created by SocialBeat on 27-03-2018.
 */

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
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
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class CampBrief extends AppCompatActivity {

    private CoordinatorLayout coordinatorLayout;
    private CollapsingToolbarLayout collapsingToolbarLayout = null;
    final Context context = this;
    Boolean isInternetPresent = false;
    ConnectionDetector cd;
    String ssuccess,smessage,scampName,scampTemplateBrief,scampCustomLink,scampCustomTags,scampDeliverables,scampDueDate,scampTerms;
    TextView campname,campbrief,camplink,camptags,campdeliverables,campduedate,campterms;
    String cid,cdcampid,TAG;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.campbrieflayout);
        SharedPreferences prfs = getSharedPreferences("CID_VALUE", Context.MODE_PRIVATE);
        cid = prfs.getString("valueofcid", "");
        //bundle values from Campain Fragment
        Bundle extras = getIntent().getExtras();
        if (extras != null) {

            cdcampid = extras.getString("campid");
        }

        ActionBar bar = getSupportActionBar();
        assert bar != null;
        bar.setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        //getSupportActionBar().setTitle(cdcampName);

        cd = new ConnectionDetector(getApplicationContext());
        isInternetPresent = cd.isConnectingToInternet();

        SharedPreferences prfs1 = getSharedPreferences("CID_VALUE", Context.MODE_PRIVATE);
        cid = prfs1.getString("valueofcid", "");

        // Displaying all values on the screen

        campname = findViewById(R.id.campname);
        campbrief = findViewById(R.id.campbrief);
        camplink = findViewById(R.id.camplink);
        camptags = findViewById(R.id.camptags);
        campdeliverables = findViewById(R.id.campdeliverables);
        campduedate = findViewById(R.id.campduedate);
        campterms = findViewById(R.id.campterms);

        if(cid.length() != 0) {
            String APPLY_URL = getResources().getString(R.string.base_url) + getResources().getString(R.string.camp_brief) + "?cid=" + cid + "";
            Log.v("APPLY URL :", APPLY_URL);
            new JSONAsyncTask1(APPLY_URL).execute();
        }
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
            pdia = new ProgressDialog(CampBrief.this);
            pdia.setIcon(R.mipmap.ac_icon);
            pdia.setTitle("Campaign Detail's");
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
                scampName = json.getString("campName");
                scampTemplateBrief = json.getString("campTemplateBrief");
                scampCustomLink = json.getString("campCustomLink");
                scampCustomTags = json.getString("campCustomTags");
                scampDeliverables = json.getString("campDeliverables");
                scampDueDate = json.getString("campDueDate");
                scampTerms = json.getString("campTerms");

                Log.d("Success : ",ssuccess);
                Log.d("message : ",smessage);
                Log.d("campName : ",scampName);
                Log.d("campTemplateBrief : ",scampTemplateBrief);

                Spanned spname = Html.fromHtml( scampName );
                campname.setText(spname);
                campname.setMovementMethod(LinkMovementMethod.getInstance());

                Spanned spcat = Html.fromHtml( scampTemplateBrief );
                campbrief.setText(spcat);
                campbrief.setMovementMethod(LinkMovementMethod.getInstance());

                Spanned spsnote = Html.fromHtml( scampCustomLink );
                camplink.setText(spsnote);
                camplink.setMovementMethod(LinkMovementMethod.getInstance());

                Spanned splnote = Html.fromHtml( scampCustomTags );
                camptags.setText(splnote);
                camptags.setMovementMethod(LinkMovementMethod.getInstance());

                Spanned spgoal = Html.fromHtml( scampDeliverables );
                campdeliverables.setText(spgoal);
                campdeliverables.setMovementMethod(LinkMovementMethod.getInstance());

                Spanned spdos = Html.fromHtml( scampDueDate );
                campduedate.setText(spdos);
                campduedate.setMovementMethod(LinkMovementMethod.getInstance());

                Spanned spdonts = Html.fromHtml( scampTerms );
                campterms.setText(spdonts);
                campterms.setMovementMethod(LinkMovementMethod.getInstance());

                camplink.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ClipboardManager cm = (ClipboardManager)context.getSystemService(Context.CLIPBOARD_SERVICE);
                        cm.setText(camplink.getText());
                        Toast.makeText(context, "Copied to clipboard", Toast.LENGTH_SHORT).show();
                    }
                });

                camptags.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ClipboardManager cm = (ClipboardManager)context.getSystemService(Context.CLIPBOARD_SERVICE);
                        cm.setText(camptags.getText());
                        Toast.makeText(context, "Copied to clipboard", Toast.LENGTH_SHORT).show();
                    }
                });

            } catch (JSONException e) {
                e.printStackTrace();
                MyApplication.getInstance().trackException(e);
                Log.e(TAG, "Exception: " + e.getMessage());
            }
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
}
