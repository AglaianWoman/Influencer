package com.socialbeat.influencer;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
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
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
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
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class MyProfileNew extends AppCompatActivity {

    private static final String PREFS_NAME = "MyProfileNew";
    private static final int REQUEST_TAKE_PHOTO = 1;
    Context context;
    private CoordinatorLayout coordinatorLayout;
    private static final String TAG = MyProfileNew.class.getSimpleName();
    EditText pname,pabout,pmobileno,paddress,plocationcity;
    TextView pdob,pcity,pcitydummy,pgender,pgenderdummy;
    Button psave;
    String mCurrentPhotoPath,last,location,lasted;
    Boolean isInternetPresent = false;
    ConnectionDetector cd;
    String cid, userChoosenTask;
    ImageView puserimage;
    RadioGroup radioGender;
    private DatePicker datePicker;
    private Calendar calendar;
    private int year, month, day;

    int currentapiVersion;
    private String filePath = null,egender=null;
    long totalSize = 0;
    int a = 0;
    File fileDesPath = null;

    String ename,eabout,emobileno,eaddress,ecity,edob;
    String cname,about,gender,mobileno,address,city,message,response,userimage,dob;
    private ProgressDialog pdialog;
    String gendervalue=null;
    String valueofgender  = null;
    RadioButton radioMale,radioFemale;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private Uri fileUri;
    // Camera activity request codes
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;

    public static final String KEY_APPNAME = "Influencer";
    public static final String KEY_USERIMAGE = "userimage";
    public static final String KEY_CID = "cid";
    public static final String KEY_NAME = "cname";
    public static final String KEY_ABOUT = "about";
    public static final String KEY_GENDER = "gender";
    public static final String KEY_DOB = "dob";
    public static final String KEY_MOBILENO = "mobileno";
    public static final String KEY_ADDRESS = "address";
    public static final String KEY_CITY = "city";

    Spinner spinnerDropDown,spinnerDropDownnew;
    String[] cities = {
            "",
            "Chennai",
            "Bangalore",
            "Mumbai",
            "Hyderabad",
            "Ahmedabad",
            "Delhi",
            "Kolkata",
            "Pune",
            "Others"
    };
    String[] genders = {
            "",
            "Male",
            "Female",
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myprofilenew);

        ActionBar bar = getSupportActionBar();
        bar.setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Edit Profile");

        currentapiVersion = android.os.Build.VERSION.SDK_INT;

        // Get reference of SpinnerView from layout/main_activity.xml
        spinnerDropDown =(Spinner)findViewById(R.id.city_spinner);
        ArrayAdapter<String> adapter= new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item ,cities);
        spinnerDropDown.setAdapter(adapter);

        spinnerDropDownnew =(Spinner)findViewById(R.id.gender_spinner);
        ArrayAdapter<String> adapternew= new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item ,genders);
        spinnerDropDownnew.setAdapter(adapternew);


        SharedPreferences prfs = getSharedPreferences("CID_VALUE", Context.MODE_PRIVATE);
        cid = prfs.getString("valueofcid", "");

        pname = (EditText) findViewById(R.id.nme_profile);
        pabout = (EditText) findViewById(R.id.about_profile);
        pdob = (TextView) findViewById(R.id.dob_profile);
        pmobileno = (EditText) findViewById(R.id.mobile_profile);
        paddress = (EditText) findViewById(R.id.address_profile);
        pcity = (TextView) findViewById(R.id.city_profile);
        pcitydummy = (TextView) findViewById(R.id.city_profile0);
        plocationcity = (EditText) findViewById(R.id.city_profile1);
        pgender = (TextView) findViewById(R.id.gender_profile);
        pgenderdummy = (TextView) findViewById(R.id.gender_profile0);
        psave = (Button) findViewById(R.id.save_button);
        puserimage = (ImageView) findViewById(R.id.profileimage);

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        showDate(year, month+1, day);
        String value="4";
        
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        cd = new ConnectionDetector(this);
        isInternetPresent = cd.isConnectingToInternet();


        SharedPreferences settings1 = getSharedPreferences(PREFS_NAME, 0);
        boolean firstStart = settings1.getBoolean("firstStart", true);
        if(cid.length()!=0){
            if (isInternetPresent) {
                MyApplication.getInstance().trackEvent("MyProfileDummy Screen", "OnClick", "Track MyProfileDummy Event");
                profileFunction();
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

        puserimage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // capture picture
                boolean result = checkPermission(MyProfileNew.this);
                userChoosenTask = "Take Photo";
                if (result) {
                    selectImage();
                } else {
                }
            }
        });

        // Checking camera availability
        if (!isDeviceSupportCamera()) {
            Toast.makeText(getApplicationContext(), "Sorry! Your device doesn't support camera", Toast.LENGTH_LONG).show();
            // will close the app if the device does't have camera
            finish();
        }

        spinnerDropDown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                // Get select item
                int sid=spinnerDropDown.getSelectedItemPosition();
                last = cities[sid].toString();
                if(sid>0){
                    pcity.setVisibility(View.INVISIBLE);
                }else{
                    pcity.setVisibility(View.VISIBLE);
                }
                //Toast.makeText(getBaseContext(), "You have selected City : " + last, Toast.LENGTH_SHORT).show();
                if (last.equalsIgnoreCase("Others")){
                    plocationcity.setVisibility(View.VISIBLE);
                    location = plocationcity.getText().toString();
                }else {
                    plocationcity.setVisibility(View.INVISIBLE);
                    location = last;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });

        spinnerDropDownnew.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                // Get select item
                int sidi=spinnerDropDownnew.getSelectedItemPosition();
                lasted = genders[sidi].toString();
                if(sidi>0){
                    pgender.setVisibility(View.INVISIBLE);
                }else{
                    pgender.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });



        psave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isInternetPresent) {
                    ename = pname.getText().toString();
                    eabout = pabout.getText().toString();
                    egender = lasted;
                    edob = pdob.getText().toString();
                    emobileno = pmobileno.getText().toString();
                    eaddress = paddress.getText().toString();

                    if (location.length()!=0){
                        ecity = last;
                    }else {
                        ecity = plocationcity.getText().toString();
                    }
                    if (a == 1) {
                        Log.v("Image", "YES");
                        new UploadFileToServerImage().execute();
                    } else {
                        Log.v("Image", "NO");
                        new UploadFileToServer().execute();
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

    @SuppressWarnings("deprecation")
    public void setDate(View view) {
        showDialog(999);
        //Toast.makeText(getApplicationContext(), "ca", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 999) {
            return new DatePickerDialog(this, myDateListener, year, month, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
                    // TODO Auto-generated method stub
                    // arg1 = year
                    // arg2 = month
                    // arg3 = day
                    showDate(arg1, arg2+1, arg3);
                }
            };

    private void showDate(int year, int month, int day) {
        pdob.setText(new StringBuilder().append(day).append("-").append(month).append("-").append(year));
    }

    private void profileFunction() {
        pdialog = new ProgressDialog(MyProfileNew.this);
        pdialog.setMessage("Loading...");
        pdialog.setCancelable(false);
        pdialog.show();
        String PROFILE_URL = getResources().getString(R.string.base_url) + getResources().getString(R.string.profile_url);
        Log.v("PROFILE_URL : ", PROFILE_URL);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, PROFILE_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String success) {
                try {
                    JSONObject json = new JSONObject(success);
                    response = json.getString("success");
                    message = json.getString("message");
                    cname = json.getString("name");
                    about = json.getString("about");
                    gender = json.getString("gender");
                    dob = json.getString("dob");
                    mobileno = json.getString("mobileno");
                    address = json.getString("address");
                    city = json.getString("city");
                    userimage = json.getString("userimage");

                    //set value to edittext box
                    pname.setText(cname);
                    pabout.setText(about);
                    pdob.setText(dob);
                    pmobileno.setText(mobileno);
                    paddress.setText(address);
                    pcity.setText(city);
                    pgender.setText(gender);


                    //Image
                    Glide.with(getApplicationContext()).load(userimage)
                            .thumbnail(0.5f)
                            .crossFade()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(puserimage);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                pdialog.dismiss();
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MyProfileNew.this, error.toString(), Toast.LENGTH_LONG).show();
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
        // super.onBackPressed();  // optional depending on your needs
        Intent intent = new Intent(this, UserSettings.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), "Camera");
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
                Intent intent = new Intent(this, UserSettings.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void selectImage() {

        final CharSequence[] items = {"Take Photo", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(MyProfileNew.this);
        builder.setTitle("Select Profile Image");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = checkPermission(MyProfileNew.this);
                if (items[item].equals("Take Photo")) {
                    userChoosenTask = "Take Photo";
                    if (result)


                        Log.v("current Version", String.valueOf(currentapiVersion));
                        Log.v("Bulid Version", String.valueOf(Build.VERSION_CODES.N));

                        if (currentapiVersion >= Build.VERSION_CODES.N) {
                            // Do something for lollipop and above versions
                            Log.v("current Version", String.valueOf(currentapiVersion));
                            Log.v("Bulid Version", String.valueOf(Build.VERSION_CODES.N));
                            Log.v("OS Version","N+");
                            startCamera();
                        } else {
                            Log.v("OS Version","N-");
                            captureImage();
                        }

                } else if (items[item].equals("Cancel")) {
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
                Uri photoURI = FileProvider.getUriForFile(MyProfileNew.this, BuildConfig.APPLICATION_ID + ".provider", createImageFile());
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
    //selecting img from gallery
    private void galleryImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
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
                puserimage.setImageBitmap(pictureBitmap);
                // ScanFile so it will be appeared on Gallery
                MediaScannerConnection.scanFile(MyProfileNew.this, new String[]{imageUri.getPath()}, null, new MediaScannerConnection.OnScanCompletedListener() {
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
                        puserimage.setImageBitmap(pictureBitmap);
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
            pdialog = new ProgressDialog(MyProfileNew.this);
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
            String REGISTER_URL = getResources().getString(R.string.base_url) + getResources().getString(R.string.userInfo_url);
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(REGISTER_URL);
            try {
                AndroidMultiPartEntity entity = new AndroidMultiPartEntity(new AndroidMultiPartEntity.ProgressListener() {
                    @Override
                    public void transferred(long num) {
                        publishProgress((int) ((num / (float) totalSize) * 100));
                    }
                });

                Log.v("New Gender Value is : ",egender);
                entity.addPart(KEY_CID, new StringBody(cid));
                entity.addPart(KEY_NAME, new StringBody(ename));
                entity.addPart(KEY_ABOUT, new StringBody(eabout));
                entity.addPart(KEY_GENDER, new StringBody(egender));
                entity.addPart(KEY_DOB, new StringBody(edob));
                entity.addPart(KEY_MOBILENO, new StringBody(emobileno));
                entity.addPart(KEY_ADDRESS, new StringBody(eaddress));
                entity.addPart(KEY_CITY, new StringBody(ecity));

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
                success = json.getString("success").toString();
                message = json.getString("message").toString();
                cid = json.getString("cid").toString();
                Log.v("success", success);
                Log.v("message", message);
                Log.v("cid", cid);
//                Log.v("userimage", userimage);
                pdialog.dismiss();

                if (success == "true") {

                    //Toast.makeText(RegistrationActivityOne.this, message, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MyProfileNew.this, UserSettings.class);
                    Bundle bund = new Bundle();
                    //Inserts a String value into the mapping of this Bundle
                    bund.putString("CID",cid);
                    //Add the bundle to the intent.
                    intent.putExtras(bund);
                    startActivity(intent);
                    Toast.makeText(MyProfileNew.this, message, Toast.LENGTH_SHORT).show();
                } else if (success == "false") {
                    Toast.makeText(MyProfileNew.this, message, Toast.LENGTH_SHORT).show();
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
            pdialog = new ProgressDialog(MyProfileNew.this);
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
            String REGISTER_URL = getResources().getString(R.string.base_url) + getResources().getString(R.string.userInfo_url);
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

                entity.addPart(KEY_CID, new StringBody(cid));
                entity.addPart(KEY_NAME, new StringBody(ename));
                entity.addPart(KEY_ABOUT, new StringBody(eabout));
                entity.addPart(KEY_GENDER, new StringBody(egender));
                entity.addPart(KEY_DOB, new StringBody(edob));
                entity.addPart(KEY_MOBILENO, new StringBody(emobileno));
                entity.addPart(KEY_ADDRESS, new StringBody(eaddress));
                entity.addPart(KEY_CITY, new StringBody(ecity));

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
                success = json.getString("success").toString();
                message = json.getString("message").toString();
                cid = json.getString("cid").toString();
                Log.v("success", success);
                Log.v("message", message);
                Log.v("cid", cid);
//                Log.v("userimage", userimage);
                pdialog.dismiss();

                if (success == "true") {
                    if (fileDesPath.isDirectory()) {
                        String[] children = fileDesPath.list();
                        for (int i = 0; i < children.length; i++) {
                            new File(fileDesPath, children[i]).delete();
                        }
                        fileDesPath.delete();
                    }
                    //Toast.makeText(RegistrationActivityOne.this, message, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MyProfileNew.this, UserSettings.class);
                    Bundle bund = new Bundle();
                    //Inserts a String value into the mapping of this Bundle
                    bund.putString("CID",cid);
                    //Add the bundle to the intent.
                    intent.putExtras(bund);
                    startActivity(intent);
                    Toast.makeText(MyProfileNew.this, message, Toast.LENGTH_SHORT).show();
                } else if (success == "false") {
                    Toast.makeText(MyProfileNew.this, message, Toast.LENGTH_SHORT).show();
                }
            } catch (org.json.JSONException e) {
                e.printStackTrace();
            }
        }
    }


}
