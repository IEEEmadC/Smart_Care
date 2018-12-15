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


public class editadpsn extends AppCompatDialogFragment {
    String email_s;
    String email_u;
    String email;
    String stat;
    Context ctx;
    private SQLiteHandler db;
    private SessionManager session;


    EditText t1;
    String n1;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.editadp,null);
        email_s = this.getArguments().getString("email_s");
        email_u = this.getArguments().getString("email");
        stat = this.getArguments().getString("stat");
        // SqLite database handler
        db = new SQLiteHandler(ctx);

        // session manager
        session = new SessionManager(ctx);

        t1 = (EditText) view.findViewById(R.id.txEdit);
        if(stat.equals("User"))
            email=email_u;
        else
            email = email_s;






        builder.setView(view).setTitle("Edit Password").setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                n1 = t1.getText().toString().trim();
                if (!n1.isEmpty())
                    edit_pass();

            }
        });

        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        ctx = context;

    }
    private void edit_pass() {
        // Tag used to cancel the request
        String tag_string_req = "req_user";
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_PASS, new Response.Listener<String>() {
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
                        if(!stat.equals("User") || !stat.equals("Supervisor") ){
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

            }
        }){

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to insert location
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", email);
                params.put("password", n1);
                params.put("stat", stat);

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
