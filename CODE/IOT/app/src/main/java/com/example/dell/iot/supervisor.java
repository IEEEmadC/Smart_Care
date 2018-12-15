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


public class supervisor extends AppCompatDialogFragment {
    private EditText name_s,email_s,phone_s,password_s;
    private SupervisorListener listener;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.supervisor,null);
        builder.setView(view).setTitle("Supervisor").setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String n1 = name_s.getText().toString();
                String n2 = email_s.getText().toString();
                String n4 = password_s.getText().toString();
                String n3 = phone_s.getText().toString();

                listener.passData(n1,n2,n3,n4);
            }
        });
        name_s=view.findViewById(R.id.name_s);
        email_s=view.findViewById(R.id.email_s);
        password_s=view.findViewById(R.id.password_s);
        phone_s=view.findViewById(R.id.phone_s);
        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (SupervisorListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "must implement SupervisorListener ");
        }
    }

    public interface SupervisorListener{
        void passData(String n1,String n2,String n3,String n4);
    }
}
