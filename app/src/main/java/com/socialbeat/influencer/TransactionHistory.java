package com.socialbeat.influencer;

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
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Admin on 04-06-2015.
 */

public class TransactionHistory extends AppCompatActivity {

    private CoordinatorLayout coordinatorLayout;
    Context context;
    private ProgressDialog pDialog;
    ListView lv;
    String cid,transid,status,amount,type,date,campname;
    SharedPreferences.Editor editor;
    Boolean isInternetPresent = false;
    // Connection detector class
    ConnectionDetector cd;
    // URL to get contacts JSON
    private static String url ;

    // JSON node keys
    private static final String TAG_TXNDATA = "transaction";
    private static final String TAG_TTRANSID = "transid";
    private static final String TAG_TSTATUS = "status";
    private static final String TAG_TAMOUNT = "amount";
    private static final String TAG_TTYPE = "type";
    private static final String TAG_TDATE = "date";
    private static final String TAG_TCAMPNAME = "campname";

    // contacts JSONArray
    JSONArray history = null;

    // Hashmap for ListView
    ArrayList<HashMap<String, String>> transactionListvalues;
    @Nullable
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        View v = inflater.inflate(R.layout.mycamp, container, false);
//        context = v.getContext();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.txnhistory);

        ActionBar bar = getSupportActionBar();
        bar.setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Transactions History");

        cd = new ConnectionDetector(TransactionHistory.this);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        SharedPreferences prfs = getSharedPreferences("CID_VALUE", Context.MODE_PRIVATE);
        cid = prfs.getString("valueofcid", "");
        isInternetPresent = cd.isConnectingToInternet();
        transactionListvalues = new ArrayList<HashMap<String, String>>();

        if (isInternetPresent) {
           // cid = "1";
            Log.v("CID Value",cid);
            lv = (ListView) findViewById(R.id.txnhistoryvalues);
            // Calling async task to get json
            new GetTransactionHistory().execute();
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
    @Override
    public void onBackPressed() {
        // code here to show dialog
        // super.onBackPressed();  // optional depending on your needs
        Intent intent  = new Intent(this, UserSettings.class);
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
                Intent intent  = new Intent(this, UserSettings.class);
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
    private class GetTransactionHistory extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(TransactionHistory.this);
            pDialog.setMessage("Loading, please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // Creating service handler class instance
            ServiceHandler sh = new ServiceHandler();
            // Making a request to url and getting response
            String TRANSACTION_HISTORY = getResources().getString(R.string.base_url)+getResources().
                    getString(R.string.transaction_history_url)+"?cid="+cid;
            System.out.println("Transaction History : "+TRANSACTION_HISTORY);
            String jsonStr = sh.makeServiceCall(TRANSACTION_HISTORY, ServiceHandler.GET);
            Log.v("Response: ", "> " + jsonStr);

            if (jsonStr != null) {
                try {

                    String result ="{\"success\":false,\"message\":\"User id is missing\",\"transaction\":[]}";
                    Log.v("Result : ",result);
                    if(jsonStr.equals(result)) {
                        Snackbar snackbar = Snackbar
                                .make(coordinatorLayout, "No Transcations.", Snackbar.LENGTH_INDEFINITE)
                                .setAction("All Campaigns", new View.OnClickListener() {
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
                    history = jsonObj.getJSONArray(TAG_TXNDATA);
                    // looping through All Contacts
                    for (int i = 0; i < history.length(); i++) {
                        JSONObject c = history.getJSONObject(i);
                        transid =  "TXN ID : "+" "+c.getString(TAG_TTRANSID);
                        status = c.getString(TAG_TSTATUS);
                        amount = getResources().getString(R.string.rs)+" "+c.getString(TAG_TAMOUNT);
                        type = c.getString(TAG_TTYPE);
                        date = c.getString(TAG_TDATE);
                        campname = c.getString(TAG_TCAMPNAME);
                        // tmp hashmap for single contact
                        HashMap<String, String> historys = new HashMap<String, String>();
                        // adding each child node to HashMap key => value
                        historys.put(TAG_TTRANSID,transid);
                        historys.put(TAG_TSTATUS, status);
                        historys.put(TAG_TAMOUNT, amount);
                        historys.put(TAG_TTYPE, type);
                        historys.put(TAG_TDATE, date);
                        historys.put(TAG_TCAMPNAME,campname);
                        // adding contact to contact list
                        transactionListvalues.add(historys);
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
            ListAdapter adapter = new SimpleAdapter(TransactionHistory.this, transactionListvalues,
                    R.layout.transactionlist1, new String[]{TAG_TTRANSID, TAG_TAMOUNT, TAG_TDATE,TAG_TCAMPNAME,TAG_TTYPE,TAG_TSTATUS},
                    new int[]{R.id.txid,R.id.txamount,R.id.txdate,R.id.campname,R.id.txtype,R.id.status});
            lv.setAdapter(adapter);
        }
}

}