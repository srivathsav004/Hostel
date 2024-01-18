package com.example.hostel;

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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class StudentFeeDetailsA extends AppCompatActivity {
    private static final int PICK_PDF_REQUEST = 1;
    private Button uploadButton;
    private Button payFeeButton;
    private ProgressDialog progressDialog;
    private Uri pdfUri;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_fee_details_a);
        uploadButton = findViewById(R.id.upload);
        payFeeButton = findViewById(R.id.payfee);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Uploading...");
        databaseReference = FirebaseDatabase.getInstance().getReference("studentA").child("your_student_id");
        storageReference = FirebaseStorage.getInstance().getReference().child("feesA");
        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Open file picker to choose a PDF
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("application/pdf");
                startActivityForResult(intent, PICK_PDF_REQUEST);
            }
        });
        payFeeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Redirect to payment link
                Uri paymentUri = Uri.parse("https://payments.billdesk.com/bdcollect/bd/puducherrytechuniv/7792");
                Intent intent = new Intent(Intent.ACTION_VIEW, paymentUri);
                try {
                    startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(StudentFeeDetailsA.this, "Unable to open payment link", Toast.LENGTH_SHORT).show();
                }
            }
        });
        databaseReference.child("name").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String studentName = dataSnapshot.getValue(String.class);
                    uploadButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // Open file picker to choose a PDF
                            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                            intent.setType("application/pdf");
                            startActivityForResult(intent, PICK_PDF_REQUEST);
                        }
                    });
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle the error
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_PDF_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            pdfUri = data.getData();
            uploadPDF();
        }
    }
    private void uploadPDF() {
        if (pdfUri != null) {
            progressDialog.show();
            String pdfName = "fee_receipt_" + System.currentTimeMillis() + ".pdf";
            StorageReference pdfRef = storageReference.child(pdfName);
            pdfRef.putFile(pdfUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(StudentFeeDetailsA.this, "PDF uploaded successfully", Toast.LENGTH_SHORT).show();
                            pdfRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri downloadUri) {
                                    savePDFDetailsToDatabase(downloadUri.toString());
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(StudentFeeDetailsA.this, "Failed to upload PDF", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
    private void savePDFDetailsToDatabase(String pdfUrl) {
        // Use the student name retrieved earlier
        // Save PDF details in real-time database
        databaseReference.child("fee_receipts").push().setValue(pdfUrl);
    }
}
