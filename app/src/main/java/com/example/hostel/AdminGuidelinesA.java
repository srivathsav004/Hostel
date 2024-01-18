package com.example.hostel;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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



public class AdminGuidelinesA extends AppCompatActivity {

    private final int req = 1;
    private StorageReference storageReference;
    private DatabaseReference reference;
    private Button uploadGuidelinesButton;
    private Button openGuidelinesButton;
    private Button updateGuidelinesButton;
    private ProgressDialog pd;
    private String downloadUrl = "";
    private Uri pdfData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_guidelines_a);

        reference = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference();

        pd = new ProgressDialog(this);
        uploadGuidelinesButton = findViewById(R.id.uploadButton);
        openGuidelinesButton = findViewById(R.id.openButton);
        updateGuidelinesButton = findViewById(R.id.updateButton);

        uploadGuidelinesButton.setOnClickListener(v -> opengallery());

        openGuidelinesButton.setOnClickListener(v -> openLatestUpdatedFile());

        updateGuidelinesButton.setOnClickListener(v -> updateFileInFirebase());
    }

    private void openLatestUpdatedFile() {
        reference.child("guidelines").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String latestDownloadUrl = dataSnapshot.getValue(String.class);
                if (latestDownloadUrl != null && !latestDownloadUrl.isEmpty()) {
                    try {
                        Intent intentUrl = new Intent(Intent.ACTION_VIEW);
                        intentUrl.setDataAndType(Uri.parse(latestDownloadUrl), "application/pdf");
                        intentUrl.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intentUrl);
                    } catch (ActivityNotFoundException e) {
                        Toast.makeText(AdminGuidelinesA.this, "No PDF Viewer Installed", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(AdminGuidelinesA.this, "No PDF file available", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(AdminGuidelinesA.this, "Failed to fetch latest PDF file URL", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateFileInFirebase() {
        if (pdfData != null) {
            pd.setMessage("Updating Guidelines...");
            pd.show();

            String fileName = "guidelines.pdf";  // Set the desired file name here
            StorageReference guidelinesRef = storageReference.child("guidelinesA/" + fileName);

            guidelinesRef.putFile(pdfData)
                    .addOnSuccessListener(taskSnapshot -> {
                        guidelinesRef.getDownloadUrl().addOnSuccessListener(uri -> {
                            downloadUrl = uri.toString();
                            reference.child("guidelines").setValue(downloadUrl);
                            pd.dismiss();
                            Toast.makeText(AdminGuidelinesA.this, "Guidelines updated successfully", Toast.LENGTH_SHORT).show();
                        }).addOnFailureListener(e -> {
                            pd.dismiss();
                            Toast.makeText(AdminGuidelinesA.this, "Failed to get download URL", Toast.LENGTH_SHORT).show();
                        });
                    })
                    .addOnFailureListener(e -> {
                        pd.dismiss();
                        Toast.makeText(AdminGuidelinesA.this, "Failed to update guidelines", Toast.LENGTH_SHORT).show();
                    });
        } else {
            Toast.makeText(AdminGuidelinesA.this, "No file selected for update", Toast.LENGTH_SHORT).show();
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
