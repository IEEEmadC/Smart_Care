package com.example.dell.iot;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.RecyclerViewHolder> {

    private static final int TYPE_HEAD = 0;
    private static final int TYPE_LIST = 1;
    ArrayList<Users> arrayList = new ArrayList<>();
    Context ctx;

    public RecyclerAdapter(ArrayList<Users> arrayList, Context ctx) {

        this.arrayList = arrayList;
        this.ctx=ctx;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType==TYPE_HEAD){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.header_layout, parent, false);
            RecyclerViewHolder recyclerViewHolder = new RecyclerViewHolder(view,viewType);
            return recyclerViewHolder;
        }
        else if(viewType==TYPE_LIST){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_layout, parent, false);
            RecyclerViewHolder recyclerViewHolder = new RecyclerViewHolder(view,viewType);
            return recyclerViewHolder;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, final int position) {


        if(holder.viewType==TYPE_LIST) {
            Users users = arrayList.get(position-1);
            // set Views
            holder.name.setText(users.getName());
            holder.email.setText(users.getEmail());
            holder.phone.setText(users.getPhone());
            if(!users.getStatus().equals("OK")){
                holder.adm.setBackgroundColor(Color.parseColor("#B3FF0000"));
            }
            else
                holder.adm.setBackgroundColor(Color.parseColor("#1A353333"));
            holder.adm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(ctx,uuser.class);
                    intent.putExtra("name",arrayList.get(position-1).getName());
                    intent.putExtra("name_s",arrayList.get(position-1).getName_s());
                    intent.putExtra("email",arrayList.get(position-1).getEmail());
                    intent.putExtra("email_s",arrayList.get(position-1).getEmail_s());
                    intent.putExtra("phone",arrayList.get(position-1).getPhone());
                    intent.putExtra("phone_s",arrayList.get(position-1).getPhone_s());
                    intent.putExtra("lat",arrayList.get(position-1).getLat());
                    intent.putExtra("lng",arrayList.get(position-1).getLng());
                    intent.putExtra("status",arrayList.get(position-1).getStatus());
                    ctx.startActivity(intent);
                }
            });
            holder.adm.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    // do your logic for long click and remember to return it
                    pushFragment(ctx,arrayList.get(position-1).getName(),arrayList.get(position-1).getEmail(),arrayList.get(position-1).getPhone(),arrayList.get(position-1).getName_s(),arrayList.get(position-1).getEmail_s(),arrayList.get(position-1).getPhone_s(),arrayList.get(position-1).getLat(),arrayList.get(position-1).getLng(),arrayList.get(position-1).getUid(),"null");

                    return true; }});

        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size()+1;
    }

    public static class RecyclerViewHolder extends RecyclerView.ViewHolder {
        TextView name, email, phone, name_s, email_s, phone_s;
        int viewType;
        LinearLayout adm;
        public RecyclerViewHolder(View view,int viewType) {
            super(view);
            if(viewType==TYPE_LIST){
                name = (TextView) view.findViewById(R.id.name12);
                phone = (TextView) view.findViewById(R.id.phone12);
                email = (TextView) view.findViewById(R.id.email12);
                adm = (LinearLayout) view.findViewById(R.id.admino);

                this.viewType=TYPE_LIST;
                }
            else if(viewType==TYPE_HEAD){
                //find view by id
                this.viewType=TYPE_HEAD;
            }

        }

    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0)
            return TYPE_HEAD;
            return TYPE_LIST;
    }
    /**
     * open setting
     */

    public void pushFragment(Context context, String n, String e, String p, String n_s, String e_s, String p_s, String lat, String lng, String uid, String stat){
        FragmentManager fm = ((FragmentActivity)context).getSupportFragmentManager();
        Bundle bundle = new Bundle();
        bundle.putString("name", n );
        bundle.putString("name_s", n_s );
        bundle.putString("email", e );
        bundle.putString("email_s", e_s );
        bundle.putString("phone", p );
        bundle.putString("phone_s", p_s );
        bundle.putString("lat", lat );
        bundle.putString("lng", lng );
        bundle.putString("uid",uid);
        bundle.putString("stat",stat);
        settingad pcf = new settingad();
        pcf.setArguments(bundle);
        pcf.show(fm, "Settings");

        // FragmentTransaction transaction = ((FragmentActivity)context).getSupportFragmentManager().beginTransaction();

    }
}
