package com.example.hostel;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

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
    private AlertDialog alertDialog;

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

        openGuidelinesButton.setOnClickListener(v -> {
            if (!downloadUrl.isEmpty()) {
                try {
                    Intent intentUrl = new Intent(Intent.ACTION_VIEW);
                    intentUrl.setDataAndType(Uri.parse(downloadUrl), "application/pdf");
                    intentUrl.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intentUrl);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(this, "No PDF Viewer Installed", Toast.LENGTH_LONG).show();
                }
            } else {
                checkAndDisplayExistingFile();
            }
        });

        updateGuidelinesButton.setOnClickListener(v -> {
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
        });
    }

    private void checkAndDisplayExistingFile() {
        if (!downloadUrl.isEmpty()) {
            displayPdfInWebView(downloadUrl);
        } else {
            Toast.makeText(AdminGuidelinesA.this, "No existing guidelines available", Toast.LENGTH_SHORT).show();
        }
    }

    private void opengallery() {
        Intent intent = new Intent();
        intent.setType("*/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select your file"), req);
    }

    private void displayPdfInWebView(String url) {
        View dialogView = getLayoutInflater().inflate(R.layout.custom_dialog_layout, null);

        WebView webView = dialogView.findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Log.e("WebView Error", "Error: " + description);
            }
        });

        webView.loadUrl(url);

        AlertDialog.Builder builder = new AlertDialog.Builder(AdminGuidelinesA.this);
        builder.setView(dialogView);

        Button btnClose = dialogView.findViewById(R.id.btnClose);
        btnClose.setOnClickListener(view -> alertDialog.dismiss());

        alertDialog = builder.create();
        alertDialog.show();
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
