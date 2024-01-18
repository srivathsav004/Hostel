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

public class AdminPageC extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_page_c);

        ImageView logoutOptionsButton = findViewById(R.id.logoutOptionsButton);

        logoutOptionsButton.setOnClickListener(new View.OnClickListener() {
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

        // Show the dialog
        builder.create().show();
    }
    private void showLogoutConfirmation() {
        LogoutConfirmationFragmentA fragment = new LogoutConfirmationFragmentA();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(android.R.id.content, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

}
