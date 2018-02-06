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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookActivity;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.login.widget.ProfilePictureView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SocialMediaAuthentication extends AppCompatActivity {
    String cid,pageid,pagename,pageabout,pagefancount,pagetoken;
    TextView tvuserid, tvusername, tvuseremail, tvusergender, tvuserbirthday,tvuserabout;
    private LoginButton loginButton;
    private Button fbloginButton,fblogoutButton;
    ImageView fb_profileimage;
    ListView list;
    private CallbackManager callbackManager;
    // Hashmap for ListView
    ArrayList<HashMap<String, String>> pageList;
    private static final String TAG_PID = "id";
    private static final String TAG_PNAME = "name";
    private static final String TAG_PABOUT = "about";
    private static final String TAG_PFANCOUNT = "fan_count";
    private static final String TAG_PTOKEN = "access_token";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(SocialMediaAuthentication.this);//Is now depricated
        
        setContentView(R.layout.socialmedia);
        callbackManager = CallbackManager.Factory.create();
        pageList = new ArrayList<HashMap<String, String>>();
        ActionBar bar = getSupportActionBar();
        bar.setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Social Media Authentications ");

        //getting customer cid value in shared preference
        SharedPreferences prfs = getSharedPreferences("CID_VALUE", Context.MODE_PRIVATE);
        cid = prfs.getString("valueofcid", "");
        loginButton = (LoginButton) findViewById(R.id.login_button);
        //fblogoutButton = (Button) findViewById(R.id.logout_button);
        tvuserid = (TextView) findViewById(R.id.tvuserid);
        tvusername = (TextView) findViewById(R.id.tvusername);
        tvuseremail = (TextView) findViewById(R.id.tvuseremail);
        tvusergender = (TextView) findViewById(R.id.tvusergender);
        tvuserbirthday = (TextView) findViewById(R.id.tvuserbirthday);
        tvuserabout = (TextView) findViewById(R.id.tvuserabout);
        fb_profileimage = (ImageView) findViewById(R.id.fb_profileimage);

        callbackManager = CallbackManager.Factory.create();
        loginButton.setReadPermissions(Arrays.asList("public_profile", "email", "user_birthday", "user_location"));
//        loginButton.setReadPermissions("accounts");

        list = (ListView) findViewById(R.id.pagevalues);
        // Listview on item click listener
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // getting values from selected ListItem
                String pgname = ((TextView) view.findViewById(R.id.pgname)).getText().toString();
                String pgid = ((TextView) view.findViewById(R.id.pgid)).getText().toString();
                String pgabout = ((TextView) view.findViewById(R.id.pgabout)).getText().toString();
                String pgfancount = ((TextView) view.findViewById(R.id.pgfancount)).getText().toString();
                String pgtoken = ((TextView) view.findViewById(R.id.pgtoken)).getText().toString();

            }
        });

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @SuppressLint("SetTextI18n")
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                Log.v("SocialMedia ", response.toString());
                                AccessToken accessToken = AccessToken.getCurrentAccessToken();
                                System.out.println("Access Token :"+accessToken);
                                try {
                                    //fbloginButton.setVisibility(View.INVISIBLE);
                                    //fblogoutButton.setVisibility(View.VISIBLE);
                                    Log.d("Facebook", "Json Object: " + object);
                                    Log.d("Facebook", "Graph Response: " + response);
                                    String id = object.getString("id");
                                    String name = object.getString("name");
                                    String gender = object.getString("gender");
                                    String birthday = object.getString("birthday");
                                    String image_url = "http://graph.facebook.com/" + id + "/picture?type=large";
                                    String email = null;
                                    if (object.has("email")) {
                                        email = object.getString("email");
                                    }
                                    String about = object.getString("about");

                                    if (object.has("accounts")) {
                                        JSONObject accounts = object.getJSONObject("accounts");
                                        JSONArray data = accounts.getJSONArray("data");
                                        for (int i=0;i<data.length();i++){
//                                            Log.i("value of Id : ",data.getJSONObject(i).getString("id"));
//                                            Log.i("value of Name : ",data.getJSONObject(i).getString("name"));
//                                            Log.i("value of About : ",data.getJSONObject(i).getString("about"));
//                                            Log.i("value of Fan Count : ",data.getJSONObject(i).getString("fan_count"));
//                                            Log.i("value of Token : ",data.getJSONObject(i).getString("access_token"));

                                            pageid = data.getJSONObject(i).getString("id");
                                            pagename = data.getJSONObject(i).getString("name");
                                            pageabout = data.getJSONObject(i).getString("about");
                                            pagefancount = data.getJSONObject(i).getString("fan_count");
                                            pagetoken = data.getJSONObject(i).getString("access_token");

                                            // tmp hashmap for single contact
                                            HashMap<String, String> page = new HashMap<String, String>();

                                            // adding each child node to HashMap key => value
                                            page.put(TAG_PID,"ID : "+pageid);
                                            page.put(TAG_PNAME,pagename);
                                            page.put(TAG_PABOUT,"ABOUT : "+pageabout);
                                            page.put(TAG_PFANCOUNT,"LIKES : "+pagefancount);
                                            page.put(TAG_PTOKEN,"ACCESS TOKEN :"+pagetoken);

                                            // adding contact to contact list
                                            pageList.add(page);
                                        }
                                    }

                                    tvuserid.setText("User ID : "+id);
                                    tvusername.setText("NAME : "+name);
                                    tvuseremail.setText( "EMAIL : "+email);
                                    tvusergender.setText("GENDER : "+gender);
                                    tvuserbirthday.setText("BIRTHDAY : "+birthday);
                                    tvuserabout.setText("ABOUT : "+about);
                                    //Image
                                    Glide.with(getApplicationContext()).load(image_url)
                                            .thumbnail(0.5f)
                                            .crossFade()
                                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                                            .into(fb_profileimage);

                                    ListAdapter adapter = new SimpleAdapter(SocialMediaAuthentication.this, pageList,
                                            R.layout.fbpagelist, new String[]{TAG_PID, TAG_PNAME,TAG_PABOUT,TAG_PFANCOUNT,TAG_PTOKEN},
                                            new int[]{R.id.pgid,R.id.pgname,R.id.pgabout,R.id.pgfancount,R.id.pgtoken});
                                    list.setAdapter(adapter);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                Bundle parameters = new Bundle();
                //parameters.putString("fields", "id,name,email,gender,birthday,location,accounts");
                parameters.putString("fields", "id,name,email,gender,birthday,accounts{app_id,name,about,fan_count,access_token},about,picture,location");
                request.setParameters(parameters);
                request.executeAsync();

            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });

//        fblogoutButton.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                tvuserid.setText("");
//                tvusername.setText("");
//                tvuseremail.setText("");
//                tvusergender.setText("");
//                tvuserbirthday.setText("");
//                tvuserabout.setText("");
//                fb_profileimage.setVisibility(View.INVISIBLE);
//                LoginManager.getInstance().logOut();
//                fblogoutButton.setVisibility(View.INVISIBLE);
//           //     fbloginButton.setVisibility(View.VISIBLE);
//            }
//        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

}