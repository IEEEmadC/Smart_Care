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


public class battery_alert extends AppCompatDialogFragment {
    private BatteryAlertListener listener;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.battery_alert,null);
        builder.setView(view).setTitle("LOW BATTERY").setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                boolean alert = false;
                listener.passData(alert);

            }
        }).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
           boolean alert = true;
           listener.passData(alert);
            }
        });
        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (BatteryAlertListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "must implement BatteryAlertListener ");
        }
    }

    public interface BatteryAlertListener{
        void passData(boolean P);
    }
}
