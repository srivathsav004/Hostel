package com.example.hostel;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class StudentCompA extends AppCompatActivity {

    private EditText titleEditText, complaintEditText;
    private Button submitButton;
    private ProgressDialog progressDialog;

    private DatabaseReference complaintsReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_comp_a);

        titleEditText = findViewById(R.id.titlecompA);
        complaintEditText = findViewById(R.id.complaintA);
        submitButton = findViewById(R.id.submit);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Submitting Complaint...");
        progressDialog.setCancelable(false);

        complaintsReference = FirebaseDatabase.getInstance().getReference("complaintA");

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitComplaint();
            }
        });
    }
    private void submitComplaint() {
        String title = titleEditText.getText().toString().trim();
        String complaint = complaintEditText.getText().toString().trim();

        if (title.isEmpty() || complaint.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }
        progressDialog.show();
        String complaintId = complaintsReference.push().getKey();

        ComplaintA complaintA = new ComplaintA(title, complaint);
        complaintsReference.child(complaintId).setValue(complaintA)
                .addOnCompleteListener(task -> {
                    progressDialog.dismiss();
                    if (task.isSuccessful()) {
                        Toast.makeText(StudentCompA.this, "Complaint submitted successfully", Toast.LENGTH_SHORT).show();
                        clearFields();
                    } else {
                        Toast.makeText(StudentCompA.this, "Failed to submit complaint", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private void clearFields() {
        titleEditText.setText("");
        complaintEditText.setText("");
    }
}
