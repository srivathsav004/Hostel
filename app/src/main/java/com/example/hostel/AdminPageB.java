package com.example.hostel;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class AdminPageB extends AppCompatActivity {

    ImageView AdminStudentListA, AdminComplaintsA;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_page_a);
        ImageView logoutButton = findViewById(R.id.logoutOptionsButton);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLogoutConfirmation();
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
    }

    private void showLogoutConfirmation() {
        LogoutConfirmationFragmentA fragment = new LogoutConfirmationFragmentA();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(android.R.id.content, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
