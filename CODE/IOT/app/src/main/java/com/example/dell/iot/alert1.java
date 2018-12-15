package com.example.dell.iot;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
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


public class alert1 extends AppCompatDialogFragment {
    private static final String TAG = alert1.class.getSimpleName();
    String status;
    TextView t;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.alert1,null);
        // SqLite database handler

        status = this.getArguments().getString("status");
        t = (TextView) view.findViewById(R.id.textView111);
        if (status.equals("ALERTBATTERY"))
            t.setText("He's battery is running out of charge you may call him !!");
        else if (status.equals("ALERTN"))
            t.setText("He needs help you may call him !!");
        else if (status.equals("ALERTS"))
            t.setText("The wheelchair is not stable you may find him with map !!");
        else
            t.setText("Everything is fine !!");
        builder.setView(view).setTitle("Alert").setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


            }
        });
        return builder.create();
    }


}
