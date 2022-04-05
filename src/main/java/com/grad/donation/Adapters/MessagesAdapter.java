package com.grad.donation.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.grad.donation.DataModels.MessageDataModel;
import com.grad.donation.R;

import java.util.ArrayList;


public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.myMessageViewHolder> {

    Context context;
    ArrayList<MessageDataModel> msgsList;


    public MessagesAdapter(Context context, ArrayList<MessageDataModel> msgsList) {
        this.context = context;
        this.msgsList = msgsList;
    }


    @NonNull
    @Override
    public myMessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_message, parent, false);

        myMessageViewHolder viewHolder;
        viewHolder = new myMessageViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull myMessageViewHolder holder, int position) {

        MessageDataModel msg = msgsList.get(position);

        holder.msgSender.setText("From : " + msg.getUserName());
        holder.msgEmail.setText("Email : " + msg.getUserEmail());
        holder.msgDate.setText(msg.getDate());
        holder.msgContent.setText(msg.getMessage());

    }


    @Override
    public int getItemCount() {
        return msgsList.size();
    }


    public class myMessageViewHolder extends RecyclerView.ViewHolder {


        TextView msgSender, msgEmail, msgDate, msgContent;


        public myMessageViewHolder(@NonNull View itemView) {
            super(itemView);

            msgSender = itemView.findViewById(R.id.msg_sender);
            msgEmail = itemView.findViewById(R.id.msg_email);
            msgDate = itemView.findViewById(R.id.msg_date);
            msgContent = itemView.findViewById(R.id.msg_content);

        }
    }
}
