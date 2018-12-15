package com.example.dell.iot;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.ImageFormat;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.Image;
import android.media.ImageReader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.PowerManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Size;
import android.util.SparseIntArray;
import android.view.Surface;
import android.view.TextureView;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.dell.iot.Modules.DirectionFinder;
import com.example.dell.iot.Modules.DirectionFinderListener;
import com.example.dell.iot.Modules.Route;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import com.example.dell.iot.Common.Common;
import com.example.dell.iot.Helper.Helper;
import com.example.dell.iot.Model.OpenWeatherMap;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Menu;
import android.view.MenuItem;
import  android.support.v7.widget.Toolbar;
import java.io.File;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;
import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class navigationActivity extends AppCompatActivity implements battery_alert.BatteryAlertListener,NavigationView.OnNavigationItemSelectedListener, LocationListener, SensorEventListener, DirectionFinderListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {
    private DrawerLayout mDrawerLayout;
    private static final String TAG = navigationActivity.class.getSimpleName();
    private ActionBarDrawerToggle mToggle;
    private Toolbar mToolbar;
    private TextView txtName;
    private TextView txtEmail;
    public ImageView imageProfile;
    public String name_photo;
    public String caption;
    public String uid;
    private SQLiteHandler db;
    HashMap<String, String> user;
    HashMap<String, String> sensors;
    private SessionManager session;
    NavigationView navigationView;
    private static final int IMAGE_REQUEST_CODE = 1234;
    private Bitmap bitmap;
    private Uri filePath;
    int MY_PERMISSION = 0;
    private TextView mTextViewPercentage;
    private ProgressBar mProgressBar;
    TextView  txtDescription, txtHumidity, txtTime, txtCelsius;
    ImageView imageView;
    ImageView imageView1;
    ImageView imageView2;
    ImageView imageView3;
    ImageView imageView4;
    LinearLayout L1;
    LinearLayout LL1;
    LinearLayout L2;
    LinearLayout L3;
    RelativeLayout R3;
    RelativeLayout R4;
    RelativeLayout R5;
    LocationManager locationManager;
    String provider;
    String created_at;
    static double lat, lng;
    double max_v = 9.00,min_v=6.5,voltage=9.00,SOC,current,battery_mah,temperature,humidity,distance;
    String status1="OK",email;
    String latt;
    String lngg;
    String statutsbat;
    private Sensor mySensor;
    private SensorManager SM;
    String X,Y,Z;
    String lng_i,lat_i;
    double a=0.0;
    boolean cancelstatu=false;

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
        setContentView(R.layout.activity_navigation);
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
        mTextViewPercentage = (TextView) findViewById(R.id.tv_percentage);
        mProgressBar = (ProgressBar) findViewById(R.id.pb);

        //api google for location

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        mLocationManager = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
        checkLocation();

        // SqLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // session manager
        session = new SessionManager(getApplicationContext());

        if (!session.isLoggedIn()) {
            //Here
            logoutUser();
        }

        if(!isNetworkAvailable()){toastMessage("Verify Your Connection");}


        //*********************************************shake******************************************************//
        // Create our Sensor Manager
        SM = (SensorManager)getSystemService(SENSOR_SERVICE);

        // Accelerometer Sensor
        mySensor = SM.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        // Register sensor Listener
        SM.registerListener(this, mySensor,10000000);

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
        LL1 = (LinearLayout) findViewById(R.id.LL1);
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
                        params.width=(int) LL1.getWidth()/3;
                        params1.width=(int) LL1.getWidth()/3;
                        params2.width=(int) LL1.getWidth()/3;
                        params.height=(int) LL1.getWidth()/3;
                        params1.height=(int) LL1.getWidth()/3;
                        params2.height=(int) LL1.getWidth()/3;
                        R3.getLayoutParams().width=(int) LL1.getWidth()/3;
                        R4.getLayoutParams().width=(int) LL1.getWidth()/3;
                        R5.getLayoutParams().width=(int) LL1.getWidth()/3;
                        R3.getLayoutParams().height=(int) LL1.getWidth()/3;
                        R4.getLayoutParams().height=(int) LL1.getWidth()/3;
                        R5.getLayoutParams().height=(int) LL1.getWidth()/3;
                        imageView.getLayoutParams().height = (int) (params.height * 0.7);
                        imageView.getLayoutParams().width = (int) (params.width * 0.7);
                        imageView1.getLayoutParams().height = (int) (params.height * 0.7);
                        imageView1.getLayoutParams().width = (int) (params.width * 0.7);
                        imageView2.getLayoutParams().height = (int) (params.height * 0.7);
                        imageView2.getLayoutParams().width = (int) (params.width * 0.7);
                        imageView3.getLayoutParams().height = (int) (params.height + params.height * 0.3);
                        imageView3.getLayoutParams().width = (int) (params.width + params.width * 0.3);
                        imageView4.getLayoutParams().height = (int) (params.height );
                        imageView4.getLayoutParams().width = (int) (params.width );
                        mProgressBar.getLayoutParams().height=(int) (params.height * 0.8);
                        mProgressBar.getLayoutParams().width=(int) (params.height * 0.8);
                        L1.setLayoutParams(params);
                        L2.setLayoutParams(params1);
                        L3.setLayoutParams(params2);
                        txtCelsius.setTextSize((float)(params.width*0.08));
                        txtHumidity.setTextSize((float)(params.width*0.08));
                        txtDescription.setTextSize((float)(params.width*0.08));
                        mTextViewPercentage.setTextSize((float)(params.width*0.08));

                    }
                });



        //Get Coordinates
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        provider = locationManager.getBestProvider(new Criteria(), false);

        //**************************************************************************************************

        // Fetching user details from sqlite
        user = db.getUserDetails();

        // Fetching photo details from sqlite
         HashMap<String, String> photos = db.getPhotoDetails();

        // Fetching location details from sqlite
        HashMap<String, String> location1 = db.getLocationDetails();

        // Fetching sensor details from sqlite
        sensors = db.getSensorDetails();

        // Fetching stat details from sqlite
        HashMap<String, String> stat = db.getStatDetails();

        //map_distance
        lat_i = user.get("lat_i");
        lng_i = user.get("lng_i");
       System.out.print(sensors.toString());
        if(!sensors.isEmpty()) {
            voltage = Double.valueOf(sensors.get("voltage"));
            min_v = Double.valueOf(sensors.get("min_v"));
            voltage = Double.valueOf(sensors.get("max_v"));
            battery_mah = Double.valueOf(sensors.get("battery_mah"));
            current = Double.valueOf(sensors.get("current"));
            distance = Double.valueOf(sensors.get("dist"));
            statutsbat = stat.get("stat");

            SOC = (voltage-min_v)/(max_v-min_v);
            mProgressBar.setProgress((int) SOC*100);
            mTextViewPercentage.setText(""+String.valueOf(SOC*100)+" %");
            if(battery_mah*SOC/current<=distance/6 && statutsbat.equals("OK")){
                status1="ALERTBATTERY";
                ImageView alert = (ImageView) findViewById(R.id.imageView4);
                alert.setImageResource(R.drawable.alert);
                insert_emergency();
                toastMessage("You should return to your home now to charge your battery");
                openbat();
                if(cancelstatu){
                Intent h4= new Intent(navigationActivity.this,LocationActivity.class);
                startActivity(h4);}
            }
            else {
                status1="OK";
                ImageView alert = (ImageView) findViewById(R.id.imageView4);
                alert.setImageResource(R.drawable.ok);
            }


        }
        else{
            mProgressBar.setProgress(0);
            mTextViewPercentage.setText("No information");
        }




        //get user info from local data base
        String name = user.get("name");
        email = user.get("email");
        uid = user.get("unique_id");
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
                    Picasso.with(navigationActivity.this).load(file).into(imageProfile);
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
                Picasso.with(navigationActivity.this).load(Uri.parse(uri1)).into(imageProfile);
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
                                Intent h3= new Intent(navigationActivity.this,Weather.class);
                                startActivity(h3);
            }});

        L2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent h3= new Intent(navigationActivity.this,Weather.class);
                startActivity(h3);
            }});

        L3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent h3= new Intent(navigationActivity.this,Weather.class);
                startActivity(h3);
            }});
        R4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             openAlert();
            }
        });

        R5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //emergency Alert
                if(status1.equals("ALERTS")||status1.equals("ALERTBATTERY")||status1.equals("ALERTN"))
                {
                    status1="OK";
                    insert_emergency();

                }
            }
        });

        //********************************************THREADS***************************************//
       /* Thread t = new Thread(){
            @Override
            public void run(){
                while (!isInterrupted()){

                    try {
                        Thread.sleep(10000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                            }
                        });

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }

            }
    };
        t.start();*/

        Thread t1 = new Thread(){
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
                                get_sensor();
                                get_emergency();
                            }
                        });

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }

            }
        };
        t1.start();
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
        Intent intent = new Intent(navigationActivity.this, LoginActivity.class);
        startActivity(intent);
        navigationActivity.this.finish();
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
                Intent h1= new Intent(navigationActivity.this,navigationActivity.class);
                startActivity(h1);
                break;

            case R.id.nav_Logout:
                logoutUser();
                Intent h2= new Intent(navigationActivity.this,LoginActivity.class);
                startActivity(h2);
                break;
            case R.id.nav_About:
                openAbout();
                break;
            case R.id.nav_mes:
                openMes(user.get("email"));
                break;
            case R.id.nav_Help:
                openHelp();
                break;
            case R.id.nav_map:
                Intent h4= new Intent(navigationActivity.this,LocationActivity.class);
                startActivity(h4);
                break;
            case R.id.nav_Settings:
                pushFragment(user.get("name"),user.get("email"),user.get("phone"),user.get("name_u"),user.get("email_u"),user.get("phone_u"),user.get("lat_i"),user.get("lng_i"),user.get("unique_id"),"User");
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
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
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

    /**
     * open admin message
     */
    public void openMes(String n) {
            FragmentManager fm = getSupportFragmentManager();
            Bundle bundle = new Bundle();
            bundle.putString("email", n );
            message pcf = new message();
            pcf.setArguments(bundle);
            pcf.show(fm, "Messages");
    }


    //***********************************************weather****************************************************
    @SuppressLint("NewApi")
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    protected void onPause() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(navigationActivity.this, new String[]{
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
            ActivityCompat.requestPermissions(navigationActivity.this, new String[]{
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
            sendRequest(latt+","+lngg,lat_i+","+lng_i);
            insert_location();
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

    @SuppressLint("NewApi")
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if(isNetworkAvailable()){
            X = String.valueOf(sensorEvent.values[0]);
            Y = String.valueOf(sensorEvent.values[1]);
            Z = String.valueOf(sensorEvent.values[2]);
            // Fetching balance details from sqlite
            HashMap<String, String> balance1 = db.getBalanceDetails();
            if(!balance1.isEmpty()){
                String X_1 = balance1.get("X");
                String Y_1 = balance1.get("Y");
                String Z_1 = balance1.get("Z");
                if((Math.abs(Double.parseDouble(X_1)-Double.parseDouble(X))>=3 || Math.abs(Double.parseDouble(Y_1)-Double.parseDouble(Y))>=3 || Math.abs(Double.parseDouble(Z_1)-Double.parseDouble(Z))>=3)&& status1.equals("OK")){
                    db.addBalance(email,uid,X,Y,Z);
                    status1="ALERTS";
                    //webscrapting
                    ImageView alert = (ImageView) findViewById(R.id.imageView4);
                    alert.setImageResource(R.drawable.alert);
                    insert_emergency();

                }
            }
            else{
              db.addBalance(email,uid,X,Y,Z);
            }

        }
        else{
            toastMessage("Verify Your Connection");
        }


    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

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
            Picasso.with(navigationActivity.this)
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

    //insert location on database
    private void insert_location() {
        // Tag used to cancel the request
        String tag_string_req = "req_location";
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.LOCATION_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Insert Location Response: " + response);

                JSONObject jObj = null;
                try {
                    jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (error) {
                        // Error in location. Get the error message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Insert location error: " + error.getMessage());
                toastMessage(error.getMessage());

            }
        }){

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to insert location
                Map<String, String> params = new HashMap<String, String>();
                params.put("email_u", email);
                params.put("lat", latt);
                params.put("lng", lngg);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }


    private void insert_emergency() {
        // Tag used to cancel the request
        String tag_string_req = "req_emergency";
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.EMERGENCY_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Insert emergency Response: " + response);

                JSONObject jObj = null;
                try {
                    jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (error) {
                        // Error in location. Get the error message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                        Log.e(TAG, "Insert emergency error: " + errorMsg);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }






            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Insert emergency error: " + error.getMessage());

            }
        }){

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to insert location
                Map<String, String> params = new HashMap<String, String>();
                params.put("unique_id_u", uid);
                params.put("email_u", email);
                params.put("status", status1);
                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void get_emergency() {
        // Tag used to cancel the request
        String tag_string_req = "req_emergency";
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.EMERGENCYGET_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "get emergency Response: " + response);

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
                        if(emergency.equals("ALERTS")||emergency.equals("ALERTBATTERY")||emergency.equals("ALERTN")){
                            status1=emergency;
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
                Log.e(TAG, "Insert emergency error: " + error.getMessage());

            }
        }){

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to insert location
                Map<String, String> params = new HashMap<String, String>();
                params.put("email_u", email);
                params.put("status", status1);
                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void get_sensor() {
        // Tag used to cancel the request
        String tag_string_req = "req_sensor";
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.SENSORGET_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "get sensor Response: " + response);

                JSONObject jObj = null;
                try {
                    jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (error) {
                        // Error in location. Get the error message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                "Error with server !", Toast.LENGTH_LONG).show();
                        Log.e(TAG, "get emergency error: " + errorMsg);
                    }
                    else{

                        humidity = Double.valueOf(jObj.getString("humidity"));
                        temperature = Double.valueOf(jObj.getString("temperature"));
                        current = Double.valueOf(jObj.getString("current"));
                        voltage = Double.valueOf(jObj.getString("voltage"));
                        battery_mah = Double.valueOf(jObj.getString("battery_mah"));
                        max_v = Double.valueOf(jObj.getString("max_v"));
                        min_v = Double.valueOf(jObj.getString("min_v"));
                        created_at = jObj.getString("created_at");
                        distance = Double.valueOf(sensors.get("dist"));
                        if(String.valueOf(distance)!="null") {
                            db.addSensor(uid, email, String.valueOf(humidity), String.valueOf(temperature), String.valueOf(current), String.valueOf(voltage), String.valueOf(battery_mah), String.valueOf(max_v), String.valueOf(min_v), String.valueOf(distance), created_at);
                        }
                        else
                            db.addSensor(uid, email, String.valueOf(humidity), String.valueOf(temperature), String.valueOf(current), String.valueOf(voltage), String.valueOf(battery_mah), String.valueOf(max_v), String.valueOf(min_v), "null", created_at);

                        SOC = (voltage-min_v)/(max_v-min_v);
                        HashMap<String, String> stat = db.getStatDetails();
                        statutsbat = stat.get("stat");
                        if(battery_mah*SOC/current<=distance/6 && statutsbat.equals("OK")){
                            status1="ALERTBATTRY";
                            ImageView alert = (ImageView) findViewById(R.id.imageView4);
                            alert.setImageResource(R.drawable.alert);
                            insert_emergency();
                            openbat();
                            toastMessage("You should return to your home now to charge your battery");
                            if(cancelstatu)
                            {
                            Intent h4= new Intent(navigationActivity.this,LocationActivity.class);
                            startActivity(h4);}
                        }
                        else{status1="OK";
                            ImageView alert = (ImageView) findViewById(R.id.imageView4);
                            alert.setImageResource(R.drawable.alert);
                        }
                        mProgressBar.setProgress((int) (SOC*100) );
                        mTextViewPercentage.setText(""+String.valueOf((int)(SOC*100))+" %");
                            Log.e(TAG, "get emergency ");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }






            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Insert emergency error: " + error.getMessage());

            }
        }){

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to insert location
                Map<String, String> params = new HashMap<String, String>();
                params.put("email_u", email);
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
    public void openAlert() {
        alert alert1 = new alert();
        alert1.show(getSupportFragmentManager(),"Alert");
    }

    public void openbat()
    {
        battery_alert battery_alert1 = new battery_alert();
        battery_alert1.show(getSupportFragmentManager(),"battery alert");
    }

    @Override
    public void passData(boolean P) {
        cancelstatu = P;
    }

    private void sendRequest(String origin,String destination) {

        if (origin.isEmpty()) {
            Toast.makeText(this, "Please verify gps!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (destination.isEmpty()) {
            Toast.makeText(this, "Please verify gps!", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            new DirectionFinder(this, origin, destination).execute();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDirectionFinderStart() {
    }

    @Override
    public void onDirectionFinderSuccess(List<Route> routes) {
        for (Route route : routes) {
            distance = Double.parseDouble(route.distance.text.split(" ")[0]);
            db.addSensor(uid, email, String.valueOf(humidity), String.valueOf(temperature), String.valueOf(current), String.valueOf(voltage), String.valueOf(battery_mah), String.valueOf(max_v), String.valueOf(min_v), String.valueOf(distance), created_at);

        }

    }
    /**
     * open setting
     */

    public void pushFragment(String n, String e, String p, String n_s, String e_s, String p_s, String lat, String lng, String uid, String stat){
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


