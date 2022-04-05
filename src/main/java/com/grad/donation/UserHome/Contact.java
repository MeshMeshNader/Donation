package com.grad.donation.UserHome;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.grad.donation.DataModels.MessageDataModel;
import com.grad.donation.DataModels.UserDataModel;
import com.grad.donation.R;
import com.grad.donation.Utils.CustomProgress;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Contact extends AppCompatActivity {


    CustomProgress mCustomProgress = CustomProgress.getInstance();
    SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");

    EditText mContactEt;
    Button mSendBtn;

    FirebaseAuth mAuth;
    DatabaseReference UsersRef;
    String currentUserID;
    MessageDataModel messageDataModel;
    DatabaseReference mMessagesRef;
    String messageKey = "";
    String userName = "";
    String userEmail = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        initViews();


    }

    private void initViews() {

        mContactEt = findViewById(R.id.contact_edit_text);
        mSendBtn = findViewById(R.id.contact_send_btn);

        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        UsersRef = FirebaseDatabase.getInstance().getReference("Users").child(currentUserID);

        messageDataModel = new MessageDataModel();
        mMessagesRef = FirebaseDatabase.getInstance().getReference("AllMessages");
        messageKey = mMessagesRef.push().getKey();

        getUserData();
        mSendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mContactEt.getText().toString().isEmpty())
                    Toast.makeText(Contact.this, "Enter your message", Toast.LENGTH_LONG).show();
                else {
                    SendMessage();
                }
            }
        });


    }

    private void getUserData() {

        UsersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    userName = snapshot.getValue(UserDataModel.class).getName();
                    userEmail = snapshot.getValue(UserDataModel.class).getEmail();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void SendMessage() {
        mCustomProgress.showProgress(Contact.this, "Loading...!", true);

        messageDataModel = new MessageDataModel(messageKey, mContactEt.getText().toString(), format.format(new Date()), userName, userEmail);


        mMessagesRef.child(messageKey).setValue(messageDataModel).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    mCustomProgress.hideProgress();
                    Log.e("Details", "Done ");
                    Toast.makeText(Contact.this, "Message Sent Successfully", Toast.LENGTH_SHORT).show();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            finish();
                        }
                    }, 200);
                } else {
                    String message = task.getException().getMessage();
                    Toast.makeText(Contact.this, "Error Occurred" + message, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


}