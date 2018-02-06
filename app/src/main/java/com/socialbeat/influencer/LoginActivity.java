package com.socialbeat.influencer;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.Html;
import android.text.InputType;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {

    Button login,send;
    TextView register, forgotpwd,terms;
    EditText emails, pwords,femailid;
    boolean flg = true;
    String email, password,femail;
    Boolean isInternetPresent = false;
    ImageView pass_visible,pass_invisible;
    ConnectionDetector cd;
    private ProgressDialog pdialog;
    final Context context = this;
    private CoordinatorLayout coordinatorLayout;

    public static final String LOGIN_NAME = "LoginFile";
    SharedPreferences.Editor editor;

    public static final String KEY_EMAIL = "email";
    public static final String KEY_PASSWORD = "password";
    private String message, cid, username, emailid, mobileno, city, blog, facebookpage, blogtraffic, rank, twitterhandle,
            twitter_followers, instagram, instagram_followers, overallreach, foi, userimage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_screen_new);
        login = (Button)findViewById(R.id.login_button);
        register = (TextView)findViewById(R.id.register);
        terms = (TextView)findViewById(R.id.terms);
        forgotpwd = (TextView)findViewById(R.id.forgotpassword);
        emails = (EditText)findViewById(R.id.emailid);
        pwords = (EditText)findViewById(R.id.password);
        pass_visible = (ImageView) findViewById(R.id.pass_visible);
        pass_invisible = (ImageView) findViewById(R.id.pass_invisible);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);

        Typeface myFont = Typeface.createFromAsset(getAssets(), "font/headfont.ttf");
        emails.setTypeface(myFont);
        Typeface myFont1 = Typeface.createFromAsset(getAssets(), "font/headfont.ttf");
        pwords.setTypeface(myFont1);
        Spanned sp = Html.fromHtml("By Signing with us, you agree to the Influencer " +
                "<a href=\"http://www.influencer.in/terms-and-conditions/\"> Terms &amp; Conditions.</a>" );
        terms.setText(sp);
        terms.setMovementMethod(LinkMovementMethod.getInstance());
        Typeface myFont6 = Typeface.createFromAsset(getAssets(), "font/gothic.ttf");
        terms.setTypeface(myFont6);
        register.setPaintFlags(register.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        forgotpwd.setPaintFlags(forgotpwd.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        cd = new ConnectionDetector(getApplicationContext());
        isInternetPresent = cd.isConnectingToInternet();


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in =new Intent(LoginActivity.this,RegistrationActivityOne.class);
                startActivity(in);
            }
        });
        pwords.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                pass_visible.setVisibility(s.length() > 0 ? View.VISIBLE : View.GONE);
            }
        });


        pass_visible.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pwords.setTransformationMethod(new PasswordTransformationMethod());
                pass_visible.setVisibility(View.INVISIBLE);
                pass_invisible.setVisibility(View.VISIBLE);
            }
        });

        pass_invisible.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pwords.setTransformationMethod(null);
                pass_invisible.setVisibility(View.INVISIBLE);
                pass_visible.setVisibility(View.VISIBLE);
            }
        });

        forgotpwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                        // custom dialog
                        final Dialog dialog = new Dialog(context);
                        dialog.setContentView(R.layout.forgotpassword_new);
                        // Custom Android Allert Dialog Title
                        //dialog.setCancelable(false);
                         send = (Button) dialog.findViewById(R.id.sendbutton);
                         femailid = (EditText) dialog.findViewById(R.id.femailid);
                         Typeface myFont2 = Typeface.createFromAsset(getAssets(), "font/headfont.ttf");
                        femailid.setTypeface(myFont2);
                        femailid.setTextColor(getResources().getColor(R.color.white));
                        isInternetPresent = cd.isConnectingToInternet();
                        SharedPreferences prefernce = getSharedPreferences(LOGIN_NAME, MODE_PRIVATE);
                        editor = prefernce.edit();
                        editor.clear();
                        editor.commit();
                        send.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                flg = true;
                                if (isInternetPresent) {
                                    femail = femailid.getText().toString();
                                    if (femail.length() > 0) {
                                        if (!isValidEmailid1(femail)) {
                                            flg = false;
                                            femailid.setError("Please Enter Valid Email Id");
                                        }
                                            forgetPassword();
                                            dialog.dismiss();
                                    } else {
                                        femailid.setError("Please Enter Valid Email id");
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
                        });

            dialog.show();
            }
        }
    );

        //onclick method for login button
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flg = true;
                //getting value from database
                email = emails.getText().toString();
                password = pwords.getText().toString();

                if ((TextUtils.isEmpty(email))) {
                    flg = false;
                    emails.setError("Email id field is empty");
                    return;
                } else if (!isValidEmailid1(email)) {
                    flg = false;
                    emails.setError("Enter Valid Email id");
                    return;
                }
                if ((TextUtils.isEmpty(password))) {
                    flg = false;
                    pwords.setError("Password field is empty");
                    return;
                } else if (!isValidPassword(password)) {
                    flg = false;
                    pwords.setError("Minimum required value is 6");
                }

                if (flg) {
                    MyApplication.getInstance().trackEvent("Login", "OnClick", "Track Login Event");
                    loginFunction();
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

    private void forgetPassword() {
        pdialog = new ProgressDialog(LoginActivity.this);
        pdialog.setMessage("Loading...");
        pdialog.setCancelable(false);
        pdialog.show();
        String LOGIN_URL = getResources().getString(R.string.base_url)+getResources().getString(R.string.forgotpassword_url);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, LOGIN_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String success) {
                try {
                    org.json.JSONObject json = new org.json.JSONObject(success);
                    success = json.getString("success").toString();
                    message = json.getString("message").toString();

                    pdialog.dismiss();

                    if (success =="true"){
                        Toast.makeText(LoginActivity.this,message, Toast.LENGTH_SHORT).show();
                    }else if (success =="false") {
                        Toast.makeText(LoginActivity.this,message, Toast.LENGTH_SHORT).show();
                    }
                } catch (org.json.JSONException e) {
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(LoginActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put(KEY_EMAIL, femail);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void loginFunction() {
        pdialog = new ProgressDialog(LoginActivity.this);
        pdialog.setMessage("Loading...");
        pdialog.setCancelable(false);
        pdialog.show();
        String LOGIN_URL = getResources().getString(R.string.base_url)+getResources().getString(R.string.login_url);
        Log.v("URL",LOGIN_URL);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, LOGIN_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String success) {
                try {
                    org.json.JSONObject json = new org.json.JSONObject(success);
                    success = json.getString("success");
                    message = json.getString("message");
                    cid = json.getString("cid");
                    emailid = json.getString("email");
                    username = json.getString("name");
                    userimage = json.getString("userimage");
                    mobileno = json.getString("mobileno");
                    city = json.getString("city");
//                    blog = json.getString("blog");
//                    blogtraffic = json.getString("blogtraffic");
//                    rank = json.getString("rank");
//                    twitterhandle = json.getString("twitterhandle");
//                    twitter_followers = json.getString("twitter_followers");
//                    facebookpage = json.getString("facebookpage");
//                    instagram = json.getString("instagram");
//                    instagram_followers = json.getString("instagram_followers");
//                    overallreach = json.getString("overallreach");
//                    foi = json.getString("foi");
                    pdialog.dismiss();

                    SharedPreferences preferences = getSharedPreferences("CID_VALUE", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("valueofcid",cid);
                    editor.putString("username1",username);
                    editor.putString("emailid1",emailid);
                    editor.putString("mobileno1",mobileno);
                    editor.putString("city1",city);
//                    editor.putString("blog1",blog);
//                    editor.putString("blogtraffic1",blogtraffic);
//                    editor.putString("rank1",rank);
//                    editor.putString("facebookpage1",facebookpage);
//                    editor.putString("twitterhandle1",twitterhandle);
//                    editor.putString("twitter_followers1",twitter_followers);
//                    editor.putString("instagram1",instagram);
//                    editor.putString("instagram_followers1",instagram_followers);
//                    editor.putString("overallreach1",overallreach);
//                    editor.putString("foi1",foi);
                    editor.putString("userimage1",userimage);
                    editor.apply();

                    if (success =="true"){
                        Toast.makeText(LoginActivity.this,message, Toast.LENGTH_SHORT).show();
                        Intent in = new Intent(LoginActivity.this, NewHomeActivity.class);
                        startActivity(in);

                    }else if (success =="false") {
                        Toast.makeText(LoginActivity.this,message, Toast.LENGTH_SHORT).show();
                    }
                } catch (org.json.JSONException e) {
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(LoginActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put(KEY_EMAIL, email);
                params.put(KEY_PASSWORD, password);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
    // validating password
    private boolean isValidPassword(String pass) {
        if (pass != null && pass.length() >= 6) {
            return true;
        }
        return false;
    }
    // validating email id
    private boolean isValidEmailid1(String emailid1) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(emailid1);
        return matcher.matches();
    }

}