package com.example.dell.iot;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class editadnl extends AppCompatDialogFragment {
    String name_u;
    String name_s;
    String email_s;
    String email_u;
    String phone_u;
    String phone_s;
    String latt;
    String lngg;
    String stat;
    Context ctx;
    private SQLiteHandler db;
    private SessionManager session;


    EditText t1,t2;
    String n1,n2;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.editlad,null);
        name_u = this.getArguments().getString("name");
        name_s = this.getArguments().getString("name_s");
        email_s = this.getArguments().getString("email_s");
        email_u = this.getArguments().getString("email");
        phone_u = this.getArguments().getString("phone");
        phone_s = this.getArguments().getString("phone_s");
        latt = this.getArguments().getString("lat");
        lngg = this.getArguments().getString("lng");
        stat = this.getArguments().getString("stat");

        db = new SQLiteHandler(ctx);

        // session manager
        session = new SessionManager(ctx);


        t1 = (EditText) view.findViewById(R.id.txEditl);
        t2 = (EditText) view.findViewById(R.id.txDeletel);

        t1.setText(latt);
        t2.setText(lngg);





        builder.setView(view).setTitle("Edit Location").setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                n1 = t1.getText().toString().trim();
                n2 = t2.getText().toString().trim();
                latt = n1;
                lngg = n2;
                edit_loc();

            }
        });

        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        ctx = context;

    }
    private void edit_loc() {
        // Tag used to cancel the request
        String tag_string_req = "req_user";
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.LOC_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                JSONObject jObj = null;
                try {
                    jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (error) {
                        // Error in location. Get the error message
                        String errorMsg = jObj.getString("error_msg");
                        System.out.print("error : " + errorMsg);

                    }
                    else{
                        if(!stat.equals("User")){
                            BackgroundTask backgroundTask = new BackgroundTask(ctx);
                            backgroundTask.execute();}
                        else{

                            logoutUser();
                            Toast.makeText(ctx,"You should disconnect now !!",Toast.LENGTH_LONG).show();
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }






            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.print("error" );

            }
        }){

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to insert location
                Map<String, String> params = new HashMap<String, String>();
                params.put("email_u", email_u);
                params.put("email_s", email_s);
                params.put("lat", latt);
                params.put("lng", lngg);
                params.put("name_u", name_u);
                params.put("phone_u", phone_u);
                params.put("name_s", name_s);
                params.put("phone_s", phone_s);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }


    private void logoutUser() {
        session.setLogin(false);
        db.deleteUsers();

    }
}
