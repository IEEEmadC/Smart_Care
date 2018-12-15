package com.example.dell.iot;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dell.iot.Model.OpenWeatherMap;
import com.example.dell.iot.Modules.DirectionFinder;
import com.example.dell.iot.Modules.DirectionFinderListener;
import com.example.dell.iot.Modules.Route;
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

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class uuser extends AppCompatActivity implements  LocationListener, OnMapReadyCallback, DirectionFinderListener {
    String lng;
    String lat;
    String phone_s;
    String email_s;
    String name_s;
    String phone;
    String email;
    String name;
    private SQLiteHandler db;
    int MY_PERMISSION = 0;
    LocationManager locationManager;
    String provider;
    static double latt, lngg;
    private GoogleMap mMap;
    private List<Marker> originMarkers = new ArrayList<>();
    private List<Marker> destinationMarkers = new ArrayList<>();
    private List<Polyline> polylinePaths = new ArrayList<>();
    String lng_i,lat_i;
    double a=0.0;
    LinearLayout LL1;
    ImageButton B2,B1,B3;
    Boolean aa=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uuser);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        // SqLite database handler
        db = new SQLiteHandler(getApplicationContext());
        //Get Coordinates
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        provider = locationManager.getBestProvider(new Criteria(), false);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(uuser.this, new String[]{
                    Manifest.permission.INTERNET,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_NETWORK_STATE,
                    Manifest.permission.SYSTEM_ALERT_WINDOW,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            }, MY_PERMISSION);}
        Location location = locationManager.getLastKnownLocation(provider);


        // Fetching photo details from sqlite
        HashMap<String, String> location1 = db.getLocationDetails();
        TextView n,e,p,n1,e1;
        getIncomingIntent();
        n = (TextView) findViewById(R.id.name1222);
        e = (TextView) findViewById(R.id.email1222);
        p = (TextView) findViewById(R.id.phone1222);
        n1 = (TextView) findViewById(R.id.name122);
        e1 = (TextView) findViewById(R.id.email122);
        n.setText(name_s);
        e.setText(email_s);
        p.setText(phone_s);
        n1.setText(name);
        e1.setText(email);
        lat_i = location1.get("lat");
        lng_i = location1.get("lng");
        if(lat_i==null){
            lat_i="0.0";
            lng_i="0.0";
        }
        LL1 = (LinearLayout) findViewById(R.id.LL1);
        B1 = (ImageButton) findViewById(R.id.imageButton);
        B2 = (ImageButton) findViewById(R.id.imageButton1);
        B3 = (ImageButton) findViewById(R.id.imageButton5);

        B1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                   LL1.setVisibility(View.GONE);
                   B2.setVisibility(View.VISIBLE);
                   B1.setVisibility(View.GONE);

            }
        });
        B3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(uuser.this,admin.class);
                startActivity(intent);
                finish();

            }
        });

        B2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                LL1.setVisibility(View.VISIBLE);
                B1.setVisibility(View.VISIBLE);
                B2.setVisibility(View.GONE);

            }
        });


    }

    @SuppressLint("NewApi")
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    protected void onPause() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(uuser.this, new String[]{
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
            ActivityCompat.requestPermissions(uuser.this, new String[]{
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
        if(isNetworkAvailable()){
            latt = location.getLatitude();
            lngg = location.getLongitude();
            lat_i =String.valueOf(latt);
            lng_i = String.valueOf(lngg);
            db.addLocation(String.valueOf(latt),String.valueOf(lngg),"admin","admin");
            sendRequest(lat+","+lng,lat_i+","+lng_i);
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



    private void getIncomingIntent(){

        if(getIntent().hasExtra("name") && getIntent().hasExtra("name_s") && getIntent().hasExtra("email") && getIntent().hasExtra("email_s") && getIntent().hasExtra("phone") && getIntent().hasExtra("phone_s")){
            name = getIntent().getStringExtra("name");
            email = getIntent().getStringExtra("email");
            phone = getIntent().getStringExtra("phone");
            name_s = getIntent().getStringExtra("name_s");
            email_s = getIntent().getStringExtra("email_s");
            phone_s = getIntent().getStringExtra("phone_s");
            lat = getIntent().getStringExtra("lat");
            lng = getIntent().getStringExtra("lng");
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
        LatLng hcmus = new LatLng(Double.parseDouble(lat), Double.parseDouble(lng));
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
        LatLng l1 = new LatLng(Double.parseDouble(lat), Double.parseDouble(lng));
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
        if(a==0.0){ toastMessageS("The direction was found");}
        polylinePaths = new ArrayList<>();
        originMarkers = new ArrayList<>();
        destinationMarkers = new ArrayList<>();
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

                polylinePaths.add(mMap.addPolyline(polylineOptions));}
            a=Double.parseDouble(route.distance.text.split(" ")[0]);
        }

    }

}
