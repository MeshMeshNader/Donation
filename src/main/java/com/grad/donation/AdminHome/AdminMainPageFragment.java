package com.grad.donation.AdminHome;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;

import com.grad.donation.AdminHome.Donations.DonationsManagement;
import com.grad.donation.AdminHome.Messages.MessagesManagement;
import com.grad.donation.AdminHome.Requests.RequestsManagement;
import com.grad.donation.Auth.EditProfile;
import com.grad.donation.R;


public class AdminMainPageFragment extends Fragment implements View.OnClickListener {


    View view;

    AdminHome mBasicHome;
    LinearLayout mDonationsBtn, mRequestsBtn, mContactBtn;

    public AdminMainPageFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_admin_main_page, container, false);

        initViews();


        return view;

    }


    private void initViews() {

        mBasicHome = new AdminHome();

        mBasicHome.mProfileBtn.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_person));
        mBasicHome.mProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), EditProfile.class);
                startActivity(intent);
            }
        });

        mDonationsBtn = view.findViewById(R.id.admin_donations_btn);
        mRequestsBtn = view.findViewById(R.id.admin_requests_btn);
        mContactBtn = view.findViewById(R.id.admin_contact_btn);

        mDonationsBtn.setOnClickListener(this);
        mRequestsBtn.setOnClickListener(this);
        mContactBtn.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.admin_donations_btn:

                mBasicHome.loadOutFragment(new DonationsManagement(), "Donation Manage", 1, getActivity());

                break;

            case R.id.admin_requests_btn:

                mBasicHome.loadOutFragment(new RequestsManagement(), "Requests Manage", 2, getActivity());

                break;

            case R.id.admin_contact_btn:

                mBasicHome.loadOutFragment(new MessagesManagement(), "Messages Manage", 3, getActivity());


                break;

        }
    }


}