package com.socialbeat.influencer;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MyProfileDummy extends AppCompatActivity {

    private static final String PREFS_NAME ="MyProfileDummy" ;
    Context context;
    private CoordinatorLayout coordinatorLayout;
    TextView pfacebookhandle,pbloggerlink, ptwitterhandle,pinstagramhandle,pyoutubehandle;
    Boolean isInternetPresent = false;
    ConnectionDetector cd;
    String cid,success,message,profileprogress,name,twitter,twitterFollowers,bloglink,blogTraffic,
            instagram,instaFollowers,response,facebookpage,facebookFollowers,youtubehandle,youtubefollowers;
    public static final String KEY_CID = "cid";
    private ProgressDialog pdialog;
    private String TAG;
    LinearLayout twitter_clr,instagram_clr,blogger_clr,facebook_clr,youtube_clr;
    Button addhandle;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_dummy);

        ActionBar bar = getSupportActionBar();
        bar.setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Social Media");

        //getting customer cid value in shared preference
        SharedPreferences prfs = getSharedPreferences("CID_VALUE", Context.MODE_PRIVATE);
        cid = prfs.getString("valueofcid", "");

        coordinatorLayout = (CoordinatorLayout)findViewById(R.id.coordinatorLayout);

        pfacebookhandle = (TextView)findViewById(R.id.facebook_handle);
        pinstagramhandle = (TextView)findViewById(R.id.instagram_handle);
        ptwitterhandle = (TextView)findViewById(R.id.twitter_handle);
        pbloggerlink = (TextView)findViewById(R.id.blogger_handle);
        pyoutubehandle = (TextView)findViewById(R.id.youtube_handle);

        facebook_clr = (LinearLayout) findViewById(R.id.scmenu);
        instagram_clr = (LinearLayout) findViewById(R.id.scmenu2);
        twitter_clr = (LinearLayout) findViewById(R.id.scmenu1);
        blogger_clr = (LinearLayout) findViewById(R.id.scmenu3);
        youtube_clr = (LinearLayout) findViewById(R.id.scmenu4);

        addhandle = (Button) findViewById(R.id.add_button);


        cd = new ConnectionDetector(this);
        isInternetPresent = cd.isConnectingToInternet();

        if(cid.length()!=0){
            if (isInternetPresent) {
                //MyApplication.getInstance().trackEvent("MyProfileDummy Screen", "OnClick", "Track MyProfileDummy Event");
                Log.v("Condition :","Before Profile function");
                profileFunction();
                Log.v("Condition :","Completed Profile function");
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

        addhandle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intents = new Intent(MyProfileDummy.this, SocialMedia.class);
                startActivity(intents);
            }
        });

        facebook_clr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               if (facebookpage.length()!=0){
                   Intent intent = new Intent(MyProfileDummy.this, FacebookPage.class);
                   Bundle bund = new Bundle();
                   bund.putString("sname",facebookpage);
                   intent.putExtras(bund);
                   startActivity(intent);
               }else{
                   pfacebookhandle.setText("Add You Facebook Page");
                   //Toast.makeText(getApplicationContext(), "Your Facebook Page is incorrect,can you Add correct page in the Add Account Button.", Toast.LENGTH_SHORT).show();
                   addaccount();
               }

            }
        });

        instagram_clr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (instagram.length()!=0){
                    Intent intent = new Intent(MyProfileDummy.this, InstagramPage.class);
                    Bundle bund = new Bundle();
                    bund.putString("sname",instagram);
                    intent.putExtras(bund);
                    startActivity(intent);
                }else{
                    pinstagramhandle.setText("Add You Instagram Handle");
                    addaccount();
                    //Toast.makeText(getApplicationContext(), "Your Instagram Handle is incorrect,can you Add correct Handle in the Add Account Button.", Toast.LENGTH_SHORT).show();
                }

            }
        });

        twitter_clr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (twitter.length()!=0){
                    Intent intent = new Intent(MyProfileDummy.this, TwitterPage.class);
                    Bundle bund = new Bundle();
                    bund.putString("sname",twitter);
                    intent.putExtras(bund);
                    startActivity(intent);
                }else{
                    ptwitterhandle.setText("Add You Twitter Handle");
                    Toast.makeText(getApplicationContext(), "Your Twitter Handle is incorrect,can you Add correct Handle in the Add Account Button.", Toast.LENGTH_SHORT).show();
                }

            }
        });

        youtube_clr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (youtubehandle.length()!=0){
                    Intent intent = new Intent(MyProfileDummy.this, YoutubePage.class);
                    Bundle bund = new Bundle();
                    bund.putString("sname",youtubehandle);
                    intent.putExtras(bund);
                    startActivity(intent);
                }else{
                    pyoutubehandle.setText("Add You Youtube Channel");
                    Toast.makeText(getApplicationContext(), "Your Youtube Page is incorrect,can you Add correct page in the Add Account Button.", Toast.LENGTH_SHORT).show();
                }

            }
        });

        blogger_clr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bloglink.length()!=0){
                    Intent intent = new Intent(MyProfileDummy.this, BloggerPage.class);
                    Bundle bund = new Bundle();
                    bund.putString("sname",bloglink);
                    intent.putExtras(bund);
                    startActivity(intent);
                }else{
                    pbloggerlink.setText("Add You BloggerLink");
                    Toast.makeText(getApplicationContext(), "Your Blogger Link is incorrect,can you Add correct Link in the Add Account Button.", Toast.LENGTH_SHORT).show();
                }


            }
        });
    }

    public void addaccount(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Your Account is incorrect,can you Add correct Account?");
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton("ADD",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        Intent intents = new Intent(MyProfileDummy.this, SocialMedia.class);
                        startActivity(intents);
                    }
                });

        alertDialogBuilder.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void profileFunction() {
        Log.v("Condition :","Enter Profile function");
        pdialog = new ProgressDialog(MyProfileDummy.this);
        pdialog.setMessage("Loading...");
        pdialog.setCancelable(false);
        pdialog.show();
        String PROFILE_URL = getResources().getString(R.string.base_url)+getResources().getString(R.string.profile_url);
        Log.v("PROFILE_URL : ",PROFILE_URL);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, PROFILE_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String success) {
                try {
                    JSONObject json = new JSONObject(success);
                    response = json.getString("success");
                    message = json.getString("message");
                    profileprogress = json.getString("profileProgress");


                    facebookpage = json.getString("facebookpage");
                    facebookFollowers = json.getString("facebookFollowers");
                    instagram = json.getString("instagram");
                    instaFollowers = json.getString("instaFollowers");
                    twitter = json.getString("twitter");
                    twitterFollowers = json.getString("twitterFollowers");
                    bloglink = json.getString("bloglink");
                    blogTraffic = json.getString("blogTraffic");
                    youtubehandle = json.getString("youtubehandle");
                    youtubefollowers = json.getString("youtubefollowers");
                    //
//                    System.out.println("Response : "+response+" "+message+"Twitter: "+twitter+"T.Follow : "+twitterFollowers+"Blog: "+bloglink +"Blog Traffic: "+blogTraffic+"Instagram : "+instagram +"I.Follow "+instaFollowers);
                    //facebook
                    String link ="<a href="+facebookpage+">"+ facebookpage +" </a>" ;
                    Spanned sp = Html.fromHtml( link );
                    pfacebookhandle.setText(sp);
                    pfacebookhandle.setMovementMethod(LinkMovementMethod.getInstance());
                    //instagram
                    //pinstagramhandle.setText(instagram);
                    String link0 ="<a href="+"https://www.instagram.com/"+instagram+">"+ instagram +" </a>" ;
                    Spanned sp0 = Html.fromHtml( link0 );
                    pinstagramhandle.setText(sp0);
                    pinstagramhandle.setMovementMethod(LinkMovementMethod.getInstance());
                    //twitter
                    ptwitterhandle.setText(twitter);
                    String link01 ="<a href="+"https://www.twitter.com/"+twitter+">"+ twitter +" </a>" ;
                    Spanned sp01 = Html.fromHtml( link01 );
                    ptwitterhandle.setText(sp01);
                    ptwitterhandle.setMovementMethod(LinkMovementMethod.getInstance());
                    //blogger
                    String link1 ="<a href="+bloglink+">"+ bloglink +" </a>" ;
                    Spanned sp1 = Html.fromHtml( link1 );
                    pbloggerlink.setText(sp1);
                    pbloggerlink.setMovementMethod(LinkMovementMethod.getInstance());
                    //youtube
                    String link2 ="<a href="+youtubehandle+">"+ youtubehandle +" </a>" ;
                    Spanned sp2 = Html.fromHtml( link2 );
                    pyoutubehandle.setText(sp2);
                    pyoutubehandle.setMovementMethod(LinkMovementMethod.getInstance());

                    Log.v("Facebook Handle : ",facebookpage);
                    Log.v("Instagram Handle : ",instagram);
                    Log.v("Twitter Handle : ",twitter);
                    Log.v("Blog Handle : ",bloglink);
                    Log.v("Youtube Handle : ",youtubehandle);

//
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                pdialog.dismiss();
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MyProfileDummy.this, error.toString(), Toast.LENGTH_LONG).show();
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
////                Intent intentgo = new Intent(SocialMedia.this, SocialMediaAuth.class);
////                startActivity(intentgo);
//////                Toast.makeText(MainActivity.this, "Notification Clicked", Toast.LENGTH_LONG).show();
////                return true;
            case android.R.id.home:
                Intent intent = new Intent(MyProfileDummy.this, UserSettings.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
