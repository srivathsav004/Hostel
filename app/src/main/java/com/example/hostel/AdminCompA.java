package com.example.hostel;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.app.ProgressDialog;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class AdminCompA extends AppCompatActivity {

    private LinearLayout complaintsLayout;
    private DatabaseReference complaintsReference;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_comp_a);

        complaintsLayout = findViewById(R.id.noticesLayout);
        complaintsReference = FirebaseDatabase.getInstance().getReference("complaintA");

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading Complaints...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        loadComplaints();
    }

    private void loadComplaints() {
        complaintsReference.orderByKey().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                progressDialog.dismiss();
                complaintsLayout.removeAllViews();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ComplaintA complaint = snapshot.getValue(ComplaintA.class);
                    if (complaint != null) {
                        addComplaintToLayout(complaint);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressDialog.dismiss();
                // Handle the error
                Toast.makeText(AdminCompA.this, "Failed to load complaints", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addComplaintToLayout(ComplaintA complaint) {
        View complaintView = LayoutInflater.from(this).inflate(R.layout.admincomp_a, null);
        TextView complaintTitle = complaintView.findViewById(R.id.complaintTitle);
        TextView complaintSubject = complaintView.findViewById(R.id.complaintSubject);

        complaintTitle.setText("Title: " + complaint.getTitle());
        complaintSubject.setText("Subject: " + complaint.getComplaint());

        LinearLayout.LayoutParams layoutParams =
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, getResources().getDimensionPixelSize(R.dimen.notice_spacing), 0, 0);
        complaintView.setLayoutParams(layoutParams);

        // Set background color for better visibility
        complaintView.setBackgroundColor(ContextCompat.getColor(this, R.color.white));

        complaintsLayout.addView(complaintView);
    }
}
