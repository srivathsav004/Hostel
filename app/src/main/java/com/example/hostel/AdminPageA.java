package com.example.hostel;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class AdminPageA extends AppCompatActivity {

    ImageView AdminStudentListA,AdminComplaintsA;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_page_a);
        ImageView logoutButton = findViewById(R.id.logoutOptionsButton);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLogoutOptionsFragment();
            }
        });
        TextView visitWebsiteTextView = findViewById(R.id.VisitWebsite);
        visitWebsiteTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open the website when the TextView is clicked
                String websiteUrl = "https://www.ptuniv.edu.in/";
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(websiteUrl));
                startActivity(intent);
            }
        });
        ImageView AdminFoodMenuA=findViewById(R.id.AdminFoodMenuA);
        AdminFoodMenuA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AdminPageA.this,AdminFoodMenuA.class);
                startActivity(intent);
            }
        });
        ImageView AdminGuidelinesA=findViewById(R.id.AdminGuidelinesA);
        AdminGuidelinesA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AdminPageA.this,AdminGuidelinesA.class);
                startActivity(intent);
            }
        });
        ImageView AdminFeeDetailsA=findViewById(R.id.AdminFeeDetailsA);
        AdminFeeDetailsA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AdminPageA.this,AdminFeeDetailsA.class);
                startActivity(intent);
            }
        });
        ImageView AdminNoticeA=findViewById(R.id.AdminNoticeA);
        AdminNoticeA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AdminPageA.this,NoticeUploadA.class);
                startActivity(intent);
            }
        });
        AdminStudentListA=findViewById(R.id.AdminStudentListA);
        AdminStudentListA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AdminPageA.this,StudentDataA.class);
                startActivity(intent);
            }
        });
        AdminComplaintsA=findViewById(R.id.AdminComplaintsA);
        AdminComplaintsA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AdminPageA.this,AdminCompA.class);
                startActivity(intent);
            }
        });
    }
    @Override
    public void onBackPressed() {
        showExitConfirmationDialog();
        super.onBackPressed();
    }

    private void showExitConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Do you want to leave?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // User clicked "Yes", finish the activity
                finish();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // User clicked "No", dismiss the dialog
                dialogInterface.dismiss();
            }
        });

        builder.create().show();
    }

    private void showLogoutOptionsFragment() {
        LogoutOptionsFragmentA fragment = new LogoutOptionsFragmentA();

        getSupportFragmentManager().beginTransaction()
                .replace(android.R.id.content, fragment, fragment.getClass().getSimpleName())
                .addToBackStack(null)
                .commit();
    }

}