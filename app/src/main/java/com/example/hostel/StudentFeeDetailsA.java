package com.example.hostel;


import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class StudentFeeDetailsA extends AppCompatActivity {

    private static final int REQUEST_PDF = 1;
    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_fee_details_a);

        storageReference = FirebaseStorage.getInstance().getReference();

        Button uploadButton = findViewById(R.id.upload);
        Button payFeesButton = findViewById(R.id.payfee);

        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        payFeesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPaymentWebsite();
            }
        });
    }

    private void openGallery() {
        Intent intent = new Intent();
        intent.setType("*/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select PDF file"), REQUEST_PDF);
    }

    private void openPaymentWebsite() {
        String paymentUrl = "https://payments.billdesk.com/bdcollect/bd/puducherrytechuniv/7792";
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(paymentUrl));
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "No web browser installed", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_PDF && resultCode == RESULT_OK) {
            Uri pdfUri = data.getData();

            // Ensure that a PDF file was selected
            if (pdfUri != null) {
                uploadPDF(pdfUri);
            } else {
                Toast.makeText(this, "Invalid PDF file", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void uploadPDF(Uri pdfUri) {
        String fileName = "fees_receipt.pdf";
        StorageReference feesRef = storageReference.child("feesA/" + fileName);

        feesRef.putFile(pdfUri)
                .addOnSuccessListener(taskSnapshot -> {
                    Toast.makeText(StudentFeeDetailsA.this, "PDF uploaded successfully", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(StudentFeeDetailsA.this, "Failed to upload PDF", Toast.LENGTH_SHORT).show();
                });
    }
}
