package com.grad.donation.AdminHome;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.auth.FirebaseAuth;
import com.grad.donation.R;

public class AdminHome extends AppCompatActivity {


    public static int navItemIndex = 0;
    public static ImageView mProfileBtn;
    private static TextView mPageTitleTv;
    final String TAG = "AdminHome";
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);


        initViews();
        loadFragment(new AdminMainPageFragment(), "Donations Manager", 0);

    }

    private void initViews() {

        mAuth = FirebaseAuth.getInstance();
        mPageTitleTv = findViewById(R.id.admin_page_title);
        mProfileBtn = findViewById(R.id.admin_profile_btn);

    }


    public void onBackPressed() {
        if (true) {
            if (navItemIndex != 0) {
                navItemIndex = 0;
                loadFragment(new AdminMainPageFragment(), "Donations Manager", 0);
                return;
            } else {
                new AlertDialog.Builder(this)
                        .setMessage("Exit Application?")
                        .setCancelable(false)
                        .setPositiveButton("Yes, Exit", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                AdminHome.super.onBackPressed();
                            }
                        })
                        .setNegativeButton("No, Stay here", null)
                        .show();
            }
        }
    }

    public void loadOutFragment(Fragment fragment, String pageTitle, int index, Context context) {
        FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.fade_in, 0, 0, 0);
        transaction.replace(R.id.admin_page_container, fragment).commit();
        mPageTitleTv.setText(pageTitle);
        navItemIndex = index;
    }

    void loadFragment(Fragment fragment, String pageTitle, int index) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.fade_in, 0, 0, 0);
        transaction.replace(R.id.admin_page_container, fragment).commit();
        mPageTitleTv.setText(pageTitle);
        navItemIndex = index;

    }


}