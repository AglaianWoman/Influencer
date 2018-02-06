package com.socialbeat.influencer;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;


public class MyCampDetails extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    // JSON node keys
    private static final String TAG_CAMPDATA = "appliedlist";
    private static final String TAG_CID = "cid";
    private static final String TAG_CAMPID = "campid";
    private static final String TAG_CAMPNAME = "campname";
    private static final String TAG_CAMPAPPLIEDSTATUS = "campappliedstatus";
    private static final String TAG_CAMPAPPLIEDDATE = "campapplieddate";
    private static final String TAG_CAMPDELIVERYSTATUS = "campdeliverystatus";
    private static final String TAG_CAMPPAYMENTSTATUS = "camppaymentstatus";
    private static final String TAG_CAMPQUOTE = "campaignquote";
    private static final String TAG_CAMPBLOGLINK = "bloglink";
    private static final String TAG_CAMPTWEETLINK = "tweetlink";
    final Context context = this;
    Boolean isInternetPresent = false;
    ConnectionDetector cd;
    private CoordinatorLayout coordinatorLayout;
    Spinner deliverystatus;
    String userid,campid1,dstatus,blink,tlink,item;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mycampdetails);


        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        cd = new ConnectionDetector(getApplicationContext());
        isInternetPresent = cd.isConnectingToInternet();

        // getting intent data
        Intent in = getIntent();

        // Get JSON values from previous intent
        String cid= in.getStringExtra(TAG_CID);
        String campid = in.getStringExtra(TAG_CAMPID);
        String campname = in.getStringExtra(TAG_CAMPNAME);
        String campapplieddate = in.getStringExtra(TAG_CAMPAPPLIEDDATE);
        String campappliedstatus = in.getStringExtra(TAG_CAMPAPPLIEDSTATUS);
        String camppaymentstatus = in.getStringExtra(TAG_CAMPPAYMENTSTATUS);
        String campdeliverystatus = in.getStringExtra(TAG_CAMPDELIVERYSTATUS);
        String bloglink =in.getStringExtra(TAG_CAMPBLOGLINK);
        String campaignquote =in.getStringExtra(TAG_CAMPQUOTE);
        String tweetlink = in.getStringExtra(TAG_CAMPTWEETLINK);

        ActionBar bar = getSupportActionBar();
        bar.setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(campname+" Status");
        // Displaying all values on the screen
        final TextView lblid_user = (TextView) findViewById(R.id.cid);
        final TextView lblcamp_id = (TextView) findViewById(R.id.campid);
        TextView lblcamp_name = (TextView) findViewById(R.id.campname);
        TextView lblcamp_applied_date = (TextView) findViewById(R.id.applydate);
        TextView lblcamp_applied_status = (TextView) findViewById(R.id.applystatus);
        TextView lblcamp_payment_status = (TextView) findViewById(R.id.paymentstatus);
        TextView lblcamp_campaign_quote = (TextView) findViewById(R.id.campaignquote);
        final EditText edtblog_link = (EditText) findViewById(R.id.bloglink);
        final EditText edttweet_link = (EditText) findViewById(R.id.twitterlink);
        Button update = (Button) findViewById(R.id.updatebutton);

        Spinner spinner = (Spinner) findViewById(R.id.deliverystatus);
        // Spinner click listener
        spinner.setOnItemSelectedListener(this);
        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();
        categories.add("Pending");
        categories.add("Completed");
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item, categories);
        // Drop down webview style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);

        lblid_user.setText(cid);
        lblcamp_id.setText(campid);
        lblcamp_name.setText(campname);
        lblcamp_applied_date.setText(campapplieddate);
        lblcamp_applied_status.setText(campappliedstatus);
        lblcamp_payment_status.setText(camppaymentstatus);
        lblcamp_campaign_quote.setText(campaignquote);
        //edtcamp_delivery_status.setText(campdeliverystatus);
        edtblog_link.setText(bloglink);
        edttweet_link.setText(tweetlink);

        userid = lblid_user.getText().toString();
        campid1 = lblcamp_id.getText().toString();
  //      dstatus = edtcamp_delivery_status.getText().toString();
        blink = edtblog_link.getText().toString();
        tlink = edttweet_link.getText().toString();
        
       update.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               userid = lblid_user.getText().toString();
               campid1 = lblcamp_id.getText().toString();
      //         dstatus = edtcamp_delivery_status.getText().toString();
               blink = edtblog_link.getText().toString();
               tlink = edttweet_link.getText().toString();
               dstatus = item;
                System.out.println("delivery Status : "+dstatus);
              // Toast.makeText(MyCampDetails.this, "sended value is : "+userid+campid+dstatus+blink+tlink, Toast.LENGTH_LONG).show();
               if (isInternetPresent) {
                   String reqUrl = "http://influencer.in/API/v4/updateDeliveryStatus.php?user_id=" + userid + "&camp_id=" + campid1 +"&status=" + dstatus + "&tweet_link=" + tlink +"&blog_link=" + blink + "";
                   new JSONAsyncTask(reqUrl).execute();
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
    }
    public void onItemSelected (AdapterView < ? > parent, View view,int position, long id){
        // On selecting a spinner item
        item = parent.getItemAtPosition(position).toString();
    }

    public void onNothingSelected (AdapterView < ? > arg0) {
        // TODO Auto-generated method stub
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
            pdia = new ProgressDialog(MyCampDetails.this);
            pdia.setMessage("Please Wait...");
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
                String responsemessage = json.getString("message").toString();
                Toast.makeText(MyCampDetails.this, responsemessage, Toast.LENGTH_LONG).show();
                Intent in = new Intent(MyCampDetails.this, NewHomeActivity.class);
                startActivity(in);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}