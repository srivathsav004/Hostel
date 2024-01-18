package com.example.hostel;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private RadioGroup radioGroupRoles;
    private Button buttonNext;
    TextView visitWebsiteTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        radioGroupRoles = findViewById(R.id.radioGroupRoles);
        buttonNext = findViewById(R.id.buttonNext);
         visitWebsiteTextView = findViewById(R.id.VisitWebsite);

        visitWebsiteTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String websiteUrl = "https://www.ptuniv.edu.in/";
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(websiteUrl));
                startActivity(intent);
            }
        });

        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int selectedId = radioGroupRoles.getCheckedRadioButtonId();
                RadioButton radioButton = findViewById(selectedId);

                if (radioButton != null) {
                    String selectedRole = radioButton.getText().toString();

                    // Start the appropriate login activity based on the selected role
                    if (selectedRole.equals("Student")) {
                        Intent studentIntent = new Intent(MainActivity.this, StudentLoginActivity.class);
                        startActivity(studentIntent);
                    } else if (selectedRole.equals("Admin")) {
                        Intent adminIntent = new Intent(MainActivity.this, AdminLoginActivity.class);
                        startActivity(adminIntent);
                    }
                }
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
}
