package com.socialbeat.influencer;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;


public class InstagramPage extends AppCompatActivity {

    private static final String TAG = InstagramPage.class.getSimpleName();
    private CoordinatorLayout coordinatorLayout;
    Boolean isInternetPresent = false;
    // Connection detector class
    ConnectionDetector cd;
    TextView campheading;
    Context context;
    WebView browser;
    String sname;
    // Movies json url
    private ProgressDialog pDialog;
    @Nullable
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview);

        ActionBar bar = getSupportActionBar();
        bar.setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Instagram");

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            sname = extras.getString("sname");
            Log.v("Insta name : ",sname);
        }

        cd = new ConnectionDetector(this);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        isInternetPresent = cd.isConnectingToInternet();
        if (isInternetPresent) {

            pDialog = new ProgressDialog(this);
            // Showing progress dialog before making http request
            pDialog.setMessage("Loading..");
            pDialog.setCancelable(false);
            pDialog.show();


//            Uri uri = Uri.parse("http://instagram.com/_u/"+sname);
//            Intent likeIng = new Intent(Intent.ACTION_VIEW, uri);
//
//            likeIng.setPackage("com.instagram.android");
//
//            try {
//                startActivity(likeIng);
//            } catch (ActivityNotFoundException e) {
//                startActivity(new Intent(Intent.ACTION_VIEW,
//                        Uri.parse("http://instagram.com/"+sname)));
//            }


   //         Toast.makeText(OurBlogPage.this, "Hang on.. we are bringing you some interesting content.", Toast.LENGTH_LONG).show();
           // final SwipeRefreshLayout swipeView =(SwipeRefreshLayout)findViewById(R.id.swipe);
            browser = (WebView)findViewById(R.id.webview);
            browser.setWebViewClient(new MyBrowser());
            browser.loadUrl("https://www.instagram.com/"+sname);
            Log.v("Web url : ","https://www.instagram.com/"+sname);
            //start progress here ofter to the following
            new CountDownTimer(6000, 600) {

                public void onTick(long millisUntilFinished) {
                }
                public void onFinish() {
                    //Stop your progress here
                   hidePDialog();
                }
            }.start();
            }
            else {
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
     //   hidePDialog();
    }

    private class MyBrowser extends WebViewClient  //If you click on any link inside the webpage of the WebView , that page will not be loaded inside your WebView. In order to do that you need to extend your class from WebViewClient by using the below function.
    {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
    @Override
    public void onBackPressed() {
        // code here to show dialog
        // super.onBackPressed();  // optional depending on your needs
        Intent intent  = new Intent(this, MyProfileDummy.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
                Intent intent  = new Intent(this, MyProfileDummy.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        hidePDialog();
    }
    private void hidePDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
            pDialog = null;
        }
    }
}