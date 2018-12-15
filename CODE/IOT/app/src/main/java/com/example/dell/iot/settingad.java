package com.example.dell.iot;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class settingad extends AppCompatDialogFragment {

    String name_u;
    String name_s;
    String email_s;
    String email_u;
    String phone_u;
    String phone_s;
    String latt;
    String lngg;
    String uid;
    String stat;
    Context ctx;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.settingad,null);
        name_u = this.getArguments().getString("name");
        name_s = this.getArguments().getString("name_s");
        email_s = this.getArguments().getString("email_s");
        email_u = this.getArguments().getString("email");
        phone_u = this.getArguments().getString("phone");
        phone_s = this.getArguments().getString("phone_s");
        latt = this.getArguments().getString("lat");
        lngg = this.getArguments().getString("lng");
        uid = this.getArguments().getString("uid");
        stat = this.getArguments().getString("stat");


        Button btDelete = (Button) view.findViewById(R.id.btDelete);
        Button btEditU = (Button) view.findViewById(R.id.btEditU);
        Button btEditS = (Button) view.findViewById(R.id.btEditS);
        Button btEditL = (Button) view.findViewById(R.id.btEditL);
        Button btSend = (Button) view.findViewById(R.id.btSend);
        Button btEditP = (Button) view.findViewById(R.id.btEditP);
        if(stat.equals("User")){
            btEditS.setVisibility(View.GONE);
            btDelete.setVisibility(View.GONE);
            btSend.setVisibility(View.GONE);
        }
        else if(stat.equals("Supervisor")){
            btEditU.setVisibility(View.GONE);
            btEditL.setVisibility(View.GONE);
            btDelete.setVisibility(View.GONE);
            btSend.setVisibility(View.GONE);
        }
        else{
            btEditP.setVisibility(View.GONE);
        }


        btDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delete_user();
            }
        });
        btEditU.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pushFragmentU(getActivity());
            }
        });
        btEditP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pushFragmentP(getActivity());
            }
        });
        btEditS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pushFragmentS(getActivity());
            }
        });
        btEditL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pushFragmentL(getActivity());
            }
        });
        btSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pushFragmentSend(getActivity());
            }
        });
        builder.setView(view).setTitle("Settings").setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        ctx = context;

    }

    private void delete_user() {
        // Tag used to cancel the request
        String tag_string_req = "req_user";
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.DELETEUSER_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                System.out.print(response);

                JSONObject jObj = null;
                try {
                    jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (error) {
                        // Error in location. Get the error message
                        String errorMsg = jObj.getString("error_msg");

                    }
                    else{
                        BackgroundTask backgroundTask = new BackgroundTask(ctx);
                        backgroundTask.execute();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }






            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


            }
        }){

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to insert location
                Map<String, String> params = new HashMap<String, String>();
                params.put("email_u", email_u);
                params.put("email_s", email_s);
                params.put("caption",uid );
                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }
    /**
     * open edit
     */

    public void pushFragmentU(Context context){
        FragmentManager fm = ((FragmentActivity)context).getSupportFragmentManager();
        Bundle bundle = new Bundle();
        bundle.putString("name", name_u );
        bundle.putString("name_s", name_s );
        bundle.putString("email", email_u );
        bundle.putString("email_s", email_s );
        bundle.putString("phone", phone_u );
        bundle.putString("phone_s", phone_s );
        bundle.putString("lat", latt );
        bundle.putString("lng", lngg );
        bundle.putString("stat", stat );
        editadusn pcf = new editadusn();
        pcf.setArguments(bundle);
        pcf.show(fm, "Edit");

        // FragmentTransaction transaction = ((FragmentActivity)context).getSupportFragmentManager().beginTransaction();

    }
    public void pushFragmentP(Context context){
        FragmentManager fm = ((FragmentActivity)context).getSupportFragmentManager();
        Bundle bundle = new Bundle();
        bundle.putString("name", name_u );
        bundle.putString("name_s", name_s );
        bundle.putString("email", email_u );
        bundle.putString("email_s", email_s );
        bundle.putString("phone", phone_u );
        bundle.putString("phone_s", phone_s );
        bundle.putString("lat", latt );
        bundle.putString("lng", lngg );
        bundle.putString("stat", stat );
        editadpsn pcf = new editadpsn();
        pcf.setArguments(bundle);
        pcf.show(fm, "Edit");

        // FragmentTransaction transaction = ((FragmentActivity)context).getSupportFragmentManager().beginTransaction();

    }
    public void pushFragmentS(Context context){
        FragmentManager fm = ((FragmentActivity)context).getSupportFragmentManager();
        Bundle bundle = new Bundle();
        bundle.putString("name", name_u );
        bundle.putString("name_s", name_s );
        bundle.putString("email", email_u );
        bundle.putString("email_s", email_s );
        bundle.putString("phone", phone_u );
        bundle.putString("phone_s", phone_s );
        bundle.putString("lat", latt );
        bundle.putString("lng", lngg );
        bundle.putString("stat", stat );
        editadssn pcf = new editadssn();
        pcf.setArguments(bundle);
        pcf.show(fm, "Edit");

        // FragmentTransaction transaction = ((FragmentActivity)context).getSupportFragmentManager().beginTransaction();

    }
    public void pushFragmentL(Context context){
        FragmentManager fm = ((FragmentActivity)context).getSupportFragmentManager();
        Bundle bundle = new Bundle();
        bundle.putString("name", name_u );
        bundle.putString("name_s", name_s );
        bundle.putString("email", email_u );
        bundle.putString("email_s", email_s );
        bundle.putString("phone", phone_u );
        bundle.putString("phone_s", phone_s );
        bundle.putString("lat", latt );
        bundle.putString("lng", lngg );
        bundle.putString("stat", stat );
        editadnl pcf = new editadnl();
        pcf.setArguments(bundle);
        pcf.show(fm, "Edit");

        // FragmentTransaction transaction = ((FragmentActivity)context).getSupportFragmentManager().beginTransaction();

    }
    public void pushFragmentSend (Context context){
        FragmentManager fm = ((FragmentActivity)context).getSupportFragmentManager();
        Bundle bundle = new Bundle();
        bundle.putString("email", email_u );
        editadsend pcf = new editadsend();
        pcf.setArguments(bundle);
        pcf.show(fm, "Edit");

        // FragmentTransaction transaction = ((FragmentActivity)context).getSupportFragmentManager().beginTransaction();

    }

}
