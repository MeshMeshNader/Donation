package com.grad.donation.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.grad.donation.DataModels.RequestDataModel;
import com.grad.donation.R;

import java.util.ArrayList;


public class RequestsAdapter extends RecyclerView.Adapter<RequestsAdapter.myRequestViewHolder> {

    Context context;
    ArrayList<RequestDataModel> reqsList;
    DatabaseReference ReqRef;


    public RequestsAdapter(Context context, ArrayList<RequestDataModel> reqsList) {
        this.context = context;
        this.reqsList = reqsList;
    }


    @NonNull
    @Override
    public myRequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_request, parent, false);

        myRequestViewHolder viewHolder;
        viewHolder = new myRequestViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull myRequestViewHolder holder, int position) {

        RequestDataModel req = reqsList.get(position);

        holder.reqItemLabel.setText("Donation : " + req.getPostLabel());
        holder.reqItemUserLabel.setText("User Name : " + req.getUserName());
        holder.reqStatus.setText("Request Status : " + req.getStatus());

        if (req.getStatus().equals(context.getResources().getString(R.string.accepted)) ||
                req.getStatus().equals(context.getResources().getString(R.string.declined))) {
            holder.reqAcc.setVisibility(View.GONE);
            holder.reqDec.setVisibility(View.GONE);
        } else {


            holder.reqAcc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ReqRef = FirebaseDatabase.getInstance().getReference("AllRequests").child(req.getRequestKey());

                    new AlertDialog.Builder(context)
                            .setMessage("Do you want to Accept this Request!")
                            .setCancelable(false)
                            .setPositiveButton("Yes!", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    ReqRef.child("status").setValue(context.getResources().getString(R.string.accepted)).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            notifyDataSetChanged();
                                        }
                                    });
                                }
                            })
                            .setNegativeButton("No!", null)
                            .show();
                }
            });

            holder.reqDec.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ReqRef = FirebaseDatabase.getInstance().getReference("AllRequests").child(req.getRequestKey());

                    new AlertDialog.Builder(context)
                            .setMessage("Do you want to Decline this Request!")
                            .setCancelable(false)
                            .setPositiveButton("Yes!", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    ReqRef.child("status").setValue(context.getResources().getString(R.string.declined)).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            notifyDataSetChanged();
                                        }
                                    });
                                }
                            })
                            .setNegativeButton("No!", null)
                            .show();
                }
            });


        }

    }


    @Override
    public int getItemCount() {
        return reqsList.size();
    }


    public class myRequestViewHolder extends RecyclerView.ViewHolder {


        ImageView reqAcc, reqDec;
        TextView reqItemLabel, reqItemUserLabel, reqStatus;


        public myRequestViewHolder(@NonNull View itemView) {
            super(itemView);

            reqAcc = itemView.findViewById(R.id.req_acc);
            reqDec = itemView.findViewById(R.id.req_dec);
            reqItemLabel = itemView.findViewById(R.id.req_label);
            reqItemUserLabel = itemView.findViewById(R.id.req_user_label);
            reqStatus = itemView.findViewById(R.id.req_status);

        }
    }
}
