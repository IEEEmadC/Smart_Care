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

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class editadsend extends AppCompatDialogFragment {
    String email_s;
    String email_u;
    Context ctx;


    EditText t1;
    String n1;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.editsend,null);
        email_u = this.getArguments().getString("email");



        t1 = (EditText) view.findViewById(R.id.txEditSend);

        builder.setView(view).setTitle("Messsage").setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).setPositiveButton("Send", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                n1 = t1.getText().toString().trim();

                edit_send();

            }
        });

        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        ctx = context;

    }
    private void edit_send() {
        // Tag used to cancel the request
        String tag_string_req = "req_user";
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.SEND_URL, new Response.Listener<String>() {
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
                System.out.print("error" );

            }
        }){

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to insert location
                Map<String, String> params = new HashMap<String, String>();
                params.put("email_u", email_u);
                params.put("message", n1);
                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }



}
