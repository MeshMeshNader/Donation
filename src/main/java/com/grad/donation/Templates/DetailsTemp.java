package com.grad.donation.Templates;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.grad.donation.AdminHome.Post.SliderAdapterExample;
import com.grad.donation.DataModels.PostDataModel;
import com.grad.donation.DataModels.RequestDataModel;
import com.grad.donation.DataModels.UserDataModel;
import com.grad.donation.R;
import com.grad.donation.Utils.CustomProgress;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;


public class DetailsTemp extends AppCompatActivity {

    PostDataModel postDataModel;
    TextView mPostName, mPostDescription, mPostValue, mAlreadyRequested;
    String postKey, databaseRefName, postName, userType;
    //Slider
    Button mRequestBtn;
    ArrayList<String> mList = new ArrayList<>();
    SliderView sliderView;
    SliderAdapterExample adapter;
    CustomProgress mCustomProgress = CustomProgress.getInstance();
    FirebaseAuth mAuth;
    DatabaseReference UsersRef;
    String currentUserID;
    RequestDataModel requestDataModel;
    DatabaseReference mRequestsRef;
    String requestKey = "";
    String userName = "";
    String postLabel = "";
    ArrayList<RequestDataModel> mRequestsList = new ArrayList<>();
    private DatabaseReference mPostRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_temp);
        initViews();

    }


    private void initViews() {


        Intent i = getIntent();

        postKey = i.getStringExtra("postKey");
        databaseRefName = getResources().getString(R.string.donations_ref);
        userType = i.getStringExtra("userType");


        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        UsersRef = FirebaseDatabase.getInstance().getReference("Users").child(currentUserID);

        requestDataModel = new RequestDataModel();
        mRequestsRef = FirebaseDatabase.getInstance().getReference("AllRequests");
        requestKey = mRequestsRef.push().getKey();

        postDataModel = new PostDataModel();
        mPostRef = FirebaseDatabase.getInstance().getReference(databaseRefName).child(postKey);
        mList = new ArrayList<>();

        mPostName = findViewById(R.id.post_detail_name);
        mPostDescription = findViewById(R.id.post_detail_description);
        mPostValue = findViewById(R.id.post_value);
        mAlreadyRequested = findViewById(R.id.already_requested);

        mRequestBtn = findViewById(R.id.post_detail_request_btn);

        mList.add("https://gfsstore.com/wp-content/themes/gfsstore.com/images/no_image_available.png");
        sliderView = findViewById(R.id.imageSlider);
        adapter = new SliderAdapterExample(this, mList);
        sliderView.setSliderAdapter(adapter);

        sliderView.setIndicatorAnimation(IndicatorAnimations.WORM); //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
        sliderView.setIndicatorSelectedColor(Color.WHITE);
        sliderView.setIndicatorUnselectedColor(Color.GRAY);
        sliderView.setScrollTimeInSec(4); //set scroll delay in seconds :
        sliderView.startAutoCycle();


        getData();


        mRequestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestDonation();
            }
        });


        if (userType.equals("admin"))
            mRequestBtn.setVisibility(View.GONE);
    }

    private void RequestDonation() {
        mCustomProgress.showProgress(DetailsTemp.this, "Loading...!", true);

        requestDataModel = new RequestDataModel(requestKey, currentUserID
                , userName, postKey, postLabel, getResources().getString(R.string.wait));

        mRequestsRef.child(requestKey).setValue(requestDataModel).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    mCustomProgress.hideProgress();
                    Log.e("Details", "Done ");
                    Toast.makeText(DetailsTemp.this, "Requested Successfully", Toast.LENGTH_SHORT).show();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            finish();
                        }
                    }, 200);
                } else {
                    String message = task.getException().getMessage();
                    Toast.makeText(DetailsTemp.this, "Error Occurred" + message, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private void getData() {


        UsersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    userName = snapshot.getValue(UserDataModel.class).getName();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        mPostRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {
                    postDataModel = snapshot.getValue(PostDataModel.class);
                    postName = postDataModel.getPostLabel();
                    postLabel = postDataModel.getPostLabel();
                    mPostName.setText(postName);
                    mPostValue.setText(postDataModel.getPostDonationValue() + " $");
                    mPostDescription.setText(postDataModel.getPostDescription());


                    if (snapshot.child("postImageURL").exists()) {
                        mList.clear();
                        mList = postDataModel.getPostImageURL();
                        try {

                            adapter = new SliderAdapterExample(DetailsTemp.this, mList);
                            sliderView.setSliderAdapter(adapter);

                        } catch (Exception e) {
                            mList.add("https://gfsstore.com/wp-content/themes/gfsstore.com/images/no_image_available.png");
                            adapter = new SliderAdapterExample(DetailsTemp.this, mList);
                            sliderView.setSliderAdapter(adapter);
                        }
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        mRequestsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mRequestsList.clear();
                if (snapshot.exists()) {
                    for (DataSnapshot childSnap : snapshot.getChildren()) {
                        mRequestsList.add(childSnap.getValue(RequestDataModel.class));
                    }
                    for (RequestDataModel req : mRequestsList) {
                        if (req.getPostKey().equals(postKey) && req.getUserID().equals(currentUserID)) {
                            mRequestBtn.setVisibility(View.GONE);
                            mAlreadyRequested.setVisibility(View.VISIBLE);
                        } else {
                            mRequestBtn.setVisibility(View.VISIBLE);
                            mAlreadyRequested.setVisibility(View.GONE);
                        }

                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

}