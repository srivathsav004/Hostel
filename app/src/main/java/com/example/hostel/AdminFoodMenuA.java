package com.example.hostel;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.List;


public class AdminFoodMenuA extends AppCompatActivity {

    private final int req = 1;
    private StorageReference storageReference;
    private DatabaseReference reference;
    private Button uploadMenuButton;
    private Button openMenuButton;
    private Button updateMenuButton;
    private ProgressDialog pd;
    private String downloadUrl = "";
    private Uri pdfData;
    private Button downloadMenuButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_food_menu_a);

        reference = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference();

        pd = new ProgressDialog(this);
        uploadMenuButton = findViewById(R.id.uploadMenuButton);
        openMenuButton = findViewById(R.id.openMenuButton);
        updateMenuButton = findViewById(R.id.updateMenuButton);
        // Add this code where you handle the click event for the download button
        Button downloadMenuButton = findViewById(R.id.downloadMenuButton);
        downloadMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Fetch the latest download URL from Firebase
                reference.child("menu").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String latestDownloadUrl = dataSnapshot.getValue(String.class);
                        if (latestDownloadUrl != null && !latestDownloadUrl.isEmpty()) {
                            // Start the DownloadService to download the file
                            Intent downloadIntent = new Intent(AdminFoodMenuA.this, DownloadService.class);
                            downloadIntent.putExtra(DownloadService.DOWNLOAD_URL_EXTRA, latestDownloadUrl);
                            startService(downloadIntent);

                            // Optionally, you can notify the user that the download has started
                            Toast.makeText(AdminFoodMenuA.this, "Downloading...", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(AdminFoodMenuA.this, "No file available for download", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(AdminFoodMenuA.this, "Failed to fetch download URL", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        uploadMenuButton.setOnClickListener(v -> opengallery());

        openMenuButton.setOnClickListener(v -> openLatestUpdatedFile());

        updateMenuButton.setOnClickListener(v -> updateFileInFirebase());
    }

    private void openLatestUpdatedFile() {
        reference.child("menu").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String latestDownloadUrl = dataSnapshot.getValue(String.class);
                if (latestDownloadUrl != null && !latestDownloadUrl.isEmpty()) {
                    // Open the latest updated PDF file
                    try {
                        Intent intentUrl = new Intent(Intent.ACTION_VIEW);
                        intentUrl.setDataAndType(Uri.parse(latestDownloadUrl), "application/pdf");
                        intentUrl.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intentUrl);
                    } catch (ActivityNotFoundException e) {
                        Toast.makeText(AdminFoodMenuA.this, "No PDF Viewer Installed", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(AdminFoodMenuA.this, "No PDF file available", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(AdminFoodMenuA.this, "Failed to fetch latest PDF file URL", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateFileInFirebase() {
        if (pdfData != null) {
            pd.setMessage("Updating Menu...");
            pd.show();
            String fileName = "menu.pdf";
            StorageReference menuRef = storageReference.child("menusA/" + fileName);
            menuRef.putFile(pdfData)
                    .addOnProgressListener(taskSnapshot -> {
                        double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                        pd.setMessage("Updating Menu... " + (int) progress + "%");
                    })
                    .addOnSuccessListener(taskSnapshot -> {
                        menuRef.getDownloadUrl().addOnSuccessListener(uri -> {
                            downloadUrl = uri.toString();
                            reference.child("menu").setValue(downloadUrl);

                            pd.dismiss();
                            Toast.makeText(AdminFoodMenuA.this, "Menu updated successfully", Toast.LENGTH_SHORT).show();
                        }).addOnFailureListener(e -> {
                            pd.dismiss();
                            Toast.makeText(AdminFoodMenuA.this, "Failed to get download URL", Toast.LENGTH_SHORT).show();
                        });
                    })
                    .addOnFailureListener(e -> {
                        pd.dismiss();
                        Toast.makeText(AdminFoodMenuA.this, "Failed to update menu", Toast.LENGTH_SHORT).show();
                    });
        } else {
            Toast.makeText(AdminFoodMenuA.this, "No file selected for update", Toast.LENGTH_SHORT).show();
        }
    }
    private void opengallery() {
        Intent intent = new Intent();
        intent.setType("*/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select your file"), req);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == req && resultCode == RESULT_OK) {
            pdfData = data.getData();
            Toast.makeText(this, "" + pdfData, Toast.LENGTH_SHORT).show();
        }
    }
}
