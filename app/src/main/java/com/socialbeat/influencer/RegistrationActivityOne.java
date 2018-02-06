package com.socialbeat.influencer;

import android.*;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
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
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegistrationActivityOne extends AppCompatActivity {

    private static final String TAG = RegistrationActivityOne.class.getSimpleName();
    Button register;
    private static final int REQUEST_TAKE_PHOTO = 1;
    int currentapiVersion;
    TextView login, terms;
    ImageView profileimage,pass_visible,pass_invisible;
    EditText name, password, emailid, mobileno, city;
    String rname, remail, rpassword, rmobile, rcity, cid, message, userimage,mCurrentPhotoPath,
            cname,email,cmobileno,ccity,blog,blogtraffic,rank,twitterhandle, twitter_followers,
            facebookpage,instagram,instagram_followers,overallreach,foi;
    Boolean isInternetPresent = false;
    ConnectionDetector cd;
    int a=0;
    private CoordinatorLayout coordinatorLayout;
    private ProgressDialog pdialog;
    public static final String LOGIN_NAME = "LoginFile";
    public static final String KEY_APPNAME = "Influencer";
    SharedPreferences.Editor editor;
    boolean flg = true;
    public static final String KEY_USERIMAGE = "userimage";
    public static final String KEY_NAME = "cname";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_MOBILENO = "mobileno";
    public static final String KEY_CITY = "city";

    // Camera activity request codes
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    public static final int MEDIA_TYPE_IMAGE = 1;
    private Uri fileUri; // file url to store image/video
    private String filePath = null;
    long totalSize = 0;
    // number of images to select
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private String userChoosenTask,imagefile;
    File destination;
    Bitmap bm=null;
    File fileDesPath = null;
    FileOutputStream fo;
    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration_screen1_new);

        register = (Button) findViewById(R.id.register_button);
        login = (TextView) findViewById(R.id.login);
        terms = (TextView) findViewById(R.id.terms);
        profileimage = (ImageView) findViewById(R.id.profileimage);
        pass_visible = (ImageView) findViewById(R.id.pass_visible);
        pass_invisible = (ImageView) findViewById(R.id.pass_invisible);
        name = (EditText) findViewById(R.id.name);
        emailid = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        mobileno = (EditText) findViewById(R.id.mobile);
        city = (EditText) findViewById(R.id.city);
        currentapiVersion = android.os.Build.VERSION.SDK_INT;
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        Typeface myFont = Typeface.createFromAsset(getAssets(), "font/headfont.ttf");
        name.setTypeface(myFont);
        Typeface myFont1 = Typeface.createFromAsset(getAssets(), "font/headfont.ttf");
        emailid.setTypeface(myFont1);
        Typeface myFont2 = Typeface.createFromAsset(getAssets(), "font/headfont.ttf");
        password.setTypeface(myFont2);
        Typeface myFont4 = Typeface.createFromAsset(getAssets(), "font/headfont.ttf");
        mobileno.setTypeface(myFont4);
        Typeface myFont5 = Typeface.createFromAsset(getAssets(), "font/headfont.ttf");
        city.setTypeface(myFont5);

        Spanned sp = Html.fromHtml("By Signing up with us, you agree to the Influencer " +
                "<a href=\"http://www.influencer.in/terms-and-conditions/\"> Terms &amp; Conditions.</a>");
        terms.setText(sp);
        terms.setMovementMethod(LinkMovementMethod.getInstance());
        Typeface myFont6 = Typeface.createFromAsset(getAssets(), "font/gothic.ttf");
        terms.setTypeface(myFont6);
        login.setPaintFlags(login.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        cd = new ConnectionDetector(getApplicationContext());
        isInternetPresent = cd.isConnectingToInternet();
        SharedPreferences prefernce = getSharedPreferences(LOGIN_NAME, MODE_PRIVATE);
        editor = prefernce.edit();
        editor.clear();
        editor.commit();

        password.addTextChangedListener(new TextWatcher() {
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
                password.setTransformationMethod(new PasswordTransformationMethod());
                pass_visible.setVisibility(View.INVISIBLE);
                pass_invisible.setVisibility(View.VISIBLE);

            }
        });

        pass_invisible.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                password.setTransformationMethod(null);
                pass_invisible.setVisibility(View.INVISIBLE);
                pass_visible.setVisibility(View.VISIBLE);
            }
        });
/**
 * Capture image button click event
 */
        profileimage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // capture picture
                boolean result=checkPermission(RegistrationActivityOne.this);
                userChoosenTask ="Take Photo";
                if(result) {
                    selectImage();
                }else{

                }
            }
        });
        // Checking camera availability
        if (!isDeviceSupportCamera()) {
            Toast.makeText(getApplicationContext(), "Sorry! Your device doesn't support camera", Toast.LENGTH_LONG).show();
            // will close the app if the device does't have camera
            finish();
        }

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(RegistrationActivityOne.this, LoginActivity.class);
                startActivity(in);
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isInternetPresent) {
                    flg = true;
                    final String username1 = name.getText().toString();
                    if ((TextUtils.isEmpty(username1))) {
                        flg = false;
                        name.setError("Name field is empty");
                        return;
                    }
                    final String emailid1 = emailid.getText().toString();
                    if ((TextUtils.isEmpty(emailid1))) {
                        flg = false;
                        emailid.setError("Email id field is empty");
                        return;
                    } else if (!isValidEmailid1(emailid1)) {
                        flg = false;
                        emailid.setError("Enter Valid Email id");
                        return;
                    }
                    final String pass = password.getText().toString();
                    if ((TextUtils.isEmpty(pass))) {
                        flg = false;
                        password.setError("Password field is empty");
                        return;
                    } else if (!isValidPassword(pass)) {
                        flg = false;
                        password.setError("Minimum required value is 6");
                        return;
                    }
                    final String mobileno1 = mobileno.getText().toString();
                    if ((TextUtils.isEmpty(mobileno1))) {
                        flg = false;
                        mobileno.setError("Enter Valid Mobile Number");
                        return;
                    } else if (!isValidMobileno1(mobileno1)) {
                        flg = false;
                        mobileno.setError("Enter your 10 Digit Mobile Number");
                        return;
                    }

                    final String city1 = city.getText().toString();
                    if ((TextUtils.isEmpty(city1))) {
                        flg = false;
                        city.setError("City Name Field is Empty");
                        return;
                    }
//                    if (filePath == null){
//                        flg = false;
//                        Toast.makeText(getApplicationContext(), "Please Select your profile Image", Toast.LENGTH_LONG).show();
//                        return;
//                    }
                    if (flg) {
                        rname = name.getText().toString();
                        rpassword = password.getText().toString();
                        remail = emailid.getText().toString();
                        rmobile = mobileno.getText().toString();
                        rcity = city.getText().toString();
                        MyApplication.getInstance().trackEvent("Registration Screen", "OnClick", "Track Registration Event");

                        if (a==1){
                            Log.v("Image","YES");
                            new UploadFileToServerImage().execute();
                        }else {
                            Log.v("Image","NO");
                            new UploadFileToServer().execute();
                        }
                      //  new UploadFileToServer().execute();
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
    }
    private void selectImage() {
        final CharSequence[] items = { "Take Photo", "Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(RegistrationActivityOne.this);
        builder.setTitle("Select Profile Image");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result=checkPermission(RegistrationActivityOne.this);
                if (items[item].equals("Take Photo")) {
                    userChoosenTask ="Take Photo";
                    currentapiVersion = android.os.Build.VERSION.SDK_INT;
                    if(result)
                        System.out.println("Current OS Version :"+currentapiVersion );
                        if (currentapiVersion >= Build.VERSION_CODES.N) {
                            // Do something for lollipop and above versions
                            System.out.println("Current OS Version1 :"+currentapiVersion );
                            Log.v("OS Version","N+");
                            startCamera();
                        } else {
                            Log.v("OS Version","N-");
                            captureImage();
                        }
                }  else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void startCamera() {
        try {
            dispatchTakePictureIntent();
        } catch (IOException e) {
        }
    }

    private void dispatchTakePictureIntent() throws IOException {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                return;
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                //Uri photoURI = Uri.fromFile(createImageFile());
                Uri photoURI = FileProvider.getUriForFile(RegistrationActivityOne.this, BuildConfig.APPLICATION_ID + ".provider", createImageFile());
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }
    /**
     * Checking device has camera hardware or not
     * */
    private boolean isDeviceSupportCamera() {
        if (getApplicationContext().getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA)) {
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM), "Camera");
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }

    //capture img from camera
    private void captureImage() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        // start the image capture Intent
        startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
        System.out.println("Camera File URI"+fileUri);
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // save file url in bundle as it will be null on screen orientation changes
        outState.putParcelable("file_uri", fileUri);
    }
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        // get the file url
        fileUri = savedInstanceState.getParcelable("file_uri");
    }
    /**
     * Receiving activity result method will be called after closing the camera
     * */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // if the result is capturing Image
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            // Show the thumbnail on ImageView
            Uri imageUri = Uri.parse(mCurrentPhotoPath);

            filePath = imageUri.getPath();
            System.out.println("filePath value zero: " + filePath);
            if (filePath != null) {
                boolean isImage = true;
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 8;
                Bitmap pictureBitmap = null;
                try {
                    String path = Environment.getExternalStorageDirectory().toString();
                    System.out.println("filePath value one : " + path);
                    OutputStream fOut = null;
                    path += "/Influencer/";
                    fileDesPath = new File(path);
                    System.out.println("File path two : " + fileDesPath);
                    if (!fileDesPath.isDirectory()) {
                        fileDesPath.mkdir();
                    }
                    File file = new File(path, "profileImage" + ".jpg"); // the File to save , append increasing numeric counter to prevent files from getting overwritten.
                    System.out.println("File path three : " + file);
                    fOut = new FileOutputStream(file);
                    System.out.println("File path four : " + fOut);
                    pictureBitmap = BitmapFactory.decodeFile(filePath, options);
                    pictureBitmap.compress(Bitmap.CompressFormat.JPEG, 85, fOut); // saving the Bitmap to a file compressed as a JPEG with 85% compression rate
                    fOut.flush(); // Not really required
                    fOut.close(); // do not forget to close the stream
                    filePath = file.getAbsolutePath();
                    System.out.println("File path five : " + filePath);
                    a = 1;
                } catch (Exception ex) {
                    Log.v("Exception in file get", ex.toString());
                }
                profileimage.setImageBitmap(pictureBitmap);
                // ScanFile so it will be appeared on Gallery
                MediaScannerConnection.scanFile(RegistrationActivityOne.this, new String[]{imageUri.getPath()}, null, new MediaScannerConnection.OnScanCompletedListener() {
                    public void onScanCompleted(String path, Uri uri) {
                    }
                });
            }
        }else if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                filePath = fileUri.getPath();
                if (filePath != null) {
                    boolean isImage = true;
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inSampleSize = 8;
                    Bitmap pictureBitmap = null;
                    try {
                        String path = Environment.getExternalStorageDirectory().toString();
                        OutputStream fOut = null;
                        path += "/Influencer/";
                        fileDesPath = new File(path);
                        if (!fileDesPath.isDirectory()) {
                            fileDesPath.mkdir();
                        }
                        File file = new File(path, "profileImage" + ".jpg"); // the File to save , append increasing numeric counter to prevent files from getting overwritten.
                        fOut = new FileOutputStream(file);
                        pictureBitmap = BitmapFactory.decodeFile(filePath, options);
                        pictureBitmap.compress(Bitmap.CompressFormat.JPEG, 85, fOut); // saving the Bitmap to a file compressed as a JPEG with 85% compression rate
                        fOut.flush(); // Not really required
                        fOut.close(); // do not forget to close the stream
                        filePath = file.getAbsolutePath();
                        a = 1;
                    } catch (Exception ex) {
                        Log.v("Exception in file get", ex.toString());
                    }
                    profileimage.setImageBitmap(pictureBitmap);
                } else {
                    Toast.makeText(getApplicationContext(), "Sorry, File path is missing!", Toast.LENGTH_LONG).show();
                }
            } else if (resultCode == RESULT_CANCELED) {
                // user cancelled Image capture
                Toast.makeText(getApplicationContext(), "User cancelled image capture", Toast.LENGTH_SHORT).show();

            } else {
                // failed to capture image
                Toast.makeText(getApplicationContext(), "Sorry! Failed to capture image", Toast.LENGTH_SHORT).show();
            }
        }
//
    }

    private void onSelectFromGalleryResult(Intent data) {

        Bitmap bm=null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        profileimage.setImageBitmap(bm);
    }

    /**
     * Creating file uri to store image/video
     */
    public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }
    /**
     * returning image / video
     */
    private static File getOutputMediaFile(int type) {
        // External sdcard location
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), KEY_APPNAME);
        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(TAG, "Oops! Failed create " + KEY_APPNAME + " directory");
                return null;
            }
        }
        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + ".jpg");
        }else {
            return null;
        }
        return mediaFile;
    }



    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static boolean checkPermission(final Context context)
    {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if(currentAPIVersion>=android.os.Build.VERSION_CODES.M)
        {
            if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                    alertBuilder.setCancelable(true);
                    alertBuilder.setTitle("Permission necessary");
                    alertBuilder.setMessage("External storage permission is necessary");
                    alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions((Activity) context, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                        }
                    });
                    AlertDialog alert = alertBuilder.create();
                    alert.show();
                } else {
                    ActivityCompat.requestPermissions((Activity) context, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                }
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    /**
     * Uploading the file to server
     * */
     class UploadFileToServer extends AsyncTask<Void, Integer, String> {
        @Override
        protected void onPreExecute() {
            // setting progress bar to zero
            super.onPreExecute();
            pdialog = new ProgressDialog(RegistrationActivityOne.this);
            pdialog.setMessage("Loading...");
            pdialog.setCancelable(false);
            pdialog.show();
        }

        @Override
        protected String doInBackground(Void... params) {
            return uploadFile();
        }

        @SuppressWarnings("deprecation")
        private String uploadFile() {
            String responseString = null;
            String REGISTER_URL = getResources().getString(R.string.base_url) + getResources().getString(R.string.regsitration_one_url);
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(REGISTER_URL);
            try {
                AndroidMultiPartEntity entity = new AndroidMultiPartEntity(new AndroidMultiPartEntity.ProgressListener() {
                    @Override
                    public void transferred(long num) {
                        publishProgress((int) ((num / (float) totalSize) * 100));
                    }
                });
                // Extra parameters if you want to pass to server
                entity.addPart(KEY_NAME, new StringBody(rname));
                entity.addPart(KEY_EMAIL, new StringBody(remail));
                entity.addPart(KEY_PASSWORD, new StringBody(rpassword));
                entity.addPart(KEY_MOBILENO, new StringBody(rmobile));
                entity.addPart(KEY_CITY, new StringBody(rcity));
                totalSize = entity.getContentLength();
                httppost.setEntity(entity);
                // Making server call
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity r_entity = response.getEntity();
                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == 200) {
                    // Server response
                    responseString = EntityUtils.toString(r_entity);
                } else {
                    responseString = "Error occurred! Http Status Code: " + statusCode;
                }
            } catch (ClientProtocolException e) {
                responseString = e.toString();

            } catch (IOException e) {
                responseString = e.toString();
            }
            return responseString;
        }

        @Override
        public void onPostExecute(String success) {
            try {
                org.json.JSONObject json = new org.json.JSONObject(success);
                success = json.getString("success");
                message = json.getString("message");
                cid = json.getString("cid");
                userimage = json.getString("userimage");
                cname = json.getString("name");
                email = json.getString("email");
                cmobileno = json.getString("mobileno");
                ccity = json.getString("city");
                pdialog.dismiss();

                SharedPreferences preferences = getSharedPreferences("CID_VALUE", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("valueofcid",cid);
                editor.putString("username1",cname);
                editor.putString("emailid1",email);
                editor.putString("mobileno1",cmobileno);
                editor.putString("city1",ccity);
                editor.putString("userimage1",userimage);
                editor.apply();

                if (success == "true") {

                    cid = json.getString("cid");
                    cname = json.getString("name");
                    email = json.getString("email");
                    cmobileno = json.getString("mobileno");
                    ccity = json.getString("city");
                    //Toast.makeText(RegistrationActivityOne.this, message, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RegistrationActivityOne.this, NewHomeActivity.class);
                    Bundle bund = new Bundle();
                    //Inserts a String value into the mapping of this Bundle
                    bund.putString("CID",cid);
                    //Add the bundle to the intent.
                    intent.putExtras(bund);
                    startActivity(intent);
                  } else if (success == "false") {
                    Toast.makeText(RegistrationActivityOne.this, message, Toast.LENGTH_SHORT).show();
                }
            } catch (org.json.JSONException e) {
                e.printStackTrace();
            }
        }
    }

    class UploadFileToServerImage extends AsyncTask<Void, Integer, String> {
        @Override
        protected void onPreExecute() {
            // setting progress bar to zero
            super.onPreExecute();
            pdialog = new ProgressDialog(RegistrationActivityOne.this);
            pdialog.setMessage("Loading...");
            pdialog.setCancelable(false);
            pdialog.show();
        }

        @Override
        protected String doInBackground(Void... params) {
            return uploadFile();
        }
        @SuppressWarnings("deprecation")
        private String uploadFile() {
            String responseString = null;
            String REGISTER_URL = getResources().getString(R.string.base_url) + getResources().getString(R.string.regsitration_one_url);
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(REGISTER_URL);
            try {
                AndroidMultiPartEntity entity = new AndroidMultiPartEntity(new AndroidMultiPartEntity.ProgressListener() {
                    @Override
                    public void transferred(long num) {
                        publishProgress((int) ((num / (float) totalSize) * 100));
                    }
                });
                File sourceFile = new File(filePath);
                System.out.println("sourceFile:" + sourceFile );
                // Adding file data to http body
                entity.addPart(KEY_USERIMAGE, new FileBody(sourceFile));
                // Extra parameters if you want to pass to server
                entity.addPart(KEY_NAME, new StringBody(rname));
                entity.addPart(KEY_EMAIL, new StringBody(remail));
                entity.addPart(KEY_PASSWORD, new StringBody(rpassword));
                entity.addPart(KEY_MOBILENO, new StringBody(rmobile));
                entity.addPart(KEY_CITY, new StringBody(rcity));
                totalSize = entity.getContentLength();
                httppost.setEntity(entity);
                // Making server call
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity r_entity = response.getEntity();
                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == 200) {
                    // Server response
                    responseString = EntityUtils.toString(r_entity);
                } else {
                    responseString = "Error occurred! Http Status Code: " + statusCode;
                }
            } catch (ClientProtocolException e) {
                responseString = e.toString();

            } catch (IOException e) {
                responseString = e.toString();
            }
            return responseString;
        }
        @Override
        public void onPostExecute(String success) {
            try {
                org.json.JSONObject json = new org.json.JSONObject(success);
                success = json.getString("success");
                message = json.getString("message");
                cid = json .getString("cid");
                userimage = json.getString("userimage");
                cname = json.getString("name");
                email = json.getString("email");
                cmobileno = json.getString("mobileno");
                ccity = json.getString("city");
                pdialog.dismiss();

                SharedPreferences preferences = getSharedPreferences("CID_VALUE", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("valueofcid",cid);
                editor.putString("username1",cname);
                editor.putString("emailid1",email);
                editor.putString("mobileno1",cmobileno);
                editor.putString("city1",ccity);
                editor.putString("userimage1",userimage);
                editor.apply();

                if (success == "true") {
                    cid = json.getString("cid");
                    cname = json.getString("name");
                    email = json.getString("email");
                    cmobileno = json.getString("mobileno");
                    ccity = json.getString("city");

                    if (fileDesPath.isDirectory()) {
                        String[] children = fileDesPath.list();
                        for (int i = 0; i < children.length; i++) {
                            new File(fileDesPath, children[i]).delete();
                        }
                        fileDesPath.delete();
                    }
                    Toast.makeText(RegistrationActivityOne.this, message, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RegistrationActivityOne.this, NewHomeActivity.class);
                    Bundle bund = new Bundle();
                    //Inserts a String value into the mapping of this Bundle
                    bund.putString("CID",cid);
                    //Add the bundle to the intent.
                    intent.putExtras(bund);
                    startActivity(intent);
                } else if (success == "false") {
                    Toast.makeText(RegistrationActivityOne.this, message, Toast.LENGTH_SHORT).show();
                }
            } catch (org.json.JSONException e) {
                e.printStackTrace();
            }
        }
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
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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
    // validating mobile number
    private boolean isValidMobileno1(String mobileno1) {
        String MOBILE_PATTERN = "[0-9]{10}";
        Pattern pattern = Pattern.compile(MOBILE_PATTERN);
        Matcher matcher = pattern.matcher(mobileno1);
        return matcher.matches();
    }

}