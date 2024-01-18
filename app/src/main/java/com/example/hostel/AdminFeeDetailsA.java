package com.example.hostel;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class AdminFeeDetailsA extends AppCompatActivity {

    private LinearLayout receiptsLayout;
    private StorageReference storageReference;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_fee_details_a);

        receiptsLayout = findViewById(R.id.studentsLayout);
        storageReference = FirebaseStorage.getInstance().getReference().child("feesA");
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading Receipts...");
        progressDialog.setCancelable(false);

        loadReceipts();
    }

    private void loadReceipts() {
        progressDialog.show();

        storageReference.listAll().addOnSuccessListener(listResult -> {
            progressDialog.dismiss();

            for (StorageReference item : listResult.getItems()) {
                addReceiptToLayout(item);
            }
        }).addOnFailureListener(exception -> {
            progressDialog.dismiss();
            // Handle any errors
            Toast.makeText(this, "Failed to load receipts", Toast.LENGTH_SHORT).show();
        });
    }

    private void addReceiptToLayout(StorageReference pdfRef) {
        View receiptView = LayoutInflater.from(this).inflate(R.layout.adminfee_a, null);
        Button openReceiptButton = receiptView.findViewById(R.id.openReceiptButton);

        openReceiptButton.setTag(pdfRef);

        openReceiptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StorageReference pdfRef = (StorageReference) v.getTag();
                openPDF(pdfRef);
            }
        });

        receiptsLayout.addView(receiptView);
    }

    private void openPDF(StorageReference pdfRef) {
        progressDialog.setMessage("Opening PDF...");
        progressDialog.show();

        pdfRef.getDownloadUrl().addOnSuccessListener(uri -> {
            progressDialog.dismiss();
            String pdfUrl = uri.toString();
            showPDFInWebView(pdfUrl);
        }).addOnFailureListener(e -> {
            progressDialog.dismiss();
            Toast.makeText(AdminFeeDetailsA.this, "Failed to retrieve PDF URL", Toast.LENGTH_SHORT).show();
        });
    }

    private void showPDFInWebView(String pdfUrl) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse(pdfUrl), "application/pdf");
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(AdminFeeDetailsA.this, "No PDF viewer installed", Toast.LENGTH_SHORT).show();
        }
    }

}
