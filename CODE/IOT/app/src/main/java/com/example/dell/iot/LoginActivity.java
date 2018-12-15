/**
 * Author: Hergli Sedki
 */
package com.example.dell.iot;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import com.example.dell.iot.R;
import com.example.dell.iot.AppConfig;
import com.example.dell.iot.AppController;
import com.example.dell.iot.SQLiteHandler;
import com.example.dell.iot.SessionManager;
import com.wingsofts.threedlayout.ThreeDLayout;

public class LoginActivity extends AppCompatActivity implements LocationListener,Terms.TermsListener {
    private static final String TAG = LoginActivity.class.getSimpleName();
    private Button btnLogin;
    private Button btnLinkToRegister;
    private Button btnUseTerms;
    private EditText inputEmail1;
    private EditText inputPassword1;
    private ProgressDialog pDialog;
    private SessionManager session;
    private static final int REQUEST_CAMERA_PERMISSION = 200;
    private SQLiteHandler db;
    String status="any";
    int MY_PERMISSION = 0;
    LocationManager locationManager;
    String provider;
    String uid11;
    String email11;
    CheckBox supervisor,user;
    static double lat, lng;
    String name,email,password,phone;
    ThreeDLayout td_login;
    AlertDialog dialog;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(LoginActivity.this, new String[]{
                    Manifest.permission.INTERNET,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_NETWORK_STATE,
                    Manifest.permission.SYSTEM_ALERT_WINDOW,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.CALL_PHONE
            }, MY_PERMISSION);
            try{
            Location location = locationManager.getLastKnownLocation(provider);
                if (location == null)
                {
                    Toast.makeText(getApplicationContext(),
                            "We can't detect your position Try again !!", Toast.LENGTH_LONG)
                            .show();
                }
            }
            catch (Exception e) {
                e.printStackTrace();

            } }
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,new String[]{
                    Manifest.permission.CAMERA,
            },REQUEST_CAMERA_PERMISSION);}

        //*****************************Location******************************
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        provider = locationManager.getBestProvider(new Criteria(), false);




        inputEmail1 = (EditText) findViewById(R.id.email);
        inputPassword1 = (EditText) findViewById(R.id.password);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLinkToRegister = (Button) findViewById(R.id.btnLinkToRegisterScreen);
        btnUseTerms = (Button) findViewById(R.id.btnUseTerms);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        // SQLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // Session manager
        session = new SessionManager(getApplicationContext());
        HashMap<String, String> type = db.getTypeDetails();
        // Check if user is already logged in or not
        if (session.isLoggedIn() && !type.get("st").isEmpty()) {
            // User is already logged in. Take him to main activity
            if(type.get("st").equals("User")){
            Intent intent = new Intent(LoginActivity.this, navigationActivity.class);
            startActivity(intent);
            finish();}
            else if(type.get("st").equals("Supervisor")){
                Intent intent = new Intent(LoginActivity.this, navigationActivity1.class);
                startActivity(intent);
                finish();}
                else{
                Intent intent = new Intent(LoginActivity.this,admin.class);
                startActivity(intent);
            }
        }

        if(!isNetworkAvailable()){toastMessage("Verify Your Connection");}
        user = (CheckBox) findViewById(R.id.user);
        supervisor = (CheckBox) findViewById(R.id.supervisor);
        supervisor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                user.setChecked(false);
                status = "Supervisor";

            }
        });
        //********************************Listners********************************
        user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                supervisor.setChecked(false);
                status = "User";
            }
        });

        // Login button Click Event
        btnLogin.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                String email = inputEmail1.getText().toString().trim();
                String password = inputPassword1.getText().toString().trim();
                // Check for empty data in the form
                if (!email.isEmpty() && !password.isEmpty()) {
                    //Here
                    // login user
                    if(email.equals("admin@admin.admin") && password.equals("azerty")){
                        db.addType("Admin");
                        session.setLogin(true);
                        session.setThread(true);
                        Intent intent = new Intent(LoginActivity.this,admin.class);
                        startActivity(intent);
                    }
                    else
                        checkLogin(email, password);
                } else {
                    // Prompt user to enter credentials
                    Toast.makeText(getApplicationContext(),
                            "Please enter the credentials!", Toast.LENGTH_LONG)
                            .show();
                }
/*
                session.setLogin(true);
                session.setThread(true);
               Intent intent = new Intent(LoginActivity.this, navigationActivity.class);
                startActivity(intent);
                finish();*/
                          }

        });

        // Link to Register Screen
        btnLinkToRegister.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),
                        RegisterActivity.class);
                startActivity(i);
                finish();
            }
        });
        btnUseTerms.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                openTerms();
            }
        });
    }

    /**
     * function to verify login details in mysql db
     * */
    private void checkLogin(final String email, final String password) {
        // Tag used to cancel the request
        String tag_string_req = "req_login";

        pDialog.setMessage("Logging in ...");
        showDialog();

        StringRequest strReq = new StringRequest(Method.POST,
                AppConfig.URL_LOGIN, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Login Response: " + response);
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    // Check for error node in json
                    if (!error) {
                        // user successfully logged in
                        // Create login session
                        session.setLogin(true);
                        session.setThread(true);
                        uid11 = jObj.getString("uid");
                        String statuts = jObj.getString("statuts");
                        JSONObject user = jObj.getJSONObject("user");
                        JSONObject photo = jObj.getJSONObject("photo");
                        JSONObject superr = jObj.getJSONObject("super");
                        JSONObject location = jObj.getJSONObject("location");
                        String name = user.getString("name");
                        email11 = user.getString("email");
                        String created_at = user
                                .getString("created_at");
                        String phone = user.getString("phone");
                        String lat = user.getString("lat");
                        String lng = user.getString("lng");
                        String name_s = superr.getString("name");
                        String email_s = superr.getString("email");
                        String phone_s = superr.getString("phone");
                        String created_at1 = superr.getString("created_at");
                        String uids = superr.getString("uid");
                        String late = location.getString("lat");
                        String lnge = location.getString("lng");

                        // Inserting row in Users table
                        db.addUser(name, email11, uid11, phone, lat, lng, name_s, email_s, phone_s, uids, created_at);

                        // Inserting row in supers table
                        db.addSuper(name_s, email_s, uids, phone_s, name, email11, uid11, phone, lat, lng, created_at1);





                        boolean error_ph = photo.getBoolean("error");
                        if(!error_ph) {
                            String photo_url = user.getString("photo_url");
                            String photo_name = user.getString("photo_name");
                            String caption = user.getString("caption");
                            // Inserting row in photos table
                            db.addPhoto(photo_name,photo_url,caption);
                        }


                        //les données doit etre mixte entre les base de donnees unique id or mail
                        if(statuts.equals("User")){
                        // Launch main activity
                            db.addType("User");
                            db.addStat("OK");
                            db.addBalance(email11,uid11,"9.0","0.0","5.0");
                            db.addSensor(uid11,email11,"0","0","0","0","0","0","0","0","0");
                            //add location
                            db.addLocation(late, lnge, email11, uid11);
                            Intent intent = new Intent(LoginActivity.this,
                                    navigationActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        if(statuts.equals("Supervisor")){
                            // Launch main activity
                            db.addType("Supervisor");
                            //add location
                            db.addLocation(late, lnge, email_s, uids);
                            Intent intent = new Intent(LoginActivity.this, navigationActivity1.class);
                            startActivity(intent);
                            finish();
                            }
                    } else {
                        // Error in login. Get the error message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", email);
                params.put("password", password);
                params.put("status", status);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
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

    @Override
    public void onLocationChanged(Location location) {
        lat = location.getLatitude();
        lng = location.getLongitude();
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
    /**
     * get location
     */

    @Override
    protected void onPause() {
        super.onPause();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(LoginActivity.this, new String[]{
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
            ActivityCompat.requestPermissions(LoginActivity.this, new String[]{
                    Manifest.permission.INTERNET,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_NETWORK_STATE,
                    Manifest.permission.SYSTEM_ALERT_WINDOW,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE


            }, MY_PERMISSION);
        }
        try{
        locationManager.requestLocationUpdates(provider, 400, 1, this);}
        catch(Exception e) {
            e.printStackTrace();
        }
    }
    public void openTerms()
    {
        Terms terms = new Terms();
        terms.show(getSupportFragmentManager(),"Supervisor Register");
    }

}