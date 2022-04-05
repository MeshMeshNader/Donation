package com.grad.donation.AdminHome.Messages;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.grad.donation.Adapters.MessagesAdapter;
import com.grad.donation.AdminHome.AdminHome;
import com.grad.donation.DataModels.MessageDataModel;
import com.grad.donation.R;
import com.grad.donation.Utils.CustomProgress;

import java.util.ArrayList;


public class MessagesManagement extends Fragment implements SwipeRefreshLayout.OnRefreshListener {


    View view;
    ArrayList<MessageDataModel> mMessagesList;
    RecyclerView mMessagesRecyclerView;
    LinearLayoutManager layoutManager;
    MessagesAdapter messageAdapter;
    CustomProgress mCustomProgress = CustomProgress.getInstance();
    SwipeRefreshLayout refreshLayout;
    TextView mNoResults;
    private DatabaseReference mMessagesRef;

    public MessagesManagement() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_message_management, container, false);

        initViews();

        return view;
    }


    private void initViews() {

        AdminHome.mProfileBtn.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_baseline_arrow_back_ios_24));
        AdminHome.mProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });

        mMessagesRef = FirebaseDatabase.getInstance().getReference("AllMessages");
        mMessagesList = new ArrayList<>();

        mNoResults = view.findViewById(R.id.message_no_result);

        mMessagesRecyclerView = view.findViewById(R.id.admin_message_recyclerview);
        mMessagesRecyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        mMessagesRecyclerView.setLayoutManager(layoutManager);

        getData();


        refreshLayout = view.findViewById(R.id.message_management_swipe_down);
        refreshLayout.setOnRefreshListener(this);

        mCustomProgress.showProgress(getContext(), "Loading Messages..!", true);
    }


    private void getData() {
        mMessagesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mMessagesList.clear();
                if (snapshot.exists()) {
                    for (DataSnapshot childSnap : snapshot.getChildren()) {
                        mMessagesList.add(childSnap.getValue(MessageDataModel.class));
                    }
                    if (mNoResults.getVisibility() == View.VISIBLE)
                        mNoResults.setVisibility(View.GONE);
                    showDonations();
                } else {
                    mNoResults.setVisibility(View.VISIBLE);
                    mCustomProgress.hideProgress();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    public void showDonations() {
        messageAdapter = new MessagesAdapter(getContext(), mMessagesList);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        mMessagesRecyclerView.setAdapter(messageAdapter);
        mCustomProgress.hideProgress();


    }

    @Override
    public void onResume() {
        super.onResume();
        getData();
    }


    @Override
    public void onRefresh() {
        getData();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                refreshLayout.setRefreshing(false);
            }
        }, 1000);
    }


}