package com.socialbeat.influencer;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MyCampaignsFragment extends AppCompatActivity {

    private CoordinatorLayout coordinatorLayout;
    Context context;
    private ProgressDialog pDialog;
    ListView lv;
    String cid,campid,campname,campapplieddate,campappliedstatus,camppaymentstatus,campdeliverystatus,bloglink,tweetlink,campaignquote;
    Boolean isInternetPresent = false;
    // Connection detector class
    ConnectionDetector cd;
    // URL to get contacts JSON
    private static String url ;

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

    // contacts JSONArray
    JSONArray contacts = null;

    // Hashmap for ListView
    ArrayList<HashMap<String, String>> contactList;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mycamp);

        ActionBar bar = getSupportActionBar();
        bar.setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("My Campaigns");

        SharedPreferences prfs = getSharedPreferences("CID_VALUE", Context.MODE_PRIVATE);
        cid = prfs.getString("valueofcid", "");

        cd = new ConnectionDetector(MyCampaignsFragment.this);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        isInternetPresent = cd.isConnectingToInternet();
        contactList = new ArrayList<HashMap<String, String>>();

        if (cid.length() != 0) {
            if (isInternetPresent) {
                cid = prfs.getString("valueofcid", "");
                url = "https://influencer.in/API/v4/appliedList.php?cid=" + cid + "";
                System.out.println(url);
                lv = (ListView) findViewById(R.id.appliedcampvalues);
                // Listview on item click listener
                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
                        // getting values from selected ListItem
                        String campcid = ((TextView) view.findViewById(R.id.cid))
                                .getText().toString();
                        String ccampid = ((TextView) view.findViewById(R.id.campid))
                                .getText().toString();
                        String ccampname = ((TextView) view.findViewById(R.id.campname))
                                .getText().toString();
                        String ccampapplieddate = ((TextView) view.findViewById(R.id.campapplieddate))
                                .getText().toString();
                        String ccampappliedstatus = ((TextView) view.findViewById(R.id.campappliedstatus))
                                .getText().toString();
                        String ccampaignquote = ((TextView) view.findViewById(R.id.campaignquote))
                                .getText().toString();
                        String campdeliverystatus = ((TextView) view.findViewById(R.id.campdeliverystatus))
                                .getText().toString();
                        String ccamppaymentstatus = ((TextView) view.findViewById(R.id.camppaymentstatus))
                                .getText().toString();
                        String cbloglink = ((TextView) view.findViewById(R.id.bloglink))
                                .getText().toString();
                        String ctweetlink = ((TextView) view.findViewById(R.id.tweetlink))
                                .getText().toString();
                    }
                });
                // Calling async task to get json
                new GetCampaign().execute();
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
            // return v;
        } else {
            Toast.makeText(getApplicationContext(), "User Could not login properly,Please Login", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MyCampaignsFragment.this, LoginActivity.class);
            startActivity(intent);
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
                // this.finish();
                Intent intent  = new Intent(this, NewHomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    /**
     * Async task class to get json by making HTTP call
     * */
    private class GetCampaign extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(MyCampaignsFragment.this);
            pDialog.setMessage("Loading, please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // Creating service handler class instance
            ServiceHandler sh = new ServiceHandler();
            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url, ServiceHandler.GET);
            Log.d("Response: ", "> " + jsonStr);

            if (jsonStr != null) {
                try {

                    String result ="{\"success\":false,\"message\":\"No campaign applied yet\"}";
                    if(jsonStr.equals(result)) {
                        Snackbar snackbar = Snackbar
                                .make(coordinatorLayout, "You have currently not applied to any campaigns. Please check out the Live campaigns.", Snackbar.LENGTH_INDEFINITE)
                                .setAction("Live Campaigns", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent intent= new Intent(getApplication(), NewHomeActivity.class);
                                        startActivity(intent);
                                    }
                                });
                        // Changing message text color
                        snackbar.setActionTextColor(Color.YELLOW);
                        // Changing action button text color
                        View sbView = snackbar.getView();
//                        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
//                        textView.setTextColor(Color.YELLOW);
                        snackbar.show();
                    }

                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    contacts = jsonObj.getJSONArray(TAG_CAMPDATA);

                    // looping through All Contacts
                    for (int i = 0; i < contacts.length(); i++) {
                        JSONObject c = contacts.getJSONObject(i);
                        cid= c.getString(TAG_CID);
                        campid = c.getString(TAG_CAMPID);
                        campname = c.getString(TAG_CAMPNAME);
                        campapplieddate = c.getString(TAG_CAMPAPPLIEDDATE);
                        campappliedstatus = c.getString(TAG_CAMPAPPLIEDSTATUS);
                        camppaymentstatus = c.getString(TAG_CAMPPAYMENTSTATUS);
                        campdeliverystatus = c.getString(TAG_CAMPDELIVERYSTATUS);
                        campaignquote = c.getString(TAG_CAMPQUOTE);
                        bloglink = c.getString(TAG_CAMPBLOGLINK);
                        tweetlink = c.getString(TAG_CAMPTWEETLINK);

                        // tmp hashmap for single contact
                        HashMap<String, String> contact = new HashMap<String, String>();

                        // adding each child node to HashMap key => value
                        contact.put(TAG_CID,cid);
                        contact.put(TAG_CAMPID,campid);
                        contact.put(TAG_CAMPNAME, campname);
                        contact.put(TAG_CAMPAPPLIEDDATE, campapplieddate);
                        contact.put(TAG_CAMPAPPLIEDSTATUS, campappliedstatus);
                        contact.put(TAG_CAMPPAYMENTSTATUS, camppaymentstatus);
                        contact.put(TAG_CAMPQUOTE, campaignquote);
                        contact.put(TAG_CAMPDELIVERYSTATUS, campdeliverystatus);
                        contact.put(TAG_CAMPBLOGLINK, bloglink);
                        contact.put(TAG_CAMPTWEETLINK, tweetlink);

                        // adding contact to contact list
                        contactList.add(contact);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();
            /**
             * Updating parsed JSON data into ListView
             * */
            ListAdapter adapter = new SimpleAdapter(MyCampaignsFragment.this, contactList,
                    R.layout.mycamplist, new String[]{TAG_CAMPNAME, TAG_CAMPAPPLIEDSTATUS,TAG_CAMPAPPLIEDDATE, TAG_CAMPDELIVERYSTATUS, TAG_CAMPPAYMENTSTATUS,TAG_CID,TAG_CAMPID,TAG_CAMPBLOGLINK,TAG_CAMPTWEETLINK,TAG_CAMPQUOTE},
                    new int[]{R.id.campname,R.id.campappliedstatus,R.id.campapplieddate,R.id.campdeliverystatus,R.id.camppaymentstatus,R.id.cid,R.id.campid,R.id.bloglink,R.id.tweetlink,R.id.campaignquote});
            lv.setAdapter(adapter);
        }

    }

}