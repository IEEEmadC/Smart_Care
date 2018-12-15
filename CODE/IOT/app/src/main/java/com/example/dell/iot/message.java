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
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class message extends AppCompatDialogFragment {
    String email;
    TextView t1,t2;
    Context ctx;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.message,null);
        email = this.getArguments().getString("email");
        t1 = (TextView) view.findViewById(R.id.textView);
        t2 = (TextView) view.findViewById(R.id.textView2);
        get_message();
        builder.setView(view).setTitle("Admin Message").setNegativeButton("OK", new DialogInterface.OnClickListener() {
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
    private void get_message() {
        // Tag used to cancel the request
        String tag_string_req = "req_user";
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.MES_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject jObj = null;
                try {
                    jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (error) {
                        // Error in location. Get the error message
                        String errorMsg = jObj.getString("error_msg");
                        t1.setText(errorMsg);

                    }
                    else{

                        String Msg = jObj.getString("message");
                        String updated_at = jObj.getString("updated_at");
                        t1.setText(Msg);
                        t2.setText(updated_at);
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
                params.put("email_u", email);


                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

}
