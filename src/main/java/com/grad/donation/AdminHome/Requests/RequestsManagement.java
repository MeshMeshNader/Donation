package com.grad.donation.AdminHome.Requests;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.grad.donation.Adapters.RequestsAdapter;
import com.grad.donation.AdminHome.AdminHome;
import com.grad.donation.DataModels.RequestDataModel;
import com.grad.donation.R;
import com.grad.donation.Utils.CustomProgress;

import java.util.ArrayList;


public class RequestsManagement extends Fragment {


    View view;
    ArrayList<RequestDataModel> mRequestsList;
    RecyclerView mRequestsRecyclerView;
    LinearLayoutManager layoutManager;
    RequestsAdapter reqAdapter;
    CustomProgress mCustomProgress = CustomProgress.getInstance();
    TextView mNoResults;
    LinearLayout mWaitReqs, mAccReqs, mDecReqs;
    private DatabaseReference mReqsRef;

    public RequestsManagement() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_request_management, container, false);

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

        mReqsRef = FirebaseDatabase.getInstance().getReference("AllRequests");
        mRequestsList = new ArrayList<>();

        mNoResults = view.findViewById(R.id.req_no_result);

        mWaitReqs = view.findViewById(R.id.req_wait_btn);
        mWaitReqs.setOnClickListener(v -> getWaitData());
        mAccReqs = view.findViewById(R.id.req_acc_btn);
        mAccReqs.setOnClickListener(v -> getAccData());
        mDecReqs = view.findViewById(R.id.req_dec_btn);
        mDecReqs.setOnClickListener(v -> getDecData());


        mRequestsRecyclerView = view.findViewById(R.id.admin_req_recyclerview);
        mRequestsRecyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        mRequestsRecyclerView.setLayoutManager(layoutManager);

        getWaitData();


        mCustomProgress.showProgress(getContext(), "Loading Donations..!", true);
    }


    private void getWaitData() {
        mReqsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mRequestsList.clear();
                if (snapshot.exists()) {
                    for (DataSnapshot childSnap : snapshot.getChildren()) {
                        if (childSnap.getValue(RequestDataModel.class).getStatus().equals(getContext().getResources().getString(R.string.wait)))
                            mRequestsList.add(childSnap.getValue(RequestDataModel.class));
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

    private void getAccData() {
        mReqsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mRequestsList.clear();
                if (snapshot.exists()) {
                    for (DataSnapshot childSnap : snapshot.getChildren()) {
                        if (childSnap.getValue(RequestDataModel.class).getStatus().equals(getContext().getResources().getString(R.string.accepted)))
                            mRequestsList.add(childSnap.getValue(RequestDataModel.class));
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

    private void getDecData() {
        mReqsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mRequestsList.clear();
                if (snapshot.exists()) {
                    for (DataSnapshot childSnap : snapshot.getChildren()) {
                        if (childSnap.getValue(RequestDataModel.class).getStatus().equals(getContext().getResources().getString(R.string.declined)))
                            mRequestsList.add(childSnap.getValue(RequestDataModel.class));
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
        if (mRequestsList.size() <= 0)
            mNoResults.setVisibility(View.VISIBLE);
        reqAdapter = new RequestsAdapter(getContext(), mRequestsList);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        mRequestsRecyclerView.setAdapter(reqAdapter);
        mCustomProgress.hideProgress();


    }

    @Override
    public void onResume() {
        super.onResume();
        getWaitData();
    }


}