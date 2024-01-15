package com.example.hostel;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class StudentPageA extends AppCompatActivity {

    private String downloadUrl = "";
    private ImageView StudentFoodMenuA;
    private ImageView StudentGuidelinesA;
    private ImageView StudentFeeDetailsA;
    private StorageReference storageReference;

    ImageView logoutOptionsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_page_a);


        logoutOptionsButton = findViewById(R.id.logoutOptionsButton);

        logoutOptionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLogoutConfirmation();
            }
        });

        storageReference = FirebaseStorage.getInstance().getReference();
        StudentFeeDetailsA=findViewById(R.id.StudentFeeDetailsA);
        StudentFeeDetailsA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(StudentPageA.this, com.example.hostel.StudentFeeDetailsA.class);
                startActivity(intent);
            }
        });
        StudentGuidelinesA = findViewById(R.id.StudentGuidelinesA);
        StudentGuidelinesA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!downloadUrl.isEmpty()) {
                    try {
                        // Customize the displayed name for guidelines.pdf
                        String customName = "Guidelines.pdf";

                        // Open the PDF using an external PDF viewer
                        Intent intentUrl = new Intent(Intent.ACTION_VIEW);
                        intentUrl.setDataAndType(Uri.parse(downloadUrl), "application/pdf");
                        intentUrl.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                        // Set a custom name for the PDF
                        intentUrl.putExtra(Intent.EXTRA_TITLE, customName);

                        startActivity(intentUrl);
                    } catch (ActivityNotFoundException e) {
                        Toast.makeText(StudentPageA.this, "No PDF Viewer Installed", Toast.LENGTH_LONG).show();
                    }
                } else {
                    // Handle case where no guidelines are available
                    Toast.makeText(StudentPageA.this, "No guidelines available", Toast.LENGTH_SHORT).show();
                }
            }
        });

        StudentFoodMenuA=findViewById(R.id.StudentFoodMenuA);
        StudentFoodMenuA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!downloadUrl.isEmpty()) {
                    try {
                        // Customize the displayed name for menu.pdf
                        String customName = "FoodMenu.pdf";

                        // Open the PDF using an external PDF viewer
                        Intent intentUrl = new Intent(Intent.ACTION_VIEW);
                        intentUrl.setDataAndType(Uri.parse(downloadUrl), "application/pdf");
                        intentUrl.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                        // Set a custom name for the PDF
                        intentUrl.putExtra(Intent.EXTRA_TITLE, customName);

                        startActivity(intentUrl);
                    } catch (ActivityNotFoundException e) {
                        Toast.makeText(StudentPageA.this, "No PDF Viewer Installed", Toast.LENGTH_LONG).show();
                    }
                } else {
                    // Handle case where no menu is available
                    Toast.makeText(StudentPageA.this, "No menu available", Toast.LENGTH_SHORT).show();
                }
            }
        });

        fetchMenuDownloadUrl();
        fetchGuidelinesDownloadUrl();
    }

    private void fetchGuidelinesDownloadUrl() {
        // Replace "menu.pdf" with the actual file name used in AdminFoodMenuA
        StorageReference menuRef = storageReference.child("guidelinesA/guidelines.pdf");

        menuRef.getDownloadUrl().addOnSuccessListener(uri -> {
            downloadUrl = uri.toString();
        }).addOnFailureListener(e -> {
            // Handle failure to get download URL
            Toast.makeText(StudentPageA.this, "Failed to get menu download URL", Toast.LENGTH_SHORT).show();
        });
    }


    private void fetchMenuDownloadUrl() {
        // Replace "menu.pdf" with the actual file name used in AdminFoodMenuA
        StorageReference menuRef = storageReference.child("menusA/menu.pdf");

        menuRef.getDownloadUrl().addOnSuccessListener(uri -> {
            downloadUrl = uri.toString();
        }).addOnFailureListener(e -> {
            // Handle failure to get download URL
            Toast.makeText(StudentPageA.this, "Failed to get menu download URL", Toast.LENGTH_SHORT).show();
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
