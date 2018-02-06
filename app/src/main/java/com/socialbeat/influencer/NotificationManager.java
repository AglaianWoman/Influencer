package com.socialbeat.influencer;

import android.app.ProgressDialog;
import android.content.Context;
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
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class NotificationManager extends AppCompatActivity {

    private static final String TAG = NotificationManager.class.getSimpleName();
    private CoordinatorLayout coordinatorLayout;
    Context context;
    private ProgressDialog pDialog;
    ListView notifylistview;
    // String cid,campid,campname,campapplieddate,campappliedstatus,camppaymentstatus,campdeliverystatus,bloglink,tweetlink;
    String cid,notificationid,notificationtype,message,datetime;
    SharedPreferences.Editor editor;
    Boolean isInternetPresent = false;
    // Connection detector class
    ConnectionDetector cd;
    // URL to get notification JSON
    private static String url ;

    // JSON node keys
    private static final String TAG_NOTIFYDATA = "notification";
    private static final String TAG_NID = "notificationid";
    private static final String TAG_NMESSAGE = "message";
    private static final String TAG_NTYPE = "notificationtype";
    private static final String TAG_NDATETIME = "datetime";

    // notification JSONArray
    JSONArray notification = null;

    // Hashmap for ListView
    ArrayList<HashMap<String, String>> notificationList;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notificationwindowmsg);

        ActionBar bar = getSupportActionBar();
        bar.setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Notifications");


        cd = new ConnectionDetector(NotificationManager.this);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        SharedPreferences prfs = getSharedPreferences("CID_VALUE", Context.MODE_PRIVATE);
        isInternetPresent = cd.isConnectingToInternet();
        notificationList = new ArrayList<HashMap<String, String>>();


        if (isInternetPresent) {
            cid = prfs.getString("valueofcid", "");
            String Notification_url = getResources().getString(R.string.base_url)+getResources().getString(R.string.notification_manager_url);
            url = Notification_url+"?cid="+ cid ;
            System.out.println(url);
            notifylistview = (ListView) findViewById(R.id.notificationwindowlist);


            // Listview on item click listener
            notifylistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {

                    String notificationid = ((TextView) view.findViewById(R.id.notificationid)).getText().toString();
                    String message = ((TextView) view.findViewById(R.id.message)).getText().toString();
                    String notificationtype = ((TextView) view.findViewById(R.id.notificationtype)).getText().toString();
                    String datetime = ((TextView) view.findViewById(R.id.datetime)).getText().toString();
                   // String campid = ((TextView) view.findViewById(R.id.datetime)).getText().toString();


                    if(!notificationtype.isEmpty()) {

                        //  Toast.makeText(Notificationwindow.this, notificationid+ "  "+message+ "  "+notificationtype+ "  "+datetime, Toast.LENGTH_LONG).show();
                        if (notificationtype.equalsIgnoreCase("campaigndetail")) {
                            Log.v("Notification Type : ", "campaigndetail");
                            Intent in = new Intent(NotificationManager.this, NewHomeActivity.class);
                            startActivity(in);
                        } else if (notificationtype.equalsIgnoreCase("profilesocial")) {
                            Log.v("Notification Type : ", "profilesocial");
                            Intent in = new Intent(NotificationManager.this, MyProfileDummy.class);
                            startActivity(in);
                        } else if (notificationtype.equalsIgnoreCase("profileaccount")) {
                            Log.v("Notification Type : ", "profileaccount");
                            Intent in = new Intent(NotificationManager.this, MyProfileNew.class);
                            startActivity(in);
                        } else if (notificationtype.equalsIgnoreCase("appliedstatus")) {
                            Log.v("Notification Type : ", "appliedstatus");
                            Intent in = new Intent(NotificationManager.this, MyCampaignsFragment.class);
                            startActivity(in);
                        } else if (notificationtype.equalsIgnoreCase("approvedstatus")) {
                            Log.v("Notification Type : ", "approvedstatus");
                            Intent in = new Intent(NotificationManager.this, MyCampaignsFragment.class);
                            startActivity(in);
                        } else if (notificationtype.equalsIgnoreCase("list")) {
                            Log.v("Notification Type : ", "list");
                            Intent in = new Intent(NotificationManager.this, NewHomeActivity.class);
                            startActivity(in);
                        }
                    }
                }
            });
            // Calling async task to get json
            new GetCampaign().execute();

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
        // return v;
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
                // this.finish();
                Intent intent  = new Intent(NotificationManager.this, NewHomeActivity.class);
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
            pDialog = new ProgressDialog(NotificationManager.this);
            pDialog.setIcon(R.mipmap.notificationflaticon40);
            pDialog.setTitle("Notifications");
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
                    String result ="{\"success\":false,\"message\":\"You have not received the notification yet\"}";
                    Log.v("RESULT : ",result);
                    if(jsonStr.equals(result)) {
                        Snackbar snackbar = Snackbar
                                .make(coordinatorLayout, "You have not received the notification yet.", Snackbar.LENGTH_INDEFINITE)
                                .setAction("OK", new View.OnClickListener() {
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
                        snackbar.show();
                    }
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    // Getting JSON Array node
                    notification = jsonObj.getJSONArray(TAG_NOTIFYDATA);

                    // looping through All Contacts
                    for (int i = 0; i < notification.length(); i++) {
                        JSONObject c = notification.getJSONObject(i);
                        notificationid = c.getString(TAG_NID);
                        message = c.getString(TAG_NMESSAGE);
                        notificationtype = c.getString(TAG_NTYPE);
                        datetime = c.getString(TAG_NDATETIME);
                        // tmp hashmap for single notice
                        HashMap<String, String> notice = new HashMap<String, String>();
                        // adding each child node to HashMap key => value
                        notice.put(TAG_NID,notificationid);
                        notice.put(TAG_NMESSAGE, message);
                        notice.put(TAG_NTYPE, notificationtype);
                        notice.put(TAG_NDATETIME, datetime);
                        // adding notice to notice list
                        notificationList.add(notice);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e(TAG, "Exception: " + e.getMessage());
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
            ListAdapter adapter = new SimpleAdapter(NotificationManager.this, notificationList,
                    R.layout.notificationlist, new String[]{TAG_NID,TAG_NMESSAGE,TAG_NTYPE,TAG_NDATETIME},
                    new int[]{R.id.notificationid,R.id.message,R.id.notificationtype,R.id.datetime});
            notifylistview.setAdapter(adapter);
        }

    }

}