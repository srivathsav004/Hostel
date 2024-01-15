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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

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
    private AlertDialog alertDialog;

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

        uploadMenuButton.setOnClickListener(v -> opengallery());

        openMenuButton.setOnClickListener(v -> {
            if (!downloadUrl.isEmpty()) {
                try
                {
                    Intent intentUrl = new Intent(Intent.ACTION_VIEW);
                    intentUrl.setDataAndType(Uri.parse(downloadUrl), "application/pdf");
                    intentUrl.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intentUrl);
                }
                catch (ActivityNotFoundException e)
                {

                    Toast.makeText(this, "No PDF Viewer Installed", Toast.LENGTH_LONG).show();
                }
                //dialogMark(downloadUrl);
                // Display PDF in WebView
                //displayPdfInWebView(downloadUrl);
            } else {
                // Check if a file exists in Firebase Storage and display it
                checkAndDisplayExistingFile();
            }
        });
        updateMenuButton.setOnClickListener(v -> {
            if (pdfData != null) {
                pd.setMessage("Updating Menu...");
                pd.show();

                // Generate a unique name for the PDF file in Firebase Storage
                String fileName = "menu.pdf"; // Fixed file name to ensure overwriting
                StorageReference menuRef = storageReference.child("menusA/" + fileName);

                // Upload the PDF file to Firebase Storage, this will overwrite the existing file
                menuRef.putFile(pdfData)
                        .addOnSuccessListener(taskSnapshot -> {
                            // Get the download URL of the uploaded PDF
                            menuRef.getDownloadUrl().addOnSuccessListener(uri -> {
                                // Update the 'downloadUrl' variable with the new URL
                                downloadUrl = uri.toString();

                                // TODO: Update the database with the new download URL
                                // For example, you can update the 'menu' node in your database
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
        });
    }
    public void dialogMark(String url)
    {
        WebView webView;
        Button btnClose;


        Dialog markDialog=new Dialog(this,R.style.dialog);
        markDialog.setContentView(R.layout.custom_dialog_layout);
        webView=markDialog.findViewById(R.id.webView);
        //btnClose=findViewById(R.id.btnClose);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(url);
//        btnClose.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                markDialog.dismiss();
//            }
//        });

        markDialog.show();



    }

    private void displayPdfInWebView(String url) {
        // Inflate the custom layout
        View dialogView = getLayoutInflater().inflate(R.layout.custom_dialog_layout, null);

        // Initialize the WebView
        WebView webView = dialogView.findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        // Set up a WebViewClient to handle errors
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Log.e("WebView Error", "Error: " + description);
            }
        });

        webView.loadUrl(url);  // Load the PDF URL directly

        // Create the AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(AdminFoodMenuA.this);
        builder.setView(dialogView);

        // Set up the Close button
        Button btnClose = dialogView.findViewById(R.id.btnClose);
        btnClose.setOnClickListener(view -> alertDialog.dismiss()); // Close the dialog

        // Show the AlertDialog
        alertDialog = builder.create();
        alertDialog.show();
    }

    private void checkAndDisplayExistingFile() {
        // Add logic to check if a file already exists in Firebase Storage
        // For example, you can check if the 'downloadUrl' is not empty and then call displayPdfInWebView(downloadUrl)

        if (!downloadUrl.isEmpty()) {
            displayPdfInWebView(downloadUrl);
        } else {
            Toast.makeText(AdminFoodMenuA.this, "No existing menu available", Toast.LENGTH_SHORT).show();
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
