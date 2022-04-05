package com.grad.donation.UserHome;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.grad.donation.Adapters.PostsAdapter;
import com.grad.donation.Auth.EditProfile;
import com.grad.donation.Auth.Login;
import com.grad.donation.DataModels.PostDataModel;
import com.grad.donation.DataModels.UserDataModel;
import com.grad.donation.R;
import com.grad.donation.Utils.CustomProgress;

import java.util.ArrayList;
import java.util.Collections;


public class UserHome extends AppCompatActivity {

    FirebaseAuth mAuth;

    DrawerLayout mDrawer;
    ImageView mMenuDrawerBtn;
    TextView mUserName, mMyProfile, mDonations, mContactUs, mLogOut;
    String fullName;
    DatabaseReference User;
    RecyclerView mPostsRecyclerView;
    LinearLayoutManager layoutManager;
    PostsAdapter postAdapter;
    ArrayList<PostDataModel> mPostsList;
    TextView mNoResults;
    CustomProgress mCustomProgress = CustomProgress.getInstance();
    private DatabaseReference mDonationsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);

        initViews();
    }

    private void initViews() {

        mAuth = FirebaseAuth.getInstance();
        mCustomProgress = CustomProgress.getInstance();

        User = FirebaseDatabase.getInstance().getReference("Users");

        mMenuDrawerBtn = findViewById(R.id.menu_drawer_btn);
        mMenuDrawerBtn.setOnClickListener(v -> openMenu());
        mDrawer = findViewById(R.id.drawer_layout);

        mUserName = findViewById(R.id.user_name_nav);

        mMyProfile = findViewById(R.id.my_profile_nav);
        mDonations = findViewById(R.id.donations_nav);
        mContactUs = findViewById(R.id.contact_us_nav);
        mLogOut = findViewById(R.id.log_out_nav);

        mMyProfile.setOnClickListener(v -> openEditAccount());
        mDonations.setOnClickListener(v -> openDonations());
        mContactUs.setOnClickListener(v -> openContactUs());
        mLogOut.setOnClickListener(v -> logoutBtn());

        mNoResults = findViewById(R.id.home_no_result);


        mPostsList = new ArrayList<>();

        mDonationsRef = FirebaseDatabase.getInstance().getReference(getResources().getString(R.string.donations_ref));
        mPostsRecyclerView = findViewById(R.id.posts_recycler_view);
        mPostsRecyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(UserHome.this, RecyclerView.VERTICAL, false);
        mPostsRecyclerView.setLayoutManager(layoutManager);

        getData();

    }


    private void openMenu() {
        mDrawer.openDrawer(Gravity.LEFT);
    }

    private void openEditAccount() {
        Intent edit = new Intent(UserHome.this, EditProfile.class);
        startActivity(edit);
    }

    private void openDonations() {
        Intent home = new Intent(UserHome.this, UserHome.class);
        startActivity(home);
        finish();
    }


    private void openContactUs() {
        Intent contact = new Intent(UserHome.this, Contact.class);
        startActivity(contact);
    }

    private void logoutBtn() {
        new AlertDialog.Builder(this)
                .setMessage("Do you want to logout from \"Donations\"")
                .setCancelable(false)
                .setPositiveButton("Yes, Log me out!", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        logout();
                    }
                })
                .setNegativeButton("No, Stay here!", null)
                .show();
    }

    private void logout() {
        mAuth.signOut();
        Intent userLogout = new Intent(UserHome.this, Login.class);
        userLogout.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        userLogout.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(userLogout);
        this.finish();

    }


    public void onBackPressed() {
        if (true) {

            new AlertDialog.Builder(this)
                    .setMessage("Exit Application?")
                    .setCancelable(false)
                    .setPositiveButton("Yes, Exit", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            UserHome.super.onBackPressed();
                        }
                    })
                    .setNegativeButton("No, Stay here", null)
                    .show();
        }

    }


    private void getData() {
        mCustomProgress.showProgress(this, "Loading", true);
        mPostsList.clear();

        User.child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    UserDataModel userData = snapshot.getValue(UserDataModel.class);
                    fullName = userData.getName();
                    mUserName.setText("Welcome " + fullName);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        mDonationsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mPostsList.clear();
                if (snapshot.exists()) {
                    for (DataSnapshot childSnap : snapshot.getChildren()) {
                        mPostsList.add(childSnap.getValue(PostDataModel.class));
                        Log.e("TAG", "onDataChange: " + "111111111111");
                    }
                    if (mNoResults.getVisibility() == View.VISIBLE)
                        mNoResults.setVisibility(View.GONE);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            showPosts();
                        }
                    }, 300);
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


    public void showPosts() {
        Collections.shuffle(mPostsList);
        postAdapter = new PostsAdapter(UserHome.this, mPostsList, true, false);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        mPostsRecyclerView.setAdapter(postAdapter);
        mCustomProgress.hideProgress();
    }


}