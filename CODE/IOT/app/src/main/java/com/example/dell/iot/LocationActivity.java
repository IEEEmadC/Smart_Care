package com.example.dell.iot;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.PowerManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
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
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

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
import java.util.ArrayList;
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

public class LocationActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, LocationListener, OnMapReadyCallback, DirectionFinderListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {
    private DrawerLayout mDrawerLayout;
    private static final String TAG = LocationActivity.class.getSimpleName();
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
    private SessionManager session;
    NavigationView navigationView;
    private static final int IMAGE_REQUEST_CODE = 1234;
    private Bitmap bitmap;
    private Uri filePath;
    int MY_PERMISSION = 0;
    LocationManager locationManager;
    String provider;
    static double lat, lng;
    String status,email;
    String latt;
    String lngg;
    private GoogleMap mMap;
    private List<Marker> originMarkers = new ArrayList<>();
    private List<Marker> destinationMarkers = new ArrayList<>();
    private List<Polyline> polylinePaths = new ArrayList<>();
    String lng_i,lat_i;
    double a=0.0;

    private GoogleApiClient mGoogleApiClient;
    private Location mLocation;
    private LocationManager mLocationManager;
    private LocationRequest mLocationRequest;
    private com.google.android.gms.location.LocationListener listener;
    private long UPDATE_INTERVAL = 2 * 1000;  /* 10 secs */
    private long FASTEST_INTERVAL = 2000; /* 2 sec */


    @SuppressLint("WrongViewCast")
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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

        //Get Coordinates
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        provider = locationManager.getBestProvider(new Criteria(), false);


        //**************************************************************************************************

        // Fetching user details from sqlite
        user = db.getUserDetails();

        // Fetching photo details from sqlite
        HashMap<String, String> photos = db.getPhotoDetails();

        // Fetching photo details from sqlite
        HashMap<String, String> location1 = db.getLocationDetails();

        //get user info from local data base
        String name = user.get("name");
        email = user.get("email");
        uid = user.get("unique_id");
        String uri = photos.get("photo_url");
        latt = location1.get("lat");
        lngg = location1.get("lng");
        lat_i = user.get("lat_i");
        lng_i = user.get("lng_i");
        //LocationActivity
        //location weather
      /*  if(!location1.isEmpty() && isNetworkAvailable() )
        {
            new GetWeather().execute(Common.apiRequest(latt,lngg));
        }*/

        //mapGoogle



        //photo profile
        if(!photos.isEmpty()) {
            Uri file = Uri.fromFile(new File(uri));
            if(file.toString() != null && file.toString().length()>0){
                try{
                    //download image from storage and put it to destination
                    Picasso.with(LocationActivity.this).load(file).into(imageProfile);
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
                Picasso.with(LocationActivity.this).load(Uri.parse(uri1)).into(imageProfile);
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
        Intent intent = new Intent(LocationActivity.this, LoginActivity.class);
        startActivity(intent);
        LocationActivity.this.finish();
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
                Intent h1= new Intent(LocationActivity.this,navigationActivity.class);
                startActivity(h1);
                break;

            case R.id.nav_Logout:
                logoutUser();
                Intent h2= new Intent(LocationActivity.this,LoginActivity.class);
                startActivity(h2);
                break;
            case R.id.nav_About:
                openAbout();
                break;
            case R.id.nav_Help:
                openHelp();
                break;
            case R.id.nav_mes:
                openMes(user.get("email"));
                break;
            case R.id.nav_map:
                Intent h4= new Intent(LocationActivity.this,LocationActivity.class);
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



    //***********************************************weather****************************************************
    @Override
    protected void onPause() {
        super.onPause();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(LocationActivity.this, new String[]{
                    Manifest.permission.INTERNET,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_NETWORK_STATE,
                    Manifest.permission.SYSTEM_ALERT_WINDOW,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE


            }, MY_PERMISSION);
        }
        locationManager.removeUpdates(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(LocationActivity.this, new String[]{
                    Manifest.permission.INTERNET,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_NETWORK_STATE,
                    Manifest.permission.SYSTEM_ALERT_WINDOW,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE


            }, MY_PERMISSION);
        }
        locationManager.requestLocationUpdates(provider, 400, 1, this);
    }


   @Override

    public void onLocationChanged(Location location) {
       if(isNetworkAvailable()){
        lat = location.getLatitude();
        lng = location.getLongitude();
        latt =String.valueOf(lat);
        lngg = String.valueOf(lng);
        db.addLocation(String.valueOf(latt),String.valueOf(lngg),email,uid);
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

    private void toastMessageS(String message){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
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
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng hcmus = new LatLng(Double.parseDouble(latt), Double.parseDouble(lngg));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(hcmus, 18));

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
        mMap.setMyLocationEnabled(true);
        LatLng l1 = new LatLng(Double.parseDouble(latt), Double.parseDouble(lngg));
        LatLng l2 = new LatLng(Double.parseDouble(lat_i), Double.parseDouble(lng_i));
        sendRequest(l1.latitude+","+l1.longitude,l2.latitude+","+l2.longitude);
    }


    @Override
    public void onDirectionFinderStart() {
       if(a==0.0) {
        toastMessageS("Please wait we will find your direction...!");
       }
        if (originMarkers != null) {
            for (Marker marker : originMarkers) {
                marker.remove();
            }
        }

        if (destinationMarkers != null) {
            for (Marker marker : destinationMarkers) {
                marker.remove();
            }
        }

        if (polylinePaths != null) {
            for (Polyline polyline:polylinePaths ) {
                polyline.remove();
            }
        }

    }

    @Override
    public void onDirectionFinderSuccess(List<Route> routes) {
        polylinePaths = new ArrayList<>();
        originMarkers = new ArrayList<>();
        destinationMarkers = new ArrayList<>();
        if(a==0.0){ toastMessageS("The direction was found");}
        for (Route route : routes) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(route.startLocation, 16));
            ((TextView) findViewById(R.id.tvDuration)).setText(route.duration.text);
            ((TextView) findViewById(R.id.tvDistance)).setText(route.distance.text);
            if(a==0.0 || Math.abs(Double.parseDouble(route.distance.text.split(" ")[0])- a) >= 0.1 ) {
                for (Marker marker : destinationMarkers) {
                    marker.remove();
                }
                for (Marker marker : originMarkers) {
                    marker.remove();
                }
                originMarkers.add(mMap.addMarker(new MarkerOptions()
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.start_blue))
                        .title(route.startAddress)
                        .position(route.startLocation)));
                destinationMarkers.add(mMap.addMarker(new MarkerOptions()
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.end_green))
                        .title(route.endAddress)
                        .position(route.endLocation)));

                PolylineOptions polylineOptions = new PolylineOptions().
                        geodesic(true).
                        color(Color.BLUE).
                        width(10);

                for (int i = 0; i < route.points.size(); i++)
                    polylineOptions.add(route.points.get(i));

                polylinePaths.add(mMap.addPolyline(polylineOptions));

            }
            a=Double.parseDouble(route.distance.text.split(" ")[0]);
        }

    }

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


