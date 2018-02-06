package com.socialbeat.influencer;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.Settings;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.Menu;
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

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class WalletOld extends AppCompatActivity  {

    private CoordinatorLayout coordinatorLayout;
    final Context context = this;
    Boolean isInternetPresent = false;
    ConnectionDetector cd;
    TextView walletamount;
    CardView recharge;
    private static String walleturl;
    ImageView homeicon, mycampicon, walleticon, profileicon, moreicon;
    TextView hometext, mycamptext, wallettext, profiletext, moretext;
    RelativeLayout category_home, category_mycamp, category_wallet, category_proflie, category_settings;
    String custid = "1",TAG,response,message,walletbalance,transcationId,validation;
    private ProgressDialog pdia;
    String cid,mobileno;

    private static final String RECHARGE_URL = "http://influencer.in/API/v2/gratification.php";
    public static final String KEY_MOBILENO = "mobile";
    public static final String KEY_CID = "cid";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.walletold);


        ActionBar bar = getSupportActionBar();
        bar.setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("My Wallet");

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);

        walletamount = (TextView) findViewById(R.id.walletamount);
        recharge =(CardView)findViewById(R.id.cardview1);
        cd = new ConnectionDetector(getApplicationContext());
        isInternetPresent = cd.isConnectingToInternet();

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }



        SharedPreferences prfs = getSharedPreferences("CID_VALUE", Context.MODE_PRIVATE);
        cid = prfs.getString("valueofcid", "");
        mobileno = prfs.getString("mobileno1", "");
        Log.v("MobileNumber : ",mobileno);


        isInternetPresent = cd.isConnectingToInternet();
        if (isInternetPresent) {
            if(custid.length()!=0){
                userinfo();
            }else{
                System.out.println("User Not Login");
            }
            //asign wallet amount
            walletamount.setText( getResources().getString(R.string.rs)+" : "+"00.00");

            recharge.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    rechargeUser();
                }
            });

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

    private void rechargeUser() {
        isInternetPresent = cd.isConnectingToInternet();
        if (isInternetPresent) {
            if (validation == "false") {
                Intent intent = new Intent(WalletOld.this, LoginSmsActivity.class);
                startActivity(intent);
            } else if (validation == "true") {
                pdia = new ProgressDialog(WalletOld.this);
                pdia.setMessage("Please Wait...");
                pdia.setCancelable(false);
                pdia.show();
                StringRequest stringRequest = new StringRequest(Request.Method.POST, RECHARGE_URL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            org.json.JSONObject json = new org.json.JSONObject(response);
                            response = json.getString("success").toString();
                            message = json.getString("message").toString();
                            // System.out.println(response);
                            pdia.dismiss();
                            if (response == "true") {
                                transcationId = json.getString("transcationId").toString();
                                //Toast.makeText(WalletOld.this,message, Toast.LENGTH_SHORT).show();
                                /* Alert Dialog Code Start*/
                                AlertDialog.Builder alert = new AlertDialog.Builder(context);
                                alert.setTitle("Transaction Details"); //Set Alert dialog title here
                                alert.setMessage(message + "." + "Transaction Id:" + transcationId); //Message here
                                alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent(WalletOld.this, NewHomeActivity.class);
                                        startActivity(intent);
                                    }
                                }); //End of alert.setPositiveButton
                                AlertDialog alertDialog = alert.create();
                                alertDialog.show();
                     /* Alert Dialog Code End*/
                            } else if (response == "false") {
                                // Toast.makeText(WalletOld.this,message, Toast.LENGTH_SHORT).show();
                                /* Alert Dialog Code Start*/
                                AlertDialog.Builder alert = new AlertDialog.Builder(context);
                                alert.setTitle("Transaction Details"); //Set Alert dialog title here
                                alert.setMessage(message); //Message here
                                alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent(WalletOld.this, NewHomeActivity.class);
                                        startActivity(intent);
                                    }
                                }); //End of alert.setPositiveButton
                                AlertDialog alertDialog = alert.create();
                                alertDialog.show();
                     /* Alert Dialog Code End*/
                            }
                        } catch (org.json.JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(WalletOld.this, error.toString(), Toast.LENGTH_LONG).show();
                            }
                        }) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put(KEY_MOBILENO, mobileno);
                        params.put(KEY_CID, cid);
                        return params;
                    }
                };
                RequestQueue requestQueue = Volley.newRequestQueue(this);
                requestQueue.add(stringRequest);
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
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(Color.YELLOW);
            snackbar.show();
        }
    }
    private void userinfo() {
        walleturl = "http://www.influencer.in/API/v3/checkwalletbalance.php?cid=" + cid + "&mobile=" + mobileno + "";
        System.out.println(walleturl);
        // Calling async task to get json
        new JSONAsyncTask(walleturl).execute();
    }
    @Override
    public void onBackPressed() {
        // code here to show dialog
        super.onBackPressed();  // optional depending on your needs
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        // getMenuInflater().inflate(R.menu.share, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
                //this.finish();
                Intent intent  = new Intent(this, UserSettings.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
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

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            pdia = new ProgressDialog(WalletOld.this);
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
                org.json.JSONObject json = new org.json.JSONObject(result);
                response = json.getString("success").toString();
                message = json.getString("message").toString();
                validation = json.getString("mobilevalidatation").toString();
                walletbalance = json.getString("wallet").toString();

                //System.out.println(response);
                if (response =="true"){
                    if (validation =="true"){
                        //after validation code
                        walletamount.setText( getResources().getString(R.string.rs)+":"+walletbalance);
                        //Toast.makeText(WalletOld.this,message, Toast.LENGTH_SHORT).show();
                    }else if(validation =="false") {
                       // Toast.makeText(WalletOld.this,message, Toast.LENGTH_SHORT).show();
                    }
                }else if(response =="false") {
                    Toast.makeText(WalletOld.this,message, Toast.LENGTH_SHORT).show();
                }
            } catch (org.json.JSONException e) {
                e.printStackTrace();
            }
        }
    }
}