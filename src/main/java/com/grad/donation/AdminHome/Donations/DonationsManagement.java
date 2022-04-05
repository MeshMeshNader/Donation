package com.grad.donation.AdminHome.Donations;

import android.content.Intent;
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

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.grad.donation.Adapters.PostsAdapter;
import com.grad.donation.AdminHome.AdminHome;
import com.grad.donation.AdminHome.Post.AddPost;
import com.grad.donation.DataModels.PostDataModel;
import com.grad.donation.R;
import com.grad.donation.Utils.CustomProgress;

import java.util.ArrayList;


public class DonationsManagement extends Fragment implements SwipeRefreshLayout.OnRefreshListener {


    View view;
    ArrayList<PostDataModel> mDonationsList;
    RecyclerView mDonationsRecyclerView;
    LinearLayoutManager layoutManager;
    PostsAdapter postAdapter;
    CustomProgress mCustomProgress = CustomProgress.getInstance();
    SwipeRefreshLayout refreshLayout;
    FloatingActionButton fab;
    TextView mNoResults;
    private DatabaseReference mDonationsRef;

    public DonationsManagement() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_donation_management, container, false);

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

        mDonationsRef = FirebaseDatabase.getInstance().getReference(getResources().getString(R.string.donations_ref));
        mDonationsList = new ArrayList<>();

        mNoResults = view.findViewById(R.id.donation_no_result);

        mDonationsRecyclerView = view.findViewById(R.id.admin_donation_recyclerview);
        mDonationsRecyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        mDonationsRecyclerView.setLayoutManager(layoutManager);

        getData();


        fab = view.findViewById(R.id.add_donation_btn);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addNewDonation = new Intent(getContext(), AddPost.class);
                addNewDonation.putExtra("pageTitle", "Add Donation");
                addNewDonation.putExtra("postName", "Donation Title");
                addNewDonation.putExtra("databaseRef", getResources().getString(R.string.donations_ref));
                startActivity(addNewDonation);
            }
        });
        refreshLayout = view.findViewById(R.id.donation_management_swipe_down);
        refreshLayout.setOnRefreshListener(this);

        mCustomProgress.showProgress(getContext(), "Loading Donations..!", true);
    }


    private void getData() {
        mDonationsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mDonationsList.clear();
                if (snapshot.exists()) {
                    for (DataSnapshot childSnap : snapshot.getChildren()) {
                        mDonationsList.add(childSnap.getValue(PostDataModel.class));
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
        postAdapter = new PostsAdapter(getContext(), mDonationsList, false, true);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        mDonationsRecyclerView.setAdapter(postAdapter);
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