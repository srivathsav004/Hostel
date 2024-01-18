package com.example.hostel;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class StudentPageB extends AppCompatActivity {
    private String downloadUrl = "";
    private ImageView StudentFoodMenuA;
    private ImageView StudentGuidelinesA;
    private ImageView StudentFeeDetailsA,StudentNoticeA,StudentComplaintsA;
    private StorageReference storageReference;
    ImageView logoutOptionsButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_page_b);
        logoutOptionsButton = findViewById(R.id.logoutOptionsButton);
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
        storageReference = FirebaseStorage.getInstance().getReference();
//        StudentFeeDetailsA=findViewById(R.id.StudentFeeDetailsA);
//        StudentFeeDetailsA.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent=new Intent(StudentPageB.this, com.example.hostel.StudentFeeDetailsB.class);
//                startActivity(intent);
//            }
//        });
//        StudentGuidelinesA = findViewById(R.id.StudentGuidelinesA);
//        StudentFoodMenuA=findViewById(R.id.StudentFoodMenuA);
//        StudentGuidelinesA.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                openLatestUpdatedFile("guidelinesB/guidelines.pdf", "Guidelines.pdf");
//            }
//        });
//        StudentFoodMenuA.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                openLatestUpdatedFile("menusB/menu.pdf", "FoodMenu.pdf");
//            }
//        });
//        StudentNoticeA=findViewById(R.id.StudentNoticeA);
//        StudentNoticeA.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent=new Intent(StudentPageB.this,StudentNoticeB.class);
//                startActivity(intent);
//            }
//        });
//        StudentComplaintsA=findViewById(R.id.StudentComplaintsA);
//        StudentComplaintsA.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent=new Intent(StudentPageB.this,StudentCompB.class);
//                startActivity(intent);
//            }
//        });
        fetchMenuDownloadUrl();
        fetchGuidelinesDownloadUrl();
    }
    private void openLatestUpdatedFile(String storagePath, String customName) {
        StorageReference fileRef = storageReference.child(storagePath);
        fileRef.getDownloadUrl().addOnSuccessListener(uri -> {
            String downloadUrl = uri.toString();
            if (!downloadUrl.isEmpty()) {
                try {
                    Intent intentUrl = new Intent(Intent.ACTION_VIEW);
                    intentUrl.setDataAndType(Uri.parse(downloadUrl), "application/pdf");
                    intentUrl.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intentUrl.putExtra(Intent.EXTRA_TITLE, customName);
                    startActivity(intentUrl);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(StudentPageB.this, "No PDF Viewer Installed", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(StudentPageB.this, "No PDF available", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(e -> {
            Toast.makeText(StudentPageB.this, "Failed to get download URL", Toast.LENGTH_SHORT).show();
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
                finish();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.create().show();
    }
    private void fetchGuidelinesDownloadUrl() {
        StorageReference menuRef = storageReference.child("guidelinesB/guidelines.pdf");

        menuRef.getDownloadUrl().addOnSuccessListener(uri -> {
            downloadUrl = uri.toString();
        }).addOnFailureListener(e -> {
            Toast.makeText(StudentPageB.this, "Failed to get menu download URL", Toast.LENGTH_SHORT).show();
        });
    }
    private void fetchMenuDownloadUrl() {
        StorageReference menuRef = storageReference.child("menusB/menu.pdf");

        menuRef.getDownloadUrl().addOnSuccessListener(uri -> {
            downloadUrl = uri.toString();
        }).addOnFailureListener(e -> {
            Toast.makeText(StudentPageB.this, "Failed to get menu download URL", Toast.LENGTH_SHORT).show();
        });
    }

    private void showLogoutConfirmation() {
        StudentLogoutFragment fragment = new StudentLogoutFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(android.R.id.content, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
