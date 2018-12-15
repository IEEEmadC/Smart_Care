package com.example.dell.iot;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.dell.iot.Common.Common;
import com.example.dell.iot.Helper.Helper;
import com.example.dell.iot.Model.OpenWeatherMap;
import com.example.dell.iot.Modules.DirectionFinder;
import com.example.dell.iot.Modules.DirectionFinderListener;
import com.example.dell.iot.Modules.Route;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class navigationActivity1 extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, LocationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {
    private DrawerLayout mDrawerLayout;
    private static final String TAG = navigationActivity1.class.getSimpleName();
    private ActionBarDrawerToggle mToggle;
    private Toolbar mToolbar;
    private TextView txtName;
    private TextView txtEmail;
    public ImageView imageProfile;
    public String name_photo;
    public String caption;
    public String uid;
    public String email_u;
    public String phone_u;
    private SQLiteHandler db;
    private SessionManager session;
    NavigationView navigationView;
    private static final int IMAGE_REQUEST_CODE = 1234;
    private Bitmap bitmap;
    private Uri filePath;
    int MY_PERMISSION = 0;
    private ImageView mProgressBar;
    TextView  txtDescription, txtHumidity, txtTime, txtCelsius;
    ImageView imageView;
    ImageView imageView1;
    ImageView imageView2;
    ImageView imageView3;
    ImageView imageView4;
    LinearLayout L1;
    LinearLayout L2;
    LinearLayout L3;
    RelativeLayout R3;
    RelativeLayout R4;
    RelativeLayout R5;
    LocationManager locationManager;
    String provider;
    static double lat, lng;
    String status1="OK",email;
    String latt;
    String lngg;
    double a=0.0;
    HashMap<String, String> superr;

    private GoogleApiClient mGoogleApiClient;
    private Location mLocation;
    private LocationManager mLocationManager;
    private LocationRequest mLocationRequest;
    private com.google.android.gms.location.LocationListener listener;
    private long UPDATE_INTERVAL = 2 * 1000;  /* 10 secs */
    private long FASTEST_INTERVAL = 2000; /* 2 sec */


    OpenWeatherMap openWeatherMap = new OpenWeatherMap();
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation1);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        //navigation
        mToolbar = (Toolbar) findViewById(R.id.nav_action);
        setSupportActionBar(mToolbar);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerlayout);
        mToggle = new ActionBarDrawerToggle(this,mDrawerLayout,R.string.open,R.string.close);

        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        txtName = (TextView) findViewById(R.id.name1);
        txtEmail = (TextView) findViewById(R.id.email1);
        imageProfile=(ImageView)findViewById(R.id.cImageView);
        mProgressBar = (ImageView) findViewById(R.id.pb);

        // SqLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // session manager
        session = new SessionManager(getApplicationContext());

        if (!session.isLoggedIn()) {
            //Here
            logoutUser();
        }

        if(!isNetworkAvailable()){toastMessage("Verify Your Connection");}

        //api google for location

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        mLocationManager = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
        checkLocation();



        //********************************************weather*****************************************************//

        //Control
        txtDescription = (TextView) findViewById(R.id.txtDescription);
        txtHumidity = (TextView) findViewById(R.id.txtHumidity);
        txtTime = (TextView) findViewById(R.id.txtTime);
        txtCelsius = (TextView) findViewById(R.id.txtCelsius);
        imageView = (ImageView) findViewById(R.id.imageView);
        imageView1 = (ImageView) findViewById(R.id.imageView1);
        imageView2 = (ImageView) findViewById(R.id.imageView2);
        imageView3 = (ImageView) findViewById(R.id.imageView3);
        imageView4 = (ImageView) findViewById(R.id.imageView4);
        L1 = (LinearLayout) findViewById(R.id.L1);
        final RelativeLayout R1 = (RelativeLayout) findViewById(R.id.R1);
        R3 = (RelativeLayout) findViewById(R.id.R3);
        R4 = (RelativeLayout) findViewById(R.id.R4);
        R5 = (RelativeLayout) findViewById(R.id.R5);
        L2 = (LinearLayout) findViewById(R.id.L2);
        L3 = (LinearLayout) findViewById(R.id.L3);


        final ViewTreeObserver observer= R1.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) L1.getLayoutParams();
                        LinearLayout.LayoutParams params1 = (LinearLayout.LayoutParams) L2.getLayoutParams();
                        LinearLayout.LayoutParams params2 = (LinearLayout.LayoutParams) L3 .getLayoutParams();
                        params.width=(int) R1.getWidth()/3;
                        params1.width=(int) R1.getWidth()/3;
                        params2.width=(int) R1.getWidth()/3;
                        params.height=(int) R1.getWidth()/3;
                        params1.height=(int) R1.getWidth()/3;
                        params2.height=(int) R1.getWidth()/3;
                        R3.getLayoutParams().width=(int) R1.getWidth()/3;
                        R4.getLayoutParams().width=(int) R1.getWidth()/3;
                        R5.getLayoutParams().width=(int) R1.getWidth()/3;
                        R3.getLayoutParams().height=(int) R1.getWidth()/3;
                        R4.getLayoutParams().height=(int) R1.getWidth()/3;
                        R5.getLayoutParams().height=(int) R1.getWidth()/3;
                        imageView.getLayoutParams().height = (int) (params.width * 0.7);
                        imageView.getLayoutParams().width = (int) (params.width * 0.7);
                        imageView1.getLayoutParams().height = (int) (params.width * 0.7);
                        imageView1.getLayoutParams().width = (int) (params.width * 0.7);
                        imageView2.getLayoutParams().height = (int) (params.width * 0.7);
                        imageView2.getLayoutParams().width = (int) (params.width * 0.7);
                        imageView3.getLayoutParams().height = (int) (params.width + params.width * 0.3);
                        imageView3.getLayoutParams().width = (int) (params.width + params.width * 0.3);
                        imageView4.getLayoutParams().height = (int) (params.width );
                        imageView4.getLayoutParams().width = (int) (params.width );
                        mProgressBar.getLayoutParams().height=(int) (params.width * 0.8);
                        mProgressBar.getLayoutParams().width=(int) (params.width * 0.8);
                        L1.setLayoutParams(params);
                        L2.setLayoutParams(params1);
                        L3.setLayoutParams(params2);
                        txtCelsius.setTextSize((float)(params.width*0.08));
                        txtHumidity.setTextSize((float)(params.width*0.08));
                        txtDescription.setTextSize((float)(params.width*0.08));

                    }
                });



        //Get Coordinates
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        provider = locationManager.getBestProvider(new Criteria(), false);

        //**************************************************************************************************

        // Fetching user details from sqlite
         superr = db.getSuperDetails();

        // Fetching photo details from sqlite
         HashMap<String, String> photos = db.getPhotoDetails();

        // Fetching location details from sqlite
        HashMap<String, String> location1 = db.getLocationDetails();


        //get user info from local data base
        String name = superr.get("name");
        email = superr.get("email");
        email_u = superr.get("email_u");
        phone_u = superr.get("phone_u");
        uid = superr.get("unique_id");
        String uri = photos.get("photo_url");
        String lat1 = location1.get("lat");
        String lng1 = location1.get("lng");
        //mapsactivity
        //location weather
        if(!location1.isEmpty() && isNetworkAvailable() )
        {
           new GetWeather().execute(Common.apiRequest(lat1,lng1));
        }

        //photo profile
        if(!photos.isEmpty()) {
            Uri file = Uri.fromFile(new File(uri));
            if(file.toString() != null && file.toString().length()>0){
                try{
                    //download image from storage and put it to destination
                    Picasso.with(navigationActivity1.this).load(file).into(imageProfile);
                    imageProfile.setBackgroundColor(0);
                }
                catch (Exception e){e.printStackTrace();
                }}
                else{
                String uri1 = AppConfig.SERVER_URL + "uploads/" ;
                String exe = photos.get("photo_name");
                String exee = photos.get("caption")+"."+exe.split(".")[exe.length()];
                uri1 += uri1 + exee;
                //download image from storage and put it to destination
                Picasso.with(navigationActivity1.this).load(Uri.parse(uri1)).into(imageProfile);
                imageProfile.setBackgroundColor(0);
            }

        }

        //Displaying the user details on the screen
        txtEmail.setText(email);
        txtName.setText(name);


        //*********************************************Listners**********************************************
        //choose image
        imageProfile.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                if(view == imageProfile){
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Complete action using"), IMAGE_REQUEST_CODE);
                }
            }});

        L1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                                Intent h3= new Intent(navigationActivity1.this,Weather.class);
                                startActivity(h3);
            }});

        L2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent h3= new Intent(navigationActivity1.this,Weather.class);
                startActivity(h3);
            }});

        L3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent h3= new Intent(navigationActivity1.this,Weather.class);
                startActivity(h3);
            }});
        R3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent h3= new Intent(navigationActivity1.this,LocationActivity1.class);
                startActivity(h3);
            }});
        R4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent callIntent =new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:"+phone_u));
                if (ActivityCompat.checkSelfPermission(navigationActivity1.this,
                        Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                startActivity(callIntent);
            }
        });


        R5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //emergency Alert
                openAlert1();
            }
        });

        //********************************************THREADS***************************************//
        Thread t = new Thread(){
            @Override
            public void run(){
                while (session.runningThread){

                    try {
                        Thread.sleep(10000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (!session.isLoggedIn()) {
                                    //Here
                                    logoutUser();
                                }
                                get_emergency();
                            }
                        });

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }

            }
    };
        t.start();
    }




    //navigation
    @Override
    public void onBackPressed(){
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawerlayout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(mToggle.onOptionsItemSelected(item))
        {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Logging out the user. Will set isLoggedIn flag to false in shared
     * preferences Clears the user data from sqlite Users table
     * */
    private void logoutUser() {
        session.setLogin(false);
        session.setThread(false);

        db.deleteUsers();

        // Launching the login activity
        Intent intent = new Intent(navigationActivity1.this, LoginActivity.class);
        startActivity(intent);
        navigationActivity1.this.finish();
    }

    /**
     * navigation button for nav window
     * @param item
     * @return
     */
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id){
            case R.id.nav_account:
                //logoutUser();
                Intent h1= new Intent(navigationActivity1.this,navigationActivity1.class);
                startActivity(h1);
                break;

            case R.id.nav_Logout:
                logoutUser();
                Intent h2= new Intent(navigationActivity1.this,LoginActivity.class);
                startActivity(h2);
                break;
            case R.id.nav_About:
                openAbout();
                break;
            case R.id.nav_Help:
                openHelp();
                break;
            case R.id.nav_map:
                Intent h4= new Intent(navigationActivity1.this,LocationActivity1.class);
                startActivity(h4);
                break;
            case R.id.nav_Settings:
                pushFragment(superr.get("name_u"),superr.get("email_u"),superr.get("phone_u"),superr.get("name"),superr.get("email"),superr.get("phone"),superr.get("lat_i"),superr.get("lng_i"),superr.get("unique_id_u"),"Supervisor");
                break;
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawerlayout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    /**
     * after chosing image from storage the result is onActivityresult
     * Add photo information to local data base
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == IMAGE_REQUEST_CODE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imageProfile.setImageBitmap(bitmap);
                uploadMultipart();
                imageProfile.setBackgroundColor(0);
                //add photo to local database
                String pathh=getPath(filePath);
                db.addPhoto(name_photo,pathh,caption);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * upload photo to data base with her information
     */

    public void uploadMultipart() {
        caption = uid.trim();
        //getting the actual path of the image
        String path = getPath(filePath);
        name_photo = path.split("/")[path.split("/").length-1];
        //Uploading code
        try {
            String uploadId = UUID.randomUUID().toString();
            //Creating a multi part request
            new MultipartUploadRequest(this, uploadId, AppConfig.UPLOAD_URL)
                    .addFileToUpload(path, "image") //Adding file
                    .addParameter("caption", caption) //Adding text parameter to the request
                    .addParameter("name-ph", name_photo)
                    .addParameter("photo_url", path)
                    .setNotificationConfig(new UploadNotificationConfig())
                    .setMaxRetries(2)
                    .startUpload(); //Starting the upload
        } catch (Exception exc) {
            Toast.makeText(this, exc.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * upload photo to data base with her information onEmergency
     */

    public void uploadMultipartE(String path) {

        //Uploading code
        try {
            String uploadId = UUID.randomUUID().toString();
            //Creating a multi part request
            new MultipartUploadRequest(this, uploadId, AppConfig.UPLOAD_URL1)
                    .addFileToUpload(path, "image") //Adding file
                    .addParameter("name", email) //Adding text parameter to the request
                    .setMaxRetries(2)
                    .startUpload(); //Starting the upload
        } catch (Exception exc) {
            Toast.makeText(this, exc.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * get the real URI of photo
     * @param uri
     * @return
     */
    public String getPath(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();

        cursor = getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();

        return path;
    }

    /**
     * open help
     */
    public void openHelp() {
        help help1 = new help();
        help1.show(getSupportFragmentManager(),"Help");
    }

    /**
     * open about us
     */
    public void openAbout() {
        about_us about_us1 = new about_us();
        about_us1.show(getSupportFragmentManager(),"About us");
    }

    //***********************************************weather****************************************************
    @SuppressLint("NewApi")
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    protected void onPause() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(navigationActivity1.this, new String[]{
                    Manifest.permission.INTERNET,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_NETWORK_STATE,
                    Manifest.permission.SYSTEM_ALERT_WINDOW,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE


            }, MY_PERMISSION);
        }
        locationManager.removeUpdates(this);
        super.onPause();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onResume() {
        super.onResume();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED  ) {
            ActivityCompat.requestPermissions(navigationActivity1.this, new String[]{
                    Manifest.permission.INTERNET,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_NETWORK_STATE,
                    Manifest.permission.SYSTEM_ALERT_WINDOW,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,

            }, MY_PERMISSION);
        }
        locationManager.requestLocationUpdates(provider, 400, 1, this);
    }

    @Override
    public void onLocationChanged(Location location) {
        if(isNetworkAvailable()) {
            lat = location.getLatitude();
            lng = location.getLongitude();
            db.addLocation(String.valueOf(lat), String.valueOf(lng), email, uid);
            latt = String.valueOf(lat);
            lngg = String.valueOf(lng);
            new GetWeather().execute(Common.apiRequest(latt, lngg));
        }
        else{
            toastMessage("Verify Your Connection");
        }

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }


    private class GetWeather extends AsyncTask<String,Void,String>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }


        @Override
        protected String doInBackground(String... params) {
            String stream = null;
            String urlString = params[0];

            Helper http = new Helper();
            stream = http.getHTTPData(urlString);
            return stream;
        }

        @SuppressLint("DefaultLocale")
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(s.contains("Error: Not found city")){
                return;
            }
            Gson gson = new Gson();
            Type mType = new TypeToken<OpenWeatherMap>(){}.getType();
            openWeatherMap = gson.fromJson(s,mType);
            txtDescription.setText(String.format("%s",openWeatherMap.getWeather().get(0).getDescription()));
            txtHumidity.setText(String.format("%d%%",openWeatherMap.getMain().getHumidity()));
            //txtTime.setText(String.format("%s/%s",Common.unixTimeStampToDateTime(openWeatherMap.getSys().getSunrise()),Common.unixTimeStampToDateTime(openWeatherMap.getSys().getSunset())));
            txtCelsius.setText(String.format("%.2f °C",openWeatherMap.getMain().getTemp()));
            Picasso.with(navigationActivity1.this)
                    .load(Common.getImage(openWeatherMap.getWeather().get(0).getIcon()))
                    .into(imageView);
            imageView1.setImageResource(R.drawable.humidity);
            imageView2.setImageResource(R.drawable.temp);
            getSupportActionBar().setTitle(getString(R.string.app_name)+ " :     " + "You are in " +
                    String.format("%s , %s",openWeatherMap.getName(),openWeatherMap.getSys().getCountry())
            + " on " +String.format("%s", Common.getDateNow()) );


        }

    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    //toaast Message
    private void toastMessage(String message){
        Toast.makeText(this,message,Toast.LENGTH_LONG).show();
    }


    private void get_emergency() {
        // Tag used to cancel the request
        String tag_string_req = "req_emergency";
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.EMERGENCYGET_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject jObj = null;
                try {
                    jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (error) {
                        // Error in location. Get the error message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                        Log.e(TAG, "get emergency error: " + errorMsg);
                    }
                    else{

                        String emergency = jObj.getString("status");
                        if(emergency.equals("ALERTS")){
                            status1="ALERTS";
                            ImageView alert = (ImageView) findViewById(R.id.imageView4);
                            alert.setImageResource(R.drawable.alert);
                            Log.e(TAG, "get emergency " + emergency);
                        }
                        else  if(emergency.equals("ALERTN")){
                            status1="ALERTN";
                            ImageView alert = (ImageView) findViewById(R.id.imageView4);
                            alert.setImageResource(R.drawable.alert);
                            Log.e(TAG, "get emergency " + emergency);
                        }
                        else  if(emergency.equals("ALERTBATTERY")){
                            status1="ALERTBATTERY";
                            ImageView alert = (ImageView) findViewById(R.id.imageView4);
                            alert.setImageResource(R.drawable.alert);
                            Log.e(TAG, "get emergency " + emergency);
                        }
                        else {
                            status1="OK";
                            ImageView alert = (ImageView) findViewById(R.id.imageView4);
                            alert.setImageResource(R.drawable.ok);
                        }

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }






            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "get emergency error: " + error.getMessage());

            }
        }){

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to get location
                Map<String, String> params = new HashMap<String, String>();
                params.put("email_u", email_u);
                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }


    private void toastMessageS(String message){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }

    //open activity supervisor
    public void openAlert1() {
        alert1 alert11 = new alert1();
        Bundle bundle = new Bundle();
        bundle.putString("status", status1 );
        alert11.setArguments(bundle);
        alert11.show(getSupportFragmentManager(),"Alert");
    }

    /**
     * open setting
     */

    public void pushFragment( String n, String e, String p, String n_s, String e_s, String p_s, String lat, String lng, String uid, String stat){
        FragmentManager fm = getSupportFragmentManager();
        Bundle bundle = new Bundle();
        bundle.putString("name", n );
        bundle.putString("name_s", n_s );
        bundle.putString("email", e );
        bundle.putString("email_s", e_s );
        bundle.putString("phone", p );
        bundle.putString("phone_s", p_s );
        bundle.putString("lat", lat );
        bundle.putString("lng", lng );
        bundle.putString("uid",uid);
        bundle.putString("stat",stat);
        settingad pcf = new settingad();
        pcf.setArguments(bundle);
        pcf.show(fm, "Settings");

        // FragmentTransaction transaction = ((FragmentActivity)context).getSupportFragmentManager().beginTransaction();
        }


    @Override
    public void onConnected(Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        startLocationUpdates();

        mLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if(mLocation == null){
            startLocationUpdates();
        }
        if (mLocation != null) {

            // mLatitudeTextView.setText(String.valueOf(mLocation.getLatitude()));
            //mLongitudeTextView.setText(String.valueOf(mLocation.getLongitude()));
        } else {
            Toast.makeText(this, "Location not Detected", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "Connection Suspended");
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i(TAG, "Connection failed. Error: " + connectionResult.getErrorCode());
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    protected void startLocationUpdates() {
        // Create the location request
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(UPDATE_INTERVAL)
                .setFastestInterval(FASTEST_INTERVAL);
        // Request location updates
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
                mLocationRequest, this);
    }

    private boolean checkLocation() {
        if(!isLocationEnabled())
            showAlert();
        return isLocationEnabled();
    }
    private void showAlert() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Enable Location")
                .setMessage("Your Locations Settings is set to 'Off'.\nPlease Enable Location to " +
                        "use this app")
                .setPositiveButton("Location Settings", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {

                        Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(myIntent);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {

                    }
                });
        dialog.show();
    }

    private boolean isLocationEnabled() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }





}


